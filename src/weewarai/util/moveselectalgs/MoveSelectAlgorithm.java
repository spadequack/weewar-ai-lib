package weewarai.util.moveselectalgs;

import java.util.List;
import java.util.SortedMap;

import weewarai.model.Coordinate;
import weewarai.model.Unit;
import weewarai.model.WeewarMap;

/**
 * This interface allows a method to use a MoveSelectAlgorithm chosen at runtime
 * to determine which algorithm is applied - See the Strategy design pattern.
 */
public interface MoveSelectAlgorithm {

	/**
	 * Selects a list of Coordinates from the given map of possible moves based
	 * on a specific algorithm
	 * 
	 * @param wmap
	 *            the weewar map object
	 * @param unit
	 *            the moving unit
	 * @param possibleMoves
	 *            a map where the values are lists of possible movements and
	 *            keys are the movement costs of the possible movement lists -
	 *            all elements of each list should have the same movement cost
	 * @return a list of Coordinates from the given map of possible moves based
	 *         on a specific algorithm, null if possibleMoves is empty
	 */
	public List<Coordinate> select(WeewarMap wmap, Unit unit,
			SortedMap<Integer, List<Coordinate>> possibleMoves);

}
