import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Flocking strategy. Each flocker follows simple rules (stay near other flockers,
// don't crowd them, run from red particles). Nobody controls the flock, the
// clusters that appear come from these rules (emergent behaviour).
public class FlockingMovement implements MovementStrategy {

    // how far a flocker can see other flockers
    private static final int VISION = 15;
    // how close a predator has to be before they run
    private static final int PANIC = 8;
    // closer than this counts as too crowded
    private static final int PERSONAL_SPACE = 1;

    private int dx = 1;
    private int dy = 1;

    @Override
    public void move(Particle p, List<Particle> all, int width, int height) {

        // 1. predators: find the closest FastParticle within PANIC cells
        Optional<Particle> threat = all.stream()
            .filter(o -> o instanceof FastParticle)
            .filter(o -> distance(p, o) < PANIC)
            .min(Comparator.comparingInt(o -> distance(p, o)));

        // run directly away from it
        if (threat.isPresent()) {
            dx = Integer.compare(p.getX(), threat.get().getX());
            dy = Integer.compare(p.getY(), threat.get().getY());
        } else {
            // 2. otherwise look for other flockers within VISION cells
            List<Particle> flock = all.stream()
                .filter(o -> o != p)
                .filter(o -> o instanceof FlockingParticle)
                .filter(o -> distance(p, o) < VISION)
                .collect(Collectors.toList());

            if (!flock.isEmpty()) {
                // cohesion: head towards the average position of the nearby flockers
                double cx = flock.stream().mapToInt(Particle::getX).average().orElse(p.getX());
                double cy = flock.stream().mapToInt(Particle::getY).average().orElse(p.getY());
                dx = Integer.signum((int) Math.round(cx) - p.getX());
                dy = Integer.signum((int) Math.round(cy) - p.getY());

                // separation: if someone is right next to me, back off instead
                if (flock.stream().anyMatch(o -> distance(p, o) <= PERSONAL_SPACE)) {
                    dx = -dx;
                    dy = -dy;
                }
            }

            // a little random wandering so flocks keep moving
            if (Math.random() < 0.1) dx = (int)(Math.random() * 3) - 1;
            if (Math.random() < 0.1) dy = (int)(Math.random() * 3) - 1;
        }

        // take the step
        p.x += dx;
        p.y += dy;
    }

    // distance in grid cells (the bigger of the x gap and the y gap)
    private static int distance(Particle a, Particle b) {
        return Math.max(Math.abs(a.getX() - b.getX()), Math.abs(a.getY() - b.getY()));
    }

    @Override
    public void reverseX() { dx = -dx; }

    @Override
    public void reverseY() { dy = -dy; }
}
