package loader;

import java.util.Map;

import data.Files;

/**
 * Loads room tile/feature data from the data file.
 * @author Milo Carbol
 * @since 14 April 2013
 */
public class RoomDataLoader extends DataLoader {

	/**
	 * Creates a new RoomDataLoader object.
	 */
	public RoomDataLoader() {
		super(Files.ROOM_DATA);
	}
	
	/**
	 * Gets the data for the room's tiles/features from the file.
	 * @param roomType - The name of the room
	 * @return A map of keys to data entries corresponding to the room's data
	 */
	public Map<String, String> loadRoomData(String roomType) {
		return data.get(roomType);
	}
}
