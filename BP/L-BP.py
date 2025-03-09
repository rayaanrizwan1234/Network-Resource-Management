import numpy as np
import pandas as pd
import time
import math
from collections import defaultdict
from tabulate import tabulate
from readData import read_data, read_networks

import csv

DATA_FILE = '../message_flows.csv'

CRIT = []
# MAKE SURE TO CHANGE THIS WHEN CHANGING CRITICALITY LEVELS
L = None
NETWORKS = None

ALLOCATIONS = defaultdict()
ALLOCATED_FLOWS = []

def l_bp(worstFit=False, bestFit=False, firstFit=False):
    global ALLOCATIONS

    # attempt to allocate everything at the lowest crticality
    for i in range(L):
        for flow in range(len(CRIT[i][0])):
            payload = CRIT[i][0][flow]
            period = CRIT[i][1][flow]

            if flow in ALLOCATIONS or payload is None or period is None:
                continue
            
            residualNetworkCap = networksCost()
            network = -1
            bandwidth = payload / period

            if worstFit:
                network = wf(bandwidth, residualNetworkCap)
            elif bestFit:
                network = bf(bandwidth, residualNetworkCap)
            elif firstFit:
                network = ff(bandwidth, residualNetworkCap)

            if network != -1:
                ALLOCATIONS[flow] = {"Network": network, "Criticality Level": i}

    # print allocations and format it nicely
    print("Allocations:")
    for flow, allocation in ALLOCATIONS.items():
        print(f"Flow {flow} -> Network {allocation['Network']} at Criticality Level {allocation['Criticality Level']}")

def wf(bandwidth, residualNetworkCap):
    max_cap = max(residualNetworkCap)
    if bandwidth <= max_cap:
        return residualNetworkCap.index(max_cap)
    return -1

def ff(bandwidth, residualNetworkCap):
    for i, cap in enumerate(residualNetworkCap):
        if cap >= bandwidth:
            return i
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
        print(f"Network {i} -> Total Cost: {cost} / Network Capacity: {NETWORKS[i]}")

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

    l_bp(bestFit=True)
    print(f"Objective Score: {objectiveScore()}")

    # print(ALLOCATIONS[1792])

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