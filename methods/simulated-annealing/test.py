from DeliveryProblem import DeliveryProblem 

def test() :
    dp = DeliveryProblem(filepath="data3.csv", nb_drones=100)
    solution = dp.generate_solution()
    print(solution)


test()