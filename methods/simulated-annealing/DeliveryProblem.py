### DeliveryProblem.py ###
#authors : Tobias Fresard and Martin Mickael
#Description : Class to represent a delivery problem


from math import inf
from matplotlib import pyplot as plt
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
    
    def calc_fitness(self, solution) :
        drone_total_time = np.zeros((self.nb_drones))
        drone_theorique_time = np.zeros((self.nb_drones))

        for i in range(self.nb_drones):
            for action in solution.actions[i] :
                drone_total_time[i] += action.expected_time()
                drone_theorique_time[i] += self.expected_time(action.order_to_deliver)

                #Plus utililsé car le temps de charge est géré par les actions
                # if action.battery_to_charge > 0 :
                #     drone_total_time[i] += Drone.charge_time(action.battery_to_charge)
                #     drone_theorique_time[i] += Drone.charge_time(action.battery_to_charge)

        return (np.max(drone_total_time-drone_theorique_time))**2
    
        
    
    def expected_time(self, order) :
        return order.distance()/Drone.speed
    
    def optimize_solution_localy(self, solution) :
        # TODO locally improve the solution


        pass

    def get_neighbor(self, solution) :
        # TODO get a neighbor solution
        # Inverting two actions of the same drone
        # Inverting two actions between two drones
        # mofiying the battery charge of an action
        # find other ideas
        neighbor = solution

        return neighbor
    
    def optimize(self,solution,nb_iterations =1000,factor = 0.9,batch_size=10,heat=100) :
        self.fitness_history = []
        cur_fitness = self.calc_fitness(solution)

        # optimize the solution
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
    

    def plot_solution(self) :
        #plot the solution
        fig, (colis_plots, cout_plot) = plt.subplots(1, 2, sharey=False, figsize=(15, 5))
        fig.suptitle('Simulation de livraison de colis par drone')
        colis_plots.set_title('Position des colis')
        cout_plot.set_title('Evolution du cout')

        #affichage des colis dans le graphe
        #récupération des coordonnées des colis (x,y) 
        x,y = [],[]
        for order in self.orders :
            x.append(order.x)
            y.append(order.y)
        
        colis_plots.scatter(x, y)

        #TODO affichage du dépôt

        #TODO affichage du coup en fonction du nombre d'itérations

        pass