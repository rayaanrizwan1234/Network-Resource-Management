import pygad
import numpy as np

networks = [64000, 1760, 48] 
crit_1_c = [1000, 1000, 30, 40000, 80, 40000, 40000, 40]
crit_1_t = [10, 5, 30, 10, 10, 10, 10, 3600]

crit_2_c = [40, 80, 10, 10, 10, 10, 10, None]
crit_2_t = [20, 10, 120, 30, 30, 30, 30, None]

crit_3_c = [10, 10, None, None, None, None, None, None]
crit_3_t = [60, 20, None, None, None, None, None, None, None]

"""
 a chromosome would be encoded by [net, crit, net, crit, net, crit, net, crit]
"""

def fitness_func(ga_instance, solution, solution_idx):
    # Calculate the total cost for each network
    total_cost = [0] * len(networks)
    for i in range(0, len(solution), 2):
        net = solution[i]
        crit = solution[i+1]
        mfIndex  = i // 2
        if not mfCheck(mfIndex, crit):
            return -1
        if crit == 1:
            total_cost[net] += (crit_1_c[mfIndex] / crit_1_t[mfIndex])
        elif crit == 2:
            total_cost[net] += (crit_2_c[mfIndex] / crit_2_t[mfIndex])
        elif crit == 3:
            total_cost[net] += (crit_3_c[mfIndex] / crit_3_t[mfIndex])

    # Check if any network exceeds its limit
    for i, cost in enumerate(total_cost):
        if cost > networks[i]:
            return -1  # Invalid solution
    
    # Calculate the fitness value
    fitness = 0
    for i in range(1, len(solution), 2):
        crit = solution[i]
        if crit == 1:
            fitness += 3
        elif crit == 2:
            fitness += 2
        elif crit == 3:
            fitness += 1
    
    return fitness

def mfCheck(i, crit):
    if crit == 1:
        if crit_1_c[i] is None:
            return False
    if crit == 2:
        if crit_2_c[i] is None:
            return False
    if crit == 3:
        if crit_3_c[i] is None:
            return False
    return True

# initial_population = [(0, 1), (0, 1), (0, 1), (0, 1), (0, 1), (-1, -1), (-1, -1), (0, 1)]

def on_generation(ga_instance):
    print(f"Generation = {ga_instance.generations_completed}")
    print(f"Fitness    = {ga_instance.best_solution(pop_fitness=ga_instance.last_generation_fitness)[1]}")
    
ga_instance = pygad.GA(num_generations=20,
                       num_parents_mating=2,
                       sol_per_pop=5,
                       num_genes=len(crit_1_c) * 2,
                       fitness_func=fitness_func,
                       gene_type=int,
                       on_generation=on_generation,
                       gene_space=[{'low': 0, 'high': 2}, {'low': 1, 'high': 3}] * len(crit_1_c)
                       )

# Running the GA to optimize the parameters of the function.
ga_instance.run()

ga_instance.plot_fitness()

# Returning the details of the best solution.
solution, solution_fitness, solution_idx = ga_instance.best_solution(ga_instance.last_generation_fitness)
print(f"Parameters of the best solution : {solution}")
print(f"Fitness value of the best solution = {solution_fitness}")
print(f"Index of the best solution : {solution_idx}")

if ga_instance.best_solution_generation != -1:
    print(f"Best fitness value reached after {ga_instance.best_solution_generation} generations.")

# Saving the GA instance.
filename = 'genetic.txt' # The filename to which the instance is saved. The name is without extension.
ga_instance.save(filename=filename)

# Loading the saved GA instance.
loaded_ga_instance = pygad.load(filename=filename)
loaded_ga_instance.plot_fitness()
    