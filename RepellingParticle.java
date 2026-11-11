import java.awt.*;

// Repelling particle: keeps away from others, drawn green
public class RepellingParticle extends Particle {
    public RepellingParticle(int x, int y) {
        super(x, y, new RepellingMovement());
    }

    @Override
    public Color getColor() {
        return new Color(90, 235, 150);
    }
}