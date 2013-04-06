package actor;

import level.Game;

/**
 * The player of the game.
 * @author Milo
 * @since 5 April 2013
 */
public class Player extends Actor {

	/** Player's move speed. Milliseconds per grid space. **/
	private static final int playerMoveSpeed = 200;
	
	/** Player's maximum health. **/
	private static final int maximumHealth = 100;
	
	/**
	 * Creates a new player.
	 * @param x - The initial x-coordinate of the player.
	 * @param y - The initial y-coordinate of the player.
	 * @param game - The game this player is a part of.
	 */
	public Player(int x, int y, Game game) {
		super(x, y, maximumHealth, playerMoveSpeed, game);
	}
}
