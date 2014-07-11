package de.mrunde.bachelorthesis.basics;

import de.mrunde.bachelorthesis.R;

/**
 * Categories of landmarks (e.g. gas station, hospital, post office)
 * 
 * @author Marius Runde
 */
public abstract class LandmarkCategory {

	/**
	 * Cemetery (global)
	 */
	public final static String CEMETERY = "cemetery";

	/**
	 * Harbour (global)
	 */
	public final static String HARBOUR = "harbour";

	/**
	 * Lake (global)
	 */
	public final static String LAKE = "lake";

	/**
	 * Church
	 */
	public final static String CHURCH = "church";

	/**
	 * Cinema
	 */
	public final static String CINEMA = "cinema";

	/**
	 * Restaurant
	 */
	public final static String RESTAURANT = "restaurant";

	/**
	 * Shop
	 */
	public final static String SHOP = "shop";

	/**
	 * Sightseeing (must always be replaced with the real name in the guidance)
	 */
	public final static String SIGHTSEEING = "sightseeing";

	/**
	 * Check if a category is a valid landmark category
	 * 
	 * @param category
	 *            Category to be controlled
	 * @return TRUE: <code>category</code> is valid<br/>
	 *         FALSE: <code>category</code> is not valid
	 */
	public static boolean isCategory(String category) {
		if (category.equals(CEMETERY))
			return true;
		if (category.equals(HARBOUR))
			return true;
		if (category.equals(LAKE))
			return true;
		if (category.equals(CHURCH))
			return true;
		if (category.equals(CINEMA))
			return true;
		if (category.equals(RESTAURANT))
			return true;
		if (category.equals(SHOP))
			return true;
		if (category.equals(SIGHTSEEING))
			return true;
		return false;
	}

	/**
	 * Get all landmark categories
	 * 
	 * @return All categories
	 */
	public static String[] getCategories() {
		String[] categories = { CEMETERY, HARBOUR, LAKE, CHURCH, CINEMA,
				RESTAURANT, SHOP, SIGHTSEEING };
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
			if (category.equals(CEMETERY))
				// Source of image file:
				// http://www.flaticon.com/free-icon/halloween-cemetery_12010
				return R.drawable.landmark_cemetery;
			if (category.equals(HARBOUR))
				// Source of image file:
				// http://www.flaticon.com/free-icon/anchor-white-shape-inside-a-black-rounded-square_27592
				return R.drawable.landmark_harbour;
			if (category.equals(LAKE))
				// Source of image file:
				// http://www.flaticon.com/free-icon/sea_3491
				return R.drawable.landmark_lake;
			if (category.equals(CHURCH))
				// Source of image file:
				// http://www.flaticon.com/free-icon/church-black-silhouette-with-a-cross-on-top_34127
				return R.drawable.landmark_church;
			if (category.equals(CINEMA))
				// Source of image file:
				// http://www.flaticon.com/free-icon/film-strip-with-play-symbol_48406
				return R.drawable.landmark_cinema;
			if (category.equals(RESTAURANT))
				// Source of image file:
				// http://www.flaticon.com/free-icon/fork-and-knife-cutlery-circle-interface-symbol-for-restaurant_45605
				return R.drawable.landmark_restaurant;
			if (category.equals(SHOP))
				// Source of image file:
				// http://www.flaticon.com/free-icon/shopping-cart-1_2772
				return R.drawable.landmark_shop;
			if (category.equals(SIGHTSEEING))
				// Source of image file:
				// http://www.flaticon.com/free-icon/greek-temple-monument_482
				return R.drawable.landmark_sightseeing;
		}
		return -1;
	}
}
