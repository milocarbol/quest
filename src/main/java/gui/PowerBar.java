package gui;

import java.awt.Color;
import java.awt.Graphics;

import control.PowerController;

/**
 * GUI for controlling powers.
 * @author Milo Carbol
 * @since 10 April 2013
 */
public class PowerBar extends InfoComponent {
	
	/** X-and y-dimensions for the images **/
	public static int IMAGE_SIZE = 50;
	
	/** Spacing between images **/
	public static int IMAGE_SPACING = 10;
	
	/** The power controller to interact with **/
	private PowerController powerController;
	
	/**
	 * Creates a new Power Bar
	 * @param x - The x-coordinate of the top left pixel of the bar
	 * @param y - The y-coordinate of the top left pixel of the bar
	 * @param powerController - The power controller to interact with
	 */
	public PowerBar(int x, int y, PowerController powerController) {
		super(x, y, powerController.getNumberOfPowers() * (IMAGE_SIZE + IMAGE_SPACING) + IMAGE_SPACING, IMAGE_SIZE + 2 * IMAGE_SPACING);
		this.powerController = powerController;
	}
	
	/**
	 * Draws the power bar.
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
		
		for (int index = 0; index < powerController.getNumberOfPowers(); index++) {
			g.drawImage(powerController.getPowerImage(index), x + IMAGE_SPACING + index * (IMAGE_SIZE + IMAGE_SPACING), y + IMAGE_SPACING, IMAGE_SIZE, IMAGE_SIZE, null);
		}
	}
}
