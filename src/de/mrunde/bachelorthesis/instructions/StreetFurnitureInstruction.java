package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Maneuver;
import de.mrunde.bachelorthesis.basics.StreetFurniture;

/**
 * This is a "street furniture"-based instruction.
 * 
 * @author Marius Runde
 */
public class StreetFurnitureInstruction extends Instruction {

	/**
	 * Street furniture at the decision point
	 */
	private StreetFurniture streetFurniture;

	/**
	 * Second street furniture before the decision point (optional)
	 */
	private StreetFurniture secondStreetFurniture;

	/**
	 * Constructor of the StreetFurnitureInstruction class for an instruction
	 * from one street furniture
	 * 
	 * @param decisionPoint
	 *            The decision point
	 * @param maneuverType
	 *            The maneuver type
	 * @param streetFurniture
	 *            The street furniture at the decision point
	 */
	public StreetFurnitureInstruction(GeoPoint decisionPoint, int maneuverType,
			StreetFurniture streetFurniture) {
		super(decisionPoint, maneuverType);
		this.streetFurniture = streetFurniture;
	}

	/**
	 * Constructor of the StreetFurnitureInstruction class for an instruction
	 * from two street furniture
	 * 
	 * @param decisionPoint
	 *            The decision point
	 * @param maneuverType
	 *            The maneuver type
	 * @param streetFurniture
	 *            The street furniture at the decision point
	 * @param secondStreetFurniture
	 *            The street furniture before the decision point
	 */
	public StreetFurnitureInstruction(GeoPoint decisionPoint, int maneuverType,
			StreetFurniture streetFurniture,
			StreetFurniture secondStreetFurniture) {
		super(decisionPoint, maneuverType);
		this.streetFurniture = streetFurniture;
		// Check if the street furniture have the same category
		if (this.streetFurniture.getCategory().equals(
				secondStreetFurniture.getCategory())) {
			this.secondStreetFurniture = secondStreetFurniture;
		} else {
			this.secondStreetFurniture = null;
		}
	}

	/**
	 * @return The instruction as a verbal text
	 */
	public String toString() {
		if (Maneuver.isTurnAction(super.getManeuverType())) {
			String instruction;
			// Check if the second street furniture is set
			if (this.secondStreetFurniture != null) {
				// Create an instruction with two street furniture
				instruction = super.getManeuver() + " after the second "
						+ this.streetFurniture.getCategory();
			} else {
				// Create an instruction with one street furniture
				instruction = super.getManeuver() + " after the "
						+ this.streetFurniture.getCategory();
			}
			return instruction;
		} else {
			return null;
		}
	}
}
