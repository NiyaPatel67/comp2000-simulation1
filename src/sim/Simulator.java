package sim;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs the simulation one step at a time and notifies observers (Observer pattern subject).
 */
public class Simulator {
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
        for (Actor actor : field.snapshot()) {
            if (actor.isAlive()) {
                actor.act(field);
            }
        }

        PopulationStats stats = new PopulationStats(
                count(Fox.class), count(Rabbit.class), count(Plant.class));

        observers.forEach(observer -> observer.onStep(step, field, stats));
    }

    /** Stream pipeline: actors -> keep those of one species -> count them. */
    private int count(Class<? extends Actor> species) {
        return (int) field.stream().filter(species::isInstance).count();
    }

    public int getStep() { return step; }
    public Field getField() { return field; }
}
