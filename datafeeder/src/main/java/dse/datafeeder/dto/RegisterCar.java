package dse.datafeeder.dto;

/*
 * This class represents the information that is necessary to register a car in the inventory service.
 */
public class RegisterCar {
    private String oem;
    private String model;
    private String vin;
    private boolean is_self_driving;

    public RegisterCar(String oem, String model, String vin, boolean is_self_driving) {
        this.oem = oem;
        this.model = model;
        this.vin = vin;
        this.is_self_driving = is_self_driving;
    }

    public String getOem() {
        return oem;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public boolean getIs_self_driving() {
        return is_self_driving;
    }

    public void setIs_self_driving(boolean is_self_driving) {
        this.is_self_driving = is_self_driving;
    }

    @Override
    public String toString() {
        return "RegisterCar{" +
                "oem='" + oem + '\'' +
                ", model='" + model + '\'' +
                ", vin='" + vin + '\'' +
                ", is_self_driving=" + is_self_driving +
                '}';
    }
}
