package entity.actor;

import java.awt.Color;

import control.Game;


/**
 * Monsters generally fight the player.
 * @author Milo
 * @since 5 April 2013
 */
public class Monster extends Actor {

	/** The monster's default act speed, in hundred-milliseconds per grid space **/
	public static final int DEFAULT_MONSTER_SPEED = 4;
	
	/** The monster's default health **/
	public static final int DEFAULT_HEALTH = 20;
	
	/** The monster's default damage **/
	public static final int DEFAULT_DAMAGE = 10;
	
	/** The color to draw monsters when they're alive **/
	public static final Color DEFAULT_ALIVE_COLOR = Color.red;
	
	/** The color to draw monsters when they're dead **/
	public static final Color DEFAULT_DEAD_COLOR = Color.DARK_GRAY;
	
	/**
	 * Creates a new monster.
	 * @param x - The initial x-coordinate.
	 * @param y - The initial y-coordinate.
	 * @param game - The game this monster is a part of.
	 */
	public Monster(int x, int y, Game game) {
		super(x, y, DEFAULT_HEALTH, DEFAULT_MONSTER_SPEED, DEFAULT_DAMAGE, DEFAULT_ALIVE_COLOR, DEFAULT_DEAD_COLOR, game);
	}
	
	/**
	 * Either attacks the player if it is close enough, or move towards a destination.
	 */
	@Override
	public void act() {
		if (game.canAttack(game.getPlayer(), this, 1))
			game.getPlayer().attack(getDamage());
		else {
			moveTo(game.getPlayer());
			super.act();
		}
	}
}
