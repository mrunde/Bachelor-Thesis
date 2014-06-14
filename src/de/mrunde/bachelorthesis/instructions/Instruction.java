package de.mrunde.bachelorthesis.instructions;

import com.mapquest.android.maps.GeoPoint;

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
	 * The decision point where the maneuver has to be done or where the global
	 * landmark or local landmark is along the route so the instruction has to
	 * be spoken at that position.
	 */
	private GeoPoint decisionPoint;

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
	public Instruction(GeoPoint decisionPoint, int maneuverType) {
		this.decisionPoint = decisionPoint;
		this.maneuverType = maneuverType;
		this.maneuver = Maneuver.getManeuverText(this.maneuverType);
		this.global = null;
	}

	/**
	 * @return The decision point
	 */
	public GeoPoint getDecisionPoint() {
		return this.decisionPoint;
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
