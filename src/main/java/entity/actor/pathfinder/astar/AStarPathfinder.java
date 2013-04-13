package entity.actor.pathfinder.astar;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import control.Game;
import entity.actor.pathfinder.IPathfinder;
import gui.GameWindow;

/**
 * Implements the A* algorithm to find the most efficient path.
 * @author Milo Carbol
 * @since 12 April 2013
 */
public class AStarPathfinder implements IPathfinder {

	/** The game to interact with **/
	private Game game;

	private Point currentLocation;
	private Point targetLocation;
	
	public AStarPathfinder (Game game, Point currentLocation) {
		this.game = game;
		this.currentLocation = new Point(currentLocation);
		this.targetLocation = new Point(currentLocation);
	}
	
	/**
	 * @return The next grid space in the actor's path.
	 */
	public Point getNextLocation() {
		if (currentLocation.equals(targetLocation))
			return new Point(currentLocation);
		else {
			Set<AStarNode> openNodes = new HashSet<AStarNode>();
			Set<AStarNode> closedNodes = new HashSet<AStarNode>();
			
			AStarNode current = new AStarNode(currentLocation, targetLocation);
			openNodes.add(current);
			
			boolean done = false;
			while (!done) {
				if (openNodes.size() == 0)
					return new Point(currentLocation);
				
				AStarNode best = openNodes.toArray(new AStarNode[0])[0];
				for (AStarNode node : openNodes)
					if (node.getF() < best.getF())
						best = node;
				current = best;
				
				openNodes.remove(current);
				closedNodes.add(current);
				
				if (current.getGridSpace().equals(targetLocation)) {
					done = true;
				}
				
				Set<AStarNode> neighbours = new HashSet<AStarNode>();
				for (int x = -1; x <= 1; x++)
					for (int y = -1; y <= 1; y++)
						if (!(x == 0 && y == 0) &&
							current.getGridSpace().x + x >= 0 && current.getGridSpace().x < GameWindow.GRID_COLUMNS &&
							current.getGridSpace().y + y >= 0 && current.getGridSpace().y < GameWindow.GRID_ROWS)
							neighbours.add(new AStarNode(new Point(current.getGridSpace().x + x, current.getGridSpace().y + y), targetLocation));
				
				for (AStarNode neighbour : neighbours) {
					if (closedNodes.contains(neighbour) ||
						game.spaceHasFeature(neighbour.getGridSpace()) ||
						(current.getGridSpace().equals(currentLocation) && game.spaceHasActor(neighbour.getGridSpace()))) {
						// Do nothing
					}
					else if (!openNodes.contains(neighbour)) {
						openNodes.add(neighbour);
						neighbour.updatePath(current, 1);
					}
					else if (openNodes.contains(neighbour) && current.getG() < neighbour.getG()) {
						neighbour.updatePath(current, 1);
					}
				}
			}
			
			AStarNode nextNode = current;
			while (!nextNode.getParent().getGridSpace().equals(currentLocation))
				nextNode = nextNode.getParent();
			
			currentLocation.x = nextNode.getGridSpace().x;
			currentLocation.y = nextNode.getGridSpace().y;
			return nextNode.getGridSpace();
		}
		
	}
	
	/**
	 * Sets the path's target.
	 * @param targetLocation - The new destination
	 **/
	public void setTargetLocation(Point targetLocation) {
		this.targetLocation.x = targetLocation.x;
		this.targetLocation.y = targetLocation.y;
	}
}
