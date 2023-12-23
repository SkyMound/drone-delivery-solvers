### DeliveryProblem.py ###
#authors : Tobias Fresard and Martin Mickael
#Description : Class to represent a delivery problem


from math import inf
import numpy as np
import os
import pandas as pd

from Order import Order
from Drone import Drone
from Action import Action
from DeliverySolution import DeliverySolution

class DeliveryProblem :
    def __init__(self, nb_drones=10, nb_orders=10, distance_max=5000, filepath=None ):
        
        #Initialisation of the problem
        self.nb_drones = nb_drones
        self.nb_orders = nb_orders
        self.distance_max = distance_max
        Order.MAX_DISTANCE = distance_max

        #Initialisation of the orders and drones lists randomly or from a file
        self.orders = []
        self.drones = []
        if filepath is None :
            for i in range(nb_orders):
                self.orders.append(Order())
                
            for i in range(nb_drones):
                self.drones.append(Drone(i))
        else :
            if os.path.exists("utils/generationColi/generationRealisticCity/generateData/" + filepath) :
                print("Getting data from " + filepath)
                df_ville = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/city1.csv")
                df_commandes = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + filepath)
                nb_orders = df_commandes.shape[0]
                for i in range(nb_orders):
                    # we get the house id in the command file and we get the house position in the city file
                    x = df_ville.loc[df_commandes['idMaison'][i], 'X']
                    y = df_ville.loc[df_commandes['idMaison'][i], 'Y']
                    self.orders.append(Order(x,y,df_commandes['Weight'][i]))
            else : 
                print("File not found")
                exit(1)

        #Initialisation of the solution and its fitness
        self.best_solution = self.generate_solution()
        self.best_fitness = self.calc_fitness(self.solution)

        print(len(self.orders))
                

    
    def generate_solution(self) :

        new_sol = [[] for i in range(self.nb_drones)]
        i = 0
        for order in self.orders :
            new_sol[i%self.nb_drones].append(Action(0,order))
            i+=1
        # print initial solution
        self.initial_solution.print_solution()

        return DeliverySolution(new_sol)
    
    def optimize_solution_localy(self, solution) :
        # TODO locally improve the solution

        pass
    
    def calc_fitness(self, solution) :
        sum = 0
        for order_index in range(self.nb_orders) :
            pass
        #TODO  sum of (expected time for an order - real time took to realize the order)**2
        pass
    
    def get_neighbor(self, solution) :
        neighbor = solution
        order_to_change = np.random.randint(0,self.nb_orders)
        neighbor[order_to_change] = np.random.randint(0,self.nb_drones)
        return neighbor
        
    
    def expected_time(self, order) :
        return order.distance()/Drone.speed
    
    def optimize(self,solution,nb_iterations =1000,factor = 0.9,batch_size=10,heat=100) :
        self.fitness_history = []
        cur_fitness = self.calc_fitness(solution)

        #TODO optimize the solution
        for i in range(nb_iterations):
            #print('la ',i,'è_me solution = ',solution,' donne le temps maximum de livraison = ',cout0,' la température actuelle =',T)
            T=T*self.factor

            self.fitness_history.append(cur_fitness)
            for j in range(batch_size):
                #on génère une solution voisine
                solution_voisine=self.get_neighbor(solution)
                #on calcule le cout de la solution voisine
                new_fitness = self.calc_fitness(solution_voisine)

                delta_cout=new_fitness-cur_fitness

                if delta_cout<=0:
                    cur_fitness=new_fitness
                    solution=solution_voisine
                    if new_fitness<self.best_fitness:
                        self.best_fitness=new_fitness
                        self.best_solution=solution
                else:
                    x=np.random.uniform()
                    if x<np.exp(-(delta_cout/T)):
                        cur_fitness=new_fitness
                        solution=solution_voisine
        print('la meilleure solution est ',self.best_solution,' avec un temps maximum de livraison de ',self.best_fitness,' secondes')

        pass