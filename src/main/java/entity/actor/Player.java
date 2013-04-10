package entity.actor;

import io.ImageLoader;
import control.Game;


/**
 * The player of the game.
 * @author Milo
 * @since 5 April 2013
 */
public class Player extends Actor {

	/** Player's move speed in hundred-milliseconds per grid space. **/
	public static final int SPEED = 2;
	
	/** Player's maximum health. **/
	public static final int MAXIMUM_HEALTH = 100;
	
	/** Player's default damage for testing **/
	public static final int DEFAULT_DAMAGE = 20;
	
	/** The image file to draw while the player is alive **/
	public static final String DEFAULT_ALIVE_IMAGE = "default_player";
	
	/** The image file to draw while the player is dead **/
	public static final String DEFAULT_DEAD_IMAGE = "default_player_dead";
	
	/**
	 * Creates a new player.
	 * @param x - The initial x-coordinate of the player.
	 * @param y - The initial y-coordinate of the player.
	 * @param game - The game this player is a part of.
	 */
	public Player(int x, int y, Game game) {
		super(x, y, MAXIMUM_HEALTH, SPEED, DEFAULT_DAMAGE, ImageLoader.loadImage(DEFAULT_ALIVE_IMAGE), ImageLoader.loadImage(DEFAULT_DEAD_IMAGE), game);
	}
	
	/**
	 * Player doesn't require any initialisation.
	 */
	@Override
	public void begin() {
		
	}
}
