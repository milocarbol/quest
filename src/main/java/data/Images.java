package data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Images {
	/** Image file names **/
	public static final String	ACTIVE = "active",
								DEFAULT_MONSTER = "default_monster",
								DEFAULT_MONSTER_DEAD = "default_monster_dead",
								DEFAULT_PLAYER = "default_player",
								DEFAULT_PLAYER_DEAD = "default_player_dead",
								POWER_UNAVAILABLE = "power_unavailable",
								DEFAULT_POWER_MELEE = "default_power_melee",
								DEFAULT_POWER_RANGED = "default_power_ranged",
								WALL = "wall",
								FLOOR_STONE = "floor_stone",
								COLUMN = "column",
								WATER = "water";
	
	/** Number of variations **/
	public static final int		NUM_ACTIVE = 1,
								NUM_DEFAULT_MONSTER = 1,
								NUM_DEFAULT_MONSTER_DEAD = 1,
								NUM_DEFAULT_PLAYER = 1,
								NUM_DEFAULT_PLAYER_DEAD = 1,
								NUM_POWER_UNAVAILABLE = 1,
								NUM_DEFAULT_POWER_MELEE = 1,
								NUM_DEFAULT_POWER_RANGED = 1,
								NUM_WALL = 1,
								NUM_FLOOR_STONE = 3,
								NUM_COLUMN = 1,
								NUM_WATER = 2;
	
	/** Map of types to numbers of variations **/
	public static final Map<String, Integer> TYPE_COUNTS;
	
	/** Set of image types that are 'edged' **/
	public static final Set<String> EDGED;
	
	/** The file name for the null tile **/
	public static final String	NULL_TILE = "null";
	
	static {
		TYPE_COUNTS = new HashMap<String, Integer>();
		TYPE_COUNTS.put(ACTIVE, NUM_ACTIVE);
		TYPE_COUNTS.put(DEFAULT_MONSTER, NUM_DEFAULT_MONSTER);
		TYPE_COUNTS.put(DEFAULT_MONSTER_DEAD, NUM_DEFAULT_MONSTER_DEAD);
		TYPE_COUNTS.put(DEFAULT_PLAYER, NUM_DEFAULT_PLAYER);
		TYPE_COUNTS.put(DEFAULT_PLAYER_DEAD, NUM_DEFAULT_PLAYER_DEAD);
		TYPE_COUNTS.put(POWER_UNAVAILABLE, NUM_POWER_UNAVAILABLE);
		TYPE_COUNTS.put(DEFAULT_POWER_MELEE, NUM_DEFAULT_POWER_MELEE);
		TYPE_COUNTS.put(DEFAULT_POWER_RANGED, NUM_DEFAULT_POWER_RANGED);
		TYPE_COUNTS.put(WALL, NUM_WALL);
		TYPE_COUNTS.put(FLOOR_STONE, NUM_FLOOR_STONE);
		TYPE_COUNTS.put(COLUMN, NUM_COLUMN);
		TYPE_COUNTS.put(WATER, NUM_WATER);
		
		EDGED = new HashSet<String>();
		EDGED.add(WATER);
	}
}