import csv
import random
import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.patches import Circle
from matplotlib.animation import FuncAnimation
from datetime import datetime, timedelta


nombreCommande = 10 #number of package to generate
nameFile = "dataTestSA.csv" #name of the file generated
fileCity = "city1.csv" #name of the file of the city used

#parameters for the weight of the package
weightMin = 10
weightMax = 100

# Probability for each hour of the day
probabilities =  [18, 10, 5, 3, 2, 2, 4, 13, 28, 43, 57, 62, 55, 59, 63, 64, 63, 67, 73, 68, 63, 70, 66, 42, 18]

hours_pb =[]
for i in range(len(probabilities)-1):
    for j in range(3600):
        hours_pb.append((j*probabilities[i+1]+(3600-j)*probabilities[i])/3600)
    
total_probability = sum(hours_pb)
normalized_probabilities = [p / total_probability for p in hours_pb]

#liste des heures possibles
hours_str = []  
for hours in range(24):
    for minutes in range(60):
        for seconds in range(60):
            hours_str.append(f"{hours:02d}:{minutes:02d}:{seconds:02d}")

import os

def genereCSV(nombreCommande,csvVille="utils/generationColi/generationRealisticCity/generateData/" + fileCity,nameFile=nameFile):
    # Crée le fichier CSV
    with open(csvVille, newline='') as csvfile:
        spamreader = csv.reader(csvfile, delimiter=',', quotechar='"')
        nombreMaison = sum(1 for row in spamreader) - 1

    # Create the directory if it doesn't exist
    os.makedirs(os.path.dirname(nameFile), exist_ok=True)

    with open(nameFile, 'w', newline='') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        
        # Génère une heure aléatoire avec minute
        base_hour = random.choices(hours_str, normalized_probabilities, k=nombreCommande)
        spamwriter.writerow(["Commande", "Heure", "idMaison", "Weight"])
        for i in range(nombreCommande):
            weight = random.randint(weightMin,weightMax)
            idMaisons = random.randint(0,nombreMaison-1)
            spamwriter.writerow([f"{i}", base_hour[i], idMaisons, weight])

for i in range(100, 1000, 100):
    genereCSV(i, "utils/generationColi/generationRealisticCity/generateData/city1.csv", f"utils/Comparison/DataDelivery/dataDelivery{i}.csv")