package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import control.Game;
import control.InputHandler;

/**
 * Information bar showing all of the player's stats, abilities, etc.
 * @author Milo
 * @since 6 April 2013
 */
public class InfoBar extends JComponent {
	
	/** Default serial version UID **/
	private static final long serialVersionUID = 1L;
	
	/** The game this bar acts on. **/
	private final Game game;
	
	/** The handler for any input to the bar **/
	private final InputHandler inputHandler;
	
	/** Dimensions of the bar **/
	public static final int	WIDTH = 1000,
							HEIGHT = 100;
	
	/** X- and Y-coordinates of the top-left corner of the bar **/
	private final int x, y;
	
	/**
	 * Creates a new InfoBar.
	 * @param x - The x-coordinate of the top-left corner
	 * @param y - The y-coordinate of the top-left corner
	 * @param game - The game to interact with
	 * @param inputHandler - The input handler to handle input
	 */
	public InfoBar(int x, int y, Game game, InputHandler inputHandler) {
		this.x = x;
		this.y = y;
		
		this.game = game;
		this.inputHandler = inputHandler;
		
		this.setSize(WIDTH, HEIGHT);
	}
	
	/**
	 * Computes the absolute x-coordinate for a given relative x-coordinate.
	 * @param x - The relative coordinate.
	 * @return the absolute x-coordinate on the game screen.
	 */
	private int absoluteX(int x) { return this.x + x; }
	
	/**
	 * Computes the absolute y-coordinate for a given relative y-coordinate.
	 * @param x - The relative coordinate.
	 * @return the absolute y-coordinate on the game screen.
	 */
	private int absoluteY(int y) { return this.y + y; }
	
	/**
	 * Draws all graphics.
	 */
	public void paint(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(absoluteX(0), absoluteY(0), absoluteX(WIDTH), absoluteY(HEIGHT));
		
		int health = game.getPlayer().getCurrentHealth();
		g.setColor(Color.red);
		g.fillRect(absoluteX(20), absoluteY(20), health, 20);
		g.setColor(Color.white);
		g.fillRect(absoluteX(20 + health), absoluteY(20), game.getPlayer().getMaximumHealth() - health, 20);
	}
}
