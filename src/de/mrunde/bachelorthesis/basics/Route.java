package de.mrunde.bachelorthesis.basics;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mapquest.android.maps.GeoPoint;

/**
 * The Route class stores all information about route segments, maneuvers,
 * locations etc.
 * 
 * @author Marius Runde
 */
public class Route {

	/**
	 * Value to check if the JSON import succeeded
	 */
	private boolean importSuccessful;

	/**
	 * These are the route segments between each turn action
	 */
	private List<RouteSegment> segments;

	/**
	 * Store the currently used segment
	 */
	private int currentSegment;

	/**
	 * Constructor of the Route class
	 * 
	 * @param json
	 *            Guidance object returned by the MapQuest API
	 */
	public Route(JSONObject json) {
		// Some temporal variables
		int[] maneuvers;
		int[] linkIndexes;
		GeoPoint[] decisionPoints;
		double[] distances;
		int[] shapePointIndexes;

		// Extract the guidance information out of the raw JSON file
		try {
			JSONObject guidance = json.getJSONObject("guidance");

			// --- Get the maneuver types and link indexes ---
			// First store them in a temporal List
			List<Integer> tempManeuverList = new ArrayList<Integer>();
			List<Integer> tempLinkList = new ArrayList<Integer>();
			JSONArray guidanceNodeCollection = guidance
					.getJSONArray("GuidanceNodeCollection");
			for (int i = 0; i < guidanceNodeCollection.length(); i++) {
				if ((guidanceNodeCollection.getJSONObject(i))
						.has("maneuverType")) {
					tempManeuverList.add(guidanceNodeCollection
							.getJSONObject(i).getInt("maneuverType"));
					tempLinkList.add(guidanceNodeCollection.getJSONObject(i)
							.getJSONArray("linkIds").getInt(0));
				}
			}
			// Then store them in an array
			maneuvers = new int[tempManeuverList.size()];
			linkIndexes = new int[tempLinkList.size()];
			for (int i = 0; i < maneuvers.length; i++) {
				maneuvers[i] = tempManeuverList.get(i);
				linkIndexes[i] = tempLinkList.get(i);
			}

			// --- Get the decision points ---
			JSONArray shapePoints = guidance.getJSONArray("shapePoints");
			decisionPoints = new GeoPoint[shapePoints.length() / 2];
			int j = 0;
			for (int i = 0; i < shapePoints.length() - 1; i += 2) {
				decisionPoints[j] = new GeoPoint(shapePoints.getDouble(i),
						(Double) shapePoints.get(i + 1));
				j++;
			}

			// --- Get the distances and shape point indexes ---
			JSONArray guidanceLinkCollection = guidance
					.getJSONArray("GuidanceLinkCollection");
			distances = new double[guidanceLinkCollection.length()];
			shapePointIndexes = new int[guidanceLinkCollection.length()];
			for (int i = 0; i < guidanceLinkCollection.length(); i++) {
				distances[i] = guidanceLinkCollection.getJSONObject(i)
						.getDouble("length");
				shapePointIndexes[i] = guidanceLinkCollection.getJSONObject(i)
						.getInt("shapeIndex");
			}

			// Create the route segments
			createRouteSegments(maneuvers, linkIndexes, decisionPoints,
					distances, shapePointIndexes);

			// Set current route segment to first segment
			this.currentSegment = 0;

			// Import has been successful
			this.importSuccessful = true;
		} catch (JSONException e) {
			// Import has not been successful
			Log.e("InstructionManager",
					"Could not extract the guidance JSONObject. This is the error message: "
							+ e.getMessage());
			this.importSuccessful = false;
		}
	}

	/**
	 * Create the route segments out of the complete route information
	 * 
	 * @param maneuvers
	 *            The maneuver types
	 * @param linkIndexes
	 *            All indexes of required entries in the
	 *            <code>GuidanceLinkCollection</code>
	 * @param decisionPoints
	 *            All shape points returned by the MapQuest API
	 * @param distances
	 *            The distances of all street segments
	 * @param shapePointIndexes
	 *            All indexes of "real" decision points" stored in
	 *            <code>decisionPoints</code>
	 */
	private void createRouteSegments(int[] maneuvers, int[] linkIndexes,
			GeoPoint[] decisionPoints, double[] distances,
			int[] shapePointIndexes) {
		this.segments = new ArrayList<RouteSegment>();

		// Create the first route segment (starting position = null)
		GeoPoint firstDecisionPoint = decisionPoints[shapePointIndexes[linkIndexes[0]]];
		double firstDistance = 0;
		for (int i = 0; i < linkIndexes[0]; i++) {
			firstDistance += distances[i];
		}
		RouteSegment firstSegment = new RouteSegment(null, firstDecisionPoint,
				maneuvers[0], (int) firstDistance);
		this.segments.add(firstSegment);

		// Create the rest of the route segments analog to the first segment
		for (int i = 1; i < maneuvers.length; i++) {
			GeoPoint lastDecisionPoint = decisionPoints[shapePointIndexes[linkIndexes[i - 1]]];
			GeoPoint nextDecisionPoint = decisionPoints[shapePointIndexes[linkIndexes[i]]];

			double nextDistance = 0;
			for (int j = (i == 0) ? 0 : linkIndexes[i - 1]; j < linkIndexes[i]; j++) {
				nextDistance += distances[j];
			}
			// Round the distance depending on its value and convert it from
			// kilometers into meters
			if (nextDistance >= 1) {
				nextDistance = Math.round(nextDistance * 10) * 100;
			} else {
				nextDistance = Math.round(nextDistance * 100) * 10;
			}

			// Create the route segment
			RouteSegment nextSegment = new RouteSegment(lastDecisionPoint,
					nextDecisionPoint, maneuvers[i], (int) nextDistance);
			this.segments.add(nextSegment);
		}
	}

	/**
	 * @return Check if the JSON import has been successful
	 */
	public boolean isImportSuccessful() {
		return this.importSuccessful;
	}

	/**
	 * Get the next route segment
	 * 
	 * @return The next route segment.<br/>
	 *         Null when the end of route segments has been reached.
	 */
	public RouteSegment getNextSegment() {
		if (currentSegment < this.segments.size()) {
			RouteSegment nextSegment = this.segments.get(this.currentSegment);
			this.currentSegment++;
			return nextSegment;
		} else {
			return null;
		}
	}

	/**
	 * Get the number of route segments
	 * 
	 * @return The number of route segments
	 */
	public int getNumberOfSegments() {
		return this.segments.size();
	}
}
