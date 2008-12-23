package weewarai.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom.Element;

public class WeewarMap {

	private Map<Coordinate, Terrain> terrains;
	private int width;
	private int height;

	public WeewarMap() {
		terrains = new HashMap<Coordinate, Terrain>();
	}

	@SuppressWarnings("unchecked")
	public void parseXmlElement(Element ele) {
		setWidth(Integer.parseInt(ele.getChildText("width")));
		setHeight(Integer.parseInt(ele.getChildText("height")));
		if (ele.getChild("terrains") != null) {
			Collection<Element> terrains = ele.getChild("terrains")
					.getChildren("terrain");
			for (Element terrain : terrains) {
				Terrain t = new Terrain();
				t.setCoordinate(new Coordinate(Integer.parseInt(terrain
						.getAttributeValue("x")), Integer.parseInt(terrain
						.getAttributeValue("y"))));
				t.setType(terrain.getAttributeValue("type"));
				getTerrains().put(t.getCoordinate(), t);
			}
		}
	}

	/**
	 * Returns the movement cost for the given unit from the start coordinate to
	 * the end coordinate
	 * 
	 * @param start
	 *            the start Coordinate
	 * @param end
	 *            the end Coordinate
	 * @param unit
	 *            the moving unit
	 * @return the movement cost for the given unit from the start coordinate to
	 *         the end coordinate
	 */
	public int getDistanceForUnitType(Coordinate start, Coordinate end,
			Unit unit) {
		int distance = Integer.MAX_VALUE;

		if (start.getDistanceInStraightLine(end) == 1) {
			Terrain terrain = get(end);
			// terrain must be on the map
			if (terrain != null) {
				distance = terrain.getMovementCost(unit);
			}
		} else {
			throw new RuntimeException("dont support step of more than 1");
		}
		return distance;
	}

	// ////////////////// Getters and Setters //////////////////////

	/**
	 * @return the terrains
	 */
	public Map<Coordinate, Terrain> getTerrains() {
		return terrains;
	}

	/**
	 * @param terrains
	 *            the terrains to set
	 */
	public void setTerrains(Map<Coordinate, Terrain> terrains) {
		this.terrains = terrains;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Returns the Terrain at the given Coordinate
	 * 
	 * @param c
	 *            the Coordinate
	 * @return the Terrain at the given Coordinate
	 */
	public Terrain get(Coordinate c) {
		return terrains.get(c);
	}

	/**
	 * Returns all the terrains in the map of a given type
	 * 
	 * @param type
	 *            the terrain type
	 * @return all the terrains in the map of a given type
	 */
	public Collection<Terrain> getTerrainsByType(String type) {
		Collection<Terrain> myTerrains = new LinkedList<Terrain>();
		for (Terrain t : getTerrains().values()) {
			if (t.getType().equals(type)) {
				myTerrains.add(t);
			}
		}
		return myTerrains;
	}
}
