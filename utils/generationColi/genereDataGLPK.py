import pandas as pd
import numpy as np

fileGenerated = "solver_drone_100.dat" #name of the file generated
cityFile = "city1.csv" #name of the file of the city used
packageFile = "dataDelivery100.csv" #name of the file of the packages used

# Manual parameters
nb_drones = 100
autonomy_max = 100
autonomy_initial = 100
recharge_time = 10
depotX = 2461228
depotY = 3984254

houses = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + cityFile)
nb_houses = len(houses)
delivery_time = []

for i in range(nb_houses):
    dist = int(np.floor(np.sqrt((depotX - houses["X"][i])**2 + (depotY - houses["Y"][i])**2)))
    if i == 0:
        delivery_time.append("param delivery_time := 1 " + str(dist) + "\n")
    elif i == nb_houses-1:
        delivery_time.append(str(i+1) + " " + str(dist) + ";\n\n")
    else:
        delivery_time.append(str(i+1) + " " + str(dist) + "\n")


packages = pd.read_csv("utils/Comparison/DataDelivery/" + packageFile)
nb_packages = len(packages)
orders = []

for i in range(nb_packages):
    if i == 0:
        orders.append("param order := 1 " + str(packages["idMaison"][i]) + "\n")
    elif i == nb_packages-1:
        orders.append(str(i+1) + " " + str(packages["idMaison"][i]) + ";\n\n")
    else:     
        orders.append(str(i+1) + " " + str(packages["idMaison"][i]) + "\n")

res = open("utils/generationColi/generationRealisticCity/generateData/" + fileGenerated, "w")

res.write("data;\n\nparam nb_p := " + str(nb_packages) + "\nparam nb_d := " + str(nb_drones) + "\nparam nb_h := " + str(nb_houses) + "\n")
res.write("param autonomy_max := " + str(autonomy_max) + "\nparam autonomy_initial := " + str(autonomy_initial) + "\n")
res.write("param recharge_time := " + str(recharge_time) + "\n\n")

res.writelines(delivery_time)
res.writelines(orders)
res.write("end;\n")