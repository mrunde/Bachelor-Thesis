package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

import de.mrunde.bachelorthesis.basics.Maneuver;

/**
 * This is a "street furniture"-based instruction.
 * 
 * @author Marius Runde
 */
public class StreetFurnitureInstruction extends Instruction {

	/**
	 * Number of street furniture
	 */
	private int number;

	/**
	 * Street furniture category
	 */
	private String category;

	/**
	 * Constructor of the StreetFurnitureInstruction class
	 * 
	 * @param decisionPoint
	 *            The decision point
	 * @param maneuverType
	 *            The maneuver type
	 * @param number
	 *            Number of street furniture
	 * @param category
	 *            Street furniture category (already formatted so that "_" are
	 *            replaced with spaces)
	 */
	public StreetFurnitureInstruction(GeoPoint decisionPoint, int maneuverType,
			int number, String category) {
		super(decisionPoint, maneuverType);
		this.number = number;
		this.category = category;
	}

	/**
	 * @return The instruction as a verbal text
	 */
	public String toString() {
		String instruction = null;
		if (Maneuver.isTurnAction(super.getManeuverType())) {
			switch (this.number) {
			case 1:
				instruction = super.getManeuver() + " after the 1st "
						+ this.category;
				break;
			case 2:
				instruction = super.getManeuver() + " after the 2nd "
						+ this.category;
				break;
			default:
				break;
			}
		}
		return instruction;
	}
}
