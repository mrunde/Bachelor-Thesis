package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

/**
 * This is a "street furniture"-based instruction.
 * 
 * @author Marius Runde
 */
public class StreetFurnitureInstruction extends Instruction {

	/**
	 * Constructor of the StreetFurnitureInstruction class
	 */
	public StreetFurnitureInstruction(GeoPoint decisionPoint, int maneuverType) {
		super(decisionPoint, maneuverType);
	}
}
