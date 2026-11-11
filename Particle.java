import java.awt.*;
import java.util.List;

// Base class for every particle: holds the position and a MovementStrategy.
// Subclasses only choose which strategy to use and what colour to be.
public abstract class Particle {

    protected int x;
    protected int y;
    // the strategy decides how this particle moves
    protected MovementStrategy movement;

    public Particle(int x, int y, MovementStrategy movement) {
        this.x = x;
        this.y = y;
        this.movement = movement;
    }

    // one tick: move using the strategy, then keep the particle inside the grid
    public void update(List<Particle> all, int width, int height) {

        // let the strategy do the moving
        movement.move(this, all, width, height);

        // wall checks: put the particle back inside and tell the strategy to reverse
        if (x < 0) {
            x = 0;
            movement.reverseX();
        } else if (x >= width) {
            x = width - 1;
            movement.reverseX();
        }

        if (y < 0) {
            y = 0;
            movement.reverseY();
        } else if (y >= height) {
            y = height - 1;
            movement.reverseY();
        }
    }

    // Short display name, e.g. "Fast" for FastParticle (used by the legend).
    public String getType() {
        return getClass().getSimpleName().replace("Particle", "");
    }

    public int getX() { return x; }
    public int getY() { return y; }

    // default colour, subclasses override this
    public Color getColor() {
        return Color.BLACK;
    }
}