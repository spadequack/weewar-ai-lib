package weewarai.model;

import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

import weewarai.stats.FactionStats;

public class Faction {

	private String state;
	private String playerName;
	private int credits;
	private List<Unit> units;
	private List<Terrain> terrains;
	private int order; // 1-6 (1=blue, 2=red, etc.)
	private FactionStats stats;

	public Faction() {
		units = new LinkedList<Unit>();
		terrains = new LinkedList<Terrain>();
	}

	@SuppressWarnings("unchecked")
	public void parseXmlElement(Element ele) {
		setPlayerName(ele.getAttributeValue("playerName"));
		setState(ele.getAttributeValue("state"));
		if (ele.getAttributeValue("credits") != null) {
			setCredits(Integer.parseInt(ele.getAttributeValue("credits")));
		}
		units.clear();
		for (Element unitEle : (List<Element>) ele.getChildren("unit")) {
			Unit u = new Unit();
			u.parseXmlElement(unitEle);
			u.setFaction(this);
			getUnits().add(u);
			getStats().addUnit(u);
		}
		terrains.clear();
		for (Element terrainEle : (List<Element>) ele.getChildren("terrain")) {
			Terrain t = new Terrain();
			t.parseXmlElement(terrainEle);
			getTerrains().add(t);
			if (t.isCapturable())
				getStats().addCapturable(t);
		}
	}

	// ////////////////// Getters and Setters //////////////////////

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param playerName
	 *            the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * @return the credits
	 */
	public int getCredits() {
		return credits;
	}

	/**
	 * @param credits
	 *            the credits to set
	 */
	public void setCredits(int credits) {
		this.credits = credits;
	}

	/**
	 * @return the units
	 */
	public List<Unit> getUnits() {
		return units;
	}

	/**
	 * @param units
	 *            the units to set
	 */
	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	/**
	 * @return the terrains
	 */
	public List<Terrain> getTerrains() {
		return terrains;
	}

	/**
	 * @param terrains
	 *            the terrains to set
	 */
	public void setTerrains(List<Terrain> terrains) {
		this.terrains = terrains;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Returns the Unit of this Faction at the given Coordinate
	 * 
	 * @param c
	 *            the Coordinate
	 * @return the Unit of this Faction at the given Coordinate, or null if
	 *         there is no Unit at that Coordinate
	 */
	public Unit getUnit(Coordinate c) {
		for (Unit unit : units) {
			if (unit.getCoordinate().equals(c)) {
				return unit;
			}
		}
		return null;
	}

	/**
	 * @param stats the stats to set
	 */
	public void setStats(FactionStats stats) {
		this.stats = stats;
	}

	/**
	 * @return the stats
	 */
	public FactionStats getStats() {
		return stats;
	}

}
