import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;

public class SRTFScheduler implements CPUSchedulersTechniques {

    @Override
    public Object[][] run(List<Process> processes) {
        int n = processes.size();
        int currentTime = 0;

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                if (p1.priority != p2.priority) {
                    return Integer.compare(p1.priority, p2.priority);
                }
                return Integer.compare(p1.remainingBurstTime, p2.remainingBurstTime); 
            }
        });

        for (Process p : processes) {
            p.waitingTime = 0;
            p.turnaroundTime = 0;
            p.completionTime = 0;
            p.remainingBurstTime = p.burstTime;
            p.waitTimeForBoost = 0;  
        }

        int completedProcesses = 0;
        while (completedProcesses < n) {
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingBurstTime > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process currentProcess = readyQueue.poll();
            
            currentProcess.remainingBurstTime--;

            if (currentProcess.remainingBurstTime == 0) {
                currentProcess.completionTime = currentTime + 1;
                completedProcesses++;
            }

            for (Process p : processes) {
                if (p != currentProcess && p.arrivalTime <= currentTime && p.remainingBurstTime > 0) {
                    p.waitingTime++;
                    p.waitTimeForBoost++; 
                }
            }

            for (Process p : processes) {
                if (p.waitTimeForBoost > 5) { 
                    p.priority = Math.max(1, p.priority - 1); 
                    p.waitTimeForBoost = 0;  
                }
            }

            currentTime++;
        }

        for (Process p : processes) {
            p.turnaroundTime = p.completionTime - p.arrivalTime;
        }

        Object[][] result = new Object[n][6];
        for (int i = 0; i < n; i++) {
            Process p = processes.get(i);
            result[i][0] = p.name;
            result[i][1] = p.arrivalTime;
            result[i][2] = p.burstTime;
            result[i][3] = p.completionTime;
            result[i][4] = p.turnaroundTime;
            result[i][5] = p.waitingTime;
        }

        return result;
    }
}
