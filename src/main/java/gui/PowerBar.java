package gui;

import io.ImageLoader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;

import control.PowerController;

/**
 * GUI for controlling powers.
 * @author Milo Carbol
 * @since 10 April 2013
 */
public class PowerBar extends InfoComponent {
	
	private static String activeEmblemSource = "active";
	
	/** X-and y-dimensions for the images **/
	private static int imageSize = 50;
	
	/** X-and y-dimensions for the active indicator **/
	private static int emblemSize = 15;
	
	/** X- and y-offsets for the emblem **/
	private static int	emblemOffsetHorizontal = 20,
						emblemOffsetVertical = 10;
	
	/** Spacing between images **/
	private static int imageSpacing = 10;
	
	/** The power controller to interact with **/
	private PowerController powerController;
	
	/** The image to render on an active power **/
	private Image activeEmblem = ImageLoader.loadImage(activeEmblemSource);
	
	/**
	 * Creates a new Power Bar
	 * @param x - The x-coordinate of the top left pixel of the bar
	 * @param y - The y-coordinate of the top left pixel of the bar
	 * @param powerController - The power controller to interact with
	 */
	public PowerBar(int x, int y, PowerController powerController) {
		super(x, y, powerController.getNumberOfPowers() * (imageSize + imageSpacing) + imageSpacing, imageSize + 2 * imageSpacing);
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
			g.drawImage(powerController.getPowerImage(index), powerX(index), powerY(), imageSize, imageSize, null);
		
		int activeIndex = powerController.getIndexOfActivePower();
		g.drawImage(activeEmblem, powerX(activeIndex) + emblemOffsetHorizontal, powerY() + emblemOffsetVertical, emblemSize, emblemSize, null);
	}
	
	/**
	 * Handles a click
	 */
	public void click(MouseEvent click) {
		for (int index = 0; index < powerController.getNumberOfPowers(); index++)
			if (powerIsClicked(index, click))
				powerController.selectPower(index);
	}
	
	/**
	 * Checks if a power is clicked.
	 * @param index - The index of the power
	 * @param click - The click event
	 * @return True if the click is within the bounds of that power, false otherwise.
	 */
	private boolean powerIsClicked(int index, MouseEvent click) {
		int clickX = click.getX();
		int clickY = click.getY();
		
		return	clickX > powerX(index) && clickX < powerX(index) + imageSize &&
				clickY > powerY() && clickY < powerY() + imageSize;
	}
	
	/**
	 * Computes the x-coordinate for a power
	 * @param index - The index of the power
	 * @return The x-coordinate of the power
	 */
	private int powerX(int index) {
		return x + imageSpacing + index * (imageSize + imageSpacing);
	}
	
	/**
	 * Computes the y-coordinate for a power
	 * @return The y-coordinate of the power
	 */
	private int powerY() {
		return y + imageSpacing;
	}
}
