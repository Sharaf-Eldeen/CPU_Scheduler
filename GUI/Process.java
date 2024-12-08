import java.util.List;

public class Process {
    private int Id;
    private int ArrivalTime;
    private int BurstTime;
    private int Priority;
    private int CompletionTime;
    private int TurnaroundTime;
    private int WaitingTime;
    private int BeginningTime;
    private int RemainingTime;
    private int Quantum;
    private double FcaiFactor;
    List<String> executionPeriods;
    public Process(int Id,int ArrivalTime,int BurstTime,int Priority){
        this.Id=Id;
        this.ArrivalTime=ArrivalTime;
        this.BurstTime=BurstTime;
        this.Priority=Priority;
    }
    public  int  GetId(){return Id;}
    public  int  GetArrivalTime(){return ArrivalTime;}
    public  int  GetCompletionTime(){return CompletionTime;}
    public  int  GetWaitingTime(){return WaitingTime;}
    public void SetWaitingTime(int x){this.WaitingTime=x;}
    public  int  GetBurstTime(){return BurstTime;}
    public  int  GetTurnaroundTime(){return TurnaroundTime;}
    public  int  GetBeginningTime(){return BeginningTime;}
    public void  IncrementPriority(){this.Priority--;}
    public  int  GetPriority(){return Priority;}
    public void  calculateTimes(int currentTime) {
        this.BeginningTime=currentTime;
        this.CompletionTime = currentTime + this.BurstTime;
        this.TurnaroundTime = this.CompletionTime - this.ArrivalTime;
        this.WaitingTime = this.TurnaroundTime - this.BurstTime;
    }

}
