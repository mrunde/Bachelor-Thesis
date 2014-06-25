package de.mrunde.bachelorthesis.basics;

import de.mrunde.bachelorthesis.R;

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
	 * Harbour
	 */
	public final static String HARBOUR = "harbour";

	/**
	 * Lake or water body in general
	 */
	public final static String LAKE = "lake";

	/**
	 * Restaurant
	 */
	public final static String RESTAURANT = "restaurant";

	/**
	 * Shop
	 */
	public final static String SHOP = "shop";

	/**
	 * Check if a category is a valid landmark category
	 * 
	 * @param category
	 *            Category to be controlled
	 * @return TRUE: <code>category</code> is valid<br/>
	 *         FALSE: <code>category</code> is not valid
	 */
	public static boolean isCategory(String category) {
		if (category.equals(CHURCH))
			return true;
		if (category.equals(HARBOUR))
			return true;
		if (category.equals(LAKE))
			return true;
		if (category.equals(RESTAURANT))
			return true;
		if (category.equals(SHOP))
			return true;
		return false;
	}

	/**
	 * Get all landmark categories
	 * 
	 * @return All categories
	 */
	public static String[] getCategories() {
		String[] categories = { CHURCH, HARBOUR, LAKE, RESTAURANT, SHOP };
		return categories;
	}

	/**
	 * Get the id of the corresponding image file for this landmark category
	 * 
	 * @param category
	 *            The landmark category
	 * @return The id of the corresponding image file. -1 if
	 *         <code>category</category> is not a valid landmark category
	 */
	public static int getDrawableId(String category) {
		if (isCategory(category)) {
			if (category.equals(CHURCH))
				return R.drawable.landmark_church;
			if (category.equals(HARBOUR))
				return -1; // TODO
			if (category.equals(LAKE))
				return -1; // TODO
			if (category.equals(RESTAURANT))
				return -1; // TODO
			if (category.equals(SHOP))
				return -1; // TODO
		}
		return -1;
	}
}
