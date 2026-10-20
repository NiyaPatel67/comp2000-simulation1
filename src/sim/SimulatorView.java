package sim;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;

/** The main window. It is itself an observer: after each step it updates the label and redraws the grid. */
public class SimulatorView extends JFrame implements SimulationObserver {
    private final FieldCanvas canvas;
    private final PopulationGraph graph;
    private final JLabel status = new JLabel(" ");

    public SimulatorView(Field field) {
        setTitle("Predator-Prey Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new FieldCanvas(field);
        graph = new PopulationGraph(canvas.getPreferredSize().width);
        status.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        setLayout(new BorderLayout());
        add(status, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(graph, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public PopulationGraph getGraph() {
        return graph;
    }

    @Override
    public void onStep(int step, Field field, PopulationStats stats) {
        status.setText(String.format("Step %d   |   Foxes %d   Rabbits %d   Plants %d",
                step, stats.foxes(), stats.rabbits(), stats.plants()));
        canvas.repaint();
    }
}
