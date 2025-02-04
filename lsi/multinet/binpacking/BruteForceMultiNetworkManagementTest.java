package lsi.multinet.binpacking;

import lsi.multinet.benchmarks.IoTEdgeAssistedLiving2020;

public class BruteForceMultiNetworkManagementTest {

	
	public static void main(String[] args){
		
		
		
		IoTEdgeAssistedLiving2020 benchmark = new IoTEdgeAssistedLiving2020();
		BruteForceMultiNetworkManagement mgmt;
		
		System.out.println("-------------------------------------------");
		System.out.println("------------------BruteForce---------------------");

		
		mgmt = new BruteForceMultiNetworkManagement(benchmark);
		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());

	}
}
