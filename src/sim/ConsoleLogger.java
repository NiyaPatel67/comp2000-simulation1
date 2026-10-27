package sim;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** An observer that prints a population report to the console every few steps, using collectors. */
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

        List<Animal> animals = field.stream()
                .filter(a -> a instanceof Animal)
                .map(a -> (Animal) a)
                .collect(Collectors.toList());

        // partitioningBy: hungry vs fed animals
        Map<Boolean, Long> hungerSplit = animals.stream()
                .collect(Collectors.partitioningBy(Animal::isHungry, Collectors.counting()));

        // groupingBy with a downstream summarizingInt: age stats per species
        Map<String, IntSummaryStatistics> agesBySpecies = animals.stream()
                .collect(Collectors.groupingBy(a -> a.getClass().getSimpleName(),
                                               TreeMap::new, Collectors.summarizingInt(Animal::getAge)));

        String ageReport = agesBySpecies.entrySet().stream()
                .map(e -> String.format("%s avg %.1f / max %d",
                        e.getKey(), e.getValue().getAverage(), e.getValue().getMax()))
                .collect(Collectors.joining(", "));

        System.out.printf("Step %4d | %s | hungry %d, fed %d | ages: %s%n",
                step, counts, hungerSplit.get(true), hungerSplit.get(false), ageReport);
    }
}
