import java.util.List;

// Strategy pattern: each way of moving is a class that implements this,
// so a particle can be given any behaviour without changing Particle
public interface MovementStrategy {
    // moves particle p one step; "all" lets the strategy look at the other particles
    void move(Particle p, List<Particle> all, int width, int height);

    // called by Particle when it hits a wall so the strategy bounces back
    void reverseX();
    void reverseY();
}