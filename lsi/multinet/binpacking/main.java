import lsi.multinet.*;
import lsi.multinet.benchmarks.*;

public class main {
    public static void main(String[] args) {
        // Test the MessageFlow class
        // Parameters for the generator
        int nflows = 1000;
        double minPeriod = 5.0;
        double maxPeriod = 120.0;
        double targetUtil = 40;
        int nCrit = 4;
        double hasCritFactor = 0.8;
        double minCritFactor = 0.4;
        double maxCritFactor = 0.8;
        int seed = 42;

        // Create an instance of the generator
        SyntheticBenchmarkGenerator generator = new SyntheticBenchmarkGenerator(
            nflows, minPeriod, maxPeriod, targetUtil, nCrit, hasCritFactor, minCritFactor, maxCritFactor, seed
        );

        // Generate the synthetic benchmark
        Benchmark benchmark = generator.getSyntheticBenchmark();

        // Print the generated benchmark
        System.out.println(benchmark);
    }
}
