package lsi.multinet.binpacking;

import lsi.multinet.benchmarks.IoTEdgeAssistedLiving2020;

public class CriticalityAwareUtilisationBasedMultiNetworkManagementTest {

	
	public static void main(String[] args){
		
		
		
		IoTEdgeAssistedLiving2020 benchmark = new IoTEdgeAssistedLiving2020();
		CriticalityAwareUtilisationBasedMultiNetworkManagement mgmt;
		
		System.out.println("-------------------------------------------");
		System.out.println("------------------CABF---------------------");

		
		mgmt = new CriticalityAwareUtilisationBasedMultiNetworkManagement();
		mgmt.setDecreasing(false);
		mgmt.setBestFit();
		
		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performInvertedAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		
		
	}
	
	
	
}
