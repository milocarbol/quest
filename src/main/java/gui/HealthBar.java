package gui;

import java.awt.Color;
import java.awt.Graphics;

import entity.actor.Player;

/**
 * The health bar displays how much health the player has left before they die.
 * @author Milo Carbol
 * @since 10 April 2013
 */
public class HealthBar extends InfoComponent {

	/** Dimensions of the health bar **/
	public static final int WIDTH = 100, HEIGHT = 20;
	
	/** The player to reference **/
	private final Player player;
	
	/** The colors to draw the parts of the health bar **/
	private final Color healthColor = Color.red, damagedColor = Color.white;
	
	/**
	 * Creates a new health bar at a given point.
	 * @param x - The x-coordinate of the top left pixel of the health bar
	 * @param y - The y-coordinate of the top left pixel of the health bar
	 * @param player - The player to reference
	 */
	public HealthBar(int x, int y, Player player) {
		super(x, y, WIDTH, HEIGHT);
		this.player = player;
	}
	
	/** @return The width of the full part of the health bar **/
	public int computeWidthOfHealth() { return (int)(1.0 * player.getCurrentHealth() / player.getMaximumHealth() * width); }
	
	/**
	 * Draws the health bar.
	 */
	@Override
	public void draw(Graphics g) {
		int widthOfHealth = computeWidthOfHealth();
		
		g.setColor(healthColor);
		g.fillRect(x, y, widthOfHealth, height);
		g.setColor(damagedColor);
		g.fillRect(x + widthOfHealth, y, width - widthOfHealth, height);
	}
}
