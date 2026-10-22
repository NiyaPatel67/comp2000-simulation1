package sim;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.ToIntFunction;

/**
 * An observer that draws fox, rabbit and plant populations over time.
 * Each line is scaled to its own peak so the predator-prey cycles are visible
 * even though there are far more plants than foxes.
 */
public class PopulationGraph extends JPanel implements SimulationObserver {
    private static final int MAX_POINTS = 400;
    private static final Color FOX_COLOR = Color.RED;
    private static final Color RABBIT_COLOR = new Color(70, 130, 180);
    private static final Color PLANT_COLOR = new Color(34, 139, 34);

    private final Deque<PopulationStats> history = new ArrayDeque<>();

    public PopulationGraph(int width) {
        setPreferredSize(new Dimension(width, 180));
        setBackground(new Color(250, 250, 250));
    }

    @Override
    public void onStep(int step, Field field, PopulationStats stats) {
        history.addLast(stats);
        if (history.size() > MAX_POINTS) {
            history.removeFirst();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2f));

        // Method references choose which number each line plots.
        drawSeries(g2, PopulationStats::plants, PLANT_COLOR);
        drawSeries(g2, PopulationStats::rabbits, RABBIT_COLOR);
        drawSeries(g2, PopulationStats::foxes, FOX_COLOR);
        drawLegend(g2);
    }

    private void drawSeries(Graphics2D g2, ToIntFunction<PopulationStats> value, Color color) {
        if (history.size() < 2) return;
        int peak = Math.max(1, history.stream().mapToInt(value).max().orElse(1));
        int top = 25;
        int plotHeight = getHeight() - top - 10;
        double xStep = (double) getWidth() / (MAX_POINTS - 1);

        g2.setColor(color);
        int i = 0;
        int prevX = 0, prevY = 0;
        for (PopulationStats stats : history) {
            int x = (int) (i * xStep);
            int y = top + plotHeight - (int) ((double) value.applyAsInt(stats) / peak * plotHeight);
            if (i > 0) g2.drawLine(prevX, prevY, x, y);
            prevX = x;
            prevY = y;
            i++;
        }
    }

    private void drawLegend(Graphics2D g2) {
        PopulationStats latest = history.peekLast();
        if (latest == null) return;
        int x = 10;
        x = legendItem(g2, x, FOX_COLOR, "Foxes " + latest.foxes());
        x = legendItem(g2, x, RABBIT_COLOR, "Rabbits " + latest.rabbits());
        x = legendItem(g2, x, PLANT_COLOR, "Plants " + latest.plants());
        g2.setColor(Color.GRAY);
        g2.drawString("(each line scaled to its own peak)", x + 10, 16);
    }

    private int legendItem(Graphics2D g2, int x, Color color, String label) {
        g2.setColor(color);
        g2.fillRect(x, 7, 12, 12);
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(label, x + 16, 17);
        return x + 30 + g2.getFontMetrics().stringWidth(label);
    }
}
