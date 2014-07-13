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
	 * Is the landmark of the category sightseeing or not?
	 */
	private boolean sightseeingLandmark;

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
				instruction += this.global.getTitle() + " now";
			} else {
				instruction += this.global.getCategory() + " now";
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
