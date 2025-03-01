package lsi.multinet.binpacking;

import lsi.multinet.*;
import lsi.multinet.benchmarks.*;

public class main_parameter_tuning {
    public static void main(String[] args) {
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

        double bestCapRatio = 0;
        double bestFlowRatio = 0;
        double bestObjectiveScore = Double.NEGATIVE_INFINITY;
        double bestNumFlows = 0;

        // double capRatio = 0.0000000001;
        // double flowRatio = 0.99;

        // Loop over possible values of capRatio and flowRatio
        for (double capRatio = 0.01; capRatio <= 1; capRatio += 0.01) {
            for (double flowRatio = 0.01; flowRatio <= 1; flowRatio += 0.01) {
            Benchmark benchmark = generator.generateCSV("message_flows.csv");
		
                // Create networks with the current capRatio
                Network wifi = new Network(64000 * capRatio, 0);
                wifi.setName("WiFi");
                benchmark.addNetwork(wifi);

                Network lora = new Network(1760 * capRatio, 0); // LoRa SF9
                lora.setName("LoRa");
                benchmark.addNetwork(lora);

                Network sigfox = new Network(48 * capRatio, 0);
                sigfox.setName("SigFox");
                benchmark.addNetwork(sigfox);

                CriticalityAwareUtilisationBasedMultiNetworkManagement mgmt;

                mgmt = new CriticalityAwareUtilisationBasedMultiNetworkManagement();
                mgmt.setDecreasing(false);
                mgmt.setBestFit();

                benchmark.clone().setManagement(mgmt);

                mgmt.populateCriticalityLists();
                mgmt.splitMessageFlows(flowRatio);

                mgmt.performAllocation();

                // Restore full capacity for the second stage
                for (NetworkBin bin : mgmt.bins) {
                    final var name = bin.network.getName();
                    if (name.equals("WiFi")) {
                        bin.network.setBandwidth(64000);
                    } else if (name.equals("LoRa")) {
                        bin.network.setBandwidth(1760);
                    } else if (name.equals("SigFox")) {
                        bin.network.setBandwidth(48);
                    }
                }

                mgmt.performAllocation2Stage();
                double objectiveScore = mgmt.getObjectiveScore();

                System.out.println("capRatio: " + capRatio);
                System.out.println("flowRatio: " + flowRatio);
                System.out.println("Objective Score: " + objectiveScore);
                final var numFlows = mgmt.printAllAllocations();

                // Check if this is the best pair of values
                if (objectiveScore > bestObjectiveScore) {
                    bestCapRatio = capRatio;
                    bestFlowRatio = flowRatio;
                    bestObjectiveScore = objectiveScore;
                    bestNumFlows = numFlows;
                }
            }
        }

        // Print the best pair of values and the corresponding objective score
        System.out.println("Best capRatio: " + bestCapRatio);
        System.out.println("Best flowRatio: " + bestFlowRatio);
        System.out.println("Best Objective Score: " + bestObjectiveScore);
        System.out.println("Number of flows: " + bestNumFlows);
    }
}