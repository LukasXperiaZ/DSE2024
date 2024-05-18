package dse.beachcombservice;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class VehicleDTO {
    @NotBlank(message = "VIN is mandatory")
    private String vin;

    @NotBlank(message = "Location is mandatory")
    private List<Double> location;

    @NotBlank(message = "Speed is mandatory")
    private Float speed;

    @NotBlank(message = "Lane is mandatory")
    @Min(value = 1, message = "Lane must be greater than 0")
    @Max(value = 3, message = "Lane must be less than 4")
    private Integer lane;

    @NotBlank
    private Date timestamp;


    private Double targetSpeed;

    @Min(value = 1, message = "TargetLane must be greater than 0")
    @Max(value = 3, message = "TargetLane must be less than 4")
    private Integer targetLane;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Integer getLane() {
        return lane;
    }

    public void setLane(Integer lane) {
        this.lane = lane;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

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

    @Override
    public String toString() {
        return "VehicleDTO{" +
                "vin='" + vin + '\'' +
                ", location=" + location +
                ", speed=" + speed +
                ", lane=" + lane +
                ", timestamp=" + timestamp +
                ", targetSpeed=" + targetSpeed +
                ", targetLane=" + targetLane +
                '}';
    }
}
