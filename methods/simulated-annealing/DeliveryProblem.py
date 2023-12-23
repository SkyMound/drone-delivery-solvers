### DeliveryProblem.py ###
#authors : Tobias Fresard and Martin Mickael
#Description : Class to represent a delivery problem


import numpy as np
import os
import pandas as pd

from Order import Order
from Drone import Drone
from Action import Action

class DeliveryProblem :
    def __init__(self, nb_drones=10, nb_orders=10, distance_max=5000, filepath=None ):
        self.nb_drones = nb_drones
        self.nb_orders = nb_orders
        self.distance_max = distance_max
        Order.MAX_DISTANCE = distance_max
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
        print(len(self.orders))
                

    
    def generate_solution(self) :
        new_sol = [[] for i in range(self.nb_drones)]
        i = 0
        for order in self.orders :
            
            new_sol[i%self.nb_drones].append(Action(0,order))
            i+=1
        print(new_sol)
        return new_sol
    
    def optimize_solution(self, solution) :
        # TODO Apply heursitics to solution
        pass
    
    def fitness(self, solution) :
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