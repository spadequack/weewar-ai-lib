package weewarai.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import weewarai.model.Specs;
import weewarai.model.Terrain;
import weewarai.model.Unit;
import weewarai.util.Debug;

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
	private Map<String, Integer> unitCountMap;
	private Map<String, Integer> capturableCountMap;

	private int income = 0;
	private int creditsPerBase;

	public FactionStats() {
		unitCountMap = new HashMap<String, Integer>();
		capturableCountMap = new HashMap<String, Integer>();
		reset();
	}

	public void reset() {
		for (String s : Unit.allUnits) {
			unitCountMap.put(s, 0);
		}
		for (String s : Terrain.capturableTerrains) {
			capturableCountMap.put(s, 0);
		}
		totalUnits = 0;
		armyStrength = 0;
		armyPotential = 0;
	}

	public void addUnit(Unit unit) {
		armyStrength += unit.getQuantity() / 10.0
				* Specs.buildCost.get(unit.getType());
		armyPotential += Specs.buildCost.get(unit.getType());
		armyHealth = armyStrength / armyPotential * 100;
		unitCountMap.put(unit.getType(), unitCountMap.get(unit.getType()) + 1);
		totalUnits++;
	}

	public Map<String, Integer> getUnitCountMap() {
		return unitCountMap;
	}

	public int getUnitCount(String unitType) {
		return unitCountMap.get(unitType);
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
		capturableCountMap.put(capturable.getType(), capturableCountMap
				.get(capturable.getType()) + 1);
	}

	public Map<String, Integer> getCapturableCountMap() {
		return capturableCountMap;
	}

	public int getCapturableCount(String baseType) {
		return capturableCountMap.get(baseType);
	}

	public void setIncomeFromCreditsPerBase(int creditsPerBase) {
		this.creditsPerBase = creditsPerBase;
		income = creditsPerBase * capturableCountMap.get("Base");
	}
	
	public void setIncomeFromCreditsPerBase() {
		income = creditsPerBase * capturableCountMap.get("Base");
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
	 * @param enemyStats 
	 * @return 
	 */
	public LinkedHashMap<String, Integer> compareUnitCounts(
			FactionStats enemyStats) {
		Map<String, Integer> enemyStatsMap = enemyStats.getUnitCountMap();
		Map<String, Integer> statsDifference = new HashMap<String, Integer>();

		for (String key : unitCountMap.keySet()) {
			statsDifference.put(key, unitCountMap.get(key)
					- enemyStatsMap.get(key));
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

	public void printStats() {
		Debug.print("---- Unit Stats ----");
		Debug.print(" -- Army values --");
		Debug.print("  Strength: " + getArmyStrength());
		Debug.print("  Potential: " + getArmyPotential());
		Debug.print("  Health: " + getArmyHealth());
		Debug.print("  Unit count: " + getTotalUnitCount());
		Debug.print(" -- Unit breakdown --");
		for (String unitType : getUnitCountMap().keySet()) {
			Debug.print(" " + unitType + ": " + getUnitCount(unitType));
		}
		Debug.print(" -- Base breakdown --");
		Set<String> allBaseTypes = getCapturableCountMap().keySet();
		for (String baseType : allBaseTypes) {
			Debug.print(" " + baseType + ": " + getCapturableCount(baseType));
		}
	}
	
	@Override
	public FactionStats clone() {
		FactionStats clone = new FactionStats();
		clone.armyHealth = armyHealth;
		clone.armyPotential = armyPotential;
		clone.armyStrength = armyStrength;
		clone.capturableCountMap = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> e : capturableCountMap.entrySet()) {
			capturableCountMap.put(e.getKey(), e.getValue());
		}
		clone.income = income;
		clone.totalUnits = totalUnits;
		clone.unitCountMap = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> e : unitCountMap.entrySet()) {
			unitCountMap.put(e.getKey(), e.getValue());
		}
		return clone;
	}
}
