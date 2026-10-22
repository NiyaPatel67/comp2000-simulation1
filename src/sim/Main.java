package sim;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Main {
    private static final int FIELD_WIDTH = 80;
    private static final int FIELD_HEIGHT = 55;
    private static final int STEP_DELAY_MS = 120;

    public static void main(String[] args) {
        demonstrateExceptionHandling();
        SwingUtilities.invokeLater(Main::runSimulation);
    }

    private static void demonstrateExceptionHandling() {
        try {
            new Field(-5, 10);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        Field testField = new Field(5, 5);
        try {
            testField.getActorAt(new Location(100, 100));
        } catch (InvalidLocationException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
    }

    /** Builds a field seeded with the starting populations. */
    public static Field createField() {
        Field field = new Field(FIELD_WIDTH, FIELD_HEIGHT);
        seed(field, Plant::new, 1400);
        seed(field, Rabbit::new, 300);
        seed(field, Fox::new, 35);
        return field;
    }

    /** Places `count` new actors made by `factory` on random free cells. */
    private static void seed(Field field, Supplier<Actor> factory, int count) {
        IntStream.range(0, count).forEach(i ->
                field.randomFreeLocation().ifPresent(loc -> field.place(factory.get(), loc)));
    }

    private static void runSimulation() {
        Simulator simulator = new Simulator(createField());

        SimulatorView view = new SimulatorView(simulator.getField());
        simulator.addObserver(view);
        simulator.addObserver(view.getGraph());
        simulator.addObserver(new ConsoleLogger(50));

        Timer timer = new Timer(STEP_DELAY_MS, e -> simulator.step());
        timer.start();
    }
}
