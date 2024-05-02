package dse.datafeeder.dto;

import static dse.datafeeder.constants.Constants.*;
import dse.datafeeder.constants.Direction;

/*
 * This class represents GPS coordinates of a car.
 */
public class Coordinates {
    private double longitude;       // Increases if we drive straight forwards
    private double latitude;        // Decreases if we drive left, increases if we drive right

    public Coordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void changeCoordinatesByDistance(double distance, Direction direction) {
        // Taken from here: https://stackoverflow.com/questions/19803604/increment-meters-to-latitude-and-longitude

        double bearing = 90.0;
        if (direction == Direction.Left) {
            bearing = 180.0;
        } else if (direction == Direction.Right) {
            bearing = 0.0;
        }

        Coordinates gp = null;

        double R = 6371000; // meters , earth Radius approx
        double PI = 3.1415926535;
        double RADIANS = PI / 180;
        double DEGREES = 180 / PI;

        double lat2;
        double lon2;

        double lat1 = this.getLatitude() * RADIANS;
        double lon1 = this.getLongitude() * RADIANS;
        double radbear = bearing * RADIANS;

        // System.out.println("lat1="+lat1 + ",lon1="+lon1);

        lat2 = Math.asin(Math.sin(lat1) * Math.cos(distance / R) +
                Math.cos(lat1) * Math.sin(distance / R) * Math.cos(radbear));
        lon2 = lon1 + Math.atan2(Math.sin(radbear) * Math.sin(distance / R) * Math.cos(lat1),
                Math.cos(distance / R) - Math.sin(lat1) * Math.sin(lat2));

        // System.out.println("lat2="+lat2*DEGREES + ",lon2="+lon2*DEGREES);

        this.setLongitude(lon2 * DEGREES);
        this.setLatitude(lat2 * DEGREES);
    }

    public double calculateDistanceTo(Coordinates coordinates) {
        double lat1Rad = Math.toRadians(this.getLatitude());
        double lat2Rad = Math.toRadians(coordinates.getLatitude());
        double lon1Rad = Math.toRadians(this.getLongitude());
        double lon2Rad = Math.toRadians(coordinates.getLongitude());

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * EARTH_RADIUS;

        return distance;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
