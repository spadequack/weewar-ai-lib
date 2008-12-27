package weewarai.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import weewarai.model.Coordinate;
import weewarai.model.Game;
import weewarai.model.MovementPath;
import weewarai.model.Unit;
import weewarai.model.WeewarMap;
import weewarai.util.moveselectalgs.MoveSelectAlgorithm;

public class MovementUtil {

	/**
	 * Finds the minimum direct distance from the given Coordinate to a
	 * Coordinate in targets. Note: this does not mean there is a valid path of
	 * that distance.
	 * 
	 * @param from
	 *            starting coordinate
	 * @param targets
	 *            targets that the unit wants to get to from from
	 * @return minimum distance from from to a Coordinate in targets
	 */
	public static int minDistance(Coordinate from,
			Collection<Coordinate> targets) {
		int n = Integer.MAX_VALUE;
		for (Coordinate c : targets) {
			int d = from.getDirectDistance(c);
			if (d < n) {
				n = d;
			}
		}
		return n;
	}

	/**
	 * Finds the Coordinate within movementOptions closest to a Coordinate in
	 * targets. Uses direct distance, not path distance.
	 * 
	 * @param movementOptions
	 *            possible movements of a unit
	 * @param targets
	 *            targets that the unit wants to get to
	 * @return the Coordinate in movementOptions closest to a Coordinate in
	 *         targets
	 */
	public static Coordinate getClosestMovementToTarget(
			List<Coordinate> movementOptions, Collection<Coordinate> targets) {
		Coordinate best = null;
		int n = Integer.MAX_VALUE;
		for (Coordinate c : movementOptions) {
			// initial case
			if (best == null) {
				best = c;
			}
			for (Coordinate target : targets) {
				int d = c.getDirectDistance(target);
				if (d < n) {
					best = c;
					n = d;
				}
			}
		}
		return best;
	}

	/**
	 * Finds the best movement to a target using Dijkstra's shortest path
	 * algorithm.
	 * 
	 * @param game
	 *            the game state
	 * @param wmap
	 *            the map info
	 * @param unit
	 *            the unit being moved
	 * @param movementOptions
	 *            the coordinates that this unit can move to
	 * @param targets
	 *            the targets that the unit wants to move to
	 * @param bestMovement
	 *            the coordinate object that will receive the coordinates of the
	 *            best movement that will get the unit closest to the closest
	 *            target
	 * @param ignoreUnits
	 *            whether to ignore units (i.e. don't consider ZoC)
	 * @param selectAlg
	 *            the move selection algorithm object that wraps a method used
	 *            to select the best move - designed this way to allow using
	 *            different selection algorithms at runtime
	 * @return the shortest distance to the target, -1 if the best move is to
	 *         stay
	 */
	public static int getBestMovementToTargets(Game game, WeewarMap wmap,
			Unit unit, List<Coordinate> movementOptions,
			Collection<Coordinate> targets, Coordinate bestMovement,
			boolean ignoreUnits, MoveSelectAlgorithm selectAlg) {

		Debug.print("        Getting best movement to targets...");

		int closestDistToTarget = Integer.MAX_VALUE;
		// map is sorted by distance
		SortedMap<Integer, List<Coordinate>> bestMoves = new TreeMap<Integer, List<Coordinate>>();

		for (Coordinate c : targets) {
			for (Coordinate m : movementOptions) {
				if (game.getUnit(m) != null && !unit.getCoordinate().equals(m)) {
					continue; // cannot move onto hex occupied by own unit
				}
				List<Coordinate> path = MovementPath.getShortestPathForUnit(
						wmap, game, unit, m, c, ignoreUnits);
				int dist = MovementPath.calculateMovementCost(path, wmap, unit);
				if (bestMoves.get(dist) == null)
					bestMoves.put(dist, new LinkedList<Coordinate>());
				bestMoves.get(dist).add(m);
			}
		}

		if (bestMoves.isEmpty()) {
			return -1; // best move is to stay
		}

		List<Coordinate> newBestMoves = selectAlg.select(wmap, unit, bestMoves);
		bestMovement.setCoordinate(selectRandom(newBestMoves));
		return closestDistToTarget;
	}

	/**
	 * Picks a random element from the list
	 * 
	 * @param list
	 *            the list to pick a random element from
	 * @return a random element from list
	 */
	public static Coordinate selectRandom(List<Coordinate> list) {
		int n = Util.dice(list.size());
		Debug.print("        .. Selected " + list.get(n - 1)
				+ " randomly from " + list);
		return list.get(n - 1);
	}
}
