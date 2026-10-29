package sim;

import java.util.Random;

/**
 * Base class for every animal.
 * Uses two design patterns:
 *  - Strategy: WHAT it eats is a FeedingStrategy object (usually a lambda) passed in by the subclass.
 *  - State:    HOW it behaves this turn depends on its current AnimalState (hungry or fed).
 */
public abstract class Animal extends Actor {
    private static final Random rand = new Random();

    private int age = 0;
    private int foodLevel;
    private AnimalState state = FedState.INSTANCE;
    private final FeedingStrategy diet;   // composition: the animal HAS a feeding strategy
    private final int foodValue;

    protected Animal(FeedingStrategy diet, int foodValue) {
        this.diet = diet;
        this.foodValue = foodValue;
        this.foodLevel = foodValue;
    }

    @Override
    public void act(Field field) {
        incrementAge();
        incrementHunger();
        if (!isAlive()) {
            field.removeActor(this);   // dead animals free up their cell
            return;
        }
        updateState();
        state.act(this, field);        // behaviour depends on the current state
    }

    /** Switch between Hungry and Fed based on how much food is left. */
    private void updateState() {
        state = (foodLevel <= foodValue / 2) ? HungryState.INSTANCE : FedState.INSTANCE;
    }

    /** Eat the first adjacent actor our feeding strategy accepts. Returns true if we ate. */
    boolean eatNearbyFood(Field field) {
        for (Location loc : field.adjacentLocations(getLocation())) {
            Actor actor = field.getActorAt(loc);
            if (actor != null && actor.isAlive() && diet.canEat(actor)) {
                actor.setDead();
                field.removeActor(actor);
                field.move(this, loc);
                foodLevel = foodValue;
                return true;
            }
        }
        return false;
    }

    void wander(Field field) {
        field.freeAdjacentLocation(getLocation())
             .ifPresent(loc -> field.move(this, loc));
    }

    void breed(Field field) {
        if (age >= getBreedingAge() && rand.nextDouble() <= getBreedingProbability()) {
            int births = rand.nextInt(getMaxLitterSize()) + 1;
            for (int i = 0; i < births; i++) {
                field.freeAdjacentLocation(getLocation())
                     .ifPresent(loc -> field.place(createYoung(), loc));
            }
        }
    }

    private void incrementAge() {
        age++;
        if (age > getMaxAge()) setDead();
    }

    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) setDead();
    }

    public int getAge() { return age; }
    public int getFoodLevel() { return foodLevel; }
    public boolean isHungry() { return state == HungryState.INSTANCE; }
    public AnimalState getState() { return state; }

    // Each species supplies its own numbers and offspring.
    protected abstract int getMaxAge();
    protected abstract int getBreedingAge();
    protected abstract double getBreedingProbability();
    protected abstract int getMaxLitterSize();
    protected abstract Animal createYoung();
}
