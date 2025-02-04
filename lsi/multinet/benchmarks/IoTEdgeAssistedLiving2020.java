package lsi.multinet.benchmarks;

import lsi.multinet.MessageFlow;
import lsi.multinet.Network;

public class IoTEdgeAssistedLiving2020 extends Benchmark{

	

	
	
	public void createBenchmark(Benchmark b){
		
		
		// declare message flows
		
		MessageFlow falld = new MessageFlow(1000, 10, 0);
		falld.setCriticalityLevel(1, 40, 20);
		falld.setCriticalityLevel(2, 10, 60);
		falld.setName("fall detection");
		b.addMessageFlow(falld);
		
		MessageFlow healthm = new MessageFlow(1000, 5, 0);
		healthm.setCriticalityLevel(1, 80, 10);
		healthm.setCriticalityLevel(2, 10, 20);
		healthm.setName("heart monitoring");
		b.addMessageFlow(healthm);
		
		MessageFlow bodyt = new MessageFlow(30, 30, 0);
		bodyt.setCriticalityLevel(1, 10, 120);
		bodyt.setName("body temperature");
		b.addMessageFlow(bodyt);
		
		MessageFlow bedsens = new MessageFlow(40000, 10, 0);
		bedsens.setCriticalityLevel(1, 10, 30);
		bedsens.setName("bedroom sensor");
		b.addMessageFlow(bedsens);
		
		MessageFlow bathsens = new MessageFlow(80, 10, 0);
		bathsens.setCriticalityLevel(1, 10, 30);
		bathsens.setName("bathroom sensor");
		b.addMessageFlow(bathsens);
		
		MessageFlow kitsens = new MessageFlow(40000, 10, 0);
		kitsens.setCriticalityLevel(1, 10, 30);
		kitsens.setName("lounge/kitchen sensor");
		b.addMessageFlow(kitsens);
		
		MessageFlow frontsens = new MessageFlow(40000, 10, 0);
		frontsens.setCriticalityLevel(1, 10, 30);
		frontsens.setName("front door sensor");
		b.addMessageFlow(frontsens);
		
		MessageFlow enermon = new MessageFlow(40, 3600, 0);
		enermon.setName("energy usage");
		b.addMessageFlow(enermon);
		
		
		// declare networks
		
		Network wifi = new Network(8000,0);
		wifi.setName("WiFi");
		b.addNetwork(wifi);
		
		
		Network lora = new Network(220,0); // LoRa SF9
		lora.setName("LoRa");
		b.addNetwork(lora);
		
		Network sigfox = new Network(6,0);
		sigfox.setName("SigFox");
		b.addNetwork(sigfox);
		
		
		
		/* original values
		 * 
		// declare message flows
		
		MessageFlow falld = new MessageFlow(1000, 60, 0);
		falld.setCriticalityLevel(1, 40, 120);
		falld.setCriticalityLevel(2, 10, 3600);
		falld.setName("fall detection");
		b.addMessageFlow(falld);
		
		MessageFlow healthm = new MessageFlow(1000, 5, 0);
		healthm.setCriticalityLevel(1, 10, 30);
		healthm.setCriticalityLevel(2, 10, 3600);
		healthm.setName("health monitoring");
		b.addMessageFlow(healthm);
		
		MessageFlow bodyt = new MessageFlow(30, 30, 0);
		bodyt.setCriticalityLevel(1, 10, 120);
		bodyt.setName("body temperature");
		b.addMessageFlow(bodyt);
		
		MessageFlow bedsens = new MessageFlow(40000, 10, 0);
		bedsens.setCriticalityLevel(1, 10, 30);
		bedsens.setName("bedroom sensor");
		b.addMessageFlow(bedsens);
		
		MessageFlow bathsens = new MessageFlow(80, 10, 0);
		bathsens.setCriticalityLevel(1, 10, 30);
		bathsens.setName("bathroom sensor");
		b.addMessageFlow(bathsens);
		
		MessageFlow kitsens = new MessageFlow(40000, 10, 0);
		kitsens.setCriticalityLevel(1, 10, 30);
		kitsens.setName("lounge/kitchen sensor");
		b.addMessageFlow(kitsens);
		
		MessageFlow frontsens = new MessageFlow(40000, 10, 0);
		frontsens.setCriticalityLevel(1, 10, 30);
		frontsens.setName("front door sensor");
		b.addMessageFlow(frontsens);
		
		MessageFlow enermon = new MessageFlow(40, 3600, 0);
		enermon.setName("energy usage");
		b.addMessageFlow(enermon);
		
		
		// declare networks
		
		Network wifi = new Network(8000,0);
		wifi.setName("WiFi");
		b.addNetwork(wifi);
		
		
		Network lora = new Network(220,0);
		lora.setName("LoRa");
		b.addNetwork(lora);
		
		Network sigfox = new Network(6,0);
		sigfox.setName("SigFox");
		b.addNetwork(sigfox);
		
		*/
		
	}
	
}
