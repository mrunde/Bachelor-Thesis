package de.mrunde.bachelorthesis.instructions;

import de.mrunde.bachelorthesis.basics.Landmark;
import de.mrunde.bachelorthesis.basics.LandmarkCategory;
import de.mrunde.bachelorthesis.basics.Maneuver;

/**
 * This is a now instruction.
 * 
 * @author Marius Runde
 */
public class NowInstruction extends Instruction {

	/**
	 * The global landmark of a <code>GlobalInstruction</code> to be used
	 */
	private Landmark global;

	/**
	 * Is the global landmark of the category sightseeing or not?
	 */
	private boolean sightseeingLandmark;

	/**
	 * Is the global landmark on the left or right from the user's perspective?
	 */
	private boolean leftTurn;

	/**
	 * Constructor of the LandmarkInstruction class
	 * 
	 * @param instruction
	 *            The current instruction
	 */
	public NowInstruction(Instruction instruction) {
		super(instruction.getDecisionPoint(), instruction.getManeuverType());
		if (instruction.getClass() == GlobalInstruction.class) {
			this.global = ((GlobalInstruction) instruction).getGlobal();
			this.sightseeingLandmark = ((GlobalInstruction) instruction)
					.getGlobal().getCategory() == LandmarkCategory.SIGHTSEEING;
			this.leftTurn = ((GlobalInstruction) instruction).getLeftTurn();
		} else {
			this.global = null;
		}
	}

	/**
	 * @return The instruction as a verbal text
	 */
	public String toString() {
		if (this.global != null) {
			// Global now instruction
			String instruction = "You pass the ";
			if (this.sightseeingLandmark) {
				instruction += this.global.getTitle();
			} else {
				instruction += this.global.getCategory();
			}
			if (this.leftTurn) {
				instruction += " now on your left";
			} else {
				instruction += " now on your right";
			}
			return instruction;
		} else if (Maneuver.isTurnAction(super.getManeuverType())) {
			// Local now instruction
			String instruction = super.getManeuver() + " now";
			return instruction;
		} else {
			// No turn action happens, can be ignored
			return null;
		}
	}
}
