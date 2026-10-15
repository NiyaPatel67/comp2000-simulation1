package sim;

public class Fox extends Animal {
    private static final int MAX_AGE = 150;
    private static final int BREEDING_AGE = 15;
    private static final double BREEDING_PROBABILITY = 0.09;
    private static final int MAX_LITTER_SIZE = 3;
    private static final int RABBIT_FOOD_VALUE = 9;

    public Fox() {
        // The feeding strategy is a lambda: foxes eat rabbits.
        super(food -> food instanceof Rabbit, RABBIT_FOOD_VALUE);
    }

    @Override protected int getMaxAge() { return MAX_AGE; }
    @Override protected int getBreedingAge() { return BREEDING_AGE; }
    @Override protected double getBreedingProbability() { return BREEDING_PROBABILITY; }
    @Override protected int getMaxLitterSize() { return MAX_LITTER_SIZE; }
    @Override protected Animal createYoung() { return new Fox(); }
}
