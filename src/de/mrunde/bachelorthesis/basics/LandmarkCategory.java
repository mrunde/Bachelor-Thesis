package de.mrunde.bachelorthesis.basics;

/**
 * Categories of landmarks (e.g. gas station, hospital, post office)
 * 
 * @author Marius Runde
 */
public abstract class LandmarkCategory {

	/**
	 * Church
	 */
	public final static String CHURCH = "church";

	/**
	 * Gas station
	 */
	public final static String GAS_STATION = "gas station";

	/**
	 * Hospital
	 */
	public final static String HOSPITAL = "hospital";

	/**
	 * Monument - should be always replaced with the real name of the landmark
	 */
	public final static String MONUMENT = "monument";

	/**
	 * Post office
	 */
	public final static String POST_OFFICE = "post office";

	/**
	 * Train station
	 */
	public final static String TRAIN_STATION = "train station";

	/**
	 * Check if a category is a valid Landmark category
	 * 
	 * @param category
	 *            Category to be controlled
	 * @return TRUE: <code>category</code> is valid<br/>
	 *         FALSE: <code>category</code> is not valid
	 */
	public static boolean isCategory(String category) {
		if (category.equals(CHURCH))
			return true;
		if (category.equals(GAS_STATION))
			return true;
		if (category.equals(HOSPITAL))
			return true;
		if (category.equals(MONUMENT))
			return true;
		if (category.equals(POST_OFFICE))
			return true;
		if (category.equals(TRAIN_STATION))
			return true;
		return false;
	}
}
