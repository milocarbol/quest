package entity;

import java.awt.Image;

import control.Game;
import entity.actor.Actor;

/**
 * Interactive features of a room, like a treasure chest.
 * @author Milo
 *
 */
public class Feature extends Entity {

	/**
	 * Creates a new Feature.
	 * @param x - The x-coordinate of the Feature.
	 * @param y - The y-coordinate of the Feature.
	 * @param intactImage - The image to render when the feature is intact.
	 * @param destroyedImage - The image to render when the feature is destroyed.
	 * @param game - The game to interact with.
	 */
	public Feature(int x, int y, Image intactImage, Image destroyedImage, Game game) {
		super(intactImage, destroyedImage, true);
	}
	
	/**
	 * Interacts with an actor. Should be overridden by subclass.
	 * @param target - The actor who is being interacted with.
	 */
	public void interactWith(Actor target) {
		
	}
}
