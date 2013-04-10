package control;

import entity.actor.Actor;
import entity.actor.Monster;
import entity.actor.Player;
import gui.GameWindow;
import io.RoomLoader;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import level.Room;

/**
 * Data regarding dungeon layout, player position, etc.
 * @author Milo
 * @since 5 April 2013
 */
public class Game {
	
	/** The player **/
	private Player player;
	
	/** The monsters in the current game window **/
	private List<Monster> monsters;
	
	/** Grid of actor positions. **/
	private Actor[][] actors = new Actor[GameWindow.GRID_ROWS][GameWindow.GRID_COLUMNS];
	
	/** The controller for the actors **/
	private ActorController actorController;
	
	/** The current map **/
	private Room map;
	
	/** The layout loader used for loading maps **/
	private RoomLoader layoutLoader;
	
	/**
	 * Makes a new game and puts the player and monsters in place for testing.
	 */
	public Game() {
		this.layoutLoader = new RoomLoader();		
		this.map = new Room(layoutLoader);
		
		Point startLocation = map.getStartLocation();
		this.player = new Player(startLocation.x, startLocation.y, this);
		this.monsters = makeMonsters();
		
		this.actorController = new ActorController(this)
									.withPlayer(player)
									.withMonsters(monsters);
		
		this.player.begin();
		for (Monster monster : monsters)
			monster.begin();
	}
	
	/**
	 * Builds the initial list of monsters in the room.
	 * TODO This is only for development/testing purposes.
	 * @return the list of test monsters.
	 */
	private List<Monster> makeMonsters() {
		monsters = new LinkedList<Monster>();
		monsters.add(new Monster(GameWindow.GRID_ROWS - 1, GameWindow.GRID_COLUMNS - 1, this));
		monsters.add(new Monster(GameWindow.GRID_ROWS - 1, 0, this));
		monsters.add(new Monster(0, GameWindow.GRID_COLUMNS - 1, this));
		return monsters;
	}
	
	/** ACCESSORS **/
	
	/** @return the actor list for animating. **/
	public Actor[][] getActors() { return actors; }
	
	/** @return the player **/
	public Player getPlayer() { return player; }
	
	/** @return The map for drawing **/
	public Room getMapLayout() { return map; }
	
	/**
	 * Fetches the actor on a space.
	 * @param x - The x-coordinate of the space to check
	 * @param y - The y-coordinate of the space to check
	 * @return the actor on the space, or null if there is no actor.
	 */
	public Actor getActor(int x, int y) {
		return actors[x][y];
	}
	
	/** CONTROL **/
	
	/**
	 * Tests if a space is available to move to.
	 * @param x - The x-coordinate of the space.
	 * @param y - The y-coordinate of the space.
	 * @return True if the space is valid (i.e. not occupied and on the board), false otherwise.
	 */
	public boolean spaceIsFree(int x, int y) {
		boolean isFree =	x >= 0 && y >= 0 &&
							x < GameWindow.GRID_ROWS &&
							y < GameWindow.GRID_COLUMNS &&
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
				x < GameWindow.GRID_ROWS &&
				y < GameWindow.GRID_COLUMNS &&
				actors[x][y] != null;
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
	
	/** INTERACTION **/
	
	/**
	 * Handles clicking on a grid space.
	 * If the space is empty, move there.
	 * If the space is occupied by the player, do nothing.
	 * If the space is occupied by a monster, attack it or move towards it.
	 * @param x - The x-coordinate of the space
	 * @param y - The y-coordinate of the space
	 */
	public void clickGridSpace(int x, int y) {
		if (actors[x][y] == null) {
			player.moveTo(x, y);
			player.stopTargeting();
		}
		else if (actors[x][y] == player)
			; // TODO Healing spells or something could go here
		else if (actors[x][y] instanceof Monster)
			player.target(actors[x][y]);
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
	
	/**
	 * Polls all actors and updates their positions in the actor grid according to their current positions.
	 */
	public void refreshActors() {		
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
	 * Converts a pixel coordinate into a grid index.
	 * @param pixel - The pixel coordinate
	 * @return The corresponding grid index, based on GameWindow.GRID_SPACE_SIZE
	 */
	public static int pixelToGridSpace(int pixel) {
		return pixel/GameWindow.GRID_SPACE_SIZE;
	}
}
