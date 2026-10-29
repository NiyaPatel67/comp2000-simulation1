package sim;

public class Rabbit extends Animal {
    private static final int MAX_AGE = 40;
    private static final int BREEDING_AGE = 5;
    private static final double BREEDING_PROBABILITY = 0.10;
    private static final int MAX_LITTER_SIZE = 3;
    private static final int PLANT_FOOD_VALUE = 12;

    public Rabbit() {
        // The feeding strategy is a lambda: rabbits eat plants.
        super(food -> food instanceof Plant, PLANT_FOOD_VALUE);
    }

    @Override protected int getMaxAge() { return MAX_AGE; }
    @Override protected int getBreedingAge() { return BREEDING_AGE; }
    @Override protected double getBreedingProbability() { return BREEDING_PROBABILITY; }
    @Override protected int getMaxLitterSize() { return MAX_LITTER_SIZE; }
    @Override protected Animal createYoung() { return new Rabbit(); }
}
