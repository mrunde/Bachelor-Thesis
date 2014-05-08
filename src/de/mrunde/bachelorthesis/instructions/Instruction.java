package de.mrunde.bachelorthesis.instructions;

import de.mrunde.bachelorthesis.basics.Landmark;
import de.mrunde.bachelorthesis.basics.Maneuver;

/**
 * The instruction is pushed to the user at decision points giving instructions
 * about how to follow the route.<br/>
 * <br/>
 * This class is abstract. Specific instructions (e.g. landmark-based) are
 * implemented individually.
 * 
 * @author Marius Runde
 */
public abstract class Instruction {

	/**
	 * The maneuver type
	 */
	private int maneuverType;

	/**
	 * Verbal instruction for the maneuver
	 */
	private String maneuver;

	/**
	 * Global landmark off road (optional)
	 */
	private Landmark global;

	/**
	 * Super constructor for all sub-instruction classes
	 * 
	 * @param maneuverType
	 *            The maneuver type
	 */
	public Instruction(int maneuverType) {
		this.maneuverType = maneuverType;
		this.maneuver = Maneuver.getManeuverText(this.maneuverType);
		this.global = null;
	}

	/**
	 * Super constructor for all sub-instruction classes with global landmark
	 * 
	 * @param maneuverType
	 *            The maneuver type
	 * @param global
	 *            Global landmark off road
	 */
	public Instruction(int maneuverType, Landmark global) {
		this.maneuverType = maneuverType;
		this.global = global;
	}

	/**
	 * @return The maneuver type
	 */
	public int getManeuverType() {
		return this.maneuverType;
	}

	/**
	 * @return The verbal instruction for the maneuver
	 */
	public String getManeuver() {
		return this.maneuver;
	}

	/**
	 * @return The global landmark off road
	 */
	public Landmark getGlobal() {
		return global;
	}
}
