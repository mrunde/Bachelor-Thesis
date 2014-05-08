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
	private int[] distances;

	/**
	 * Instructions created by the InstructionManager
	 */
	private Instruction[] instructions;

	/**
	 * Store the last instruction, pushed to the system. Default = 0
	 */
	private int lastInstruction;

	/**
	 * Constructor of the InstructionManager class
	 * 
	 * @param json
	 *            The guidance information in a JSON format
	 */
	public InstructionManager(JSONObject json) {
		// Extract the guidance information out of the raw JSON file
		try {
			JSONObject guidance = json.getJSONObject("guidance");

			// Get the maneuver types
			this.maneuvers = new ArrayList<Integer>();
			JSONArray guidanceNodeCollection = guidance
					.getJSONArray("GuidanceNodeCollection");
			for (int i = 0; i < guidanceNodeCollection.length(); i++) {
				if ((guidanceNodeCollection.getJSONObject(i))
						.has("maneuverType")) {
					this.maneuvers.add(guidanceNodeCollection.getJSONObject(i)
							.getInt("maneuverType"));
				}
			}

			// Get the decision points TODO shape points contain more points
			// than needed
			this.decisionPoints = new GeoPoint[this.maneuvers.size()];
			// TODO just for testing
			for (int i = 0; i < this.decisionPoints.length; i++) {
				this.decisionPoints[i] = new GeoPoint(52, 7);
			}
			// JSONArray shapePoints = guidance.getJSONArray("shapePoints");
			// for (int i = 0; i < shapePoints.length() - 1; i += 2) {
			// this.decisionPoints[i] = new GeoPoint(shapePoints.getDouble(i),
			// (Double) shapePoints.get(i + 1)); // TODO now here an error
			// occurs :(
			// }

			// Get the distances
			this.distances = new int[this.maneuvers.size()];
			// TODO just for testing
			for (int i = 0; i < this.distances.length; i++) {
				this.distances[i] = (int) Math.round(Math.random() * 100);
			}
			// JSONArray guidanceLinkCollection = guidance
			// .getJSONArray("GuidanceLinkCollection");
			// int distanceLength = this.distances.length; TODO test
			// int guidanceLinkCollectionLength =
			// guidanceLinkCollection.length(); TODO test
			// for (int i = 0; i < guidanceLinkCollection.length(); i++) {
			// this.distances[i] = (Integer)
			// guidanceLinkCollection.getJSONObject(i)
			// .get("length");
			// }

			// Set the last instruction index
			this.lastInstruction = 0;

			this.importSuccessful = true;
		} catch (JSONException e) {
			Log.e("InstructionManager",
					"Could not extract the guidance JSONObject. This is the error message: "
							+ e.getMessage());
			this.importSuccessful = false;
		}
	}

	/**
	 * @return Check if the JSON import has been successful
	 */
	public boolean isImportSuccessful() {
		return this.importSuccessful;
	}

	/**
	 * Get the instruction at the desired index
	 * 
	 * @param index
	 *            Index of the instruction
	 * @return The instruction
	 */
	public Instruction getInstruction(int index) {
		if (this.instructions[index] != null) {
			this.lastInstruction = index;
			return this.instructions[index];
		} else {
			Log.e("InstructionManager", "Could not get instruction at index "
					+ index);
			return null;
		}
	}

	/**
	 * Get the last instruction, pushed to the system
	 * 
	 * @return The last instruction, pushed to the system
	 */
	public Instruction getLastInstruction() {
		return this.instructions[this.lastInstruction];
	}

	/**
	 * Create the instructions
	 */
	public void createInstructions() {
		this.instructions = new Instruction[this.maneuvers.size()];
		for (int i = 0; i < this.instructions.length; i++) {
			this.instructions[i] = createInstruction(this.decisionPoints[i],
					this.maneuvers.get(i), this.distances[i]);
		}
	}

	/**
	 * This is the super-method to create instructions of any type. The
	 * InstructionManager automatically finds out which type of instruction has
	 * to be created and returns it as a verbal text.
	 * 
	 * @param decisionPoint
	 *            Decision point where the maneuver has to be done
	 * @param maneuverType
	 *            Maneuver type
	 * @param distance
	 *            Distance to decision point
	 * @return The instruction
	 */
	private Instruction createInstruction(GeoPoint decisionPoint,
			Integer maneuverType, Integer distance) {
		Instruction instruction = new DistanceInstruction(decisionPoint,
				maneuverType, distance);

		return instruction;
	}
}
