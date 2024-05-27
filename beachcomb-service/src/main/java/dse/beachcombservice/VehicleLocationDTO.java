package dse.beachcombservice;

public class VehicleLocationDTO {
    String vin;
    private Coordinates coordinates;
    private Double speed;
    private Double targetSpeed;
    private Integer lane;
    private Integer targetLane;

    public VehicleLocationDTO() {
    }

    public VehicleLocationDTO(String vin, Coordinates coordinates, Double speed) {
        this.vin = vin;
        this.coordinates = coordinates;
        this.speed = speed;
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

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getTargetSpeed() {
        return targetSpeed;
    }

    public void setTargetSpeed(Double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public Integer getLane() {
        return lane;
    }

    public void setLane(Integer lane) {
        this.lane = lane;
    }

    public Integer getTargetLane() {
        return targetLane;
    }

    public void setTargetLane(Integer targetLane) {
        this.targetLane = targetLane;
    }
}
