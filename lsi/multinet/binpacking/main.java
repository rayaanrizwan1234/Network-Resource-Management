package lsi.multinet.binpacking;

import lsi.multinet.*;
import lsi.multinet.benchmarks.*;

public class main {
    public static void main(String[] args) {
        // Parameters for the generator
        int nflows = 5000;
        double minPeriod = 5.0;
        double maxPeriod = 120.0;
        double targetUtil = 30;
        int nCrit = 3;
        double hasCritFactor = 0.8;
        double minCritFactor = 0.4;
        double maxCritFactor = 0.8;
        int seed = 3;

        // Create an instance of the generator
        SyntheticBenchmarkGenerator generator = new SyntheticBenchmarkGenerator(
            nflows, minPeriod, maxPeriod, targetUtil, nCrit, hasCritFactor, minCritFactor, maxCritFactor, seed
        );

        double capRatio = 1.0;
        double flowRatio = 1.0;

        // Loop over possible values of capRatio and flowRatio
        Benchmark benchmark = generator.generateCSV("Network-Resource-Management/message_flows.csv");
        // IoTEdgeAssistedLiving2020 benchmark = new IoTEdgeAssistedLiving2020();

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

        mgmt = new CriticalityAwareUtilisationBasedMultiNetworkManagement(nCrit);
        mgmt.setDecreasing(false);
        mgmt.setBestFit();

        benchmark.clone().setManagement(mgmt);

        // mgmt.populateCriticalityLists();
        // mgmt.splitMessageFlows(flowRatio);

        mgmt.performAllocation();

        // // Restore full capacity for the second stage
        // for (NetworkBin bin : mgmt.bins) {
        //     final var name = bin.network.getName();
        //     if (name.equals("WiFi")) {
        //         bin.network.setBandwidth(64000);
        //     } else if (name.equals("LoRa")) {
        //         bin.network.setBandwidth(1760);
        //     } else if (name.equals("SigFox")) {
        //         bin.network.setBandwidth(48);
        //     }
        // }

        // mgmt.performAllocation2Stage();
        final var numFlows = mgmt.printAllAllocations();

        double objectiveScore = mgmt.getObjectiveScore();

        // System.out.println("capRatio: " + capRatio);
        // System.out.println("flowRatio: " + flowRatio);
        System.out.println("Objective Score: " + objectiveScore);
        mgmt.getNetworkCost();

    }
}