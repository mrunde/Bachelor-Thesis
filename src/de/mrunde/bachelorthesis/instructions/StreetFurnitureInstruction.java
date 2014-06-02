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
	 * Constructor of the StreetFurnitureInstruction class
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
	 * @return The instruction as a verbal text
	 */
	public String toString() {
		if (Maneuver.isTurnAction(super.getManeuverType())) {
			String instruction = super.getManeuver() + " after the "
					+ this.streetFurniture.getCategory();
			return instruction;
		} else {
			return null;
		}
	}
}
