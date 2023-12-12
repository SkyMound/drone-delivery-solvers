import csv
import random
import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.patches import Circle
from matplotlib.animation import FuncAnimation
from datetime import datetime, timedelta


nomberPackage = 1000 #number of package to generate
nameFile = "data3.csv" #name of the file generated
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

def genereCSV(nombreCommande,csvVille="utils/generationColi/generationRealisticCity/generateData/" + fileCity):
    # Crée le fichier CSV
    with open(csvVille, newline='') as csvfile:
        spamreader = csv.reader(csvfile, delimiter=',', quotechar='"')
        nombreMaison = sum(1 for row in spamreader) - 1

    with open("utils/generationColi/generationRealisticCity/generateData/"+ nameFile, 'w', newline='') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        
        # Génère une heure aléatoire avec minute
        
        base_hour = random.choices(hours_str, normalized_probabilities, k=nombreCommande)
        spamwriter.writerow(["Commande", "Heure", "idMaison", "Weight"])
        for i in range(nombreCommande):
            weight = random.randint(weightMin,weightMax)
            idMaisons = random.randint(0,nombreMaison-1)
            spamwriter.writerow([f"{12634+i}", base_hour[i], idMaisons, weight])



#Simule l'apparation des colis via une animation
def simulate_day():
    # Lire les données
    df_ville = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + fileCity)
    df_commandes = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + nameFile)

    # Convertir la colonne 'Heure' en datetime
    df_commandes['Heure'] = pd.to_datetime(df_commandes['Heure'])

    # Créer le graphique
    fig, ax = plt.subplots()

    # Tracer les maisons
    ax.scatter(df_ville['X'], df_ville['Y'])

    # Assurer que le cercle apparaisse comme un cercle
    ax.set_aspect('equal', 'box')

    # Créer une fonction pour mettre à jour le graphique
    def update(delta):
        # Convertir le timedelta en datetime
        time = datetime.min + delta 
        # Effacer le graphique
        ax.clear()

        # Tracer les maisons
        ax.scatter(df_ville['X'], df_ville['Y'], s=1)

        time_seconds = (time.hour * 3600) + (time.minute * 60) + time.second

        # Convertir l'heure des commandes en secondes
        df_commandes['Heure_seconds'] = df_commandes['Heure'].dt.hour * 3600 + df_commandes['Heure'].dt.minute * 60 + df_commandes['Heure'].dt.second

        # Sélectionner les commandes qui ont été passées dans les 8 minutes de l'heure actuelle
        commandes_time = df_commandes[(df_commandes['Heure_seconds'] >= time_seconds - 480) & (df_commandes['Heure_seconds'] <= time_seconds + 480)]       
        ax.scatter(df_ville.loc[commandes_time['idMaison'], 'X'], df_ville.loc[commandes_time['idMaison'], 'Y'], color='red', s=10)

        # Assurer que le cercle apparaisse comme un cercle
        ax.set_aspect('equal', 'box')

        # Mettre à jour le titre avec l'heure actuelle
        plt.title(f"Heure: {time.time()}")

    # Créer l'animation
    times = [timedelta(hours=h, minutes=m, seconds=s) for h in range(12,24) for m in range(0,60,2) for s in range(1)]
    anim = FuncAnimation(fig, update, frames=times, repeat=False, interval=0.1)

    # Sauvegarder l'animation en tant que GIF
    #anim.save('animation.gif', writer='pillow')
    plt.show()


genereCSV(nomberPackage)

# simulate_day()