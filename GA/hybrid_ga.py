import pygad
import numpy as np
import pandas as pd
import time
import math
from collections import defaultdict
import random
import matplotlib.pyplot as plt
from readData import read_data, read_networks

# Constants and Global Variables
DATA_FILE = '../message_flows.csv'

CRIT = []
# MAKE SURE TO CHANGE THIS WHEN CHANGING CRITICALITY LEVELS
L = None
NETWORKS = None

INITIAL_POPULATION = []
objectiveScores = []

# Metrics
best_fitness_values = []
average_fitness_values = []
generation_times = []
diversity_values = []
start_time = time.time()

def objective_score(solution):
    score = 0

    for i in range(0, len(solution), 2):
        net = solution[i]
        crit = solution[i + 1]
        mfIndex = i // 2

        if not mf_check(mfIndex, crit):
            continue

        score += L - crit
    return score

def initialize_population():
    data = pd.read_csv('../BP/alloc.csv')
    allocations = defaultdict()
    for index, row in data.iterrows():
        flow = row['Flow']
        network = row['Network']
        criticality = row['Criticality Level']
        allocations[flow] = {'Network': network, 'Criticality Level': criticality}

    # sort by key
    allocations = dict(sorted(allocations.items()))
    init = []
    for i in range(len(CRIT[0][0])):
        # check if flow allocated
        if i in allocations:
            init.append(allocations[i]['Network'])
            init.append(allocations[i]['Criticality Level'])
        else:
            init.append(0)
            init.append(L)
    
    print(objective_score(init))

    return generate_initial_population(init, 100)

