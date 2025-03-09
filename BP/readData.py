import pandas as pd
import math
import numpy as np

def read_data(file_path):
    CRIT = []

    CRIT_0_C, CRIT_0_T = [], []
    CRIT_1_C, CRIT_1_T = [], []
    CRIT_2_C, CRIT_2_T = [], []
    CRIT_3_C, CRIT_3_T = [], []

    CRIT.append((CRIT_0_C, CRIT_0_T))
    CRIT.append((CRIT_1_C, CRIT_1_T))
    CRIT.append((CRIT_2_C, CRIT_2_T))
    CRIT.append((CRIT_3_C, CRIT_3_T))

    """Read data from CSV file and populate criticality arrays."""
    data = pd.read_csv(file_path)

    for index, row in data.iterrows():
        payload = row['Payload']
        period = row['Period']
        criticality = row['CriticalityLevel']

        if math.isnan(payload) or math.isnan(period):
            payload = None
            period = None

        CRIT[criticality][0].append(payload)
        CRIT[criticality][1].append(period)
    
    return CRIT

def read_networks(file_path):
    NETWORKS = []
    L = None

    data = np.loadtxt(file_path, delimiter=',')

    # Assign the first row to L
    L = data[0]

    # Store the remaining values in an array
    NETWORKS = data[1:]

    return NETWORKS, L

if __name__ == "__main__":
    # read_data('../message_flows.csv')
    networks, l = read_networks('networks.csv')
    print(networks, l)
