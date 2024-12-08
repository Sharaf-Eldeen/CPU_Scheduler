import java.util.*;

 class PrioritySchedule {
    private List<Process> Processes;

    public PrioritySchedule() {
        Processes = new ArrayList<>();
    }

    public void AddProcess(Process p) {
        Processes.add(p);
    }

    public List<Process> priorityschedule() {
        List<Process> FinishedProcesses = new ArrayList<>();
        Processes.sort(Comparator.comparingInt(Process::GetArrivalTime)); // Initial sort by arrival time
        int currentTime = 0;

        while (!Processes.isEmpty()) {
            // Get all processes that have arrived by the current time
            List<Process> availableProcesses = new ArrayList<>();
            for (Process p : Processes) {
                if (p.GetArrivalTime() <= currentTime) {
                    availableProcesses.add(p);
                }
            }

            if (availableProcesses.isEmpty()) {
                currentTime++;
                continue;
            }

            // Sort available processes by priority and then by arrival time
            availableProcesses.sort(
                Comparator.comparingInt(Process::GetPriority)
                          .thenComparingInt(Process::GetArrivalTime)
            );

            // Pick the process with the highest priority (lowest priority number)
            Process nextProcess = availableProcesses.get(0);
            nextProcess.calculateTimes(currentTime); // Calculate times for the selected process
            Processes.remove(nextProcess);           // Remove from pending processes
            currentTime += nextProcess.GetBurstTime(); // Update current time
            FinishedProcesses.add(nextProcess);      // Add to finished processes
        }

        return FinishedProcesses;
    }

    public static void main(String[] args) {
        PrioritySchedule scheduler = new PrioritySchedule();

        // Add processes
        scheduler.AddProcess(new Process(1, 0, 7, 2)); // PID, ArrivalTime, BurstTime, Priority
        scheduler.AddProcess(new Process(2, 2, 4, 1));
        scheduler.AddProcess(new Process(3, 4, 1, 3));
        scheduler.AddProcess(new Process(4, 5, 4, 2));

        // Perform priority scheduling
        List<Process> completed = scheduler.priorityschedule();

        // Print results
        System.out.println("PID\tArrivalTime\tBurstTime\tBeginningTime\tCompletionTime\tTurnaroundTime\tWaitingTime");
        for (Process p : completed) {
            System.out.printf("%d\t       %d\t       %d\t        %d\t        %d\t         %d\t        %d\n",
                    p.GetId(), p.GetArrivalTime(), p.GetBurstTime(), p.GetBeginningTime(),
                    p.GetCompletionTime(), p.GetTurnaroundTime(), p.GetWaitingTime());
        }
    }
}
