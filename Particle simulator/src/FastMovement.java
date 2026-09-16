import java.util.List;

public class FastMovement implements MovementStrategy {

    private int dx = 2;
    private int dy = 2;

    @Override
    public void move(Particle p, List<Particle> all, int width, int height) {

        p.x += dx;
        p.y += dy;

        if (Math.random() < 0.1) dx = ((int)(Math.random() * 3) - 1) * 2;
        if (Math.random() < 0.1) dy = ((int)(Math.random() * 3) - 1) * 2;
    }

    @Override
    public void reverseX() { dx = -dx; }

    @Override
    public void reverseY() { dy = -dy; }
}