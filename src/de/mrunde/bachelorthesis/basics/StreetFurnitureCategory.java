package de.mrunde.bachelorthesis.basics;

/**
 * Categories of street furniture (e.g. roundabout, stop sign)
 * 
 * @author Marius Runde
 */
public abstract class StreetFurnitureCategory {

	/**
	 * Bridge
	 */
	public final static String BRIDGE = "bridge";
	/**
	 * Crosswalk
	 */
	public final static String CROSSWALK = "crosswalk";

	/**
	 * Roundabout
	 */
	public final static String ROUNDABOUT = "roundabout";

	/**
	 * Stop sign
	 */
	public final static String STOP_SIGN = "stop_sign";

	/**
	 * Traffic light
	 */
	public final static String TRAFFIC_LIGHT = "traffic_light";

	/**
	 * Check if a category is a valid street furniture category
	 * 
	 * @param category
	 *            Category to be controlled
	 * @return TRUE: <code>category</code> is valid<br/>
	 *         FALSE: <code>category</code> is not valid
	 */
	public static boolean isCategory(String category) {
		if (category.equals(BRIDGE)) {
			return true;
		}
		if (category.equals(CROSSWALK)) {
			return true;
		}
		if (category.equals(ROUNDABOUT)) {
			return true;
		}
		if (category.equals(STOP_SIGN)) {
			return true;
		}
		if (category.equals(TRAFFIC_LIGHT)) {
			return true;
		}
		return false;
	}
}
