package weewarai.model;

import java.util.Collection;
import java.util.HashMap;
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
            Collection<Element> terrains = ele.getChild("terrains").
                    getChildren("terrain");
            for (Element terrain : terrains) {
                Terrain t = new Terrain();
                t.setCoordinate(new Coordinate(
                        Integer.parseInt(terrain.getAttributeValue("x")), 
                        Integer.parseInt(terrain.getAttributeValue("y"))));
                t.setType(terrain.getAttributeValue("type"));
                getTerrains().put(t.getCoordinate(), t);
            }
        }
    }
    
	// ////////////////// Getters and Setters //////////////////////

	/**
	 * @return the terrains
	 */
	public Map<Coordinate, Terrain> getTerrains() {
		return terrains;
	}

	/**
	 * @param terrains the terrains to set
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
	 * @param width the width to set
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
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}
