from DeliveryProblem import DeliveryProblem 
from SimulatedAnnealing import SimulatedAnnealing
from Drone import Drone
from Order import Order
from matplotlib import pyplot as plt
def test() :
    a = DeliveryProblem.import_for_visualisation("test.csv")
    print(a)
    # dp = DeliveryProblem(5, 40,filepath="dataDelivery100.csv")
    # sa = SimulatedAnnealing(1000, 0.95)
    
    # best_cost, best_solution, costs = sa.solve(dp, 100, 20)
    # print(best_cost)
    # dp.print_solution(best_solution)
    
    # dp.export_for_visualisation(best_solution, "test.csv")
    # plot costs
    # plt.plot(costs)
    # plt.show()

test()