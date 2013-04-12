package entity.actor;

import java.awt.Point;

import loader.ImageLoader;
import control.Game;
import control.PowerController;
import entity.actor.pathfinder.DirectPathfinder;


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
	
	/** The image file to draw when a monster is alive **/
	public static final String DEFAULT_ALIVE_IMAGE = "default_monster";
	
	/** The image file to draw when a monster is dead **/
	public static final String DEFAULT_DEAD_IMAGE = "default_monster_dead";
	
	/**
	 * Creates a new monster.
	 * @param x - The initial x-coordinate.
	 * @param y - The initial y-coordinate.
	 * @param game - The game this monster is a part of.
	 */
	public Monster(Point startLocation, Game game) {
		super(startLocation, DEFAULT_HEALTH, DEFAULT_MONSTER_SPEED, new DirectPathfinder(game, startLocation), new PowerController(), ImageLoader.loadImage(DEFAULT_ALIVE_IMAGE), ImageLoader.loadImage(DEFAULT_DEAD_IMAGE), game);
	}
	
	/**
	 * Target the player.
	 */
	@Override
	public void begin() {
		target(game.getPlayer());
	}
}
