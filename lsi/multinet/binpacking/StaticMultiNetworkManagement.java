package lsi.multinet.binpacking;

import lsi.multinet.Configuration;
import lsi.multinet.binpacking.exceptions.BinFullException;
import lsi.multinet.binpacking.exceptions.DuplicateElementException;



public class StaticMultiNetworkManagement extends UtilisationBasedMultiNetworkManagement{

	public boolean feasible;
	boolean fullyAllocated;

	double averageCrit;
	int allocatedFlows;
	int[] mapping, crit;	

	public StaticMultiNetworkManagement(int[] mapping, int[] criticality){
		super();
		this.mapping = mapping.clone();
		this.crit = criticality.clone();

	}
	
	/**
	 * @return the mapping
	 */
	public int[] getMapping() {
		return mapping;
	}

	/**
	 * @param mapping the mapping to set
	 */
	public void setMapping(int[] mapping, int[] crit) {
		this.mapping = mapping;
	}
	
	
	/**
	 * @return the criticality list
	 */
	public int[] getCriticalities() {
		return crit;
	}

	/**
	 * @param crit the crit to set
	 */
	public void setCriticalities(int[] crit) {
		this.crit = crit;
	}

	public boolean performAllocation() {
		
		feasible=true;
		fullyAllocated=true;
		
		for(int i=0;i<mapping.length;i++){
			
			elements.get(i).setAllocatedCritLevel(crit[i]);
			if(!elements.get(i).getMessageFlow().hasCriticality(crit[i])){
//				System.out.println(elements.get(i).getId()+" does not have crit "+crit[i]);
				feasible=false;
				return false;
			}
			
			try {
				allocate(elements.get(i),bins.get(mapping[i]));
			} catch (BinFullException e) {
				
				unallocatedElements.add(elements.get(i));
				mapping[i]=-1;
				fullyAllocated=false;

			} catch (DuplicateElementException e){
				e.printStackTrace();
			}
			
		}
		
		return fullyAllocated;
		
	}
	
	
	public int getQuality(){
		
		if(!feasible)return 0;
		
		int quality=0;
		
		for(int i=0;i<mapping.length;i++){
			
			if(mapping[i]!=-1){
				
				quality = quality + Configuration.CriticalityLevels-(3*crit[i]);
				
			}
			
		}
		
		
		return quality;
		
	}
	
	
}
