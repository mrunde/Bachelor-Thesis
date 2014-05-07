package de.mrunde.bachelorthesis.basics;

/**
 * This is the Maneuver class that converts the maneuver types of MapQuest into
 * the verbal instructions.
 * 
 * @author Marius Runde
 */
public abstract class Maneuver {

	// --- The maneuver types of MapQuest ---
	// --- The maneuver types are primarily listed here to enable the user to
	// include the maneuver type values manually in the application. ---

	public static final int NONE = 0;
	public static final int STRAIGHT = 1;
	public static final int BECOMES = 2;
	public static final int SLIGHT_LEFT = 3;
	public static final int LEFT = 4;
	public static final int SHARP_LEFT = 5;
	public static final int SLIGHT_RIGHT = 6;
	public static final int RIGHT = 7;
	public static final int SHARP_RIGHT = 8;
	public static final int STAY_LEFT = 9;
	public static final int STAY_RIGHT = 10;
	public static final int STAY_STRAIGHT = 11;
	public static final int UTURN = 12;
	public static final int UTURN_LEFT = 13;
	public static final int UTURN_RIGHT = 14;
	public static final int EXIT_LEFT = 15;
	public static final int EXIT_RIGHT = 16;
	public static final int RAMP_LEFT = 17;
	public static final int RAMP_RIGHT = 18;
	public static final int RAMP_STRAIGHT = 19;
	public static final int MERGE_LEFT = 20;
	public static final int MERGE_RIGHT = 21;
	public static final int MERGE_STRAIGHT = 22;
	public static final int ENTERING = 23;
	public static final int DESTINATION = 24;
	public static final int DESTINATION_LEFT = 25;
	public static final int DESTINATION_RIGHT = 26;
	public static final int ROUNDABOUT1 = 27;
	public static final int ROUNDABOUT2 = 28;
	public static final int ROUNDABOUT3 = 29;
	public static final int ROUNDABOUT4 = 30;
	public static final int ROUNDABOUT5 = 31;
	public static final int ROUNDABOUT6 = 32;
	public static final int ROUNDABOUT7 = 33;
	public static final int ROUNDABOUT8 = 34;
	public static final int TRANSIT_TAKE = 35;
	public static final int TRANSIT_TRANSFER = 36;
	public static final int TRANSIT_ENTER = 37;
	public static final int TRANSIT_EXIT = 38;
	public static final int TRANSIT_REMAIN_ON = 39;

	// --- End of maneuver types of MapQuest ---

	/**
	 * Store the verbal maneuver instructions in an array
	 */
	private static final String[] maneuverTexts = new String[] {
			"No maneuver occurs here.", "Continue straight.",
			"No maneuver occurs here. Road name changes.",
			"Make a slight left.", "Turn left.", "Make a sharp left.",
			"Make a slight right.", "Turn right.", "Make a sharp right.",
			"Stay left.", "Stay right.", "Stay straight.", "Make a U-turn.",
			"Make a left U-turn.", "Make a right U-turn.", "Exit left.",
			"Exit right.", "Take the ramp on the left.",
			"Take the ramp on the right.", "Take the ramp straight ahead.",
			"Merge left.", "Merge right.", "Merge.", "Enter state/province.",
			"Arrive at your destination.",
			"Arrive at your destination on the left.",
			"Arrive at your destination on the right.",
			"Enter the roundabout and take the 1st exit.",
			"Enter the roundabout and take the 2nd exit.",
			"Enter the roundabout and take the 3rd exit.",
			"Enter the roundabout and take the 4th exit.",
			"Enter the roundabout and take the 5th exit.",
			"Enter the roundabout and take the 6th exit.",
			"Enter the roundabout and take the 7th exit.",
			"Enter the roundabout and take the 8th exit.",
			"Take a public transit bus or rail line.",
			"Transfer to a public transit bus or rail line.",
			"Enter a public transit bus or rail station",
			"Exit a public transit bus or rail station",
			"Remain on the current bus/rail car" };

	/**
	 * Get the corresponding verbal maneuver instruction of the maneuver type
	 * 
	 * @param maneuverType
	 *            The maneuver type received from MapQuest
	 * @return The verbal maneuver instruction
	 */
	public static String getManeuverText(int maneuverType) {
		return maneuverTexts[maneuverType];
	}

	/**
	 * Check if the maneuver requires a turn action
	 * 
	 * @param maneuverType
	 *            The maneuver type received from MapQuest
	 * @return TRUE: Turn action is required.<br/>
	 *         FALSE: No turn action is required.
	 */
	public static boolean isTurnAction(int maneuverType) {
		if (maneuverType == 0 || maneuverType == 2) {
			return false;
		} else {
			return true;
		}
	}
}