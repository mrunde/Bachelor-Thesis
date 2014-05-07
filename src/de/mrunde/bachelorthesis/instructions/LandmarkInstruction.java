package de.mrunde.bachelorthesis.instructions;

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
	 * 
	 * @param text
	 *            Instruction as a verbal text
	 */
	public LandmarkInstruction(Integer maneuver) {
		super(maneuver);
	}
}
