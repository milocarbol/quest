package loader;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import control.Game;
import data.Files;
import entity.actor.Monster;
import entity.actor.power.Power;

/**
 * Loads a monster from a block of text.
 * @author Milo Carbol
 * @since 13 April 2013
 */
public class MonsterLoader extends DataLoader {

	/** The game the monsters are for **/
	private final Game game;
	
	/** The loader for power text **/
	private final PowerLoader powerLoader;
	
	/**
	 * Creates a new monster loader and loads the monster file.
	 * @param game - The game the monsters are for.
	 * @param powerLoader - The loader for power text
	 */
	public MonsterLoader(Game game, PowerLoader powerLoader) {
		super(Files.MONSTERS);
		this.game = game;
		this.powerLoader = powerLoader;
	}
	
	/**
	 * Loads a monster from a line of text.
	 * @param monsterText - The line
	 * @return - The monster object
	 */
	public Monster loadMonster(String monsterText) {
		String[] monsterData = monsterText.split(RoomLoader.inLineDeliminator);
		
		String name = monsterData[0];
		Point location = new Point(Integer.parseInt(monsterData[1]), Integer.parseInt(monsterData[2]));
		Map<String, String> details = data.get(name);
		int health = Integer.parseInt(details.get("health"));
		int speed = Integer.parseInt(details.get("speed"));
		String imageAlive = details.get("alive");
		String imageDead = details.get("dead");
		String powerString = details.get("powers");
		List<Power> powers = powerLoader.loadPowers(powerString);
		
		return new Monster(location, health, speed, powers, imageAlive, imageDead, game);
	}
}
