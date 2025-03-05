import pygad
import numpy as np
import pandas as pd
import time
import math

# Constants and Global Variables
NETWORKS = [64000, 1760, 48]
DATA_FILE = '../message_flows.csv'

CRIT = []
# MAKE SURE TO CHANGE THIS WHEN CHANGING CRITICALITY LEVELS
L = 3

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

def read_data(file_path):
    global CRIT
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
       total_cost[net] += (crit_c[mfIndex] / crit_t[mfIndex])


       # fitness squared
       fitness += ((L - crit) ** 2)

   # Adjust fitness based on the difference between cost and network capability
   invalid = False
   diff = 0
   for i, cost in enumerate(total_cost):
       if cost > NETWORKS[i]:
           invalid = True
           diff += ((cost - NETWORKS[i]) / NETWORKS[i]) * 200   
   if invalid:
       fitness = - diff
   else:
       fitness = (fitness**2)
   res = [fitness]


   # number of allocated flows
   allocated_flows = sum(1 for i in range(0, len(solution), 2) if mf_check(i // 2, solution[i + 1]))
   res.append(allocated_flows)
  
   return fitness

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
    best_fitness_values.append(best_solution_fitness)
    average_fitness = np.mean(ga_instance.last_generation_fitness)
    average_fitness_values.append(average_fitness)
    
    diversity = calculate_diversity(ga_instance.population)
    diversity_values.append(diversity)
    
    print(f"Generation = {ga_instance.generations_completed}")
    print(f"Best Fitness = {best_solution_fitness}")

def main():
    """Main function to run the genetic algorithm."""
    read_data(DATA_FILE)

    ga_instance = pygad.GA(num_generations=100,
                           num_parents_mating=30,
                           sol_per_pop=80,
                           num_genes=len(CRIT[0][0]) * 2,
                           fitness_func=fitness_func2,
                           gene_type=int,
                           on_generation=on_generation,
                           gene_space=[{'low': 0, 'high': 3}, {'low': 1, 'high': 3}] * len(CRIT[0][0]), # CHANGE THIS WHEN CHANGING CRITICALITY
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
    solution_df.to_csv('/Users/rayaanrizwan/Desktop/Year 3/Dissertation/code/Network-Resource-Management/solution.csv', index=False)

    if ga_instance.best_solution_generation != -1:
        print(f"Best fitness value reached after {ga_instance.best_solution_generation} generations.")

    # Print metrics
    total_execution_time = sum(generation_times)
    print(f"Total Execution Time: {total_execution_time} seconds")
    print(f"Average Time per Generation: {total_execution_time / len(generation_times)} seconds")

    ga_instance.plot_fitness()

    ga_instance.plot_pareto_front_curve()

    ga_instance.plot_new_solution_rate()

if __name__ == "__main__":
    main()