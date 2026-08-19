package sim;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::runSimulation);
    }

    private static void demonstrateExceptionHandling() {
    try {
        new Field(-5, 10);
    } catch (IllegalArgumentException e) {
        System.out.println("Caught expected error: " + e.getMessage());
    }

    Field testField = new Field(5, 5);
    try {
        testField.getActorAt(new Location(100, 100));
    } catch (InvalidLocationException e) {
        System.out.println("Caught expected error: " + e.getMessage());
    }
}

    private static void runSimulation() {
        Field field = new Field(30, 20);
        Random rand = new Random();

        for (int i = 0; i < 150; i++) {
            field.place(new Plant(), randomFreeLocation(field, rand));
        }
        for (int i = 0; i < 40; i++) {
            field.place(new Rabbit(), randomFreeLocation(field, rand));
        }
        for (int i = 0; i < 8; i++) {
            field.place(new Fox(), randomFreeLocation(field, rand));
        }

        SimulatorView view = new SimulatorView(field);

        Timer timer = new Timer(300, e -> {
            List<Actor> actorsCopy = new ArrayList<>(field.getActors());
            for (Actor actor : actorsCopy) {
                if (actor.isAlive()) {
                    actor.act(field);
                }
            }
            view.refresh();
        });
        timer.start();
    }

    private static Location randomFreeLocation(Field field, Random rand) {
        Location loc;
        do {
            loc = new Location(rand.nextInt(field.getHeight()), rand.nextInt(field.getWidth()));
        } while (field.getActorAt(loc) != null);
        return loc;
    }
}