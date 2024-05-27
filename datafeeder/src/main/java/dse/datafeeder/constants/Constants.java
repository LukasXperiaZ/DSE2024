package dse.datafeeder.constants;

/*
 * This class contains constants used by the simulation classes.
 */
public class Constants {

    public static final int tickMill = 100;

    // Earth radios approximated in meter.
    public static final int EARTH_RADIUS = 6371000;

    // We assume that one lane is ~3.25m wide.
    // Start position for the autonomous vehicle.
    public static final double START_AUT_FIRST_LANE_LON = 48.199064;

    // Start position for the non-autonomous vehicle.
    public static final double START_NON_FIRST_LANE_LON = 48.196641;

    public static final double FIRST_LANE_LAT = 16.370004;

    public static final double START_SECOND_LANE_LON = 48.199064;
    public static final double SECOND_LANE_LAT = 16.369975;

    public static final double START_THIRD_LANE_LON = 48.199064;
    public static final double THIRD_LANE_LAT = 16.369946;

    public static final double MOVEMENT_SIDEWAYS_PER_TICK = 0.1;    // in meters
    public static final int SPEED_INCREASE_PER_TICK = 1;            // in km/h
}
