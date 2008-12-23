package weewarai.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

public class Game extends HqGame {

	private int round;
	private int baseIncome;
	private boolean hasPendingInvites;
	private int pace;
	private boolean isBasic;
	private boolean isRated = true;
	private boolean hasOnlyStandardUnits = false;

	private String currentPlayer;
	private List<String> players;
	private List<Faction> factions;
	private List<String> disabledUnitTypes;

	private int mapId;
	private int creditsPerBase;
	private int initialCredits;
	private Date startTime;

	public Game() {
		players = new LinkedList<String>();
		factions = new LinkedList<Faction>();
		disabledUnitTypes = new LinkedList<String>();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void parseXmlElement(Element ele) {

		super.parseXmlElement(ele);

		if (ele.getChildText("round") != null) { // round number
			setRound(Integer.parseInt(ele.getChildText("round")));
		}

		if (ele.getChildText("creditsPerBase") != null) {
			setBaseIncome(Integer.parseInt(ele.getChildText("creditsPerBase")));
		}

		if (ele.getChildText("pace") != null) { // 86400 = 1 day
			setPace(Integer.parseInt(ele.getChildText("pace")));
		}

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
				f.getStats().setIncomeFromCreditsPerBase(creditsPerBase); // more stats
			}
			setFactions(factions);
		}

		if (ele.getChildText("map") != null) { // map ID
			setMapId(Integer.parseInt(ele.getChildText("map")));
		}

		String type = ele.getChildText("type"); // basic or pro
		setBasic(type != null && type.equalsIgnoreCase("basic"));

		String rated = ele.getChildText("rated");
		setRated(rated != null && rated.equals("true"));

		if (ele.getChild("disabledUnitTypes") != null) {
			List<Element> disUnits = ele.getChild("disabledUnitTypes")
					.getChildren("type");
			List<String> d = new LinkedList<String>();
			for (Element disUnit : disUnits) {
				d.add(disUnit.getValue());
			}
			setDisabledUnitTypes(d);
		}
	}

	// ////////////////// Getters and Setters //////////////////////

	/**
	 * @return the round
	 */
	public int getRound() {
		return round;
	}

	/**
	 * @param round
	 *            the round to set
	 */
	public void setRound(int round) {
		this.round = round;
	}

	/**
	 * @return the baseIncome
	 */
	public int getBaseIncome() {
		return baseIncome;
	}

	/**
	 * @param baseIncome
	 *            the baseIncome to set
	 */
	public void setBaseIncome(int baseIncome) {
		this.baseIncome = baseIncome;
	}

	/**
	 * @return the hasPendingInvites
	 */
	public boolean isHasPendingInvites() {
		return hasPendingInvites;
	}

	/**
	 * @param hasPendingInvites
	 *            the hasPendingInvites to set
	 */
	public void setHasPendingInvites(boolean hasPendingInvites) {
		this.hasPendingInvites = hasPendingInvites;
	}

	/**
	 * @return the pace
	 */
	public int getPace() {
		return pace;
	}

	/**
	 * @param pace
	 *            the pace to set
	 */
	public void setPace(int pace) {
		this.pace = pace;
	}

	/**
	 * @return the isBasic
	 */
	public boolean isBasic() {
		return isBasic;
	}

	/**
	 * @param isBasic
	 *            the isBasic to set
	 */
	public void setBasic(boolean isBasic) {
		this.isBasic = isBasic;
	}

	/**
	 * @return the isRated
	 */
	public boolean isRated() {
		return isRated;
	}

	/**
	 * @param isRated
	 *            the isRated to set
	 */
	public void setRated(boolean isRated) {
		this.isRated = isRated;
	}

	/**
	 * @return the hasOnlyStandardUnits
	 */
	public boolean isHasOnlyStandardUnits() {
		return hasOnlyStandardUnits;
	}

	/**
	 * @param hasOnlyStandardUnits
	 *            the hasOnlyStandardUnits to set
	 */
	public void setHasOnlyStandardUnits(boolean hasOnlyStandardUnits) {
		this.hasOnlyStandardUnits = hasOnlyStandardUnits;
	}

	/**
	 * @return the currentPlayer
	 */
	public String getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * @param currentPlayer
	 *            the currentPlayer to set
	 */
	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	/**
	 * @return the players
	 */
	public List<String> getPlayers() {
		return players;
	}

	/**
	 * @param players
	 *            the players to set
	 */
	public void setPlayers(List<String> players) {
		this.players = players;
	}

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
	 * @return the disabledUnitTypes
	 */
	public List<String> getDisabledUnitTypes() {
		return disabledUnitTypes;
	}

	/**
	 * @param disabledUnitTypes
	 *            the disabledTypes to set
	 */
	public void setDisabledUnitTypes(List<String> disabledUnitTypes) {
		this.disabledUnitTypes = disabledUnitTypes;
	}

	/**
	 * @return the mapId
	 */
	public int getMapId() {
		return mapId;
	}

	/**
	 * @param mapId
	 *            the mapId to set
	 */
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	/**
	 * @return the creditsPerBase
	 */
	public int getCreditsPerBase() {
		return creditsPerBase;
	}

	/**
	 * @param creditsPerBase
	 *            the creditsPerBase to set
	 */
	public void setCreditsPerBase(int creditsPerBase) {
		this.creditsPerBase = creditsPerBase;
	}

	/**
	 * @return the initialCredits
	 */
	public int getInitialCredits() {
		return initialCredits;
	}

	/**
	 * @param initialCredits
	 *            the initialCredits to set
	 */
	public void setInitialCredits(int initialCredits) {
		this.initialCredits = initialCredits;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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
	 * Returns the Faction that has a Unit on the given Terrain
	 * 
	 * @param t
	 *            the Terrain
	 * @return the Faction that has a Unit on the given Terrain, or null if no
	 *         Faction has a Unit on the given Terrain
	 */
	public Faction getTerrainOwner(Terrain t) {
		for (Faction faction : getFactions()) {
			Unit u = faction.getUnit(t.getCoordinate());
			if (u != null) {
				return faction;
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
