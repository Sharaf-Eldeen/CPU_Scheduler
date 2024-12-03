import java.util.List;

public class Algo {

    public static Object[][] simulateSJF(List<Process> processes) {
        Object[][] SJFoutput = {
            {"P1", 0, 2, "#FF5733"},
            {"Idle", 2, 3, "#D3D3D3"},
            {"P2", 3, 5, "#33FF57"},
            {"Idle", 5, 6, "#D3D3D3"},
            {"P1", 6, 8, "#FF5733"}
        };
        return SJFoutput;
    }

    public static Object[][] simulateSRTF(List<Process> processes) {
        Object[][] SRTFoutput = {
            {"P3", 0, 2, "#FF5733"},
            {"Idle", 2, 3, "#D3D3D3"},
            {"P2", 3, 5, "#33FF57"},
            {"Idle", 5, 6, "#D3D3D3"},
            {"P3", 6, 8, "#FF5733"}
        };
        return SRTFoutput;
    }

    public static Object[][] simulateFCAI(List<Process> processes) {
        Object[][] FCAIoutput = {
            {"P5", 0, 2, "#FF5733"},
            {"Idle", 2, 3, "#D3D3D3"},
            {"P2", 3, 5, "#33FF57"},
            {"Idle", 5, 6, "#D3D3D3"},
            {"P5", 6, 8, "#FF5733"}
        };
        return FCAIoutput;
    }

    public static Object[][] priorityScheduling(List<Process> processes) {
        Object[][] PSoutput = {
            {"P6", 0, 2, "#FF5733"},
            {"Idle", 2, 3, "#D3D3D3"},
            {"P2", 3, 5, "#33FF57"},
            {"Idle", 5, 6, "#D3D3D3"},
            {"P6", 6, 8, "#FF5733"}
        };
        return PSoutput;
    }
}
