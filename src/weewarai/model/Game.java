package weewarai.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import weewarai.battlecalc.BonusInfo;
import weewarai.util.Util;

public class Game extends HqGame {

	private List<Faction> factions;

	// game is associated with a player
	private String username;
	private Faction myFaction;
	private Map<Coordinate, BonusInfo> bonusMap;
	private boolean isOver = false;

	public Game(String username) {
		super();
		factions = new LinkedList<Faction>();
		this.username = username;
	}

	// TODO bah fix OO-ness
	public void initBonusMap() {
		// initialize the map for storing attack bonus info
		bonusMap = new HashMap<Coordinate, BonusInfo>();
		for (Coordinate c : Util.getEnemyUnits(this, myFaction)) {
			bonusMap.put(c, new BonusInfo(c));
		}
	}

	public Faction getMyFaction() {
		return myFaction;
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

		myFaction = getFactionByPlayerName(username);
		initBonusMap();
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
	 * Returns the same unit as u that resides in memory as linked with this
	 * game, for cloned unit uses
	 * 
	 * @param u
	 * @return reference to this game's copy of unit u
	 */
	public Unit getSameUnit(Unit u) {
		for (Faction faction : getFactions()) {
			if (u.getFaction().getPlayerName().equals(faction.getPlayerName())) {
				Unit unit = faction.getUnit(u.getCoordinate());
				if (unit != null) {
					return unit;
				}
			}
		}

		System.out.println(u);
		System.out.println(getUnit(u.getCoordinate()));
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

	/**
	 * @return the bonusMap
	 */
	public Map<Coordinate, BonusInfo> getBonusMap() {
		return bonusMap;
	}

	/**
	 * @param bonusMap
	 *            the bonusMap to set
	 */
	public void setBonusMap(Map<Coordinate, BonusInfo> bonusMap) {
		this.bonusMap = bonusMap;
	}

	public boolean isOver() {
		return isLost() || isWon();
	}

	public boolean isLost() {
		Faction myFaction = getFactionByPlayerName(username);
		return myFaction.getCapturedTerrains().isEmpty()
				&& myFaction.getUnits().isEmpty();
	}

	public boolean isWon() {
		for (Faction f : factions) {
			if (!f.getPlayerName().equals(username)) {
				if (!(f.getCapturedTerrains().isEmpty() && f.getUnits()
						.isEmpty()))
					return false;
			}
		}
		return true;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

	@Override
	public Game clone() {
		Game clone = new Game(username);
		clone.factions = new LinkedList<Faction>();
		for (Faction f : factions) {
			clone.factions.add(f.clone());
		}
		clone.setBasic(isBasic());
		clone.setCreditsPerBase(getCreditsPerBase());
		clone.setCurrentPlayer(getCurrentPlayer());
		clone.setDisabledUnitTypes(getDisabledUnitTypes());
		clone.setId(getId());
		clone.setInitialCredits(getInitialCredits());
		clone.setInNeedOfAttention(isInNeedOfAttention());
		clone.setMapId(getMapId());
		clone.setName(getName());
		clone.setOnlyStandardUnits(hasOnlyStandardUnits());
		clone.setPace(getPace());
		clone.setPendingInvites(hasPendingInvites());
		clone.setPlayers(new LinkedList<String>(getPlayers()));
		clone.setRated(isRated());
		clone.setRequiringAnInviteAccept(isRequiringAnInviteAccept());
		clone.setRound(getRound());
		clone.setStartTime(getStartTime());
		clone.setState(getState());
		clone.setUrl(getUrl());
		clone.bonusMap = new HashMap<Coordinate, BonusInfo>();
		for (Map.Entry<Coordinate, BonusInfo> e : bonusMap.entrySet()) {
			clone.bonusMap.put(e.getKey().clone(), e.getValue().clone());
		}
		return clone;
	}

	@Override
	public int hashCode() {
		// hashing only uses faction data
		int hash = 1;
		if (factions.size() >= 6)
			hash += 13 * factions.get(5).hashCode();
		if (factions.size() >= 5)
			hash += 11 * factions.get(4).hashCode();
		if (factions.size() >= 4)
			hash += 7 * factions.get(3).hashCode();
		if (factions.size() >= 3)
			hash += 5 * factions.get(2).hashCode();
		if (factions.size() >= 2)
			hash += 3 * factions.get(1).hashCode();
		hash += 2 * factions.get(0).hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null & o instanceof Game) {
			Game g = (Game) o;
			return factions.equals(g.factions);
		}
		return false;
	}

	@Override
	public String toString() {
		String s = getName() + "\n";
		for (Faction f : factions) {
			s += f.getPlayerName() + "\n";
			s += "-- " + f.getUnits() + "\n";
			s += "-- " + f.getCapturedTerrains() + "\n";
		}
		return s;
	}

}
