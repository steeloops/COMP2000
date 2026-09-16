import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationWindow extends JPanel {

    private final Simulation sim;

    public SimulationWindow(Simulation sim) {
        this.sim = sim;
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.WHITE);

        Timer timer = new Timer(100, e -> {
            sim.step();
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        List<Particle> particles = sim.getParticles();

        for (Particle p : particles) {
            g.setColor(p.getColor());
            g.fillOval(p.getX() * 10, p.getY() * 10, 10, 10);
        }
    }
}