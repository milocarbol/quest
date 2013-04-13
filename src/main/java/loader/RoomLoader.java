package loader;

import data.Images;
import entity.Feature;
import entity.actor.Monster;
import entity.actor.power.Power;
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
	
	/** The deliminator for subsections in the file **/
	private static final String subsectionDeliminator = "---";
	
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
		Feature[][] features = new Feature[GameWindow.GRID_COLUMNS][GameWindow.GRID_ROWS];
		Point playerStartLocation = null;
		List<Monster> monsters = new LinkedList<Monster>();
		
		PowerLoader powerLoader = new PowerLoader();
		MonsterLoader monsterLoader = new MonsterLoader(game, powerLoader);
		
		File file = new File(roomFile);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
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
						if (!featureStrings[column].equals(Images.NULL_TILE))
							features[column][row] = new Feature(column, row, ImageLoader.loadImage(featureStrings[column]), null, game);
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
					String monsterText = "";
					do {
						monsterText += line + System.getProperty("line.separator");
					} while ((line = reader.readLine()) != null && !line.equals(subsectionDeliminator));
					monsters.add(monsterLoader.loadMonster(monsterText));
				} while ((line = reader.readLine()) != null && !line.equals(sectionDeliminator));
				section++;
				break;
			}
		}
		reader.close();
		
		return new RoomData(tiles, features, playerStartLocation, monsters);
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
				if (row == 0 ||
						row == GameWindow.GRID_ROWS - 1 ||
						column == 0 ||
						column == GameWindow.GRID_COLUMNS - 1 ||
						(row > 5 && row < GameWindow.GRID_ROWS - 5 && column == GameWindow.GRID_COLUMNS / 2))
					outWriter.print(Images.WALL);
				else if (row % 7 == 0 && column % 9 == 0)
					outWriter.print(Images.COLUMN);
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
		outWriter.print(subsectionDeliminator + System.getProperty("line.separator"));
		printTestMonster(GameWindow.GRID_COLUMNS - 2, 1, outWriter);
		outWriter.print(subsectionDeliminator + System.getProperty("line.separator"));
		printTestMonster(1, GameWindow.GRID_ROWS - 2, outWriter);
		
		outWriter.close();
	}
	
	/**
	 * Prints a test monster to the test map file.
	 * @param column - The column the monster starts at
	 * @param row - The row the monster starts at
	 * @param outWriter - The writer to write to
	 */
	private static void printTestMonster(int column, int row, PrintWriter outWriter) {
		outWriter.print(column + inLineDeliminator + row + inLineDeliminator);
		outWriter.print(Monster.DEFAULT_HEALTH + inLineDeliminator);
		outWriter.print(Monster.DEFAULT_MONSTER_SPEED + inLineDeliminator);
		outWriter.print(Images.MONSTER_DEFAULT_ALIVE_IMAGE + inLineDeliminator);
		outWriter.print(Images.MONSTER_DEFAULT_DEAD_IMAGE);
		outWriter.print(System.getProperty("line.separator"));
		outWriter.print("default" + inLineDeliminator);
		outWriter.print(Power.DEFAULT_MELEE_IMAGE + inLineDeliminator);
		outWriter.print(1 + inLineDeliminator);
		outWriter.print(0 + inLineDeliminator);
		outWriter.print(Power.DEFAULT_DAMAGE);
		outWriter.print(System.getProperty("line.separator"));
	}
}
