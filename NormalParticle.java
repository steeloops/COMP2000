import java.awt.*;

// Basic particle: random movement, drawn blue
public class NormalParticle extends Particle {
    public NormalParticle(int x, int y) {
        super(x, y, new RandomMovement());
    }

    @Override
    public Color getColor() {
        return new Color(90, 170, 255);
    }
}