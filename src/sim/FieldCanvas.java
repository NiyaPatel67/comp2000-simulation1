package sim;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class FieldCanvas extends JPanel {
    private static final int CELL_SIZE = 10;

    private final Field field;

    public FieldCanvas(Field field) {
        this.field = field;
        int width = field.getWidth() * CELL_SIZE;
        int height = field.getHeight() * CELL_SIZE;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Field is Iterable<Actor>, so we can loop over it directly (Iterator pattern).
        for (Actor actor : field) {
            if (!actor.isAlive()) continue;
            Location loc = actor.getLocation();
            int x = loc.getCol() * CELL_SIZE;
            int y = loc.getRow() * CELL_SIZE;
            g.setColor(colorFor(actor));
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }

    private Color colorFor(Actor actor) {
        if (actor instanceof Fox) {
            return Color.RED;
        } else if (actor instanceof Rabbit fed) {
            // Hungry rabbits are drawn paler, so the State pattern is visible on screen.
            return fed.isHungry() ? new Color(150, 190, 225) : new Color(70, 130, 180);
        } else if (actor instanceof Plant) {
            return new Color(34, 139, 34);
        }
        return Color.GRAY;
    }
}
