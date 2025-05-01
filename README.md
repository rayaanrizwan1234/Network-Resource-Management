# Network Resource Management with Criticality-Aware Algorithms

This project implements and evaluates various algorithms for criticality-aware flow allocation across constrained network environments. It includes benchmark generation in Java, multiple Bin Packing (BP) heuristics in Python, and Genetic Algorithm (GA) variants with advanced optimization metrics.

---

## 📁 Project Structure

```
Network-Resource-Management/
│
├── lsi/multinet/binpacking/main.java                  # Java-based synthetic benchmark generator
├── message_flows.csv         # Generated message flow benchmark (auto-generated)
├── networks.csv              # Network capacities and criticality levels (auto-generated)
│
├── readData.py               # Data loading utility for all Python algorithms
│
├── BP/H-BP.py                   # High-criticality Bin Packing (Worst Fit, Best Fit, First Fit)
├── BP/L-BP.py                   # Low-criticality Bin Packing variants
├── BP/cabf.py                   # CABF (Criticality-Aware Best Fit) Bin Packing algorithm
│
├── GA/Seeded-GA.py              # Genetic Algorithm initialized with a BP-based seed
├── GA/Unseeded-GA.py            # Purely random initialization GA variant
│
└── /BP/alloc.csv             # Used by Seeded-GA as initialization input
```

---

## 🛠️ Requirements

- Java (for benchmark generation)
- Python 3.7+
- Python Libraries:
  - `numpy`
  - `pandas`
  - `pygad`
  - `matplotlib`

Install Python dependencies via:

```bash
pip install numpy pandas pygad matplotlib
```

---

## 🚀 How to Run

### 1. Generate Benchmark Files
Navigate to lsi/multinet/binpacking and compile and run the Java benchmark generator to create the required CSV files:

```bash
javac main.java
java main
```

This generates:
- `message_flows.csv` — synthetic message flow benchmark
- `networks.csv` — corresponding network configuration

---

### 2. Run the Allocation Algorithms

Each Python file is already configured and can be executed directly using:

```bash
python3 H-BP.py         # Runs High-criticality Bin Packing (Best Fit by default)
python3 L-BP.py         # Runs Low-criticality Bin Packing
python3 cabf.py         # Runs CABF (Criticality-Aware Best Fit)
python3 Unseeded-GA.py  # Runs GA with random initialization
python3 Seeded-GA.py    # Runs GA initialized with BP allocations
```

Each script will:
- Read the generated benchmark files
- Perform resource allocation
- Print performance metrics (e.g., Objective Score, Running Time, Criticality Levels)
- Save allocation or solution data to a CSV file

---

### 🔍 Exploring GA Variants

Inside the GA implementations:
- `Unseeded-GA.py` explores performance from a randomly initialized population.
- `Seeded-GA.py` uses prior BP allocations to initialize its population for faster convergence.

You can modify parameters like population size, mutation rate, number of generations, and stop criteria directly in the files.

---

## 📊 Output

Most scripts output performance metrics and save results to:

- `alloc.csv` (for BP algorithms)
- `solution.csv` (for GAs)

You can visualize performance trends by enabling the plotting functions within the GA scripts (e.g., `plotObjectiveScores()` and `plotDiversity()`).

---

## 📌 Notes

- Ensure `message_flows.csv` and `networks.csv` are in the correct relative paths.
- To test different criticality levels or networks, adjust parameters inside `main.java` before regenerating benchmarks.

---

## 📣 License

This project is academic in nature and may be extended or modified freely for research purposes.
