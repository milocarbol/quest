package data;

import java.util.HashMap;
import java.util.Map;

public class Images {
	public static final String	WALL = "wall",
								FLOOR_STONE = "floor_stone",
								COLUMN = "column";
	
	public static final int		NUM_WALL = 1,
								NUM_FLOOR_STONE = 3,
								NUM_COLUMN = 1;
	
	public static final Map<String, Integer> TYPE_COUNTS;
	
	/** The file name for the null tile **/
	public static final String	NULL_TILE = "null";
	
	/** The image file to draw when a monster is alive **/
	public static final String MONSTER_DEFAULT_ALIVE_IMAGE = "default_monster";
	
	/** The image file to draw when a monster is dead **/
	public static final String MONSTER_DEFAULT_DEAD_IMAGE = "default_monster_dead";
	
	/** The image file to draw while the player is alive **/
	public static final String PLAYER_DEFAULT_ALIVE_IMAGE = "default_player";
	
	/** The image file to draw while the player is dead **/
	public static final String PLAYER_DEFAULT_DEAD_IMAGE = "default_player_dead";
	
	static {
		TYPE_COUNTS = new HashMap<String, Integer>();
		
		TYPE_COUNTS.put(WALL, NUM_WALL);
		TYPE_COUNTS.put(FLOOR_STONE, NUM_FLOOR_STONE);
		TYPE_COUNTS.put(COLUMN, NUM_COLUMN);
	}
}