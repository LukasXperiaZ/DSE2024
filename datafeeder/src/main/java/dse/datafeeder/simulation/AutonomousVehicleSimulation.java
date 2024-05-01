package dse.datafeeder.simulation;

import dse.datafeeder.constants.Direction;
import dse.datafeeder.dto.Coordinates;
import dse.datafeeder.dto.LeadingVehicleData;
import dse.datafeeder.dto.VehicleData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static dse.datafeeder.constants.Constants.*;

public class AutonomousVehicleSimulation {

    private VehicleData vehicleData;

    // Semaphore necessary when this vehicle is made a leading vehicle.
    private final Semaphore vehicleSemaphore = new Semaphore(1);

    private final List<Instruction> instructions;
    private final Iterator<Instruction> instructionIterator;
    private Instruction currentInstruction;

    private ScheduledExecutorService executor;

    public AutonomousVehicleSimulation() {
        Coordinates startPoint = new Coordinates(START_FIRST_LANE_LON, START_FIRST_LANE_LAT);
        this.vehicleData = new VehicleData("5YJ3E7EB7KF240393", startPoint, 100.0, 1,
                new Timestamp(System.currentTimeMillis()));
        this.instructions = this.generateInstructions();
        this.instructionIterator = this.instructions.iterator();
        this.currentInstruction = this.instructionIterator.next();
    }

    public void startSimulation() {
        this.executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(simulation, 0, 100, TimeUnit.MILLISECONDS);
    }

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

    private List<Instruction> generateInstructions() {
        List<Instruction> instructions = new ArrayList<>();

        // Note that 1 tick = 100ms, thus e.g. 100 ticks = 10 seconds.
        // A minimum of 65 ticks is needed to do a lane change.

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

    private final Runnable simulation = new Runnable() {
        @Override
        public void run() {
            try {
                vehicleSemaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!(vehicleData instanceof LeadingVehicleData)) {
                // --- Vehicle is not a leading vehicle ---
                VehicleData vehicleDataCopy = vehicleData.deepCopy();

                vehicleSemaphore.release();

                // send updated VehicleDataCopy
                // TODO
                System.out.println("Speed: " + vehicleDataCopy.getSpeed() + " Lane: " + vehicleDataCopy.getLane());
            } else {
                // --- Vehicle is a leading vehicle ---
                simulateLeadingVehicle();
            }

        }

        private void simulateLeadingVehicle() {
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
            double diffLane1 = Math.abs(vehicleData.getCoordinates().getLatitude() - START_FIRST_LANE_LAT);
            double diffLane2 = Math.abs(vehicleData.getCoordinates().getLatitude() - START_SECOND_LANE_LAT);
            double diffLane3 = Math.abs(vehicleData.getCoordinates().getLatitude() - START_THIRD_LANE_LAT);

            if (diffLane1 < diffLane2 && diffLane1 < diffLane3) {
                // The car is closest to lane 1
                vehicleData.setLane(1);
            } else if (diffLane2 < diffLane1 && diffLane2 < diffLane3) {
                vehicleData.setLane(2);
            } else {
                vehicleData.setLane(3);
            }

            LeadingVehicleData leadingVehicleDataCopy = (LeadingVehicleData) vehicleData.deepCopy();
            // Set target speed and lane to the current speed and lane.
            leadingVehicleDataCopy.setTargetSpeed(leadingVehicleDataCopy.getSpeed());
            leadingVehicleDataCopy.setTargetLane(leadingVehicleDataCopy.getLane());

            // Instruction has been executed for 1 tick. Decrease the tick
            currentInstruction.setTicks(currentInstruction.getTicks() - 1);

            vehicleSemaphore.release();

            // send updated LeadingVehicleDataCopy.
            // TODO
            System.out.println("Speed: " + leadingVehicleDataCopy.getSpeed() + " Lane: " + leadingVehicleDataCopy.getLane());
        }
    };


}
