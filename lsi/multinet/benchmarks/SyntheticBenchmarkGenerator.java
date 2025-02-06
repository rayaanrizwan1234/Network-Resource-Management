package lsi.multinet.benchmarks;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import lsi.multinet.MessageFlow;


/**********************************************
 * 
 * @author Leandro Soares Indrusiak
 *
 *
 *
 * The class aims to create a set of message flows according 
 * to a few parameters:
 * 
 * nflows - number of flows on the benchmark
 * 
 *   
 * nCrit - number of criticality levels of the flows in the benchmark 
 * 
 * minPeriod and�maxPeriod - lower and upper bounds on the 
 *    period of the generated flows at the lowest level of criticality
 *    
 * 
 * targetUtil - the desired utilisation of the complete benchmark at the lowest level of criticality, 
 *    i.e. the sum of all individual flow utilisations (where 
 *    utilisation is C / T, i.e. largest payload over time period)��
 * 
 * 
 * minCritFactor and maxCritFactor - upper and lower bound of a multiplicative factor 0<critFactor<1 to generate the utilisation
 *    of the next level of criticality up, from the utilisation of a given level of criticality 
 * 
 * 
 * hasCritFactor - factor 0<hasCritFactor<1, which indicates 
 *     the probability of a flow to have declared a given level of criticality  (i.e. the highest the criticality, 
 *     the lower the likelihood that it will be declared);
 *     factor is multiplied by itself once for every criticality level above the lowest 
 * 
 * 
 * Given the parameters above, the class does the following:
 * 
 * 1 - generates the individual utilisation of each flow y sampling a uniform distribution between target - 10% and target + 10%
 * 
 * 2 - generates periods for each flow by sampling a uniform distribution 
 * between minPeriod and�maxPeriod 
 * 
 * 3 - calculate the maximum payload of each flow by multiplying
 * the values generated in steps 1 and 2 above
 * 

 * 4 - for each criticality level up to nCrit, generate period and maximum payload of each flow by sampling
 * a uniform distribution between minCritFactor and maxCritFactor, and multiplying the result by the period and
 * payload of the previous level 
 * 
 * 
 *
 */
public class SyntheticBenchmarkGenerator {

	int nflows;
	int nCrit;
	double hasCritFactor;
	double minCritFactor;
	double maxCritFactor;
	double minPeriod, maxPeriod, targetUtil;
	Random rnd;
	
	public SyntheticBenchmarkGenerator(int nflows, double minPeriod, double maxPeriod, double targetUtil, int nCrit, double hasCritFactor,
			double minCritFactor, double maxCritFactor, int seed){
		
		this.minPeriod = minPeriod; // minimum interval between messages
		this.maxPeriod = maxPeriod; // maximum interval between messages
		this.targetUtil = targetUtil; 
		this.nflows = nflows;
		this.nCrit=nCrit;
		this.hasCritFactor = ensureRange(hasCritFactor);
		this.minCritFactor = ensureRange(minCritFactor);
		this.maxCritFactor = ensureRange(maxCritFactor);
		rnd = new Random(seed);
		
		
	}
	
	
	// if d is not a value between 0 and 1
	// returns only its fractional part
	
	
	private double ensureRange(double d){
		long l = (long) d;
		return d -l;
		
	}

	public Benchmark getSyntheticBenchmark(){
		
		Benchmark app = new Benchmark();
		
		double[] utils = generateUtilizations(targetUtil, nflows, rnd);
		
		for(int i=0; i<nflows;i++){

			double has = hasCritFactor;
			double period = minPeriod + ((maxPeriod-minPeriod)*rnd.nextDouble());
			int payload = (int) (period*utils[i]);
			
			MessageFlow mf = new MessageFlow(payload, period, 0);
			mf.setName("mf"+i);
			for (int c=1;c<nCrit;c++){
				if(rnd.nextDouble()<has){

					double critFactorC = minCritFactor + ((maxCritFactor-minCritFactor)*rnd.nextDouble());
					double critFactorT = minCritFactor + ((maxCritFactor-minCritFactor)*rnd.nextDouble());
					
					period = period/critFactorT;
					payload = (int)(payload*critFactorC);
					mf.setCriticalityLevel(c, payload, period);
					mf.setName("mf"+i+"-"+c);

				}
				has = has*has;				
			}
			
			app.addMessageFlow(mf);
			
		}
		
		
		return app;
		
		
	}
	
	
	protected double[] generateUtilizations(double utilisation, int nflows, Random rnd) {
		double[] util = new double[nflows];

		double maxUtil = utilisation + utilisation*0.2;
		double minUtil = utilisation - utilisation*0.2;
		
		for(int i=0;i<util.length;i++){
			
			util[i]= minUtil + rnd.nextDouble()*(maxUtil-minUtil);
			
		}
		
		
		return util;
	}

	public Benchmark generateCSV(String filePath) {
        Benchmark benchmark = getSyntheticBenchmark();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Name,Payload,Period,CriticalityLevel\n");
            for (MessageFlow mf : benchmark.flows) {
                for (int c = 1; c < nCrit; c++) {
                    writer.append(mf.getName())
                          .append(',');
                    if (mf.hasCriticality(c)) {
                        writer.append(String.valueOf(mf.getPayload(c)))
                              .append(',')
                              .append(String.valueOf(mf.getPeriod(c)));
                    } else {
                        writer.append("None,None");
                    }
                    writer.append(',')
                          .append(String.valueOf(c))
                          .append('\n');
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

		return benchmark;
    }

}