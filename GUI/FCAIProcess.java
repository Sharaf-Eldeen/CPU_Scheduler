public class FCAIProcess extends Process {
    public int fcaiFactor;
    public Boolean canBePreempted;

    public FCAIProcess(String name, String color, int arrivalTime, int burstTime, int priority, int timeQuantum, int contextSwitching) {
        super(name, color, arrivalTime, burstTime, priority, timeQuantum, contextSwitching);
        this.fcaiFactor = 0;
        this.canBePreempted = false;
    }

    public void calculateFcaiFactor(double v1, double v2) {
        this.fcaiFactor = (int) Math.ceil((10 - this.priority) + (this.arrivalTime / v1) + (this.burstTime / v2));
    }

    public void setTimeQuantum(int amount) {
        this.timeQuantum += amount;
    }
}
