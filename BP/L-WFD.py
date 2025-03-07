import numpy as np
import pandas as pd
import time
import math
from collections import defaultdict
from tabulate import tabulate

import csv

NETWORKS = [48, 1760, 64000]
DATA_FILE = '../message_flows.csv'

CRIT = []
# MAKE SURE TO CHANGE THIS WHEN CHANGING CRITICALITY LEVELS
L = 3

ALLOCATIONS = defaultdict()
ALLOCATED_FLOWS = []


def read_data(file_path):
    global CRIT
    """Read data from CSV file and populate criticality arrays."""
    data = pd.read_csv(file_path)

    CRIT_1_C, CRIT_1_T = [], []
    CRIT_2_C, CRIT_2_T = [], []
    CRIT_3_C, CRIT_3_T = [], []
    CRIT_4_C, CRIT_4_T = [], []

    for index, row in data.iterrows():
        payload = row['Payload']
        period = row['Period']
        criticality = row['CriticalityLevel']

        if math.isnan(payload) or math.isnan(period):
            payload = None
            period = None

        if criticality == 0:
            CRIT_1_C.append(payload)
            CRIT_1_T.append(period)
        elif criticality == 1:
            CRIT_2_C.append(payload)
            CRIT_2_T.append(period)
        elif criticality == 2:
            CRIT_3_C.append(payload)
            CRIT_3_T.append(period)
        elif criticality == 3:
            CRIT_4_C.append(payload)
            CRIT_4_T.append(period)

    # CRIT_1_C = [1000, 1000, 30, 40000, 80, 40000, 40000, 40]
    # CRIT_1_T = [10, 5, 30, 10, 10, 10, 10, 3600]

    # CRIT_2_C = [40, 80, 10, 10, 10, 10, 10, None] 
    # CRIT_2_T = [20, 10, 120, 30, 30, 30, 30, None]

    # CRIT_3_C = [10, 10, None, None, None, None, None, None] 
    # CRIT_3_T = [60, 20, None, None, None, None, None, None] 

    CRIT.append((CRIT_1_C, CRIT_1_T))
    CRIT.append((CRIT_2_C, CRIT_2_T))
    CRIT.append((CRIT_3_C, CRIT_3_T))
    CRIT.append((CRIT_4_C, CRIT_4_T))

def l_wfd():
    global ALLOCATIONS

    # attempt to allocate everything at the lowest crticality
    for i in range(L):
        for flow in range(len(CRIT[i][0])):
            payload = CRIT[i][0][flow]
            period = CRIT[i][1][flow]

            if flow in ALLOCATED_FLOWS or payload is None or period is None:
                continue
            
            residualNetworkCap = networksCost()
            network = worstFit(payload / period, residualNetworkCap)
            if network != -1:
                ALLOCATIONS[flow] = {"Network": network, "Criticality Level": i}
                ALLOCATED_FLOWS.append(flow)

    # print allocations and format it nicely
    print("Allocations:")
    for flow, allocation in ALLOCATIONS.items():
        print(f"Flow {flow} -> Network {allocation['Network']} at Criticality Level {allocation['Criticality Level']}")

def worstFit(bandwidth, residualNetworkCap):
    max_cap = max(residualNetworkCap)
    if bandwidth <= max_cap:
        return residualNetworkCap.index(max_cap)
    return -1

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
    read_data(DATA_FILE)
    l_wfd()
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