package dse.datafeeder;

import dse.datafeeder.constants.Constants;
import dse.datafeeder.constants.Direction;
import dse.datafeeder.dto.Coordinates;
import dse.datafeeder.dto.VehicleData;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static dse.datafeeder.constants.Constants.*;

/*
 * This class includes some basic tests to test the correct behaviour of important methods.
 */
class DatafeederApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testVehicleDataCreation() {
        try {
            VehicleData vehicleData = new VehicleData("SB164ABN1PE082986", new Coordinates(1, 2), 100.0, 1, new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            assert false;
        }
        assert true;
    }

    @Test
    void testChangeForward() {
        Coordinates coordinates = new Coordinates(START_AUT_FIRST_LANE_LON, FIRST_LANE_LAT);

        coordinates.changeCoordinatesByDistance(1, Direction.Forward);
        assert (Math.abs(coordinates.getLatitude() - FIRST_LANE_LAT) < 0.000001);
        assert (coordinates.getLongitude() > START_AUT_FIRST_LANE_LON);
    }

    @Test
    void testChangeLeftRight() {
        Coordinates coordinates = new Coordinates(START_AUT_FIRST_LANE_LON, FIRST_LANE_LAT);

        coordinates.changeCoordinatesByDistance(1, Direction.Left);
        assert(coordinates.getLatitude() < FIRST_LANE_LAT);
        assert(coordinates.getLongitude() == START_AUT_FIRST_LANE_LON);

        coordinates.changeCoordinatesByDistance(1, Direction.Right);
        assert(coordinates.getLatitude() - FIRST_LANE_LAT < 0.000001);
        assert(coordinates.getLongitude() == START_AUT_FIRST_LANE_LON);

        coordinates.changeCoordinatesByDistance(1, Direction.Right);
        assert(coordinates.getLatitude() > FIRST_LANE_LAT);
        assert(coordinates.getLongitude() == START_AUT_FIRST_LANE_LON);
    }

    @Test
    void testDistanceBetweenVehicles() {
        Coordinates startAutVehicle = new Coordinates(START_AUT_FIRST_LANE_LON, FIRST_LANE_LAT);
        Coordinates startNonVehicle = new Coordinates(START_NON_FIRST_LANE_LON, FIRST_LANE_LAT);

        double distance = startNonVehicle.calculateDistanceTo(startAutVehicle);
        System.out.println(distance);
        assert(distance > 250);
    }
}
