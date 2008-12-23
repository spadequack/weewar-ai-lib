package weewarai.model;

public class Unit {

	private String type;
	private boolean finished;
	private Coordinate coordinate;
	private int quantity;
	private Faction faction;

	public Unit() {
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

}
