package generator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Navigates a node map of wall locations and creates random entrances into closed loops.
 * @author Milo Carbol
 * @since 14 April 2013
 */
public class WallCleaner {

	/** Number of wall pieces to remove when creating an entrance **/
	private final int 	minimumSizeOfEntrance = 2,
						maximumSizeOfEntrance = 10;
	
	private final int	minimumWallsToWait = 0,
						maximumWallsToWait = 50;
	
	private final int	minimumEntrancesToMake = 1,
						maximumEntrancesToMake = 4;
	
	/** The node map **/
	private List<WallCleanerNode> allNodes;
	
	/** The wall locations **/
	private List<Point> walls;
	
	/**
	 * Creates a new wall cleaner.
	 * @param walls - The wall locations. Not modified by cleaning.
	 */
	public WallCleaner(List<Point> walls) {
		this.walls = walls;
		this.allNodes = new ArrayList<WallCleanerNode>();
	}
	
	/**
	 * Navigates the node map and creates entrances into closed loops.
	 * @return The updated list of wall locations
	 */
	public List<Point> cleanWalls() {
		List<Point> cleanedWalls = new LinkedList<Point>();
		
		for (int i = 0; i < walls.size(); i++) {
			allNodes.add(new WallCleanerNode(walls.get(i)));
		}
		
		for (int i = 0; i < allNodes.size(); i++) {
			WallCleanerNode node = allNodes.get(i);
			for (int j = 0; j < allNodes.size(); j++) {
				if (j == i)
					continue;
				else {
					WallCleanerNode otherNode = allNodes.get(j);
					if (otherNode.isAbove(node))
						node.up = otherNode;
					else if (otherNode.isRightOf(node))
						node.right = otherNode;
					else if (otherNode.isBelow(node))
						node.down = otherNode;
					else if (otherNode.isLeftOf(node))
						node.left = otherNode;
				}
			}
		}
		
		for (int i = 0; i < 2; i++) {
			Set<WallCleanerNode> visitedNodes = new HashSet<WallCleanerNode>();
			WallCleanerNode startNode;
			while ((startNode = getStartNode()) != null) {
				clean(startNode, startNode, visitedNodes, false, 0, 0, RandomNumber.randomBetween(minimumEntrancesToMake, maximumEntrancesToMake));
			}
			
			for (WallCleanerNode node : allNodes)
				cleanedWalls.add(node.location);
		}
		
		return cleanedWalls;
	}
	
	/**
	 * Recursively navigates the node map, going up and right when possible.
	 * Cleans once it hits a visited node (meaning it has found a closed loop).
	 * @param currentNode - The current node
	 * @param previousNode - The previous node
	 * @param visitedNodes - The list of already-visited nodes
	 * @param cleaning - Whether we're creating an entrance now
	 * @param cleaned - How many walls we've removed in this entrance
	 * @return null when finished
	 */
	private WallCleanerNode clean(WallCleanerNode currentNode, WallCleanerNode previousNode, Set<WallCleanerNode> visitedNodes, boolean cleaning, int toClean, int toWait, int entrancesToMake) {
		if (cleaning) {
			if (toClean > 0) {
				if (toWait > 0) {
					toWait--;
				}
				else {
					allNodes.remove(currentNode);
					toClean--;
				}
				if (currentNode.up != null && currentNode.up != previousNode)
					return clean(currentNode.up, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
				if (currentNode.right != null && currentNode.right != previousNode)
					return clean(currentNode.right, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
				if (currentNode.down != null && currentNode.down != previousNode)
					return clean(currentNode.down, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
				if (currentNode.left != null && currentNode.left != previousNode)
					return clean(currentNode.left, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
			}
			else if (entrancesToMake > 0) {
				entrancesToMake--;
				toWait = RandomNumber.randomBetween(minimumWallsToWait, maximumWallsToWait);
				toClean = RandomNumber.randomBetween(minimumSizeOfEntrance, maximumSizeOfEntrance);
				if (currentNode.up != null && currentNode.up != previousNode)
					return clean(currentNode.up, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
				if (currentNode.right != null && currentNode.right != previousNode)
					return clean(currentNode.right, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
				if (currentNode.down != null && currentNode.down != previousNode)
					return clean(currentNode.down, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
				if (currentNode.left != null && currentNode.left != previousNode)
					return clean(currentNode.left, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
				
			}
		}
		else {
			if (visitedNodes.contains(currentNode)) {
				cleaning = true;
				toWait = RandomNumber.randomBetween(minimumWallsToWait, maximumWallsToWait);
				toClean = RandomNumber.randomBetween(minimumSizeOfEntrance, maximumSizeOfEntrance);
				if (currentNode.up != null && currentNode.up != previousNode)
					return clean(currentNode.up, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
				if (currentNode.right != null && currentNode.right != previousNode)
					return clean(currentNode.right, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
				if (currentNode.down != null && currentNode.down != previousNode)
					return clean(currentNode.down, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
				if (currentNode.left != null && currentNode.left != previousNode)
					return clean(currentNode.left, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
			}
			else {
				visitedNodes.add(currentNode);
				currentNode.notHandled = false;
			}
			if (currentNode.up != null)
				return clean(currentNode.up, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
			if (currentNode.right != null)
				return clean(currentNode.right, currentNode, visitedNodes, cleaning, toClean, toWait, entrancesToMake);
		}
		return null;
	}
	
	/** @return The lowest, left-most node that hasn't been handled yet. **/
	private WallCleanerNode getStartNode() {
		if (allNodes.size() == 0)
			return null;
		else {
			WallCleanerNode startNode = allNodes.get(0);
			for (WallCleanerNode node : allNodes) {
				if (node.notHandled) {
					startNode = node;
					break;
				}
			}
			for (WallCleanerNode node : allNodes) {
				if (node.notHandled && node.location.x <= startNode.location.x && node.location.y >= startNode.location.y)
					startNode = node;
			}
			

			if (startNode.notHandled)
				return startNode;
			else
				return null;
		}
	}
}
