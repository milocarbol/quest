package loader;

import data.Images;
import entity.Feature;
import gui.GameWindow;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import level.Room;
import control.Game;

/**
 * Loads map layouts from files.
 * @author Milo
 * @since 8 April 2013
 */
public class RoomLoader {
	
	/** The deliminator for tiles in the file **/
	private static final String tileDeliminator = ":";
	
	/** The deliminator for sections in the file **/
	private static final String sectionDeliminator = "--";
	
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
		
		File file = new File(roomFile);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		int section = 0;
		int row = 0;
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.equals(sectionDeliminator)) {
				section++;
				row = 0;
			}
			else {
				String[] dataStrings = line.split(tileDeliminator);
				if (dataStrings.length != GameWindow.GRID_COLUMNS || row == GameWindow.GRID_ROWS) {
					reader.close();
					throw new IOException("Layout dimensions are incorrect for file " + roomFile);
				}
				else {
					switch(section) {
					case 0:
						for (int column = 0; column < GameWindow.GRID_COLUMNS; column++)
							tiles[column][row] = ImageLoader.loadImage(dataStrings[column]);
						break;
					case 1:
						for (int column = 0; column < GameWindow.GRID_COLUMNS; column++)
							if (!dataStrings[column].equals(Images.NULL_TILE))
								features[column][row] = new Feature(column, row, ImageLoader.loadImage(dataStrings[column]), null, game);
						break;
					}
					row++;
				}
			}
		}
		reader.close();
		
		return new RoomData(tiles, features);
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
					outWriter.print(tileDeliminator);
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
					outWriter.print(tileDeliminator);
			}
			outWriter.print(System.getProperty("line.separator"));
		}
		
		outWriter.close();
	}
}
