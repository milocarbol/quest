package loader;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.Files;
import entity.actor.power.Power;

/**
 * Loads a power from a line of text.
 * @author Milo Carbol
 * @since 13 April 2013
 */
public class PowerLoader extends DataLoader {
	
	/** The deliminator between powers **/
	private static final String deliminator = ",";
	
	/**
	 * Creates a new power loader and loads the power file.
	 */
	public PowerLoader() {
		super(Files.POWERS);
	}
	
	/**
	 * Loads powers from text.
	 * @param powerString - The textual list of powers.
	 * @return The list of Power objects
	 */
	public List<Power> loadPowers(String powerString) {
		List<Power> powerList = new LinkedList<Power>();
		
		String[] powers = powerString.split(deliminator);
		
		for (String name : powers) {
			powerList.add(loadPower(name));
		}
		return powerList;
	}
	
	/**
	 * Creates a power object using the data file.
	 * @param name - The name of the power
	 * @return The power object
	 */
	private Power loadPower(String name) {
		Map<String, String> details = data.get(name);
		
		String imageSource = details.get("image");
		int range = Integer.parseInt(details.get("range"));
		int radius = Integer.parseInt(details.get("radius"));
		int damage = Integer.parseInt(details.get("damage"));
		boolean usable = details.get("usable").equals("yes");
		
		return new Power(name, imageSource, range, radius, damage, usable);
	}
}
