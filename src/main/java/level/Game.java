package level;

import gui.Screen;

import java.util.LinkedList;
import java.util.List;

import controller.ActorController;

import actor.Actor;
import actor.Monster;
import actor.Player;

/**
 * Data regarding dungeon layout, player position, etc.
 * @author Milo
 * @since 5 April 2013
 */
public class Game {
	
	/** The player **/
	private Player player;
	
	/** A monster for testing. TODO Make this a list of monsters. **/
	private List<Monster> monsters;
	
	/** Grid of actor positions. TODO make this cleaner **/
	private Actor[][] actors = new Actor[Screen.GRID_X_SPACES][Screen.GRID_Y_SPACES];
	
	/** The controller for the actors **/
	private ActorController actorController;
	
	/**
	 * Makes a new game and puts the player and monster in place for testing.
	 */
	public Game() {
		this.player = new Player(0, 0, this);
		this.monsters = makeMonsters();
		
		this.actorController = new ActorController(this)
									.withPlayer(player)
									.withMonsters(monsters);
	}
	
	/**
	 * @return the actor list for animating.
	 */
	public Actor[][] getActors() { return actors; }
	
	/**
	 * @return the player
	 */
	public Player getPlayer() { return player; }
	
	/**
	 * Tests if a space is available to move to.
	 * @param x - The x-coordinate of the space.
	 * @param y - The y-coordinate of the space.
	 * @return True if the space is valid (i.e. not occupied and on the board), false otherwise.
	 */
	public boolean spaceIsFree(int x, int y) {
		boolean isFree =	x >= 0 && y >= 0 &&
							x < Screen.GRID_X_SPACES &&
							y < Screen.GRID_Y_SPACES &&
							actors[x][y] == null;
		
		return isFree;
				
	}
	
	/**
	 * Builds the initial list of monsters in the room.
	 * TODO This is only for development purposes.
	 * @return the list of test monsters.
	 */
	private List<Monster> makeMonsters() {
		monsters = new LinkedList<Monster>();
		monsters.add(new Monster(Screen.GRID_X_SPACES - 1, Screen.GRID_Y_SPACES - 1, this));
		monsters.add(new Monster(Screen.GRID_X_SPACES - 1, 0, this));
		monsters.add(new Monster(0, Screen.GRID_Y_SPACES - 1, this));
		return monsters;
	}
	
	/**
	 * Tells the player to move towards a grid space.
	 * Receives pixel coordinates and converts them to the right grid space.
	 * @param xPixelDestination - The x-pixel to head towards.
	 * @param yPixelDestination - The y-pixel to head towards.
	 */
	public void movePlayer(int xPixelDestination, int yPixelDestination) {
		player.moveTo(xPixelDestination/Screen.GRID_SPACE_SIZE, yPixelDestination/Screen.GRID_SPACE_SIZE);
	}
	
	/**
	 * Polls all actors and updates their positions in the actor grid according to their current positions.
	 */
	public void refreshActors() {
		for (Monster monster : monsters)
			monster.moveTo(player);
		
		for (int row = 0; row < actors.length; row++)
			for (int column = 0; column < actors[row].length; column++)
				if (row == player.getXPosition() && column == player.getYPosition())
					actors[row][column] = player;
				else {
					boolean isOccupiedByMonster = false;
					for (Monster monster : monsters) {
						if (row == monster.getXPosition() && column == monster.getYPosition()) {
							actors[row][column] = monster;
							isOccupiedByMonster = true;
						}
					}
					if (!isOccupiedByMonster)
						actors[row][column] = null;
				}
	}
	
	/**
	 * Checks if a given monster can attack the player.
	 * @param monster - The given monster
	 * @return whether or not the monster can attack the player.
	 */
	public boolean monsterCanAttackPlayer(Monster monster) {
		return	Math.abs(monster.getXPosition() - player.getXPosition()) <= 1 &&
				Math.abs(monster.getYPosition() - player.getYPosition()) <= 1;
	}
	
	/**
	 * Pauses the game
	 */
	public void pause() {
		actorController.pause();
	}
	
	/**
	 * Resumes the game
	 */
	public void resume() {
		actorController.resume();
	}
}
