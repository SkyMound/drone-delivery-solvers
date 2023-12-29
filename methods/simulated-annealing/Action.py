from Drone import Drone

class Action:
    def __init__(self, battery_to_charge, order_to_deliver):
        self.battery_to_charge = battery_to_charge
        self.order_to_deliver = order_to_deliver

    def expected_time(self):
        time=  self.order_to_deliver.distance()/Drone.speed
        if self.battery_to_charge > 0 :
            time += Drone.charge_time(self.battery_to_charge)
        
        time += Drone.takeoff_time
        return time

    def __str__(self):
        return "({} %, {})".format(self.battery_to_charge, self.order_to_deliver.order_tostring())