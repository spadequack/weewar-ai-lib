package weewarai.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * An implementation of Dijkstra's shortest path algorithm. It computes the
 * shortest path (in distance) to all coordinates in the map. The output of the
 * algorithm is the shortest distance from the start coordinate to every other
 * coordinate, and the shortest path from the start coordinate to every other.
 * <p>
 * Upon calling {@link #execute(Coordinate, Coordinate, boolean)}, the results
 * of the algorithm are made available by calling
 * {@link #getPredecessor(Coordinate)} and
 * {@link #getShortestDistance(Coordinate)}. To get the shortest path between
 * the destination coordinate and the source coordinate after running the
 * algorithm, one would do:
 * 
 * <pre>
 * ArrayList&lt;City&gt; l = new ArrayList&lt;City&gt;();
 * 
 * for (Coordinate c = destination; c != null; c = engine.getPredecessor(c)) {
 * 	l.add(c);
 * }
 * 
 * Collections.reverse(l);
 * 
 * return l;
 * </pre>
 * 
 * @see #execute(Coordinate, Coordinate, boolean)
 * @author Renaud Waldura &lt;renaud+tw@waldura.com&gt;
 * @version $Id: DijkstraEngine.java 2379 2007-08-23 19:06:29Z renaud $
 */

public class MovementPath {
	/**
	 * Infinity value for distances.
	 */
	public static final int INFINITE_DISTANCE = Integer.MAX_VALUE;

	/**
	 * Some value to initialize the priority queue with.
	 */
	private static final int INITIAL_CAPACITY = 8;

	/** The distance for the last move possible. */
	private static int LAST_MOVE = 9998;
	private static int LAST_NODE = 9999;

	// getting rid of magic numbers
	private static int UNPASSABLE = Specs.UNPASSABLE;

	/**
	 * This comparator orders coordinates according to their shortest distances,
	 * in ascending fashion. If two coordinates have the same shortest distance,
	 * we compare the coordinates themselves.
	 */
	private final Comparator<Coordinate> shortestDistanceComparator = new Comparator<Coordinate>() {
		public int compare(Coordinate first, Coordinate second) {
			// note that this trick doesn't work for huge distances, close to
			// Integer.MAX_VALUE
			int result = getShortestDistance(first) - getShortestDistance(second);

			return result;
			// TODO look into
			// return (result == 0) ? left.compareTo(right) : result;
		}
	};

	/** The graph. */
	private final WeewarMap wmap;
	private final Unit unit;
	private final Game game;

	/** The working set of coordinates, kept ordered by shortest distance. */
	private final PriorityQueue<Coordinate> unsettledNodes = new PriorityQueue<Coordinate>(
			INITIAL_CAPACITY, shortestDistanceComparator);

	/**
	 * The set of coordinates for which the shortest distance to the source has
	 * been found.
	 */
	private final Set<Coordinate> settledNodes = new HashSet<Coordinate>();

	/** The currently known shortest distance for all coordinates. */
	private final Map<Coordinate, Integer> shortestDistances = new HashMap<Coordinate, Integer>();

	/**
	 * Predecessors list: maps a c to its predecessor in the spanning tree of
	 * shortest paths.
	 */
	private final Map<Coordinate, Coordinate> predecessors = new HashMap<Coordinate, Coordinate>();

	/** Constructor. */
	public MovementPath(WeewarMap wmap, Game detailed, Unit unit) {
		this.wmap = wmap;
		this.unit = unit;
		this.game = detailed;
	}

	/**
	 * Initialize all data structures used by the algorithm.
	 * 
	 * @param start
	 *            the source node
	 */
	private void init(Coordinate start) {
		settledNodes.clear();
		unsettledNodes.clear();

		shortestDistances.clear();
		predecessors.clear();

		// add source
		setShortestDistance(start, 0);
		unsettledNodes.add(start);
	}

	/**
	 * Run Dijkstra's shortest path algorithm on the map. The results of the
	 * algorithm are available through {@link #getPredecessor(Coordinate)} and
	 * {@link #getShortestDistance(Coordinate)} upon completion of this method.
	 * 
	 * @param start
	 *            the starting c
	 * @param destination
	 *            the destination c. If this argument is <code>null</code>, the
	 *            algorithm is run on the entire graph, instead of being stopped
	 *            as soon as the destination is reached.
	 * @param ignoreUnits
	 *            whether other units should be ignored
	 */
	public ArrayList<Coordinate> execute(Coordinate start,
			Coordinate destination, boolean ignoreUnits) {
		init(start);

		// the current node
		Coordinate u;

		// extract the node with the shortest distance
		while ((u = unsettledNodes.poll()) != null) {
			assert !isSettled(u);

			// destination reached, stop
			if (u == destination) {
				break;
			}

			settledNodes.add(u);

			relaxNeighbors(u, unit.getMovementPointsFirstMove(), ignoreUnits);
		}

		ArrayList<Coordinate> l = new ArrayList<Coordinate>();

		for (Coordinate c = destination; c != null; c = getPredecessor(c)) {
			l.add(c);
		}

		Collections.reverse(l);

		return l;
	}

	/**
	 * Compute new shortest distance for neighboring nodes and update if a
	 * shorter distance is found.
	 * 
	 * @param u
	 *            the node
	 */
	private void relaxNeighbors(Coordinate u, int maxMoveCost,
			boolean ignoreUnits) {
		Faction unitFaction = unit.getFaction();
		List<Unit> myUnits = unitFaction.getUnits();

		List<Coordinate> circle1 = u.getCircle(1);
		for (Coordinate v : circle1) {
			// if outside the map or node has already settled
			Terrain terrain = wmap.get(v);
			if (terrain == null || isSettled(v)) {
				continue;
			}

			int nextStepDistance = wmap.getDistanceForUnitType(u, v, unit);
			int shortDist = getShortestDistance(u) + nextStepDistance;
			// a bit redundant && check
			if ((shortDist < UNPASSABLE) && (nextStepDistance < UNPASSABLE)) {
				Unit unitOnWay = game.getUnit(v);
				if ((unitOnWay != null) && !ignoreUnits) {
					if (!myUnits.contains(unitOnWay)) {
						if (unit.canExertZocOn(unitOnWay)) {
							shortDist = UNPASSABLE;
						}
					}
				}

				// subs can not pass enemy bases
				if (unit.canEnterEnemyHarbor()) {
					Faction terrainOwner = game.getTerrainOwner(u);
					if ((terrainOwner != null)
							&& (!terrainOwner.equals(unitFaction))) {
						shortDist = UNPASSABLE;
					}
				}

				// ZOC - if v next to a unit and u was next to a unit
				if (!ignoreUnits
						&& (isNextToEnemyUnitThatCanExertZOC(v, unit, myUnits))) {
					if (maxMoveCost >= shortDist) {
						shortDist = Math.max(shortDist, LAST_MOVE);
					} else {
						shortDist = UNPASSABLE;
					}
				}
			}

			// if there is a real step possible
			if (shortDist < UNPASSABLE) {
				if (shortDist < getShortestDistance(v)) {
					// if (shortDist < maxMoveCost)
					{
						// assign new shortest distance and mark unsettled
						setShortestDistance(v, shortDist);

						// assign predecessor in shortest path
						setPredecessor(v, u);
					}
				}
			}
		}
	}

	private boolean isNextToEnemyUnitThatCanExertZOC(Coordinate v, Unit unit2,
			List<Unit> myUnits) {
		boolean result = false;
		List<Coordinate> circle = v.getCircle(1);
		for (Coordinate target : circle) {
			Unit unitOnWay = game.getUnit(target);
			if (unitOnWay != null) {
				if (!myUnits.contains(unitOnWay)) {
					if (unitOnWay.canExertZocOn(unit2)) {
						result = true;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Test a node.
	 * 
	 * @param v
	 *            the node to consider
	 * @return whether the node is settled, ie. its shortest distance has been
	 *         found.
	 */
	private boolean isSettled(Coordinate v) {
		return settledNodes.contains(v);
	}

	/**
	 * @return the shortest distance from the source to the given c, or
	 *         {@link MovementPath#INFINITE_DISTANCE} if there is no route to
	 *         the destination.
	 */
	public int getShortestDistance(Coordinate c) {
		Integer d = shortestDistances.get(c);
		return (d == null) ? INFINITE_DISTANCE : d;
	}

	/**
	 * Set the new shortest distance for the given node, and re-balance the
	 * queue according to new shortest distances.
	 * 
	 * @param c
	 *            the node to set
	 * @param distance
	 *            new shortest distance value
	 */
	private void setShortestDistance(Coordinate c, int distance) {
		/*
		 * This crucial step ensures no duplicates are created in the queue when
		 * an existing unsettled node is updated with a new shortest distance.
		 * Note: this operation takes linear time. If performance is a concern,
		 * consider using a TreeSet instead instead of a PriorityQueue.
		 * TreeSet.remove() performs in logarithmic time, but the PriorityQueue
		 * is simpler. (An earlier version of this class used a TreeSet.)
		 */
		unsettledNodes.remove(c);

		/*
		 * Update the shortest distance.
		 */
		shortestDistances.put(c, distance);

		/*
		 * Re-balance the queue according to the new shortest distance found
		 * (see the comparator the queue was initialized with).
		 */
		unsettledNodes.add(c);
	}

	/**
	 * @return the c leading to the given c on the shortest path, or
	 *         <code>null</code> if there is no route to the destination.
	 */
	public Coordinate getPredecessor(Coordinate c) {
		return predecessors.get(c);
	}

	private void setPredecessor(Coordinate a, Coordinate b) {
		predecessors.put(a, b);
	}

	/** How much does it cost to move from one location to another. */
	public static int calculateMovementCost(List<Coordinate> path,
			WeewarMap wmap2, Unit unit2) {
		int movementCost = 0;

		Coordinate lastStep = null;
		for (Coordinate step : path) {
			if (lastStep != null) {
				movementCost = movementCost
						+ wmap2.getDistanceForUnitType(lastStep, step, unit2);
			}
			lastStep = step;
		}

		return movementCost;
	}

	public static List<Coordinate> getShortestPathForUnit(WeewarMap wmap,
			Game detailed, Unit unit, Coordinate start, Coordinate destination,
			boolean ignoreUnits) {
		MovementPath shortestPath = new MovementPath(wmap, detailed, unit);
		List<Coordinate> resultList = shortestPath.execute(start, destination,
				ignoreUnits);
		return resultList;
	}

	public static List<Coordinate> getMovesAtCostLessThan(WeewarMap wmap2,
			Game detailed2, Unit unit2, Coordinate location, int maxMoveCost,
			int maxMoveRange, List<Coordinate> maxMoveCircle,
			boolean ignoreUnits) {
		MovementPath shortestPath = new MovementPath(wmap2, detailed2, unit2);
		List<Coordinate> resultList = shortestPath
				.getMovesAtCostLessThan(location, maxMoveCost, maxMoveRange,
						maxMoveCircle, ignoreUnits);
		return resultList;
	}

	private List<Coordinate> getMovesAtCostLessThan(Coordinate start,
			int maxMoveCost, int maxMoveRange, List<Coordinate> maxMoveCircle,
			boolean ignoreUnits) {
		init(start);

		// the current node
		Coordinate u;

		// extract the node with the shortest distance
		while ((u = unsettledNodes.poll()) != null) {
			assert !isSettled(u);

			// nothing still in range
			Integer shortestRemainingDistance = shortestDistances.get(u);
			// if (shortestRemainingDistance > maxMoveCost)
			if (shortestRemainingDistance > LAST_NODE) {
				break;
			}

			settledNodes.add(u);

			relaxNeighbors(u, maxMoveCost, ignoreUnits);
		}

		maxMoveCircle.remove(start);
		List<Coordinate> resultList = new LinkedList<Coordinate>();
		for (Coordinate destination : maxMoveCircle) {
			Integer shortestRemainingDistance = shortestDistances
					.get(destination);
			// an excluded location
			if (shortestRemainingDistance != null) {
				if (shortestRemainingDistance <= maxMoveCost) {
					resultList.add(destination);
				}

				if (shortestRemainingDistance == LAST_MOVE) {
					ArrayList<Coordinate> pathToDest = new ArrayList<Coordinate>();

					for (Coordinate c = destination; c != null; c = getPredecessor(c)) {
						pathToDest.add(c);
					}

					if ((maxMoveRange + 1) >= pathToDest.size()) {
						resultList.add(destination);
					}
				}
			}
		}
		return resultList;
	}
}
