package entity.actor.pathfinder;

import java.awt.Point;

import control.Game;

/**
 * Computes straight-line paths.
 * @author Milo Carbol
 * @since 12 April 2013
 */
public class DirectPathfinder implements IPathfinder {

	/** Target grid space **/
	private Point targetLocation;
	
	/** Current grid space **/
	private Point currentLocation;
	
	/**
	 * If the chunks (the line segments placed in
	 * 	separate rows or columns to make the appearance of a straight line on the grid
	 * 	are horizontally aligned. False if they are vertically aligned.
	 **/
	private boolean chunksAreHorizontal;
	
	/** Array of chunk sizes. Each index is a separate chunk. **/
	private int[] chunkSizes = {};
	
	/** The current chunk **/
	private int chunkIndex = 0;
	
	/**
	 * The current position in the chunk.
	 * 1-indexed because 1 grid space counts as 1 distance, not 0.
	 **/
	private int indexInChunk = 1;
	
	/** The game to interact with **/
	private Game game;
	
	public DirectPathfinder(Game game, Point currentLocation) {
		this.game = game;
		this.currentLocation = new Point(currentLocation);
		this.targetLocation = new Point(currentLocation);
	}
	
	/**
	 * @return The next grid space in the actor's path.
	 */
	public Point getNextLocation() {
		Point next = new Point(currentLocation);
		if (currentLocation.x != targetLocation.x || currentLocation.y != targetLocation.y) {
			if (chunksAreHorizontal) {
				int potentialXPosition = currentLocation.x + computeDirection(currentLocation.x, targetLocation.x);
				
				if (indexInChunk < chunkSizes[chunkIndex]) {
					if (game.spaceIsFree(potentialXPosition, currentLocation.y)) {
						next.x = potentialXPosition;
						indexInChunk++;
					}
				}
				else {
					int potentialYPosition = currentLocation.y + computeDirection(currentLocation.y, targetLocation.y);
					
					if (game.spaceIsFree(potentialXPosition, potentialYPosition)) {
						next.x = potentialXPosition;
						next.y = potentialYPosition;
						chunkIndex++;
						indexInChunk = 1;
					}
				}
			}
			else {
				int potentialYPosition = currentLocation.y + computeDirection(currentLocation.y, targetLocation.y);
				
				if (indexInChunk < chunkSizes[chunkIndex]) {
					if (game.spaceIsFree(currentLocation.x, potentialYPosition)) {
						next.y = potentialYPosition;
						indexInChunk++;
					}
				}
				else {
					int potentialXPosition = currentLocation.x + computeDirection(currentLocation.x, targetLocation.x);
					
					if (game.spaceIsFree(potentialXPosition, potentialYPosition)) {
						next.x = potentialXPosition;
						next.y = potentialYPosition;
						chunkIndex++;
						indexInChunk = 1;
					}
				}
			}
		}
		
		currentLocation.x = next.x;
		currentLocation.y = next.y;
		return next;
	}
	
	/**
	 * Sets the path's target point and recomputes the actor's path.
	 * @param targetLocation - The new destination
	 **/
	public void setTargetLocation(Point targetLocation) {
		this.targetLocation.x = targetLocation.x;
		this.targetLocation.y = targetLocation.y;
		
		indexInChunk = 1;
		chunkIndex = 0;
		
		int distanceX = Math.abs(targetLocation.x - currentLocation.x),
			distanceY = Math.abs(targetLocation.y - currentLocation.y);
		
		int chunkSize, remainder;
		
		if (distanceX >= distanceY) {
			chunksAreHorizontal = true;
			chunkSizes = new int[distanceY+1];
			
			if (distanceY != 0) {
				chunkSize = (distanceX + 1) / (distanceY + 1);
				remainder = (distanceX + 1) % (distanceY + 1);
			}
			else {
				chunkSize = distanceX;
				remainder = 0;
			}
		}
		else {
			chunksAreHorizontal = false;
			chunkSizes = new int[distanceX+1];
			
			if (distanceX != 0) {
				chunkSize = (distanceY + 1) / (distanceX + 1);
				remainder = (distanceY + 1) % (distanceX + 1);
			}
			else {
				chunkSize = distanceY;
				remainder = 0;
			}
		}
		
		for (int index = 0; index < chunkSizes.length; index++)
			chunkSizes[index] = chunkSize;
		
		for (int index = chunkSizes.length / 2 - remainder / 2; remainder > 0; index++) {
			chunkSizes[index]++;
			remainder--;
		}
	}
	
	/**
	 * Computes the direction (positive or negative) required to reach the target.
	 * @param current - The current grid space on this axis.
	 * @param target - The target grid space on this axis.
	 * @return 1 if the actor must increment their position on this axis, -1 if they must decrement, 0 if they are at the target space.
	 */
	private int computeDirection(int current, int target) {
		if (target == current)
			return 0;
		return (target - current) / Math.abs(target - current);
	}
}
