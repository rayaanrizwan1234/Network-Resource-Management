package lsi.multinet.binpacking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import lsi.multinet.Allocation;
import lsi.multinet.MessageFlow;
import lsi.multinet.MultiNetworkManagement;
import lsi.multinet.Network;
import lsi.multinet.binpacking.exceptions.BinFullException;
import lsi.multinet.binpacking.exceptions.DuplicateElementException;


/*
 *  
 * @author      Leandro Soares Indrusiak
 * @version 1.0 (York, 04/08/2020) 
 * @version 1.0.1 (York, 09/09/2020)
 * @version 1.1 (York, 15/09/2020)
 *  
 * 
 * Simple utilisation-based multi-network manager. Given the bandwidth provided by all available networks, 
 * it allocates message flows to networks using their network utilisation to guide classic bin packing algorithms. 
 * 
 */


public class UtilisationBasedMultiNetworkManagement extends MultiNetworkManagement {
	
	
	ArrayList<MessageFlowElement> elements;
	ArrayList<MessageFlowElement> unallocatedElements;
	ArrayList<NetworkBin> bins;
	boolean decreasing = false;
	boolean highest = false;
	boolean best = false;
	boolean worst = false;
	boolean first = false;


	public UtilisationBasedMultiNetworkManagement(){
		 
		 super();
		 elements = new ArrayList<MessageFlowElement>();
		 unallocatedElements = new ArrayList<MessageFlowElement>();
		 bins = new ArrayList<NetworkBin>();
		 
	}
	
	 
	public void addMessageFlow(MessageFlow flow){
		super.addMessageFlow(flow);
		
		if(highest){elements.add(new MessageFlowElement(flow, flow.getHighestCriticality()));}
		else{elements.add(new MessageFlowElement(flow, flow.getLowestCriticality()));}
		
	}
	
	public void addNetwork(Network network){
		
		super.addNetwork(network);
		bins.add(new NetworkBin(network));
		
	}

	
	
	public Size getLargestFreeSpace(){
		
		Size largest=null;
		
		for (Iterator iterator1 = bins.iterator(); iterator1.hasNext();) {
            Bin currentBin = (Bin) iterator1.next();
            
            if(largest==null){largest = currentBin.getFreeSpace();}
            else if	(largest.compareTo(currentBin.getFreeSpace())< 0){
            	largest = currentBin.getFreeSpace();
            }
		
		}
		return largest;
		
	}
	
	public Bin getLargestBin(){
		
		Bin largest=null;
		 
		for (Iterator iterator1 = bins.iterator(); iterator1.hasNext();) {
            Bin currentBin = (Bin) iterator1.next();
            
            if(largest==null){largest = currentBin;}
            else if	(largest.getCapacity().compareTo(currentBin.getCapacity())< 0){
            	largest = currentBin;
            }
		
		}
		return largest;
	}
	
	
	/**
	 * @return if allocation is performed in decreasing order of size
	 */
	public boolean isDecreasing() {
		return decreasing;
	}


	/**
	 * @param decreasing defines whether or not the allocation is performed in decreasing order of size
	 */
	public void setDecreasing(boolean decreasing) {
		this.decreasing = decreasing;
	}


	/**
	 * @return the highest
	 */
	public boolean isHighest() {
		return highest;
	}


	/**
	 * @param highest the highest to set
	 */
	public void setHighest(boolean highest) {
		this.highest = highest;
	}


	@Override
	public boolean performAllocation() {
		
		if(decreasing){
			
			this.sortMessageFlowElementByBandwidthUtilisation(elements, !decreasing);
		}
		

		
        for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
        	
        	
        	
        	MessageFlowElement element = (MessageFlowElement) iterator.next();

        	performAllocationStep(element);		
        	
        }
		
		if(unallocatedElements.size()>0) return false;
		
