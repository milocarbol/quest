package gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * Components like health bars drawn on the player's info bar.
 * @author Milo Carbol
 * @since 10 April 2013
 */
public abstract class InfoComponent {
	
	/** Dimensions of the component **/
	protected final int width, height;
	
	/** Coordinates of the top left corner of the component **/
	protected final int x, y;
	
	/**
	 * Creates a new InfoComponent
	 * @param x - The x-coordinate of the top left pixel
	 * @param y - The y-coordinate of the top left pixel
	 * @param width - The width of the component
	 * @param height - The height of the component
	 */
	public InfoComponent(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Checks if a click intersects the component.
	 * @param click - The click event
	 * @return True if it intersects, false if not.
	 */
	public boolean isClickedOn(MouseEvent click) {
		int clickX = click.getX();
		int clickY = click.getY();
		
		return
				clickX > x && clickX < x + width &&
				clickY > y && clickY < y + height;
	}
	
	/**
	 * Draws the component
	 * @param g - The graphics object to draw with
	 */
	public abstract void draw(Graphics g);
	
	/**
	 * Handles a click
	 * @param click - The click event
	 */
	public abstract void click(MouseEvent click);
}
