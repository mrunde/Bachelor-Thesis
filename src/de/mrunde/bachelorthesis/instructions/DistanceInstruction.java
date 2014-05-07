package de.mrunde.bachelorthesis.instructions;

/**
 * This is a distance-based instruction.
 * 
 * @author Marius Runde
 */
public class DistanceInstruction extends Instruction {

	/**
	 * Distance to the decision point
	 */
	private Integer distance;

	/**
	 * Constructor of the DistanceInstruction class
	 * 
	 * @param maneuver
	 *            The maneuver
	 * @param distance
	 *            Distance to the decision point
	 */
	public DistanceInstruction(Integer maneuver, Integer distance) {
		super(maneuver);

		this.distance = distance;
	}

	/**
	 * @return The Instruction as a verbal text
	 */
	public String toString() {
		String instruction = super.getManeuver() + " in " + distance
				+ " meters";

		return null;
	}
}
