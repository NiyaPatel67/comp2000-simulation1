package sim;

import javax.swing.JFrame;

public class SimulatorView extends JFrame {
    private final FieldCanvas canvas;

    public SimulatorView(Field field) {
        setTitle("Predator-Prey Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new FieldCanvas(field);
        add(canvas);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void refresh() {
        canvas.repaint();
    }
}