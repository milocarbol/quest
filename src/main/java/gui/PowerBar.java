package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

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
		
		for (int index = 0; index < powerController.getNumberOfPowers(); index++)
			g.drawImage(powerController.getPowerImage(index), x + IMAGE_SPACING + index * (IMAGE_SIZE + IMAGE_SPACING), y + IMAGE_SPACING, IMAGE_SIZE, IMAGE_SIZE, null);
	}
	
	/**
	 * Handles a click
	 */
	public void click(MouseEvent click) {
		for (int index = 0; index < powerController.getNumberOfPowers(); index++)
			if (powerIsClicked(index, click))
				powerController.selectPower(index);
	}
	
	private boolean powerIsClicked(int index, MouseEvent click) {
		int clickX = click.getX();
		int clickY = click.getY();
		
		return	clickX > powerX(index) && clickX < powerX(index) + IMAGE_SIZE &&
				clickY > powerY() && clickY < powerY() + IMAGE_SIZE;
	}
	
	/**
	 * Computes the x-coordinate for a power
	 * @param index - The index of the power
	 * @return The x-coordinate of the power
	 */
	private int powerX(int index) {
		return x + IMAGE_SPACING + index * (IMAGE_SIZE + IMAGE_SPACING);
	}
	
	/**
	 * Computes the y-coordinate for a power
	 * @return The y-coordinate of the power
	 */
	private int powerY() {
		return y + IMAGE_SPACING;
	}
}
