package dse.beachcombservice.mongodb.models;

public class LeadingVehicleModel extends VehicleModel{
    private Double targetSpeed;
    private Integer targetLane;

    public Double getTargetSpeed() {
        return targetSpeed;
    }

    public void setTargetSpeed(Double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public Integer getTargetLane() {
        return targetLane;
    }

    public void setTargetLane(Integer targetLane) {
        this.targetLane = targetLane;
    }
}
