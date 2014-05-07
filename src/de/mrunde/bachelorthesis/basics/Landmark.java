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
	 * The description is optional
	 */
	private String description;

	/**
	 * Central position
	 */
	private GeoPoint center;

	/**
	 * Category (must be from list of categories from LandmarkCategory)
	 */
	private int category;

	/**
	 * Constructor of the Landmark class
	 * 
	 * @param local
	 *            Indicator for local or global landmark
	 * @param title
	 *            Title
	 * @param center
	 *            Central position
	 * @param category
	 *            Category (must be from list of categories from
	 *            LandmarkCategory)
	 */
	public Landmark(boolean local, String title, GeoPoint center, int category) {
		this.local = local;
		this.title = title;
		this.description = null;
		this.center = center;

		// If the category is not correct, set it to -1
		if (LandmarkCategory.isCategory(category)) {
			this.category = category;
		} else {
			Log.e("Landmark", "Category is not correct and will be set to -1");
			this.category = -1;
		}
	}

	/**
	 * Constructor of the Landmark class with landmark description
	 * 
	 * @param local
	 *            Indicator for local or global landmark
	 * @param title
	 *            Title
	 * @param description
	 *            Description
	 * @param center
	 *            Central position
	 * @param category
	 *            Category (must be from list of categories from
	 *            LandmarkCategory)
	 */
	public Landmark(boolean local, String title, String description,
			GeoPoint center, int category) {
		this.local = local;
		this.title = title;
		this.description = description;
		this.center = center;

		// If the category is not correct, set it to -1
		if (LandmarkCategory.isCategory(category)) {
			this.category = category;
		} else {
			Log.e("Landmark", "Category is not correct and will be set to -1");
			this.category = -1;
		}
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
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return The central position
	 */
	public GeoPoint getCenter() {
		return center;
	}

	/**
	 * @return the category
	 */
	public int getCategory() {
		return category;
	}
}
