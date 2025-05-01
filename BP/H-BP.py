import time
from collections import defaultdict
from readData import read_data, read_networks

import csv

DATA_FILE = '../message_flows.csv'

CRIT = []
# MAKE SURE TO CHANGE THIS WHEN CHANGING CRITICALITY LEVELS
L = None
NETWORKS = None

ALLOCATIONS = defaultdict()
ALLOCATED_FLOWS = []

def h_bp(worstFit=False, bestFit=False, firstFit=False):
    global ALLOCATIONS

    # attempt to allocate all flows at their highest criticality level
    for i in range(L - 1, -1, -1):
        critC, critT = CRIT[i]

        for flow in range(len(critC)):
            if critC[flow] is not None and flow not in ALLOCATIONS:
                # perform best fit allocation
                bandwidth = critC[flow] / critT[flow]
                residualNetworkCap = networksCost()
                networkId = -1

                if worstFit:
                    networkId = wf(bandwidth, residualNetworkCap)
                elif bestFit:
                    networkId = bf(bandwidth, residualNetworkCap)
                elif firstFit:
                    networkId = ff(bandwidth, residualNetworkCap)
                    
                if networkId != -1:
                    ALLOCATIONS[flow] = {'Network': networkId, 'Criticality Level': i}


    # print allocations and format it nicely
    # print("Allocations:")
    # for flow, allocation in ALLOCATIONS.items():
    #     print(f"Flow {flow} -> Network {allocation['Network']} at Criticality Level {allocation['Criticality Level']}")

def ff(bandwidth, residualNetworkCap):
    for i, cap in enumerate(residualNetworkCap):
        if bandwidth <= cap:
            return i
    return -1

def wf(bandwidth, residualNetworkCap):
    max_cap = max(residualNetworkCap)
    if max_cap >= bandwidth:
        return residualNetworkCap.index(max_cap)
    return -1

def bf(bandwidth, residualNetworkCap):
    best_fit = -1
    best_fit_value = -1
    for i in range(len(residualNetworkCap)):
        if residualNetworkCap[i] >= bandwidth and (best_fit == -1 or residualNetworkCap[i] < best_fit_value):
            best_fit = i
            best_fit_value = residualNetworkCap[i]
    return best_fit

def networksCost(flowId=None):
    total_cost = [0] * len(NETWORKS)
    for flow, allocation in ALLOCATIONS.items():
        if flow == flowId:
            continue
        network_id = allocation["Network"]
        crit_level = allocation["Criticality Level"]
        critC, critT = CRIT[crit_level]
        total_cost[network_id] += critC[flow] / critT[flow]

    # print total cost and network cost
    for i, cost in enumerate(total_cost):
        # print(f"Network {i} -> Total Cost: {cost} / Network Capacity: {NETWORKS[i]}")
        pass

    residual_bandwidth = [NETWORKS[i] - total_cost[i] for i in range(len(NETWORKS))]

    return residual_bandwidth

def objectiveScore():
    score = 0
    totalCriticality = 0
    for flow, allocation in ALLOCATIONS.items():
        crit_level = allocation["Criticality Level"]
        score += (L - crit_level)
        totalCriticality += crit_level + 1
    
    # Average criticality level
    print("Average Criticality Level: ", totalCriticality / len(ALLOCATIONS))
    return score


def main():
    global CRIT, L, NETWORKS
    CRIT = read_data('../message_flows.csv')
    NETWORKS, L = read_networks('../networks.csv')
    L = int(L)

    # Measure the start time
    start_time = time.time()

    # Run the L-BP algorithm with Best Fit
    h_bp(bestFit=True)
    # h_bp(worstFit=True)

    # Measure the end time
    end_time = time.time()

    # Calculate and print the elapsed time
    elapsed_time = end_time - start_time

    print(f"Objective Score: {objectiveScore()}")

    print("Number of allocated flows: ", len(ALLOCATIONS))

    print(f"Running Time: {elapsed_time:.4f} seconds")

    # average criticality of flows
    totalCriticality = 0
    for flow, allocation in ALLOCATIONS.items():
        totalCriticality += allocation["Criticality Level"] + 1
    print("Average Criticality Level: ", totalCriticality / len(ALLOCATIONS))

    networksCost()

    # Write allocations to a CSV file
    with open('alloc.csv', 'w', newline='') as csvfile:
        fieldnames = ['Flow', 'Network', 'Criticality Level']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

        writer.writeheader()
        for flow, allocation in ALLOCATIONS.items():
            writer.writerow({'Flow': flow, 'Network': allocation['Network'], 'Criticality Level': allocation['Criticality Level']})

if __name__ == '__main__':
    main()