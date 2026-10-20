package sim;

/**
 * Observer pattern: anything that wants to react after each simulation step
 * (the window, the graph, the console log) implements this interface and
 * registers with the Simulator. The Simulator never needs to know what its
 * observers actually are.
 */
@FunctionalInterface
public interface SimulationObserver {
    void onStep(int step, Field field, PopulationStats stats);
}
