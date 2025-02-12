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
        double capRatio = 0.9;
        Network wifi = new Network(64000 * capRatio,0);
		wifi.setName("WiFi");
		benchmark.addNetwork(wifi);
		
		
		Network lora = new Network(1760 * capRatio,0); // LoRa SF9
		lora.setName("LoRa");
		benchmark.addNetwork(lora);
		
		Network sigfox = new Network(48 * capRatio,0);
		sigfox.setName("SigFox");
		benchmark.addNetwork(sigfox);
		

        CriticalityAwareUtilisationBasedMultiNetworkManagement mgmt;
		
		System.out.println("-------------------------------------------");
		System.out.println("------------------CABF---------------------");

        final double flowRatio = 1;
		
		mgmt = new CriticalityAwareUtilisationBasedMultiNetworkManagement();
		mgmt.setDecreasing(false);
		mgmt.setBestFit();
		
		benchmark.clone().setManagement(mgmt);

        mgmt.populateCriticalityLists(); 

        mgmt.splitMessageFlows(flowRatio);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
        System.out.println(mgmt.getObjectiveScore());
		// mgmt.printUnallocatedElements();

        for (NetworkBin bin : mgmt.bins) {
            final var name = bin.network.getName();
            if (name == "WiFi") {
                bin.network.setBandwidth(64000);
            } else if (name == "LoRa") {
                bin.network.setBandwidth(1760);
            } else if (name == "SigFox") {
                bin.network.setBandwidth(48);
            }
        }

        System.out.println("Second Stage =======================================");

        System.out.println(mgmt.performAllocation2Stage());
		mgmt.printAllAllocations();
        System.out.println(mgmt.getObjectiveScore());
        
    }
}
