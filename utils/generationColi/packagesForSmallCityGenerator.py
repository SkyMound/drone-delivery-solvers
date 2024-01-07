import pandas as pd
import numpy as np

nb_voulu = 30

cityFile = "smallCity_"+str(nb_voulu)+".csv" #name of the file of the city used
houses = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + cityFile)



fileGenerated = "packagesSmallCity_"+str(nb_voulu)+".csv" #name of the file generated
res = open("utils/generationColi/generationRealisticCity/generateData/" + fileGenerated, "w")
res.write("Commande,Maison,Heure,idMaisonReel,Weight\n")
index = 1
for i in range(len(houses['Maison'])):
    ran = np.random.randint(0,4)
    while ran > 0:
        res.write(str(index)+","+str(i+1) + "," + str(np.random.randint(0,24)) + "," + str(houses['Maison'][i]) + "," + str(round(np.random.uniform(0.1,2),1)) + "\n")
        ran -= 1
        index += 1


