### DeliveryProblem.py ###
#authors : Tobias Fresard and Martin Mickael
#Description : Class to represent a delivery problem


from math import inf
from matplotlib import pyplot as plt
import copy
import numpy as np
import os
import pandas as pd

from Order import Order
from Drone import Drone
from Action import Action
from DeliverySolution import DeliverySolution

class DeliveryProblem :
    def __init__(self, nb_drones=10, nb_orders=10, distance_max=5000, package_filepath=None, house_filepath=None):
        
        #Initialisation of the problem
        self.nb_drones = nb_drones
        self.nb_orders = nb_orders
        self.distance_max = distance_max
        Order.MAX_DISTANCE = distance_max/2

        #Initialisation of the orders and drones lists randomly or from a file
        self.orders = []
        self.drones = []
        if package_filepath is None :
            for i in range(nb_orders):
                self.orders.append(Order(i))
                
            for i in range(nb_drones):
                self.drones.append(Drone(i))
        else :
            if os.path.exists(package_filepath) and os.path.exists(house_filepath) :
                print("Getting data from " + package_filepath + " and " + house_filepath)
                df_ville = pd.read_csv(house_filepath)
                df_commandes = pd.read_csv(package_filepath)
                for i in range(nb_orders):
                    # we get the house id in the command file and we get the house position in the city file
                    x = df_ville.loc[df_commandes['Maison'][i], 'X']
                    y = df_ville.loc[df_commandes['Maison'][i], 'Y']
                    self.orders.append(Order(i,df_commandes['Maison'][i],x,y,df_commandes['Weight'][i]))
            else : 
                print("File not found")
                exit(1)


        print(len(self.orders))
    
    def rectify(self, solution) :
        new_sol = copy.deepcopy(solution)
        for drones_actions in new_sol:
            drone = Drone(0)
            for i,action in enumerate(drones_actions) :
                if action.battery_to_charge > 100 :
                    action.battery_to_charge = 100
                elif action.battery_to_charge < 0 :
                    action.battery_to_charge = 0
                drone.battery += action.battery_to_charge 
                if drone.battery > 100 :
                    drone.battery = 100
                drone.battery -= drone.battery_needed(action.order_to_deliver)
                if drone.battery < 0 :
                  drones_actions[i].battery_to_charge -= drone.battery
                  drone.battery = 0
            
        return new_sol
        
    def optimize(self, solution) :
        new_sol = copy.deepcopy(solution)
        
        
        # Not overcharging the battery
        for drones_actions in new_sol:
            for i in range(len(drones_actions)) :
                drones_actions[i].battery_to_charge = 0
                
        # Assigning an order of the drone taking the most time to the drone taking the least time
        drone_delivery_time = np.zeros(self.nb_drones)

        for i, drone_actions in enumerate(new_sol) :
            for action in drone_actions :
                drone_delivery_time[i] += Drone.time_needed(action.order_to_deliver)
        drone_most_time_index = np.argmax(drone_delivery_time)
        drone_least_time_index = np.argmin(drone_delivery_time)
        action_index = np.random.randint(0, len(new_sol[drone_most_time_index]))
        
        action = new_sol[drone_most_time_index].pop(action_index)
        new_sol[drone_least_time_index].append(action)
        return self.rectify(new_sol)
    
    def generate_solution(self) :

        new_sol = [list() for i in range(self.nb_drones)]
        
        for i,order in enumerate(self.orders) :
            new_sol[i%self.nb_drones].append(Action(0,order))
            
        new_sol = self.rectify(new_sol)

        return new_sol
    
    def cost(self, solution) :
        orders_theoric_time = np.array([Drone.time_needed(order) for order in self.orders])
        orders_real_time = np.zeros(len(self.orders))

        for drone_actions in solution :
            current_time = 0
            drone = Drone(0)
            for action in drone_actions :
                current_time += drone.do_action(action)
                orders_real_time[action.order_to_deliver.ID] = current_time

        return np.max(orders_real_time-orders_theoric_time)
    

    def get_neighbor(self, solution) :
        # TODO get a neighbor solution
        # Inverting two actions of the same drone
        # Assigning an action to another drone
        # Modifying the battery of an action
        neighbor = copy.deepcopy(solution)
        random = np.random.randint(0, 2)
        random = 1
        if random == 0 : # Inverting two actions of the same drone
            drone_index = np.random.randint(0, self.nb_drones)
            while(len(neighbor[drone_index]) == 0) :
                drone_index = np.random.randint(0, self.nb_drones)
            action_index_1 = np.random.randint(0, len(neighbor[drone_index]))
            action_index_2 = np.random.randint(0, len(neighbor[drone_index]))
            (neighbor[drone_index][action_index_1], neighbor[drone_index][action_index_2]) = (neighbor[drone_index][action_index_2], neighbor[drone_index][action_index_1])
        
        elif random == 1 : # Assigning an action to another drone
            
            drone_index_1 = np.random.randint(0, self.nb_drones)
            drone_index_2 = np.random.randint(0, self.nb_drones)
            while(len(neighbor[drone_index_1]) == 0) :
                drone_index_1 = np.random.randint(0, self.nb_drones)
            action_index = np.random.randint(0, len(neighbor[drone_index_1]))
            action = neighbor[drone_index_1].pop(action_index)
            neighbor[drone_index_2].append(action)
            
        else : # Modifying battery of an action
            drone_index = np.random.randint(0, self.nb_drones)
            while(len(neighbor[drone_index]) == 0) :
                drone_index = np.random.randint(0, self.nb_drones)
            action_index = np.random.randint(0, len(neighbor[drone_index]))
            neighbor[drone_index][action_index].battery_to_charge += np.random.randint(-10,0)
            
        return self.rectify(neighbor)
    
    def print_solution(self, solution) :
        str = ""
        for i,drone_actions in enumerate(solution) :
            str += "Drone {} : ".format(i)
            for action in drone_actions :
                str += "{} ".format(action)
            str += "\n"
        print(str)
    
    def export_for_visualisation(self, solution, filename) :
        # export using pandas dataframe to csv with the following format :
        # drone_id, battery_to_charge, house_id
        df = pd.DataFrame(columns=['drone_id', 'battery_to_continue', 'house_id'])
        for i,drone_actions in enumerate(solution) :
            drone = Drone(i)
            for action in drone_actions :
                drone.charge(action.battery_to_charge)
                df.loc[-1] = {'drone_id': i, 'battery_to_continue': drone.battery, 'house_id': action.order_to_deliver.house_id}  # adding a row
                drone.battery -= drone.battery_needed(action.order_to_deliver)
                df.index = df.index + 1  # shifting index
        
        df.to_csv(filename, index=False, header=True)
    
    @staticmethod
    def import_for_visualisation(filename) :
        df = pd.read_csv(filename)
        solution = [[]]
        current_drone_id = 0
        for i in range(len(df)):
            if(current_drone_id != df['drone_id'][i]) :
                current_drone_id = df['drone_id'][i]
                solution.append([])
            solution[current_drone_id].append((df['battery_to_continue'][i],df['house_id'][i]))
        
        return solution
    
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