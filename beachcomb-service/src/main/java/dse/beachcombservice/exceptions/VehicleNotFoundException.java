package dse.beachcombservice.exceptions;

// Custom Exception if a vehicle is not found
public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String vin) {
        super("Vehicle not found for vin: " + vin);
    }
}
