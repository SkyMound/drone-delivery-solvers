import csv
import random
import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.patches import Circle
from matplotlib.animation import FuncAnimation
from datetime import datetime, timedelta


nombreZone = 1
nombreMaison = 100
rayonVille = 1000

# Probabilités pour chaque heure
probabilities =  [18, 10, 5, 3, 2, 2, 4, 13, 28, 43, 57, 62, 55, 59, 63, 64, 63, 67, 73, 68, 63, 70, 66, 42,18]


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



def genereCSV(nombreCommande,csvVille="utils/generationColi/generationRealisticCity/generateData/city1.csv"):
    # Crée le fichier CSV
    with open(csvVille, newline='') as csvfile:
        spamreader = csv.reader(csvfile, delimiter=',', quotechar='"')
        nombreMaison = sum(1 for row in spamreader) - 1

    with open("utils/generationColi/generationRealisticCity/generateData/data.csv", 'w', newline='') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        
        # Génère une heure aléatoire avec minute
        
        base_hour = random.choices(hours_str, normalized_probabilities, k=nombreCommande)
        spamwriter.writerow(["Commande", "Heure", "idMaison"])
        for i in range(nombreCommande):
            idMaisons = random.randint(0,nombreMaison-1)
            spamwriter.writerow([f"{12634+i}", base_hour[i], idMaisons])


#Ancienne fonction pour générer les maisons
def genereCSVVille(nombreMaison, rayonVille, rayonMin):
    with open('city1.csv', 'w', newline='') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        spamwriter.writerow(["Maison", "X", "Y"])
        
        center_density = 0.8  # Densité du centre
        outskirts_density = 0.3  # Densité de la périphérie
        
        for i in range(nombreMaison):
            while True:
                x = random.uniform(-rayonVille, rayonVille)
                y = random.uniform(-rayonVille, rayonVille)
                
                distance = (x**2 + y**2) ** 0.5
                
                # Vérification si la maison est à l'intérieur du cercle maximal
                if distance <= rayonVille:
                    # Vérification si la maison est à l'extérieur du cercle minimal
                    if distance >= rayonMin:
                        probability = center_density if distance <= rayonMin * 2 else outskirts_density
                        if random.random() < probability:
                            break
            
            spamwriter.writerow([f"{i}", x, y])



#Simule l'apparation des colis via une animation
def simulate_day():
    # Lire les données
    df_ville = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/city1.csv")
    df_commandes = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/data.csv")

    # Convertir la colonne 'Heure' en datetime
    df_commandes['Heure'] = pd.to_datetime(df_commandes['Heure'])

    # Créer le graphique
    fig, ax = plt.subplots()

    # Tracer les maisons
    ax.scatter(df_ville['X'], df_ville['Y'])

    # Tracer les limites de la ville
    # circle = Circle((0, 0), rayonVille, fill=False)
    # ax.add_patch(circle)
    # circle = Circle((0, 0), 50, fill=False)
    # ax.add_patch(circle)

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

        # Mettre en évidence les maisons qui ont passé une commande à cette heure
        # Convertir l'heure actuelle en secondes
        time_seconds = (time.hour * 3600) + (time.minute * 60) + time.second

        # Convertir l'heure des commandes en secondes
        df_commandes['Heure_seconds'] = df_commandes['Heure'].dt.hour * 3600 + df_commandes['Heure'].dt.minute * 60 + df_commandes['Heure'].dt.second

        # Sélectionner les commandes qui ont été passées dans les 8 minutes de l'heure actuelle
        commandes_time = df_commandes[(df_commandes['Heure_seconds'] >= time_seconds - 480) & (df_commandes['Heure_seconds'] <= time_seconds + 480)]       
        ax.scatter(df_ville.loc[commandes_time['idMaison'], 'X'], df_ville.loc[commandes_time['idMaison'], 'Y'], color='red', s=10)
        
        # Tracer les limites de la ville
        # circle = Circle((0, 0), rayonVille, fill=False)
        # ax.add_patch(circle)

        # circle = Circle((0, 0), 50, fill=False)
        # ax.add_patch(circle)

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


#genereCSV(200)

simulate_day()