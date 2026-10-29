package sim;

import java.util.Set;
import java.util.stream.Collector;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A custom Collector that turns a stream of actors into a PopulationStats in ONE pass.
 *
 * It is safe to use with a parallel stream: each thread gets its own Tally from
 * supplier(), fills it with accumulator(), and the partial Tallies are merged by
 * combiner(). No Tally is ever shared between threads, so no locks are needed.
 */
public class PopulationStatsCollector implements Collector<Actor, PopulationStatsCollector.Tally, PopulationStats> {

    /** Mutable running totals. Each thread works on its own instance. */
    static class Tally {
        int foxes, rabbits, plants, hungry;
        long totalAge;

        void add(Actor actor) {
            if (actor instanceof Fox) foxes++;
            else if (actor instanceof Rabbit) rabbits++;
            else if (actor instanceof Plant) plants++;

            if (actor instanceof Animal animal) {
                totalAge += animal.getAge();
                if (animal.isHungry()) hungry++;
            }
        }

        Tally merge(Tally other) {
            foxes += other.foxes;
            rabbits += other.rabbits;
            plants += other.plants;
            hungry += other.hungry;
            totalAge += other.totalAge;
            return this;
        }

        PopulationStats toStats() {
            int animals = foxes + rabbits;
            double avgAge = animals == 0 ? 0 : (double) totalAge / animals;
            return new PopulationStats(foxes, rabbits, plants, hungry, avgAge);
        }
    }

    @Override public Supplier<Tally> supplier() { return Tally::new; }
    @Override public BiConsumer<Tally, Actor> accumulator() { return Tally::add; }
    @Override public BinaryOperator<Tally> combiner() { return Tally::merge; }
    @Override public Function<Tally, PopulationStats> finisher() { return Tally::toStats; }
    @Override public Set<Characteristics> characteristics() { return Set.of(Characteristics.UNORDERED); }
}
