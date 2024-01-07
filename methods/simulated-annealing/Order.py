import numpy as np


class Order():
    PACKAGE_WEIGHT_MAX = 4 #kg
    MAX_DISTANCE = 2000 #m
    
    def __init__(self, ID, x = None, y = None, weight = None, order_time = None):
        self.ID = ID
        self.x = x or np.random.randint(-Order.MAX_DISTANCE,Order.MAX_DISTANCE)
        self.y = y or np.random.randint(-Order.MAX_DISTANCE,Order.MAX_DISTANCE)
        self.weight = weight or np.random.randint(50,Order.PACKAGE_WEIGHT_MAX*1000)/1000
        self.distance = np.sqrt(self.x**2+self.y**2)*2
        self.order_time = order_time or 0
    
    def __eq__(self, __value: object) -> bool:
        return self.ID==__value.ID
    
    def __str__(self) :
        return "[Order : x={}, y={}, w={}]".format(self.x, self.y, self.weight)