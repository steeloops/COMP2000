import java.awt.Color;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

// Factory pattern: Main asks for a particle by name instead of using "new" directly.
// Adding a type is one register() call rather than editing an if/else or switch.
public class ParticleFactory {

    // type name -> code that builds that type (a lambda or constructor reference)
    private final Map<String, BiFunction<Integer, Integer, Particle>> registry = new LinkedHashMap<>();

    public ParticleFactory() {
        // existing classes: NormalParticle::new is a constructor reference
        register("normal", NormalParticle::new);
        register("fast", FastParticle::new);
        register("repelling", RepellingParticle::new);
        register("flocking", FlockingParticle::new);

        // No new classes: a lambda is the whole behaviour. Seekers head for the
        // nearest NormalParticle (Manhattan distance), or stand still if none.
        register("seeker", (x, y) -> new GenericParticle(x, y,
            new LambdaMovement((p, all) -> all.stream()
                .filter(o -> o instanceof NormalParticle)
                .min(Comparator.comparingInt(o -> Math.abs(o.getX() - p.getX()) + Math.abs(o.getY() - p.getY())))
                .map(t -> new int[] { Integer.compare(t.getX(), p.getX()), Integer.compare(t.getY(), p.getY()) })
                .orElse(new int[] { 0, 0 })),
            new Color(255, 205, 70), "Seeker"));
    }

    public void register(String type, BiFunction<Integer, Integer, Particle> maker) {
        registry.put(type, maker);
    }

    // look up the type and build it, unknown names are an error
    public Particle create(String type, int x, int y) {
        BiFunction<Integer, Integer, Particle> maker = registry.get(type);
        if (maker == null) {
            throw new IllegalArgumentException("Unknown particle type: " + type);
        }
        return maker.apply(x, y);
    }
}
