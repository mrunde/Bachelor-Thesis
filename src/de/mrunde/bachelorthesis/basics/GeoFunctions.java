package de.mrunde.bachelorthesis.basics;

import com.mapquest.android.maps.GeoPoint;

/**
 * This class offers some geo-functions that are not contained in the MapQuest
 * API.
 * 
 * @author Marius Runde
 */
public abstract class GeoFunctions {

	/**
	 * Calculate the distance between two points in kilometers using the
	 * harvesine formula
	 * 
	 * @param pointA
	 *            First point
	 * @param pointB
	 *            Second point
	 * @return Distance between <code>pointA</code> and <code>pointB</code> in
	 *         kilometers
	 */
	public static double calculateDistanceBetweenPoints(GeoPoint pointA,
			GeoPoint pointB) {
		// Radius of the earth in km
		int R = 6371;

		double lat1 = pointA.getLatitude();
		double lat2 = pointB.getLatitude();
		double lng1 = pointA.getLongitude();
		double lng2 = pointB.getLongitude();

		double dLat = deg2rad(lat2 - lat1);
		double dLon = deg2rad(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return d;
	}

	/**
	 * Convert angle from degree to radiant
	 * 
	 * @param deg
	 *            Angle in degree
	 * @return Angle in radiant
	 */
	private static double deg2rad(double deg) {
		return deg * (Math.PI / 180);
	}
}
