package lsi.multinet;

/*
 *  
 * @author      Leandro Soares Indrusiak
 * @version 1.0 (York, 29/07/2020)
 * @version 1.0.1 (York, 10/09/2020)
 *  
 * 
 * Simple representation of an application message flow. It supports multiple sets of parameters, 
 * each of them linked to a specific level of criticality.  
 * 
 * 
 */


public class MessageFlow {
	
	double[] period;
	int[] payload;
	int[] privacy;
	boolean[] critLevels;
	String name ="";
	
	public MessageFlow(int payload, double period, int critLevel){
		
		this.period=new double[Configuration.CriticalityLevels];
		this.payload=new int[Configuration.CriticalityLevels];
		this.privacy=new int[Configuration.CriticalityLevels];
		this.critLevels=new boolean[Configuration.CriticalityLevels];
		
		for(int i=0;i<critLevels.length;i++){critLevels[i]=false;} // initialises flow with no declared criticality levels
		
		
		this.period[critLevel]=period;
		this.payload[critLevel]=payload;
		this.critLevels[critLevel]=true; // declares first criticality level -- mandatory for a message flow
		
	}
	
	/**
	 * @param critLevel the criticality level to set
	 * @param payload the payload at the respective criticality level
	 * @param period the period at the respective criticality level
	 */	
	public boolean setCriticalityLevel(int critLevel, int payload, double period){
		
		this.period[critLevel]=period;
		this.payload[critLevel]=payload;
		
		
		this.critLevels[critLevel]=true;
		
		
		
		return this.critLevels[critLevel];
	}

	
	/**
	 * @param critLevel the criticality level to remove
	 */	
	public void removeCriticalityLevel(int critLevel){
		
		this.critLevels[critLevel]=false;
		
	}	
	
	

	/**
	 * @param critLevel requested criticality level
	 * @return the period at the requested criticality level
	 */
	public double getPeriod(int critLevel){
		
		if(critLevels[critLevel]){
			return period[critLevel];
		}
		else return -1;
		
	}
	
	
	/**
 	 * @param critLevel requested criticality level
	 * @return the payload at the requested criticality level
	 */
	public int getPayload(int critLevel){

		if(critLevels[critLevel]){
			return payload[critLevel];
		}
		else return -1;
		
	}
	
	
	
	/**
 	 * @param critLevel baseline criticality level
	 * @return the lowest criticality level that is higher than the provided baseline; returns -1 if there are no criticality levels higher than the baseline registered for this flow.
	 * 
	 */
	public int getHigherCriticality(int critLevel){
		assert critLevel<Configuration.CriticalityLevels;
		for(int i=critLevel+1;i<critLevels.length;i++){
			
			if(critLevels[i]) return i;
			
		}
		
		return -1;		
		
		
	}

	
	/**
 	 * @param critLevel baseline criticality level
	 * @return the highest criticality level that is lower than the provided baseline; returns -1 if there are no criticality levels lower than the baseline registered for this flow.
	 * 
	 */
	public int getLowerCriticality(int critLevel){
		assert critLevel<Configuration.CriticalityLevels;
		for(int i=critLevel;i>0;i--){
			
			if(critLevels[i-1]) return i-1;
			
		}
		
		return -1;
	}
	
	
	/**
 	 * 
	 * @return the highest criticality level registered for this flow; returns -1 if no criticality levels registered.
	 * 
	 */
	public int getHighestCriticality(){
		
		for(int i=critLevels.length;i>0;i--){
			
			if(critLevels[i-1]) return i-1;
			
		}
		
		return -1;		
		
		
	}

	
	/**
 	 * 
	 * @return the lowest criticality level registered for this flow; returns -1 if no criticality levels registered.
	 * 
	 */
	public int getLowestCriticality(){
		
		for(int i=0;i<critLevels.length;i++){
			
			if(critLevels[i]) return i;
			
		}
		
		return -1;
	}

	
	
	/**
 	 * 
	 * @return whether the provided criticality level is registered for this flow.
	 * 
	 */
	public boolean hasCriticality(int i){
		
		return critLevels[i];
		
	}
	
	
	public String printCritLevel(int critLevel){
		String clevel = "crit level "+critLevel+" - T="+getPeriod(critLevel)+" C="+getPayload(critLevel);
//		System.out.println(clevel);
		return clevel;		
	}
	
	public String printAllCritLevels(){
		
		String clevels="";
		for(int i=0;i<critLevels.length;i++){
			
			
			
			if(critLevels[i]){
				
				clevels = clevels + printCritLevel(i) + "\r\n";
			}
			
		}
		
		return clevels;
	}
	
	
	public double getBandwidthUtilisation(int critLevel){
		
		return getPayload(critLevel)/getPeriod(critLevel);
		
	}
	
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		
		return name;
	}
	
	
	// simple unit test
	
	public static void main(String[] args){
		
		
		
		MessageFlow flow1= new MessageFlow(20, 0.02, 2);
		
		flow1.setCriticalityLevel(1, 20, 0.01);
		flow1.setCriticalityLevel(0, 80, 0.01);
		flow1.setCriticalityLevel(3, 10, 0.02);
		flow1.setCriticalityLevel(4, 10, 0.05);
		flow1.setCriticalityLevel(5, 10, 0.1);
		
		
		flow1.printAllCritLevels();
		
		System.out.println("highest: "+flow1.getHighestCriticality());
		System.out.println("lowest: "+flow1.getLowestCriticality());
		System.out.println("higher than 2: "+flow1.getHigherCriticality(2));
		System.out.println("lower than 2: "+flow1.getLowerCriticality(2));
		System.out.println("higher than 1: "+flow1.getHigherCriticality(1));
		System.out.println("lower than 1: "+flow1.getLowerCriticality(1));
		System.out.println("higher than 0: "+flow1.getHigherCriticality(0));
		System.out.println("lower than 0: "+flow1.getLowerCriticality(0));		
		System.out.println("higher than 5: "+flow1.getHigherCriticality(5));
		System.out.println("lower than 5: "+flow1.getLowerCriticality(5));
	
		
	}
	


}








