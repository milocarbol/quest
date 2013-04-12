package entity.actor;

import java.awt.Image;
import java.awt.Point;

import control.Game;
import control.PowerController;
import entity.Entity;
import entity.actor.pathfinder.IPathfinder;
import entity.actor.power.Power;


/**
 * Base class for all movable characters in the game.
 * @author Milo
 * @since 5 April 2013
 */
public abstract class Actor extends Entity {	
	
	/** Current grid space **/
	private Point currentLocation;
	
	/** The health of the actor. When current health is 0, the actor is dead. **/
	private int currentHealth, maximumHealth;
	
	/** The speed of the actor, in multiples of the Actor Controller refresh timer **/
	private int speed;
	
	/** The controller for the actor's powers **/
	private PowerController powerController;
	
	/** The game this actor is a part of **/
	protected final Game game;
	
	/** The actor (if any) this actor is targeting **/
	private Actor target;
	
	/** The pathfinder **/
	private IPathfinder pathfinder;
	
	/**
	 * Creates a new actor.
	 * @param x - The initial x-coordinate.
	 * @param y - The initial y-coordinate.
	 * @param speed - The speed of the actor, in milliseconds per grid space.
	 * @param game - The game this actor is a part of.
	 */
	public Actor(Point startLocation, int maximumHealth, int speed, IPathfinder pathfinder, PowerController powerController, Image aliveImage, Image deadImage, Game game) {
		super(aliveImage, deadImage, true);
		
		this.currentLocation = new Point(startLocation);
		this.currentHealth = this.maximumHealth = maximumHealth;
		this.speed = speed;
		this.powerController = powerController;
		this.game = game;
		this.pathfinder = pathfinder;
	}
	
	/** @return the actor's speed (in multiples of the actor controller's refresh rate) **/
	public int getSpeed() { return speed; }
	
	/** @return the actor's current location. **/
	public Point getLocation() { return currentLocation; }

	/** @return the actor's current health **/
	public int getCurrentHealth() { return currentHealth; }
	
	/** @return the actor's maximum health **/
	public int getMaximumHealth() { return maximumHealth; }
	
	/** @return This actor's power controller **/
	public PowerController getPowerController() { return powerController; }
	
	/**
	 * Once all game components have been set, activate the actor.
	 */
	public abstract void begin();
	
	/**
	 * Deals damage to the actor.
	 * @param damage - the amount to reduce current health by.
	 */
	public void attack(int damage) {
		if (currentHealth >= damage)
			currentHealth -= damage;
		else {
			currentHealth = 0;
			deactivate();
		}
	}
	
	/**
	 * If the actor has a target actor, move towards it and attack if possible.
	 * If the actor doesn't have a target, move towards its destination.
	 */
	public void act() {
		if (target != null) {
			if (target.isActive()) {
				pathfinder.setTargetLocation(target.getLocation());
				Power activePower = powerController.getActivePower();
				if (game.canAttack(target, this, activePower.getRange()))
					activePower.use(target);
				else
					move();
			}
			else {
				stopTargeting();
				pathfinder.setTargetLocation(currentLocation);
			}
		}
		else
			move();
	}
	
	/**
	 * Targets an actor.
	 * Actors will move towards targets and attack when possible.
	 * @param target - The target actor
	 */
	public void target(Actor target) {
		this.target = target;
	}
	
	/**
	 * Stops targeting, allowing free movement.
	 */
	public void stopTargeting() {
		this.target = null;
	}
	
	/**
	 * Tells the actor to move towards a grid space.
	 * @param column - The x-coordinate of the space
	 * @oaran row - The y-coordinate of the space
	 **/
	public void moveTo(int column, int row) {
		pathfinder.setTargetLocation(new Point(column, row));
	}
	
	/**
	 * If the next position in the actor's path is available, move into it.
	 */
	private void move() {
		currentLocation = pathfinder.getNextLocation();
	}
}
