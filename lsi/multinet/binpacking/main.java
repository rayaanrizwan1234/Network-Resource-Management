package lsi.multinet.binpacking;

import lsi.multinet.*;
import lsi.multinet.benchmarks.*;

public class main {
    public static void main(String[] args) {
        // Test the MessageFlow class
        // Parameters for the generator
        int nflows = 5000;
        double minPeriod = 5.0;
        double maxPeriod = 120.0;
        double targetUtil = 30;
        int nCrit = 4;
        double hasCritFactor = 0.8;
        double minCritFactor = 0.4;
        double maxCritFactor = 0.8;
        int seed = 1;

        // Create an instance of the generator
        SyntheticBenchmarkGenerator generator = new SyntheticBenchmarkGenerator(
            nflows, minPeriod, maxPeriod, targetUtil, nCrit, hasCritFactor, minCritFactor, maxCritFactor, seed
        );

        Benchmark benchmark = generator.generateCSV("message_flows.csv");
        Network wifi = new Network(64000,0);
		wifi.setName("WiFi");
		benchmark.addNetwork(wifi);
		
		
		Network lora = new Network(1760,0); // LoRa SF9
		lora.setName("LoRa");
		benchmark.addNetwork(lora);
		
		Network sigfox = new Network(48,0);
		sigfox.setName("SigFox");
		benchmark.addNetwork(sigfox);
		

        CriticalityAwareUtilisationBasedMultiNetworkManagement mgmt;
		
		System.out.println("-------------------------------------------");
		System.out.println("------------------CABF---------------------");

		
		mgmt = new CriticalityAwareUtilisationBasedMultiNetworkManagement();
		mgmt.setDecreasing(false);
		mgmt.setBestFit();
		
		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		// mgmt.printUnallocatedElements();

		System.out.println(mgmt.getObjectiveScore());


        // Generate the synthetic benchmark
        // Benchmark benchmark = generator.getSyntheticBenchmark();

        // Print the generated benchmark
        // System.out.println(benchmark);
    }
}
