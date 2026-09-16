import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Simulation sim = new Simulation(60, 60);

        sim.addParticle(new NormalParticle(10, 10));
        sim.addParticle(new FastParticle(20, 20));
        sim.addParticle(new RepellingParticle(30, 30));

        JFrame frame = new JFrame("Particle Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SimulationWindow(sim));
        frame.pack();
        frame.setVisible(true);
    }
}