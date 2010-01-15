package weewarai.battlecalc;

import weewarai.model.Coordinate;
import weewarai.model.Game;
import weewarai.model.Unit;
import weewarai.model.WeewarMap;
import weewarai.util.Debug;

public class BattleCalcUtil {

	/**
	 * Gets most probable damage done by unit unit moving to fromCoord and
	 * attacking the enemy unit on enemyCoord. Accounts for range. Note: now [2
	 * damage with 60%, 3 damage with 40%] (weighted avg = 2.4) is preferred
	 * over [2 damage with 70%, 3 damage with 30%] (weighted avg = 2.1)
	 * 
	 * @param game
	 *            the game state
	 * @param wmap
	 *            the map info
	 * @param unit
	 *            the attacking unit
	 * @param fromCoord
	 *            the attacking unit's location for the attack
	 * @param enemyCoord
	 *            the defending unit's location
	 * @param bonus
	 * @return the most probable damage done by unit moving to fromCoord and
	 *         attacking the enemy unit on enemyCoord.
	 */
	public static double getMostProbableDamageDealt(Game game, WeewarMap wmap,
			Unit unit, Coordinate fromCoord, Coordinate enemyCoord, int bonus) {
		Unit enemy = game.getUnit(enemyCoord);

		int dist = fromCoord.getDirectDistance(enemyCoord);
		if (dist < unit.getMinRange(enemy) || unit.getMaxRange(enemy) < dist)
			return 0;

		double[] probs = BattleCalc.getProbabilities(unit.getQuantity(), unit
				.getType(), wmap.get(fromCoord).getType(), enemy.getQuantity(),
				enemy.getType(), wmap.get(enemyCoord).getType(), bonus);
		double weightedSum = Math.round(calcWeightedSum(probs, enemy
				.getQuantity())) / 100.0;
		Debug.print("Battle calc: dealt to " + enemy.getType() + " at "
				+ enemy.getCoordinate() + " from " + fromCoord + ": ("
				+ weightedSum + ") " + BattleCalc.formatProbArray(probs));
		return weightedSum;
	}

	/**
	 * Gets most probable damage received by unit unit moving to fromCoord and
	 * attacking the enemy unit on enemyCoord. Accounts for range.
	 * 
	 * @param game
	 *            the game state
	 * @param wmap
	 *            the map info
	 * @param unit
	 *            the attacking unit
	 * @param fromCoord
	 *            the attacking unit's location for the attack
	 * @param enemyCoord
	 *            the defending unit's location
	 * @param bonus
	 * @return the most probable damage received by unit moving to fromCoord and
	 *         attacking the enemy unit on enemyCoord.
	 */
	public static double getMostProbableDamageReceived(Game game,
			WeewarMap wmap, Unit unit, Coordinate fromCoord,
			Coordinate enemyCoord, int bonus) {
		Unit enemy = game.getUnit(enemyCoord);

		int dist = fromCoord.getDirectDistance(enemyCoord);
		if (dist < enemy.getMinRange(unit) || enemy.getMaxRange(unit) < dist)
			return 0;

		double[] probs = BattleCalc.getProbabilities(enemy.getQuantity(), enemy
				.getType(), wmap.get(enemy.getCoordinate()).getType(), unit
				.getQuantity(), unit.getType(), wmap.get(fromCoord).getType(),
				bonus);
		double weightedSum = Math.round(calcWeightedSum(probs, unit
				.getQuantity())) / 100.0;
		Debug.print("Battle calc: received from " + enemy.getType() + " at "
				+ enemy.getCoordinate() + " from " + fromCoord + ": ("
				+ weightedSum + ") " + BattleCalc.formatProbArray(probs));
		return weightedSum;
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
