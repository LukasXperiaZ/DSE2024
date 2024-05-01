package dse.datafeeder.dto;

import dse.datafeeder.exception.ValidationException;

import java.sql.Timestamp;

public class VehicleData {

    private String vin;     // len=17
    private Coordinates coordinates;
    private double speed;   // [0.0, 1000.0]
    private int lane;       // [1, 3]
    private Timestamp timestamp;

    public VehicleData(String vin, Coordinates coordinates, double speed, int lane, Timestamp timestamp) {
        if (!checkVin(vin)) {
            throw new ValidationException("VIN '" + vin + "' does not conform to its format!");
        }
        if (!checkSpeed(speed)) {
            throw new ValidationException("Speed '" + speed + "' is either too low or too high!");
        }
        if (!checkLane(lane)) {
            throw new ValidationException("Lane '" + lane + "' has to be between 1 and 3!");
        }

        this.vin = vin;
        this.coordinates = coordinates;
        this.speed = speed;
        this.lane = lane;
        this.timestamp = timestamp;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        if (!checkVin(vin)) {
            throw new ValidationException("VIN '" + vin + "' does not conform to its format!");
        }
        this.vin = vin;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        if (!checkSpeed(speed)) {
            throw new ValidationException("Speed '" + speed + "' is either too low or too high!");
        }
        this.speed = speed;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        if (!checkLane(lane)) {
            throw new ValidationException("Lane '" + lane + "' has to be between 1 and 3!");
        }
        this.lane = lane;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    private boolean checkVin(String vin) {
        return vin != null && vin.length() == 17 && vin.matches("^(?=.*[0-9])(?=.*[A-z])[0-9A-z-]{17}$");
    }

    protected boolean checkSpeed(double speed) {
        return speed >= 0.0 && speed <= 1000.0;
    }

    protected boolean checkLane(int lane) {
        return lane >= 1 && lane <= 3;
    }
}
