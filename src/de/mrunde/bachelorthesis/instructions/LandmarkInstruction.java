package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Landmark;
import de.mrunde.bachelorthesis.basics.Maneuver;

/**
 * This is a landmark-based instruction.
 * 
 * @author Marius Runde
 */
public class LandmarkInstruction extends Instruction {

	/**
	 * The local landmark at the position
	 */
	private Landmark local;

	/**
	 * Constructor of the LandmarkInstruction class
	 * 
	 * @param decisionPoint
	 *            The decision point
	 * @param maneuverType
	 *            The maneuver type
	 * @param local
	 *            The local landmark at the decision point
	 */
	public LandmarkInstruction(GeoPoint decisionPoint, int maneuverType,
			Landmark local) {
		super(decisionPoint, maneuverType);
		this.local = local;
	}

	/**
	 * @return The instruction as a verbal text
	 */
	public String toString() {
		if (Maneuver.isTurnAction(super.getManeuverType())) {
			String instruction = super.getManeuver() + " at the "
					+ this.local.getCategory();
			return instruction;
		} else {
			return null;
		}
	}
}
