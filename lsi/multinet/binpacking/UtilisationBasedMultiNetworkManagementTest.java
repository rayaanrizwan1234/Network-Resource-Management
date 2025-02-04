package lsi.multinet.binpacking;

import lsi.multinet.benchmarks.IoTEdgeAssistedLiving2020;

public class UtilisationBasedMultiNetworkManagementTest {

	
	public static void main(String[] args){
		
		
		
		IoTEdgeAssistedLiving2020 benchmark = new IoTEdgeAssistedLiving2020();
		UtilisationBasedMultiNetworkManagement mgmt;
		
		System.out.println("-------------------------------------------");
		System.out.println("------------------L-LF---------------------");

		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setDecreasing(false);
		
		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		
		System.out.println("-------------------L-LFD--------------------");
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setDecreasing(true);
		

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();


		System.out.println("----------------H-LF-----------------------");
		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setHighest(true);
		mgmt.setDecreasing(false);
		

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		


		System.out.println("-----------------H-LFD---------------------");
		
		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setHighest(true);
		mgmt.setDecreasing(true);
		

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();


		System.out.println("-------------------------------------------");
		System.out.println("------------------L-FF---------------------");

		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setDecreasing(false);
		mgmt.setFirstFit();
		
		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		
		System.out.println("-------------------L-FFD--------------------");
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setDecreasing(true);
		mgmt.setFirstFit();
		

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();


		System.out.println("----------------H-FF-----------------------");
		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setHighest(true);
		mgmt.setDecreasing(false);
		mgmt.setFirstFit();
		

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		


		System.out.println("-----------------H-FFD---------------------");
		
		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setHighest(true);
		mgmt.setDecreasing(true);
		mgmt.setFirstFit();

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();

		System.out.println("-------------------------------------------");
		System.out.println("------------------L-WF---------------------");

		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setDecreasing(false);
		mgmt.setWorstFit();
		
		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		
		System.out.println("-------------------L-WFD--------------------");
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setDecreasing(true);
		mgmt.setWorstFit();
		

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();


		System.out.println("----------------H-WF-----------------------");
		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setHighest(true);
		mgmt.setDecreasing(false);
		mgmt.setWorstFit();
		

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		


		System.out.println("-----------------H-WFD---------------------");
		
		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setHighest(true);
		mgmt.setDecreasing(true);
		mgmt.setWorstFit();

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		
		System.out.println("-------------------------------------------");
		System.out.println("------------------L-BF---------------------");

		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setDecreasing(false);
		mgmt.setBestFit();
		
		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		
		System.out.println("-------------------L-BFD--------------------");
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setDecreasing(true);
		mgmt.setBestFit();
		

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();


		System.out.println("----------------H-BF-----------------------");
		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setHighest(true);
		mgmt.setDecreasing(false);
		mgmt.setBestFit();
		

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		


		System.out.println("-----------------H-BFD---------------------");
		
		
		mgmt = new UtilisationBasedMultiNetworkManagement();
		mgmt.setHighest(true);
		mgmt.setDecreasing(true);
		mgmt.setBestFit();

		benchmark.clone().setManagement(mgmt);
		
		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		
	}
	
	
	
}
