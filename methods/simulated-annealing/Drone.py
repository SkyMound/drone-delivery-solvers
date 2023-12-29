
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
        
    # charge the drone's battery by a given amount (in %) considering  0 to 100% in 1h30
    def charge_time(self, to_charge):
        #TODO make this function more realistic (considering the battery's charge rate)
        return to_charge*1.5*3600/100
    
        
    
        