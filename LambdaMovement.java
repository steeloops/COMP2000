import java.util.List;

// A MovementStrategy built from a lambda instead of a new subclass.
// A new behaviour is one expression rather than one more class.
public class LambdaMovement implements MovementStrategy {

    // a lambda that is given a particle and everyone else and
    // returns the direction {dx, dy} to move
    @FunctionalInterface
    public interface Steering {
        int[] steer(Particle p, List<Particle> all);
    }

    // the behaviour, passed in from outside (e.g. as a lambda)
    private final Steering steering;
    private int dx = 1;
    private int dy = 1;

    public LambdaMovement(Steering steering) {
        this.steering = steering;
    }

    @Override
    public void move(Particle p, List<Particle> all, int width, int height) {
        // ask the lambda which way to go this step
        int[] d = steering.steer(p, all);
        dx = d[0];
        dy = d[1];

        p.x += dx;
        p.y += dy;
    }

    @Override
    public void reverseX() { dx = -dx; }

    @Override
    public void reverseY() { dy = -dy; }
}
