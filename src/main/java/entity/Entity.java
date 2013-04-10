package entity;

import java.awt.Image;

/**
 * An entity on the map. Entities block Actor movement and can interact.
 * @author Milo
 * @since 9 April 2013
 */
public class Entity {

	/** The image to render for this entity while it is active **/
	private Image activeImage;
	
	/** The image to render for this entity while it is inactive **/
	private Image inactiveImage;
	
	/** The image to render per this entity's current status **/
	private Image currentImage;
	
	/** This entity's current status **/
	private boolean isActive;
	
	/**
	 * Creates a new Entity.
	 * @param activeImage - The image to render for this entity while it is active
	 * @param inactiveImage - The image to render for this entity while it is inactive
	 * @param startsActive - If the entity begins the game active or not
	 */
	public Entity(Image activeImage, Image inactiveImage, boolean startsActive) {
		this.activeImage = activeImage;
		this.inactiveImage = inactiveImage;
		
		this.isActive = startsActive;
		if(startsActive)
			this.currentImage = activeImage;
		else
			this.currentImage = inactiveImage;
	}
	
	/**
	 * Deactivates the entity.
	 */
	public void deactivate() {
		isActive = false;
		currentImage = inactiveImage;
	}
	
	/**
	 * Activates the entity.
	 */
	public void activate() {
		isActive = true;
		currentImage = activeImage;
	}
	
	/** @return If this entity is active **/
	public boolean isActive() { return isActive; }
	
	/** @return The image to render for this entity **/
	public Image getImage() { return currentImage; }
}
