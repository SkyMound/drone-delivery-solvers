import os
import re
import matplotlib.pyplot as plt
import matplotlib.cm as cm
import random

directory = 'methods/glpk-solver/solution/'

numberOfDrones = []
numberOfPackages = []
TimeProcessing = []

for numberOfDrone in range(1,11):
    for numberOfPackage in range(10,51,10):
        print(f"Processing number of drone: {numberOfDrone}, number: {numberOfPackage}")
        filename = f"solver_drone_solution_{numberOfPackage}_{numberOfDrone}.log"
        with open(os.path.join(directory, filename), 'r') as f:
            data = f.read()
            data = data.split("\n")
            for line in data:
                if "time_max" in line and "check" not in line:
                    time = line.replace(' ','').split("*")
                    TimeProcessing.append(int(time[1]))
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

import numpy as np
from matplotlib.ticker import FuncFormatter

# Prendre le logarithme des données
log_time_grid = np.log10(time_grid)

# Plot the results 3D
fig = plt.figure()
ax = plt.axes(projection='3d')
ax.plot_surface(drone_grid, package_grid, log_time_grid, cmap='rainbow', edgecolor='none')

# Définir les labels de l'axe z pour afficher les valeurs d'origine
formatter = FuncFormatter(lambda x, pos: "{:.0f}".format(10**x))
ax.zaxis.set_major_formatter(formatter)

ax.set_xlabel('Number of drones')
ax.set_ylabel('Number of packages')
ax.set_zlabel('Delivery Time (log scale)')
plt.show()