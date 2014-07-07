package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Maneuver;

/**
 * This is a now instruction.
 * 
 * @author Marius Runde
 */
public class NowInstruction extends Instruction {

	/**
	 * Constructor of the LandmarkInstruction class
	 * 
	 * @param decisionPoint
	 *            The decision point
	 * @param maneuverType
	 *            The maneuver type
	 */
	public NowInstruction(GeoPoint decisionPoint, int maneuverType) {
		super(decisionPoint, maneuverType);
	}

	/**
	 * @return The instruction as a verbal text
	 */
	public String toString() {
		if (Maneuver.isTurnAction(super.getManeuverType())) {
			String instruction = super.getManeuver() + " now";
			return instruction;
		} else {
			return null;
		}
	}
}
