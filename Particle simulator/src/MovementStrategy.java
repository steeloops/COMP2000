import java.util.List;

public interface MovementStrategy {
    void move(Particle p, List<Particle> all, int width, int height);

    void reverseX();
    void reverseY();
}