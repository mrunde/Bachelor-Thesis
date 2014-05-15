package de.mrunde.bachelorthesis.basics;

import com.mapquest.android.maps.GeoPoint;

/**
 * A route segment is the part of a route between two turn actions.
 * 
 * @author Marius Runde
 */
public class RouteSegment {

	/**
	 * Starting position
	 */
	private GeoPoint startPoint;

	/**
	 * Final position
	 */
	private GeoPoint endPoint;

	/**
	 * Maneuver type at the final position
	 */
	private int maneuverType;

	/**
	 * Distance between the starting and the final position
	 */
	private int distance;

	/**
	 * Constructor of the RouteSegment class
	 * 
	 * @param startPoint
	 *            Starting position
	 * @param endPoint
	 *            Final position
	 * @param maneuverType
	 *            Maneuver type at the final position
	 * @param distance
	 *            Distance between the starting and the final position
	 */
	public RouteSegment(GeoPoint startPoint, GeoPoint endPoint,
			int maneuverType, int distance) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.maneuverType = maneuverType;
		this.distance = distance;
	}

	/**
	 * @return The starting position
	 */
	public GeoPoint getStartPoint() {
		return startPoint;
	}

	/**
	 * @return The final position
	 */
	public GeoPoint getEndPoint() {
		return endPoint;
	}

	/**
	 * @return The maneuver type
	 */
	public int getManeuverType() {
		return maneuverType;
	}

	/**
	 * @return The distance between starting and final position
	 */
	public int getDistance() {
		return distance;
	}
}
