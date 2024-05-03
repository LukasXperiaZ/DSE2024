package dse.datafeeder.dto;

import dse.datafeeder.exception.ValidationException;

import java.sql.Timestamp;

/*
 * This class represents the data a leading vehicle sends to the backend.
 * It also represents the state of the simulated leading vehicle.
 */
public class LeadingVehicleData extends VehicleData {

    private double targetSpeed;     // [0.0, 1000.0]
    private int targetLane;         // [1, 3]


    public LeadingVehicleData(String vin, Coordinates coordinates, double speed, int lane, Timestamp timestamp, double targetSpeed, int targetLane) {
        super(vin, coordinates, speed, lane, timestamp);

        if (!this.checkSpeed(targetSpeed)) {
            throw new ValidationException("Speed '" + targetSpeed + "' is either too low or too high!");
        }
        if (!this.checkLane(targetLane)) {
            throw new ValidationException("Lane '" + targetLane + "' has to be between 1 and 3!");
        }

        this.targetSpeed = targetSpeed;
        this.targetLane = targetLane;
    }

    public LeadingVehicleData(VehicleData vehicleData, double targetSpeed, int targetLane) {
        super(vehicleData.getVin(), vehicleData.getCoordinates(), vehicleData.getSpeed(), vehicleData.getLane(), vehicleData.getTimestamp());

        if (!this.checkSpeed(targetSpeed)) {
            throw new ValidationException("Speed '" + targetSpeed + "' is either too low or too high!");
        }
        if (!this.checkLane(targetLane)) {
            throw new ValidationException("Lane '" + targetLane + "' has to be between 1 and 3!");
        }

        this.targetSpeed = targetSpeed;
        this.targetLane = targetLane;
    }

    public double getTargetSpeed() {
        return targetSpeed;
    }

    public void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public int getTargetLane() {
        return targetLane;
    }

    public void setTargetLane(int targetLane) {
        this.targetLane = targetLane;
    }

    public LeadingVehicleData deepCopy() {
        return new LeadingVehicleData(this.getVin(), new Coordinates(this.getCoordinates().getLongitude(),
                this.getCoordinates().getLatitude()), this.getSpeed(), this.getLane(),
                new Timestamp(this.getTimestamp().getTime()), this.getTargetSpeed(), this.getTargetLane());
    }

    @Override
    public String toString() {
        return "LeadingVehicleData{" +
                "targetSpeed=" + targetSpeed +
                ", targetLane=" + targetLane +
                "} " + super.toString();
    }
}
