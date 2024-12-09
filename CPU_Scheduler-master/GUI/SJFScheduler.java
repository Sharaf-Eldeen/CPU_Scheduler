import java.util.*;

public class SJFScheduler implements CPUSchedulersTechniques {

    @Override
    public Object[][] run(List<Process> processes) {
        List<Process> finishedProcesses = new ArrayList<>();

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        Process previousProcess = null; // Track the previously executed process

        while (!processes.isEmpty()) {
            // Filter processes that have arrived
            List<Process> readyQueue = new ArrayList<>();
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime) {
                    readyQueue.add(p);
                }
            }

            // If no process is ready, advance the time
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Sort ready queue by burst time
            readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));

            // Select the next process
            Process nextProcess = readyQueue.get(0);

            // Handle context switching if the process has changed
            if (previousProcess != null && nextProcess != previousProcess) {
                currentTime += previousProcess.contextSwitching; // Add context switch time
            }

            // Calculate start time, completion time, and other metrics
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
        Object[][] result = new Object[finishedProcesses.size()][8];
        for (int i = 0; i < finishedProcesses.size(); i++) {
            Process p = finishedProcesses.get(i);
            result[i][0] = p.name;  // Process name
            result[i][1] = p.startTime; // Start time
            result[i][2] = p.completionTime; // Completion time
            result[i][3] = p.color; // Process color
            result[i][4] = p.waitingTime; // Waiting time
            result[i][5] = p.turnaroundTime; // Turnaround time
        }

        return result;
    }
}
