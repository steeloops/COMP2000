import java.util.List;

public class RandomMovement implements MovementStrategy {

    private int dx = 1;
    private int dy = 1;

    @Override
    public void move(Particle p, List<Particle> all, int width, int height) {

        p.x += dx;
        p.y += dy;

        if (Math.random() < 0.1) dx = (int)(Math.random() * 3) - 1;
        if (Math.random() < 0.1) dy = (int)(Math.random() * 3) - 1;
    }

    @Override
    public void reverseX() { dx = -dx; }

    @Override
    public void reverseY() { dy = -dy; }
}