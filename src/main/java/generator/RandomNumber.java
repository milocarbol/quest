package generator;

/**
 * Contains random number methods.
 * @author Milo Carbol
 * @since 15 April 2013
 */
public class RandomNumber {

	/**
	 * Generates a random number between two parameters.
	 * @param low - Lower bound (inclusive)
	 * @param high - Upper bound (exclusive)
	 * @return A random number between the upper and lower bounds
	 */
	public static int randomBetween(int low, int high) {
		return (int)(Math.random() * (high - low) + low);
	}
}
