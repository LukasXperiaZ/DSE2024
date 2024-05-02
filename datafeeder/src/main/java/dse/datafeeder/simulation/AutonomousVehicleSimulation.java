package dse.datafeeder.simulation;

import dse.datafeeder.constants.Direction;
import dse.datafeeder.dto.Coordinates;
import dse.datafeeder.dto.LeadingVehicleData;
import dse.datafeeder.dto.VehicleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static dse.datafeeder.constants.Constants.*;

/*
 * This class is used for simulating an autonomous vehicle.
 * It assumes, that the vehicle goes on a straight road in the direction of +longitude.
 * Furthermore, steering left corresponds to -latitude and steering right to +latitude.
 */

public class AutonomousVehicleSimulation {

    private final Logger logger = LoggerFactory.getLogger(AutonomousVehicleSimulation.class);

    // Example vin.
    private final String vin = "5YJ3E7EB7KF240393";

    // VehicleData that is being updated every tick of the simulation.
    private VehicleData vehicleData;

    // A semaphore is necessary when this vehicle is made a leading vehicle.
    private final Semaphore vehicleSemaphore = new Semaphore(1);

    private final List<Instruction> instructions;
    private final Iterator<Instruction> instructionIterator;
    private Instruction currentInstruction;

    private ScheduledExecutorService executor;

    // Initialize the vehicle to be at a defined start point (first lane).
    public AutonomousVehicleSimulation() {
        Coordinates startPoint = new Coordinates(START_AUT_FIRST_LANE_LON, START_AUT_FIRST_LANE_LAT);
        this.vehicleData = new VehicleData(vin, startPoint, 100.0, 1,
                new Timestamp(System.currentTimeMillis()));
        this.instructions = this.generateInstructions();
        this.instructionIterator = this.instructions.iterator();
        this.currentInstruction = this.instructionIterator.next();
    }

    // Start the simulation: This will generate a new executor running in a thread pool that simulates the autonomous car.
    public void startSimulation() {
        this.executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(simulation, 0, 100, TimeUnit.MILLISECONDS);
    }

