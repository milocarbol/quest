package actor;

import level.Game;

/**
 * Monsters generally fight the player.
 * @author Milo
 * @since 5 April 2013
 */
public class Monster extends Actor {

	/** The monster's default move speed, in milliseconds per grid space **/
	private static final int monsterMoveSpeed = 400;
	
	/**
	 * Creates a new monster.
	 * @param x - The initial x-coordinate.
	 * @param y - The initial y-coordinate.
	 * @param game - The game this monster is a part of.
	 */
	public Monster(int x, int y, Game game) {
		super(x, y, monsterMoveSpeed, game);
	}
}
