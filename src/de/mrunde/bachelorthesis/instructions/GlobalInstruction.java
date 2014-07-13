package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Landmark;
import de.mrunde.bachelorthesis.basics.LandmarkCategory;

/**
 * This is a "global landmark"-based instruction.
 * 
 * @author Marius Runde
 */
public class GlobalInstruction extends Instruction {

	/**
	 * The global landmark along the route
	 */
	private Landmark global;

	/**
	 * Constructor of the GlobalInstruction class
	 * 
	 * @param instructionPoint
	 *            The point where the instruction should be spoken
	 * @param global
	 *            The global landmark along the route
	 */
	public GlobalInstruction(GeoPoint instructionPoint, Landmark global) {
		super(instructionPoint, 0);
		this.global = global;
	}

	/**
	 * @return The instruction as a verbal text
	 */
	public String toString() {
		String instruction = "You will pass ";
		if (LandmarkCategory.SIGHTSEEING.equals(this.global.getCategory())) {
			// Use landmark title for landmarks of the category SIGHTSEEING
			instruction += "the " + this.global.getTitle() + " soon";
		} else {
			// Use landmark category for all other landmarks
			instruction += "the " + this.global.getFormattedCategory()
					+ " soon";
		}
		return instruction;
	}

	/**
	 * @return The extended instruction as a verbal text which uses the title of
	 *         the landmark instead of its category
	 */
	public String toExtendedString() {
		String instruction = "You will pass the " + this.global.getTitle()
				+ " soon";
		return instruction;
	}

	/**
	 * @return The global landmark along the route
	 */
	public Landmark getGlobal() {
		return this.global;
	}
}
