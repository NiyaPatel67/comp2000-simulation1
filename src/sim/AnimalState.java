package sim;

/**
 * State pattern: an animal behaves differently depending on whether it is
 * hungry or well fed. Animal delegates its turn to its current state, and
 * switches state itself as its food level changes.
 */
public interface AnimalState {
    void act(Animal animal, Field field);
    String name();
}
