package dse.beachcombservice;

public class VehicleLocationDTO {
    String vin;
    private Coordinates coordinates;

    public VehicleLocationDTO(String vin, Coordinates coordinates) {
        this.vin = vin;
        this.coordinates = coordinates;
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
}
