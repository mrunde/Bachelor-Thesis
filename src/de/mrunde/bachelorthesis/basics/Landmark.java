package de.mrunde.bachelorthesis.basics;

import android.util.Log;

import com.mapquest.android.maps.GeoPoint;

/**
 * The Landmark class represents local and global landmarks.
 * 
 * @author Marius Runde
 */
public class Landmark {

	/**
	 * Indicator for local or global landmark
	 */
	private boolean local;

	/**
	 * Title
	 */
	private String title;

	/**
	 * Central position
	 */
	private GeoPoint center;

	/**
	 * Radius of visibility in meters
	 */
	private int radius;

	/**
	 * Category (must be from list of categories from LandmarkCategory)
	 */
	private String category;

	/**
	 * Constructor of the Landmark class
	 * 
	 * @param local
	 *            Indicator for local or global landmark
	 * @param title
	 *            Title
	 * @param center
	 *            Central position
	 * @param radius
	 *            Radius of visibility in meters
	 * @param category
	 *            Category (must be from list of categories from
	 *            LandmarkCategory)
	 */
	public Landmark(boolean local, String title, GeoPoint center, int radius,
			String category) {
		this.local = local;
		this.title = title;
		this.center = center;
		this.radius = radius;

		// If the category is not correct, set it to -1
		if (LandmarkCategory.isCategory(category)) {
			this.category = category;
		} else {
			Log.e("Landmark", "Category is not correct and will be set to null");
			this.category = null;
		}
	}

	public String toString() {
		return "Landmark({\"local\":\"" + this.local + "\",\"title\":\""
				+ this.title + "\",\"center\":{\"lat\":\""
				+ this.center.getLatitude() + "\",\"lng\":\""
				+ this.center.getLongitude() + "\"},\"radius\":" + this.radius
				+ ",\"category\":\"" + this.category + "\"});";
	}

	/**
	 * @return The indicator whether this is a local or global landmark
	 */
	public boolean isLocal() {
		return local;
	}

	/**
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return The central position
	 */
	public GeoPoint getCenter() {
		return center;
	}

	/**
	 * @return The radius of visibility in meters
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
}
