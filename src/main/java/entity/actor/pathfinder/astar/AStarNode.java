package entity.actor.pathfinder.astar;

import java.awt.Point;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A node in the A* algorithm's path.
 * @author Milo Carbol
 * @since 12 April 2013
 */
public class AStarNode {
	private double G, H;
	private Point gridSpace;
	private AStarNode parent;
	
	/**
	 * Creates a new A* node.
	 * @param gridSpace - The grid space this node represents.
	 * @param target - The target grid space (for H computation).
	 */
	public AStarNode(Point gridSpace, Point target) {
		this.gridSpace = gridSpace;
		this.G = 0;
		this.H = Math.abs(target.distance(gridSpace));
	}
	
	/** @return The F value for this A* node **/
	public double getF() { return G + H; }
	
	/** @return The G value (cost to reach this node) **/
	public double getG() { return G; }
	
	/** @return The grid space represented by this node **/
	public Point getGridSpace() { return gridSpace; }
	
	/** @return The F value for this A* node **/
	public AStarNode getParent() { return parent; }
	
	/**
	 * Rearranges the path, updating this node's parent and G value.
	 * @param newParent - The new parent node in the path.
	 * @param cost - The cost to go from the parent to this space.
	 */
    public void updatePath(AStarNode newParent, int cost) {
    	parent = newParent;
    	G = newParent.G + cost;
    }
    
    /**
     * Computes the hashcode for this node.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
            append(gridSpace).
            toHashCode();
    }

    /**
     * Checks if this node is equal to another node.
     * @param obj - The object to compare to.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;

        AStarNode rhs = (AStarNode) obj;
        return new EqualsBuilder().
            append(gridSpace, rhs.gridSpace).
            isEquals();
    }
}
