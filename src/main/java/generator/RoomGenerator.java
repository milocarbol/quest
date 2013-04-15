package generator;

import java.awt.Image;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import loader.FeatureLoader;
import loader.IRoomLoader;
import loader.ImageLoader;
import loader.PlayerLoader;
import loader.RoomData;
import loader.RoomDataLoader;
import data.Images;
import entity.Feature;
import entity.actor.Monster;
import entity.actor.Player;
import gui.GameWindow;

/**
 * Generates a room.
 * @author Milo Carbol
 * @since 14 April 2013
 */
public class RoomGenerator implements IRoomLoader {
	
	/** Room data lookup map **/
	private final Map<String, String> data;
	
	/** The wall generator to use **/
	private final IWallGenerator wallGenerator;
	
	/** The loader for features **/
	private final FeatureLoader featureLoader;
	
	/** The loader for the player **/
	private final PlayerLoader playerLoader;
	
	/** The deliminator between available types **/
	private static final String deliminator = ",";
	
	/**
	 * Creates a new Room Generator.
	 * @param roomType - The type of room to generate.
	 * @param wallGenerator - The wall generator to use
	 * @param dataLoader - The loader for room data.
	 * @param featureLoader - The loader for features.
	 * @param playerLoader - The loader for the player.
	 */
	public RoomGenerator(String roomType, IWallGenerator wallGenerator, RoomDataLoader dataLoader, FeatureLoader featureLoader, PlayerLoader playerLoader) {
		this.data = dataLoader.loadRoomData(roomType);
		this.wallGenerator = wallGenerator;
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
		
		List<Point> walls = wallGenerator.generateWalls(wallType);
		
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
	
	/**
	 * Randomly chooses a type from those available.
	 * @param typeString - The available types, deliminated by RoomGenerator.deliminator
	 * @return The randomly chosen type.
	 */
	private String randomizeType(String typeString) {
		String[] types = typeString.split(deliminator);
		return types[(int)(Math.random()*types.length)];
	}
}
