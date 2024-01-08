import pandas as pd
import numpy as np
import random

nb_voulu = 100

cityFile = "smallCity_"+str(nb_voulu)+".csv" #name of the file of the city used
houses = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + cityFile)



fileGenerated = "packagesSmallCity_"+str(nb_voulu)+".csv" #name of the file generated
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

index = 1
lines1 = []
lines2 = []
for i in range(len(houses['Maison'])):
    ran = np.random.randint(0,4)
    while ran > 0:
        lines1.append(str(index)+","+str(i+1) + "," )
        lines2.append("," + str(houses['Maison'][i]) + "," + str(round(np.random.uniform(0.1,2),1)) + "\n")
        ran -= 1
        index += 1

base_hour = random.choices(hours_str, normalized_probabilities, k=index)
for i in range(index-1):
    res.write(lines1[i] + str(base_hour[i]) + lines2[i])