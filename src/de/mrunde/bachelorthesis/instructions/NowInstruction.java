package de.mrunde.bachelorthesis.instructions;

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
	 * @param instruction
	 *            The current instruction
	 */
	public NowInstruction(Instruction instruction) {
		super(instruction.getDecisionPoint(), instruction.getManeuverType());
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
