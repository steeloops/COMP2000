import java.util.stream.Collector;

// Custom Collector: counts particles per block of the grid.
// Thread safe because every thread of a parallel stream fills its OWN grid,
// and the grids are only merged afterwards, so nothing is shared.
public final class DensityCollector {

    private DensityCollector() {}

    public static Collector<Particle, int[][], int[][]> of(int width, int height, int cellSize) {
        int cols = (width + cellSize - 1) / cellSize;
        int rows = (height + cellSize - 1) / cellSize;

        // Collector.of(supplier, accumulator, combiner, characteristics)
        return Collector.of(
            // supplier: each thread starts with its own empty grid
            () -> new int[cols][rows],
            // accumulator: add one particle to the cell it is in
            (grid, p) -> grid[p.getX() / cellSize][p.getY() / cellSize]++,
            // combiner: merge two threads' grids by adding the counts
            (a, b) -> {
                for (int i = 0; i < cols; i++) {
                    for (int j = 0; j < rows; j++) {
                        a[i][j] += b[i][j];
                    }
                }
                return a;
            },
            // the order of the particles doesn't matter
            Collector.Characteristics.UNORDERED
        );
    }
}
