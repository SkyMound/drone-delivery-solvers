from Order import Order
from Action import Action
class Drone :
    
    speed = 8.5 #m/s
    weight = 4 #kg
    takeoff_time = 5 #s
    landing_time = 5 #s
    
    charging_ratio = 1.5
    takeoff_battery_loss = 1 #%
    takeoff_ratio = 1/4
    distance_max = 15000 #m
    
    def __init__(self, ID, starting_battery = 100):
        self.ID = ID
        self.battery = starting_battery
        self.order = None
        
    # charge the drone's battery by a given amount (in %) considering  0 to 100% in 1h30
    def charge(self, to_charge):
        self.battery += to_charge
        return to_charge*Drone.charging_ratio*3600/100

        
    def battery_needed(self, order : Order):
        # distance max with with this particular package
        distance_max_with_package = Drone.distance_max*(Drone.weight+order.weight)/Drone.weight
        percentage_battery_needed = order.distance*100/distance_max_with_package + order.distance*100/Drone.distance_max
        # takeoff and landing
        percentage_battery_needed += (Drone.weight+order.weight)*Drone.takeoff_ratio + Drone.takeoff_battery_loss
        # return percentage rounded to the superior integer
        return int(percentage_battery_needed+0.99)
    
    def time_needed(order : Order):
        time = order.distance/Drone.speed
        time += Drone.takeoff_time * 2
        time += Drone.landing_time * 2
        return time
    
    def do_action(self, action : Action) :
        time_taken = self.charge(action.battery_to_charge)
        time_taken += Drone.time_needed(action.order_to_deliver)
        self.battery -= self.battery_needed(action.order_to_deliver)
        return time_taken