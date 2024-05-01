package dse.datafeeder;

import dse.datafeeder.constants.Direction;
import dse.datafeeder.dto.Coordinates;
import dse.datafeeder.dto.VehicleData;
import dse.datafeeder.simulation.AutonomousVehicleSimulation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static dse.datafeeder.constants.Constants.*;

@SpringBootTest
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
    void testChaneLeftRight() {
        Coordinates coordinates = new Coordinates(START_FIRST_LANE_LON, START_FIRST_LANE_LAT);

        coordinates.changeCoordinatesByDistance(1, Direction.Left);
        assert(coordinates.getLatitude() < START_FIRST_LANE_LAT);
        assert(coordinates.getLongitude() == START_FIRST_LANE_LON);

        coordinates.changeCoordinatesByDistance(1, Direction.Right);
        assert(coordinates.getLatitude() - START_FIRST_LANE_LAT < 0.000001);
        assert(coordinates.getLongitude() == START_FIRST_LANE_LON);

        coordinates.changeCoordinatesByDistance(1, Direction.Right);
        assert(coordinates.getLatitude() > START_FIRST_LANE_LAT);
        assert(coordinates.getLongitude() == START_FIRST_LANE_LON);
    }

    @Test
    void testAutonomousVehicleSimulation() {
        AutonomousVehicleSimulation autonomousVehicleSimulation = new AutonomousVehicleSimulation();

        autonomousVehicleSimulation.startSimulation();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            assert false;
        }

        autonomousVehicleSimulation.makeLeadingVehicle();

        try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {
            assert false;
        }
    }

}
