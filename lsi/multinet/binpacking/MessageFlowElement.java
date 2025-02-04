package lsi.multinet.binpacking;


import lsi.multinet.MessageFlow;

public class MessageFlowElement implements Element {

	/**
	 * 
	 */

	
	int allocatedCritLevel;
	MessageFlow flow;
	
	public MessageFlowElement(MessageFlow flow, int critLevel) {
		this.flow = flow;
		this.allocatedCritLevel=critLevel;
		
	}
	
	public MessageFlow getMessageFlow(){
		
		return flow;
		
	}
	

	@Override
	public boolean fitsInto(Size arg) {

		if(getSize().compareTo(arg)<=0) return true;
		
		return false;
	}

	@Override
	public String getId() {

		return flow.getName();
	}

	@Override
	public Size getSize() {

		return new DoubleValueSize(flow.getBandwidthUtilisation(allocatedCritLevel));
		
		
	}

	/**
	 * @return the allocatedCritLevel
	 */
	public int getAllocatedCritLevel() {
		return allocatedCritLevel;
	}

	/**
	 * @param allocatedCritLevel the allocatedCritLevel to set
	 */
	public void setAllocatedCritLevel(int allocatedCritLevel) {
		this.allocatedCritLevel = allocatedCritLevel;
	}

	
	
	@Override
	public String toString(){
		
		return flow.getName()+" "+super.toString();
		
	}
	
	
}
