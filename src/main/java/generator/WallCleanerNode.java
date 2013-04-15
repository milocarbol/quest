package generator;

import java.awt.Point;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class WallCleanerNode {

	public final Point location;
	public WallCleanerNode up = null, right = null, down = null, left = null;
	public boolean notHandled = true;
	
	public WallCleanerNode(Point self) {
		this.location = self;
	}
	
	public boolean isAbove(WallCleanerNode other) {
		return location.x == other.location.x && location.y == other.location.y - 1;
	}
	
	public boolean isRightOf(WallCleanerNode other) {
		return location.x == other.location.x + 1 && location.y == other.location.y;
	}
	
	public boolean isBelow(WallCleanerNode other) {
		return location.x == other.location.x && location.y == other.location.y + 1;
	}
	
	public boolean isLeftOf(WallCleanerNode other) {
		return location.x == other.location.x - 1 && location.y == other.location.y;
	}
	
	/**
     * Computes the hashcode for this node.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
            append(location).
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

        WallCleanerNode rhs = (WallCleanerNode) obj;
        return new EqualsBuilder().
            append(location, rhs.location).
            isEquals();
    }
}
