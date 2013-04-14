package loader;

import data.Images;
import entity.Feature;
import entity.actor.Monster;
import entity.actor.Player;
import gui.GameWindow;

import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import level.Room;
import control.Game;

/**
 * Loads map layouts from files.
 * @author Milo
 * @since 8 April 2013
 */
public class RoomLoader {
	
	/** The deliminator for items within a line in the file **/
	public static final String inLineDeliminator = ":";
	
	/** The deliminator for sections in the file **/
	public static final String sectionDeliminator = "***";
	
	/**
	 * Creates a map from a file.
	 * @param roomFile - The layout file name (must be a MAP file)
	 * @param game - The game we're loading the room for
	 * @return The data loaded from the file.
	 * @throws IOException if the file can't be found or the layout doesn't match the standard dimensions.
	 */
	public RoomData loadRoom(String roomFile, Game game) throws IOException {
		Image[][] tiles = new Image[GameWindow.GRID_COLUMNS][GameWindow.GRID_ROWS];
		Feature[][] features;
		Player player;
		List<Monster> monsters = new LinkedList<Monster>();
		
		PowerLoader powerLoader = new PowerLoader();
		MonsterLoader monsterLoader = new MonsterLoader(game, powerLoader);
		PlayerLoader playerLoader = new PlayerLoader(game, powerLoader);
		
		File file = new File(roomFile);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		Point playerStartLocation = null;
		String[][] featureGrid = new String[GameWindow.GRID_COLUMNS][GameWindow.GRID_ROWS];
		
		int section = 0;
		int row = 0;
		String line;
		while ((line = reader.readLine()) != null) {
			switch(section) {
			case 0:
				do {
					String[] tileStrings = line.split(inLineDeliminator);
					if (tileStrings.length != GameWindow.GRID_COLUMNS || row == GameWindow.GRID_ROWS) {
						reader.close();
						throw new IOException("Layout dimensions are incorrect for file " + roomFile);
					}
					for (int column = 0; column < GameWindow.GRID_COLUMNS; column++)
						tiles[column][row] = ImageLoader.loadImage(tileStrings[column]);
					row++;
				} while ((line = reader.readLine()) != null && !line.equals(sectionDeliminator));
				section++;
				row = 0;
				break;
			case 1:
				do {
					String[] featureStrings = line.split(inLineDeliminator);
					if (featureStrings.length != GameWindow.GRID_COLUMNS || row == GameWindow.GRID_ROWS) {
						reader.close();
						throw new IOException("Layout dimensions are incorrect for file " + roomFile);
					}
					for (int column = 0; column < GameWindow.GRID_COLUMNS; column++)
						featureGrid[column][row] = featureStrings[column];
					row++;
				} while ((line = reader.readLine()) != null && !line.equals(sectionDeliminator));
				section++;
				break;
			case 2:
				String[] pointStrings = line.split(inLineDeliminator);
				playerStartLocation = new Point(Integer.parseInt(pointStrings[0]), Integer.parseInt(pointStrings[1]));
				reader.readLine();
				section++;
				break;
			case 3:
				do {
					monsters.add(monsterLoader.loadMonster(line));
				} while ((line = reader.readLine()) != null && !line.equals(sectionDeliminator));
				section++;
				break;
			}
		}
		reader.close();
		
		player = playerLoader.loadPlayer(playerStartLocation);
		features = createFeatures(featureGrid, game);
		
		monsterLoader.release();
		powerLoader.release();
		playerLoader.release();
		
		return new RoomData(tiles, features, player, monsters);
	}
	
	/**
	 * Creates the true feature grid from a string grid.
	 * @param featureGrid - The string grid
	 * @param game - The game we're interacting with
	 * @return The true grid of Feature objects
	 */
	private Feature[][] createFeatures(String[][] featureGrid, Game game) {
		Feature[][] features = new Feature[GameWindow.GRID_COLUMNS][GameWindow.GRID_ROWS];
		for (int row = 0; row < GameWindow.GRID_ROWS; row++)
			for (int column = 0; column < GameWindow.GRID_COLUMNS; column++)
				if (!featureGrid[column][row].equals(Images.NULL_TILE))
					if (Images.EDGED.contains(featureGrid[column][row])) {
						features[column][row] = new Feature(column, row, ImageLoader.loadImage(featureGrid[column][row] + getPositionString(featureGrid[column][row], column, row, featureGrid)), null, game);
					}
					else
						features[column][row] = new Feature(column, row, ImageLoader.loadImage(featureGrid[column][row]), null, game);
		return features;
						
	}
	
