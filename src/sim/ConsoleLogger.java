package sim;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** An observer that prints a population report to the console every few steps. */
public class ConsoleLogger implements SimulationObserver {
    private final int every;

    public ConsoleLogger(int every) {
        this.every = every;
    }

    @Override
    public void onStep(int step, Field field, PopulationStats stats) {
        if (step % every != 0) return;

        // groupingBy + counting: how many of each species?
        Map<String, Long> counts = field.stream()
                .collect(Collectors.groupingBy(a -> a.getClass().getSimpleName(),
                                               TreeMap::new, Collectors.counting()));

        System.out.printf("Step %4d | %s%n", step, counts);
    }
}
