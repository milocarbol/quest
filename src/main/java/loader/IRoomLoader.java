package loader;

import java.io.IOException;

/**
 * Loads or generates rooms.
 * @author Milo Carbol
 * @since 14 April 2013
 */
public interface IRoomLoader {

	/**
	 * Loads the room data.
	 * @return - The room data
	 * @throws IOException If any files can't be accessed.
	 */
	public RoomData loadRoom() throws IOException;
}
