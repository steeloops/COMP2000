import java.util.List;

// Wanders around, sometimes picking a new random direction
public class RandomMovement implements MovementStrategy {

    // current direction (-1, 0 or 1 in each axis)
    private int dx = 1;
    private int dy = 1;

    @Override
    public void move(Particle p, List<Particle> all, int width, int height) {

        // take the step
        p.x += dx;
        p.y += dy;

        // 10% chance each step to pick a new direction
        if (Math.random() < 0.1) dx = (int)(Math.random() * 3) - 1;
        if (Math.random() < 0.1) dy = (int)(Math.random() * 3) - 1;
    }

    @Override
    public void reverseX() { dx = -dx; }

    @Override
    public void reverseY() { dy = -dy; }
}