package lsi.multinet.benchmarks;

import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import lsi.multinet.Network;
import lsi.multinet.binpacking.BruteForceMultiNetworkManagement;
import lsi.multinet.binpacking.CriticalityAwareUtilisationBasedMultiNetworkManagement;
import lsi.multinet.binpacking.UtilisationBasedMultiNetworkManagement;

public class SyntheticBenchmarkGeneratorTest {

	public static void main(String[] args){

		int nPoints=100;

		SyntheticBenchmarkGenerator gen = new SyntheticBenchmarkGenerator(80, 5.0, 120.0, 1800.0, 4, 0.8,
				0.4, 0.8, 23031965);


		ArrayList<Point2D.Double[]> results = new ArrayList<Point2D.Double[]>();



		for(int i=0;i<nPoints;i++){

			Benchmark benchmark = gen.getSyntheticBenchmark();
		

			Network wifi = new Network(8000,0);
			wifi.setName("WiFi");
			benchmark.addNetwork(wifi);


			Network lora = new Network(220,0); // LoRa SF9
			lora.setName("LoRa");
			benchmark.addNetwork(lora);

			Network sigfox = new Network(6,0);
			sigfox.setName("SigFox");
			benchmark.addNetwork(sigfox);

			
			System.out.println(benchmark.toString());	
			
			Point2D.Double[] result = new Point2D.Double[15];
			results.add(result);

			
			UtilisationBasedMultiNetworkManagement mgmt;
			

			
			//0 - H-FF
			mgmt = new UtilisationBasedMultiNetworkManagement();
			//mgmt.setDecreasing(false);
			mgmt.setFirstFit();
			mgmt.setHighest(true);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p0 = new Point2D.Double();
			p0.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[0]=p0;
			
			
			
			//1 - H-FFD
			mgmt = new UtilisationBasedMultiNetworkManagement();
			mgmt.setDecreasing(true);
			mgmt.setFirstFit();
			mgmt.setHighest(true);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p1 = new Point2D.Double();
			p1.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[1]=p1;
			
			
			//2 - H-BF
			mgmt = new UtilisationBasedMultiNetworkManagement();
			//mgmt.setDecreasing(false);
			mgmt.setBestFit();
			mgmt.setHighest(true);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p2 = new Point2D.Double();
			p2.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[2]=p2;
			
			//3 - H-BFD
			mgmt = new UtilisationBasedMultiNetworkManagement();
			mgmt.setDecreasing(true);
			mgmt.setBestFit();
			mgmt.setHighest(true);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p3 = new Point2D.Double();
			p3.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[3]=p3;
			
			//4 - H-WF
			mgmt = new UtilisationBasedMultiNetworkManagement();
			//mgmt.setDecreasing(false);
			mgmt.setWorstFit();
			mgmt.setHighest(true);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p4 = new Point2D.Double();
			p4.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[4]=p4;
			
			//5 - H-WFD
			mgmt = new UtilisationBasedMultiNetworkManagement();
			mgmt.setDecreasing(true);
			mgmt.setWorstFit();
			mgmt.setHighest(true);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p5 = new Point2D.Double();
			p5.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[5]=p5;
			
			
			
			
			
			
			
			
			//6 - L-FF
			mgmt = new UtilisationBasedMultiNetworkManagement();
			//mgmt.setDecreasing(false);
			mgmt.setFirstFit();
			mgmt.setHighest(false);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p6 = new Point2D.Double();
			p6.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[6]=p6;
			
			
			
			//7 - L-FFD
			mgmt = new UtilisationBasedMultiNetworkManagement();
			mgmt.setDecreasing(true);
			mgmt.setFirstFit();
			mgmt.setHighest(false);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p7 = new Point2D.Double();
			p7.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[7]=p7;
			
			
			//8 - L-BF
			mgmt = new UtilisationBasedMultiNetworkManagement();
			//mgmt.setDecreasing(false);
			mgmt.setBestFit();
			mgmt.setHighest(false);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p8 = new Point2D.Double();
			p8.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[8]=p8;
			
			//9 - L-BFD
			mgmt = new UtilisationBasedMultiNetworkManagement();
			mgmt.setDecreasing(true);
			mgmt.setBestFit();
			mgmt.setHighest(false);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p9 = new Point2D.Double();
			p9.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[9]=p9;
			
			//10 - L-WF
			mgmt = new UtilisationBasedMultiNetworkManagement();
			//mgmt.setDecreasing(false);
			mgmt.setWorstFit();
			mgmt.setHighest(false);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p10 = new Point2D.Double();
			p10.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[10]=p10;
			
			//11 - L-WFD
			mgmt = new UtilisationBasedMultiNetworkManagement();
			mgmt.setDecreasing(true);
			mgmt.setWorstFit();
			mgmt.setHighest(false);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();

			Point2D.Double p11 = new Point2D.Double();
			p11.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[11]=p11;
			
			
			
			//12 - CABF

			CriticalityAwareUtilisationBasedMultiNetworkManagement cmgmt;
			
		
			cmgmt = new CriticalityAwareUtilisationBasedMultiNetworkManagement();
			cmgmt.setDecreasing(false);
			cmgmt.setBestFit();
			
			benchmark.clone().setManagement(cmgmt);
			
			cmgmt.performAllocation();
			
			Point2D.Double p12 = new Point2D.Double();
			p12.setLocation(cmgmt.getAllocatedPercentage(), cmgmt.getAverageCriticalityPercentage());
			result[12]=p12;
			
			

			//13 - CABFinv
			
			cmgmt = new CriticalityAwareUtilisationBasedMultiNetworkManagement();
			cmgmt.setDecreasing(false);
			cmgmt.setBestFit();
			
			benchmark.clone().setManagement(cmgmt);
			
			cmgmt.performInvertedAllocation();
			
			Point2D.Double p13 = new Point2D.Double();
			p13.setLocation(cmgmt.getAllocatedPercentage(), cmgmt.getAverageCriticalityPercentage());
			result[13]=p13;
			
			//14 - Optimal
		/*	
			mgmt = new BruteForceMultiNetworkManagement(benchmark);
			benchmark.clone().setManagement(mgmt);
			
			mgmt.performAllocation();
			
			Point2D.Double p14 = new Point2D.Double();
			p14.setLocation(mgmt.getAllocatedPercentage(), mgmt.getAverageCriticalityPercentage());
			result[14]=p14;
			*/
		}


		
		
		for(Point2D.Double[] points: results){
			
			System.out.println(	points[0].getX()+","+points[0].getY()+","+
								points[1].getX()+","+points[1].getY()+","+
								points[2].getX()+","+points[2].getY()+","+
								points[3].getX()+","+points[3].getY()+","+
								points[4].getX()+","+points[4].getY()+","+
								points[5].getX()+","+points[5].getY()+","+
								points[6].getX()+","+points[6].getY()+","+
								points[7].getX()+","+points[7].getY()+","+
								points[8].getX()+","+points[8].getY()+","+
								points[9].getX()+","+points[9].getY()+","+
								points[10].getX()+","+points[10].getY()+","+
								points[11].getX()+","+points[11].getY()+","+
								points[12].getX()+","+points[12].getY()+","+
								points[13].getX()+","+points[13].getY());
					
		
		}
		
	}

}
