from DeliveryProblem import DeliveryProblem 
from SimulatedAnnealing import SimulatedAnnealing
from Drone import Drone
from Order import Order
from matplotlib import pyplot as plt
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.cm as cm

if __name__ == "__main__":
    directory = 'methods/simulated-annealing/solutions/'

    numberOfDrones = []
    numberOfPackages = []
    TimeProcessing = []
    sa = SimulatedAnnealing(1000, 0.95)
    
    generate = False
    
    if generate:
        for nb_drone in range(1,11):
            for nb_orders in range(10,51,10):
                print(f"Processing number of drone: {nb_drone}, number: {nb_orders}")
                dp = DeliveryProblem(nb_drone, nb_orders,package_filepath="utils\generationColi\generationRealisticCity\generateData\packagesSmallCity_"+str(nb_orders)+".csv",house_filepath="utils\generationColi\generationRealisticCity\generateData\smallCity_"+str(nb_orders)+".csv")
                best_cost, best_solution, costs = sa.solve(dp, 100, 20)
                
                dp.export_for_visualisation(best_solution, directory + f"solution_{nb_drone}_{nb_orders}.csv")
                
                TimeProcessing.append(int(best_cost))
                numberOfDrones.append(nb_drone)
                numberOfPackages.append(nb_orders)
                
                # export time processing, number of drones and number of packages in a csv file
                np.savetxt(directory + "timeProcessing.csv", TimeProcessing, delimiter=",")
                np.savetxt(directory + "numberOfDrones.csv", numberOfDrones, delimiter=",")
                np.savetxt(directory + "numberOfPackages.csv", numberOfPackages, delimiter=",")
                
    else :
        
        # Import from csv file
        TimeProcessing = np.genfromtxt(directory + "timeProcessing.csv", delimiter=",")
        numberOfDrones = np.genfromtxt(directory + "numberOfDrones.csv", delimiter=",")
        numberOfPackages = np.genfromtxt(directory + "numberOfPackages.csv", delimiter=",")
                
                

    from scipy.interpolate import griddata
    import numpy as np

    # Convert lists to numpy arrays
    numberOfDrones = np.array(numberOfDrones)
    numberOfPackages = np.array(numberOfPackages)
    TimeProcessing = np.array(TimeProcessing)

    # Create a grid
    drone_grid, package_grid = np.mgrid[numberOfDrones.min():numberOfDrones.max():100j, numberOfPackages.min():numberOfPackages.max():100j]

    # Interpolate data on the grid
    time_grid = griddata((numberOfDrones, numberOfPackages), TimeProcessing, (drone_grid, package_grid), method='cubic')

    # Plot the results 3D
    fig = plt.figure()
    ax = plt.axes(projection='3d')
    #ax.plot_surface(drone_grid, package_grid, time_grid, cmap='Reds', edgecolor='none')~
    ax.plot_surface(drone_grid, package_grid, time_grid, cmap='rainbow', edgecolor='none')
    ax.set_xlabel('Number of drones')
    ax.set_ylabel('Number of packages')
    ax.set_zlabel('Delivery Time')
    plt.show()