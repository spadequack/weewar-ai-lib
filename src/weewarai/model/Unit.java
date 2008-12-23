package weewarai.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom.Element;

public class Unit {

	private String type;
	private boolean finished;
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

}
