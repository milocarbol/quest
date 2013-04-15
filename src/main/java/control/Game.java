package control;

import java.awt.Point;
import java.util.List;

import level.Room;
import loader.FeatureLoader;
import loader.MonsterLoader;
import loader.PlayerLoader;
import loader.PowerLoader;
import loader.RoomDataLoader;
import data.Rooms;
import entity.actor.Actor;
import entity.actor.Monster;
import entity.actor.Player;
import generator.ConstrainedWallGenerator;
import generator.IWallGenerator;
import generator.RoomGenerator;
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
	
	/**
	 * Makes a new game and puts the player and monsters in place for testing.
	 */
	public Game() {
		IWallGenerator wallGenerator = new ConstrainedWallGenerator();
		RoomDataLoader dataLoader = new RoomDataLoader();
		FeatureLoader featureLoader = new FeatureLoader(this);
		PowerLoader powerLoader = new PowerLoader();
		PlayerLoader playerLoader = new PlayerLoader(this, powerLoader);
		MonsterLoader monsterLoader = new MonsterLoader(this, powerLoader);
		this.room = new Room(new RoomGenerator(Rooms.DUNGEON, wallGenerator, dataLoader, featureLoader, playerLoader, monsterLoader), this);
		
		this.player = room.getPlayer();
		this.monsters = room.getMonsters();
		
		this.actorController = new ActorController(this)
									.withPlayer(player)
									.withMonsters(monsters);
		
		for (Monster monster : monsters)
			monster.begin();
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
	 * @param location - The coordinates of the space to check
	 * @return the actor on the space, or null if there is no actor.
	 */
	public Actor getActor(Point location) {
		return actors[location.x][location.y];
	}
	
	/**
	 * Tests if a space is available to move to.
	 * @param location - The coordinates of the space.
	 * @return True if the space is valid (i.e. not occupied and on the board), false otherwise.
	 */
	public boolean spaceIsFree(Point location) {
		return	spaceIsWalkable(location) &&
				actors[location.x][location.y] == null;
	}
	
	/**
	 * Checks if a space has an actor on it.
	 * @param location - The coordinates of the space to check
	 * @return true if the space has an actor on it, false otherwise
	 */
	public boolean spaceHasActor(Point location) {
		return	spaceIsValid(location) &&
				actors[location.x][location.y] != null;
	}
	
	/**
	 * Checks if a space has a feature on it.
	 * @param location - The coordinates of the space to check
	 * @return true if the space has a feature on it, false otherwise
	 */
	public boolean spaceHasFeature(Point location) {
		return	spaceIsValid(location) &&
				room.featureAt(location.x, location.y) != null;
	}
	
	/**
	 * Checks if a grid space is walkable for actors.
	 * @param location - The coordinates of the space to check
	 * @return True if actors are allowed on the space, false otherwise.
	 */
	public boolean spaceIsWalkable(Point location) {
		return	spaceIsValid(location) &&
				room.featureAt(location.x, location.y) == null;
	}
	
	/**
	 * Validates a grid space.
	 * @param location - The coordinates of the space to check
	 * @return True if the space is within the confines of the game board, false otherwise.
	 */
	public boolean spaceIsValid(Point location) {
		return	location.x >= 0 && location.x < GameWindow.GRID_COLUMNS &&
				location.y >= 0 && location.y < GameWindow.GRID_ROWS;
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
