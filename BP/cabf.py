import numpy as np
import pandas as pd
import time
import math
from collections import defaultdict
from tabulate import tabulate
from readData import read_data, read_networks

import csv

DATA_FILE = '../message_flows.csv'

CRIT = None
# MAKE SURE TO CHANGE THIS WHEN CHANGING CRITICALITY LEVELS
L = None
NETWORKS = None

ALLOCATIONS = defaultdict()
ALLOCATED_FLOWS = []

def cabf_algorithm():
    global ALLOCATIONS

    # allocate flows at the current critcality level
    for i in range(len(CRIT) - 1, -1, -1):
        critC, critT = CRIT[i]

        # attempt to lower the criticality of already allocated flows
        for flow, allocation in ALLOCATIONS.items():
            critLevel = allocation["Criticality Level"]
            if critLevel > i and critC[flow] is not None:
                bandwidth = critC[flow] / critT[flow]
                # remove the flow from the current allocation
                residualNetworkCap = networksCost(flow)
                network_id = bestFit(bandwidth, residualNetworkCap)
                if network_id != -1:
                    ALLOCATIONS[flow] = {'Network': network_id, 'Criticality Level': i}

        for flow in range(len(critC)):
            if critC[flow] is not None and flow not in ALLOCATED_FLOWS:
                # perform best fit allocation
                bandwidth = critC[flow] / critT[flow]
                residualNetworkCap = networksCost()
                network_id = bestFit(bandwidth, residualNetworkCap)
                if network_id != -1:
                    ALLOCATIONS[flow] = {'Network': network_id, 'Criticality Level': i}
                    ALLOCATED_FLOWS.append(flow)

    # print allocations and format it nicely
    print("Allocations:")
    for flow, allocation in ALLOCATIONS.items():
        print(f"Flow {flow} -> Network {allocation['Network']} at Criticality Level {allocation['Criticality Level']}")

def bestFit(bandwidth, residualNetworkCap):
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
        print(f"Network {i} -> Total Cost: {cost} / Network Capacity: {NETWORKS[i]}")

    residual_bandwidth = [NETWORKS[i] - total_cost[i] for i in range(len(NETWORKS))]

    return residual_bandwidth

def objectiveScore():
    score = 0
    for flow, allocation in ALLOCATIONS.items():
        crit_level = allocation["Criticality Level"]
        score += (L - crit_level)
    return score


def main():
    global CRIT, L, NETWORKS
    CRIT = read_data(DATA_FILE)
    NETWORKS, L = read_networks('../networks.csv')

    cabf_algorithm()
    print(f"Objective Score: {objectiveScore()}")

    # average criticality of allocated flows
    avg_crit = sum([allocation["Criticality Level"] + 1 for allocation in ALLOCATIONS.values()]) / len(ALLOCATIONS)
    print(f"Average Criticality of Allocated Flows: {avg_crit}")

    print("Number of allocated flows: ", len(ALLOCATIONS))

    networksCost()

    with open('alloc.csv', 'w', newline='') as csvfile:
        fieldnames = ['Flow', 'Network', 'Criticality Level']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

        writer.writeheader()
        for flow, allocation in ALLOCATIONS.items():
            writer.writerow({'Flow': flow, 'Network': allocation['Network'], 'Criticality Level': allocation['Criticality Level']})
if __name__ == '__main__':
    main()