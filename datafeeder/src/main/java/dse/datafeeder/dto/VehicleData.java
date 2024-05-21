package dse.datafeeder.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;


/*
 * This class represents the data a vehicle sends to the backend.
 * It also represents the state of the simulated vehicle.
 */
public class VehicleData {

    @NotBlank(message = "VIN is mandatory")
    private String vin;     // len=17

    @NotBlank(message = "coordinates are mandatory")
    private Coordinates coordinates;

    @NotBlank(message = "speed is mandatory")
    private double speed;   // [0.0, 1000.0], in km/h

    @NotBlank(message = "lane is mandatory")
    @Min(value = 1, message = "lane must be greater or equal than 1")
    @Max(value = 3, message = "lane must be smaller or equal than 3")
    private int lane;       // [1, 3]

    @NotBlank
    private Timestamp timestamp;

    public VehicleData(String vin, Coordinates coordinates, double speed, int lane, Timestamp timestamp) {
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
        this.speed = speed;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public VehicleData deepCopy() {
        return new VehicleData(this.getVin(), new Coordinates(this.getCoordinates().getLongitude(),
                this.getCoordinates().getLatitude()), this.getSpeed(), this.getLane(),
                new Timestamp(this.getTimestamp().getTime()));
    }

    @Override
    public String toString() {
        return "VehicleData{" +
                "vin='" + vin + '\'' +
                ", coordinates=" + coordinates +
                ", speed=" + speed +
                ", lane=" + lane +
                ", timestamp=" + timestamp +
                '}';
    }
}
