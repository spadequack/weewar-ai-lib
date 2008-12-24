package weewarai.util;

import java.util.Random;

public class Utils {

	public static Random random = new Random();

	/**
	 * Generate a random number from 1 to n, inclusive
	 * 
	 * @param n
	 *            upper bound of random number, inclusive
	 * @return a random number from 1 to n, inclusive
	 */
	public static int dice(int n) {
		return random.nextInt(n) + 1;
	}
}
