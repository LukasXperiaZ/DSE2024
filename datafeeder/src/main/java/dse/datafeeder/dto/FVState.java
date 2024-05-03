package dse.datafeeder.dto;

public class FVState {
    private boolean usesFM;
    private String vinLV;
    private double targetSpeed;
    private int targetLane;         // [1, 3]

    public boolean getUsesFM() {
        return usesFM;
    }

    public void setUsesFM(boolean usesFM) {
        this.usesFM = usesFM;
    }

    public String getVinLV() {
        return vinLV;
    }

    public void setVinLV(String vinLV) {
        this.vinLV = vinLV;
    }

    public double getTargetSpeed() {
        return targetSpeed;
    }

    public void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public int getTargetLane() {
        return targetLane;
    }

    public void setTargetLane(int targetLane) {
        this.targetLane = targetLane;
    }

    @Override
    public String toString() {
        return "FVState{" +
                "usesFM=" + usesFM +
                ", vinLV='" + vinLV + '\'' +
                ", targetSpeed=" + targetSpeed +
                ", targetLane=" + targetLane +
                '}';
    }
}
