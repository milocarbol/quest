package generator;

import gui.GameWindow;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

/**
 * Generates a semi-random layout of rooms
 * @author Milo Carbol
 * @since 15 April 2013
 */
public class ConstrainedWallGenerator implements IWallGenerator {
	
	/** Layout patterns **/
	private static enum Layout { BORDER_PASSAGE, NO_BORDER_PASSAGE };
	
	/** Number of layouts available (randomizer will select a layout less than this index) **/
	private static final int numberOfLayouts = 1;
	
	/** Constraints **/
	private static final int	minimumRoomWidth = 6,
								maximumRoomWidth = GameWindow.GRID_COLUMNS - 2,
								minimumRoomHeight = 4,
								maximumRoomHeight = GameWindow.GRID_ROWS / 2,
								minimumSpacing = 2;
		
	/** Percentage chance for a room or a connector to be generated **/
	private double percentChanceToCreateRoom = 0.8;
	
	/**
	 * Generates the walls using a specified type.
	 * @param wallType - The type of wall to use
	 * @return The list of wall locations
	 */
	public List<Point> generateWalls(String wallType) {
		Layout layout = randomizePattern();
		switch (layout) {
		case BORDER_PASSAGE:
			return generateBorderPassageLayout();
		case NO_BORDER_PASSAGE:
			return generateNoBorderPassageLayout();
		}
		return new LinkedList<Point>();
	}

	/**
	 * Generates walls using the "Border Passage" layout.
	 * The "Border Passage" layout has rooms in the centre and a clear path around the border.
	 * @return The list of wall locations
	 */
	private List<Point> generateBorderPassageLayout() {
		int minimumBorderSize = 2;
		List<Point> walls = generateRooms(minimumBorderSize);
		
		walls = new WallCleaner(walls).cleanWalls();
		
		for (int row = 0; row < GameWindow.GRID_ROWS; row++)
			for (int column = 0; column < GameWindow.GRID_COLUMNS; column++)
				if (row == 0 || row == GameWindow.GRID_ROWS - 1 ||
					column == 0 || column == GameWindow.GRID_COLUMNS - 1)
					walls.add(new Point(column, row));
		
		return walls;
	}
	
	/**
	 * Generates walls using the "No Border Passage" layout.
	 * The "No Border Passage" layout is allowed to have rooms touching the border of the window.
	 * @return The list of wall locations
	 */
	private List<Point> generateNoBorderPassageLayout() {
		return new LinkedList<Point>();
	}
	
	private List<Point> generateRooms(int borderSize) {
		List<Point> walls = new LinkedList<Point>();
		
		int roomWidth = RandomNumber.randomBetween(minimumRoomWidth, maximumRoomWidth - 2 * borderSize);
		int roomHeight = RandomNumber.randomBetween(minimumRoomHeight, maximumRoomHeight - 2 * borderSize);
		
		int[] horizontalSpacings = generateSpacings(GameWindow.GRID_COLUMNS - 2 * borderSize, roomWidth);
		int[] verticalSpacings = generateSpacings(GameWindow.GRID_ROWS - 2 * borderSize, roomHeight);
		
		for (int horizontalRoom = 0; horizontalRoom < horizontalSpacings.length - 1; horizontalRoom++)
			for (int verticalRoom = 0; verticalRoom < verticalSpacings.length - 1; verticalRoom++) {
				int leftColumn = borderSize + sumTo(horizontalRoom, horizontalSpacings) + roomWidth * horizontalRoom;
				int topRow = borderSize + sumTo(verticalRoom, verticalSpacings) + roomHeight * verticalRoom;
				int horizontalConnectorWidth, verticalConnectorHeight;
				if (horizontalRoom < horizontalSpacings.length - 2)
					horizontalConnectorWidth = horizontalSpacings[horizontalRoom + 1];
				else
					horizontalConnectorWidth = 0;
				if (verticalRoom < verticalSpacings.length - 2)
					verticalConnectorHeight = verticalSpacings[verticalRoom + 1];
				else
					verticalConnectorHeight = 0;
				
				walls.addAll(generateRoom(leftColumn, topRow, roomWidth, roomHeight, horizontalConnectorWidth, verticalConnectorHeight));
			}
		
		return walls;
	}
	
