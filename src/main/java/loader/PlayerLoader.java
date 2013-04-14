package loader;

import java.awt.Point;
import java.util.List;

import control.Game;
import data.Files;
import entity.actor.Player;
import entity.actor.power.Power;

/**
 * Loads the player from a data file.
 * @author Milo Carbol
 * @since 13 April 2013
 */
public class PlayerLoader extends DataLoader {

	/** The loader for the player's powers **/
	private PowerLoader powerLoader;
	
	/** The game the player is part of **/
	private Game game;
	
	/**
	 * Creates a new player loader.
	 * @param game - The game the player is part of.
	 * @param powerLoader - The loader for the player's powers.
	 */
	public PlayerLoader(Game game, PowerLoader powerLoader) {
		super(Files.PLAYER);
		this.game = game;
		this.powerLoader = powerLoader;
	}
	
	/**
	 * Loads the player.
	 * @param startLocation - The location to start the player at.
	 * @return - The player object.
	 */
	public Player loadPlayer(Point startLocation) {
		int health = Integer.parseInt(get("health"));
		int speed = Integer.parseInt(get("speed"));
		String alive = get("alive");
		String dead = get("dead");
		List<Power> powers = powerLoader.loadPowers(get("powers"));
		
		return new Player(startLocation, health, speed, powers, alive, dead, game);
	}
	
	/**
	 * Gets player data from the map.
	 * @param key - The key.
	 * @return - The entry corresponding to the key.
	 */
	private String get(String key) {
		return data.get("Player").get(key);
	}
}
