import java.awt.*;
import java.util.List;

public abstract class Particle {

    protected int x;
    protected int y;
    protected MovementStrategy movement;

    public Particle(int x, int y, MovementStrategy movement) {
        this.x = x;
        this.y = y;
        this.movement = movement;
    }

    public void update(List<Particle> all, int width, int height) {

        movement.move(this, all, width, height);

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

    public int getX() { return x; }
    public int getY() { return y; }

    public Color getColor() {
        return Color.BLACK;
    }
}