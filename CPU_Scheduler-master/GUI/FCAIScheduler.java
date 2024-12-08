import java.util.*;

class Processfaci {
    int pid;
    int priority;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int quantum;
    double fcaiFactor;
    List<String> executionPeriods;

    public Processfaci(int pid, int priority, int arrivalTime, int burstTime, int quantum) {
        this.pid = pid;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.quantum = quantum;
        this.executionPeriods = new ArrayList<>();
    }

    public void calculateFCAIFactor(double V1, double V2) {
        this.fcaiFactor = (10 - this.priority)
                        + (this.arrivalTime / V1)
                        + (this.remainingTime / V2);
    }
}

 class fcaiScheduler {

    public static void fcaiScheduler(List<Processfaci> processList) {
        int time = 0;
        List<Processfaci> readyQueue = new ArrayList<>();
        List<Processfaci> completedProcesses = new ArrayList<>();

        int lastArrivalTime = processList.stream().mapToInt(p -> p.arrivalTime).max().orElse(1);
        int maxBurstTime = processList.stream().mapToInt(p -> p.burstTime).max().orElse(1);
        double V1 = lastArrivalTime / 10.0;
        double V2 = maxBurstTime / 10.0;

        for (Processfaci process : readyQueue) {
            process.calculateFCAIFactor(V1, V2);
        }







        while (!processList.isEmpty() || !readyQueue.isEmpty()) {
            Iterator<Processfaci> it = processList.iterator();
            while (it.hasNext()) {
                Processfaci process = it.next();
                if (process.arrivalTime <= time) {
                    System.out.println(time);
                    System.out.println(process.pid);
                    System.out.println("----------------------------------------");
                    readyQueue.add(process);
                    it.remove();
                }
            }


            readyQueue.sort(Comparator.comparingDouble(p -> p.fcaiFactor));

            if (!readyQueue.isEmpty()) {
                Processfaci currentProcess = readyQueue.remove(0);
                int initialQuantum = currentProcess.quantum;
                int nonPreemptiveTime = (int) Math.ceil(initialQuantum * 0.4);
                int executionTime;

                if (currentProcess.remainingTime > nonPreemptiveTime) {
                    executionTime = nonPreemptiveTime;
                    currentProcess.executionPeriods.add("[" + time + "-" + (time + executionTime) + "]");
                    time += executionTime;
                    currentProcess.remainingTime -= executionTime;

                    if (currentProcess.remainingTime > 0) {
                        executionTime = Math.min(currentProcess.remainingTime, currentProcess.quantum - nonPreemptiveTime);
                        currentProcess.executionPeriods.add("[" + time + "-" + (time + executionTime) + "]");
                        time += executionTime;
                        currentProcess.remainingTime -= executionTime;
                    }
                } else {
                    executionTime = Math.min(currentProcess.remainingTime, currentProcess.quantum);
                    currentProcess.executionPeriods.add("[" + time + "-" + (time + executionTime) + "]");
                    time += executionTime;
                    currentProcess.remainingTime -= executionTime;
                }

                if (currentProcess.remainingTime > 0) {
                    if (executionTime == initialQuantum) {
                        currentProcess.quantum += 2; 
                    } else {
                        currentProcess.quantum += initialQuantum - executionTime; 
                    }
                    readyQueue.add(currentProcess); 
                } else {
                    completedProcesses.add(currentProcess); 
                }
            } else {
                time++;
            }
        }

        System.out.println("Execution Order:");
        for (Processfaci process : completedProcesses) {
            System.out.println("Process " + process.pid + ": " + process.executionPeriods);
        }
    }

    public static void main(String[] args) {
        List<Processfaci> processes = new ArrayList<>(Arrays.asList(
            new Processfaci(1, 4, 0, 17, 4),
            new Processfaci(2, 9, 3, 6, 3),  
            new Processfaci(3, 3, 4, 10, 5),
            new Processfaci(4, 10, 29, 4, 2)
        ));

        fcaiScheduler(processes);
    }
}
