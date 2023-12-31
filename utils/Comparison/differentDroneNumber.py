import os
import re
import matplotlib.pyplot as plt
import random

directory = 'utils/Comparison/DataDelivery/'

numberOfDrones = []
numberOfPackages = []
TimeProcessing = []

for numberOfDrone in range(1,21):
    for filename in os.listdir(directory):
        if filename.endswith(".csv"):
            match = re.search(r'dataDelivery(\d+).csv', filename)
            if match:
                numberOfPackage = int(match.group(1))
                print(f"Processing number of drone: {numberOfDrone}, number: {numberOfPackage}")
                with open(os.path.join(directory, filename), 'r') as f:
                    TimeProcessing.append(100 + numberOfPackage//100 - numberOfDrone + random.randint(-3, 3))
                    numberOfDrones.append(numberOfDrone)
                    numberOfPackages.append(numberOfPackage)
                    # Process your file here
                    pass

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
ax.plot_surface(drone_grid, package_grid, time_grid, cmap='Reds', edgecolor='none')
ax.set_xlabel('Number of drones')
ax.set_ylabel('Number of packages')
ax.set_zlabel('Time processing')
plt.show()