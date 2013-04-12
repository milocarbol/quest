package control;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import level.Room;
import loader.RoomLoader;
import entity.actor.Actor;
import entity.actor.Monster;
import entity.actor.Player;
import gui.GameWindow;

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
	private Actor[][] actors = new Actor[GameWindow.GRID_COLUMNS][GameWindow.GRID_ROWS];
	
	/** The controller for the actors **/
	private ActorController actorController;
	
	/** The current map **/
	private Room room;
	
	/** The layout loader used for loading maps **/
	private RoomLoader layoutLoader;
	
	/**
	 * Makes a new game and puts the player and monsters in place for testing.
	 */
	public Game() {
		this.layoutLoader = new RoomLoader();		
		this.room = new Room(layoutLoader, this);
		
		Point startLocation = room.getStartLocation();
		this.player = new Player(startLocation, this);
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
		monsters.add(new Monster(new Point(GameWindow.GRID_COLUMNS - 2, GameWindow.GRID_ROWS - 2), this));
		monsters.add(new Monster(new Point(1, GameWindow.GRID_ROWS - 2), this));
		monsters.add(new Monster(new Point(GameWindow.GRID_COLUMNS - 2, 1), this));
		return monsters;
	}
	
	/** ACCESSORS **/
	
	/** @return the actor list for animating. **/
	public Actor[][] getActors() { return actors; }
	
	/** @return the player **/
	public Player getPlayer() { return player; }
	
	/** @return The map for drawing **/
	public Room getMapLayout() { return room; }
	
	/**
	 * Fetches the actor on a space.
	 * @param column - The x-coordinate of the space to check
	 * @param row - The y-coordinate of the space to check
	 * @return the actor on the space, or null if there is no actor.
	 */
	public Actor getActor(int column, int row) {
		return actors[column][row];
	}
	
	/**
	 * Tests if a space is available to move to.
	 * @param column - The x-coordinate of the space.
	 * @param row - The y-coordinate of the space.
	 * @return True if the space is valid (i.e. not occupied and on the board), false otherwise.
	 */
	public boolean spaceIsFree(int column, int row) {
		boolean isFree =	column >= 0 && row >= 0 &&
							column < GameWindow.GRID_COLUMNS &&
							row < GameWindow.GRID_ROWS &&
							actors[column][row] == null &&
							room.featureAt(column, row) == null;
		
		return isFree;	
	}
	
	/**
	 * Checks if a space has an actor on it.
	 * @param column - The x-coordinate of the space to check
	 * @param row - The y-coordinate of the space to check
	 * @return true if the space has an actor on it, false otherwise
	 */
	public boolean spaceHasActor(int column, int row) {
		return	column >= 0 && row >= 0 &&
				column < GameWindow.GRID_ROWS &&
				row < GameWindow.GRID_COLUMNS &&
				actors[column][row] != null;
	}
	
	/**
	 * Checks if a given actor can attack another.
	 * @param target - the target of the attack
	 * @param source - the source of the attack
	 * @param range - the range of the attack
	 * @return whether or not the source can attack the target
	 */
	public boolean canAttack(Actor target, Actor source, int range) {
		Point	targetLocation = target.getLocation(),
				sourceLocation = source.getLocation();
		
		return	Math.abs(targetLocation.x - sourceLocation.x) <= range &&
				Math.abs(targetLocation.y - sourceLocation.y) <= range;
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
	 * @param column - The x-coordinate of the space
	 * @param row - The y-coordinate of the space
	 */
	public void clickGridSpace(int column, int row) {
		if (actors[column][row] == null) {
			player.moveTo(column, row);
			player.stopTargeting();
		}
		else if (actors[column][row] == player)
			; // TODO Healing spells or something could go here
		else if (actors[column][row] instanceof Monster)
			player.target(actors[column][row]);
	}
	
	/**
	 * Tells the player to move towards a grid space.
	 * Receives pixel coordinates and converts them to the right grid space.
	 * @param xPixelDestination - The x-pixel to head towards.
	 * @param yPixelDestination - The y-pixel to head towards.
	 */
	public void movePlayer(int column, int row) {
		player.moveTo(column, row);
	}
	
	/**
	 * Polls all actors and updates their positions in the actor grid according to their current positions.
	 */
	public void refreshActors() {		
		for (int row = 0; row < GameWindow.GRID_ROWS; row++)
			for (int column = 0; column < GameWindow.GRID_COLUMNS; column++) {
				Point playerLocation = player.getLocation();
				if (row == playerLocation.y && column == playerLocation.x)
					actors[column][row] = player;
				else {
					boolean isOccupiedByMonster = false;
					for (Monster monster : monsters) {
						Point monsterLocation = monster.getLocation();
						
						if (row == monsterLocation.y && column == monsterLocation.x) {
							actors[column][row] = monster;
							isOccupiedByMonster = true;
						}
					}
					if (!isOccupiedByMonster)
						actors[column][row] = null;
				}
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
