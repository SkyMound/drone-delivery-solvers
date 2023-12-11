
class Drone :
    
    speed = 8.5 #m/s
    weight = 4 #kg
    takeoff_time = 5 #s
    distance_max_wo_package = 5000 #m
    distance_max_w_package = 14000 #m
    
    
    def __init__(self, ID, starting_battery = 100):
        self.ID = ID
        self.battery = starting_battery
        self.order = None
        
    
        
    
        