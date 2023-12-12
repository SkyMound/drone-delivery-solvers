from DeliveryProblem import DeliveryProblem 

def test() :
    dp = DeliveryProblem(10, 20, 1000)
    solution = dp.generate_solution()
    print(solution)


test()