	/**
	 * Generates a room with connectors sprouting off to the right and below.
	 * @param leftColumn - The x-coordinate of the left side
	 * @param topRow - The y-coordinate of the top
	 * @param width - The width of the room
	 * @param height - The height of the room
	 * @param horizontalConnectorWidth - The width of the right-connector
	 * @param verticalConnectorHeight - The width of the below-connector
	 * @return A list of wall locations
	 */
	private List<Point> generateRoom(int leftColumn, int topRow, int width, int height, int horizontalConnectorWidth, int verticalConnectorHeight) {
		List<Point> walls = new LinkedList<Point>();
		if (Math.random() < percentChanceToCreateRoom) {
			for (int column = 0; column < width; column++)
				for (int row = 0; row < height; row++)
					if (column == 0 || column == width - 1 || row == 0 || row == height - 1)
						walls.add(new Point(leftColumn + column, topRow + row));
		}
		
		if (Math.random() < percentChanceToCreateRoom) {
			int row = topRow + height / 2;
			for (int connectorColumn = 0; connectorColumn < horizontalConnectorWidth; connectorColumn++)
				walls.add(new Point(leftColumn + width + connectorColumn, row));
		}
		
		if (Math.random() < percentChanceToCreateRoom) {
			int column = leftColumn + width / 2;
			for (int connectorRow = 0; connectorRow < verticalConnectorHeight; connectorRow++)
				walls.add(new Point(column, topRow + height + connectorRow));
		}
		
		return walls;
	}
	
	/**
	 * Computes the spacing between each room for an axis.
	 * @param totalSpace - The total space available to fill with rooms
	 * @param roomSize - The size of the rooms on this axis
	 * @return An array one index larger than the number of rooms, containing all the spacings before each room (and after in the case of the last room).
	 */
	private int[] generateSpacings(int totalSpace, int roomSize) {
		int numberOfRooms = RandomNumber.randomBetween(1, computeMaximumNumberOfRooms(roomSize, totalSpace));
		int numberOfSpacings = numberOfRooms + 1;
		int[] spacings = new int[numberOfSpacings];
		
		int totalSpacing = totalSpace - numberOfRooms * roomSize;
		int spacing = totalSpacing / numberOfSpacings;
		int remainder = totalSpacing % numberOfSpacings;
		
		for (int i = 0; i < numberOfSpacings; i++)
			spacings[i] = spacing;
		
		int i = 0;
		while (remainder > 0) {
			int up = (int)Math.round(1.0 * (numberOfSpacings - 1) / 2) + i;
			int down = (numberOfSpacings - 1) / 2 - i;
			if (up < numberOfSpacings) {
				spacings[up]++;
				remainder--;
			}
			if (remainder > 0 && down >= 0) {
				spacings[down]++;
				remainder--;
			}
			if (up < numberOfSpacings && down >= 0)
				i++;
			else
				i = 0;
		}
		
		return spacings;
	}
	
	/**
	 * Computes the maximum number of rooms that will fit in the given space.
	 * @param roomSize - The size of the rooms on this axis
	 * @param totalSpace - The total space available to fill
	 * @return The maximum number of rooms that will fit (i.e. when the spacing is equal to ConstrainedWallGenerator.minimumSpacing)
	 */
	private int computeMaximumNumberOfRooms(int roomSize, int totalSpace) {
		int number = 1;
		while (number * (roomSize + minimumSpacing) + minimumSpacing < totalSpace) {
			number++;
		}
		return number;
	}
	
	/**
	 * Selects a random pattern to follow.
	 * @return The chosen pattern.
	 */
	private Layout randomizePattern() {
		int i = RandomNumber.randomBetween(0, numberOfLayouts);
		switch (i) {
		case 0:
			return Layout.BORDER_PASSAGE;
		case 1:
			return Layout.NO_BORDER_PASSAGE;
		}
		return null;
	}
	
	/**
	 * Sums the values in an array up to the specified index (inclusive).
	 * @param to - The index
	 * @param values - The array
	 * @return The sum of all values up to and including that index
	 */
	private int sumTo(int to, int[] values) {
		int sum = 0;
		for (int i = 0; i <= to; i++)
			sum += values[i];
		return sum;
	}
}
