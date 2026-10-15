package sim;

/**
 * Strategy pattern: decides what an animal can eat.
 * It has a single abstract method, so it is a functional interface
 * and each animal's diet can be supplied as a lambda.
 */
@FunctionalInterface
public interface FeedingStrategy {
    boolean canEat(Actor food);
}
