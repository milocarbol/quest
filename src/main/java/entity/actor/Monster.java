package entity.actor;

import java.awt.Point;
import java.util.List;

import loader.ImageLoader;
import control.Game;
import control.PowerController;
import entity.actor.pathfinder.astar.AStarPathfinder;
import entity.actor.power.Power;


/**
 * Monsters generally fight the player.
 * @author Milo
 * @since 5 April 2013
 */
public class Monster extends Actor {
	
	/**
	 * Creates a new monster.
	 * @param startLocation - The point at which to start the monster
	 * @param health - The health of the monster
 	 * @param speed - The monster's speed, in hundred-milliseconds per grid space
 	 * @param powers - The monster's powers
	 * @param aliveImage - The image to render when the monster is alive
	 * @param deadImage - The image to render when the monster is dead
	 * @param game - The game the monster is for
	 */
	public Monster(Point startLocation, int health, int speed, List<Power> powers, String aliveImage, String deadImage, Game game) {
		super(startLocation, health, speed, new AStarPathfinder(game, startLocation), new PowerController(powers), ImageLoader.loadImage(aliveImage), ImageLoader.loadImage(deadImage), game);
	}
	
	/**
	 * Target the player.
	 */
	@Override
	public void begin() {
		target(game.getPlayer());
	}
}
