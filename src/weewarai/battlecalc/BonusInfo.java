package weewarai.battlecalc;

import java.util.LinkedList;
import java.util.List;

import weewarai.model.Coordinate;

public class BonusInfo {

	private List<Coordinate> attackFromLocations;
	private Coordinate attackedCoordinate;

	public BonusInfo(Coordinate attackTo) {
		attackFromLocations = new LinkedList<Coordinate>();
		attackedCoordinate = attackTo;
	}

	@Override
	public String toString() {
		return "Bonus Info: Attack: " + attackedCoordinate + " from : "
				+ attackFromLocations;
	}

	/**
	 * Add a coordinate that the attacked unit (at attackedCoordinate) has been
	 * attacked from to the list
	 * 
	 * @param c
	 *            the coordinate from which the attacked unit was attacked
	 */
	public void addAttackFromLocation(Coordinate c) {
		attackFromLocations.add(c);
	}

	/**
	 * Calculates the bonus if a unit a Coordinate c were to attack this unit
	 * (at attackedCoordinate)
	 * 
	 * @param c
	 *            the coordinate from which the attacked unit would
	 *            hypothetically be attacked
	 * @return the bonus this attack would get
	 */
	public int calculateBonus(Coordinate c) {
		// System.out.println("       .. calculating bonus on attack from " + c
		// +
		// " to " + attackedCoordinate);
		int bonus = 0;
		for (Coordinate old : attackFromLocations) {
			Coordinate.Direction oldDir = attackedCoordinate.getDirection(old);
			Coordinate.Direction newDir = attackedCoordinate.getDirection(c);
			// System.out.println("Old dir: " + oldDir + ", New dir: " +
			// newDir);
			if (oldDir == newDir) {
				return 0;
			} else if (oldDir == Coordinate.Direction.RANGED) {
				// System.out.println("Ranged hit from: " + old);
				bonus += 1;
			} else if (oldDir == Coordinate.clockwise(newDir)
					|| newDir == Coordinate.clockwise(oldDir)) {
				// System.out.println("Adjacent hit from: " + old);
				bonus += 1; // adjacent attack
			} else if (oldDir == Coordinate.oppositeDirection(newDir)) {
				// System.out.println("Opposite hit from: " + old);
				bonus += 3;
			} else {
				// System.out.println("Other hit from: " + old);
				bonus += 2;
			}
		}
		// System.out.println("          .. bonus: " + bonus);
		return bonus;
	}
}