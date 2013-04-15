package generator;

import data.Images;
import entity.Feature;
import entity.actor.Monster;
import entity.actor.Player;
import gui.GameWindow;

import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import loader.FeatureLoader;
import loader.IRoomLoader;
import loader.ImageLoader;
import loader.PlayerLoader;
import loader.RoomData;
import loader.RoomDataLoader;

/**
 * Generates a room.
 * @author Milo Carbol
 * @since 14 April 2013
 */
public class RoomGenerator implements IRoomLoader {
	
	private static final int	maximumNumberOfWallChunks = 5,
								minimumNumberOfWallChunks = 3,
								maximumNumberOfRooms = 6,
								minimumNumberOfRooms = 2;
	
	private static final int	maximumWallWidth = GameWindow.GRID_COLUMNS / 2,
								minimumWallWidth = 7,
								maximumWallHeight = GameWindow.GRID_ROWS / 2,
								minimumWallHeight = 7;
	
	/** Room data lookup map **/
	private final Map<String, String> data;
	
	/** The loader for features **/
	private final FeatureLoader featureLoader;
	
	/** The loader for the player **/
	private final PlayerLoader playerLoader;
	
	/** The deliminator between available types **/
	private static final String deliminator = ",";
	
	/**
	 * Creates a new Room Generator.
	 * @param roomType - The type of room to generate.
	 * @param dataLoader - The loader for room data.
	 * @param featureLoader - The loader for features.
	 * @param playerLoader - The loader for the player.
	 */
	public RoomGenerator(String roomType, RoomDataLoader dataLoader, FeatureLoader featureLoader, PlayerLoader playerLoader) {
		this.data = dataLoader.loadRoomData(roomType);
		this.featureLoader = featureLoader;
		this.playerLoader = playerLoader;
	}
	
	/**
	 * Generates a room.
	 */
	public RoomData loadRoom() {
		
		String[][] tileStrings = generateTiles(data);
		String[][] featureStrings = generateFeatures(data);
		
		Image[][] tiles = buildTileMap(tileStrings);
		Feature[][] features = buildFeatureMap(featureStrings);
		Player player = playerLoader.loadPlayer(new Point(1, 1));
		List<Monster> monsters = new LinkedList<Monster>();
		
		return new RoomData(tiles, features, player, monsters);
	}
	
	/**
	 * Generates the tile base for the room.
	 * @param data - The data lookup map.
	 * @return The tile map
	 */
	private String[][] generateTiles(Map<String, String> data) {
		String[][] tileStrings = new String[GameWindow.GRID_COLUMNS][GameWindow.GRID_ROWS];
		
		String floorType = randomizeType(data.get("floor"));
		for (int column = 0; column < GameWindow.GRID_COLUMNS; column++)
			for (int row = 0; row < GameWindow.GRID_ROWS; row++)
				tileStrings[column][row] = floorType;
		
		return tileStrings;
	}
	
	/**
	 * Builds the true tile map of images from a tile map of strings.
	 * @param tileStrings - The grid of strings.
	 * @return The tile map
	 */
	private Image[][] buildTileMap(String[][] tileStrings) {
		Image[][] tiles = new Image[GameWindow.GRID_COLUMNS][GameWindow.GRID_ROWS];
		
		for (int column = 0; column < GameWindow.GRID_COLUMNS; column++)
			for (int row = 0; row < GameWindow.GRID_ROWS; row++)
				tiles[column][row] = ImageLoader.loadImage(tileStrings[column][row]);
		
		return tiles;
	}
	
	/**
	 * Generates the features in the room.
	 * @param data - The data lookup map.
	 * @return The features in the room.
	 */
	private String[][] generateFeatures(Map<String, String> data) {
		String[][] featureStrings = new String[GameWindow.GRID_COLUMNS][GameWindow.GRID_ROWS];
		
		for (int column = 0; column < GameWindow.GRID_COLUMNS; column++)
			for (int row = 0; row < GameWindow.GRID_ROWS; row++)
				featureStrings[column][row] = Images.NULL_TILE;
		
		String wallType = randomizeType(data.get("wall"));
//		for (Point wall : generateStraightWalls())
//			featureStrings[wall.x][wall.y] = wallType;
//		
//		Map<Point, String> walledRooms = generateWalledRooms(wallType);
//		for (Point wall : walledRooms.keySet())
//			featureStrings[wall.x][wall.y] = walledRooms.get(wall); 
		
		List<Point> walls = generateStraightWalls();
		Map<Point, String> walledRooms = generateWalledRooms(wallType);
		for (Point wall : walledRooms.keySet())
			if (walledRooms.get(wall).equals(wallType) && !walls.contains(wall))
				walls.add(wall);
			else if (walledRooms.get(wall).equals(Images.NULL_TILE) && walls.contains(wall))
				walls.remove(wall);
		
		walls = new WallCleaner(walls).cleanWalls();
		
		for (Point wall : walls)
			featureStrings[wall.x][wall.y] = wallType;
		
		
		return featureStrings;
	}
	
	/**
	 * Builds the true feature map from the string map.
	 * @param featureStrings - The string map.
	 * @return The true feature map.
	 */
	private Feature[][] buildFeatureMap(String[][] featureStrings) {
		return featureLoader.createFeatures(featureStrings);
	}
	
	private List<Point> generateStraightWalls() {
		List<Point> wallLocations = new LinkedList<Point>();
		
		int numberOfWallChunks = randomBetween(minimumNumberOfWallChunks, maximumNumberOfWallChunks);
		for (int i = 0; i < numberOfWallChunks; i++) {
			boolean horizontal = (int)(Math.random() * 2) == 0;
			
			if (horizontal) {
				int width = randomBetween(minimumWallWidth, maximumWallWidth);
				int startColumn = randomBetween(0, GameWindow.GRID_COLUMNS - width);
				int startRow = randomBetween(0, GameWindow.GRID_ROWS);
				
				for (int x = 0; x < width; x++) {
					int column = startColumn + x;
					if (column >= 0 && column < GameWindow.GRID_COLUMNS)
						wallLocations.add(new Point(column, startRow));
					else
						break;
				}
			}
			else {
				int height = randomBetween(minimumWallHeight, maximumWallHeight);
				int startColumn = randomBetween(0, GameWindow.GRID_COLUMNS);
				int startRow = randomBetween(0, GameWindow.GRID_ROWS - height);
				
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
	
	private Map<Point, String> generateWalledRooms(String wallType) {
		Map<Point, String> wallLocations = new HashMap<Point, String>();
		
		int numberOfRooms = randomBetween(minimumNumberOfRooms, maximumNumberOfRooms);
		for (int i = 0; i < numberOfRooms; i++) {		
			int width = randomBetween(minimumWallWidth, maximumWallWidth);
			int height = randomBetween(minimumWallHeight, maximumWallHeight);
			
			Point topLeftCorner = new Point(randomBetween(0, GameWindow.GRID_COLUMNS - width), randomBetween(0, GameWindow.GRID_ROWS - height));
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
	
	/**
	 * Randomly chooses a type from those available.
	 * @param typeString - The available types, deliminated by RoomGenerator.deliminator
	 * @return The randomly chosen type.
	 */
	private String randomizeType(String typeString) {
		String[] types = typeString.split(deliminator);
		return types[(int)(Math.random()*types.length)];
	}
	
	private static int randomBetween(int low, int high) {
		return (int)(Math.random() * (high - low) + low);
	}
}
