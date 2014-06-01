package de.mrunde.bachelorthesis.instructions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Landmark;
import de.mrunde.bachelorthesis.basics.Route;
import de.mrunde.bachelorthesis.basics.RouteSegment;
import de.mrunde.bachelorthesis.basics.StreetFurniture;

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
	 * Store the route information
	 */
	private Route route;

	/**
	 * Instructions created by the InstructionManager
	 */
	private List<Instruction> instructions;

	/**
	 * Store the current instruction. Default = 0
	 */
	private int currentInstruction;

	/**
	 * Landmarks to be used
	 */
	private List<Landmark> landmarks;

	/**
	 * Street furniture to be used
	 */
	private List<StreetFurniture> streetFurniture;

	/**
	 * Constructor of the InstructionManager class
	 * 
	 * @param guidance
	 *            The guidance information in a JSON format
	 * @param landmarks
	 *            The landmarks from res/raw/landmarks.json
	 */
	public InstructionManager(JSONObject guidance, JSONObject landmarks,
			JSONArray streetFurniture) {
		// Initialize the route
		this.route = new Route(guidance);

		// Check if the JSON import has been successful
		this.importSuccessful = this.route.isImportSuccessful();

		// Initialize the landmarks
		initLandmarks(landmarks);

		// Initialize the street furniture
		initStreetFurniture(streetFurniture);
	}

	/**
	 * Initialize the landmarks
	 * 
	 * @param landmarks
	 *            The landmarks from res/raw/landmarks.json
	 */
	private void initLandmarks(JSONObject landmarks) {
		this.landmarks = new ArrayList<Landmark>();
		try {
			// Initialize all local landmarks
			JSONArray local = landmarks.getJSONArray("local");
			for (int i = 0; i < local.length(); i++) {
				String title = ((JSONObject) local.get(i)).getString("title");
				GeoPoint center = new GeoPoint(((JSONObject) local.get(i))
						.getJSONObject("center").getDouble("lng"),
						((JSONObject) local.get(i)).getJSONObject("center")
								.getDouble("lng"));
				String category = ((JSONObject) local.get(i))
						.getString("category");
				this.landmarks.add(new Landmark(true, title, center, category));
			}

			// Initialize all global landmarks
			JSONArray global = landmarks.getJSONArray("global");
			for (int i = 0; i < global.length(); i++) {
				String title = ((JSONObject) global.get(i)).getString("title");
				GeoPoint center = new GeoPoint(((JSONObject) global.get(i))
						.getJSONObject("center").getDouble("lng"),
						((JSONObject) global.get(i)).getJSONObject("center")
								.getDouble("lng"));
				String category = ((JSONObject) global.get(i))
						.getString("category");
				this.landmarks
						.add(new Landmark(false, title, center, category));
			}
		} catch (JSONException e) {
			// Error while parsing JSONObject
			Log.e("InstructionManager",
					"Error while parsing JSONObject to initialize the landmarks.");
			this.importSuccessful = false;
		}

		// Log the landmarks
		for (int i = 0; i < this.landmarks.size(); i++) {
			Log.v("InstructionManager.initLandmarks", "Landmark " + i + ": "
					+ this.landmarks.get(i).toString());
		}
	}

	/**
	 * Initialize the street furniture
	 * 
	 * @param streetFurniture
	 *            The street furniture from res/raw/streetfurniture.json
	 */
	private void initStreetFurniture(JSONArray streetFurniture) {
		this.streetFurniture = new ArrayList<StreetFurniture>();
		try {
			for (int i = 0; i < streetFurniture.length(); i++) {
				GeoPoint center = new GeoPoint(
						((JSONObject) streetFurniture.get(i)).getJSONObject(
								"center").getDouble("lng"),
						((JSONObject) streetFurniture.get(i)).getJSONObject(
								"center").getDouble("lng"));
				String category = ((JSONObject) streetFurniture.get(i))
						.getString("category");
				this.streetFurniture.add(new StreetFurniture(center, category));
			}
		} catch (JSONException e) {
			// Error while parsing JSONObject
			Log.e("InstructionManager",
					"Error while parsing JSONArray to initialize the street furniture.");
			this.importSuccessful = false;
		}

		// Log the street furniture
		for (int i = 0; i < this.landmarks.size(); i++) {
			Log.v("InstructionManager.initLandmarks", "Street furniture " + i
					+ ": " + this.streetFurniture.get(i).toString());
		}
	}

	/**
	 * @return Check if the JSON import has been successful
	 */
	public boolean isImportSuccessful() {
		return this.importSuccessful;
	}

	/**
	 * Get all shape points from the route that create it
	 * 
	 * @return All shape points
	 */
	public GeoPoint[] getShapePoints() {
		return this.route.getShapePoints();
	}

	/**
	 * Get the instruction at the desired index
	 * 
	 * @param index
	 *            Index of the instruction
	 * @return The instruction
	 */
	public Instruction getInstruction(int index) {
		if (this.instructions.get(index) != null) {
			this.currentInstruction = index;
			return this.instructions.get(index);
		} else {
			Log.e("InstructionManager", "Could not get instruction at index "
					+ index);
			return null;
		}
	}

	/**
	 * Get the current instruction
	 * 
	 * @return The current instruction
	 */
	public Instruction getCurrentInstruction() {
		return this.instructions.get(this.currentInstruction);
	}

	/**
	 * Get the next instruction
	 * 
	 * @return The next instruction. <code>Null</code> if last instruction has
	 *         already been reached.
	 */
	public Instruction getNextInstruction() {
		if (this.instructions.size() > this.currentInstruction + 1) {
			// Increase the pointer
			this.currentInstruction++;
			// Return the next instruction
			return this.instructions.get(this.currentInstruction);
		} else {
			// Return null when last instruction has already been reached
			return null;
		}
	}

	/**
	 * Create the instructions from the route information
	 */
	public void createInstructions() {
		this.instructions = new ArrayList<Instruction>();
		int j = 0;
		for (int i = 0; i < this.route.getNumberOfSegments(); i++) {
			RouteSegment rs = this.route.getNextSegment();
			Instruction instruction = createInstruction(rs.getEndPoint(),
					rs.getManeuverType(), rs.getDistance());

			// Remove "no-turn" instructions
			if (instruction.toString() != null) {
				this.instructions.add(instruction);
				// Log all instructions
				Log.v("InstructionManager.createInstructions", "Instruction "
						+ j + ": " + this.instructions.get(j).toString()
						+ " | Maneuver Type: "
						+ this.instructions.get(j).getManeuverType());
				j++;
			}
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
	 *            Distance to decision point (only used for
	 *            <code>DistanceInstruction</code> objects)
	 * @return The instruction
	 */
	private Instruction createInstruction(GeoPoint decisionPoint,
			Integer maneuverType, Integer distance) {
		Instruction instruction = null;

		// Search for global landmark
		Landmark globalLandmark = searchForGlobalLandmark(decisionPoint);

		// Search for local landmark or street furniture
		Landmark localLandmark;
		StreetFurniture streetFurniture;
		if ((localLandmark = searchForLocalLandmark(decisionPoint)) != null) {
			// TODO Create LandmarkInstruction
		} else if ((streetFurniture = searchForStreetFurniture(decisionPoint)) != null) {
			// TODO Create StreetFurnitureInstruction
		} else {
			instruction = new DistanceInstruction(decisionPoint, maneuverType,
					distance);
		}

		return instruction;
	}

	/**
	 * Search for a global landmark close to the given location
	 * 
	 * @param location
	 *            Decision point
	 * @return <code>Landmark</code> object if available. Otherwise
	 *         <code>null</code> will be returned.
	 */
	private Landmark searchForGlobalLandmark(GeoPoint location) {
		// TODO
		return null;
	}

	/**
	 * Search for a local landmark close to the given location
	 * 
	 * @param location
	 *            Decision point
	 * @return <code>Landmark</code> object if available. Otherwise
	 *         <code>null</code> will be returned.
	 */
	private Landmark searchForLocalLandmark(GeoPoint location) {
		// TODO
		return null;
	}

	/**
	 * Search for a street furniture close to the given location
	 * 
	 * @param location
	 *            Decision point
	 * @return <code>StreetFurniture</code> object if available. Otherwise
	 *         <code>null</code> will be returned.
	 */
	private StreetFurniture searchForStreetFurniture(GeoPoint location) {
		// TODO
		return null;
	}
}
