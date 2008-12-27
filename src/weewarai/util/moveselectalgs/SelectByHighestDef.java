package weewarai.util.moveselectalgs;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;

import weewarai.model.Coordinate;
import weewarai.model.Unit;
import weewarai.model.WeewarMap;

public class SelectByHighestDef implements MoveSelectAlgorithm {

	/**
	 * Selects coordinates with the best defense value from the list of
	 * coordinates that has the smallest associated movement cost
	 */
	@Override
	public List<Coordinate> select(WeewarMap wmap, Unit unit,
			SortedMap<Integer, List<Coordinate>> possibleMoves) {
		if (possibleMoves.isEmpty())
			return null;
		List<Coordinate> shortestDistMoves = possibleMoves.get(0);

		int highestTerrainDefMod = Integer.MIN_VALUE;
		List<Coordinate> newBestMoves = new LinkedList<Coordinate>();

		for (Coordinate m : shortestDistMoves) {
			int terrainDefMod = wmap.get(m).getDefenseMod(unit);
			if (highestTerrainDefMod < terrainDefMod) {
				highestTerrainDefMod = terrainDefMod;
				newBestMoves.clear();
				newBestMoves.add(m);
			} else if (terrainDefMod == highestTerrainDefMod) {
				newBestMoves.add(m);
			}
		}
		return newBestMoves;

	}

}
