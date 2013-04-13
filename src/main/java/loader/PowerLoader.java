package loader;

import entity.actor.power.Power;

/**
 * Loads a power from a line of text.
 * @author Milo Carbol
 * @since 13 April 2013
 */
public class PowerLoader {

	/**
	 * Loads a power from a line of text.
	 * @param powerLine - The line of text
	 * @return The power object
	 */
	public Power loadPower(String powerLine) {
		String[] powerData = powerLine.split(RoomLoader.inLineDeliminator);
		
		String name = powerData[0];
		String imageSource = powerData[1];
		int range = Integer.parseInt(powerData[2]);
		int radius = Integer.parseInt(powerData[3]);
		int damage = Integer.parseInt(powerData[4]);
		
		return new Power(name, imageSource, range, radius, damage);
	}
}
