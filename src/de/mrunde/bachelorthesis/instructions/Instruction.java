package de.mrunde.bachelorthesis.instructions;

import de.mrunde.bachelorthesis.basics.Landmark;

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
	 * The maneuver
	 */
	private Integer maneuver;

	/**
	 * Global landmark off road (optional)
	 */
	private Landmark global;

	/**
	 * Super constructor for all sub-instruction classes
	 * 
	 * @param maneuver
	 *            The maneuver
	 */
	public Instruction(Integer maneuver) {
		this.maneuver = maneuver;
		this.global = null;
	}

	/**
	 * Super constructor for all sub-instruction classes with global landmark
	 * 
	 * @param maneuver
	 *            The maneuver
	 * @param global
	 *            Global landmark off road
	 */
	public Instruction(Integer maneuver, Landmark global) {
		this.maneuver = maneuver;
		this.global = global;
	}

	/**
	 * @return The maneuver
	 */
	public Integer getManeuver() {
		return maneuver;
	}

	/**
	 * @return The global landmark off road
	 */
	public Landmark getGlobal() {
		return global;
	}
}
