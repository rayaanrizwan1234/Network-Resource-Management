package lsi.multinet.binpacking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lsi.multinet.MultiNetworkManagement;
import lsi.multinet.benchmarks.Benchmark;
import lsi.multinet.binpacking.exceptions.BinFullException;
import lsi.multinet.binpacking.exceptions.DuplicateElementException;

public class BruteForceMultiNetworkManagement extends UtilisationBasedMultiNetworkManagement{

	ArrayList<int[]> mappings;
	ArrayList<int[]> crits;
	Benchmark bench;
	
	public BruteForceMultiNetworkManagement(Benchmark b){
		
		mappings = new ArrayList<int[]>();
		crits = new ArrayList<int[]>();
		bench = b;
		
	}
	
	
	@Override
	public boolean performAllocation() {

		
		
		initialiseArrays();
		
		int bestQuality=0;
		int[] bestmap = null;
		int[] bestcrit = null;


		
		for(int i=0;i<mappings.size();i++){ // for each possible mapping

			
			for(int j=0;j<crits.size();j++){ // for each possible crit assignment
				
				
				StaticMultiNetworkManagement smgmt = new StaticMultiNetworkManagement(mappings.get(i), crits.get(j));
				bench.clone().setManagement(smgmt);
				
				smgmt.performAllocation();
				int currQuality=smgmt.getQuality();
				
				if(currQuality>bestQuality){

					bestQuality=currQuality;
					bestmap=mappings.get(i);
					bestcrit=crits.get(j);

//					System.out.println(i);
//					System.out.println(bestQuality);
//					System.out.println(Arrays.toString(bestmap));
//					System.out.println(Arrays.toString(bestcrit));
				}
				

			}
		}
		
		completeAllocation(bestmap, bestcrit);
		
//		System.out.println("best quality: " + bestQuality);
//		System.out.println("best mapping: " + Arrays.toString(bestmap));
//		System.out.println("best crits: " + Arrays.toString(bestcrit));
		
		return true;
	
	}
	
	
	private void initialiseArrays(){
		
	    int[] n = new int[elements.size()];
	    int nr[] = new int[elements.size()];
	    
	    for(int i=0;i<elements.size();i++){
	    	
	    	nr[i]=bins.size()-1;
	    	
	    }
	    	
	    generateArrays(n, nr, 0, mappings);

	    int[] m = new int[elements.size()];
	    int mr[] = new int[elements.size()];
	    
	    for(int i=0;i<elements.size();i++){
	    	
	    	mr[i]=elements.get(i).getMessageFlow().getHighestCriticality();
	    	
	    }
	    
	    generateArrays(m, mr, 0, crits);
	}
	
	private void generateArrays(int[] n, int[] nr, int idx, ArrayList<int[]> store) {
	    if (idx == n.length) {  //stop condition for the recursion [base clause]
//	        System.out.println(Arrays.toString(n));
	        store.add(n.clone());
	        return;
	    }
	    for (int i = 0; i <= nr[idx]; i++) { 
	        n[idx] = i;
	        generateArrays(n, nr, idx+1, store); //recursive invokation, for next elements
	    }
	}	
	

	private void completeAllocation(int[] mapping, int[] crit){
		
		
		for(int i=0;i<mapping.length;i++){
			
			elements.get(i).setAllocatedCritLevel(crit[i]);

			try {
				allocate(elements.get(i),bins.get(mapping[i]));
			} catch (BinFullException e) {
				
				unallocatedElements.add(elements.get(i));

			} catch (DuplicateElementException e){
				e.printStackTrace();
			}
			
		}
		
	}
	
}
