package sim;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class FieldCanvas extends JPanel {
    private static final int CELL_SIZE = 20;

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
        for (Actor actor : field.getActors()) {
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
    } else if (actor instanceof Rabbit) {
        return new Color(70, 130, 180);
    } else if (actor instanceof Plant) {
        return new Color(34, 139, 34);
    }
    return Color.GRAY;
}
}