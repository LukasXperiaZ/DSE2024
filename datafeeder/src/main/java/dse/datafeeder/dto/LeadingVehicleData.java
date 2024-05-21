package dse.datafeeder.dto;

import dse.datafeeder.exception.ValidationException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

/*
 * This class represents the data a leading vehicle sends to the backend.
 * It also represents the state of the simulated leading vehicle.
 */
public class LeadingVehicleData extends VehicleData {

    @NotBlank(message = "Target speed is mandatory")
    private double targetSpeed;     // [0.0, 1000.0]

    @NotBlank(message = "target lane is mandatory")
    @Min(value = 1, message = "target lane must be greater or equal than 1")
    @Max(value = 3, message = "target lane must be smaller or equal than 3")
    private int targetLane;         // [1, 3]


    public LeadingVehicleData(String vin, Coordinates coordinates, double speed, int lane, Timestamp timestamp, double targetSpeed, int targetLane) {
        super(vin, coordinates, speed, lane, timestamp);

        this.targetSpeed = targetSpeed;
        this.targetLane = targetLane;
    }

    public LeadingVehicleData(VehicleData vehicleData, double targetSpeed, int targetLane) {
        super(vehicleData.getVin(), vehicleData.getCoordinates(), vehicleData.getSpeed(), vehicleData.getLane(), vehicleData.getTimestamp());

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
