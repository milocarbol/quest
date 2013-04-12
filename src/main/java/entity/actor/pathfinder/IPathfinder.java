package entity.actor.pathfinder;

import java.awt.Point;

/**
 * Handles path-finding for actors.
 * @author Milo Carbol
 * @since 12 April 2013
 */
public interface IPathfinder {
	
	/**
	 * @return The next grid space in the actor's path.
	 */
	public Point getNextLocation();
	
	/**
	 * Sets the path's target point and recomputes the actor's path.
	 * @param targetLocation - The new destination
	 **/
	public void setTargetLocation(Point targetLocation);
}
