import pandas as pd
import numpy as np

nb_voulu = 50

fileGenerated = "solver_drone_data_"+str(nb_voulu)+".dat" #name of the file generated
cityFile = "smallCity_"+str(nb_voulu)+".csv" #name of the file of the city used
packageFile = "packagesSmallCity_"+str(nb_voulu)+".csv" #name of the file of the packages used

houses = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + cityFile)
packages = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + packageFile)

# Manual parameters
nb_drones = 20
nb_houses = len(houses['Maison'])
nb_packages = len(packages['idMaisonReel'])
autonomy_max = 1764
recharge_time = 3
depotX = 2461228
depotY = 3984254


delivery_time = []
orders = []
for i in range(nb_houses):
    dist = int(np.floor(np.sqrt((depotX - houses["X"][i])**2 + (depotY - houses["Y"][i])**2)/8.5))
    if i == 0:
        delivery_time.append("param delivery_time := 1 " + str(dist) + "\n")
    elif i == nb_houses-1:
        delivery_time.append(str(i+1) + " " + str(dist) + ";\n\n")
    else:
        delivery_time.append(str(i+1) + " " + str(dist) + "\n")



for i in range(nb_packages):
    if i == 0:
        orders.append("param orders := 1 " + str(packages["Maison"][i]) + "\n")
    elif i == nb_packages-1:
        orders.append(str(i+1) + " " + str(packages["Maison"][i]) + ";\n\n")
    else:     
        orders.append(str(i+1) + " " + str(packages["Maison"][i]) + "\n")

res = open("methods/glpk-solver/" + fileGenerated, "w")

res.write("data;\n\nparam nb_p := " + str(nb_packages) + ";\nparam nb_d := " + str(nb_drones) + ";\nparam nb_h := " + str(nb_houses) + ";\n")
res.write("param autonomy_max := " + str(autonomy_max) + ";\n")
res.write("param recharge_time := " + str(recharge_time) + ";\n\n")

res.writelines(delivery_time)
res.writelines(orders)
res.write("end;\n")