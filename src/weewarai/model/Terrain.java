package weewarai.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

public class Terrain {

	private String type;
	private Coordinate coordinate;
	private boolean finished;

	// TODO consider enum
	public static final String Airfield = "Airfield";
	public static final String Base = "Base";
	public static final String Desert = "Desert";
	public static final String Harbor = "Harbor";
	public static final String Mountains = "Mountains";
	public static final String Plains = "Plains";
	public static final String Repair_Patch = "Repair Patch";
	public static final String Swamp = "Swamp";
	public static final String Water = "Water";
	public static final String Woods = "Woods";

	public static final List<String> allTerrains = new LinkedList<String>();

	static {
		allTerrains
				.addAll(Arrays.asList(new String[] { Airfield, Base, Desert,
						Harbor, Mountains, Plains, Repair_Patch, Swamp, Water,
						Woods }));
	}

	private boolean isCapturable;
	public static final List<String> capturableTerrains = new LinkedList<String>();

	static {
		capturableTerrains.add(Airfield);
		capturableTerrains.add(Base);
		capturableTerrains.add(Harbor);
	}

	public Terrain() {
	}

	public void parseXmlElement(Element ele) {
		setCoordinate(new Coordinate(Integer.parseInt(ele
				.getAttributeValue("x")), Integer.parseInt(ele
				.getAttributeValue("y"))));
		String type = ele.getAttributeValue("type");
		setType(type);
		setCapturable(capturableTerrains.contains(type));
		setFinished(ele.getAttributeValue("finished").equals("true"));
	}

	/**
	 * Returns a string representation of a Terrain in the form of:
	 * (Swamp:[5,4])
	 * 
	 * @return a string representation of a Terrain
	 */
	@Override
	public String toString() {
		return "(" + type + ":" + coordinate + ")";
	}

	/**
	 * Returns the movement cost of the unit moving onto this terrain
	 * 
	 * @param unit
	 *            the moving unit
	 * @return the movement cost of the unit moving onto this terrain
	 */
	public int getMovementCost(Unit unit) {
		return Specs.terrainMovement.get(getType()).get(unit.getUnitType());
	}

	/**
	 * Returns the attack modifier of the given unit attacking from this terrain
	 * 
	 * @param unit
	 *            the unit
	 * @return the attack modifier of the given unit attacking from this terrain
	 */
	public int getAttackMod(Unit unit) {
		return Specs.terrainAttack.get(getType()).get(unit.getUnitType());
	}

	/**
	 * Returns the defense modifier of the given unit attacking from this
	 * terrain
	 * 
	 * @param unit
	 *            the unit
	 * @return the defense modifier of the given unit attacking from this
	 *         terrain
	 */
	public int getDefenseMod(Unit unit) {
		return Specs.terrainDefense.get(getType()).get(unit.getUnitType());
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Terrain))
			return false;
		Terrain t = (Terrain) o;
		return (getCoordinate().equals(t.getCoordinate()) && getType().equals(
				t.getType()));
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
	 * @return the isCapturable
	 */
	public boolean isCapturable() {
		return isCapturable;
	}

	/**
	 * @param isCapturable
	 *            the isCapturable to set
	 */
	public void setCapturable(boolean isCapturable) {
		this.isCapturable = isCapturable;
	}

}
