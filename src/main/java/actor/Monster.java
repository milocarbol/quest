package actor;

import java.awt.event.ActionEvent;

import level.Game;

/**
 * Monsters generally fight the player.
 * @author Milo
 * @since 5 April 2013
 */
public class Monster extends Actor {

	/** The monster's default move speed, in milliseconds per grid space **/
	public static final int DEFAULT_MONSTER_SPEED = 400;
	
	/** The monster's default health **/
	public static final int DEFAULT_MONSTER_HEALTH = 20;
	
	/**
	 * Creates a new monster.
	 * @param x - The initial x-coordinate.
	 * @param y - The initial y-coordinate.
	 * @param game - The game this monster is a part of.
	 */
	public Monster(int x, int y, Game game) {
		super(x, y, DEFAULT_MONSTER_HEALTH, DEFAULT_MONSTER_SPEED, game);
	}
	
	/**
	 * When the monster's timer fires, refresh the actors on the screen.
	 * Then either attack the player if it is close enough, or move towards a destination.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		game.refreshActors();
		if (game.monsterCanAttackPlayer(this))
			game.getPlayer().attack(10);
		else
			move();
	}
}
