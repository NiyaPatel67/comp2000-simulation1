package sim;

/** A well-fed animal ignores food, wanders, and may breed. */
public class FedState implements AnimalState {
    public static final FedState INSTANCE = new FedState();
    private FedState() { }

    @Override
    public void act(Animal animal, Field field) {
        animal.wander(field);
        animal.breed(field);
    }

    @Override public String name() { return "Fed"; }
}
