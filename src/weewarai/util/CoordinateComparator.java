package weewarai.util;

import java.util.Comparator;

import weewarai.model.Coordinate;

/**
 * Sort Coordinate by x then by y
 * @author Me
 *
 */
public class CoordinateComparator implements Comparator<Coordinate> {

	@Override
	public int compare(Coordinate o1, Coordinate o2) {
		int diffX = o1.getX()-o2.getX();
		if (diffX == 0)
			return o1.getY() - o2.getY();
		return diffX;
	}
}