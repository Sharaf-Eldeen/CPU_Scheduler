import java.util.*;

public class PriorityScheduler implements CPUSchedulersTechniques {

    @Override
    public Object[][] run(List<Process> processes) {
        List<Process> finishedProcesses = new ArrayList<>();

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        Process previousProcess = null; // Track the previously executed process

        while (!processes.isEmpty()) {
            // Get all processes that have arrived by the current time
            List<Process> availableProcesses = new ArrayList<>();
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime) {
                    availableProcesses.add(p);
                }
            }

            if (availableProcesses.isEmpty()) {
                currentTime++;
                continue;
            }

            // Sort available processes first by priority, then by arrival time
            availableProcesses.sort(Comparator
                    .comparingInt((Process p) -> p.priority)  // Sort by priority first
                    .thenComparingInt(p -> p.arrivalTime));   // Then by arrival time

            // Pick the process with the highest priority (lowest priority number)
            Process nextProcess = availableProcesses.get(0);

            // Handle context switching if the process has changed
            if (previousProcess != null && nextProcess != previousProcess) {
                currentTime += previousProcess.contextSwitching; // Add context switch time
            }

            // Calculate times for the selected process
            if (nextProcess.startTime == 0) { // Set start time if not already set
                nextProcess.startTime = currentTime;
            }
            nextProcess.completionTime = currentTime + nextProcess.burstTime;
            nextProcess.turnaroundTime = nextProcess.completionTime - nextProcess.arrivalTime;
            nextProcess.waitingTime = nextProcess.turnaroundTime - nextProcess.burstTime;

            // Update time and move process to finished
            currentTime += nextProcess.burstTime;
            finishedProcesses.add(nextProcess);
            processes.remove(nextProcess);

            // Update the previous process
            previousProcess = nextProcess;
        }

        // Prepare results in Object[][] format
        Object[][] result = new Object[finishedProcesses.size()][6];
        for (int i = 0; i < finishedProcesses.size(); i++) {
            Process p = finishedProcesses.get(i);
            result[i][0] = p.name;             // Process Name
            result[i][1] = p.startTime;        // Start Time
            result[i][2] = p.completionTime;   // Completion Time
            result[i][3] = p.color;            // Color
            result[i][4] = p.waitingTime;      // Waiting Time
            result[i][5] = p.turnaroundTime;   // Turnaround Time
        }

        return result;
    }
}
