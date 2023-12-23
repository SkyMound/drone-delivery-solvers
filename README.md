# Package Delivery Optimization by Drones

## Project Objective
The project aims to address the optimization challenge of package delivery using a swarm of drones in a medium-sized city, with the primary goal of minimizing the total delivery time. The city of Grenoble, France, serves as an example for this project.

## Optimization Methods

### 1. Exact Solver based on GLPK
Utilizing an exact solver based on the GNU Linear Programming Kit (GLPK) to solve the optimization problem. This approach ensures an optimal solution but may be resource-intensive for large problem instances.

### 2. Simulated Annealing
Implementing a simulated annealing method for optimization. Simulated annealing is a stochastic optimization technique that can provide near-optimal solutions with more efficient resource utilization.

### 3. Agent-Based Application in SARL
Developing an agent-based application in SARL (Smart Agent Oriented Language) to model and simulate the behavior of drones in package delivery. This agent-based approach accommodates dynamics and adaptability in the delivery environment.

## Input Data
The system takes into account data such as the geographical dimensions of the city, weight constraints of packages, possible routes, and other relevant parameters.

## Overall Project Architecture
The project is structured into three distinct modules, each corresponding to one of the optimization methods. Results obtained by each method are compared to assess the performance and efficiency of each.

## Languages and Libraries Used
- Exact Solver: Implementation using the  GNU Linear Programming Kit (GLPK) and the GNU MathProg modeling language

- Simulated Annealing: Implementation in python using 
    - `numpy` for matrix operations
    - `matplotlib` for visualization
    - `pandas` for data analysis

- SARL: Utilizing the SARL language in a Maven project to be run on the Janus platform for agent-based simulation. 


## Usage Instructions
- Exact Solver: [insert instructions]
- Simulated Annealing: [insert instructions]
- SARL: [insert instructions]


## Usage Examples

## Base for the project 
### Articles
• Efficient Large-Scale Multi-Drone Delivery Using Transit Networks

• Last mile delivery by drones: an estimation of viable market potential and access to
citizens across European cities

• A Hybrid Genetic Algorithm for the Traveling Salesman Problem with Drone

• Optimization of Multi-package Drone Deliveries Considering Battery Capacity:

https://www.researchgate.net/publication/347485538_Optimization_of_Multipackage_Drone_Deliveries_Considering_Battery_Capacity

### Code samples
• https://github.com/sisl/MultiAgentAllocationTransit.jl

• https://github.com/utiasDSL/gym-pybullet-drones
Feel free to make any adjustments or additions, and let me know if there's anything else you'd like to include in the README.md.