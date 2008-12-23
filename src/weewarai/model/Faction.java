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
	private List<Terrain> capturedTerrains;
	private int order; // 1-6 (1=blue, 2=red, etc.)
	private FactionStats stats;

	public Faction() {
		units = new LinkedList<Unit>();
		capturedTerrains = new LinkedList<Terrain>();
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
		capturedTerrains.clear();
		for (Element terrainEle : (List<Element>) ele.getChildren("terrain")) {
			// get the list of captured terrains of this faction
			Terrain t = new Terrain();
			t.parseXmlElement(terrainEle);
			if (t.isCapturable()) {
				getCapturedTerrains().add(t);
				getStats().addCapturable(t);
			} else {
				System.out.println("That's weird... " + t.getType()
						+ " is owned by " + getPlayerName());
			}
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
	 * @return the capturedTerrains
	 */
	public List<Terrain> getCapturedTerrains() {
		return capturedTerrains;
	}

	/**
	 * @param capturedTerrains
	 *            the capturedTerrains to set
	 */
	public void setCapturedTerrains(List<Terrain> capturedTerrains) {
		this.capturedTerrains = capturedTerrains;
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
	 * @return the stats
	 */
	public FactionStats getStats() {
		return stats;
	}

	/**
	 * @param stats
	 *            the stats to set
	 */
	public void setStats(FactionStats stats) {
		this.stats = stats;
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

}
