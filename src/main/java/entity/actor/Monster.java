package entity.actor;

import java.awt.Point;

import loader.ImageLoader;
import control.Game;
import control.PowerController;
import entity.actor.pathfinder.astar.AStarPathfinder;


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
	
	/**
	 * Creates a new monster.
	 * @param startLocation - The point at which to start the monster
	 * @param health - The health of the monster
 	 * @param speed - The monster's speed, in hundred-milliseconds per grid space
	 * @param aliveImage - The image to render when the monster is alive
	 * @param deadImage - The image to render when the monster is dead
	 * @param game - The game the monster is for
	 */
	public Monster(Point startLocation, int health, int speed, String aliveImage, String deadImage, Game game) {
		super(startLocation, health, speed, new AStarPathfinder(game, startLocation), new PowerController(), ImageLoader.loadImage(aliveImage), ImageLoader.loadImage(deadImage), game);
	}
	
	/**
	 * Target the player.
	 */
	@Override
	public void begin() {
		target(game.getPlayer());
	}
}
