import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Prepare the data
data = {
    "Benchmark": [],
    "Mutation Type": [],
    "Objective Score": [],
    "Allocated Flows": [],
    "Running Time": [],
    "Generations to Converge": [],
    "Average Diversity": []
}

# Benchmark data
benchmarks = {
    "1": [
        ("Random", 1211, 469, 2.56, 14, 0.376),
        ("Swap", 1248, 483, 34, 100, 0.1),
        ("Inversion", 1211, 470, 13.56, 63, 0.71),
        ("Scramble", 1202, 477, 8.73, 53, 0.71),
    ],
    "2": [
        ("Random", 3425, 1369, 5.67, 11, 0.39),
        ("Swap", 3131, 1357, 35.68, 100, 0.07),
        ("Inversion", 3428, 1363, 49.2, 93, 0.73),
        ("Scramble", 3423, 1365, 41.17, 78, 0.73),
    ],
    "3": [
        ("Random", 5673, 2265, 9.72, 11, 0.39),
        ("Swap", 5077, 2213, 50.96, 100, 0.08),
        ("Inversion", 5312, 2207, 52.43, 93, 0.73),
        ("Scramble", 5585, 2257, 34.04, 84, 0.74),
    ],
    "4": [
        ("Random", 7897, 3134, 13.56, 13, 0.21),
        ("Swap", 6888, 3049, 65.48, 100, 0.12),
        ("Inversion", 6263, 2891, 6.28, 0, 1.19),
        ("Scramble", 6280, 2890, 8.66, 0, 1.82),
    ],
    "5": [
        ("Random", 10205, 4044, 18.34, 14, 0.65),
        ("Swap", 8472, 3794, 60, 100, 0.1),
        ("Inversion", 8018, 3675, 7.59, 0, 1.69),
        ("Scramble", 7986, 3673, 8.63, 0, 1.63),
    ],
}

# Populate the data dictionary
for benchmark, records in benchmarks.items():
    for record in records:
        mutation, score, flows, time, gen, diversity = record
        data["Benchmark"].append(benchmark)
        data["Mutation Type"].append(mutation)
        data["Objective Score"].append(score)
        data["Allocated Flows"].append(flows)
        data["Running Time"].append(time)
        data["Generations to Converge"].append(gen)
        data["Average Diversity"].append(diversity)

# Convert to DataFrame
df = pd.DataFrame(data)

# Set plot style
sns.set(style="whitegrid")

# Function to plot grouped bar charts
def plot_metric(metric, ylabel):
    plt.figure(figsize=(6, 4))
    ax = sns.barplot(data=df, x="Benchmark", y=metric, hue="Mutation Type")
    plt.title(f"{metric} Comparison Across Benchmarks")
    plt.ylabel(ylabel)
    plt.xlabel("Benchmark")
    plt.legend(title="Mutation Type")
    plt.tight_layout()
    plt.grid(True, axis='y')
    plt.show()

# Plot all metrics
plot_metric("Objective Score", "Objective Score")
plot_metric("Allocated Flows", "Number of Allocated Flows")
plot_metric("Running Time", "Running Time (s)")
plot_metric("Generations to Converge", "Generations Until Convergence")
plot_metric("Average Diversity", "Average Diversity")
