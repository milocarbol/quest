package loader;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import control.Game;
import entity.actor.Monster;
import entity.actor.power.Power;

/**
 * Loads a monster from a block of text.
 * @author Milo Carbol
 * @since 13 April 2013
 */
public class MonsterLoader {

	/** The game the monsters are for **/
	private final Game game;
	
	/** The loader for power text **/
	private final PowerLoader powerLoader;
	
	/**
	 * Creates a new monster loader.
	 * @param game - The game the monsters are for.
	 * @param powerLoader - The loader for power text
	 */
	public MonsterLoader(Game game, PowerLoader powerLoader) {
		this.game = game;
		this.powerLoader = powerLoader;
	}
	
	/**
	 * Loads a monster from a block of text.
	 * @param monsterText - The text
	 * @return - The monster object
	 */
	public Monster loadMonster(String monsterText) {
		String[] lines = monsterText.split(System.getProperty("line.separator"));
		
		String[] monsterData = lines[0].split(RoomLoader.inLineDeliminator);
		
		Point location = new Point(Integer.parseInt(monsterData[0]), Integer.parseInt(monsterData[1]));
		int health = Integer.parseInt(monsterData[2]);
		int speed = Integer.parseInt(monsterData[3]);
		String imageAlive = monsterData[4];
		String imageDead = monsterData[5];
		
		List<Power> powers = new LinkedList<Power>();
		for (int lineNumber = 1; lineNumber < lines.length; lineNumber++) {
			powers.add(powerLoader.loadPower(lines[lineNumber]));
		}
		
		return new Monster(location, health, speed, imageAlive, imageDead, game);
	}
}
