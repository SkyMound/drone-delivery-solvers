import numpy as np


class Order():
    PACKAGE_WEIGHT_MAX = 4 #kg
    MAX_DISTANCE = 2000 #m
    DEPOSIT_X = 2461228
    DEPOSIT_Y = 3984254
    
    def __init__(self, ID, house_id, x = None, y = None, weight = None):
        self.ID = ID
        self.house_id = house_id
        self.x = x or np.random.randint(-Order.MAX_DISTANCE,Order.MAX_DISTANCE)
        self.y = y or np.random.randint(-Order.MAX_DISTANCE,Order.MAX_DISTANCE)
        self.weight = weight or np.random.randint(50,Order.PACKAGE_WEIGHT_MAX*1000)/1000
        self.distance = np.sqrt((self.x-Order.DEPOSIT_X)**2+(self.y-Order.DEPOSIT_Y)**2)
    
    def __eq__(self, __value: object) -> bool:
        return self.ID==__value.ID
    
    def __str__(self) :
        return "[Order : x={}, y={}, w={}]".format(self.x, self.y, self.weight)