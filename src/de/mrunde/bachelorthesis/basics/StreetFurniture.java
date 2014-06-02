package de.mrunde.bachelorthesis.basics;

import android.util.Log;

import com.mapquest.android.maps.GeoPoint;

/**
 * A StreetFurniture can be e.g. a roundabout or stop sign.
 * 
 * @author Marius Runde
 */
public class StreetFurniture {

	/**
	 * Central position
	 */
	private GeoPoint center;

	/**
	 * Category (must be from list of categories from StreetFurnitureCategory)
	 */
	private String category;

	/**
	 * Constructor of the StreetFurniture class
	 * 
	 * @param center
	 *            Central position
	 * @param category
	 *            Category (must be from list of categories from
	 *            LandmarkCategory)
	 */
	public StreetFurniture(GeoPoint center, String category) {
		this.center = center;
		if (StreetFurnitureCategory.isCategory(category)) {
			this.category = category;
		} else {
			Log.e("StreetFurniture",
					"Category is not correct and will be set to null");
			this.category = null;
		}
	}

	public String toString() {
		return "StreetFurniture({\"center\":{\"lat\":\""
				+ this.center.getLatitude() + "\",\"lng\":\""
				+ this.center.getLongitude() + "\"},\"category\":\""
				+ this.category + "\"});";
	}

	/**
	 * @return The central position
	 */
	public GeoPoint getCenter() {
		return center;
	}

	/**
	 * @return The category formatted so that "_" are replaced with spaces
	 */
	public String getCategory() {
		String formattedCategory = category.replace("_", " ");
		return formattedCategory;
	}
}
