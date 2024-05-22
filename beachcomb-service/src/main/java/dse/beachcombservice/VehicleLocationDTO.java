package dse.beachcombservice;

public class VehicleLocationDTO {
    String vin;
    private Coordinates location;

    public VehicleLocationDTO(String vin, Coordinates location) {
        this.vin = vin;
        this.location = location;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Coordinates getLocation() {
        return location;
    }

    public void setLocation(Coordinates location) {
        this.location = location;
    }
}
