package gui;

import java.awt.Graphics;

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
	
	public abstract void draw(Graphics g);
}
