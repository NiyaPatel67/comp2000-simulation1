package sim;

import java.util.Random;

public class Plant extends Actor {
    private static final double SPREAD_PROBABILITY = 0.10;
    private static final int MIN_LIFESPAN = 30;
    private static final int LIFESPAN_RANGE = 40;
    private static final Random rand = new Random();

    private final int lifespan = MIN_LIFESPAN + rand.nextInt(LIFESPAN_RANGE);
    private int age = 0;

    @Override
    public void act(Field field) {
        age++;
        if (age > lifespan) {
            // Old plants wither, leaving room for animals to move and breed.
            setDead();
            field.removeActor(this);
            return;
        }
        if (rand.nextDouble() <= SPREAD_PROBABILITY) {
            field.freeAdjacentLocation(getLocation())
                 .ifPresent(loc -> field.place(new Plant(), loc));
        }
    }
}
