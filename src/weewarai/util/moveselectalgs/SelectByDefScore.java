package weewarai.util.moveselectalgs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import weewarai.model.Coordinate;
import weewarai.model.Terrain;
import weewarai.model.Unit;
import weewarai.model.WeewarMap;

/**
 * David's algorithm
 *
 */
public class SelectByDefScore implements MoveSelectAlgorithm {

	Map<String, Integer> terrainTrooperFightingBias = new HashMap<String, Integer>();
	{
		terrainTrooperFightingBias.put(Terrain.Airfield, 2);
		terrainTrooperFightingBias.put(Terrain.Base, 2);
		terrainTrooperFightingBias.put(Terrain.Desert, -1);
		terrainTrooperFightingBias.put(Terrain.Harbor, 2);
		terrainTrooperFightingBias.put(Terrain.Mountains, 4);
		terrainTrooperFightingBias.put(Terrain.Plains, 0);
		terrainTrooperFightingBias.put(Terrain.Repair_Patch, -6);
		terrainTrooperFightingBias.put(Terrain.Swamp, -2);
		terrainTrooperFightingBias.put(Terrain.Water, -10);
		terrainTrooperFightingBias.put(Terrain.Woods, 3);
	}

	Map<String, Integer> terrainHovercraftFightingBias = new HashMap<String, Integer>();
	{
		terrainHovercraftFightingBias.put(Terrain.Airfield, -2);
		terrainHovercraftFightingBias.put(Terrain.Base, -1);
		terrainHovercraftFightingBias.put(Terrain.Desert, 0);
		terrainHovercraftFightingBias.put(Terrain.Harbor, -2);
		terrainHovercraftFightingBias.put(Terrain.Mountains, 4);
		terrainHovercraftFightingBias.put(Terrain.Plains, 0);
		terrainHovercraftFightingBias.put(Terrain.Repair_Patch, 0);
		terrainHovercraftFightingBias.put(Terrain.Swamp, -2);
		terrainHovercraftFightingBias.put(Terrain.Water, -10);
		terrainHovercraftFightingBias.put(Terrain.Woods, -3);
	}

	// mechanical units
	Map<String, Integer> terrainOrdinaryFightingBias = new HashMap<String, Integer>();
	{
		terrainOrdinaryFightingBias.put(Terrain.Airfield, -1);
		terrainOrdinaryFightingBias.put(Terrain.Base, -1);
		terrainOrdinaryFightingBias.put(Terrain.Desert, 0);
		terrainOrdinaryFightingBias.put(Terrain.Harbor, -1);
		terrainOrdinaryFightingBias.put(Terrain.Mountains, 4);
		terrainOrdinaryFightingBias.put(Terrain.Plains, 0);
		terrainOrdinaryFightingBias.put(Terrain.Repair_Patch, -6);
		terrainOrdinaryFightingBias.put(Terrain.Swamp, -2);
		terrainOrdinaryFightingBias.put(Terrain.Water, -10);
		terrainOrdinaryFightingBias.put(Terrain.Woods, -3);
	}

	@Override
	public List<Coordinate> select(WeewarMap wmap, Unit unit,
			SortedMap<Integer, List<Coordinate>> possibleMoves) {
		if (possibleMoves.isEmpty())
			return null;

		int lowestScore = Integer.MAX_VALUE;
		List<Coordinate> newBestMoves = new LinkedList<Coordinate>();

		for (Map.Entry<Integer, List<Coordinate>> e : possibleMoves.entrySet()) {
			for (Coordinate m : e.getValue()) {
				int score = e.getKey() * 10 - getDefenseScore(unit, wmap.get(m));
				if (score < lowestScore) {
					lowestScore = score;
					newBestMoves.clear();
					newBestMoves.add(m);
				} else if (score == lowestScore) {
					newBestMoves.add(m);
				}
			}
		}
		return newBestMoves;
	}

	public int getDefenseScore(Unit unit, Terrain t) {
		int result = 0;

		if (unit.isOfUnitType(Unit.Soft_Type)) {
			result = terrainTrooperFightingBias.get(t.getType());
		} else if (unit.isOfUnitType(Unit.Amphibic_Type)) {
			result = terrainHovercraftFightingBias.get(t.getType());
		} else {
			result = terrainOrdinaryFightingBias.get(t.getType());
		}

		return result;
	}

}
