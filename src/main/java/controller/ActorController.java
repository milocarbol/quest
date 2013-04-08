package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Timer;

import level.Game;
import actor.Monster;
import actor.Player;

/**
 * Controller class to trigger actor events.
 * @author Milo
 * @since 7 April 2013
 */
public class ActorController implements ActionListener {

	/** The player to control **/
	private Player player;
	
	/** The monsters to control **/
	private List<Monster> monsters = new LinkedList<Monster>();
	
	/** The game to interact with **/
	private final Game game;
	
	/**
	 * The base time unit to tick, in milliseconds.
	 * Actor speeds are in multiples of this.
	 **/
	private final int rate = 100;
	
	/** The timer to trigger actor events **/
	private final Timer actorTimer = new Timer(rate, this);
	
	/** If the game is paused **/
	public boolean paused = false;
	
	/**
	 * Incremented every time the timer fires and triggers the correct actors.
	 * In theory this could overflow if the game is left on for a very long time (i.e. 6 years at the current timer rate).
	 **/
	private int tickCount = 0;
	
	/**
	 * Creates a new actor controller and starts the timer.
	 * @param game - The game to interact with.
	 */
	public ActorController(Game game) {
		this.game = game;
		
		actorTimer.start();
	}
	
	/**
	 * Sets the player.
	 * @param player - The player to control
	 * @return this instance
	 */
	public ActorController withPlayer(Player player) {
		this.player = player;
		return this;
	}
	
	/**
	 * Adds monsters to the control list
	 * @param monsters - Monsters to control
	 * @return this instance
	 */
	public ActorController withMonsters(Collection<Monster> monsters) {
		this.monsters.addAll(monsters);
		return this;
	}
	
	/**
	 * Refreshes actor positions and checks if any actors should act.
	 */
	public void actionPerformed(ActionEvent event) {
		if (!paused) {
			if (tickCount % player.getSpeed() == 0) {
				player.act();
				game.refreshActors();
			}
			
			for (Monster monster : monsters) {
				if (tickCount % monster.getSpeed() == 0) {
					monster.act();
					game.refreshActors();
				}
			}
			tickCount++;
		}
	}

	/**
	 * Prevents actors from acting until game is resumed.
	 * @see resume()
	 */
	public void pause() {
		paused = true;
	}
	
	/**
	 * Allows actors to act.
	 * @see pause()
	 */
	public void resume() {
		paused = false;
	}
	
}
