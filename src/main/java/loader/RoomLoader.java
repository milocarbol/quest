package loader;

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

/**
 * Loads map layouts from files.
 * @author Milo
 * @since 8 April 2013
 */
public class RoomLoader {
	
	/** The deliminator for tiles in the file **/
	private static final String separator = ":";
	
	/** The file name for wall tile 00 **/
	private static final String wall_00 = "wall_00";
	
	/** The file name for wall floor 00 **/
	private static final String floor_00 = "floor_00";
	
	/**
	 * Creates a map from a file.
	 * @param roomFile - The layout file name (must be a MAP file)
	 * @return The grid of tiles (images) to paint as the background.
	 * @throws IOException if the file can't be found or the layout doesn't match the standard dimensions.
	 */
	public Image[][] loadRoom(String roomFile) throws IOException {
		Image[][] tiles = new Image[GameWindow.GRID_ROWS][GameWindow.GRID_COLUMNS];
		
		File file = new File(roomFile);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		int row = 0;
		String line;
		while ((line = reader.readLine()) != null) {
			String[] tileStrings = line.split(separator);	
			if (tileStrings.length != GameWindow.GRID_COLUMNS || row == GameWindow.GRID_ROWS) {
				reader.close();
				throw new IOException("Layout dimensions are incorrect for file " + roomFile);
			}
			else
				for (int column = 0; column < GameWindow.GRID_COLUMNS; column++) {
					tiles[row][column] = ImageLoader.loadImage(tileStrings[column]);
				}
			row++;
		}
		
		reader.close();
		
		return tiles;
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
				if (row == 0 ||
					row == GameWindow.GRID_ROWS - 1 ||
					column == 0 ||
					column == GameWindow.GRID_COLUMNS - 1)
					outWriter.print(wall_00);
				else
					outWriter.print(floor_00);
				if (column < GameWindow.GRID_COLUMNS - 1)
					outWriter.print(separator);
			}
			outWriter.print(System.getProperty("line.separator"));
		}
		
		outWriter.close();
	}
}
