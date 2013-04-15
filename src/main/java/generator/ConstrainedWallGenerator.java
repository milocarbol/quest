package generator;

import gui.GameWindow;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Generates a semi-random layout of rooms
 * @author Milo Carbol
 * @since 15 April 2013
 */
public class ConstrainedWallGenerator implements IWallGenerator {

	private static final String SPACING = "spacing",
								SIZE = "size";
	
	/** Layout patterns **/
	private static enum Layout { BORDER_PASSAGE, NO_BORDER_PASSAGE };
	
	/** Number of layouts available (randomizer will select a layout less than this index) **/
	private static final int numberOfLayouts = 1;
	
	/** Constraints **/
	private static final int	minimumBorderPassageWidth = 4,
								maximumBorderPassageWidth = 8,
								minimumRoomSize = 6,
								maximumRoomSize = 10,
								minimumSpacing = 2,
								maximumSpacing = 20;
			
	private double percentChanceToCreateRoom = 0.75;
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
	 * TODO - Add doors to rooms. Add more randomness.
	 * @return The list of wall locations
	 */
	private List<Point> generateBorderPassageLayout() {
		List<Point> walls = new LinkedList<Point>();
		
		for (int row = 0; row < GameWindow.GRID_ROWS; row++)
			for (int column = 0; column < GameWindow.GRID_COLUMNS; column++)
				if (row == 0 || row == GameWindow.GRID_ROWS - 1 ||
					column == 0 || column == GameWindow.GRID_COLUMNS - 1)
					walls.add(new Point(column, row));
		
		Map<String, int[]> horizontalData = generateAxisRoomData(GameWindow.GRID_COLUMNS);
		int[] horizontalSpacings = horizontalData.get(SPACING);
		int[] widths = horizontalData.get(SIZE);
		
		Map<String, int[]> verticalData = generateAxisRoomData(GameWindow.GRID_ROWS);
		int[] verticalSpacings = verticalData.get(SPACING);
		int[] heights = verticalData.get(SIZE);
		
		for (int horizontalRoomsMade = 0; horizontalRoomsMade < horizontalSpacings.length; horizontalRoomsMade++) {
			
			for (int verticalRoomsMade = 0; verticalRoomsMade < verticalSpacings.length; verticalRoomsMade++) {
			
				
				if (Math.random() < percentChanceToCreateRoom) {
					int verticalOffset = sumTo(verticalRoomsMade, verticalSpacings);
					int topRow = 1 + verticalRoomsMade * heights[verticalRoomsMade] + verticalOffset;
					int bottomRow = (verticalRoomsMade + 1) * heights[verticalRoomsMade] + verticalOffset;
					
					for (int row = topRow; row <= bottomRow; row++) {
						
						int horizontalOffset = sumTo(horizontalRoomsMade, horizontalSpacings);
						int leftColumn = 1 + horizontalRoomsMade * widths[horizontalRoomsMade] + horizontalOffset;
						int rightColumn = (horizontalRoomsMade + 1) * widths[horizontalRoomsMade] + horizontalOffset;
						
						for (int column = leftColumn; column <= rightColumn; column++) {
							if (row == topRow || row == bottomRow ||
								column == leftColumn || column == rightColumn)
								walls.add(new Point(column, row));
						}
					}
				}
			}
		}
		
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
	
	/**
	 * Generates room data for one axis for a bordered layout.
	 * @param windowSize - The size of the window on that axis
	 * @return A map containing the room widths and the spacings between the rooms
	 */
	private Map<String, int[]> generateAxisRoomData(int windowSize) {
		Map<String, int[]> roomData = new HashMap<String, int[]>();
		int[] roomSpacings;
		int[] roomWidths;
		
		int border = RandomNumber.randomBetween(minimumBorderPassageWidth, maximumBorderPassageWidth);
		int remainingSpace = windowSize - 2 - border * 2;
		
		int size = RandomNumber.randomBetween(minimumRoomSize, maximumRoomSize);
		
		int minimumNumberOfRooms = remainingSpace / (size + maximumSpacing);
		if (minimumNumberOfRooms == 0)
			minimumNumberOfRooms = 1;
		int maximumNumberOfRooms = remainingSpace / (size + minimumSpacing);
		int numberOfRooms = RandomNumber.randomBetween(minimumNumberOfRooms, maximumNumberOfRooms + 1);
		
		int numberOfSpacings = numberOfRooms - 1;
		roomSpacings = new int[numberOfRooms];
		roomWidths = new int[numberOfRooms];
		
		roomSpacings[0] = border;
		
		if (numberOfRooms == 1)
			size = remainingSpace;
		else {
			int spaceToAllocate = remainingSpace - size * numberOfRooms;
			int spacing = spaceToAllocate / numberOfSpacings;
			int remainder = spaceToAllocate % numberOfSpacings;
			for (int i = 0; i < numberOfSpacings; i++)
				roomSpacings[i + 1] = spacing;
			
			int i = 0;
			while (remainder > 0) {
				int up = (int)Math.round(1.0 * (numberOfSpacings - 1) / 2) + i;
				int down = (numberOfSpacings - 1) / 2 - i;
				if (up < numberOfSpacings) {
					roomSpacings[up + 1]++;
					remainder--;
				}
				if (remainder > 0 && down >= 0) {
					roomSpacings[down + 1]++;
					remainder--;
				}
				if (up < numberOfSpacings && down >= 0)
					i++;
				else
					i = 0;
			}
		}
		
		for (int i = 0; i < numberOfRooms; i++)
			roomWidths[i] = size;
		
		roomData.put(SPACING, roomSpacings);
		roomData.put(SIZE, roomWidths);
		
		return roomData;
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
