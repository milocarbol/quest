package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
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
	
	/** The place to draw the targeter **/
	private Point targeter = new Point();
	
	/** Width and height values **/
	public static final int	WIDTH = 1000,
							HEIGHT = 560;
	
	/** The x- and y-dimensions of a single grid space, in pixels **/
	public static final int GRID_SPACE_SIZE = 20;
	
	/** The x- and y-dimensions of the grid, in grid spaces **/
	public static final int GRID_COLUMNS = WIDTH / GRID_SPACE_SIZE,
							GRID_ROWS = HEIGHT / GRID_SPACE_SIZE;
	
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
				g.drawImage(layout.tileAt(column, row), column*GRID_SPACE_SIZE, row*GRID_SPACE_SIZE, GRID_SPACE_SIZE, GRID_SPACE_SIZE, this);
				Image feature = layout.featureImageAt(column, row);
				if (feature != null)
					g.drawImage(feature, column*GRID_SPACE_SIZE, row*GRID_SPACE_SIZE, GRID_SPACE_SIZE, GRID_SPACE_SIZE, this);
			}
		
		Actor[][] actors = game.getActors();
		for (int row = 0; row < GRID_ROWS; row++)
			for (int column = 0; column < GRID_COLUMNS; column++) {
				Actor actor = actors[column][row];
				if (actor != null) {
					g.drawImage(actor.getImage(), column*GRID_SPACE_SIZE, row*GRID_SPACE_SIZE, GRID_SPACE_SIZE, GRID_SPACE_SIZE, this);
				}
			}
		
		g.setColor(Color.cyan);
		g.drawRect(targeter.x*GRID_SPACE_SIZE, targeter.y*GRID_SPACE_SIZE, GRID_SPACE_SIZE, GRID_SPACE_SIZE);
	}
	
	/**
	 * Listens for a key press.
	 */
	public void keyPressed(KeyEvent key) {
		inputHandler.handle(key);
	}

	/**
	 * Listens for a mouse click.
	 */
	public void mouseClicked(MouseEvent click) {
		inputHandler.handle(click);
	}
	
	/**
	 * Listens for a mouse drag.
	 */
	public void mouseDragged(MouseEvent click) {
		if(positionIsValid(click)) {
			inputHandler.handle(click);
			setTargeter(click);
		}
	}
	
	/**
	 * Listens for a mouse release.
	 */
	public void mouseReleased(MouseEvent click) {
		if(positionIsValid(click))
			inputHandler.handle(click);
	}
	
	/** When the mouse is moved, adjust the targeter **/
	public void mouseMoved(MouseEvent move) {
		if(positionIsValid(move))
			setTargeter(move);
	}
	
	/**
	 * Validates that the click we're handling is within the game window.
	 * If it isn't, we dismiss the click.
	 * @param click - The click to handle
	 * @return True if the click is within the game window, false otherwise
	 */
	private boolean positionIsValid(MouseEvent click) {
		int column = click.getX();
		int row = click.getY();
		
		return	column >= 0 && column < WIDTH &&
				row >= 0 && row < HEIGHT;
	}
	
	/**
	 * Sets the targeter to the position of the mouse pointer.
	 * @param pixelPosition - The position of the mouse pointer.
	 */
	private void setTargeter(MouseEvent pixelPosition) {
		targeter.x = Game.pixelToGridSpace(pixelPosition.getX());
		targeter.y = Game.pixelToGridSpace(pixelPosition.getY());
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
}