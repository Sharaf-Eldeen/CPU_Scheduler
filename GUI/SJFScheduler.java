import java.util.*;

public class ShortestJobFirst {
    private List<Process>Processes;
    public ShortestJobFirst(){
        Processes=new ArrayList<Process>() ;
    }
    public void AddProcess(Process p){
           Processes.add(p);
    }
    public List<Process> SJFSchedule(){
        List<Process> FinishedProcesses = new ArrayList<>();
        Processes.sort(Comparator.comparingInt(p->p.GetArrivalTime()));
        int currentTime=0;
        while(Processes.size()!=0){
            List<Process> PC=new ArrayList<>();
            for(Process P :Processes){
                if (P.GetArrivalTime()<=currentTime) {
                    PC.add(P);
                }
            }
            if(PC.isEmpty()){
                currentTime++;
                continue;
            }
            else {
                for (Process P : Processes) {
                    if (P.GetArrivalTime() <= currentTime && !PC.contains(P)) {
                        P.IncrementPriority();
                    }
                }
                PC.sort(Comparator.comparingInt(Process::GetBurstTime).thenComparingInt(Process::GetPriority));
                Process nextProcess=PC.getFirst();
                nextProcess.calculateTimes(currentTime);
                Processes.remove(nextProcess);
                currentTime+=nextProcess.GetBurstTime();
                FinishedProcesses.add(nextProcess);
            }
        }
        return FinishedProcesses;
    }

    public static void main(String[] args) {
        ShortestJobFirst scheduler = new ShortestJobFirst();

        // Add processes
        scheduler.AddProcess(new Process(1, 0, 7, 1));
        scheduler.AddProcess(new Process(2, 2, 4, 2));
        scheduler.AddProcess(new Process(3, 4, 1, 3));
        scheduler.AddProcess(new Process(4, 5, 4, 4));

        // Perform SJF scheduling
        List<Process> completed = scheduler.SJFSchedule();

        // Print results
        System.out.println("PID\tArrivalTime\tBurestTime\tBeginningTime\tComplectionTime\tTurnaroundTime\tWaitingTime");
        for (Process p : completed) {
            System.out.printf("%d\t       %d\t       %d\t        %d\t        %d\t         %d\t        %d\n",
                    p.GetId(), p.GetArrivalTime(), p.GetBurstTime(),p.GetBeginningTime(),p.GetCompletionTime(),p.GetTurnaroundTime(),p.GetWaitingTime());
        }
    }

}
