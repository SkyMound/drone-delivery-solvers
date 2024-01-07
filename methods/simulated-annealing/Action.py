
class Action:
    def __init__(self, battery_to_charge, order_to_deliver):
        self.battery_to_charge = battery_to_charge
        self.order_to_deliver = order_to_deliver


    def __str__(self):
        return "({} %, {})".format(self.battery_to_charge, str(self.order_to_deliver))