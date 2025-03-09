import pygad
import numpy as np
import pandas as pd
import time
import math
import matplotlib.pyplot as plt
from readData import read_data, read_networks
# Constants and Global Variables
DATA_FILE = '../message_flows.csv'

CRIT = []
# MAKE SURE TO CHANGE THIS WHEN CHANGING CRITICALITY LEVELS
L = None
NETWORKS = None

# Metrics
best_fitness_values = []
average_fitness_values = []
generation_times = []
diversity_values = []
start_time = time.time()
objectiveScores = []

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
    
def mf_check(i, crit):
    """Check if the message flow is defined at the given criticality level."""
    if crit >= L:
        return False
    return CRIT[crit][0][i] is not None

def fitness_func2(ga_instance, solution, solution_idx):
    """FITNESS FUNCTION USED FROM SHERIFF PAPER"""
    total_cost = [0] * len(NETWORKS)
    fitness = 0
    
    for i in range(0, len(solution), 2):
        net = solution[i]
        crit = solution[i + 1]
        mfIndex = i // 2

        if not mf_check(mfIndex, crit):
            fitness += 1
            continue

        crit_c, crit_t = CRIT[crit]
        total_cost[net] += (crit_c[mfIndex] / crit_t[mfIndex])

    # Adjust fitness based on the difference between cost and network capability
    invalid = False
    diff = []
    for i, cost in enumerate(total_cost):
        if cost > NETWORKS[i]:
            diff.append( (NETWORKS[i] - cost) / NETWORKS[i])
        else:
           diff.append(cost / NETWORKS[i])
    
    return diff

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
    best_fitness_values.append(best_solution_fitness)
    average_fitness = np.mean(ga_instance.last_generation_fitness)
    average_fitness_values.append(average_fitness)
    objectiveScores.append(objectiveScore)
    
    diversity = calculate_diversity(ga_instance.population)
    diversity_values.append(diversity)
    
    print(f"Generation = {ga_instance.generations_completed}")
    print(f"Best Fitness = {best_solution_fitness}")
    print(f"Objective Score: {objectiveScore}")

def plotObjectiveScores():
    """Plot the objective scores."""
    plt.plot(objectiveScores)
    plt.xlabel('Generation')
    plt.ylabel('Objective Score')
    plt.title('Objective Score vs Generation')
    plt.show()

def main():
    """Main function to run the genetic algorithm."""
    global CRIT, L, NETWORKS
    CRIT = read_data('../message_flows.csv')
    NETWORKS, L = read_networks('../networks.csv')
    L = int(L)

    ga_instance = pygad.GA(num_generations=100,
                           num_parents_mating=30,
                           sol_per_pop=80,
                           num_genes=len(CRIT[0][0]) * 2,
                           fitness_func=fitness_func2,
                           gene_type=int,
                           on_generation=on_generation,
                           gene_space=[{'low': 0, 'high': len(NETWORKS)}, {'low': 0, 'high': L+1}] * len(CRIT[0][0]), # CHANGE THIS WHEN CHANGING CRITICALITY
                           save_solutions=True,
                           parent_selection_type="sss",
                           mutation_type="random",
                        #    mutation_percent_genes=(0.1, 0.1), # higher mutation chance for lower fitness solution
                           crossover_type="scattered",
                           )

    # Running the GA to optimize the parameters of the function.
    ga_instance.run()

    # Returning the details of the best solution.
    solution, solution_fitness, solution_idx = ga_instance.best_solution(ga_instance.last_generation_fitness)
    print(f"Parameters of the best solution : {solution}")
    print(f"Fitness value of the best solution = {solution_fitness}")
    check_valid(solution)
    print(f"Objective Score: {objective_score(solution)}")

    # Number of flows allocated
    total_flows = sum(1 for i in range(0, len(solution), 2) if mf_check(i // 2, solution[i + 1]))
    print(f"Total flows allocated: {total_flows}")

    # Average criticality of allocated flows
    totalCrit = 0
    for i in range(1, len(solution), 2):
        mfIndex = i // 2
        if mf_check(mfIndex, solution[i]):
            totalCrit += solution[i] + 1
    print(f"Average criticality of allocated flows: {totalCrit / total_flows}")

    # Create a DataFrame with the solution details
    solution_df = pd.DataFrame({'Solution': solution})

    # Save the DataFrame to a CSV file
    solution_df.to_csv('solution.csv', index=False)

    if ga_instance.best_solution_generation != -1:
        print(f"Best fitness value reached after {ga_instance.best_solution_generation} generations.")

    # Print metrics
    total_execution_time = sum(generation_times)
    print(f"Total Execution Time: {total_execution_time} seconds")
    print(f"Average Time per Generation: {total_execution_time / len(generation_times)} seconds")

    ga_instance.plot_fitness()

    plotObjectiveScores()

    ga_instance.plot_pareto_front_curve()

    ga_instance.plot_new_solution_rate()

if __name__ == "__main__":
    main()