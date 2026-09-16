import java.awt.*;

public class RepellingParticle extends Particle {
    public RepellingParticle(int x, int y) {
        super(x, y, new RepellingMovement());
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }
}