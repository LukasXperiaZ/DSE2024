package dse.datafeeder.simulation;

/*
 * This class represents an instruction for a car.
 */
public class Instruction {
    private double speed;
    private int lane;
    private int ticks;      // For how long (how many ticks) the instruction is valid.

    public Instruction(double speed, int lane, int ticks) {
        this.speed = speed;
        this.lane = lane;
        this.ticks = ticks;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public Instruction deepCopy() {
        return new Instruction(speed, lane, ticks);
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "speed=" + speed +
                ", lane=" + lane +
                ", ticks=" + ticks +
                '}';
    }
}
