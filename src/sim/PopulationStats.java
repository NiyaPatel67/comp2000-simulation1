package sim;

/** An immutable summary of the field after one step. Produced by PopulationStatsCollector. */
public record PopulationStats(int foxes, int rabbits, int plants, int hungryAnimals, double averageAnimalAge) {

    public int animals() {
        return foxes + rabbits;
    }
}
