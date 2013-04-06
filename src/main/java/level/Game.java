package level;

import gui.QuestUI;
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
	private Monster monster;
	
	/** Grid of actor positions. TODO make this cleaner **/
	private Actor[][] actors = new Actor[QuestUI.GRID_X_SPACES][QuestUI.GRID_Y_SPACES];
	
	/**
	 * Makes a new game and puts the player and monster in place for testing.
	 */
	public Game() {
		this.player = new Player(0, 0, this);
		this.monster = new Monster(QuestUI.GRID_X_SPACES - 1, QuestUI.GRID_Y_SPACES - 1, this);
		actors[player.getXPosition()][player.getYPosition()] = player;
		actors[monster.getXPosition()][monster.getYPosition()] = monster;
	}
	
	/**
	 * @return the actor list for animating.
	 */
	public Actor[][] getActors() { return actors; }
	
	/**
	 * Tests if a space is available to move to.
	 * @param x - The x-coordinate of the space.
	 * @param y - The y-coordinate of the space.
	 * @return True if the space is valid (i.e. not occupied and on the board), false otherwise.
	 */
	public boolean spaceIsFree(int x, int y) {
		return
				x >= 0 && y >= 0 &&
				x < QuestUI.GRID_X_SPACES &&
				y < QuestUI.GRID_Y_SPACES &&
				actors[x][y] == null;
	}
	
	/**
	 * Tells the player to move towards a grid space.
	 * Receives pixel coordinates and converts them to the right grid space.
	 * @param xPixelDestination - The x-pixel to head towards.
	 * @param yPixelDestination - The y-pixel to head towards.
	 */
	public void movePlayer(int xPixelDestination, int yPixelDestination) {
		player.moveTo(xPixelDestination/QuestUI.GRID_SPACE_SIZE, yPixelDestination/QuestUI.GRID_SPACE_SIZE);
	}
	
	/**
	 * Polls all actors and updates their positions in the actor grid according to their current positions.
	 * TODO This is really hacky.
	 * 	If something's position gets updated before this is called (different timers),
	 * 		they could be momentarily on the same grid space but seem like they aren't...
	 */
	public void refreshActors() {
		monster.moveTo(player);
		
		for (int row = 0; row < actors.length; row++)
			for (int column = 0; column < actors[row].length; column++)
				if (row == player.getXPosition() && column == player.getYPosition())
					actors[row][column] = player;
				else if (row == monster.getXPosition() && column == monster.getYPosition())
					actors[row][column] = monster;
				else
					actors[row][column] = null;
	}
}
