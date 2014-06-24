package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Landmark;

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
		String category = this.global.getFormattedCategory();
		if (category.startsWith("a") || category.startsWith("e")
				|| category.startsWith("i") || category.startsWith("o")
				|| category.startsWith("u")) {
			instruction = "an " + category + " soon";
		} else {
			instruction = "a " + category + " soon";
		}
		return instruction;
	}

	/**
	 * @return The extended instruction as a verbal text which uses the title of
	 *         the landmark instead of its category
	 */
	public String toExtendedString() {
		String instruction = "You will pass ";
		String title = this.global.getTitle();
		if (title.startsWith("a") || title.startsWith("e")
				|| title.startsWith("i") || title.startsWith("o")
				|| title.startsWith("u")) {
			instruction = "an " + title + " soon";
		} else {
			instruction = "a " + title + " soon";
		}
		return instruction;
	}

	/**
	 * @return The global landmark along the route
	 */
	public Landmark getGlobal() {
		return this.global;
	}
}
