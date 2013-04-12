package control;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


/**
 * Handles all key presses.
 * @author Milo
 * @since 5 April 2013
 */
public class InputHandler {

	/** The game to control **/
	private final Game game;
	
	/**
	 * Creates a new InputHandler.
	 * @param game - The game to control.
	 */
	public InputHandler(Game game) {
		this.game = game;
	}
	
	/**
	 * Handles a key event.
	 * @param key - The key event to handle.
	 */
	public void handle(KeyEvent key) {
		
	}
	
	/**
	 * Handles a mouse click.
	 * @param click - The click event to handle.
	 */
	public void handle(MouseEvent click) {
		int column = Game.pixelToGridSpace(click.getX());
		int row = Game.pixelToGridSpace(click.getY());
		
		game.clickGridSpace(column, row);
	}
}
