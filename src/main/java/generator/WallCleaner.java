package generator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WallCleaner {

	private final int numberOfWallsToClean = 8;
	
	private List<WallCleanerNode> allNodes;
	private List<Point> walls;
	
	public WallCleaner(List<Point> walls) {
		this.walls = walls;
		this.allNodes = new ArrayList<WallCleanerNode>();
	}
	
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
			while ((startNode = getStartNode()).notHandled) {
				clean(startNode, startNode, visitedNodes, false, 0);
			}
			
			for (WallCleanerNode node : allNodes)
				cleanedWalls.add(node.location);
		}
		
		return cleanedWalls;
	}
	
	private WallCleanerNode clean(WallCleanerNode currentNode, WallCleanerNode previousNode, Set<WallCleanerNode> visitedNodes, boolean cleaning, int cleaned) {
		if (cleaning) {
			if (cleaned < numberOfWallsToClean) {
				if (cleaned >= 0)
					allNodes.remove(currentNode);
				cleaned++;
				if (currentNode.up != null && currentNode.up != previousNode)
					return clean(currentNode.up, currentNode, visitedNodes, cleaning, cleaned);
				if (currentNode.right != null && currentNode.right != previousNode)
					return clean(currentNode.right, currentNode, visitedNodes, cleaning, cleaned);
				if (currentNode.down != null && currentNode.down != previousNode)
					return clean(currentNode.down, currentNode, visitedNodes, cleaning, cleaned);
				if (currentNode.left != null && currentNode.left != previousNode)
					return clean(currentNode.left, currentNode, visitedNodes, cleaning, cleaned);
			}
		}
		else {
			if (visitedNodes.contains(currentNode)) {
				cleaning = true;
				if (currentNode.up != null && currentNode.up != previousNode)
					return clean(currentNode.up, currentNode, visitedNodes, cleaning, cleaned);
				if (currentNode.right != null && currentNode.right != previousNode)
					return clean(currentNode.right, currentNode, visitedNodes, cleaning, cleaned);
				if (currentNode.down != null && currentNode.down != previousNode)
					return clean(currentNode.down, currentNode, visitedNodes, cleaning, cleaned);
				if (currentNode.left != null && currentNode.left != previousNode)
					return clean(currentNode.left, currentNode, visitedNodes, cleaning, cleaned);
			}
			else {
				visitedNodes.add(currentNode);
				currentNode.notHandled = false;
			}
			if (currentNode.up != null)
				return clean(currentNode.up, currentNode, visitedNodes, cleaning, cleaned);
			if (currentNode.right != null)
				return clean(currentNode.right, currentNode, visitedNodes, cleaning, cleaned);
		}
		return null;
	}
	
	private WallCleanerNode getStartNode() {
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
		return startNode;
	}
}
