package sim;

import java.util.Random;

public class Rabbit extends Animal {
    private static final int MAX_AGE = 40;
    private static final int BREEDING_AGE = 5;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 4;
    private static final int PLANT_FOOD_VALUE = 6;
    private static final Random rand = new Random();

    public Rabbit() {
        setFoodLevel(PLANT_FOOD_VALUE);
    }

    @Override
    public void act(Field field) {
        incrementAge(MAX_AGE);
        incrementHunger();
        if (isAlive()) {
            Location plantLocation = eatPlant(field);
            if (plantLocation != null) {
                field.move(this, plantLocation);
            } else {
                field.freeAdjacentLocation(getLocation())
                     .ifPresent(loc -> field.move(this, loc));
            }
            breed(field);
        }
    }

    private Location eatPlant(Field field) {
        for (Location loc : field.adjacentLocations(getLocation())) {
            Actor actor = field.getActorAt(loc);
            if (actor instanceof Plant plant && plant.isAlive()) {
                plant.setDead();
                field.removeActor(plant);
                setFoodLevel(PLANT_FOOD_VALUE);
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
                     .ifPresent(loc -> field.place(new Rabbit(), loc));
            }
        }
    }
}