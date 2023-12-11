import numpy as np
import Order
import Drone

class DeliveryProblem :
    def __init__(self, nb_drones, nb_orders, distance_max):
        self.nb_drones = nb_drones
        self.nb_orders = nb_orders
        self.distance_max = distance_max
        Order.MAX_DISTANCE = distance_max
        
        self.orders = np.zeros(nb_orders)
        for i in range(nb_orders):
            self.orders[i] = Order()
            
        self.drones = np.zeros(nb_drones)
        for i in range(nb_drones):
            self.drones[i] = Drone(i)

    
    def generate_solution(self) :
        return np.random.randint(0,self.nb_orders,self.nb_drones)
    
    def optimize_solution(self, solution) :
        # Apply heursitics to solution
        pass
    
    def fitness(self, solution) :
        sum = 0
        for order_index in range(self.nb_orders) :
            pass
        # sum of (expected time for an order - real time took to realize the order)**2
        pass
    
    def get_neighbor(self, solution) :
        neighbor = solution
        order_to_change = np.random.randint(0,self.nb_orders)
        neighbor[order_to_change] = np.random.randint(0,self.nb_drones)
        return neighbor
        
    
    def expected_time(self, order) :
        return order.distance()/Drone.speed