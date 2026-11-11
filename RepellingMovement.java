import java.util.List;

// Moves away from any particle that gets too close
public class RepellingMovement implements MovementStrategy {

    private int dx = 1;
    private int dy = 1;

    @Override
    public void move(Particle p, List<Particle> all, int width, int height) {

        // find another particle within 10 cells (filter out myself first)
        // and if there is one, head in the opposite direction
        all.stream()
            .filter(other -> other != p)
            .filter(other -> Math.abs(other.getX() - p.getX()) < 10 &&
                             Math.abs(other.getY() - p.getY()) < 10)
            .findFirst()
            .ifPresent(near -> {
                dx = Integer.compare(p.getX(), near.getX());
                dy = Integer.compare(p.getY(), near.getY());
            });

        // take the step
        p.x += dx;
        p.y += dy;
    }

    @Override
    public void reverseX() { dx = -dx; }

    @Override
    public void reverseY() { dy = -dy; }
}