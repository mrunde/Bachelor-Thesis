package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Landmark;

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
	 */
	public LandmarkInstruction(GeoPoint decisionPoint, int maneuverType) {
		super(decisionPoint, maneuverType);
	}
}
