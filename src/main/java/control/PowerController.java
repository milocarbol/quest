package control;

import java.awt.Image;
import java.util.List;

import entity.actor.power.Power;

/**
 * The manager for all of an actor's powers.
 * @author Milo Carbol
 * @since 10 April 2013
 */
public class PowerController {

	/** The number of available powers **/
	public static final int NUMBER_OF_PLAYER_POWERS = 3;
	
	/** The powers available for the player to use **/
	private Power[] powers;
	
	/** The index of the currently active power **/
	private int activePowerIndex;
	
	public PowerController(List<Power> powers) {
		if (powers.size() == 0)
			throw new RuntimeException("Power list length is 0");
		else {
			this.powers = powers.toArray(new Power[0]);
			this.activePowerIndex = 0;
		}
	}
	
	/**
	 * Selects a new power.
	 * @param index - The index of the new power. Must be > 0 and < number of powers.
	 */
	public void selectPower(int index) {
		if (isValidIndex(index) && powers[index].isUsable())
			activePowerIndex = index;
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
	
	/** @return The index of the currently active power **/
	public int getIndexOfActivePower() { return activePowerIndex; }
	
	/** @return The currently active power **/
	public Power getActivePower() { return powers[activePowerIndex]; }
	
	/** @return The number of powers **/
	public int getNumberOfPowers() { return powers.length; }
}
