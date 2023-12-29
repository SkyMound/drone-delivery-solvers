from DeliveryProblem import DeliveryProblem
import numpy as np
class SimulatedAnnealing :
    def __init__(self, temperature = 1000, factor = 0.95) :
        self.T = temperature
        self.factor = factor
    
    
    def solve(self,problem, iterations, batch_size) :
        T = self.T
        costs= np.zeros(iterations)
        
        best_solution = problem.generate_solution()
        best_solution = np.copy(current_solution)
        costs[0] = best_cost = current_cost = problem.fitness(current_solution)
                

        for i in range(iterations):
            #print('la ',i,'è_me solution = ',solution,' donne le temps maximum de livraison = ',cout0,' la température actuelle =',T)
            T=T*self.factor
            
            # Deep search (optimizing current solution)
            new_solution = problem.optimize_solution(current_solution)
            new_cost = problem.fitness(new_solution)
            if new_cost < current_cost:
                current_cost = new_cost
                current_solution = np.copy(new_solution)
                if new_cost < best_cost:
                    best_cost = new_cost
                    best_solution = np.copy(new_solution)
            
            # Local search (explore neighbor solutions)
            for j in range(batch_size):
                new_solution = problem.get_neighbor(current_solution)
                new_cost = problem.fitness(new_solution)
                costs[i] = new_cost
                if new_cost < current_cost:
                    current_cost = new_cost
                    current_solution = np.copy(new_solution)
                    if new_cost < best_cost:
                        best_cost = new_cost
                        best_solution = np.copy(new_solution)
                else:
                    x=np.random.uniform()
                    if x<np.exp((current_cost-new_cost)/T):
                        current_cost=new_cost
                        current_solution=np.copy(new_solution)
    
        return best_cost, best_solution,costs