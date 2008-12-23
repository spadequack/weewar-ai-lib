package weewarai.model;

import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

public class Game extends HqGame {

	private List<Faction> factions;

	public Game() {
		super();
		factions = new LinkedList<Faction>();
	}

	@Override
	public void parseXmlElement(Element ele) {
		parseXmlElement(ele, true);
	}
	
	@SuppressWarnings("unchecked")
	public void parseXmlElement(Element ele, boolean loadNonFactionData) {
		if (loadNonFactionData)
			super.parseXmlElement(ele);
		
		if (ele.getChild("factions") != null) {
			List<Element> factionEles = ele.getChild("factions").getChildren(
					"faction");
			int i = 1;
			List<Faction> factions = new LinkedList<Faction>();
			for (Element factionEle : factionEles) {
				Faction f = new Faction();
				factions.add(f);
				f.setOrder(i++);
				f.parseXmlElement(factionEle);
				f.getStats().setIncomeFromCreditsPerBase(getCreditsPerBase());
			}
			setFactions(factions);
		}
	}


	// ////////////////// Getters and Setters //////////////////////

	/**
	 * @return the factions
	 */
	public List<Faction> getFactions() {
		return factions;
	}

	/**
	 * @param factions
	 *            the factions to set
	 */
	public void setFactions(List<Faction> factions) {
		this.factions = factions;
	}

	/**
	 * Returns the faction of the given player name
	 * 
	 * @param name
	 *            the player name
	 * @return the faction of the given player name, or null if there is no
	 *         faction with that player name
	 */
	public Faction getFactionByPlayerName(String name) {
		for (Faction faction : getFactions()) {
			if (faction.getPlayerName().equals(name)) {
				return faction;
			}
		}
		return null;
	}

	/**
	 * Returns the Unit at the given Coordinate
	 * 
	 * @param c
	 *            the Coordinate
	 * @return the Unit at the given Coordinate, or null if there is no Unit at
	 *         that Coordinate
	 */
	public Unit getUnit(Coordinate c) {
		for (Faction faction : getFactions()) {
			Unit u = faction.getUnit(c);
			if (u != null) {
				return u;
			}
		}
		return null;
	}

	/**
	 * Returns the Faction that owns the given Terrain - only makes sense for
	 * capturable Terrain types
	 * 
	 * @param t
	 *            the Terrain
	 * @return the Faction that owns the given Terrain, or null if no Faction
	 *         owns the given Terrain
	 */
	public Faction getTerrainOwner(Terrain t) {
		for (Faction faction : getFactions()) {
			System.out.println("ARGH");
			System.out.println(faction.getPlayerName());
			System.out.println(faction.getCapturedTerrains());
			System.out.println("testing: " + t);
			if (faction.getCapturedTerrains().contains(t)) {
				return faction;
			}
		}
		return null;
	}

	/**
	 * Returns the Faction that owns the given Coordinate - only makes sense for
	 * Coordinates that have capturable Terrain types
	 * 
	 * @param c
	 *            the Coordinate
	 * @return the Faction that owns the given Coordinate, or null if no Faction
	 *         owns the given Coordinate
	 */
	public Faction getTerrainOwner(Coordinate c) {
		for (Faction faction : getFactions()) {
			for (Terrain t : faction.getCapturedTerrains()) {
				if (t.getCoordinate().equals(c)) {
					return faction;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the total number of units currently on the map
	 * 
	 * @return the total number of units currently on the map
	 */
	public int getUnitCount() {
		int s = 0;
		for (Faction faction : getFactions()) {
			s += faction.getUnits().size();
		}
		return s;
	}

}
