import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private final List<Particle> particles = new ArrayList<>();
    private final int width;
    private final int height;

    public Simulation(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addParticle(Particle p) {
        particles.add(p);
    }

    public void step() {
        for (Particle p : particles) {
            p.update(particles, width, height);
        }
    }

    public List<Particle> getParticles() {
        return particles;
    }
}