def generate_initial_population(base_solution, population_size=100):
    """Generate diverse initial solutions based on a given base solution."""
    # initial_population = [base_solution]  # Start with the base solution
    print(f'Diversity of Base Solution: {calculate_diversity(base_solution)}')
    initial_population = []
    for i in range(population_size):
        new_solution = None
        # if i >= population_size // 2:
        if False:
            new_solution = []
            for idx in range(0, len(base_solution), 2):
                new_solution.append(random.randint(0, len(NETWORKS) - 1))
                new_solution.append(random.randint(0, L - 1))

        else:   
            new_solution = base_solution.copy()

            # Modify a subset of the solution randomly
            num_changes = int( (len(base_solution) // 2) * 0.1 )# Change 33% of the solution
            change_indices = random.sample(range(0, len(base_solution), 2), num_changes)  # Select random flows to change

            for idx in change_indices:
                # Randomly assign a new network and criticality within valid range
                new_solution[idx] = random.randint(0, len(NETWORKS) - 1)  # Network assignment
                new_solution[idx + 1] = random.randint(0, L - 1)  # Criticality assignment

        initial_population.append(new_solution)
    
    # for idx, solution in enumerate(initial_population):
    #     # print the objective score of each solution 
    #     print(f"Objective Score of Solution {idx}: {objective_score(solution)}")
    #     print(f'Diversity of Solution {idx}: {calculate_diversity(solution)}')
    return initial_population

def mf_check(i, crit):
    """Check if the message flow is defined at the given criticality level."""
    if crit >= L:
        return False
    return CRIT[crit][0][i] is not None

def fitness_func2(ga_instance, solution, solution_idx):
    """Multi-objective fitness function"""
    total_cost = [0] * len(NETWORKS)
    fitness = 0

    for i in range(0, len(solution), 2):
        net = solution[i]
        crit = solution[i + 1]
        mfIndex = i // 2

        if not mf_check(mfIndex, crit):
        #    fitness += 1
            continue

        crit_c, crit_t = CRIT[crit]
        bandwidth = (crit_c[mfIndex] / crit_t[mfIndex])

        # WF Heuristic: Assign to the network with the most available capacity
        available_capacity = [NETWORKS[j] - total_cost[j] for j in range(len(NETWORKS))]
        biggestNetwork = available_capacity.index(max(available_capacity))  # Network with most capacity
        if net == biggestNetwork:
            fitness += 1
        total_cost[net] += bandwidth

    # Adjust fitness based on the difference between cost and network capability
    invalid = False
    diff = 0
    for i, cost in enumerate(total_cost):
        if cost > NETWORKS[i]:
            invalid = True
            diff += ((cost - NETWORKS[i]) / NETWORKS[i]) * 100000

    for i in range(0, len(solution), 2):
        net = solution[i]
        crit = solution[i + 1]
        mfIndex = i // 2

        if not mf_check(mfIndex, crit):
            # fitness += 1
            continue

        crit_c, crit_t = CRIT[crit]
        total_cost[net] += (crit_c[mfIndex] / crit_t[mfIndex])
        
        if not invalid:
            fitness += (L - crit) ** 2
        else:
            fitness += (crit + 1) # the only difference between FF1 and FF2

    if invalid:
        fitness -= diff

    res = [fitness]

    # number of allocated flows
    allocated_flows = sum(1 for i in range(0, len(solution), 2) if mf_check(i // 2, solution[i + 1]))
    res.append(allocated_flows)


    return fitness * allocated_flows

def check_valid(solution):
    """Check if the solution is valid and print any violations."""
    total_cost = [0] * len(NETWORKS)
    for i in range(0, len(solution), 2):
        net = solution[i]
        crit = solution[i + 1]
        mfIndex = i // 2

        if not mf_check(mfIndex, crit):
            continue

        crit_c, crit_t = CRIT[crit]
        total_cost[net] += (crit_c[mfIndex] / crit_t[mfIndex])
        
    # Check if any network exceeds its limit
    for i, cost in enumerate(total_cost):
        print(f'Network {i} limit, {cost} > {NETWORKS[i]}')

def calculate_diversity(population):
    """Calculate the diversity of the population."""
    population_array = np.array(population)
    diversity = np.mean(np.std(population_array, axis=0))
    return diversity

def on_generation(ga_instance):
    """Callback function to execute at the end of each generation."""
    global start_time
    generation_time = time.time() - start_time
    generation_times.append(generation_time)
    start_time = time.time()
    
    best_solution, best_solution_fitness, _ = ga_instance.best_solution(pop_fitness=ga_instance.last_generation_fitness)
    objectiveScore = objective_score(best_solution)
    objectiveScores.append(objectiveScore)
    best_fitness_values.append(best_solution_fitness)
    average_fitness = np.mean(ga_instance.last_generation_fitness)
    average_fitness_values.append(average_fitness)
    
    diversity = calculate_diversity(ga_instance.population)
    diversity_values.append(diversity)
    
    print(f"Generation = {ga_instance.generations_completed}")
    print(f"Best Fitness = {best_solution_fitness}")
    print(f"Objective Score = {objectiveScore}")

def plotObjectiveScores():
    plt.plot(objectiveScores)
    plt.xlabel('Generation')
    plt.ylabel('Objective Score')
    plt.title('Objective Score over Generations')
    plt.show()

def main():
    """Main function to run the genetic algorithm."""
    global CRIT, L, NETWORKS, INITIAL_POPULATION
    CRIT = read_data(DATA_FILE)
    NETWORKS, L = read_networks('../networks.csv')
    L = int(L)
    print(f"Number of Networks: {(NETWORKS)}")
    print(f"Number of Criticality Levels: {L}")
    INITIAL_POPULATION = initialize_population()

    ga_instance = pygad.GA( num_generations=100,
                            num_parents_mating=30,
                            initial_population=INITIAL_POPULATION,
                            fitness_func=fitness_func2,
                            gene_type=int,
                            on_generation=on_generation,
                            gene_space=[{'low': 0, 'high': len(NETWORKS)}, {'low': 0, 'high': L + 1}] * len(CRIT[0][0]), # CHANGE THIS WHEN CHANGING CRITICALITY
                            # save_solutions=True,
                            parent_selection_type="sss",
                            mutation_type="random",
                            stop_criteria="saturate_15",
                            crossover_type="scattered",
                        )

    # Running the GA to optimize the parameters of the function.
    ga_instance.run()

    # Returning the details of the best solution.
    solution, solution_fitness, solution_idx = ga_instance.best_solution(ga_instance.last_generation_fitness)
    # print(f"Parameters of the best solution : {solution}")
    print(f"Fitness value of the best solution = {solution_fitness}")
    check_valid(solution)
    print('\n')
    print(f"Objective Score: {objective_score(solution)}")

    # Number of flows allocated
    total_flows = sum(1 for i in range(0, len(solution), 2) if mf_check(i // 2, solution[i + 1]))
    print(f"Total flows allocated: {total_flows}")

    total_execution_time = sum(generation_times)
    print(f"Total Execution Time: {total_execution_time} seconds")

    # Average criticality of allocated flows
    totalCrit = 0
    for i in range(1, len(solution), 2):
        mfIndex = i // 2
        if mf_check(mfIndex, solution[i]):
            totalCrit += solution[i] + 1
    print(f"Average criticality of allocated flows: {totalCrit / total_flows}")

    print('\n')

    # Create a DataFrame with the solution details
    solution_df = pd.DataFrame({'Solution': solution})

    # Save the DataFrame to a CSV file
    solution_df.to_csv('/Users/rayaanrizwan/Desktop/Year 3/Dissertation/code/Network-Resource-Management/solution.csv', index=False)

    if ga_instance.best_solution_generation != -1:
        print(f"Best fitness value reached after {ga_instance.best_solution_generation} generations.")

    # Print metrics
    print(f"Average Time per Generation: {total_execution_time / len(generation_times)} seconds")

    # ga_instance.plot_fitness()

    # ga_instance.plot_new_solution_rate()

if __name__ == "__main__":
    main()