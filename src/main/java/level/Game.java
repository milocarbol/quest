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
	 * Checks if a space has an actor on it.
	 * @param x - The x-coordinate of the space to check
	 * @param y - The y-coordinate of the space to check
	 * @return true if the space has an actor on it, false otherwise
	 */
	public boolean spaceHasActor(int x, int y) {
		return	x >= 0 && y >= 0 &&
				x < Screen.GRID_X_SPACES &&
				y < Screen.GRID_Y_SPACES &&
				actors[x][y] != null;
	}
	
	/**
	 * Fetches the actor on a space.
	 * @param x - The x-coordinate of the space to check
	 * @param y - The y-coordinate of the space to check
	 * @return the actor on the space, or null if there is no actor.
	 */
	public Actor getActor(int x, int y) {
		return actors[x][y];
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
	public void movePlayer(int x, int y) {
		player.moveTo(x, y);
	}
	
	public void click(int x, int y) {
		if (actors[x][y] == null)
			player.moveTo(x, y);
		else if (actors[x][y] == player)
			; // TODO Healing spells or something could go here
		else if (actors[x][y] instanceof Monster)
			if (canAttack(actors[x][y], player, 1))
				actors[x][y].attack(player.getDamage());
			else
				player.moveTo(actors[x][y]);
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
	 * Checks if a given actor can attack another.
	 * @param target - the target of the attack
	 * @param source - the source of the attack
	 * @param range - the range of the attack
	 * @return whether or not the source can attack the target
	 */
	public boolean canAttack(Actor target, Actor source, int range) {
		return	Math.abs(target.getXPosition() - source.getXPosition()) <= range &&
				Math.abs(target.getYPosition() - source.getYPosition()) <= range;
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
	
	public static int pixelToGridSpace(int pixel) {
		return pixel/Screen.GRID_SPACE_SIZE;
	}
}
