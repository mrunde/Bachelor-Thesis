package de.mrunde.bachelorthesis.instructions;

import java.util.ArrayList;
import java.util.List;

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
	private List<Instruction> instructions;

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
	 * Get the current instruction, pushed to the system
	 * 
	 * @return The current instruction, pushed to the system
	 */
	public Instruction getCurrentInstruction() {
		return this.instructions.get(this.currentInstruction);
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
				// TODO Log all instructions for testing
				Log.d("InstructionManager", "Instruction " + j + ": "
						+ this.instructions.get(j).toString()
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
