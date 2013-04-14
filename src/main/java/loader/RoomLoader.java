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
		FeatureLoader featureLoader = new FeatureLoader(game);
		
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
		features = featureLoader.createFeatures(featureGrid);
		
		monsterLoader.release();
		powerLoader.release();
		playerLoader.release();
		
		return new RoomData(tiles, features, player, monsters);
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
