package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Maneuver;

/**
 * This is a distance-based instruction.
 * 
 * @author Marius Runde
 */
public class DistanceInstruction extends Instruction {

	/**
	 * Distance to the decision point
	 */
	private int distance;

	/**
	 * Constructor of the DistanceInstruction class
	 * 
	 * @param maneuver
	 *            The maneuver type
	 * @param distance
	 *            Distance to the decision point
	 */
	public DistanceInstruction(GeoPoint decisionPoint, int maneuverType,
			int distance) {
		super(decisionPoint, maneuverType);
		this.distance = distance;
	}

	/**
	 * @return The Instruction as a verbal text
	 */
	public String toString() {
		if (Maneuver.isTurnAction(super.getManeuverType())) {
			String instruction = super.getManeuver() + " in " + distance
					+ " meters";
			return instruction;
		} else {
			return null;
		}
	}
}
