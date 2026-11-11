import java.awt.*;

// Particle whose strategy, colour and name are passed in, so a new type
// doesn't need its own subclass (the factory builds these with lambdas)
public class GenericParticle extends Particle {

    private final Color color;
    private final String type;

    public GenericParticle(int x, int y, MovementStrategy movement, Color color, String type) {
        super(x, y, movement);
        this.color = color;
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
