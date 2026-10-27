package sim;

/** A hungry animal looks for food first, and only wanders if there is none nearby. It does not breed. */
public class HungryState implements AnimalState {
    public static final HungryState INSTANCE = new HungryState();
    private HungryState() { }

    @Override
    public void act(Animal animal, Field field) {
        if (!animal.eatNearbyFood(field)) {
            animal.wander(field);
        }
    }

    @Override public String name() { return "Hungry"; }
}
