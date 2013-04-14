package entity.actor;

import java.awt.Point;
import java.util.List;

import loader.ImageLoader;
import control.Game;
import control.PowerController;
import entity.actor.pathfinder.DirectPathfinder;
import entity.actor.power.Power;


/**
 * The player of the game.
 * @author Milo
 * @since 5 April 2013
 */
public class Player extends Actor {
	
	/**
	 * Creates a new player.
	 * @param startLocation - The start location in this room
	 * @param health - The player's maximum health
	 * @param speed - The player's speed, in milliseconds per grid space
	 * @param powers - The player's powers
	 * @param aliveImage - The image to render when the player is alive
	 * @param deadImage - The image to render when the player is dead
	 * @param game - The game this player is part of
	 */
	public Player(Point startLocation, int health, int speed, List<Power> powers, String aliveImage, String deadImage, Game game) {
		super(startLocation, health, speed, new DirectPathfinder(game, startLocation), new PowerController(powers), ImageLoader.loadImage(aliveImage), ImageLoader.loadImage(deadImage), game);
	}
	
	/**
	 * Player doesn't require any initialisation.
	 * @deprecated
	 */
	@Override
	public void begin() {
		
	}
}