    // Transform the autonomous vehicle into a leading vehicle. It will now also send the target speed and lane.
    public void makeLeadingVehicle() {
        try {
            this.vehicleSemaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Make this vehicle a leading vehicle.
        this.vehicleData = new LeadingVehicleData(this.vehicleData.getVin(), this.vehicleData.getCoordinates(),
                this.vehicleData.getSpeed(), this.vehicleData.getLane(), this.vehicleData.getTimestamp(),
                this.vehicleData.getSpeed(), this.vehicleData.getLane());

        this.vehicleSemaphore.release();
    }

    public void stayLeadingVehicle() {
        // Does nothing as nothing has to change.
    }

    // Transform the leading vehicle into a non-leading vehicle. It will now only send normal VehicleData.
    public void disableLeadingVehicle() {
        try {
            this.vehicleSemaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Make this vehicle a non-leading vehicle.
        this.vehicleData = new VehicleData(this.vehicleData.getVin(), this.vehicleData.getCoordinates(),
                this.vehicleData.getSpeed(), this.vehicleData.getLane(), this.vehicleData.getTimestamp());

        this.vehicleSemaphore.release();
    }

    public void stayNonLeadingVehicle() {
        // DOes nothing as nothing has to change.
    }

    // This generates the instructions used for the simulation scenario.
    private List<Instruction> generateInstructions() {
        List<Instruction> instructions = new ArrayList<>();

        // Note that 1 tick = 100ms, thus e.g. 100 ticks = 10 seconds.
        // A minimum of 65 ticks is needed to do a lane change.

        // This is the initial instruction that will be executed until the vehicle is made a leading vehicle.
        instructions.add(new Instruction(100.0, 1, 1));

        // Decrease the speed.
        instructions.add(new Instruction(90.0, 1, 100));

        // Increase the speed and change to lane 2.
        instructions.add(new Instruction(110.0, 2, 100));

        // Increase the speed again and change to lane 3.
        instructions.add(new Instruction(130.0, 3, 80));

        // Decrease the speed and change to lane 2.
        instructions.add(new Instruction(125.0, 2, 70));

        // Change to lane 1.
        instructions.add(new Instruction(125.0, 1, 65));

        return instructions;
    }

    // This is a runnable doing the simulation of the autonomous car.
    private final Runnable simulation = new Runnable() {
        @Override
        public void run() {
            // Try to acquire the semaphore. If this blocks, the vehicle is currently transformed into a leading vehicle.
            try {
                vehicleSemaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!(vehicleData instanceof LeadingVehicleData)) {
                // --- Vehicle is not a leading vehicle ---
                // Increase the ticks of the current instruction, so that the vehicle executes this instruction until
                // it is made a leading vehicle.
                currentInstruction.setTicks(currentInstruction.getTicks() + 1);
            }

            simulateVehicle();

            VehicleData deepCopy = vehicleData.deepCopy();
            // Set target speed and lane to the current speed and lane.
            LeadingVehicleData leadingVehicleData = new LeadingVehicleData(deepCopy, deepCopy.getSpeed(), deepCopy.getLane());

            vehicleSemaphore.release();

            // send updated LeadingVehicleDataCopy.
            // TODO
            logger.debug("Speed: {}, Lane: {}, Coordinates: {}", leadingVehicleData.getSpeed(),
                    leadingVehicleData.getLane(), leadingVehicleData.getCoordinates());
        }

        // Simulate the leading vehicle: Try to reach what the current instruction says by adjusting the speed
        // and/or steering left or right.
        private void simulateVehicle() {
            // Get the current instruction
            if (currentInstruction.getTicks() < 1) {
                if (instructionIterator.hasNext()) {
                    currentInstruction = instructionIterator.next();
                }
            }

            // Adjust the speed
            // It can adjust 1km/h per tick
            if (vehicleData.getSpeed() < currentInstruction.getSpeed()) {
                // The vehicle has to go faster.
                vehicleData.setSpeed(vehicleData.getSpeed() + SPEED_INCREASE_PER_TICK);
            } else if (vehicleData.getSpeed() > currentInstruction.getSpeed()) {
                // The vehicle has to slow down
                vehicleData.setSpeed(vehicleData.getSpeed() - SPEED_INCREASE_PER_TICK);
            }
            // Adjust the coordinates to go forward
            double distance = (vehicleData.getSpeed() / 3.6) * 0.1;
            vehicleData.getCoordinates().changeCoordinatesByDistance(distance, Direction.Forward);


            // Adjust the lane
            if (vehicleData.getLane() < currentInstruction.getLane()) {
                // The vehicle has to change one lane to the left.
                vehicleData.getCoordinates().changeCoordinatesByDistance(MOVEMENT_SIDEWAYS_PER_TICK, Direction.Left);

            } else if (vehicleData.getLane() > currentInstruction.getLane()) {
                // The vehicle has to change one lane to the right.
                vehicleData.getCoordinates().changeCoordinatesByDistance(MOVEMENT_SIDEWAYS_PER_TICK, Direction.Right);
            }
            // Adjust the lane number
            double diffLane1 = Math.abs(vehicleData.getCoordinates().getLatitude() - START_AUT_FIRST_LANE_LAT);
            double diffLane2 = Math.abs(vehicleData.getCoordinates().getLatitude() - START_SECOND_LANE_LAT);
            double diffLane3 = Math.abs(vehicleData.getCoordinates().getLatitude() - START_THIRD_LANE_LAT);

            if (diffLane1 < diffLane2 && diffLane1 < diffLane3) {
                // The car is closest to lane 1
                vehicleData.setLane(1);
            } else if (diffLane2 < diffLane1 && diffLane2 < diffLane3) {
                // The car is closest to lane 2
                vehicleData.setLane(2);
            } else {
                // The car is closest to lane 3
                vehicleData.setLane(3);
            }

            vehicleData.setTimestamp(new Timestamp(System.currentTimeMillis()));

            // Instruction has been executed for 1 tick. Decrease the tick
            currentInstruction.setTicks(currentInstruction.getTicks() - 1);
        }
    };


}
