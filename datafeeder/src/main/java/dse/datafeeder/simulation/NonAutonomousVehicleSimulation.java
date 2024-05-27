package dse.datafeeder.simulation;

import dse.datafeeder.constants.Constants;
import dse.datafeeder.constants.Direction;
import dse.datafeeder.dto.Coordinates;
import dse.datafeeder.dto.FVState;
import dse.datafeeder.dto.RegisterCar;
import dse.datafeeder.dto.VehicleData;
import dse.datafeeder.rabbitMq.RabbitMq;
import dse.datafeeder.rest.InventoryClient;

import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static dse.datafeeder.constants.Constants.*;

/*
 * This class is used for simulating a non-autonomous vehicle.
 * It assumes, that the vehicle goes on a straight road in the direction of +longitude.
 * Furthermore, steering left corresponds to -latitude and steering right to +latitude.
 */
public class NonAutonomousVehicleSimulation {

    // Example vin.
    private final String vin = "XP7VGCEJXPB204655";

    private final VehicleData vehicleData;

    // The instruction that should be done next.
    private Instruction nextInstruction;
    // Semaphore required to manage concurrent access when updating the next instruction.
    private final Semaphore nextInstructionSemaphore = new Semaphore(1);

    // The instruction that should be done if the next instruction is being updated.
    private Instruction previousInstruction;
    // Semaphore required to manage concurrent access when updating the previous instruction.
    private final Semaphore previousInstructionSemaphore = new Semaphore(1);

    private boolean behaveBad = false;

    private ScheduledExecutorService executor;

    private final RabbitMq rabbitMq = new RabbitMq();

    public NonAutonomousVehicleSimulation() {
        Coordinates startPoint = new Coordinates(START_NON_FIRST_LANE_LON, Constants.FIRST_LANE_LAT);
        this.vehicleData = new VehicleData(vin, startPoint, 110.0, 1,
                new Timestamp(System.currentTimeMillis()));
        this.nextInstruction = new Instruction(110.0, 1, 1);
        this.previousInstruction = new Instruction(110.0, 1, 1);
    }

    public void startSimulation() {
        this.executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(simulation, 0, tickMill, TimeUnit.MILLISECONDS);
    }

    public void enableFM(FVState fvState) {
        this.setNextInstruction(new Instruction(fvState.getTargetSpeed(), fvState.getTargetLane(), 1));
    }

    public void stayInFM(FVState fvState) {
        this.setNextInstruction(new Instruction(fvState.getTargetSpeed(), fvState.getTargetLane(), 1));
    }

    // When disabling the follow mode, the car will go to the same speed and lane as the previous instruction said.
    public void disableFM() {
        try {
            previousInstructionSemaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Instruction prevInstructionCopy = previousInstruction.deepCopy();

        previousInstructionSemaphore.release();

        this.setNextInstruction(prevInstructionCopy);
    }

    public void stayInNonFM() {
        // Does nothing as the instruction is not being updated.
    }

    private void setNextInstruction(Instruction instruction) {
        // Block until the semaphore can be acquired.
        try {
            nextInstructionSemaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Update the next instruction
        nextInstruction = instruction;

        nextInstructionSemaphore.release();
    }

    private final Runnable simulation = new Runnable() {
        @Override
        public void run() {
            VehicleData vehicleDataCopy;

            if (nextInstructionSemaphore.tryAcquire()) {
                // Semaphore could be acquired, we can use the nextInstruction.
                Instruction instructionToUse = nextInstruction.deepCopy();
                nextInstructionSemaphore.release();

                simulateVehicle(instructionToUse);
                vehicleDataCopy = vehicleData.deepCopy();

                // Update the previous instruction.
                try {
                    previousInstructionSemaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                previousInstruction = instructionToUse;

                previousInstructionSemaphore.release();

            } else {
                // Semaphore could not be acquired, we use the previousInstruction instead.
                try {
                    previousInstructionSemaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Instruction instructionToUse = previousInstruction.deepCopy();
                previousInstructionSemaphore.release();

                simulateVehicle(instructionToUse);

                vehicleDataCopy = vehicleData.deepCopy();
            }

            // Send updated VehicleDataCopy
            rabbitMq.send(vehicleDataCopy);
        }

        // Simulate the following vehicle: Try to reach what the instruction says by adjusting the speed
        // and/or steering left or right.
        private void simulateVehicle(Instruction instruction) {
            // Adjust the speed
            // It can adjust 1km/h per tick
            if (vehicleData.getSpeed() < instruction.getSpeed()) {
                // The vehicle has to go faster.
                if (instruction.getSpeed() < 121.2 && instruction.getSpeed() > 120.8) {
                    // This "bug" is intentional to satisfy the simulation scenario:
                    // I.e. at the last speed change (speeding up to 121.0 km/h), the FV will not adjust its speed to
                    // the target speed.
                    behaveBad = true;
                } else if (!behaveBad) {
                    // Normal and expected behavior otherwise: The vehicle adjusts the speed.
                    vehicleData.setSpeed(vehicleData.getSpeed() + SPEED_INCREASE_PER_TICK);
                }
            } else if (vehicleData.getSpeed() > instruction.getSpeed()) {
                // The vehicle has to slow down
                vehicleData.setSpeed(vehicleData.getSpeed() - SPEED_INCREASE_PER_TICK);

            }
            // Adjust the coordinates to go forward
            double distance = (vehicleData.getSpeed() / 3.6) * (tickMill / 1000.0);
            vehicleData.getCoordinates().changeCoordinatesByDistance(distance, Direction.Forward);


            // Adjust the lane
            int laneToGo = instruction.getLane();
            double laneCoordinates = FIRST_LANE_LAT;
            if (laneToGo == 2) {
                laneCoordinates = SECOND_LANE_LAT;
            } else if (laneToGo == 3) {
                laneCoordinates = THIRD_LANE_LAT;
            }

            // + 0.000001 to prevent the car from going mad left-right-left-right-... when staying on one lane
            if (vehicleData.getCoordinates().getLatitude() > laneCoordinates + 0.000001) {
                // The vehicle has to change one lane to the left.
                vehicleData.getCoordinates().changeCoordinatesByDistance(MOVEMENT_SIDEWAYS_PER_TICK, Direction.Left);

            } else if (vehicleData.getCoordinates().getLatitude() < laneCoordinates - 0.000001) {
                // The vehicle has to change one lane to the right.
                vehicleData.getCoordinates().changeCoordinatesByDistance(MOVEMENT_SIDEWAYS_PER_TICK, Direction.Right);
            }
            // Adjust the lane number
            double diffLane1 = Math.abs(vehicleData.getCoordinates().getLatitude() - FIRST_LANE_LAT);
            double diffLane2 = Math.abs(vehicleData.getCoordinates().getLatitude() - SECOND_LANE_LAT);
            double diffLane3 = Math.abs(vehicleData.getCoordinates().getLatitude() - THIRD_LANE_LAT);

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
        }
    };

    public void registerVehicle() {
        RegisterCar nonAutonomousCar = new RegisterCar("Tesla", "Model Y", this.vin, false);
        InventoryClient inventoryClient = new InventoryClient();
        inventoryClient.registerCar(nonAutonomousCar);
    }

    public String getVin() {
        return vin;
    }
}
