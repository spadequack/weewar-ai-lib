package weewarai.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import weewarai.model.Coordinate;
import weewarai.model.Faction;
import weewarai.model.Specs;
import weewarai.model.Terrain;
import weewarai.model.Unit;

/**
 * @author pluto, spadequack
 */
public class FactionStats {

	// army strength equals unit quantity * cost
	private float armyStrength = 0;

	// army potential is the army strength if all units
	private int armyPotential = 0;

	// health of the whole army equals strength/potential
	private float armyHealth = 0;

	private int totalUnits = 0;
	private Map<String, Integer> unitCount;
	private Map<String, Integer> capturableCount;

	private int income = 0;

	public FactionStats() {
		unitCount = new HashMap<String, Integer>();
		for (String s : Unit.allUnits) {
			unitCount.put(s, 0);
		}
		capturableCount = new HashMap<String, Integer>();
		for (String s : Terrain.capturableTerrains) {
			capturableCount.put(s, 0);
		}
	}

	public FactionStats(Faction faction, List<Coordinate> coords) {
		for (Coordinate coord : coords) {
			Unit unit = faction.getUnit(coord);
			if (unit != null) {
				addUnit(unit);
			}
		}
	}

	public void addUnit(Unit unit) {
		armyStrength += unit.getQuantity() / 10.0
				* Specs.buildCost.get(unit.getType());
		armyPotential += Specs.buildCost.get(unit.getType());
		armyHealth = armyStrength / armyPotential * 100;
		unitCount.put(unit.getType(), unitCount.get(unit.getType()) + 1);
		totalUnits++;
	}

	public int getUnitCount(String unitType) {
		return unitCount.get(unitType);
	}

	public double getArmyStrength() {
		return armyStrength;
	}

	public int getArmyPotential() {
		return armyPotential;
	}

	public double getArmyHealth() {
		return armyHealth;
	}

	public int getTotalUnitCount() {
		return totalUnits;
	}

	public void addCapturable(Terrain capturable) {
		capturableCount.put(capturable.getType(), capturableCount
				.get(capturable.getType()) + 1);
	}

	public int getCapturableCount(String baseType) {
		return capturableCount.get(baseType);
	}

	public void setIncomeFromCreditsPerBase(int creditsPerBase) {
		income = creditsPerBase * capturableCount.get("Base");
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getIncome() {
		return income;
	}

	/**
	 * Takes the unit counts of your enemy and compares them to yours to see
	 * where you are deficient. Subtracts the passed enemy unitCounts from yours
	 * and then sorts the counts in ascending order
	 */
	public LinkedHashMap<String, Integer> compareUnitCounts(
			Map<String, Integer> enemyStats) {

		Map<String, Integer> statsDifference = new HashMap<String, Integer>();

		for (String key : this.unitCount.keySet()) {
			statsDifference.put(key, this.unitCount.get(key)
					- enemyStats.get(key));
		}

		// sorts the new difference map so you can see where you are most
		// deficient
		return sortHashMapByValues(statsDifference, true);
	}

	/**
	 * Sorts a HashMap by its values. (A SortedMap can only be sorted by its
	 * keys) I got this algorithm from
	 * http://www.theserverside.com/discussions/thread.tss?thread_id=29569, by
	 * Tim Ringwood
	 */
	private LinkedHashMap<String, Integer> sortHashMapByValues(
			Map<String, Integer> passedMap, boolean ascending) {

		List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
		List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
		Collections.sort(mapKeys);
		Collections.sort(mapValues);

		if (!ascending) {
			Collections.reverse(mapValues);
		}

		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

		for (Integer val : mapValues) {
			for (String key : mapKeys) {
				if (passedMap.get(key) == val) {
					passedMap.remove(key);
					mapKeys.remove(key);
					sortedMap.put(key, val);
					break;
				}
			}
		}

		return sortedMap;
	}
}
