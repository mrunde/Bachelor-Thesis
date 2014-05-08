package de.mrunde.bachelorthesis.instructions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mapquest.android.maps.GeoPoint;

/**
 * The InstructionManager handles turn events in the navigation process. It can
 * create instructions depending on the available landmarks, street furniture or
 * intersections.
 * 
 * @author Marius Runde
 */
public class InstructionManager {

	/**
	 * The guidance information
	 */
	private JSONObject guidance;

	/**
	 * Value to check if the JSON import succeeded
	 */
	private boolean importSuccessful;

	/**
	 * Maneuver types at the decision points
	 */
	private List<Integer> maneuvers;

	/**
	 * Decision points of the route
	 */
	private GeoPoint[] decisionPoints;

	/**
	 * Distance of each leg in the route
	 */
	private Integer[] distances;

	/**
	 * Instructions created by the InstructionManager
	 */
	private String[] instructions;

	/**
	 * Constructor of the InstructionManager class
	 * 
	 * @param json
	 *            The guidance information in a JSON format
	 */
	public InstructionManager(JSONObject json) {
		// Extract the guidance information out of the raw JSON file
		try {
			this.guidance = json.getJSONObject("guidance");

			// Get the maneuver types
			this.maneuvers = new ArrayList<Integer>();
			JSONArray guidanceNodeCollection = guidance
					.getJSONArray("GuidanceNodeCollection");
			for (int i = 0; i < guidanceNodeCollection.length(); i++) {
				if ((guidanceNodeCollection.getJSONObject(i))
						.has("maneuverType")) {
					Log.i("Test", i + " has maneuverType");
					this.maneuvers.add(guidanceNodeCollection.getJSONObject(i)
							.getInt("maneuverType"));
				} else {
					Log.i("Test", i + " has no maneuverType");
				}
			}

			// Get the decision points
			this.decisionPoints = new GeoPoint[this.maneuvers.size()];
			JSONArray shapePoints = guidance.getJSONArray("shapePoints");
			for (int i = 0; i < shapePoints.length() - 1; i += 2) {
				this.decisionPoints[i] = new GeoPoint(shapePoints.getDouble(i),
						(Double) shapePoints.get(i + 1)); // TODO now here an error occurs :(
			}

			// Get the distances
			this.distances = new Integer[this.maneuvers.size()];
			JSONArray guidanceLinkCollection = guidance
					.getJSONArray("guidanceLinkCollection");
			for (int i = 0; i < guidanceLinkCollection.length(); i++) {
				this.distances[i] = (Integer) guidanceLinkCollection.getJSONObject(i)
						.get("length");
			}

			this.importSuccessful = true;
		} catch (JSONException e) {
			Log.e("InstructionManager",
					"Could not extract the guidance JSONObject");
			this.importSuccessful = false;
		}
	}

	/**
	 * @return Check if the JSON import has been successful
	 */
	public boolean isImportSuccessful() {
		return this.importSuccessful;
	}

//	/**
//	 * Create the instructions
//	 */
//	public void createInstructions() {
//		this.instructions = new String[this.maneuvers.size()];
//		for (int i = 0; i < this.instructions.length; i++) {
//			this.instructions[i] = createInstruction(this.maneuvers.get(i),
//					this.distances[i]);
//		}
//	}
//
//	/**
//	 * This is the super-method to create instructions of any type. The
//	 * InstructionManager automatically finds out which type of instruction has
//	 * to be created and returns it as a verbal text.
//	 * 
//	 * @param maneuver
//	 *            maneuver
//	 * @return The instruction as a verbal text
//	 */
//	private String createInstruction(Integer maneuver, Integer distance) {
//		String instruction = createDistanceInstruction(maneuver, distance);
//
//		return instruction;
//	}
//
//	/**
//	 * Create a landmark-based instruction
//	 * 
//	 * @param maneuver
//	 *            The maneuver
//	 * @param local
//	 *            The local landmark at the decision point
//	 * @return The instruction as a verbal text
//	 */
//	private String createLandmarkInstruction(Integer maneuver, Landmark local) {
//		return null;
//	}
//
//	/**
//	 * Create a landmark-based instruction with a global landmark
//	 * 
//	 * @param maneuver
//	 *            The maneuver
//	 * @param local
//	 *            The local landmark at the decision point
//	 * @param global
//	 *            The global landmark off road
//	 * @return The instruction as a verbal text
//	 */
//	private String createLandmarkInstruction(Integer maneuver, Landmark local,
//			Landmark global) {
//		return null;
//	}
//
//	/**
//	 * Create a "street furniture"-based instruction
//	 * 
//	 * @param maneuver
//	 *            The maneuver
//	 * @param sf
//	 *            The street furniture at the decision point
//	 * @return The instruction as a verbal text
//	 */
//	private String createStreetFurnitureInstruction(Integer maneuver,
//			StreetFurniture sf) {
//		return null;
//	}
//
//	/**
//	 * Create a "street furniture"-based instruction with a global landmark
//	 * 
//	 * @param maneuver
//	 *            The maneuver
//	 * @param sf
//	 *            The street furniture at the decision point
//	 * @param global
//	 *            The global landmark off road
//	 * @return The instruction as a verbal text
//	 */
//	private String createStreetFurnitureInstruction(Integer maneuver,
//			StreetFurniture sf, Landmark global) {
//		return null;
//	}
//
//	/**
//	 * Create an intersection-based instruction
//	 * 
//	 * @param maneuver
//	 *            The maneuver
//	 * @param numberOfIntersections
//	 *            Number of intersections to pass
//	 * @param position
//	 *            Position where the maneuver takes place
//	 * @return The instruction as a verbal text
//	 */
//	private String createIntersectionInstruction(Integer maneuver,
//			int numberOfIntersections, GeoPoint position) {
//		String instruction = maneuver + " after " + numberOfIntersections
//				+ " intersections";
//
//		return instruction;
//	}
//
//	/**
//	 * Create an intersection-based instruction with a global landmark
//	 * 
//	 * @param maneuver
//	 *            The maneuver
//	 * @param numberOfIntersections
//	 *            Number of intersections to pass
//	 * @param position
//	 *            Position where the maneuver takes place
//	 * @param global
//	 *            The global landmark off road
//	 * @return The instruction as a verbal text
//	 */
//	private String createIntersectionInstruction(Integer maneuver,
//			int numberOfIntersections, GeoPoint position, Landmark global) {
//		return null;
//	}
//
//	/**
//	 * Create a distance-based instruction
//	 * 
//	 * @param maneuver
//	 *            The maneuver
//	 * @param distance
//	 *            Distance to maneuver
//	 * @return The instruction as a verbal text
//	 */
//	private String createDistanceInstruction(Integer maneuver, Integer distance) {
//		String instruction = new DistanceInstruction(maneuver, distance)
//				.toString();
//
//		return instruction;
//	}
//
//	/**
//	 * Create a distance-based instruction with a global landmark
//	 * 
//	 * @param maneuver
//	 *            The maneuver
//	 * @param distance
//	 *            Distance to maneuver
//	 * @param global
//	 *            The global landmark off road
//	 * @return The instruction as a verbal text
//	 */
//	private String createDistanceInstruction(Integer maneuver,
//			Integer distance, Landmark global) {
//		return null;
//	}
}
