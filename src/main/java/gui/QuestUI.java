package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

import level.Game;
import actor.Actor;
import actor.Monster;
import actor.Player;
import controller.InputHandler;

/**
 * Main class.
 * @author Milo
 * @since 5 April 2013
 */
public class QuestUI extends JFrame {

	/** Default Serial Version ID **/
	private static final long serialVersionUID = 1L;

	/** Width and height values **/
	public static final int	WIDTH = 1000,
							HEIGHT = 600;
	
	/** The x- and y- dimensions of a single grid space, in pixels **/
	public static final int GRID_SPACE_SIZE = 20;
	
	public static final int GRID_X_SPACES = WIDTH / GRID_SPACE_SIZE,
							GRID_Y_SPACES = HEIGHT / GRID_SPACE_SIZE;
	
	/** Title **/
	private static final String title = "Quest";
	
	/** The game this screen will display **/
	private final Game game;
	
	private final InputHandler inputHandler;
	
	/**
	 * Creates a new QuestUI.
	 */
	public QuestUI(Game game) {
		this.game = game;
		this.inputHandler = new InputHandler(game);
		
		this.setTitle(title);
    	this.setVisible(true);
    	this.setLocation(40, 40);
    	this.setResizable(false);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
		this.add(new Renderer(), BorderLayout.CENTER);
		this.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.pack();
	}
	
	/**
	 * The screen on which to animate.
	 * @author Milo
	 * @since 5 April 2013
	 */
	private class Renderer extends JComponent implements ActionListener, KeyListener, MouseListener {
		
		/** Default Serial Version ID **/
		private static final long serialVersionUID = 1L;

		private static final int refreshPeriod = 100;
		
		Timer animationTimer = new Timer(refreshPeriod, this);
		
		/**
		 * Creates a new Renderer and starts the painting timer.
		 */
		public Renderer() {
			animationTimer.start();
	    	
			this.setSize(QuestUI.WIDTH, QuestUI.HEIGHT);
			
	    	addKeyListener(this);
	    	addMouseListener(this);
		}
		
		/**
		 * Animates.
		 */
		public void paint(Graphics g) {
			g.setColor(Color.black);
			for (int i = GRID_SPACE_SIZE; i <= QuestUI.WIDTH; i += GRID_SPACE_SIZE)
				g.drawLine(i, 0, i, QuestUI.HEIGHT);
			for (int i = GRID_SPACE_SIZE; i <= QuestUI.HEIGHT; i += GRID_SPACE_SIZE)
				g.drawLine(0, i, QuestUI.WIDTH, i);
				
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
		 * When the animation timer is fired, refresh actor positions and animate.
		 */
		public void actionPerformed(ActionEvent action) {
			game.refreshActors();
			repaint();
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
		
		/** Does nothing **/
		public void keyReleased(KeyEvent key) {}
		/** Does nothing **/
		public void keyTyped(KeyEvent key) {}
		/** Does nothing **/
		public void mouseReleased(MouseEvent click) {}
		/** Does nothing **/
		public void mouseEntered(MouseEvent click) {}
		/** Does nothing **/
		public void mouseExited(MouseEvent click) {}
		/** Does nothing **/
		public void mousePressed(MouseEvent click) {}
	}
	
	/**
	 * Main method.
	 * TODO Make this a legitimate main method rather than this temporary one.
	 * @param args - Command line arguments. Unused.
	 */
	public static void main(String[] args) {
		new QuestUI(new Game());
	}
}
