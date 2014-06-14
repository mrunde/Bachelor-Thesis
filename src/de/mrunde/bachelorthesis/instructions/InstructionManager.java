package de.mrunde.bachelorthesis.instructions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Landmark;
import de.mrunde.bachelorthesis.basics.Maneuver;
import de.mrunde.bachelorthesis.basics.Route;
import de.mrunde.bachelorthesis.basics.RouteSegment;
import de.mrunde.bachelorthesis.basics.StreetFurniture;
import de.mrunde.bachelorthesis.basics.StreetFurnitureCategory;

/**
 * The InstructionManager handles turn events in the navigation process. It can
 * create instructions depending on the available landmarks, street furniture or
 * intersections.
 * 
 * @author Marius Runde
 */
public class InstructionManager {

	/**
	 * This is the maximal distance between the decision point and a street
	 * furniture
	 */
	private final int MAX_DISTANCE_TO_STREET_FURNITURE = 20;

	/**
	 * This is the maximal number of intersections to be used for an instruction
	 */
	private final int MAX_NUMBER_OF_STREET_FURNITURE = 2;

	/**
	 * This is the maximal distance between the decision point and an
	 * intersection
	 */
	private final int MAX_DISTANCE_TO_INTERSECTION = 10;

	/**
	 * This is the maximal number of intersections to be used for an instruction
	 */
	private final int MAX_NUMBER_OF_INTERSECTIONS = 3;

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
	 * Stores a <code>boolean</code> whether the last instruction contained
	 * information of a roundabout. Then the following instruction must be
	 * ignored because it has the same information.
	 */
	private boolean lastInstructionWasForRoundabout;

	/**
	 * Local landmarks to be used
	 */
	private List<Landmark> localLandmarks;

	/**
	 * Global landmarks to be used
	 */
	private List<Landmark> globalLandmarks;

	/**
	 * Street furniture to be used
	 */
	private List<StreetFurniture> streetFurniture;

	/**
	 * Intersections to be used
	 */
	private List<GeoPoint> intersections;

	/**
	 * Constructor of the InstructionManager class
	 * 
	 * @param guidance
	 *            The guidance information in a JSON format
	 * @param landmarks
	 *            The landmarks from res/raw/landmarks.json
	 */
	public InstructionManager(JSONObject guidance, JSONObject landmarks,
			JSONArray streetFurniture, JSONArray intersections) {
		// Initialize the route
		this.route = new Route(guidance);

		// Check if the JSON import has been successful
		this.importSuccessful = this.route.isImportSuccessful();

		// Initialize the control variable for the roundabout instructions
		this.lastInstructionWasForRoundabout = false;

		// Initialize the landmarks
		initLandmarks(landmarks);

		// Initialize the street furniture
		initStreetFurniture(streetFurniture);

		// Initialize the intersections
		initIntersections(intersections);
	}

