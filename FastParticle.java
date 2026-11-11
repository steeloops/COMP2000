import java.awt.*;

// Fast particle: moves 2 cells at a time, drawn red.
// The flockers treat these as predators.
public class FastParticle extends Particle {
    public FastParticle(int x, int y) {
        super(x, y, new FastMovement());
    }

    @Override
    public Color getColor() {
        return new Color(255, 85, 95);
    }
}