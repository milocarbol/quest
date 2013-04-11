package entity.actor.power;


import java.awt.Image;

import loader.ImageLoader;

import entity.actor.Actor;

/**
 * Powers can be used by players and monsters to affect each other.
 * Attacking uses the currently selected power.
 * @author Milo Carbol
 * @since 10 April 2013
 */
public class Power {

	/** The default melee power image **/
	public static final String DEFAULT_MELEE_IMAGE = "default_power_melee";
	
	/** The default ranged power image **/
	public static final String DEFAULT_RANGED_IMAGE = "default_power_ranged";
	
	/** The unavailable power image **/
	public static final String UNAVAILABLE_IMAGE = "power_unavailable";
	
	/** The default name for melee powers **/
	public static final String DEFAULT_MELEE_NAME = "Melee Power";
	
	/** The default name for ranged powers **/
	public static final String DEFAULT_RANGED_NAME = "Ranged Power";
	
	/** The default damage for a power **/
	public static final int DEFAULT_DAMAGE = 10;
	
	/** The default range for a ranged power **/
	public static final int DEFAULT_RANGE = 10;
	
	/** The name of this power **/
	private String name;
	
	/** The image to render in the player's info bar **/
	private Image image;
	
	/** The range of this power. Melee powers have range 1. **/
	private int range;
	
	/** The radius of this power. Single-target powers have radius 0. **/
	private int radius;
	
	/** The damage dealt by this power **/
	private int damage;
	
	/** If this power is usable **/
	boolean usable = true;
	
	/**
	 * Creates a power.
	 * @param name - The name of the power
	 * @param imageSource - The image file
	 * @param range - The range of the power (melee range is 1)
	 * @param radius - The radius of the power (single-target radius is 0)
	 * @param damage - The damage of the power
	 */
	public Power(String name, String imageSource, int range, int radius, int damage) {
		this.name = name;
		this.image = ImageLoader.loadImage(imageSource);
		this.range = range;
		this.radius = radius;
		this.damage = damage;
	}
	
	/**
	 * Creates a melee power.
	 * @param name - The name of the power
	 * @param imageSource - The image file
	 * @param damage - The power's damage
	 */
	public Power(String name, String imageSource, int damage) {
		this(name, imageSource, 1, 0, damage);
	}
	
	/**
	 * Creates an unavailable power. Unavailable powers can't be used.
	 */
	public Power() {
		this("Unavailable", UNAVAILABLE_IMAGE, 0, 0, 0);
		this.usable = false;
	}
	
	/**
	 * Uses the power on a target.
	 * @param target - The target actor.
	 */
	public void use(Actor target) {
		target.attack(damage);
	}
	
	/** @return The name of this power **/
	public String getName() { return name; }
	
	/** The image to render in the player's info bar **/
	public Image getImage() { return image; }
	
	/** @return The range of this power **/
	public int getRange() { return range; }
	
	/** @return The radius of this power **/
	public int getRadius() { return radius; }
	
	/** @return Whether the power is usable or not **/
	public boolean isUsable() { return usable; }
	
	/**
	 * Creates a default melee power.
	 * @return A default melee power instance.
	 */
	public static Power createDefaultMeleePower() {
		return new Power(DEFAULT_MELEE_NAME, DEFAULT_MELEE_IMAGE, DEFAULT_DAMAGE);
	}
	
	/**
	 * Creates a default ranged power.
	 * @return A default ranged power instance.
	 */
	public static Power createDefaultRangedPower() {
		return new Power(DEFAULT_RANGED_NAME, DEFAULT_RANGED_IMAGE, DEFAULT_RANGE, 0, DEFAULT_DAMAGE);
	}
}
