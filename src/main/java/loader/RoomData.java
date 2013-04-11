package loader;

import java.awt.Image;

import entity.Feature;

/**
 * Data structure containing room data (floor tiles, features, etc.).
 * @author Milo Carbol
 * @since 11 April 2013
 */
public class RoomData {

	/** The tiles in the room **/
	public final Image[][] tiles;
	
	/** The features in the room **/
	public final Feature[][] features;
	
	/**
	 * Creates a new set of room data.
	 * @param tiles - The tiles in the room
	 * @param features - The features in the room.
	 */
	public RoomData(Image[][] tiles, Feature[][] features) {
		this.tiles = tiles;
		this.features = features;
	}
}
