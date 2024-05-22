package dse.beachcombservice;

public class VehicleLocationDTO {
    String vin;
    private Coordinates coordinates;
    private double speed;

    public VehicleLocationDTO(String vin, Coordinates coordinates, double speed) {
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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
