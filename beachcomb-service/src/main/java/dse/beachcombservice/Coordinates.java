package dse.beachcombservice;

public class Coordinates {
    private double latitude;
    private double longitude;

    public Coordinates() {
    }

    public Coordinates(double longitude, double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
