package entity.actor;

import java.awt.Color;

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
	
	/** The color to draw the player while they're alive **/
	public static final Color DEFAULT_ALIVE_COLOR = Color.cyan;
	
	/** The color to draw the player while they're dead **/
	public static final Color DEFAULT_DEAD_COLOR = Color.blue;
	
	/**
	 * Creates a new player.
	 * @param x - The initial x-coordinate of the player.
	 * @param y - The initial y-coordinate of the player.
	 * @param game - The game this player is a part of.
	 */
	public Player(int x, int y, Game game) {
		super(x, y, MAXIMUM_HEALTH, SPEED, DEFAULT_DAMAGE, DEFAULT_ALIVE_COLOR, DEFAULT_DEAD_COLOR, game);
	}
}
