package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

import level.Game;
import controller.InputHandler;

/**
 * Main class.
 * @author Milo
 * @since 5 April 2013
 */
public class QuestUI extends JFrame implements ActionListener {

	/** Default Serial Version ID **/
	private static final long serialVersionUID = 1L;
	
	/** Title **/
	private static final String title = "Quest";
	
	/** The components that make up the UI **/
	private final List<JComponent> components = new LinkedList<JComponent>();
	
	/** How frequently to repaint the screen **/
	private static final int refreshPeriod = 100;
	
	/** The timer to repaint the screen **/
	Timer animationTimer = new Timer(refreshPeriod, this);
	
	/**
	 * Creates a new QuestUI.
	 */
	public QuestUI(Game game) {
		this.setTitle(title);
    	this.setLocation(40, 40);
    	this.setResizable(false);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
		this.getContentPane().setPreferredSize(
								this.addComponentsUsing(game, new InputHandler(game)));
		this.pack();
		this.setVisible(true);
		
		animationTimer.start();
	}
	
	/**
	 * Adds components like the game window and info bar to the main screen.
	 * @param game - The game this is for.
	 * @param inputHandler - The input handler we're using.
	 * @return the required dimensions of the content screen.
	 */
	private Dimension addComponentsUsing(Game game, InputHandler inputHandler) {
		GameWindow screen = new GameWindow(game, inputHandler);
		screen.setBounds(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);
		
		InfoBar infoBar = new InfoBar(0, GameWindow.HEIGHT, game, inputHandler);
		infoBar.setBounds(0, GameWindow.HEIGHT, InfoBar.WIDTH, InfoBar.HEIGHT);
		
		components.add(screen);
		components.add(infoBar);
		
		for (JComponent component : components) {
			this.add(component);
		}
		
		return new Dimension(
						GameWindow.WIDTH,
						GameWindow.HEIGHT + InfoBar.HEIGHT
					);
	}
	
	/**
	 * When the animation timer is fired, animate.
	 */
	public void actionPerformed(ActionEvent action) {
		for (JComponent component : components)
			component.repaint();
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
