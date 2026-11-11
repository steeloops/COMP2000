import javax.swing.*;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        // 60 x 60 grid
        Simulation sim = new Simulation(60, 60);

        // the original three starter particles
        sim.addParticle(new NormalParticle(10, 10));
        sim.addParticle(new FastParticle(20, 20));
        sim.addParticle(new RepellingParticle(30, 30));

        // use the factory to spawn many more of each type at random positions
        // (type name -> how many)
        ParticleFactory factory = new ParticleFactory();
        Random rnd = new Random();
        Map<String, Integer> population = Map.of("normal", 10, "fast", 3, "repelling", 5, "flocking", 40, "seeker", 3);
        population.forEach((type, n) ->
            IntStream.range(0, n).forEach(i ->
                sim.addParticle(factory.create(type, rnd.nextInt(60), rnd.nextInt(60)))));

        // open the window
        JFrame frame = new JFrame("Particle Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SimulationWindow(sim));
        frame.pack();
        frame.setVisible(true);
    }
}