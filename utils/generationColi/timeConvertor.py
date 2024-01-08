import pandas as pd
import numpy as np
import random

cityFile = "packagesSmallCity_"+str(30)+".csv" #name of the file of the city used
packages = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + cityFile)



fileGenerated = "packagesSmallCity_"+str(30)+"test.csv" #name of the file generated
res = open("utils/generationColi/generationRealisticCity/generateData/" + fileGenerated, "w")
res.write("Commande,Maison,Heure,idMaisonReel,Weight\n")

# Probability for each hour of the day
probabilities =  [18, 10, 5, 3, 2, 2, 4, 13, 28, 43, 57, 62, 55, 59, 63, 64, 63, 67, 73, 68, 63, 70, 66, 42, 18]
hours_pb =[]
for i in range(len(probabilities)-1):
    for j in range(3600):
        hours_pb.append((j*probabilities[i+1]+(3600-j)*probabilities[i])/3600)
    
total_probability = sum(hours_pb)
normalized_probabilities = [p / total_probability for p in hours_pb]
hours_str = []  
for hours in range(24):
    for minutes in range(60):
        for seconds in range(60):
            hours_str.append(f"{hours:02d}:{minutes:02d}:{seconds:02d}")

base_hour = random.choices(hours_str, normalized_probabilities, k=len(packages['Maison']))
for i in range(len(packages['Maison'])):
    res.write(str(packages['Commande'][i])+","+str(packages['Maison'][i]) + "," + str(base_hour[i]) + "," + str(packages['idMaisonReel'][i]) + "," + str(packages['Weight'][i]) + "\n")


