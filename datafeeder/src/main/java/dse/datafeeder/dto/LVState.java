package dse.datafeeder.dto;

/*
 * This class represents the state of a Leading Vehicle.
 */
public class LVState {
    private boolean isLeadingVehicle;
    private String vinFV;

    public boolean getIsLeadingVehicle() {
        return isLeadingVehicle;
    }

    public void setIsLeadingVehicle(boolean leadingVehicle) {
        isLeadingVehicle = leadingVehicle;
    }

    public String getVinFV() {
        return vinFV;
    }

    public void setVinFV(String vinFV) {
        this.vinFV = vinFV;
    }

    @Override
    public String toString() {
        return "LVState{" +
                "isLeadingVehicle=" + isLeadingVehicle +
                ", vin='" + vinFV + '\'' +
                '}';
    }
}
