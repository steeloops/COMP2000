import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// Draws the simulation. Ticks every 100 ms but repaints about 60 times a second,
// gliding each particle between its old and new position so movement looks smooth
public class SimulationWindow extends JPanel {

    private static final int CELL = 10;          // pixels per grid cell
    private static final int STEP_MS = 100;      // simulation tick
    private static final int TAIL = 10;          // length of the fading trails
    private static final int DENSITY_CELL = 5;   // grid cells per heat-map block

    private final Simulation sim;
    // recent positions of each particle, used for the fading trails
    private final Map<Particle, Deque<Point>> trails = new HashMap<>();
    private long lastStep = System.currentTimeMillis();

    // refreshed once per tick so painting (60 fps) stays cheap
    private int[][] density;
    private Map<String, IntSummaryStatistics> stats;

    public SimulationWindow(Simulation sim) {
        this.sim = sim;
        setPreferredSize(new Dimension(600, 600));
        setBackground(new Color(10, 12, 28));
        refresh();

        // every tick: record positions, advance the simulation, recompute the stats
        Timer timer = new Timer(STEP_MS, e -> {
            remember();
            sim.step();
            refresh();
            lastStep = System.currentTimeMillis();
        });
        timer.start();

        // redraw far more often than we step, and glide between positions
        new Timer(16, e -> repaint()).start();
    }

    // density (custom collector) and crowding (groupingBy) are worked out once per
    // tick instead of every frame
    private void refresh() {
        density = sim.density(DENSITY_CELL);
        stats = new TreeMap<>(sim.crowding(5));
    }

    // save each particle's current position into its trail before it moves
    private void remember() {
        for (Particle p : sim.getParticles()) {
            Deque<Point> trail = trails.computeIfAbsent(p, k -> new ArrayDeque<>());
            trail.addFirst(new Point(p.getX(), p.getY()));
            while (trail.size() > TAIL) trail.removeLast();
        }
    }

    // drawing order: background, heat-map glow, trails and particles, then the legend
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setPaint(new GradientPaint(0, 0, new Color(8, 10, 26), 0, getHeight(), new Color(30, 14, 50)));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(255, 255, 255, 12));
        for (int i = 0; i <= getWidth(); i += CELL * DENSITY_CELL) {
            g2.drawLine(i, 0, i, getHeight());
            g2.drawLine(0, i, getWidth(), i);
        }

        // soft heat-map glow where particles gather
        int block = DENSITY_CELL * CELL;
        for (int i = 0; i < density.length; i++) {
            for (int j = 0; j < density[i].length; j++) {
                if (density[i][j] > 0) {
                    int alpha = Math.min(120, 25 + density[i][j] * 18);
                    Point2D centre = new Point2D.Double(i * block + block / 2.0, j * block + block / 2.0);
                    g2.setPaint(new RadialGradientPaint(centre, block * 0.9f,
                        new float[] { 0f, 1f },
                        new Color[] { new Color(130, 100, 255, alpha), new Color(130, 100, 255, 0) }));
                    g2.fillRect(i * block - block / 2, j * block - block / 2, block * 2, block * 2);
                }
            }
        }

        // how far between the last tick and the next we are (0 to 1)
        double t = Math.min(1.0, (System.currentTimeMillis() - lastStep) / (double) STEP_MS);
        List<Particle> particles = sim.getParticles();

        for (Particle p : particles) {
            Deque<Point> trail = trails.get(p);
            double cx = p.getX() * CELL + CELL / 2.0;
            double cy = p.getY() * CELL + CELL / 2.0;
            if (trail != null && !trail.isEmpty()) {
                Point from = trail.peekFirst();
                cx = (from.x + (p.getX() - from.x) * t) * CELL + CELL / 2.0;
                cy = (from.y + (p.getY() - from.y) * t) * CELL + CELL / 2.0;
            }

            Color c = p.getColor();

            // fading trail
            if (trail != null) {
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                double px = cx, py = cy;
                int i = 0;
                for (Point q : trail) {
                    double qx = q.x * CELL + CELL / 2.0;
                    double qy = q.y * CELL + CELL / 2.0;
                    g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 80 - 80 * i / TAIL));
                    g2.drawLine((int) px, (int) py, (int) qx, (int) qy);
                    px = qx;
                    py = qy;
                    i++;
                }
            }

            // glow, body, highlight
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 40));
            g2.fill(new Ellipse2D.Double(cx - 14, cy - 14, 28, 28));
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 90));
            g2.fill(new Ellipse2D.Double(cx - 9, cy - 9, 18, 18));
            g2.setColor(c);
            g2.fill(new Ellipse2D.Double(cx - 5.5, cy - 5.5, 11, 11));
            g2.setColor(new Color(255, 255, 255, 170));
            g2.fill(new Ellipse2D.Double(cx - 2.5, cy - 3.5, 3.5, 3.5));
        }

        drawLegend(g2, particles);
        g2.dispose();
    }

    // small panel listing each type with its colour and average neighbours
    private void drawLegend(Graphics2D g2, List<Particle> particles) {
        int rows = stats.size();
        int w = 215;
        int h = 34 + rows * 20;

        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRoundRect(12, 12, w, h, 16, 16);
        g2.setColor(new Color(255, 255, 255, 40));
        g2.drawRoundRect(12, 12, w, h, 16, 16);

        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        g2.setColor(new Color(235, 238, 250));
        g2.drawString("Particles  ·  avg neighbours", 26, 32);

        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        int y = 54;
        for (Map.Entry<String, IntSummaryStatistics> e : stats.entrySet()) {
            Color c = particles.stream()
                .filter(p -> p.getType().equals(e.getKey()))
                .map(Particle::getColor)
                .findFirst().orElse(Color.WHITE);
            g2.setColor(c);
            g2.fill(new Ellipse2D.Double(26, y - 9, 10, 10));
            g2.setColor(new Color(220, 225, 240));
            g2.drawString(e.getKey(), 44, y);
            g2.drawString(String.format("%.1f", e.getValue().getAverage()), 190, y);
            y += 20;
        }
    }
}
