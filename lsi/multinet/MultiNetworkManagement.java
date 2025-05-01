package lsi.multinet;

import java.util.ArrayList;

/*
 *  
 * @author      Leandro Soares Indrusiak
 * @version 1.0 (York, 29/07/2020) 
 * 
 * 
 * Abstract class defining the main functionality of a multi-network management framework.
 * It provides storage for all available networks, all registered application message flows, and the currently 
 * valid set of allocations. It provides all methods to create, store and access those model elements, but 
 * leaves the actual functionality of creating allocations to the subclasses (which must implement the performAllocation
 * method).
 * 
 * 
 */


public abstract class MultiNetworkManagement {

	protected ArrayList<Network> networks;
	protected ArrayList<MessageFlow> flows;
	protected ArrayList<Allocation> allocations;
	
	
	public MultiNetworkManagement(){
		
		networks = new ArrayList<Network>();
		flows = new ArrayList<MessageFlow>();
		allocations = new ArrayList<Allocation>();
	}
	
	
	protected void allocate(MessageFlow messageFlow, Network network, int critLevel){
		
		deallocate(messageFlow);
		
		allocations.add(new Allocation(messageFlow, network, critLevel));
		
	}
	
	protected void deallocate(MessageFlow messageFlow){
		
		
		Allocation old = getAllocation(messageFlow);
		if(old!=null) allocations.remove(old);
		
		
	}
	
	public Network getNetwork(MessageFlow messageFlow){
		
		Allocation current = getAllocation(messageFlow);
		if (current==null) return null;
		else return current.getNetwork();
		
		
	}
	
	public int getCriticalityLevel(MessageFlow messageFlow){
		
		Allocation current = getAllocation(messageFlow);
		if (current==null) return -1;
		else return current.getCritLevel();
		
		
	}
	
	
	public boolean isAllocated(MessageFlow messageFlow){
		
		Allocation current = getAllocation(messageFlow);
		if (current==null) return false;
		else return true;
		
	}
	
	public ArrayList<MessageFlow> getAllAllocatedFlows(){
		
		ArrayList<MessageFlow> allocatedFlows = new ArrayList<MessageFlow>();
		
		for(Allocation a: allocations){
		
			allocatedFlows.add(a.getFlow());
			
		}

		return flows;
		
		
	}
	
	
	public ArrayList<MessageFlow> getAllocatedFlows(Network network){
		
		ArrayList<Allocation> allocs = getAllocation(network);
		
		ArrayList<MessageFlow> flows = new ArrayList<MessageFlow>();
		
		for(int i=0;i<allocs.size();i++){
			
			if(allocs.get(i).getNetwork() == network) flows.add(allocs.get(i).getFlow());
			
		}
		
		
		
		return flows;
		
		
	}
	
	
	protected Allocation getAllocation(MessageFlow messageFlow){
		
		
		for(int i=0;i<allocations.size();i++){
			
			if(allocations.get(i).getFlow() == messageFlow) return allocations.get(i);
			
		}
		
		
		return null;
		
	} 
	
	protected ArrayList<Allocation> getAllocation(Network network){
		
		ArrayList<Allocation> allocs = new ArrayList<Allocation>();
		
		for(int i=0;i<allocations.size();i++){
			
			if(allocations.get(i).getNetwork() == network) allocs.add(allocations.get(i));
			
		}
		
		
		return allocs;
		
	} 
	
	
	public void addMessageFlow(MessageFlow flow){
		
		flows.add(flow);
		
	}
	
	public void addNetwork(Network network){
		
		networks.add(network);
		
	}



	/* To be implemented by subclasses, should consider existing networks and
	 * create allocations for each of the existing message flows. 
	 *
	 * @return whether the allocation was successful
	 * 
	 */
	
	public abstract boolean performAllocation();

	
	
	public int printAllAllocations(){
		
        // for (Allocation alloc: allocations) {
            
    	// 	System.out.println(alloc.toString());
        // }
        
		// System.out.println("Total number of allocations: "+allocations.size());
		return allocations.size();
	}
	

}
