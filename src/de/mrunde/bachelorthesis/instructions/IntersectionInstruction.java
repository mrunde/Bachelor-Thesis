package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

public class IntersectionInstruction extends Instruction {

	/**
	 * Constructor of the IntersectionInstruction class
	 * 
	 * @param maneuverType
	 *            The maneuver type
	 */
	public IntersectionInstruction(GeoPoint decisionPoint, int maneuverType) {
		super(decisionPoint, maneuverType);
	}
}
