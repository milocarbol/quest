package entity.actor;

import java.awt.Point;

import loader.ImageLoader;
import control.Game;
import control.PowerController;
import data.Images;
import entity.actor.pathfinder.DirectPathfinder;


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
	
	/**
	 * Creates a new player.
	 * @param x - The initial x-coordinate of the player.
	 * @param y - The initial y-coordinate of the player.
	 * @param game - The game this player is a part of.
	 */
	public Player(Point startLocation, Game game) {
		super(startLocation, MAXIMUM_HEALTH, SPEED, new DirectPathfinder(game, startLocation), new PowerController(true), ImageLoader.loadImage(Images.PLAYER_DEFAULT_ALIVE_IMAGE), ImageLoader.loadImage(Images.PLAYER_DEFAULT_DEAD_IMAGE), game);
	}
	
	/**
	 * Player doesn't require any initialisation.
	 */
	@Override
	public void begin() {
		
	}
}
