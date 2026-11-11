import java.util.List;

// Same idea as RandomMovement but moves 2 cells per step
public class FastMovement implements MovementStrategy {

    // speed is 2 instead of 1
    private int dx = 2;
    private int dy = 2;

    @Override
    public void move(Particle p, List<Particle> all, int width, int height) {

        // take the step
        p.x += dx;
        p.y += dy;

        // small chance to change direction, still in steps of 2
        if (Math.random() < 0.1) dx = ((int)(Math.random() * 3) - 1) * 2;
        if (Math.random() < 0.1) dy = ((int)(Math.random() * 3) - 1) * 2;
    }

    @Override
    public void reverseX() { dx = -dx; }

    @Override
    public void reverseY() { dy = -dy; }
}