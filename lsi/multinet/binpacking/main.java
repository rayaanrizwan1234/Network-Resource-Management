package lsi.multinet.binpacking;

import java.io.FileWriter;

import lsi.multinet.*;
import lsi.multinet.benchmarks.*;

public class main {

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
        int nflows = 1000;
        double minPeriod = 30.0;
        double maxPeriod = 120.0;
        double targetUtil = 10;
        int nCrit = 3;
        double hasCritFactor = 0.8;
        double minCritFactor = 0.4;
        double maxCritFactor = 0.8;
        int seed = 200;

        int[] networks =  {4000, 1760, 1000};
        writeCsv(nCrit, networks);

        // Create an instance of the generator
        SyntheticBenchmarkGenerator generator = new SyntheticBenchmarkGenerator(
            nflows, minPeriod, maxPeriod, targetUtil, nCrit, hasCritFactor, minCritFactor, maxCritFactor, seed
        );

        // Loop over possible values of capRatio and flowRatio
        Benchmark benchmark = generator.generateCSV("Network-Resource-Management/message_flows.csv");
        // IoTEdgeAssistedLiving2020 benchmark = new IoTEdgeAssistedLiving2020();

        // Create networks with the current capRatio
        Network wifi = new Network(networks[0], 0);
        wifi.setName("WiFi");
        benchmark.addNetwork(wifi);

        Network lora = new Network(networks[1], 0); // LoRa SF9
        lora.setName("LoRa");
        benchmark.addNetwork(lora);

        Network sigfox = new Network(networks[2], 0);
        sigfox.setName("SigFox");
        benchmark.addNetwork(sigfox);

        // Network nbIot = new Network(networks[3], 0);
        // nbIot.setName("NB-IoT");
        // benchmark.addNetwork(nbIot);

        // Network lte = new Network(networks[4], 0);
        // lte.setName("LTE");
        // benchmark.addNetwork(lte);
        
        // Network fiveG = new Network(networks[5], 0);
        // fiveG.setName("5G");
        // benchmark.addNetwork(fiveG);

        // Network sat = new Network(networks[6], 0);
        // sat.setName("Satellite");
        // benchmark.addNetwork(sat);

        long startTime = System.nanoTime();

        CriticalityAwareUtilisationBasedMultiNetworkManagement mgmt;

        mgmt = new CriticalityAwareUtilisationBasedMultiNetworkManagement(nCrit);
        mgmt.setDecreasing(false);
        mgmt.setBestFit();

        benchmark.clone().setManagement(mgmt);


        mgmt.performAllocation();

        long endTime = System.nanoTime();


        System.out.println("Normal CABF ======================");


        System.out.println("Objective Score: " + mgmt.getObjectiveScore());
        System.out.println("Number of Flows: " + mgmt.printAllAllocations());
        System.out.println("Execution Time: " + (endTime - startTime) / 1000000 + "ms");

        // // mgmt.getNetworkCost();

        // // Two Stage CABF =====================================

        // System.out.println("Two Stage CABF ======================");

        // double capRatio = 0.36;
        // double flowRatio = 0.23;
        // double[] triggerValues = { 0.5, 0.55, 0.65,
        //     0.7, 0.75, 0.8, 0.85, 0.9, 0.95 };

        // double[] flowValues = {0.65, 0.7, 0.75, 0.8};
        // double[] capValues = {0.8, 0.85, 0.95};

        // double bestCapRatio = 0;
        // double bestFlowRatio = 0;
        // double bestObjectiveScore = Double.NEGATIVE_INFINITY;
        // double averageCriticality = 0;
        // int numFlows = 0;

        // Long totalTime = 0L;

        // // // for (double capr: triggerValues) {
        // // //     for (double flowr: triggerValues) {
        // for (double capr = 0.0; capr < 1; capr += 0.05) {
        //     for (double flowr = 0.0; flowr < 1; flowr += 0.05) {
        //         CABFTwoStage mgmt2;

        //         mgmt2 = new CABFTwoStage(nCrit);
        //         mgmt2.setDecreasing(false);
        //         mgmt2.setBestFit();
        
        //         benchmark.clone().setManagement(mgmt2);
        
        //         mgmt2.populateCriticalityLists();
        //         mgmt2.splitMessageFlows(flowr);
        
        //         for (int i = 0; i < networks.length; i++) {
        //             NetworkBin bin = mgmt2.bins.get(i);
        //             bin.network.setBandwidth(networks[i] * capr);
        //         }

        //         Long startTime2 = System.nanoTime();
        
        //         mgmt2.performAllocation();
        
        //         // Restore full capacity for the second stage
        //         for (int i = 0; i < networks.length; i++) {
        //             NetworkBin bin = mgmt2.bins.get(i);
        //             bin.network.setBandwidth(networks[i]);
        //         }
        
        //         mgmt2.performAllocation2Stage();

        //         Long endTime2 = System.nanoTime();

        //         totalTime += (endTime2 - startTime2) / 1000000; 
        
        //         double objectiveScore = mgmt2.getObjectiveScore();
        
        //         System.out.println("capRatio: " + capr);
        //         System.out.println("flowRatio: " + flowr);
        //         // System.out.println("Objective Score: " + objectiveScore);

        //         if (objectiveScore >= bestObjectiveScore) {
        //             bestObjectiveScore = objectiveScore;
        //             bestCapRatio = capr;
        //             bestFlowRatio = flowr;
        //             averageCriticality = mgmt2.getAverageCriticality();
        //             numFlows = mgmt2.printAllAllocations();
        //         }
        //     }
        // }


        // System.out.println("Best capRatio: " + bestCapRatio);
        // System.out.println("Best flowRatio: " + bestFlowRatio);
        // System.out.println("Best Objective Score: " + bestObjectiveScore);
        // System.out.println("Number of Flows: " + numFlows);
        // System.out.println("Execution Time: " + totalTime + "ms");
        // System.out.println("Average Criticality: " + averageCriticality);
    }
}