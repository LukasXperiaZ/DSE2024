package dse.datafeeder.simulation;

import dse.datafeeder.dto.Coordinates;
import dse.datafeeder.dto.VehicleData;

import java.sql.Timestamp;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;

import static dse.datafeeder.constants.Constants.*;

/*
 * This class is used for simulating a non-autonomous vehicle.
 * It assumes, that the vehicle goes on a straight road in the direction of +longitude.
 * Furthermore, steering left corresponds to -latitude and steering right to +latitude.
 */
public class NonAutonomousVehicleSimulation {

    // Example vin.
    private final String vin = "XP7VGCEJXPB204655";

    private VehicleData vehicleData;

    // The instruction that should be done next.
    private Instruction nextInstruction;
    private Semaphore nextInstructionSemaphore;

    // The instruction that should be done if the next instruction is being updated.
    private Instruction previousInstruction;

    private ScheduledExecutorService executor;

    public NonAutonomousVehicleSimulation() {
        Coordinates startPoint = new Coordinates(START_NON_FIRST_LANE_LON, START_NON_FIRST_LANE_LAT);
        this.vehicleData = new VehicleData(vin, startPoint, 110.0, 1,
                new Timestamp(System.currentTimeMillis()));
    }
}
