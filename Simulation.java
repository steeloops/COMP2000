import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Holds all the particles and runs the simulation one step at a time
public class Simulation {

    // generic list, so only Particle objects can be added
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

    // one tick: every particle updates itself.
    // This stays a normal loop on purpose: particles read each other's positions
    // while moving, so doing it in parallel would be a race condition.
    public void step() {
        for (Particle p : particles) {
            p.update(particles, width, height);
        }
    }

    public List<Particle> getParticles() {
        return particles;
    }

    // Read-only, so safe to run as a parallel stream (see DensityCollector).
    public int[][] density(int cellSize) {
        return particles.parallelStream()
            .collect(DensityCollector.of(width, height, cellSize));
    }

    // Crowding per particle type: how many other particles share each one's
    // neighbourhood. groupingByConcurrent + summarizingInt gives min/avg/max
    // per type, and the concurrent collector is safe to use from a parallel stream.
    public Map<String, IntSummaryStatistics> crowding(int radius) {
        return particles.parallelStream().collect(
            Collectors.groupingByConcurrent(
                Particle::getType,
                Collectors.summarizingInt(p -> (int) particles.stream()
                    .filter(o -> o != p)
                    .filter(o -> Math.abs(o.getX() - p.getX()) <= radius &&
                                 Math.abs(o.getY() - p.getY()) <= radius)
                    .count())));
    }
}