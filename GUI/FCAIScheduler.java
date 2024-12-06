import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class FCAIScheduler implements CPUSchedulersTechniques {

    private void calculateFcaiFactorForAllProcesses(List<FCAIProcess> processes) {
        int lastArrival = 0;
        int maxBurstTime = 0;

        for (FCAIProcess process : processes) {
            if (process.arrivalTime > lastArrival) {
                lastArrival = process.arrivalTime;
            }
            if (process.burstTime > maxBurstTime) {
                maxBurstTime = process.burstTime;
            }
        }

        double v1 = lastArrival / 10.0;
        double v2 = maxBurstTime / 10.0;
        for (FCAIProcess process : processes) {
            process.calculateFcaiFactor(v1, v2);
        }
    }

    private void schedule(List<FCAIProcess> processes) {

        List<Object[]> logs = new ArrayList<>();

        PriorityQueue<FCAIProcess> Queue = new PriorityQueue<>(Comparator.comparingInt((FCAIProcess p) -> p.arrivalTime ).thenComparingInt((FCAIProcess p) -> p.fcaiFactor));
        // PriorityQueue<FCAIProcess> fcaiFactorQueue = new PriorityQueue<>(Comparator.comparingInt((FCAIProcess p) -> p.fcaiFactor));
        Queue.addAll(processes);
        // fcaiFactorQueue.addAll(processes);

        int currantTime = 0;
        while (!Queue.isEmpty()) {
            FCAIProcess turnProcess = Queue.poll();

            if(turnProcess.arrivalTime > currantTime) {
                currantTime = turnProcess.arrivalTime;
            }

            int firstFortyPercent = (int) Math.ceil(.4 * turnProcess.timeQuantum); 
            int actualTimeNonPreemptive = Math.min(firstFortyPercent, turnProcess.burstTime);
            int actualTurnProcessRemainingTime = turnProcess.burstTime - actualTimeNonPreemptive;
            currantTime += actualTimeNonPreemptive;

            logs.add(new Object[]{turnProcess.name,
                currantTime - actualTimeNonPreemptive,
                currantTime,
                turnProcess.color});

            if(actualTurnProcessRemainingTime > 0) {
                turnProcess.canBePreempted = true;
                turnProcess.burstTime = actualTurnProcessRemainingTime;
                turnProcess.arrivalTime = currantTime;
                Queue.add(turnProcess);
                FCAIProcess next = Queue.peek();
                if(turnProcess.name != next.name) {
                    turnProcess.setTimeQuantum(actualTurnProcessRemainingTime);

                } else {

                }
            }
        }



    }

    @Override
    public Object[][] run(List<Process> processes) {

        List<FCAIProcess> fcaiProcesses = processes.stream()
            .filter(FCAIProcess.class::isInstance)
            .map(FCAIProcess.class::cast)
            .toList();

        calculateFcaiFactorForAllProcesses(fcaiProcesses);

        schedule(fcaiProcesses);

        

        // Placeholder: Add scheduling logic here
        return new Object[0][0];
    }

}
