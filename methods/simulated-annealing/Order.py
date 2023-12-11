import numpy as np


class Order():
    PACKAGE_WEIGHT_MAX = 4 #kg
    MAX_DISTANCE = 5000 #m
    
    def __init__(self, x = None, y = None, weight = None):
        self.x = x or np.random.uniform(-Order.MAX_DISTANCE,Order.MAX_DISTANCE)
        self.y = y or np.random.uniform(-Order.MAX_DISTANCE,Order.MAX_DISTANCE)
        self.weight = weight or np.random.randint(50,Order.PACKAGE_WEIGHT_MAX*1000)/1000
    
    def distance(self) :
        return np.sqrt(self.x**2+self.y**2)*2