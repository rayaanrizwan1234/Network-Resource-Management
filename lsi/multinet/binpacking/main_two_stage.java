package lsi.multinet.binpacking;

import java.io.FileWriter;

import lsi.multinet.*;
import lsi.multinet.benchmarks.*;

public class main_two_stage {

    static void writeCsv(int nCrit, int[] networks) {
        try {
            FileWriter writer = new FileWriter("Network-Resource-Management/networks.csv");
            writer.append(String.valueOf(nCrit));
            for (int i = 0; i < networks.length; i++) {
                writer.append("\n");
                writer.append(String.valueOf(networks[i]));
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Parameters for the generator
        int nflows = 800;
        double minPeriod = 5.0;
        double maxPeriod = 120.0;
        double targetUtil = 30;
        int nCrit = 2;
        double hasCritFactor = 0.8;
        double minCritFactor = 0.4;
        double maxCritFactor = 0.8;
        int seed = 3;

        int[] networks = { 6000, 6000, 4000};

        writeCsv(nCrit, networks);

        // Create an instance of the generator
        SyntheticBenchmarkGenerator generator = new SyntheticBenchmarkGenerator(
            nflows, minPeriod, maxPeriod, targetUtil, nCrit, hasCritFactor, minCritFactor, maxCritFactor, seed
        );

        double capRatio = 0.36;
        double flowRatio = 0.13;

        // Loop over possible values of capRatio and flowRatio
        Benchmark benchmark = generator.generateCSV("Network-Resource-Management/message_flows.csv");
        // IoTEdgeAssistedLiving2020 benchmark = new IoTEdgeAssistedLiving2020();

        // Create networks with the current capRatio
        Network wifi = new Network(networks[0] * capRatio, 0);
        wifi.setName("WiFi");
        benchmark.addNetwork(wifi);

        Network lora = new Network(networks[1] * capRatio, 0); // LoRa SF9
        lora.setName("LoRa");
        benchmark.addNetwork(lora);

        Network sigfox = new Network(networks[2] * capRatio, 0);
        sigfox.setName("SigFox");
        benchmark.addNetwork(sigfox);

        // Network nbIot = new Network(networks[3] * capRatio, 0);
        // nbIot.setName("NB-IoT");
        // benchmark.addNetwork(nbIot);

        // Network lte = new Network(networks[4] * capRatio, 0);
        // lte.setName("LTE");
        // benchmark.addNetwork(lte);

        CABFTwoStage mgmt;

        mgmt = new CABFTwoStage(nCrit);
        mgmt.setDecreasing(false);
        mgmt.setBestFit();

        benchmark.clone().setManagement(mgmt);

        mgmt.populateCriticalityLists();
        mgmt.splitMessageFlows(flowRatio);

        mgmt.performAllocation();

        // // Restore full capacity for the second stage
        for (NetworkBin bin : mgmt.bins) {
            final var name = bin.network.getName();
            if (name.equals("WiFi")) {
                bin.network.setBandwidth(networks[0]);
            } else if (name.equals("LoRa")) {
                bin.network.setBandwidth(networks[1]);
            } else if (name.equals("SigFox")) {
                bin.network.setBandwidth(networks[2]);
            }
        }

        mgmt.performAllocation2Stage();
        final var numFlows = mgmt.printAllAllocations();

        double objectiveScore = mgmt.getObjectiveScore();

        System.out.println("capRatio: " + capRatio);
        System.out.println("flowRatio: " + flowRatio);
        System.out.println("Objective Score: " + objectiveScore);
        mgmt.getNetworkCost();

    }
}