	/**
	 * Returns the correct suffix for edged features.
	 * @param featureType - The feature base type (i.e. "water")
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return The correct suffix for edged features
	 */
	private String getPositionString(String featureType, int column, int row, String[][] featureGrid) {
		if (isTopLeft(featureType, column, row, featureGrid))
			return "_tl";
		else if (isTopEdge(featureType, column, row, featureGrid))
			return "_t";
		else if (isTopRight(featureType, column, row, featureGrid))
			return "_tr";
		else if (isRightEdge(featureType, column, row, featureGrid))
			return "_r";
		else if (isBottomRight(featureType, column, row, featureGrid))
			return "_br";
		else if (isBottomEdge(featureType, column, row, featureGrid))
			return "_b";
		else if (isBottomLeft(featureType, column, row, featureGrid))
			return "_bl";
		else if (isLeftEdge(featureType, column, row, featureGrid))
			return "_l";
		else if (isInverseTopLeft(featureType, column, row, featureGrid))
			return "_itl";
		else if (isInverseTopRight(featureType, column, row, featureGrid))
			return "_itr";
		else if (isInverseBottomRight(featureType, column, row, featureGrid))
			return "_ibr";
		else if (isInverseBottomLeft(featureType, column, row, featureGrid))
			return "_ibl";
		else
			return "";
	}
	
