package de.mrunde.bachelorthesis.basics;

/**
 * Categories of landmarks (e.g. gas station, hospital, post office)
 * 
 * @author Marius Runde
 */
public abstract class LandmarkCategory {

	/**
	 * Gas station
	 */
	public final static int GAS_STATION = 0;

	/**
	 * Hospital
	 */
	public final static int HOSPITAL = 1;

	/**
	 * Post office
	 */
	public final static int POST_OFFICE = 2;

	/**
	 * Fire station
	 */
	public final static int FIRE_STATION = 3;

	/**
	 * Train station
	 */
	public final static int TRAIN_STATION = 4;

	/**
	 * Test if a category belongs to one of the implemented categories
	 * 
	 * @param category
	 *            The category to be tested
	 * @return TRUE: category is correct<br/>
	 *         FALSE: category is not correct
	 */
	public static boolean isCategory(int category) {
		if (category < 0 || category > 4) {
			return false;
		} else {
			return true;
		}
	}
}
