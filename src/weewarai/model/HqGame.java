package weewarai.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

public class HqGame {

	private int id;
	private String name;
	
	private boolean isRequiringAnInviteAccept = false;
	private boolean isInNeedOfAttention;

	private String state;
	private String url;
	private int round;
	private int baseIncome;
	private boolean hasPendingInvites;
	private int pace;
	private boolean isBasic;
	private boolean isRated = true;
	private boolean hasOnlyStandardUnits = false;

	private String currentPlayer;
	private List<String> players;
	private List<String> disabledUnitTypes;

	private int mapId;
	private int creditsPerBase;
	private int initialCredits;
	private Date startTime;
	
	public HqGame() {
		players = new LinkedList<String>();
		disabledUnitTypes = new LinkedList<String>();
	}

	@SuppressWarnings("unchecked")
	public void parseXmlElement(Element ele) {
		String att = ele.getAttributeValue("inNeedOfAttention");
		setInNeedOfAttention(att != null && att.equals("true"));

		setId(Integer.parseInt(ele.getChildText("id")));

		setName(ele.getChildText("name"));

		// running, lobby, or finished
		setState(ele.getChildText("state"));

		setUrl(ele.getChildText("url"));

		setRequiringAnInviteAccept(getUrl().contains("join"));

		// from the hq xml...
		// there's also a <since> child
		// there's also a <result> field if game is finished
		
		if (ele.getChildText("round") != null) { // round number
			setRound(Integer.parseInt(ele.getChildText("round")));
		}

		if (ele.getChildText("creditsPerBase") != null) {
			setBaseIncome(Integer.parseInt(ele.getChildText("creditsPerBase")));
		}

		if (ele.getChildText("pace") != null) { // 86400 = 1 day
			setPace(Integer.parseInt(ele.getChildText("pace")));
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
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isRequiringAnInviteAccept
	 */
	public boolean isRequiringAnInviteAccept() {
		return isRequiringAnInviteAccept;
	}

	/**
	 * @param isRequiringAnInviteAccept
	 *            the isRequiringAnInviteAccept to set
	 */
	public void setRequiringAnInviteAccept(boolean isRequiringAnInviteAccept) {
		this.isRequiringAnInviteAccept = isRequiringAnInviteAccept;
	}

	/**
	 * @return the isInNeedOfAttention
	 */
	public boolean isInNeedOfAttention() {
		return isInNeedOfAttention;
	}

	/**
	 * @param isInNeedOfAttention
	 *            the isInNeedOfAttention to set
	 */
	public void setInNeedOfAttention(boolean isInNeedOfAttention) {
		this.isInNeedOfAttention = isInNeedOfAttention;
	}

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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
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
	public boolean hasPendingInvites() {
		return hasPendingInvites;
	}

	/**
	 * @param hasPendingInvites
	 *            the hasPendingInvites to set
	 */
	public void setPendingInvites(boolean hasPendingInvites) {
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
	public boolean hasOnlyStandardUnits() {
		return hasOnlyStandardUnits;
	}

	/**
	 * @param hasOnlyStandardUnits
	 *            the hasOnlyStandardUnits to set
	 */
	public void setOnlyStandardUnits(boolean hasOnlyStandardUnits) {
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
}
