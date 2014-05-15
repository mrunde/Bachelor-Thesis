package de.mrunde.bachelorthesis.instructions;

import org.json.JSONObject;

import android.util.Log;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Route;
import de.mrunde.bachelorthesis.basics.RouteSegment;

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
	private Instruction[] instructions;

	/**
	 * Store the current instruction, pushed to the system. Default = 0
	 */
	private int currentInstruction;

	/**
	 * Constructor of the InstructionManager class
	 * 
	 * @param json
	 *            The guidance information in a JSON format
	 */
	public InstructionManager(JSONObject json) {
		this.route = new Route(json);

		// Check if the JSON import has been successful
		this.importSuccessful = this.route.isImportSuccessful();
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
			this.currentInstruction = index;
			return this.instructions[index];
		} else {
			Log.e("InstructionManager", "Could not get instruction at index "
					+ index);
			return null;
		}
	}

	/**
	 * Get the current instruction, pushed to the system
	 * 
	 * @return The current instruction, pushed to the system
	 */
	public Instruction getCurrentInstruction() {
		return this.instructions[this.currentInstruction];
	}

	/**
	 * Create the instructions from the route information
	 */
	public void createInstructions() {
		this.instructions = new Instruction[this.route.getNumberOfSegments()];
		for (int i = 0; i < this.instructions.length; i++) {
			RouteSegment rs = this.route.getNextSegment();
			this.instructions[i] = createInstruction(rs.getEndPoint(),
					rs.getManeuverType(), rs.getDistance());
			// TODO Log all instructions for testing
			Log.d("InstructionManager", "Instruction " + i + ": "
					+ this.instructions[i].toString() + " | Maneuver Type: "
					+ this.instructions[i].getManeuverType());
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
