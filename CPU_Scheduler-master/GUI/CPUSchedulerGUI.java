import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class CPUSchedulerGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CPUSchedulerGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        // Main frame
        JFrame frame = new JFrame("CPU Scheduler Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        // Table for process input
        String[] columnNames = {"Process Name", "Color", "Arrival Time", "Burst Time", "Priority","Time Quantum","context switching"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable processTable = new JTable(tableModel);

        // Add process button
        JButton addRowButton = new JButton("Add Process");
        addRowButton.addActionListener(e -> {
            Object[] row = {"", "Choose Color", "", "", "","",""};
            tableModel.addRow(row);
        });

        // Color selection 
        processTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int column = processTable.columnAtPoint(e.getPoint());
                int row = processTable.rowAtPoint(e.getPoint());
                if (column == 1) { 
                    Color selectedColor = JColorChooser.showDialog(null, "Choose a Color", Color.WHITE);
                    if (selectedColor != null) {
                        processTable.setValueAt(colorToHex(selectedColor), row, column);
                    }
                }
            }
        });

        // Scheduler selection panel
        JPanel schedulerPanel = new JPanel();
        schedulerPanel.setLayout(new GridLayout(0, 1));
        schedulerPanel.setBorder(BorderFactory.createTitledBorder("Select Scheduler"));

        ButtonGroup schedulerGroup = new ButtonGroup();
        JRadioButton priorityButton = new JRadioButton("Priority Scheduling");
        JRadioButton sjfButton = new JRadioButton("Shortest Job First (SJF)");
        JRadioButton srtfButton = new JRadioButton("Shortest Remaining Time First (SRTF)");
        JRadioButton fcaiButton = new JRadioButton("FCAI Scheduling");

        schedulerGroup.add(priorityButton);
        schedulerGroup.add(sjfButton);
        schedulerGroup.add(srtfButton);
        schedulerGroup.add(fcaiButton);

        schedulerPanel.add(priorityButton);
        schedulerPanel.add(sjfButton);
        schedulerPanel.add(srtfButton);
        schedulerPanel.add(fcaiButton);

        // Action buttons
        JButton simulateButton = new JButton("Simulate");
        JButton clearButton = new JButton("Clear");

        simulateButton.addActionListener(e -> showSimulationOutput(processTable, priorityButton, sjfButton, srtfButton, fcaiButton));
        clearButton.addActionListener(e -> tableModel.setRowCount(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(simulateButton);
        buttonPanel.add(clearButton);

        // Adding components to the frame
        frame.add(new JScrollPane(processTable), BorderLayout.CENTER);
        frame.add(addRowButton, BorderLayout.NORTH);
        frame.add(schedulerPanel, BorderLayout.EAST);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Show frame
        frame.setVisible(true);
    }

    private String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private void showSimulationOutput(JTable processTable, JRadioButton priorityButton, JRadioButton sjfButton,
                                   JRadioButton srtfButton, JRadioButton fcaiButton) {
    int rowCount = processTable.getRowCount();
    if (rowCount == 0) {
        JOptionPane.showMessageDialog(null, "No processes to simulate.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Convert table data to a list of Process objects
    List<Process> processes = new ArrayList<>();
    for (int i = 0; i < rowCount; i++) {
        String name = (String) processTable.getValueAt(i, 0);
        String color = (String) processTable.getValueAt(i, 1);
        int arrivalTime = Integer.parseInt((String) processTable.getValueAt(i, 2));
        int burstTime = Integer.parseInt((String) processTable.getValueAt(i, 3));
        int priority = Integer.parseInt((String) processTable.getValueAt(i, 4));
        int timeQuantum = Integer.parseInt((String) processTable.getValueAt(i, 5));
        int contextSwitching = Integer.parseInt((String) processTable.getValueAt(i, 6));
        processes.add(new Process(name, color, arrivalTime, burstTime, priority, timeQuantum, contextSwitching));
    }

    // Determine which scheduling algorithm to use
    Object[][] result;
    CPUSchedulersTechniques context;
     if (priorityButton.isSelected()) {
         context = new PriorityScheduler();
         result = context.run(processes);
 } 

    // else if (fcaiButton.isSelected()) {
    //     context = new FCAIScheduler();
    //     result = context.run(processes);
    // }
  else  if (sjfButton.isSelected()) {
        context = new SJFScheduler();
        result = context.run(processes);
        System.out.println(result);
    } else if (srtfButton.isSelected()) {
        context = new SRTFScheduler();
        result = context.run(processes);
        System.out.println(result);

    }
     else {
         JOptionPane.showMessageDialog(null, "Please select a scheduling algorithm.", "Error", JOptionPane.ERROR_MESSAGE);
         return;
     }

    showGraphicalRepresentation(result);
}


private void showGraphicalRepresentation(Object[][] output) {
    JFrame graphFrame = new JFrame("Graphical Representation");
    graphFrame.setSize(900, 600);
    graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    graphFrame.setLayout(new BorderLayout());

    // Panel for Gantt chart
    JPanel chartPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = 50; // Starting X-coordinate
            int y = 100; // Starting Y-coordinate
            int barHeight = 50; // Height of each bar
            int scaleFactor = 50; // Scale factor for 1 time unit = 50 pixels

            // Draw timeline markers
            for (int time = 0; time <= 15; time++) {
                int markerX = x + time * scaleFactor;

                // Draw marker line
                g2d.drawLine(markerX, y + barHeight + 15, markerX, y + barHeight + 25);

                // Draw time label
                g2d.drawString(String.valueOf(time), markerX - 5, y + barHeight + 40);
            }

            // Draw process bars
            for (Object[] process : output) {
                String processName = (String) process[0];
                int startTime = (int) process[1];
                int endTime = (int) process[2];
                String colorHex = (String) process[3];

                // Calculate width based on time duration
                int barWidth = (endTime - startTime) * scaleFactor;

                // Parse color from the provided hex string
                Color barColor = Color.decode(colorHex);

                // Draw bar
                g2d.setColor(barColor);
                g2d.fillRect(x + startTime * scaleFactor, y, barWidth, barHeight);

                // Draw process name
                g2d.setColor(Color.BLACK);
                g2d.drawString(processName, x + startTime * scaleFactor + 5, y + 25);
            }
        }
    };

    // Add chart panel
    graphFrame.add(chartPanel, BorderLayout.CENTER);

    // Panel for scheduling details
    JPanel detailsPanel = new JPanel();
    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

    // Display waiting times and turnaround times from output
    double totalWaitingTime = 0;
    double totalTurnaroundTime = 0;
    int n = output.length;

    StringBuilder detailsText = new StringBuilder("<html><table border='1'><tr><th>Process</th><th>Waiting Time</th><th>Turnaround Time</th></tr>");
    for (Object[] process : output) {
        String processName = (String) process[0];
        int waitingTime = (int) process[4];       // Precomputed WT from result
        int turnaroundTime = (int) process[5];   // Precomputed TAT from result

        totalWaitingTime += waitingTime;
        totalTurnaroundTime += turnaroundTime;

        detailsText.append("<tr>")
                   .append("<td>").append(processName).append("</td>")
                   .append("<td>").append(waitingTime).append("</td>")
                   .append("<td>").append(turnaroundTime).append("</td>")
                   .append("</tr>");
    }
    detailsText.append("</table><br>");

    // Calculate averages
    double avgWaitingTime = totalWaitingTime / n;
    double avgTurnaroundTime = totalTurnaroundTime / n;

    detailsText.append("Average Waiting Time: ").append(avgWaitingTime).append("<br>");
    detailsText.append("Average Turnaround Time: ").append(avgTurnaroundTime).append("<br>");
    detailsText.append("</html>");

    JLabel detailsLabel = new JLabel(detailsText.toString());
    detailsPanel.add(detailsLabel);

    // Add details panel
    graphFrame.add(detailsPanel, BorderLayout.SOUTH);

    graphFrame.setVisible(true);
}

}