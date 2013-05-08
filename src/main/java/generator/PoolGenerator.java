package generator;

import gui.GameWindow;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import control.Game;

/**
 * Generates a pool of water.
 * @author Milo Carbol
 * @since 6 May 2013
 */
public class PoolGenerator implements IFeatureGenerator {
	
	private static final int	blockWidth = 3,
								blockHeight = 3;
	
	private static final int	minimumWidthInBlocks = 1,
								maximumWidthInBlocks = 6,
								minimumHeightInBlocks = 1,
								maximumHeightInBlocks = 4;
	
	/**
	 * Generates the pool shape.
	 * @param featureType - unused but required by interface
	 * @return The points in the pool shape
	 */
	public List<Point> generateFeatures(String featureType) {
		List<Point> waterSpaces = new LinkedList<Point>();
		waterSpaces.addAll(generatePool());
		
		return waterSpaces;
	}
	
	/**
	 * Generates a pool shape.
	 * @return The points in the pool shape
	 */
	private List<Point> generatePool() {
		List<Point> pool = new LinkedList<Point>();
		
		int column = RandomNumber.randomBetween(0, GameWindow.GRID_COLUMNS);
		int row = RandomNumber.randomBetween(0, GameWindow.GRID_ROWS);
		
		int widthInBlocks = RandomNumber.randomBetween(minimumWidthInBlocks, maximumWidthInBlocks);
		
		for (int i = 0; i < widthInBlocks; i++) {
			int segmentHeightInBlocks = RandomNumber.randomBetween(minimumHeightInBlocks, maximumHeightInBlocks);
			for (int isub = 0; isub < blockWidth; isub++) {
				for (int j = 0; j < segmentHeightInBlocks; j++) {
					for (int jsub = 0; jsub < blockHeight; jsub++) {
						Point point = new Point(column + i * blockWidth + isub, row + j * blockHeight + jsub - segmentHeightInBlocks * blockHeight / 2 + 1);
						if (Game.spaceIsValid(point))
							pool.add(point);
					}
				}
			}
		}
		
		return pool;
	}
	
}
