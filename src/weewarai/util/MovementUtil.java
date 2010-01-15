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
	public static Coordinate findClosestMovementToTarget(
			List<Coordinate> movementOptions, Collection<Coordinate> targets) {
		Coordinate best = null;
		int smallestDirectDist = Integer.MAX_VALUE;
		for (Coordinate c : movementOptions) {
			// initial case
			if (best == null) {
				best = c;
			}
			for (Coordinate target : targets) {
				int dist = c.getDirectDistance(target);
				if (dist < smallestDirectDist) {
					best = c;
					smallestDirectDist = dist;
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
	 * @param ignoreUnits
	 *            whether to ignore units (i.e. don't consider ZoC)
	 * @param selectAlg
	 *            the move selection algorithm object that wraps a method used
	 *            to select the best move - designed this way to allow using
	 *            different selection algorithms at runtime
	 * @return the best movement that will get the unit closest to the closest
	 *         target and the shortest distance to the target, -1 if the best
	 *         move is to stay
	 */
	public static Pair<Coordinate, Integer> findBestMovementToTargets(
			Game game, WeewarMap wmap, Unit unit,
			List<Coordinate> movementOptions, Collection<Coordinate> targets,
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
			return new Pair<Coordinate, Integer>(null, -1); // best move is to
			// stay
		}

		List<Coordinate> newBestMoves = selectAlg.select(wmap, unit, bestMoves);
		return new Pair<Coordinate, Integer>(selectRandom(newBestMoves),
				closestDistToTarget);
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

	/**
	 * Find the closest target, (that the unit can reach).
	 * 
	 * @param game
	 *            the game state
	 * @param wmap
	 *            the map info
	 * @param unit
	 *            the moving unit
	 * @param targets
	 *            the targets
	 * @return the closest target that this unit can move to
	 */
	public static Coordinate findClosest(Game game, WeewarMap wmap, Unit unit,
			Collection<Coordinate> targets) {
		Coordinate unitLocation = unit.getCoordinate();
		Coordinate best = null;
		int smallestDist = Integer.MAX_VALUE;

		for (Coordinate target : targets) {
			boolean ignoreUnits = false;
			List<Coordinate> path = MovementPath.getShortestPathForUnit(wmap,
					game, unit, unitLocation, target, ignoreUnits);
			int dist = MovementPath.calculateMovementCost(path, wmap, unit);
			if (dist != 0) {
				if (dist < smallestDist) {
					best = target;
					smallestDist = dist;
				}
			}
		}

		return best;
	}

	/**
	 * Find the closest target of the given unit type using direct distance.
	 * 
	 * @param game
	 *            the game info
	 * @param unitType
	 *            the unit type being looked for, pass null for any type
	 * @param unitLocation
	 *            the coordinate of the unit
	 * @param targets
	 *            the coordinates of the targets
	 * @return the coordinate of the closest unit
	 */
	public static Coordinate findClosestUnit(Game game, String unitType,
			Coordinate unitLocation, Collection<Coordinate> targets) {
		Coordinate best = null;
		int smallestDirectDist = Integer.MAX_VALUE;

		for (Coordinate target : targets) {
			Unit unitAtLocation = game.getUnit(target);
			if (unitAtLocation != null) {
				if ((unitType == null)
						|| (unitType.equals(unitAtLocation.getType()))) {
					int distance = unitLocation.getDirectDistance(target);
					if (distance < smallestDirectDist) {
						best = target;
						smallestDirectDist = distance;
					}
				}
			}
		}

		return best;
	}

	/** Find the closest target. */
	// public static Unit getClosestUnitForUnitType(Game game, WeewarMap wmap,
	// Unit unit, Coordinate unitLocation, List<Unit> targets) {
	// Unit best = null;
	// int bestDistance = Integer.MAX_VALUE;
	//
	// boolean ignoreUnits = false;
	// for (Unit target : targets) {
	// if (target.getQuantity() != 0) {
	// List<Coordinate> path = MovementPath.getShortestPathForUnit(wmap,
	// game, unit, unitLocation, target.getCoordinate(), ignoreUnits);
	// int dist = MovementPath.calculateMovementCost(path, wmap, unit);
	//				
	// List<Coordinate> path = ShortestPath.getShortestPathForUnit(
	// wmap, game, unit, unitLocation, target
	// .getCoordinate(), ignoreUnits, bestDistance);
	//
	// int distance = path.size();
	// if (distance > 1) // it always has the destination node
	// {
	// if (distance < bestDistance) {
	// best = target;
	// bestDistance = distance;
	// }
	// }
	// }
	// }
	//
	// return best;
	// }
}
