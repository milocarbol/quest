package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import level.Game;
import actor.Actor;
import actor.Monster;
import actor.Player;
import controller.InputHandler;

/**
 * The screen on which to animate.
 * @author Milo
 * @since 5 April 2013
 */
public class Screen extends JComponent implements KeyListener, MouseListener, MouseMotionListener {
	
	/** Default Serial Version ID **/
	private static final long serialVersionUID = 1L;
	
	/** The game this interacts with **/
	private final Game game;
	
	/** The input handler we're using **/
	private final InputHandler inputHandler;
	
	/** Width and height values **/
	public static final int	WIDTH = 1000,
							HEIGHT = 560;
	
	/** The x- and y-dimensions of a single grid space, in pixels **/
	public static final int GRID_SPACE_SIZE = 20;
	
	/** The x- and y-dimensions of the grid, in grid spaces **/
	public static final int GRID_X_SPACES = WIDTH / GRID_SPACE_SIZE,
							GRID_Y_SPACES = HEIGHT / GRID_SPACE_SIZE;
	
	/**
	 * Creates a new Screen and starts the painting timer.
	 */
	public Screen(Game game, InputHandler inputHandler) {
		this.game = game;
		this.inputHandler = inputHandler;
    	
		this.setSize(WIDTH, HEIGHT);
		
    	addKeyListener(this);
    	addMouseListener(this);
    	addMouseMotionListener(this);
	}
	
	/**
	 * Animates.
	 */
	public void paint(Graphics g) {
		g.setColor(Color.black);
		for (int i = GRID_SPACE_SIZE; i <= WIDTH; i += GRID_SPACE_SIZE)
			g.drawLine(i, 0, i, HEIGHT);
		for (int i = GRID_SPACE_SIZE; i <= HEIGHT; i +=  GRID_SPACE_SIZE)
			g.drawLine(0, i, WIDTH, i);
			
		Actor[][] actors = game.getActors();
		for (int row = 0; row < actors.length; row++)
			for (int column = 0; column < actors[row].length; column++) {
				Actor actor = actors[row][column];
				if (actor != null) {
					if (actor instanceof Player) {
						g.setColor(Color.black);
						g.fillRect(row*GRID_SPACE_SIZE + 1, column*GRID_SPACE_SIZE + 1, GRID_SPACE_SIZE - 1, GRID_SPACE_SIZE - 1);
					}
					else if (actor instanceof Monster) {
						g.setColor(Color.red);
						g.fillRect(row*GRID_SPACE_SIZE + 1, column*GRID_SPACE_SIZE + 1, GRID_SPACE_SIZE - 1, GRID_SPACE_SIZE - 1);
					}
				}
			}
	}
	
	/**
	 * Listens for a key press.
	 */
	public void keyPressed(KeyEvent key) {
		inputHandler.act(key);
	}

	/**
	 * Listens for a mouse click.
	 */
	public void mouseClicked(MouseEvent click) {
		inputHandler.act(click);
	}
	
	/**
	 * Listens for a mouse drag.
	 */
	public void mouseDragged(MouseEvent click) {
		inputHandler.act(click);
	}
	
	/**
	 * Listens for a mouse release.
	 */
	public void mouseReleased(MouseEvent click) {
		inputHandler.act(click);
	}
	
	/** Does nothing **/
	public void keyReleased(KeyEvent key) {}
	/** Does nothing **/
	public void keyTyped(KeyEvent key) {}
	/** Does nothing **/
	public void mousePressed(MouseEvent click) {}
	/** Does nothing **/
	public void mouseEntered(MouseEvent click) {}
	/** Does nothing **/
	public void mouseExited(MouseEvent click) {}
	/** Does nothing **/
	public void mouseMoved(MouseEvent arg0) {}
}