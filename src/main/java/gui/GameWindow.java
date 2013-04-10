package gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import level.Room;
import control.Game;
import control.InputHandler;
import entity.actor.Actor;

/**
 * The part of the screen which renders the gameplay on which to animate.
 * @author Milo
 * @since 5 April 2013
 */
public class GameWindow extends JComponent implements KeyListener, MouseListener, MouseMotionListener {
	
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
	public static final int GRID_ROWS = WIDTH / GRID_SPACE_SIZE,
							GRID_COLUMNS = HEIGHT / GRID_SPACE_SIZE;
	
	/**
	 * Creates a new Screen and starts the painting timer.
	 */
	public GameWindow(Game game, InputHandler inputHandler) {
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
		Room layout = game.getMapLayout();
		for (int row = 0; row < GRID_ROWS; row++)
			for (int column = 0; column < GRID_COLUMNS; column++) {
				g.drawImage(layout.tileAt(row, column), row*GRID_SPACE_SIZE, column*GRID_SPACE_SIZE, GRID_SPACE_SIZE, GRID_SPACE_SIZE, this);
			}
		
		Actor[][] actors = game.getActors();
		for (int row = 0; row < GRID_ROWS; row++)
			for (int column = 0; column < GRID_COLUMNS; column++) {
				Actor actor = actors[row][column];
				if (actor != null) {
					g.drawImage(actor.getImage(), row*GRID_SPACE_SIZE, column*GRID_SPACE_SIZE, GRID_SPACE_SIZE, GRID_SPACE_SIZE, this);
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