package weewarai.battlecalc;

import java.io.IOException;

import org.jdom.JDOMException;

import weewarai.model.Coordinate;
import weewarai.model.Game;
import weewarai.model.Unit;
import weewarai.model.WeewarMap;

public class BattleCalcUtil {

	/**
	 * Gets most probable damage done by unit unit moving to from and attacking
	 * the enemy unit on enemyCoord. Note: now [2 damage with 60%, 3 damage with
	 * 40%] (weighted avg = 2.4) is preferred over [2 damage with 70%, 3 damage
	 * with 30%] (weighted avg = 2.1)
	 * 
	 * @param game
	 *            the game state
	 * @param wmap
	 *            the map info
	 * @param unit
	 *            the attacking unit
	 * @param from
	 *            the attacking unit's location for the attack
	 * @param enemyCoord
	 *            the defending unit's location
	 * @param bonus
	 * @return the most probable damage done by unit moving to from and
	 *         attacking the enemy unit on enemyCoord.
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static double getMostProbableDamageDealt(Game game, WeewarMap wmap,
			Unit unit, Coordinate from, Coordinate enemyCoord, int bonus)
			throws IOException, JDOMException {
		Unit enemy = game.getUnit(enemyCoord);
		double[] probs = BattleCalc.getProbabilities(unit.getQuantity(), unit
				.getType(), wmap.get(from).getType(), enemy.getQuantity(),
				enemy.getType(), wmap.get(enemyCoord).getType(), bonus);
		System.out.println("        .. dealt to " + enemy.getType() + " at "
				+ enemy.getCoordinate() + ":    "
				+ BattleCalc.formatProbArray(probs));
		return calcWeightedSum(probs, enemy.getQuantity()) / 100.0;
	}
	
	/**
	 * Gets most probable damage received by unit unit moving to from and
	 * attacking the enemy unit on enemyCoord.
	 * 
	 * @param game
	 *            the game state
	 * @param wmap
	 *            the map info
	 * @param unit
	 *            the attacking unit
	 * @param from
	 *            the attacking unit's location for the attack
	 * @param enemyCoord
	 *            the defending unit's location
	 * @param bonus
	 * @return the most probable damage received by unit moving to from and
	 *         attacking the enemy unit on enemyCoord.
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static double getMostProbableDamageReceived(Game game,
			WeewarMap wmap, Unit unit, Coordinate from, Coordinate enemyCoord,
			int bonus) throws IOException, JDOMException {
		Unit enemy = game.getUnit(enemyCoord);
		double[] probs = BattleCalc
				.getProbabilities(enemy.getQuantity(), enemy.getType(), wmap
						.get(enemy.getCoordinate()).getType(), unit
						.getQuantity(), unit.getType(), wmap.get(from)
						.getType(), bonus);
		System.out.println("        .. recv'd from " + enemy.getType() + " at "
				+ enemy.getCoordinate() + ": "
				+ BattleCalc.formatProbArray(probs));
		return calcWeightedSum(probs, unit.getQuantity()) / 100.0;
	}

	/**
	 * Finds the first index i in array a such that a[i] > all other values in
	 * a.
	 * 
	 * @param a
	 *            the array
	 * @return the first index i in array a such that a[i] > all other values in
	 *         a
	 */
	public static int findIndexOfGreatestValue(double[] a) {
		int maxIndex = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > maxIndex) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	/**
	 * Finds the sum of the values of the array, weighted by quantity - index
	 * (quantity - index = damage)
	 * 
	 * @param a
	 *            the array
	 * @param quantity
	 *            value that will the index will be subtracted from
	 * @return the sum of the values of the array, weighted by quantity - index
	 */
	public static double calcWeightedSum(double[] a, int quantity) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += (quantity - i) * a[i];
		}
		return sum;
	}
}
