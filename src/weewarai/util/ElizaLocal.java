package weewarai.util;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jdom.JDOMException;

import weewarai.communication.Eliza;
import weewarai.model.Coordinate;
import weewarai.model.Faction;
import weewarai.model.Game;
import weewarai.model.MovementPath;
import weewarai.model.Specs;
import weewarai.model.Unit;
import weewarai.model.WeewarMap;

public class ElizaLocal {

	/**
	 * Value 0 to 100 where 0 is never check against Eliza and 100 is always
	 * check against Eliza
	 */
	public static final int checkAttackCoordsRatio = 100;

	private static CoordinateComparator comp = new CoordinateComparator();

	/**
	 * Returns the attack coordinates of a unit from a particular coordinate.
	 * 
	 * @param game
	 * @param myFaction
	 * @param unit
	 * @param location
	 * @return the attack coordinates of a unit from a particular coordinate.
	 */
	public static List<Coordinate> getAttackCoords(Game game,
			Faction myFaction, Unit unit, Coordinate location) {
		List<Coordinate> coords = new LinkedList<Coordinate>();

		List<Coordinate> outerCircle = unit.getCoordinate().getCircle(
				unit.getMaxRange());
		List<Coordinate> innerCircle = unit.getCoordinate().getCircle(
				unit.getMinRange());
		outerCircle.removeAll(innerCircle);
		List<Coordinate> rangeRing = outerCircle;

		for (Coordinate c : rangeRing) {
			Unit u = game.getUnit(c);
			if (u != null && !u.getFaction().equals(myFaction)) {
				int dist = location.getDistanceInStraightLine(c);
				// need to confirm because Anti-Air has different range based
				// on enemy type
				if (unit.getMinRange(u) <= dist && dist <= unit.getMaxRange(u))
					coords.add(c);
			}
		}
		return coords;
	}

	/**
	 * Returns the attack coordinates of a unit from a particular coordinate.
	 * Also performs random check against the Eliza implementation to make sure
	 * the local version is accurate.
	 * 
	 * @param eliza
	 * @param game
	 * @param myFaction
	 * @param location
	 * @param unit
	 * @return the attack coordinates of a unit from a particular coordinate
	 */
	public List<Coordinate> getAttackCoords(Eliza eliza, Game game,
			Faction myFaction, Coordinate location, Unit unit) {

		String type = unit.getType();
		List<Coordinate> elizaCoords = null;

		// only check a small sample of cases for errors
		int randomChoice = Utils.dice(100);
		if (randomChoice < checkAttackCoordsRatio) {
			try {
				elizaCoords = eliza.getAttackCoords(game.getId(), location,
						type);
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<Coordinate> localCoords = getAttackCoords(game, myFaction, unit, location);

		if (elizaCoords != null) {
			Collections.sort(localCoords, comp);
			Collections.sort(elizaCoords, comp);
			if (!(localCoords.equals(elizaCoords))) {
				throw new RuntimeException(
						"getAttackCoords new implementation is broken");
			}
		}

		return localCoords;
	}

	public static List<Coordinate> getMovementCoordsLocal(Game game,
			WeewarMap wmap, Faction myFaction, Coordinate location, Unit unit) {

		int maxMoveCost = unit.getMovementPointsFirstMove();
		// TODO deal with second move

		List<Unit> myUnits = myFaction.getUnits();

		List<Coordinate> maxMoveCircle = location
				.getCircle(Specs.MAX_MOVE_RANGE);

		// remove finished units and enemy units
		for (Coordinate resultListTarget : maxMoveCircle) {
			Unit unitAtLocation = game.getUnit(resultListTarget);
			if (unitAtLocation != null) {
				if (myUnits.contains(unitAtLocation)) {
					if (unitAtLocation.isFinished()) {
						maxMoveCircle.remove(resultListTarget);
					}
				} else {
					maxMoveCircle.remove(resultListTarget);
				}
			}
		}

		// reworked
		boolean ignoreUnits = false;
		List<Coordinate> resultList = MovementPath.getMovesAtCostLessThan(wmap,
				game, unit, location, maxMoveCost, Specs.MAX_MOVE_RANGE,
				maxMoveCircle, ignoreUnits);

		for (Coordinate circle1Target : location.getCircle(1)) {
			if (!location.equals(circle1Target)) {
				Unit unitAtLocation = game.getUnit(circle1Target);
				if ((unitAtLocation == null)
						|| (myUnits.contains(unitAtLocation) && (!unitAtLocation
								.isFinished()))) {
					int nextStepDistance = wmap.getDistanceForUnitType(
							location, circle1Target, unit);
					if (nextStepDistance < Specs.UNPASSABLE) {
						if (!resultList.contains(circle1Target)) {
							resultList.add(circle1Target);
						}
					}
				}
			}
		}

		// a sub, destroyer and battleship can not move onto an enemy base
		if (!unit.canEnterEnemyHarbor()) {
			for (Coordinate movementLocations : resultList) {
				Faction terrainOwner = game.getTerrainOwner(movementLocations);
				if ((terrainOwner != null) && (!terrainOwner.equals(myFaction))) {
					resultList.remove(movementLocations);
				}
			}
		}

		return resultList;
	}

	public List<Coordinate> getMovementCoords(Eliza eliza, Game game,
			WeewarMap wmap, Faction myFaction, Coordinate location, Unit unit) {

		String type = unit.getType();
		List<Coordinate> elizaCoords = null;

		// only check a small sample of cases for errors
		int randomChoice = Utils.dice(100);
		if (randomChoice < checkAttackCoordsRatio) {
			try {
				elizaCoords = eliza.getMovementCoords(game.getId(), location,
						type);
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<Coordinate> localCoords = getMovementCoordsLocal(game, wmap,
				myFaction, location, unit);

		if ((elizaCoords != null)) {
			Collections.sort(localCoords, comp);
			Collections.sort(elizaCoords, comp);
			if (!(localCoords.equals(elizaCoords))) {
				// eliza is broken for secondary move count
				if (unit.getMoveCount() == 0) {
					System.out.println("location = " + location);
					System.out.println("resultList = " + localCoords);
					System.out.println("coords = " + elizaCoords);

					throw new RuntimeException(
							"getMovementCoords new implementation is broken");
				}
			}
		}
		
		return localCoords;
	}
}