	/**
	 * Checks if a feature is the top left corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is the top left corner of the set
	 */
	private boolean isTopLeft(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, false, true, true, false);
	}
	
	/**
	 * Checks if a feature is a top edge of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is a top edge of the set
	 */
	private boolean isTopEdge(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, false, true, true, true);
	}
	
	/**
	 * Checks if a feature is the top right corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is the top right corner of the set
	 */
	private boolean isTopRight(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, false, false, true, true);
	}
	
	/**
	 * Checks if a feature is a right edge of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is a right edge of the set
	 */
	private boolean isRightEdge(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, true, false, true, true);
	}
	
	/**
	 * Checks if a feature is the bottom right corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is the bottom right corner of the set
	 */
	private boolean isBottomRight(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, true, false, false, true);
	}
	
	/**
	 * Checks if a feature is a bottom edge of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is a bottom edge of the set
	 */
	private boolean isBottomEdge(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, true, true, false, true);
	}

	/**
	 * Checks if a feature is the bottom left corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is the bottom left corner of the set
	 */
	private boolean isBottomLeft(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, true, true, false, false);
	}

	/**
	 * Checks if a feature is a left edge of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is a left edge of the set
	 */
	private boolean isLeftEdge(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, true, true, true, false);
	}
	
	/**
	 * Checks if a feature is an inverse top left corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is an inverse top left corner of the set
	 */
	private boolean isInverseTopLeft(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, true, true, true, true, false, true, true, true);
	}
	
	/**
	 * Checks if a feature is an inverse top right corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is an inverse top right corner of the set
	 */
	private boolean isInverseTopRight(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, true, true, true, true, true, true, false, true);
	}
	
	/**
	 * Checks if a feature is an inverse bottom left corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is an inverse bottom left corner of the set
	 */
	private boolean isInverseBottomLeft(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, true, true, false, true, true, true, true, true);
	}
	
	/**
	 * Checks if a feature is an inverse bottom right corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The string grid of feature types
	 * @return True if the feature is an inverse bottom right corner of the set
	 */
	private boolean isInverseBottomRight(String featureType, int column, int row, String[][] featureGrid) {
		return isPosition(featureType, column, row, featureGrid, false, true, true, true, true, true, true, true);
	}
	
	/**
	 * Checks if a feature's surroundings matches the parameters.
	 * @param featureType - The feature's type
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The grid of feature type strings
	 * @param top - If the feature above should match this feature type
	 * @param right - If the feature to the right should match this feature type
	 * @param bottom - If the feature below should match this feature type
	 * @param left - If the feature to the left should match this feature type
	 * @return True if the feature's surroundings match the parameters
	 */
	private boolean isPosition(	String featureType, int column, int row, String[][] featureGrid,
								boolean top, boolean right, boolean bottom, boolean left) {
		return	featureGrid[column][row - 1].equals(featureType) == top &&
				featureGrid[column + 1][row].equals(featureType) == right &&
				featureGrid[column][row + 1].equals(featureType) == bottom &&
				featureGrid[column - 1][row].equals(featureType) == left;
	}
	
	/**
	 * Checks if a feature's surroundings matches the parameters.
	 * @param featureType - The feature's type
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param featureGrid - The grid of feature type strings
	 * @param topLeft - If the feature above and to the left should match this feature type
	 * @param top - If the feature above should match this feature type
	 * @param topRight - If the feature above and to the right should match this feature type
	 * @param right - If the feature to the right should match this feature type
	 * @param bottomRight - If the feature below and to the right should match this feature type
	 * @param bottom - If the feature below should match this feature type
	 * @param bottomLeft - If the feature below and to the left should match this feature type
	 * @param left - If the feature to the left should match this feature type
	 * @return True if the feature's surroundings match the parameters
	 */
	private boolean isPosition(	String featureType, int column, int row, String[][] featureGrid,
						boolean topLeft, boolean top, boolean topRight,
						boolean right, boolean bottomRight, boolean bottom,
						boolean bottomLeft, boolean left) {
		return	featureGrid[column - 1][row - 1].equals(featureType) == topLeft &&
				featureGrid[column][row - 1].equals(featureType) == top &&
				featureGrid[column + 1][row - 1].equals(featureType) == topRight &&
				featureGrid[column + 1][row].equals(featureType) == right &&
				featureGrid[column + 1][row + 1].equals(featureType) == bottomRight &&
				featureGrid[column][row + 1].equals(featureType) == bottom &&
				featureGrid[column - 1][row + 1].equals(featureType) == bottomLeft &&
				featureGrid[column - 1][row].equals(featureType) == left;
	}
	
	/**
	 * Creates a test room file: walls on the borders and floor tiles everywhere else.
	 * @throws IOException if the file can't be created.
	 */
	public static void createTestRoom() throws IOException {
		File file = new File(Room.TEST_ROOM);
		PrintWriter outWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)), true);
		
		for (int row = 0; row < GameWindow.GRID_ROWS; row++) {
			for (int column = 0; column < GameWindow.GRID_COLUMNS; column++) {
				outWriter.print(Images.FLOOR_STONE);
				if (column < GameWindow.GRID_COLUMNS - 1)
					outWriter.print(inLineDeliminator);
			}
			outWriter.print(System.getProperty("line.separator"));
		}
		
		outWriter.print(sectionDeliminator + System.getProperty("line.separator"));
		
		for (int row = 0; row < GameWindow.GRID_ROWS; row++) {
			for (int column = 0; column < GameWindow.GRID_COLUMNS; column++) {
				if (shouldBeWall(column, row))
					outWriter.print(Images.WALL);
				else if (shouldBeColumn(column, row))
					outWriter.print(Images.COLUMN);
				else if (shouldBeWater(column, row))
					outWriter.print(Images.WATER);
				else
					outWriter.print(Images.NULL_TILE);
				if (column < GameWindow.GRID_COLUMNS - 1)
					outWriter.print(inLineDeliminator);
			}
			outWriter.print(System.getProperty("line.separator"));
		}
		
		outWriter.print(sectionDeliminator + System.getProperty("line.separator"));
		
		outWriter.print(1 + inLineDeliminator + 1);
		outWriter.print(System.getProperty("line.separator"));
		
		outWriter.print(sectionDeliminator + System.getProperty("line.separator"));
		
		printTestMonster(GameWindow.GRID_COLUMNS - 2, GameWindow.GRID_ROWS - 2, outWriter);
		printTestMonster(GameWindow.GRID_COLUMNS - 2, 1, outWriter);
		printTestMonster(1, GameWindow.GRID_ROWS - 2, outWriter);
		
		outWriter.close();
	}
	
	/**
	 * Defines where to put walls on the test map
	 * @param column - The column to check
	 * @param row - The row to check
	 * @return True if there should be walls here, false otherwise
	 */
	private static boolean shouldBeWall(int column, int row) {
		return	row == 0 ||
				row == GameWindow.GRID_ROWS - 1 ||
				column == 0 ||
				column == GameWindow.GRID_COLUMNS - 1 ||
				(row > 5 && row < GameWindow.GRID_ROWS - 5 && column == GameWindow.GRID_COLUMNS / 2);
	}
	
	/**
	 * Defines where to put columns on the test map
	 * @param column - The column to check
	 * @param row - The row to check
	 * @return True if there should be a column here, false otherwise
	 */
	private static boolean shouldBeColumn(int column, int row) {
		return row % 7 == 0 && column % 9 == 0;
	}
	
	/**
	 * Defines where to put a lake on the test map
	 * @param column - The column to check
	 * @param row - The row to check
	 * @return True if there should be water here, false otherwise
	 */
	private static boolean shouldBeWater(int column, int row) {
		return	(row > 4 && row < 10 && column > 28 && column < 39 &&
				!(row == 9 && column == 29) && !(row == 7 && column == 35) &&
				!(row == 8 && column == 34) && !(row == 7 && column == 34) &&
				!(row == 8 && column == 33) && !(row == 9 && column == 33)) ||
				(row == 4 && column > 30 && column < 36) ||
				(row == 10 && column > 33 && column < 38);
	}
	
	/**
	 * Prints a test monster to the test map file.
	 * @param column - The column the monster starts at
	 * @param row - The row the monster starts at
	 * @param outWriter - The writer to write to
	 */
	private static void printTestMonster(int column, int row, PrintWriter outWriter) {
		outWriter.print("Default Monster" + inLineDeliminator);
		outWriter.print(column + inLineDeliminator + row);
		outWriter.print(System.getProperty("line.separator"));
	}
}
