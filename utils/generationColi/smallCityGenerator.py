import pandas as pd
import numpy as np

nb_voulu = 30

fileGenerated = "smallCity_"+str(nb_voulu)+".csv" #name of the file generated
cityFile = "city1.csv" #name of the file of the city used

houses = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + cityFile)

res = open("utils/generationColi/generationRealisticCity/generateData/" + fileGenerated, "w")
res.write("id,X,Y,Maison\n")

index = 0
for i in range(len(houses['Maison'])):
    if i%np.floor(len(houses['Maison'])/nb_voulu) == 0:
        index += 1
        res.write(str(index) + "," + str(houses['X'][i]) + "," + str(houses['Y'][i]) + "," + str(houses['Maison'][i]) + "\n")