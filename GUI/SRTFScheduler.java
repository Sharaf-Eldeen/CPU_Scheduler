import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;

public class SRTFScheduler implements CPUSchedulersTechniques {

    @Override
    public Object[][] run(List<Process> processes) {
        int n = processes.size();
        int currentTime = 0;

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        // Priority queue for SRTF
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                if (p1.remainingBurstTime != p2.remainingBurstTime) {
                    return Integer.compare(p1.remainingBurstTime, p2.remainingBurstTime);
                }
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
        });

        // Initialize process properties
        for (Process p : processes) {
            p.remainingBurstTime = p.burstTime;
            p.startTime = -1; // Not started yet
        }

        // For tracking results directly
        Object[][] result = new Object[n][8];

        int completedProcesses = 0;

        while (completedProcesses < n) {
            // Add processes to the ready queue that have arrived
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingBurstTime > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Select the process with the shortest remaining time
            Process currentProcess = readyQueue.poll();

            // Mark the start time if not already started
            if (currentProcess.startTime == -1) {
                currentProcess.startTime = currentTime;
            }

            // Execute the process for one unit of time
            currentProcess.remainingBurstTime--;

            // If the process finishes execution
            if (currentProcess.remainingBurstTime == 0) {
                currentProcess.completionTime = currentTime + 1; // Completion time is 1-based
                completedProcesses++;

                // Calculate metrics
                int turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                int waitingTime = turnaroundTime - currentProcess.burstTime;

                // Record result for this process
                int index = processes.indexOf(currentProcess);
                result[index][0] = currentProcess.name;
                result[index][1] = currentProcess.startTime;
                result[index][2] = currentProcess.completionTime;
                result[index][3] = currentProcess.color;
                result[index][4] = waitingTime;
                result[index][5] = turnaroundTime;

                // Store WT and TAT in the process for averaging later
                currentProcess.waitingTime = waitingTime;
                currentProcess.turnaroundTime = turnaroundTime;
            }

            // Increment the current time
            currentTime++;
        }

        // Calculate averages
        double totalWT = 0, totalTAT = 0;
        for (Process p : processes) {
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
        }

        double avgWT = totalWT / n;
        double avgTAT = totalTAT / n;

        // Add averages to the result
        result[n - 1][6] = avgWT;
        result[n - 1][7] = avgTAT;

        return result;
    }
}
