public class Process {
    public String name;
    public String color;
    public int arrivalTime;
    public int burstTime;
    public int priority;
    public int timeQuantum;
    public int contextSwitching;
    public int completionTime; 
    public int turnaroundTime; 
    public int waitingTime; 
    public int waitTimeForBoost; 
    public int remainingBurstTime;
    public int startTime;


    public Process(String name, String color, int arrivalTime, int burstTime, int priority, int timeQuantum, int contextSwitching) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.timeQuantum = timeQuantum;
        this.contextSwitching = contextSwitching;
    }
}
