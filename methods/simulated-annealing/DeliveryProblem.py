import numpy as np
from Order import Order
from Drone import Drone

class DeliveryProblem :
    def __init__(self, nb_drones, nb_orders, distance_max):
        #TODO : redo this to be able to generate a problem with different orders and drones only if the parameters filepath is not given
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
        new_sol = np.array([] for i in range(self.nb_drones))
        orders = np.random.shuffle(np.arange(self.nb_orders))
        i = 0
        for order in orders :
            new_sol[i%self.nb_drones].append(order)
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