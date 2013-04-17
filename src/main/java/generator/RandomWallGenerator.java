package generator;

import gui.GameWindow;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.Images;

/**
 * Generates a random wall pattern of rooms and straight chunks.
 * @author Milo Carbol
 * @since 14 April 2013
 * @deprecated - Too random, can trigger an infinite loop in the wall cleaner
 */
public class RandomWallGenerator implements IFeatureGenerator {

	/** Constraints **/
	private static final int	maximumNumberOfWallChunks = 5,
								minimumNumberOfWallChunks = 3,
								maximumNumberOfRooms = 6,
								minimumNumberOfRooms = 2;

	/** Constraints **/
	private static final int	maximumWallWidth = GameWindow.GRID_COLUMNS / 2,
								minimumWallWidth = 7,
								maximumWallHeight = GameWindow.GRID_ROWS / 2,
								minimumWallHeight = 7;
	
	/**
	 * Generates a random pattern of walled rooms and straight chunks.
	 * @param wallType - The type of wall to use
	 * @return The list of wall locations
	 */
	public List<Point> generateFeatures(String wallType) {
		List<Point> walls = generateStraightWalls();
		Map<Point, String> walledRooms = generateWalledRooms(wallType);
		for (Point wall : walledRooms.keySet())
			if (walledRooms.get(wall).equals(wallType) && !walls.contains(wall))
				walls.add(wall);
			else if (walledRooms.get(wall).equals(Images.NULL_TILE) && walls.contains(wall))
				walls.remove(wall);
		
		walls = new WallCleaner(walls).cleanWalls();
		return walls;
	}

	/**
	 * Generates straight wall chunks.
	 * @return The list of wall locations
	 */
	private List<Point> generateStraightWalls() {
		List<Point> wallLocations = new LinkedList<Point>();
		
		int numberOfWallChunks = RandomNumber.randomBetween(minimumNumberOfWallChunks, maximumNumberOfWallChunks);
		for (int i = 0; i < numberOfWallChunks; i++) {
			boolean horizontal = (int)(Math.random() * 2) == 0;
			
			if (horizontal) {
				int width = RandomNumber.randomBetween(minimumWallWidth, maximumWallWidth);
				int startColumn = RandomNumber.randomBetween(0, GameWindow.GRID_COLUMNS - width);
				int startRow = RandomNumber.randomBetween(0, GameWindow.GRID_ROWS);
				
				for (int x = 0; x < width; x++) {
					int column = startColumn + x;
					if (column >= 0 && column < GameWindow.GRID_COLUMNS)
						wallLocations.add(new Point(column, startRow));
					else
						break;
				}
			}
			else {
				int height = RandomNumber.randomBetween(minimumWallHeight, maximumWallHeight);
				int startColumn = RandomNumber.randomBetween(0, GameWindow.GRID_COLUMNS);
				int startRow = RandomNumber.randomBetween(0, GameWindow.GRID_ROWS - height);
				
				for (int y = 0; y < height; y++) {
					int row = startRow + y;
					if (row >= 0 && row < GameWindow.GRID_ROWS)
						wallLocations.add(new Point(startColumn, row));
					else
						break;
				}
			}
		}
		
		return wallLocations;
	}
	
	/**
	 * Generates walled rooms.
	 * @return The list of wall locations
	 */
	private Map<Point, String> generateWalledRooms(String wallType) {
		Map<Point, String> wallLocations = new HashMap<Point, String>();
		
		int numberOfRooms = RandomNumber.randomBetween(minimumNumberOfRooms, maximumNumberOfRooms);
		for (int i = 0; i < numberOfRooms; i++) {		
			int width = RandomNumber.randomBetween(minimumWallWidth, maximumWallWidth);
			int height = RandomNumber.randomBetween(minimumWallHeight, maximumWallHeight);
			
			Point topLeftCorner = new Point(RandomNumber.randomBetween(0, GameWindow.GRID_COLUMNS - width), RandomNumber.randomBetween(0, GameWindow.GRID_ROWS - height));
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++) {
					int column = topLeftCorner.x + x;
					int row = topLeftCorner.y + y;
					if (column >= 0 && column < GameWindow.GRID_COLUMNS &&
						row >= 0 && row < GameWindow.GRID_ROWS)
						if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
							wallLocations.put(new Point(column, row), wallType);
						else
							wallLocations.put(new Point(column, row), Images.NULL_TILE);
				}
		}
		
		return wallLocations;
	}
}
