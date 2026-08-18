package sim;

import java.util.Random;

public class Fox extends Animal {
    private static final int MAX_AGE = 150;
    private static final int BREEDING_AGE = 15;
    private static final double BREEDING_PROBABILITY = 0.09;
    private static final int MAX_LITTER_SIZE = 3;
    private static final int RABBIT_FOOD_VALUE = 9;
    private static final Random rand = new Random();

    public Fox() {
        setFoodLevel(RABBIT_FOOD_VALUE);
    }

    @Override
    public void act(Field field) {
        incrementAge(MAX_AGE);
        incrementHunger();
        if (isAlive()) {
            Location rabbitLocation = huntRabbits(field);
            if (rabbitLocation != null) {
                field.move(this, rabbitLocation);
            } else {
                field.freeAdjacentLocation(getLocation())
                     .ifPresent(loc -> field.move(this, loc));
            }
            breed(field);
        }
    }

    private Location huntRabbits(Field field) {
        for (Location loc : field.adjacentLocations(getLocation())) {
            Actor actor = field.getActorAt(loc);
            if (actor instanceof Rabbit rabbit && rabbit.isAlive()) {
                rabbit.setDead();
                field.removeActor(rabbit);
                setFoodLevel(RABBIT_FOOD_VALUE);
                return loc;
            }
        }
        return null;
    }

    private void breed(Field field) {
        if (getAge() >= BREEDING_AGE && rand.nextDouble() <= BREEDING_PROBABILITY) {
            int births = rand.nextInt(MAX_LITTER_SIZE) + 1;
            for (int i = 0; i < births; i++) {
                field.freeAdjacentLocation(getLocation())
                     .ifPresent(loc -> field.place(new Fox(), loc));
            }
        }
    }
}