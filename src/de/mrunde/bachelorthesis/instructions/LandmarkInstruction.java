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
	 * Is the landmark on the left or right from the user's perspective?
	 */
	private boolean leftTurn;

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
			Landmark local, boolean leftTurn) {
		super(decisionPoint, maneuverType);
		this.local = local;
		this.leftTurn = leftTurn;
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
			if (this.leftTurn) {
				instruction += " on your left";
			} else {
				instruction += " on your right";
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
			if (this.leftTurn) {
				instruction += " on your left";
			} else {
				instruction += " on your right";
			}
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

	/**
	 * @return Whether the landmark is on the left (<code>TRUE</code>) or right
	 *         (<code>FALSE</code>) from the user's perspective
	 */
	public boolean getLeftTurn() {
		return this.leftTurn;
	}
}
