from DeliveryProblem import DeliveryProblem
import numpy as np
import copy
class SimulatedAnnealing :
    def __init__(self, temperature = 1000, factor = 0.95) :
        self.T = temperature
        self.factor = factor
    
    
    def solve(self,problem, iterations, batch_size) :
        T = self.T
        costs= np.zeros(iterations)
        
        current_solution = problem.generate_solution()
        best_solution = copy.deepcopy(current_solution)
        costs[0] = best_cost = current_cost = problem.cost(current_solution)
                

        for i in range(iterations):
            #print('la ',i,'è_me solution = ',solution,' donne le temps maximum de livraison = ',cout0,' la température actuelle =',T)
            T=T*self.factor
            
            # Deep search (optimizing current solution)
            new_solution = problem.optimize(current_solution)
            new_cost = problem.cost(new_solution)
            if new_cost < current_cost:
                current_cost = new_cost
                current_solution = copy.deepcopy(new_solution)
                if new_cost < best_cost:
                    best_cost = new_cost
                    best_solution = copy.deepcopy(new_solution)
            
            # Local search (explore neighbor solutions)
            for j in range(batch_size):
                new_solution = problem.get_neighbor(current_solution)
                new_cost = problem.cost(new_solution)
                if new_cost < current_cost:
                    current_cost = new_cost
                    current_solution = copy.deepcopy(new_solution)
                    if new_cost < best_cost:
                        best_cost = new_cost
                        best_solution = copy.deepcopy(new_solution)
                elif new_cost != current_cost :
                    x=np.random.uniform()
                    if x<np.exp((current_cost-new_cost)/T):
                        print(np.exp((current_cost-new_cost)/T),T,current_cost,new_cost)
                        current_cost=new_cost
                        current_solution=copy.deepcopy(new_solution)
                        
                costs[i] = current_cost
                
        return best_cost, best_solution,costs