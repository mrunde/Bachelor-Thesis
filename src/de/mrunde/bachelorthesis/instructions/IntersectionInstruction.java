package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Maneuver;

/**
 * This is the intersection-based instruction.
 * 
 * @author Marius Runde
 */
public class IntersectionInstruction extends Instruction {

	/**
	 * Number of intersections
	 */
	private int intersections;

	/**
	 * Constructor of the IntersectionInstruction class
	 * 
	 * @param decisionPoint
	 *            The decision point
	 * @param maneuverType
	 *            The maneuver type
	 * @param intersections
	 *            The number of intersections
	 */
	public IntersectionInstruction(GeoPoint decisionPoint, int maneuverType,
			int intersections) {
		super(decisionPoint, maneuverType);
		this.intersections = intersections;
	}

	/**
	 * @return The instruction as a verbal text
	 */
	public String toString() {
		if (Maneuver.isTurnAction(super.getManeuverType())) {
			String instruction;
			switch (this.intersections) {
			case 1:
				instruction = super.getManeuver() + " at the next intersection";
				break;
			case 2:
				instruction = super.getManeuver() + " at the 2nd intersection";
				break;
			case 3:
				instruction = super.getManeuver() + " at the 3rd intersection";
				break;
			default:
				instruction = null;
				break;
			}
			return instruction;
		} else {
			return null;
		}
	}
}