	/**
	 * Initialize the landmarks
	 * 
	 * @param landmarks
	 *            The landmarks from res/raw/landmarks.json
	 */
	private void initLandmarks(JSONObject landmarks) {
		this.localLandmarks = new ArrayList<Landmark>();
		this.globalLandmarks = new ArrayList<Landmark>();
		try {
			// Initialize all local landmarks
			JSONArray local = landmarks.getJSONArray("local");
			for (int i = 0; i < local.length(); i++) {
				String title = ((JSONObject) local.get(i)).getString("title");
				GeoPoint center = new GeoPoint(((JSONObject) local.get(i))
						.getJSONObject("center").getDouble("lat"),
						((JSONObject) local.get(i)).getJSONObject("center")
								.getDouble("lng"));
				int radius = ((JSONObject) local.get(i)).getInt("radius");
				String category = ((JSONObject) local.get(i))
						.getString("category");
				this.localLandmarks.add(new Landmark(true, title, center,
						radius, category));
			}

			// Initialize all global landmarks
			JSONArray global = landmarks.getJSONArray("global");
			for (int i = 0; i < global.length(); i++) {
				String title = ((JSONObject) global.get(i)).getString("title");
				GeoPoint center = new GeoPoint(((JSONObject) global.get(i))
						.getJSONObject("center").getDouble("lat"),
						((JSONObject) global.get(i)).getJSONObject("center")
								.getDouble("lng"));
				int radius = ((JSONObject) local.get(i)).getInt("radius");
				String category = ((JSONObject) global.get(i))
						.getString("category");
				this.globalLandmarks.add(new Landmark(false, title, center,
						radius, category));
			}
		} catch (JSONException e) {
			// Error while parsing JSONObject
			Log.e("InstructionManager",
					"Error while parsing JSONObject to initialize the landmarks.");
			this.importSuccessful = false;
		}

		// Log the landmarks
		for (int i = 0; i < this.localLandmarks.size(); i++) {
			Log.v("InstructionManager.initLandmarks", "Local Landmark " + i
					+ ": " + this.localLandmarks.get(i).toString());
		}
		for (int i = 0; i < this.globalLandmarks.size(); i++) {
			Log.v("InstructionManager.initLandmarks", "Global Landmark " + i
					+ ": " + this.globalLandmarks.get(i).toString());
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
								"center").getDouble("lat"),
						((JSONObject) streetFurniture.get(i)).getJSONObject(
								"center").getDouble("lng"));
				String category = ((JSONObject) streetFurniture.get(i))
						.getString("category");
				this.streetFurniture.add(new StreetFurniture(center, category));
			}
		} catch (JSONException e) {
			// Error while parsing JSONArray
			Log.e("InstructionManager",
					"Error while parsing JSONArray to initialize the street furniture.");
			this.importSuccessful = false;
		}

		// Log the street furniture
		for (int i = 0; i < this.streetFurniture.size(); i++) {
			Log.v("InstructionManager.initStreetFurniture", "Street furniture "
					+ i + ": " + this.streetFurniture.get(i).toString());
		}
	}

	/**
	 * Initialize the intersections
	 * 
	 * @param intersections
	 *            The intersections from res/raw/intersections.json
	 */
	private void initIntersections(JSONArray intersections) {
		this.intersections = new ArrayList<GeoPoint>();
		try {
			for (int i = 0; i < intersections.length(); i++) {
				this.intersections.add(new GeoPoint(((JSONObject) intersections
						.get(i)).getDouble("lat"), ((JSONObject) intersections
						.get(i)).getDouble("lng")));
			}
		} catch (JSONException e) {
			// Error while parsing JSONArray
			Log.e("InstructionManager",
					"Error while parsing JSONArray to initialize the intersections.");
			this.importSuccessful = false;
		}

		// Log the intersections
		for (int i = 0; i < this.intersections.size(); i++) {
			Log.v("InstructionManager.initIntersections", "Intersection " + i
					+ ": " + this.intersections.get(i).toString());
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
	 * Get all verbal instructions
	 * 
	 * @return All verbal instructions
	 */
	public String[] getVerbalInstructions() {
		String[] result = new String[this.instructions.size()];

		for (int i = 0; i < result.length; i++) {
			result[i] = this.instructions.get(i).toString();
		}

		return result;
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
			Instruction[] instruction = createInstruction(rs.getEndPoint(),
					rs.getStartPoint(), rs.getManeuverType(), rs.getDistance());

			if (instruction != null) {
				// Add the global instruction
				if (instruction[0] != null) {
					this.instructions.add(instruction[0]);
					// Log all global instructions
					Log.v("InstructionManager.createInstructions",
							"(Global) Instruction "
									+ j
									+ ": "
									+ this.instructions.get(j).toString()
									+ " | "
									+ this.instructions.get(j)
											.getDecisionPoint().toString());
					j++;
				}
				// Remove "no-turn" instructions by ignoring them
				if (instruction[1].toString() != null) {
					this.instructions.add(instruction[1]);
					// Log all local instructions
					Log.v("InstructionManager.createInstructions",
							"(Local) Instruction "
									+ j
									+ ": "
									+ this.instructions.get(j).toString()
									+ " | Maneuver Type: "
									+ this.instructions.get(j)
											.getManeuverType()
									+ " | "
									+ this.instructions.get(j)
											.getDecisionPoint().toString()
									+ " | Instruction Type: "
									+ this.instructions.get(j).getClass());
					j++;
				}
			}
		}
	}

	/**
	 * This is the super-method to create instructions of any type. The
	 * InstructionManager automatically finds out which type of instruction has
	 * to be created and returns it.<br/>
	 * <br/>
	 * The different types of instructions are created by this priority order:
	 * <ul>
	 * <li>LandmarkInstruction</li>
	 * <li>StreetFurnitureInstruction</li>
	 * <li>IntersectionInstruction</li>
	 * <li>DistanceInstruction</li>
	 * </ul>
	 * Global landmarks are used in the instructions if available.
	 * 
	 * @param decisionPoint
	 *            Decision point where the maneuver has to be done
	 * @param previousDecisionPoint
	 *            Previous decision point. <code>Null</code> if first
	 *            instruction. Used to find global landmarks
	 * @param maneuverType
	 *            The maneuver type
	 * @param distance
	 *            Distance to decision point (only used for
	 *            <code>DistanceInstruction</code> objects)
	 * @return The global instruction along the route (first element in array,
	 *         if available) and the local instruction at the decision point
	 *         (second element in array). If the previous local instruction
	 *         contained information about a roundabout action,
	 *         <code>null</code> will be returned.
	 */
	private Instruction[] createInstruction(GeoPoint decisionPoint,
			GeoPoint previousDecisionPoint, Integer maneuverType,
			Integer distance) {
		if (this.lastInstructionWasForRoundabout) {
			this.lastInstructionWasForRoundabout = false;
			return null;
		} else {
			// Check if the next instruction will be for a roundabout
			if (Maneuver.isRoundaboutAction(maneuverType)) {
				this.lastInstructionWasForRoundabout = true;
			}

			Instruction[] instruction = new Instruction[2];

			// All maneuver types with ID greater or equal 23 already contain
			// enough
			// information in the maneuver text (e.g. a roundabout or the
			// destination) or use the short-distance public transport so that a
			// distance-based instruction reaches out there
			if (maneuverType >= 23) {
				instruction[1] = new DistanceInstruction(decisionPoint,
						maneuverType, distance);
			} else {

				// Search for global landmark along the route and create the
				// corresponding instruction
				instruction[0] = searchForGlobalLandmarkAlongRoute(
						decisionPoint, previousDecisionPoint);
				// Create a LandmarkAlongRouteInstruction if no global landmark
				// could be
				// found before
				if (instruction[0] == null) {
					instruction[0] = searchForLocalLandmarkAlongRoute(
							decisionPoint, previousDecisionPoint);
				}

				Landmark localLandmark;
				String[] streetFurniture;
				int intersections;

				// Search for local landmark or street furniture to create
				// instruction
				if ((localLandmark = searchForLocalLandmark(decisionPoint)) != null) {
					// Create a LandmarkInstruction
					instruction[1] = new LandmarkInstruction(decisionPoint,
							maneuverType, localLandmark);
				} else if ((streetFurniture = searchForStreetFurniture(
						decisionPoint, previousDecisionPoint)) != null) {
					// Create a StreetFurnitureInstruction from one street
					// furniture
					instruction[1] = new StreetFurnitureInstruction(
							decisionPoint, maneuverType,
							Integer.valueOf(streetFurniture[0]),
							streetFurniture[1]);
				}

				// Check if the instruction is null in case the
				// StreetFurnitureInstruction could not be created due to an
				// intersection crossing the last route segment
				if (instruction[1] == null
						&& (intersections = searchForIntersections(
								decisionPoint, previousDecisionPoint)) > 0) {
					// Create an IntersectionInstruction
					instruction[1] = new IntersectionInstruction(decisionPoint,
							maneuverType, intersections);
				} else if (instruction[1] == null) {
					// Create a DistanceInstruction if all other options failed
					instruction[1] = new DistanceInstruction(decisionPoint,
							maneuverType, distance);
				}
			}
			return instruction;
		}
	}

	/**
	 * Search for a global landmark along the route between the two given
	 * locations. The index of the current decision point in the shape points
	 * will be decreased by 2 so that there is still room for an instruction at
	 * that decision point. If this was successful, a
	 * <code>GlobalInstruction</code> object will be created.
	 * 
	 * @param decisionPoint
	 *            Decision point
	 * @param previousDecisionPoint
	 *            Previous decision point
	 * @return <code>GlobalInstruction</code> object if available. Otherwise
	 *         <code>null</code> will be returned.
	 */
	private GlobalInstruction searchForGlobalLandmarkAlongRoute(
			GeoPoint decisionPoint, GeoPoint previousDecisionPoint) {
		GlobalInstruction result = null;

		// Get the shape points from the route
		GeoPoint[] shapePoints = this.route.getShapePoints();

		// Find the indexes of the current and the previous decision points
		int indexCurrent = searchDecisionPointIndex(decisionPoint, shapePoints);
		indexCurrent = indexCurrent - 2;
		int indexPrevious = searchDecisionPointIndex(previousDecisionPoint,
				shapePoints);

		for (int i = 0; i < this.globalLandmarks.size(); i++) {
			// Get the landmark location
			org.osmdroid.util.GeoPoint currentLandmark = new org.osmdroid.util.GeoPoint(
					this.globalLandmarks.get(i).getCenter().getLatitude(),
					this.globalLandmarks.get(i).getCenter().getLongitude());

			// Iterate through all shape points that lay between the current and
			// the previous decision points
			for (int j = indexCurrent; j > indexPrevious + 1; j--) {
				org.osmdroid.util.GeoPoint currentShapePoint = new org.osmdroid.util.GeoPoint(
						shapePoints[j].getLatitude(),
						shapePoints[j].getLongitude());

				double distance = currentShapePoint.distanceTo(currentLandmark);
				if (distance <= this.globalLandmarks.get(i).getRadius()) {
					result = new GlobalInstruction(shapePoints[j],
							this.globalLandmarks.get(i));
					return result;
				}
			}
		}
		return result;
	}

	/**
	 * Search for a local landmark along the route between the two given
	 * locations. The index of the current decision point in the shape points
	 * will be decreased by 2 so that there is still room for an instruction at
	 * that decision point. If this was successful, a
	 * <code>GlobalInstruction</code> object will be created.
	 * 
	 * @param decisionPoint
	 *            Decision point
	 * @param previousDecisionPoint
	 *            Previous decision point
	 * @return <code>GlobalInstruction</code> object if available. Otherwise
	 *         <code>null</code> will be returned.
	 */
	private GlobalInstruction searchForLocalLandmarkAlongRoute(
			GeoPoint decisionPoint, GeoPoint previousDecisionPoint) {
		GlobalInstruction result = null;

		// Get the shape points from the route
		GeoPoint[] shapePoints = this.route.getShapePoints();

		// Find the indexes of the current and the previous decision points
		int indexCurrent = searchDecisionPointIndex(decisionPoint, shapePoints);
		indexCurrent = indexCurrent - 2;
		int indexPrevious = searchDecisionPointIndex(previousDecisionPoint,
				shapePoints);

		for (int i = 0; i < this.localLandmarks.size(); i++) {
			// Get the landmark location
			org.osmdroid.util.GeoPoint currentLandmark = new org.osmdroid.util.GeoPoint(
					this.localLandmarks.get(i).getCenter().getLatitude(),
					this.localLandmarks.get(i).getCenter().getLongitude());

			// Iterate through all shape points that lay between the current and
			// the previous decision points
			for (int j = indexCurrent; j > indexPrevious + 1; j--) {
				org.osmdroid.util.GeoPoint currentShapePoint = new org.osmdroid.util.GeoPoint(
						shapePoints[j].getLatitude(),
						shapePoints[j].getLongitude());

				double distance = currentShapePoint.distanceTo(currentLandmark);
				if (distance <= this.localLandmarks.get(i).getRadius()) {
					result = new GlobalInstruction(shapePoints[j],
							this.localLandmarks.get(i));
					return result;
				}
			}
		}
		return result;
	}

	/**
	 * Search for a local landmark close to the given location
	 * 
	 * @param decisionPoint
	 *            Decision point
	 * @return <code>Landmark</code> object if available. Otherwise
	 *         <code>null</code> will be returned.
	 */
	private Landmark searchForLocalLandmark(GeoPoint decisionPoint) {
		Landmark result = null;

		org.osmdroid.util.GeoPoint decisionPointFromOsmdroid = new org.osmdroid.util.GeoPoint(
				decisionPoint.getLatitude(), decisionPoint.getLongitude());

		for (int i = 0; i < this.localLandmarks.size(); i++) {
			org.osmdroid.util.GeoPoint currentLandmark = new org.osmdroid.util.GeoPoint(
					this.localLandmarks.get(i).getCenter().getLatitude(),
					this.localLandmarks.get(i).getCenter().getLongitude());
			double distance = decisionPointFromOsmdroid
					.distanceTo(currentLandmark);
			if (distance <= this.localLandmarks.get(i).getRadius()) {
				result = this.localLandmarks.get(i);
				return result;
			}
		}

		return result;
	}

	/**
	 * Search for a street furniture on this route segment
	 * 
	 * @param decisionPoint
	 *            Decision point
	 * @param previousDecisionPoint
	 *            Previous decision point
	 * @return Number of street furniture and index of the street furniture
	 *         category
	 */
	private String[] searchForStreetFurniture(GeoPoint decisionPoint,
			GeoPoint previousDecisionPoint) {
		// Street furniture categories
		String[] categories = StreetFurnitureCategory.getCategories();
		// Number of the found street furniture for each category
		int[] numberOfStreetFurniture = new int[categories.length];
		for (int temp = 0; temp < numberOfStreetFurniture.length; temp++) {
			numberOfStreetFurniture[temp] = 0;
		}
		// Index of the shape point of the last street furniture for each
		// category
		int[] indexLastStreetFurniture = new int[categories.length];

		// Get the shape points from the route
		GeoPoint[] shapePoints = this.route.getShapePoints();

		// Find the indexes of the current and the previous decision points
		int indexCurrent = searchDecisionPointIndex(decisionPoint, shapePoints);
		int indexPrevious = searchDecisionPointIndex(previousDecisionPoint,
				shapePoints);

		for (int j = 0; j < this.streetFurniture.size(); j++) {
			// Get the street furniture category and store its index
			int indexCategory = 0;
			StreetFurniture currentStreetFurniture = this.streetFurniture
					.get(j);
			while (!categories[indexCategory].replace("_", " ").equals(
					currentStreetFurniture.getCategory())) {
				indexCategory++;
			}
			// Get the street furniture location
			org.osmdroid.util.GeoPoint streetFurnitureGeoPoint = new org.osmdroid.util.GeoPoint(
					currentStreetFurniture.getCenter().getLatitude(),
					currentStreetFurniture.getCenter().getLongitude());

			// Iterate through all shape points that lay between the current and
			// the previous decision points
			for (int k = indexCurrent; k > indexPrevious + 1; k--) {
				org.osmdroid.util.GeoPoint currentShapePoint = new org.osmdroid.util.GeoPoint(
						shapePoints[k].getLatitude(),
						shapePoints[k].getLongitude());

				double distance = currentShapePoint
						.distanceTo(streetFurnitureGeoPoint);
				if (distance <= this.MAX_DISTANCE_TO_STREET_FURNITURE) {
					if (numberOfStreetFurniture[indexCategory] == 0) {
						// Store the index of the shape point
						indexLastStreetFurniture[indexCategory] = k;
					}
					numberOfStreetFurniture[indexCategory]++;
					break;
				}
			}
		}

		// Store the results (number of street furniture is converted to String
		// and must be reconverted when creating the StreetFurnitureInstruction)
		String[] result = null;

		// Find a street furniture category that can be used for the instruction
		for (int j = 0; j < categories.length; j++) {
			// Check if the number of street furniture of this category is
			// higher than the maximal allowed number
			if (0 < numberOfStreetFurniture[j]
					&& numberOfStreetFurniture[j] <= this.MAX_NUMBER_OF_STREET_FURNITURE) {
				if (result == null) {
					result = new String[2];
				}
				// Store the number of street furniture
				result[0] = String.valueOf(numberOfStreetFurniture[j]);
				// Store the category
				result[1] = categories[j].replace("_", " ");

				// Check if any street furniture has been found or any
				// intersections lay between the last street furniture and
				// current decision point
				if (searchForIntersections(decisionPoint,
						shapePoints[indexLastStreetFurniture[j]]) > 0) {
					result = null;
					break;
				}
			}
		}

		return result;
	}

	/**
	 * Search for intersections on this route segment
	 * 
	 * @param decisionPoint
	 *            Decision point
	 * @param previousDecisionPoint
	 *            Previous decision point
	 * @return Number of intersections
	 */
	private int searchForIntersections(GeoPoint decisionPoint,
			GeoPoint previousDecisionPoint) {
		int result = 0;

		// Get the shape points from the route
		GeoPoint[] shapePoints = this.route.getShapePoints();

		// Find the indexes of the current and the previous decision points
		int indexCurrent = searchDecisionPointIndex(decisionPoint, shapePoints);
		int indexPrevious = searchDecisionPointIndex(previousDecisionPoint,
				shapePoints);

		for (int i = 0; i < this.intersections.size(); i++) {
			// Get the intersection location
			org.osmdroid.util.GeoPoint intersectionGeoPoint = new org.osmdroid.util.GeoPoint(
					this.intersections.get(i).getLatitude(), this.intersections
							.get(i).getLongitude());

			// Iterate through all shape points that lay between the current and
			// the previous decision points
			for (int j = indexCurrent; j > indexPrevious; j--) {
				org.osmdroid.util.GeoPoint currentShapePoint = new org.osmdroid.util.GeoPoint(
						shapePoints[j].getLatitude(),
						shapePoints[j].getLongitude());

				double distance = currentShapePoint
						.distanceTo(intersectionGeoPoint);
				if (distance <= this.MAX_DISTANCE_TO_INTERSECTION) {
					result++;
					break;
				}
			}

			// Check if the number of intersections is higher than the maximal
			// allowed number
			if (result > this.MAX_NUMBER_OF_INTERSECTIONS) {
				result = 0;
				break;
			}
		}

		return result;
	}

	/**
	 * Search the index of the given decision point
	 * 
	 * @param decisionPoint
	 *            The decision point
	 * @param shapePoints
	 *            The shape points that create the route
	 * @return Index of the decision point. -1 if decision point could not be
	 *         found or is <code>null</code>
	 */
	private int searchDecisionPointIndex(GeoPoint decisionPoint,
			GeoPoint[] shapePoints) {
		int index = -1;

		if (decisionPoint == null) {
			// If the decision point is null it seems to be the first
			// instruction being created
			index = 0;
		} else {
			// Iterate through all shape points until the correct one has been
			// found
			for (int i = 0; i < shapePoints.length; i++) {
				if (decisionPoint.equals(shapePoints[i])) {
					index = i;
					break;
				}
			}
		}
		return index;
	}
}
