package generator;

import gui.GameWindow;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public class WaterGenerator implements IFeatureGenerator {

	public List<Point> generateFeatures(String featureType) {
		List<Point> waterSpaces = new LinkedList<Point>();
		waterSpaces.add(new Point(0, 0));
		waterSpaces.add(new Point(0, 1));
		waterSpaces.add(new Point(0, 2));
		waterSpaces.add(new Point(0, 3));
		waterSpaces.add(new Point(1, 0));
		waterSpaces.add(new Point(1, 1));
		waterSpaces.add(new Point(1, 2));
		waterSpaces.add(new Point(1, 3));
		waterSpaces.add(new Point(2, 0));
		waterSpaces.add(new Point(2, 1));
		waterSpaces.add(new Point(3, 0));
		waterSpaces.add(new Point(3, 1));
		
		waterSpaces.add(new Point(0, GameWindow.GRID_ROWS - 2));
		waterSpaces.add(new Point(0, GameWindow.GRID_ROWS - 1));
		waterSpaces.add(new Point(1, GameWindow.GRID_ROWS - 2));
		waterSpaces.add(new Point(1, GameWindow.GRID_ROWS - 1));
		
		waterSpaces.add(new Point(GameWindow.GRID_COLUMNS - 2, 0));
		waterSpaces.add(new Point(GameWindow.GRID_COLUMNS - 2, 1));
		waterSpaces.add(new Point(GameWindow.GRID_COLUMNS - 1, 0));
		waterSpaces.add(new Point(GameWindow.GRID_COLUMNS - 1, 1));

		waterSpaces.add(new Point(GameWindow.GRID_COLUMNS - 2, GameWindow.GRID_ROWS - 2));
		waterSpaces.add(new Point(GameWindow.GRID_COLUMNS - 2, GameWindow.GRID_ROWS - 1));
		waterSpaces.add(new Point(GameWindow.GRID_COLUMNS - 1, GameWindow.GRID_ROWS - 2));
		waterSpaces.add(new Point(GameWindow.GRID_COLUMNS - 1, GameWindow.GRID_ROWS - 1));
		
		return waterSpaces;
	}

}
