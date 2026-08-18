package sim;

public class Main {
    public static void main(String[] args) {
        Field field = new Field(10, 10);

        Rabbit r1 = new Rabbit();
        Rabbit r2 = new Rabbit();
        Fox f1 = new Fox();

        field.place(r1, new Location(2, 2));
        field.place(r2, new Location(2, 3));
        field.place(f1, new Location(5, 5));

        for (int round = 1; round <= 20; round++) {
            System.out.println("Round " + round);
            for (Actor actor : new java.util.ArrayList<>(field.getActors())) {
                if (actor.isAlive()) {
                    actor.act(field);
                    System.out.println("  " + actor.getClass().getSimpleName()
                        + " at " + actor.getLocation());
                }
            }
        }
    }
}