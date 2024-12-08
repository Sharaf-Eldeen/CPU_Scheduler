import java.util.*;

public class SJFScheduler implements CPUSchedulersTechniques {
    @Override
    public Object[][] run(List<Process> processes) {
        List<Process> finishedProcesses = new ArrayList<>();

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;

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

            // Sort ready queue by burst time, then by priority
            readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));

            // Select the next process
            Process nextProcess = readyQueue.get(0);

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
        }

        // Prepare results in Object[][] format
        Object[][] result = new Object[finishedProcesses.size()][8];
        for (int i = 0; i < finishedProcesses.size(); i++) {
            Process p = finishedProcesses.get(i);
            result[i][0] = p.name;  
            result[i][1] = p.startTime;
            result[i][2] = p.completionTime;       // Completion Time    
            result[i][3] = p.color;    
            result[i][4] = p.waitingTime;       // Completion Time
            result[i][5] = p.turnaroundTime;       // Turnaround Time
        }

        return result;
    }

//     public static void main(String[] args) {
//         SJFScheduler scheduler = new SJFScheduler();

//         // Create processes
//         List<Process> processes = new ArrayList<>();
//         processes.add(new Process("P1", "Red", 0, 7, 1, 0, 0));
//         processes.add(new Process("P2", "Green", 0, 4, 2, 0, 0));
      
//         // Run the scheduler
//         Object[][] results = scheduler.run(processes);

//         // Print results
//      //   System.out.println("Name\tColor\tArrival\tStart\tCompletion\tTurnaround\tWaiting\tPriority");
//       //  for (Object[] row : results) {
//    //         System.out.printf("%s\t%s\t%d\t%d\t%d\t%d\t%d\t%d\n",
//  //                   row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]);
//  //       }
//  System.out.println(results);
 
//     }
}
