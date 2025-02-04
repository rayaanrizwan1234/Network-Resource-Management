package lsi.multinet;

/*
 *  
 * @author      Leandro Soares Indrusiak
 * @version 1.0 (York, 29/07/2020) 
 * 
 * 
 * Instances of this class represent the allocation of a message flow to a specific network
 * at a well-defined criticality level. Once granted, allocations do not change, so there are no
 * methods to set any attributes. They are set only once, upon instantiation.
 * 
 */



public class Allocation {

	final MessageFlow flow;
	final Network net;
	final int critLevel;
	
	public Allocation(MessageFlow messageFlow, Network network, int critLevel){
		
		this.flow = messageFlow;
		this.net = network;
		this.critLevel = critLevel;
		
	}
	
	/**
	 * @return the flow
	 */
	public MessageFlow getFlow() {
		return flow;
	}

	/**
	 * @return the net
	 */
	public Network getNetwork() {
		return net;
	}

	/**
	 * @return the critLevel
	 */
	public int getCritLevel() {
		return critLevel;
	}	
	
	
	public String toString(){
		
		return net.getName()+"<-"+getCritLevel()+"-"+flow.getName();
		
	}
	
}
