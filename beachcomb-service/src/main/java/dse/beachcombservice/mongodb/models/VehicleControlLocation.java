package dse.beachcombservice.mongodb.models;

import java.util.Date;
import java.util.List;

public class VehicleControlLocation implements IVehicleModel {

    private final String vin;
    private final List<Double> location;
    private final Date timestamp;
    private final Double speed;
    private final Double targetSpeed;
    private final Integer lane;
    private final Integer targetLane;

    public VehicleControlLocation(String vin, List<Double> location, Date timestamp, Double speed, Double targetSpeed, Integer lane, Integer targetLane) {
        this.vin = vin;
        this.location = location;
        this.timestamp = timestamp;
        this.speed = speed;
        this.targetSpeed = targetSpeed;
        this.lane = lane;
        this.targetLane = targetLane;
    }

    @Override
    public String getVin() {
        return vin;
    }

    @Override
    public List<Double> getLocation() {
        return location;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    public Double getSpeed() {
        return speed;
    }

    public Double getTargetSpeed() {
        return targetSpeed;
    }

    public Integer getLane() {
        return lane;
    }

    public Integer getTargetLane() {
        return targetLane;
    }
}
