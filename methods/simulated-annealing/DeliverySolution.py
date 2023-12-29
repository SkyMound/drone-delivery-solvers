from Action import Action
class DeliverySolution :

    def __init__(self,solution) :
        self.solution = solution
        
    def print_solution(self) :

        for drone_nb,action_list in enumerate(self.solution) :
            sol_str = "Drone {} : [".format(drone_nb)
            for action in action_list :
                sol_str += "\n            "+action.action_tostring()
            print(sol_str+"]")
        