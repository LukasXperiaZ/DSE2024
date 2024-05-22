package dse.beachcombservice.exceptions;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String vin) {
        super("Vehicle not found for vin: " + vin);
    }
}
