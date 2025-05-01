package lsi.multinet.binpacking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import lsi.multinet.Allocation;
import lsi.multinet.Configuration;
import lsi.multinet.MessageFlow;

public class CABFTwoStage
		extends UtilisationBasedMultiNetworkManagement {

		ArrayList<MessageFlowElement>[] criticalities;
		ArrayList<MessageFlowElement>[] criticalities1;
		ArrayList<MessageFlowElement>[] criticalities2;
        int maxCriticality;

	public CABFTwoStage(int maxCriticality) {

		super();
		this.setBestFit();
		this.setHighest(true);
		this.setDecreasing(true);

		criticalities = new ArrayList[Configuration.CriticalityLevels];
		criticalities1 = new ArrayList[Configuration.CriticalityLevels];
		criticalities2 = new ArrayList[Configuration.CriticalityLevels];
        this.maxCriticality = maxCriticality;

		for (int i = 0; i < criticalities.length; i++) {

			criticalities[i] = new ArrayList<MessageFlowElement>();

		}

	}

	void splitMessageFlows(double flowRatio) {
		for (int i = 0; i < criticalities.length; i++) {
			if (criticalities[i].size() > 0){
				int numFlows = (int) (criticalities[i].size() * flowRatio);
				criticalities1[i] = new ArrayList<MessageFlowElement>(criticalities[i].subList(0, numFlows));
			} else {
				criticalities1[i] = new ArrayList<MessageFlowElement>();
			}
		}	
		criticalities2 = criticalities.clone();
	}

	// put all unallocated flows from the first stage in the second stage
	void putUnallocatedFlowsInSecondStage() {
		for (int i = 0; i < criticalities2.length; i++) {
			for (Iterator<MessageFlowElement> iterator = criticalities2[i].iterator(); iterator.hasNext();) {
				if (isAllocated(iterator.next().getMessageFlow())) {
					iterator.remove();
				}
			}		
		}
	}

	@Override
	public boolean performAllocation() {

		boolean allSuccess = true;
		// populateCriticalityLists();

		for (int i = criticalities1.length - 1; i >= 0; i--) { // iterate all crit levels from hi->lo

			ArrayList allocated = getAllAllocatedElements();

			this.sortMessageFlowElementByBandwidthUtilisation(allocated, !decreasing);

			for (Iterator iterator1 = allocated.iterator(); iterator1.hasNext();) {

				MessageFlowElement flow = (MessageFlowElement) iterator1.next();

				if (flow.getMessageFlow().hasCriticality(i) & flow.getAllocatedCritLevel() > i) {

					int old = flow.getAllocatedCritLevel();
					deallocate(flow);

					// tries to re-allocate flow at a lower crit level
					flow.setAllocatedCritLevel(i);
					boolean success = performAllocationStep(flow);
					// System.out.println("upgrade attempt: "+flow.getId()+" "+old+"->"+i);
					if (!success) {
						// System.out.println("upgrade failed -- rollback: "+flow.getId()+"
						// "+old+"<-"+i);
						// rollback

						flow.setAllocatedCritLevel(old);
						success = performAllocationStep(flow);
						if (!success) {
							System.out
									.println("CRITICAL ERROR - unsuccessful rollback of criticality-aware allocation");
						}
					} else {
						// System.out.println("upgrade success: "+flow.getId()+" "+old+"->"+i);
					}

				}

			}

			// allocate unallocated flows at the current crit level

			for (Iterator<MessageFlowElement> iterator2 = criticalities1[i].iterator(); iterator2.hasNext();) {

				if (isAllocated(iterator2.next().getMessageFlow())) {

					iterator2.remove();

				}

			}

			elements = criticalities1[i];
			allSuccess = super.performAllocation() || allSuccess;

		}

		putUnallocatedFlowsInSecondStage();

		return allSuccess;

	}

	public boolean performAllocation2Stage() {

		boolean allSuccess = true;

		for (int i = criticalities2.length - 1; i >= 0; i--) { // iterate all crit levels from hi->lo
			ArrayList allocated = getAllAllocatedElements();

			this.sortMessageFlowElementByBandwidthUtilisation(allocated, !decreasing);

			for (Iterator iterator1 = allocated.iterator(); iterator1.hasNext();) {
				MessageFlowElement flow = (MessageFlowElement) iterator1.next();

				if (flow.getMessageFlow().hasCriticality(i) & flow.getAllocatedCritLevel() > i) {

					int old = flow.getAllocatedCritLevel();
					deallocate(flow);

					// tries to re-allocate flow at a lower crit level
					flow.setAllocatedCritLevel(i);
					boolean success = performAllocationStep(flow);
					// System.out.println("upgrade attempt: "+flow.getId()+" "+old+"->"+i);
					if (!success) {
						// System.out.println("upgrade failed -- rollback: "+flow.getId()+"
						// "+old+"<-"+i);
						// rollback

						flow.setAllocatedCritLevel(old);
						success = performAllocationStep(flow);
						if (!success) {
							System.out
									.println("CRITICAL ERROR - unsuccessful rollback of criticality-aware allocation");
						}
					} else {
						// System.out.println("upgrade success: "+flow.getId()+" "+old+"->"+i);
					}
				}
			}

			// allocate unallocated flows at the current crit level
			for (Iterator<MessageFlowElement> iterator2 = criticalities2[i].iterator(); iterator2.hasNext();) {
				if (isAllocated(iterator2.next().getMessageFlow())) {
					iterator2.remove();
				}
			}
			elements = criticalities2[i];
			allSuccess = super.performAllocation() || allSuccess;
		}
		return allSuccess;
	}

	public boolean performInvertedAllocation() {

		boolean allSuccess = true;
		populateCriticalityLists();

		for (int i = criticalities.length - 1; i >= 0; i--) { // iterate all crit levels from hi->lo

			ArrayList allocated = getAllAllocatedElements();

			this.sortMessageFlowElementByBandwidthUtilisation(allocated, !decreasing);

			// allocate unallocated flows at the current crit level

			for (Iterator<MessageFlowElement> iterator2 = criticalities[i].iterator(); iterator2.hasNext();) {

				if (isAllocated(iterator2.next().getMessageFlow())) {

					iterator2.remove();

				}

			}

			elements = criticalities[i];
			allSuccess = super.performAllocation() || allSuccess;

			for (Iterator iterator1 = allocated.iterator(); iterator1.hasNext();) {

				MessageFlowElement flow = (MessageFlowElement) iterator1.next();

				if (flow.getMessageFlow().hasCriticality(i) & flow.getAllocatedCritLevel() > i) {

					int old = flow.getAllocatedCritLevel();
					deallocate(flow);

					// tries to re-allocate flow at a lower crit level
					flow.setAllocatedCritLevel(i);
					boolean success = performAllocationStep(flow);
					// System.out.println("upgrade attempt: "+flow.getId()+" "+old+"->"+i);
					if (!success) {
						// System.out.println("upgrade failed -- rollback: "+flow.getId()+"
						// "+old+"<-"+i);
						// rollback

						flow.setAllocatedCritLevel(old);
						success = performAllocationStep(flow);
						if (!success) {
							System.out
									.println("CRITICAL ERROR - unsuccessful rollback of criticality-aware allocation");
						}
					} else {
						// System.out.println("upgrade success: "+flow.getId()+" "+old+"->"+i);
					}

				}

			}

		}

		return allSuccess;

	}

	// adds each Message Flow Element to all the lists representing the
	// criticalities that it has defined

	protected void populateCriticalityLists() {

		for (Element flow : elements) {

			for (int i = 0; i < criticalities.length; i++) {

				MessageFlowElement mfe = (MessageFlowElement) flow;
				MessageFlow mf = mfe.getMessageFlow();

				if (mf.hasCriticality(i))
					criticalities[i].add(mfe);

			}

		}

	}

	public double getObjectiveScore() {
		double score = 0;
		for (Allocation allocation : allocations) {
			int critLevel = allocation.getCritLevel();
			score += maxCriticality - critLevel;
		}
		return score;
	}

	public double getAverageCriticality() {
		double totalCriticality = 0;
		for (Allocation allocation : allocations) {
			totalCriticality += allocation.getCritLevel() + 1;
		}
		return totalCriticality / allocations.size();
	}

	public void getNetworkCost() {
		Double[] networkCosts = new Double[4];
		Arrays.fill(networkCosts, 0.0);
		for (Allocation allocation : allocations) {
			var net = allocation.getNetwork().getName();
			if (net.equals("WiFi")) {
				networkCosts[0] += allocation.getFlow().getBandwidthUtilisation(allocation.getCritLevel());
			} else if (net.equals("LoRa")) {
				networkCosts[1] += allocation.getFlow().getBandwidthUtilisation(allocation.getCritLevel());
			} else if (net.equals("SigFox")) {
				networkCosts[2] += allocation.getFlow().getBandwidthUtilisation(allocation.getCritLevel());
			}
			else if (net.equals("rayaan")) {
				networkCosts[3] += allocation.getFlow().getBandwidthUtilisation(allocation.getCritLevel());
			}
		}
		System.out.println("Network costs: ");
		System.out.println("WiFi: " + networkCosts[0]);
		System.out.println("LoRa: " + networkCosts[1]);
		System.out.println("SigFox: " + networkCosts[2]);
		System.out.println("rayaan: " + networkCosts[3]);
	}

}
