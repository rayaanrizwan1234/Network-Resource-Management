package lsi.multinet.benchmarks;

import java.util.ArrayList;


// import lsi.multinet.MessageFlow;
// import lsi.multinet.MultiNetworkManagement;
import lsi.multinet.*;

public class Benchmark {
	
	
	ArrayList<Network> networks;
	ArrayList<MessageFlow> flows;
	
	
	public Benchmark(){
		
		networks = new ArrayList<Network>();
		flows = new ArrayList<MessageFlow>();
		createBenchmark(this);
		
	}
	
	
	public void addMessageFlow(MessageFlow flow){
		
		flows.add(flow);
		
	}
	
	public void addNetwork(Network network){
		
		networks.add(network);
		
	}
	
	
	
	public void setManagement(MultiNetworkManagement mgmt){
		
        for (Network net: networks) {
            
    		mgmt.addNetwork(net);
        }
		
        for (MessageFlow flow: flows) {
            
    		mgmt.addMessageFlow(flow);
        }
        
        
        
	}

	
	
	public void createBenchmark(Benchmark b){
		
		// to be redefined in the subclasses
		// with the benchmark's message flows and networks
		
		
		for(MessageFlow flow: flows){
			
			b.addMessageFlow(flow);
		}
		
		for(Network net: networks){
			
			b.addNetwork(net);
		}
		
		
		
	}
	
	
	
	public Benchmark clone(){
		Benchmark b = new Benchmark();
		createBenchmark(b);
		return b;
		
	}
	
	
	public String toString(){
		

		String str="=================== \r\n";
		str=str+"Networks: \r\n";
		str=str+"=================== \r\n";
		
        for (Network net: networks) {
            
    		str=str+net.getName()+"\r\n";
        }

		str=str+"=================== \r\n";
		str=str+"Message Flows: \r\n";
		str=str+"=================== \r\n";
        for (MessageFlow flow: flows) {
            
    		str=str+flow.getName()+"\r\n";
    		str=str+flow.printAllCritLevels()+"\r\n";
        }
		
		
		
		return str;
		
	}
	
	
}
