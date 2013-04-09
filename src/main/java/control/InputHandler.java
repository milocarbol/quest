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
	 * Creates a new KeyHandler.
	 * @param game - The game to control.
	 */
	public InputHandler(Game game) {
		this.game = game;
	}
	
	/**
	 * Handles a key event.
	 * @param key - The key event to handle.
	 */
	public void act(KeyEvent key) {
		
	}
	
	/**
	 * Handles a mouse click.
	 * @param click - The click event to handle.
	 */
	public void act(MouseEvent click) {
		int x = Game.pixelToGridSpace(click.getX());
		int y = Game.pixelToGridSpace(click.getY());
		
		game.click(x, y);
	}
}