		return true;
		
	} 
	 
	
	
	protected boolean performAllocationStep(MessageFlowElement element){
		
		Size binCapacity = getLargestBin().getCapacity();
		
		if (!element.fitsInto(binCapacity)) { // the element is too large for our bins
        	unallocatedElements.add(element);
        } else {

            Bin matchingBin = null;
            Bin currentBin;
        
            Size largestSpace = binCapacity.createZeroInstance(); // start using zero space
            Size lowestSpace = binCapacity; // start using largest
            
            // for each existing bin
            for (Iterator iterator1 = bins.iterator(); iterator1.hasNext();) {
            	
        		currentBin = (Bin) iterator1.next();
        		Size currentFreeSpace = currentBin.getFreeSpace();
        		
            	if(worst){ // worst fit
            		

                    
            		// current free space is the largest and element fits the bin
            		if (element.fitsInto(currentFreeSpace) & currentFreeSpace.compareTo(largestSpace) > 0) { // the current element fits into the current space
            			largestSpace = currentFreeSpace; // memorize the bin's space
            			matchingBin = currentBin;  // memorize the bin itself
            		}
                
            	}
            	else if(best){ // best fit
            	

            		// current free space is the smallest and element fits the bin
                    if (element.fitsInto(currentFreeSpace) & currentFreeSpace.compareTo(lowestSpace) <= 0) { // the current element fits into the current space
                        lowestSpace = currentFreeSpace;  // memorize the bin's space
                        matchingBin = currentBin; // memorize the bin itself
                    }
             		
            	}
            	else if(first){	// first fit
            		
            		if (matchingBin==null & element.fitsInto(currentFreeSpace)){
            			matchingBin = currentBin;
            		}
            		
            	}
            	else{ // last fit
            		
            		if (element.fitsInto(currentFreeSpace)){
            			
            			matchingBin = currentBin;
            			//System.out.println(element.getId()+ " fits into "+matchingBin.getId());
            		}
            	}
            	
                
            }
            if (matchingBin == null || !element.fitsInto(matchingBin.getFreeSpace())) { // element does not fit into any bin
                unallocatedElements.add(element);
            }
            else{
            	
            	
            	try{
            		

            		MessageFlowElement mfe = ((MessageFlowElement)element);
            		NetworkBin nb = ((NetworkBin)matchingBin);
            		allocate(mfe,nb);
            		return true;
            		
            	}
            	catch(DuplicateElementException | BinFullException e){
            		
            		System.out.println(e);
            		return false;
            		
            	}
            }
        }
		
		return false;
		
	}
	
	
	protected void allocate(MessageFlowElement messageFlow, NetworkBin network) throws BinFullException, DuplicateElementException{
		
		network.addElement(messageFlow);
		allocate(messageFlow.getMessageFlow(),network.getNetwork(), messageFlow.getAllocatedCritLevel());

//		System.out.println("allocated "+messageFlow.getMessageFlow().getName()+"  crit="+messageFlow.getAllocatedCritLevel() + "   bw="+messageFlow.getSize()+ "   nw="+network.getId());		
		
	}
	
	protected void deallocate(MessageFlowElement messageFlow) {
		
		
		NetworkBin network = (NetworkBin) getBin(messageFlow);
		if(network!=null){
			network.removeElement(messageFlow);
			deallocate(messageFlow.getMessageFlow());
		}
		//System.out.println("deallocated "+messageFlow.getId()+"  from network "+network.getId());		

	}

	
	protected ArrayList getAllAllocatedElements(){
		
		ArrayList allocatedElements = new ArrayList();
		for(NetworkBin netb: bins){
			
			allocatedElements.addAll(netb.getElements());
		}
		
		return allocatedElements;
	}
	
	
	protected Bin getBin(Element element){
		for (Iterator iterator1 = bins.iterator(); iterator1.hasNext();) {
            Bin currentBin = (Bin) iterator1.next();
            
            if(currentBin.contains(element)){return currentBin;}
		}
		return null;
		
	}
	
	

	
	
	public void printUnallocatedElements(){
		
        for (Element el: unallocatedElements) {
            
    		System.out.println(el.toString());
        }
        
	}
	
	
	public void sortMessageFlowElementByBandwidthUtilisation(ArrayList<MessageFlowElement> list, boolean ascending){
		
		
		list.sort(new SortbyBandwidthUtilisation(ascending));
		
	}
	
	

	class SortbyBandwidthUtilisation implements Comparator<MessageFlowElement> 
	{ 
		boolean ascending;
		int crit;
		
		public SortbyBandwidthUtilisation(boolean ascending){
			
			this.ascending=ascending;

			
		}
		// Used for sorting according to bandwidth utilisation 
	    public int compare(MessageFlowElement a, MessageFlowElement b) 
	    { 
	      
	    	
	    	Size ba = a.getSize();
	    	Size bb = b.getSize();
	    	
	    	if(ascending){ return ba.compareTo(bb);}
	    	else{return bb.compareTo(ba);}
	    	  
	    } 
	} 
	
	
	
	public boolean isFirstFit(){return first;}
	public boolean isBestFit(){return best;}
	public boolean isWorstFit(){return worst;}

	public void setFirstFit(){first=true; worst=false; best=false;}
	public void setBestFit(){first=false; worst=false; best=true;}
	public void setWorstFit(){first=false; worst=true; best=false;}
	
	
	public double getAllocatedPercentage(){
		
		int total = flows.size();
		int allocated = allocations.size();

System.out.println("%="+allocated+"/"+total+"="+(double)allocated/total);		
		return (double) allocated/total;
		
	}
	
	
	public double getAverageCriticality(){
		
		int totalCrit=0;
		for(Allocation a: allocations){
			
			totalCrit = totalCrit+a.getCritLevel();
		}

		
System.out.println("avg= "+totalCrit+ "/"+allocations.size()+"="+(double)totalCrit/allocations.size());				
		return (double) totalCrit/allocations.size();
		
	}
	
	
	public double getAverageCriticalityPercentage(){
		
		int totalCrit=0;
		int maxCrit=0;
		for(Allocation a: allocations){
			
			totalCrit = totalCrit+a.getCritLevel();
		}

		for(MessageFlow f: flows){
			
			if(f.getHighestCriticality()>maxCrit){maxCrit=f.getHighestCriticality();}
		}
		
		if(maxCrit==0) return 1.0;
		
		return (double)((double) totalCrit/allocations.size())/maxCrit;
		
	}
	
	
}
