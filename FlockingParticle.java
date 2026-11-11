import java.awt.*;

// Flocking particle: moves with the flocking rules, drawn pink
public class FlockingParticle extends Particle {
    public FlockingParticle(int x, int y) {
        super(x, y, new FlockingMovement());
    }

    @Override
    public Color getColor() {
        return new Color(255, 120, 230);
    }
}
