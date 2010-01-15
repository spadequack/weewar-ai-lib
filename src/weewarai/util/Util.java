package weewarai.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import weewarai.model.Coordinate;
import weewarai.model.Faction;
import weewarai.model.Game;
import weewarai.model.Terrain;
import weewarai.model.Unit;
import weewarai.model.WeewarMap;

public class Util {

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

	/**
	 * Finds the Coordinates of all enemy units.
	 * 
	 * @param game
	 *            the game state
	 * @param myFaction
	 *            the bot's faction
	 * @return list of Coordinates of enemy units
	 */
	public static List<Coordinate> getEnemyUnits(Game game, Faction myFaction) {
		List<Coordinate> targets = new LinkedList<Coordinate>();
		for (Faction faction : game.getFactions()) {
			if (faction == myFaction) {
				continue;
			}
			for (Unit otherUnit : faction.getUnits()) {
				targets.add(otherUnit.getCoordinate());
			}
		}
		return targets;
	}

	/**
	 * Finds the Coordinates of all enemy bases.
	 * 
	 * @param game
	 *            the game state
	 * @param wmap
	 *            the map info
	 * @param myFaction
	 *            the bot's faction
	 * @return list of Coordinates of enemy bases
	 */
	public static List<Coordinate> getEnemyBases(Game game, WeewarMap wmap,
			Faction myFaction) {
		List<Coordinate> targets = new LinkedList<Coordinate>();
		Collection<Terrain> bases = wmap.getTerrainsByType(Terrain.Base);
		for (Terrain base : bases) {
			if (game.getTerrainOwner(base) != myFaction) {
				targets.add(base.getCoordinate());
			}
		}
		return targets;
	}

	/**
	 * Finds the Coordinates of all enemy bases.
	 * 
	 * @param game
	 *            the game state
	 * @param wmap
	 *            the map info
	 * @param myFaction
	 *            the bot's faction
	 * @return list of Coordinates of enemy bases
	 */
	public static List<Coordinate> getOwnBases(Game game, WeewarMap wmap,
			Faction myFaction) {
		List<Coordinate> targets = new LinkedList<Coordinate>();
		Collection<Terrain> bases = wmap.getTerrainsByType(Terrain.Base);
		for (Terrain base : bases) {
			if (game.getTerrainOwner(base) == myFaction) {
				targets.add(base.getCoordinate());
			}
		}
		return targets;
	}

	/**
	 * Finds an unoccupied enemy or empty base from coords.
	 * 
	 * @param game
	 *            the game state
	 * @param wmap
	 *            the map info
	 * @param coords
	 *            the coordinates to look for a base in
	 * @param myFaction
	 *            the bot's faction
	 * @return an unoccupied enemy or empty base from coords; null if none
	 *         exists
	 */
	public static Coordinate matchEnemyOrFreeBase(Game game, WeewarMap wmap,
			Faction myFaction, List<Coordinate> coords) {
		for (Coordinate c : coords) {
			Terrain t = wmap.get(c);
			Faction owner = game.getTerrainOwner(t);
			if (t.getType().equals(Terrain.Base)
					&& (owner == null || owner != myFaction)
					&& game.getUnit(c) == null) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Returns a list of all of the bot's empty and uncaptured capturable
	 * terrains.
	 * 
	 * @param game
	 *            the game object
	 * @param myFaction
	 *            the bot's faction
	 * @return a list of all of the bot's empty and uncaptured capturable
	 *         terrains.
	 */
	public static List<Terrain> getBuildableBases(Game game, Faction myFaction) {
		LinkedList<Terrain> buildableBases = new LinkedList<Terrain>();
		List<Terrain> terrains = myFaction.getCapturedTerrains();
		for (Terrain terrain : terrains) {
			boolean isBaseEmpty = (game.getUnit(terrain.getCoordinate()) == null);
			boolean isBaseFinished = terrain.isFinished();

			if (isBaseEmpty && !isBaseFinished) {
				buildableBases.add(terrain);
			}
		}
		return buildableBases;
	}

	/**
	 * Returns the number of own units that are at most maxDist away from the
	 * given Coordinate
	 * 
	 * @param coordinate
	 *            the coordinate
	 * @param myUnits
	 *            a list of own units
	 * @param maxDist
	 *            the maximum distance
	 * @return the number of own units that are at most maxDist away from the
	 *         given Coordinate
	 */
	public static int countNearOwnUnits(Coordinate coordinate,
			List<Unit> myUnits, int maxDist) {
		int count = 0;
		for (Unit unit : myUnits) {
			int distance = coordinate.getDirectDistance(unit.getCoordinate());
			if (distance <= maxDist) {
				count++;
			}
		}
		return count;
	}
	
	

}
