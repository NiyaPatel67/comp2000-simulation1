package sim;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs the simulation one step at a time and notifies observers (Observer pattern subject).
 */
public class Simulator {
    private static final int SEEDLINGS_PER_STEP = 6;   // new plants sprout on empty ground

    private final Field field;
    private final List<SimulationObserver> observers = new ArrayList<>();
    private int step = 0;

    public Simulator(Field field) {
        this.field = field;
    }

    public void addObserver(SimulationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(SimulationObserver observer) {
        observers.remove(observer);
    }

    public void step() {
        step++;

        // Act on a snapshot, because actors are born and die during the loop.
        // This loop stays sequential: actors move each other around the shared grid.
        for (Actor actor : field.snapshot()) {
            if (actor.isAlive()) {
                actor.act(field);
            }
        }

        for (int i = 0; i < SEEDLINGS_PER_STEP; i++) {
            field.randomFreeLocation().ifPresent(loc -> field.place(new Plant(), loc));
        }

        // Read-only, so this part can safely run in parallel with the custom collector.
        PopulationStats stats = field.stream()
                .parallel()
                .collect(new PopulationStatsCollector());

        observers.forEach(observer -> observer.onStep(step, field, stats));
    }

    public int getStep() { return step; }
    public Field getField() { return field; }
}
