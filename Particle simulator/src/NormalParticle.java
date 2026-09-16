import java.awt.*;

public class NormalParticle extends Particle {
    public NormalParticle(int x, int y) {
        super(x, y, new RandomMovement());
    }

    @Override
    public Color getColor() {
        return Color.BLUE;
    }
}