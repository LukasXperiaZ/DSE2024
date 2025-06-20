package dse.beachcombservice.mongodb.models;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public final class VehicleLocation implements IVehicleModel {
    private final String vin;
    private final Date timestamp;
    private final List<Double> location;

    private final Double speed;

    public VehicleLocation(String vin, Date timestamp, List<Double> location, Double speed) {
        this.vin = vin;
        this.timestamp = timestamp;
        this.location = location;
        this.speed = speed;
    }

    public String getVin() {
        return vin;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<Double> getLocation() {
        return location;
    }

    public Double getSpeed() {
        return speed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (VehicleLocation) obj;
        return Objects.equals(this.vin, that.vin) &&
                Objects.equals(this.timestamp, that.timestamp) &&
                Objects.equals(this.location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin, timestamp, location);
    }

    @Override
    public String toString() {
        return "VehicleLocation[" +
                "vin=" + vin + ", " +
                "timestamp=" + timestamp + ", " +
                "location=" + location + ']';
    }
}
