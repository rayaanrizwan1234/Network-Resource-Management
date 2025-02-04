package lsi.multinet.binpacking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import lsi.multinet.Network;
import lsi.multinet.binpacking.exceptions.BinFullException;
import lsi.multinet.binpacking.exceptions.DuplicateElementException;

public class NetworkBin implements Bin{

	ArrayList<Element> allocated;
	Network network;
	
	public NetworkBin(Network network) {
		this.network=network;
		allocated = new ArrayList<Element>();
	}
	
	public Network getNetwork(){
		
		return network;
	}
	

	@Override
	public void addElement(Element element) throws BinFullException,
			DuplicateElementException {
		
        boolean isDuplicate = allocated.contains(element);
        if (isDuplicate) {
            throw new DuplicateElementException("Bin "+this.getId()+" already contains the element " + element.getId());
        }
        if (!element.fitsInto(getFreeSpace())) {

            throw new BinFullException("Bin "+this.getId()+" is full, can't add "+element.getId());
     
        }
        else{
        	allocated.add(element);
        }
	}

	public void removeElement(Element element)  {
		allocated.remove(element);

	}
	
	@Override
	public boolean contains(Element element) {

		return allocated.contains(element);
	}

	@Override
	public Size getCapacity() {

		return new DoubleValueSize(network.getBandwidth());
		
		
	}

	@Override
	public int getElementCount() {

		return allocated.size();
	}

	@Override
	public Set getElements() {
		
		return new HashSet(allocated);
	}

	
	@Override
	public double getFillLevel() {
		
		return 1 - getCapacity().calculatePercentage(getFreeSpace());
	}

	@Override
	public Size getFreeSpace() {

		return getCapacity().subtract(getAllocatedSize());
		
	}

	@Override
	public String getId() {
		return network.getName();
	}

	
	protected Size getAllocatedSize(){
		
		Size usage= new DoubleValueSize(0.0);
		
        for (Element element: allocated) {
            
        		usage = usage.add(element.getSize());
        }
		
		return usage;
		
		
	}
	
	private void printAllocated(){
		
        for (Element element: allocated) {
            
    		System.out.println(element.getId()+ " "+element.getSize());
        }
		
	}
	
	
}
