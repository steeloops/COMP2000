import java.awt.*;

public class FastParticle extends Particle {
    public FastParticle(int x, int y) {
        super(x, y, new FastMovement());
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }
}