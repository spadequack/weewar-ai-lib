package weewarai.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jdom.Element;

public class Unit {

	private String type;
	private boolean finished;
	private int moveCount = 0;
	private Coordinate coordinate;
	private int quantity;
	private Faction faction;
	private boolean isBasic;

	// TODO consider enum

	// unit names
	public static final String Trooper = "Trooper";
	public static final String Heavy_Trooper = "Heavy Trooper";
	public static final String Raider = "Raider";
	public static final String Tank = "Tank";
	public static final String Heavy_Tank = "Heavy Tank";
	public static final String Light_Artillery = "Light Artillery";
	public static final String Heavy_Artillery = "Heavy Artillery";
	public static final String Capturing = "Capturing";

	public static final String Assault_Artillery = "Assault Artillery";
	public static final String Berserker = "Berserker";
	public static final String DFA = "D.F.A.";
	public static final String Anti_Aircraft = "Anti Aircraft";

	public static final String Hovercraft = "Hovercraft";
	public static final String Speedboat = "Speedboat";
	public static final String Destroyer = "Destroyer";
	public static final String Battleship = "Battleship";
	public static final String Submarine = "Submarine";

	public static final String Helicopter = "Helicopter";
	public static final String Jetfighter = "Jetfighter";
	public static final String Bomber = "Bomber";

	// unit type names
	public static final String Soft_Type = "soft";
	public static final String Hard_Type = "hard";
	public static final String Air_Type = "air";
	public static final String Amphibic_Type = "amphibic";
	public static final String Speedboat_Type = "speedboat";
	public static final String Boat_Type = "boat";
	public static final String Sub_Type = "sub";

	public static List<String> basicUnits = new ArrayList<String>();

	static {
		basicUnits.addAll(Arrays.asList(new String[] { Trooper, Heavy_Trooper,
				Raider, Tank, Heavy_Tank, Light_Artillery, Heavy_Artillery,
				Capturing }));
	}

	public static List<String> proUnits = new ArrayList<String>();

	static {
		proUnits.addAll(Arrays.asList(new String[] { Assault_Artillery,
				Berserker, DFA, Anti_Aircraft, Hovercraft, Speedboat,
				Destroyer, Battleship, Submarine, Helicopter, Jetfighter,
				Bomber }));
	}

	public static List<String> allUnits = new ArrayList<String>();

	static {
		allUnits.addAll(basicUnits);
		allUnits.addAll(proUnits);
	}

	// /////////////////////////// Methods /////////////////////////////

	public Unit() {
	}

	public void parseXmlElement(Element ele) {
		setCoordinate(new Coordinate(Integer.parseInt(ele
				.getAttributeValue("x")), Integer.parseInt(ele
				.getAttributeValue("y"))));
		String type = ele.getAttributeValue("type");
		setType(type);
		setBasic(basicUnits.contains(type));
		setFinished(ele.getAttributeValue("finished").equals("true"));
		setQuantity(Integer.parseInt(ele.getAttributeValue("quantity")));
	}

	/**
	 * Returns a String representation of a Unit in the form of: Trooper (6) on
	 * [15,14], not finished (ai_botbot)
	 * 
	 * @return a String representation of a Unit
	 */
	@Override
	public String toString() {
		return type + "(" + quantity + ") on " + coordinate + ", "
				+ (finished ? "" : "not ") + "finished  ("
				+ faction.getPlayerName() + ")";
	}

	/**
	 * Returns whether this unit is of the given unit type (soft, hard, etc.)
	 * 
	 * @param type
	 *            the type to be tested
	 * @return whether this unit is of the given unit type
	 */
	public boolean isOfUnitType(String type) {
		return getUnitType().equals(type);
	}

	/**
	 * Returns whether this unit can exert zone of control on the given unit. It
	 * can when 1) this unit can attack the given unit and 2) this unit's
	 * minimum range against the given unit is greater than 1
	 * 
	 * @param unit
	 *            the unit to be tested against
	 * @return whether this unit can exert ZoC on the given unit
	 */
	public boolean canExertZocOn(Unit unit) {
		return Specs.unitAttack.get(getType()).get(unit.getUnitType()) > 0
				&& Specs.unitMinRange.get(getType()).get(unit.getUnitType()) == 1;
	}

	/**
	 * Returns the unit type (soft, hard, etc.) of this unit
	 * 
	 * @return the unit type (soft, hard, etc.) of this unit
	 */
	public String getUnitType() {
		return Specs.unitTypes.get(getType());
	}

	public boolean canEnterEnemyHarbor() {
		return !Specs.unitsCannotEnterEnemyHarbor.contains(getType());
	}

	public int getMovementPoints() {
		if (moveCount == 0)
			return Specs.unitMobilityFirst.get(getType());
		else if (moveCount == 1)
			return Specs.unitMobilitySecond.get(getType());
		else {
			throw new RuntimeException("move count is invalid: " + moveCount);
		}
	}

	public int getMinRange(Unit target) {
		return Specs.unitMinRange.get(getType()).get(target.getUnitType());
	}

	public int getMaxRange(Unit target) {
		return Specs.unitMaxRange.get(getType()).get(target.getUnitType());
	}

	public int getMinRange() {
		return Collections.min(Specs.unitMinRange.get(getType()).values());
	}

	public int getMaxRange() {
		return Collections.max(Specs.unitMaxRange.get(getType()).values());
	}

	// ////////////////// Getters and Setters //////////////////////

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * @param finished
	 *            the finished to set
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * @return the coordinate
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * @param coordinate
	 *            the coordinate to set
	 */
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the faction
	 */
	public Faction getFaction() {
		return faction;
	}

	/**
	 * @param faction
	 *            the faction to set
	 */
	public void setFaction(Faction faction) {
		this.faction = faction;
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
	 * @return the moveCount
	 */
	public int getMoveCount() {
		return moveCount;
	}

	/**
	 * @param moveCount
	 *            the moveCount to set
	 */
	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}

	/**
	 * Increments the move count by 1
	 */
	public void incrementMoveCount() {
		moveCount++;
	}
}
