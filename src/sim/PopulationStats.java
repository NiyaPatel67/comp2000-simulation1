package sim;

/** An immutable summary of the field after one step. */
public record PopulationStats(int foxes, int rabbits, int plants) {

    public int animals() {
        return foxes + rabbits;
    }
}
