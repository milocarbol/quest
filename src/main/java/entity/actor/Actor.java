package entity.actor;

import java.awt.Image;

import control.Game;
import entity.Entity;


/**
 * Base class for all movable characters in the game.
 * @author Milo
 * @since 5 April 2013
 */
public abstract class Actor extends Entity {	
	
	/** LOCATION DATA **/
	
	/** Current grid space **/
	private int currentXPosition, currentYPosition;
	
	/** Target grid space **/
	private int targetXPosition, targetYPosition;
	
	
	/** NAVIGATION DATA **/
	
	/**
	 * If the chunks (the line segments placed in
	 * 	separate rows or columns to make the appearance of a straight line on the grid
	 * 	are horizontally aligned. False if they are vertically aligned.
	 **/
	private boolean chunksAreHorizontal;
	
	/** Array of chunk sizes. Each index is a separate chunk. **/
	private int[] chunkSizes = {};
	
	/** The current chunk **/
	private int chunkIndex = 0;
	
	/**
	 * The current position in the chunk.
	 * 1-indexed because 1 grid space counts as 1 distance, not 0.
	 **/
	private int indexInChunk = 1;
	
	
	/** ATTRIBUTE DATA **/
	
	/** The health of the actor. When current health is 0, the actor is dead. **/
	private int currentHealth, maximumHealth;
	
	/** The speed of the actor, in multiples of the Actor Controller refresh timer **/
	private int speed;
	
	/** The damage this actor deals to other actors **/
	private int damage;
	
	
	/** INTERACTION DATA **/
	
	/** The game this actor is a part of **/
	protected final Game game;
	
	/** The actor (if any) this actor is targeting **/
	private Actor target;
	
	/**
	 * Creates a new actor.
	 * @param x - The initial x-coordinate.
	 * @param y - The initial y-coordinate.
	 * @param speed - The speed of the actor, in milliseconds per grid space.
	 * @param game - The game this actor is a part of.
	 */
	public Actor(int x, int y, int maximumHealth, int speed, int damage, Image aliveImage, Image deadImage, Game game) {
		super(aliveImage, deadImage, true);
		
		currentXPosition = targetXPosition = x;
		currentYPosition = targetYPosition = y;
		
		this.currentHealth = this.maximumHealth = maximumHealth;
		
		this.speed = speed;
		this.damage = damage;
		
		this.game = game;
	}
	
	/** ACCESSORS **/
	
	/** @return The damage this actor deals to other actors **/
	public int getDamage() { return damage; }
	
	/** @return the actor's speed (in multiples of the actor controller's refresh rate) **/
	public int getSpeed() { return speed; }
	
	/** @return the actor's current x position. **/
	public int getXPosition() { return currentXPosition; }
	
	/** @return the actor's current y position **/
	public int getYPosition() { return currentYPosition; }

	/** @return the actor's current health **/
	public int getCurrentHealth() { return currentHealth; }
	
	/** @return the actor's maximum health **/
	public int getMaximumHealth() { return maximumHealth; }
	
	/** INTERACTION **/
	
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
			moveTo(target.getXPosition(), target.getYPosition());
			if (game.canAttack(target, this, 1))
				target.attack(getDamage());
			else
				move();
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
	
	/** NAVIGATION **/
	
	/**
	 * Tells the actor to move towards another actor.
	 * @param actor - The target actor.
	 */
	public void moveTo(Actor actor) {
		moveTo(actor.getXPosition(), actor.getYPosition());
	}
	
	/**
	 * Tells the actor to move towards a grid space.
	 * Computes chunk-sizes too.
	 * TODO Make this javadoc more informative.
	 * @param x - The x-coordinate of the target.
	 * @param y - The y-coordinate of the target.
	 */
	public void moveTo(int x, int y) {
		targetXPosition = x;
		targetYPosition = y;
		
		indexInChunk = 1;
		chunkIndex = 0;
		
		int distanceX = Math.abs(targetXPosition - currentXPosition),
			distanceY = Math.abs(targetYPosition - currentYPosition);
		
		int chunkSize, remainder;
		
		if (distanceX >= distanceY) {
			chunksAreHorizontal = true;
			chunkSizes = new int[distanceY+1];
			
			if (distanceY != 0) {
				chunkSize = (distanceX + 1) / (distanceY + 1);
				remainder = (distanceX + 1) % (distanceY + 1);
			}
			else {
				chunkSize = distanceX;
				remainder = 0;
			}
		}
		else {
			chunksAreHorizontal = false;
			chunkSizes = new int[distanceX+1];
			
			if (distanceX != 0) {
				chunkSize = (distanceY + 1) / (distanceX + 1);
				remainder = (distanceY + 1) % (distanceX + 1);
			}
			else {
				chunkSize = distanceY;
				remainder = 0;
			}
		}
		
		for (int index = 0; index < chunkSizes.length; index++)
			chunkSizes[index] = chunkSize;
		
		for (int index = chunkSizes.length / 2 - remainder / 2; remainder > 0; index++) {
			chunkSizes[index]++;
			remainder--;
		}
	}
	
	/**
	 * If the next position in the actor's path is available, move into it.
	 */
	private void move() {
		if (currentXPosition != targetXPosition || currentYPosition != targetYPosition) {
			if (chunksAreHorizontal) {
				int potentialXPosition = currentXPosition + computeDirection(currentXPosition, targetXPosition);
				
				if (indexInChunk < chunkSizes[chunkIndex]) {
					if (game.spaceIsFree(potentialXPosition, currentYPosition)) {
						currentXPosition = potentialXPosition;
						indexInChunk++;
					}
				}
				else {
					int potentialYPosition = currentYPosition + computeDirection(currentYPosition, targetYPosition);
					
					if (game.spaceIsFree(potentialXPosition, potentialYPosition)) {
						currentXPosition = potentialXPosition;
						currentYPosition = potentialYPosition;
						chunkIndex++;
						indexInChunk = 1;
					}
				}
			}
			else {
				int potentialYPosition = currentYPosition + computeDirection(currentYPosition, targetYPosition);
				
				if (indexInChunk < chunkSizes[chunkIndex]) {
					if (game.spaceIsFree(currentXPosition, potentialYPosition)) {
						currentYPosition = potentialYPosition;
						indexInChunk++;
					}
				}
				else {
					int potentialXPosition = currentXPosition + computeDirection(currentXPosition, targetXPosition);
					
					if (game.spaceIsFree(potentialXPosition, potentialYPosition)) {
						currentXPosition = potentialXPosition;
						currentYPosition = potentialYPosition;
						chunkIndex++;
						indexInChunk = 1;
					}
				}
			}
		}
	}
	
	/**
	 * Computes the direction (positive or negative) required to reach the target.
	 * @param current - The current grid space on this axis.
	 * @param target - The target grid space on this axis.
	 * @return 1 if the actor must increment their position on this axis, -1 if they must decrement, 0 if they are at the target space.
	 */
	private int computeDirection(int current, int target) {
		if (target == current)
			return 0;
		return (target - current) / Math.abs(target - current);
	}
}
