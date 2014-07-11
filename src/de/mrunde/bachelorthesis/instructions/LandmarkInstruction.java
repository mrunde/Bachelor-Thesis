package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Landmark;
import de.mrunde.bachelorthesis.basics.LandmarkCategory;
import de.mrunde.bachelorthesis.basics.Maneuver;

/**
 * This is a landmark-based instruction.
 * 
 * @author Marius Runde
 */
public class LandmarkInstruction extends Instruction {

	/**
	 * The local landmark at the decision point
	 */
	private Landmark local;

	/**
	 * Constructor of the LandmarkInstruction class
	 * 
	 * @param decisionPoint
	 *            The decision point
	 * @param maneuverType
	 *            The maneuver type
	 * @param local
	 *            The local landmark at the decision point
	 */
	public LandmarkInstruction(GeoPoint decisionPoint, int maneuverType,
			Landmark local) {
		super(decisionPoint, maneuverType);
		this.local = local;
	}

	/**
	 * @return The instruction as a verbal text
	 */
	public String toString() {
		if (Maneuver.isTurnAction(super.getManeuverType())) {
			String instruction = super.getManeuver() + " at the ";
			if (LandmarkCategory.SIGHTSEEING.equals(this.local.getCategory())) {
				// Use landmark title for landmarks of the category SIGHTSEEING
				instruction += this.local.getTitle();
			} else {
				// Use landmark category for all other landmarks
				instruction += this.local.getFormattedCategory();
			}
			return instruction;
		} else {
			return null;
		}
	}

	/**
	 * @return The extended instruction as a verbal text which uses the title of
	 *         the landmark instead of its category
	 */
	public String toExtendedString() {
		if (Maneuver.isTurnAction(super.getManeuverType())) {
			String instruction = super.getManeuver() + " at the "
					+ this.local.getTitle();
			return instruction;
		} else {
			return null;
		}
	}

	/**
	 * @return The local landmark at the decision point
	 */
	public Landmark getLocal() {
		return this.local;
	}
}
