package lsi.multinet.binpacking;

import lsi.multinet.benchmarks.IoTEdgeAssistedLiving2020;

public class StaticMultiNetworkManagementTest {


	public static void main(String[] args){



		IoTEdgeAssistedLiving2020 benchmark = new IoTEdgeAssistedLiving2020();
		StaticMultiNetworkManagement mgmt;


		int[] map = {2,2,2,0,2,1,2,1};
		int[] cri = {0,0,1,1,1,1,1,0};

		mgmt = new StaticMultiNetworkManagement(map, cri);

		benchmark.clone().setManagement(mgmt);

		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
	
		System.out.println("Q="+mgmt.getQuality());
		
		System.out.println("------------------------------------");
		
		

		int[] map1 = {2,1,2,2,1,0,0,2};
		int[] cri1 = {1,0,0,1,0,0,0,0};
		
		mgmt = new StaticMultiNetworkManagement(map1, cri1);

		benchmark.clone().setManagement(mgmt);

		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		
		System.out.println("Q="+mgmt.getQuality());
		
		
		System.out.println("------------------------------------");
		
		

		int[] map2 = {0,0,0,0,0,0,0,0};
		int[] cri2 = {0,0,0,0,0,0,0,0};
		
		mgmt = new StaticMultiNetworkManagement(map2, cri2);

		benchmark.clone().setManagement(mgmt);

		System.out.println(mgmt.performAllocation());
		mgmt.printAllAllocations();
		mgmt.printUnallocatedElements();
		
		System.out.println("Q="+mgmt.getQuality());
		System.out.println(mgmt.getMapping());
		System.out.println(mgmt.getCriticalities());
		
		
	}
}
