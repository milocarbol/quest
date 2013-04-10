package control;

import java.awt.Image;

import entity.actor.power.Power;

/**
 * The manager for all of an actor's powers.
 * @author Milo Carbol
 * @since 10 April 2013
 */
public class PowerController {

	/** The number of available powers **/
	public static final int NUMBER_OF_PLAYER_POWERS = 3;
	
	private static final Power unusablePower = new Power();
	
	/** The powers available for the player to use **/
	private Power[] powers;
	
	/** The currently active power **/
	private Power activePower;
	
	/**
	 * Creates a new power controller using a default melee power.
	 */
	public PowerController() {
		powers = new Power[1];
		powers[0] = Power.createDefaultMeleePower();
		activePower = powers[0];
	}
	
	/**
	 * Creates a new power controller with a number of power slots.
	 * If it's for a player, it defaults to one melee, one ranged, and one unusable.
	 * @param forPlayer - If this is a player power controller. If not, it just adds a default melee.
	 */
	public PowerController(boolean forPlayer) {
		if (forPlayer) {
			powers = new Power[NUMBER_OF_PLAYER_POWERS];
			
			powers[0] = Power.createDefaultMeleePower();
			powers[1] = Power.createDefaultRangedPower();
			
			for (int index = 2; index < NUMBER_OF_PLAYER_POWERS; index++)
				powers[index] = unusablePower;
		}
		else {
			powers = new Power[1];
			powers[0] = Power.createDefaultMeleePower();
		}
		activePower = powers[0];
	}
	
	/**
	 * Selects a new power.
	 * @param index - The index of the new power. Must be > 0 and < number of powers.
	 */
	public void selectPower(int index) {
		if (isValidIndex(index) && powers[index].isUsable())
			activePower = powers[index];
	}
	
	/**
	 * Gets the image for a power.
	 * @param index - The index of the power
	 * @return The image for the power
	 */
	public Image getPowerImage(int index) {
		if (isValidIndex(index))
			return powers[index].getImage();
		else
			throw new RuntimeException("Power index " + index + " is out of range.");
	}
	
	/**
	 * Checks if an index is valid.
	 * @param index - the index to check
	 * @return True if the index is > 0 and < the number of powers
	 */
	private boolean isValidIndex(int index) {
		return index >= 0 && index < powers.length;
	}
	
	/** @return The currently active power **/
	public Power getActivePower() { return activePower; }
	
	/** @return The number of powers **/
	public int getNumberOfPowers() { return powers.length; }
}
