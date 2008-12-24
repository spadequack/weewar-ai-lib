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
import weewarai.model.Unit;

public class ElizaLocal {

	/**
	 * Value 0 to 100 where 0 is never check against Eliza and 100 is always
	 * check against Eliza
	 */
	public static final int checkAttackCoordsRatio = 100;

	/**
	 * Returns the attack coordinates of a unit from a particular coordinate.
	 * 
	 * @param game
	 * @param myFaction
	 * @param unit
	 * @param from
	 * @return the attack coordinates of a unit from a particular coordinate.
	 */
	public static List<Coordinate> getAttackCoords(Game game,
			Faction myFaction, Unit unit, Coordinate from) {
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
				int dist = from.getDistanceInStraightLine(c);
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
	 * @param from
	 * @param unit
	 * @return the attack coordinates of a unit from a particular coordinate
	 */
	public List<Coordinate> getAttackCoords(Eliza eliza, Game game,
			Faction myFaction, Coordinate from, Unit unit) {

		String type = unit.getType();
		List<Coordinate> elizaCoords = null;
		List<Coordinate> localCoords = null;

		// only check a small sample of cases for errors
		int randomChoice = Utils.dice(100);
		if (randomChoice < checkAttackCoordsRatio) {
			try {
				elizaCoords = eliza.getAttackCoords(game.getId(), from, type);
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		localCoords = getAttackCoords(game, myFaction, unit, from);

		if (elizaCoords != null) {
			CoordinateComparator comp = new CoordinateComparator();
			Collections.sort(localCoords, comp);
			Collections.sort(elizaCoords, comp);
			if (!(localCoords.equals(elizaCoords))) {
				throw new RuntimeException(
						"getAttackCoords new implementation is broken");
			}
		}

		return localCoords;
	}
}
