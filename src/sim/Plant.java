package sim;

import java.util.Random;

public class Plant extends Actor {
    private static final double SPREAD_PROBABILITY = 0.06;
    private static final Random rand = new Random();

    @Override
    public void act(Field field) {
        if (rand.nextDouble() <= SPREAD_PROBABILITY) {
            field.freeAdjacentLocation(getLocation())
                 .ifPresent(loc -> field.place(new Plant(), loc));
        }
    }
}