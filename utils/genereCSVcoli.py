import csv
import random
import pandas as pd
import matplotlib.pyplot as plt

# listeProba = [[0] * 18, [1] * 10, [2] * 5, [3] * 3, [4] * 2, [5] * 2, [6] * 4, [7] * 13, [8] * 28, [9] * 43, [10] * 57, [11] * 62, [12] * 55, [13] * 59, [14] * 63, [15] * 64, [16] * 63, [17] * 67, [18] * 73, [19] * 68, [20] * 63, [21] * 70, [22] * 66, [23] * 42]
# listeProba = [item for sublist in listeProba for item in sublist]


# def trace():
#     count = []

#     for i in range(0, 24):
#         count.append(listeProba.count(i))
#     #affiche count sur un graphique
#     plt.plot(count)
#     plt.show()




# def genereCSV(nombreCommande):
#     # Création du fichier csv
#     with open('data.csv', 'w', newline='') as csvfile:
#         spamwriter = csv.writer(csvfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
#         for i in range(nombreCommande):
#             # Choix random d'une heure dans la listeProba
#             base_hour = random.choice(listeProba)
#             adjusted_hour = base_hour
#             rounded_hour = round(adjusted_hour)  # Round to the nearest integer
#             formatted_hour = f"{rounded_hour:02d}:00"  # Format as HH:00
#             spamwriter.writerow([f"Commande {i}", formatted_hour])




# Probabilités pour chaque heure (remarquez que les probabilités ne totalisent pas 100%, elles peuvent être normalisées si nécessaire)
probabilities =  [18, 10, 5, 3, 2, 2, 4, 13, 28, 43, 57, 62, 55, 59, 63, 64, 63, 67, 73, 68, 63, 70, 66, 42,18]

# Normalisez les probabilités pour qu'elles totalisent 100%

hours_pb =[]
for i in range(len(probabilities)-1):
    for j in range(3600):
        hours_pb.append((j*probabilities[i+1]+(3600-j)*probabilities[i])/3600)

#print(hours_pb)


    
total_probability = sum(hours_pb)
normalized_probabilities = [p / total_probability for p in hours_pb]
#hour_choice = random.choices(hours, probabilities, k=1)

#tracé du graphique
# plt.plot(normalized_probabilities)
# plt.show()

#liste des heures possibles
hours_str = []  
for hours in range(24):
    for minutes in range(60):
        for seconds in range(60):
            hours_str.append(f"{hours:02d}:{minutes:02d}:{seconds:02d}")

# print(hours_str)

# print(len(hours_str))
# print(len(normalized_probabilities))



def genereCSV(nombreCommande):
    # Crée le fichier CSV
    with open('data.csv', 'w', newline='') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        
        # Génère une heure aléatoire avec minute

        base_hour = random.choices(hours_str, normalized_probabilities, k=nombreCommande)
        spamwriter.writerow(["Commande", "Heure"])
        for i in range(nombreCommande):
            spamwriter.writerow([f"{12634+i}", base_hour[i]])
        
def traceviaCSVbyhour():
    df = pd.read_csv("data.csv")
    #count le nombre de commande par heure et le trace sur un graphique heure par heure croissante
    df['Heure'] = pd.to_datetime(df['Heure'])
    df['Heure'] = df['Heure'].dt.hour
    df = df.sort_values(by='Heure')
    df = df.groupby('Heure').count()
    df.plot()

    plt.show()



genereCSV(10000)
traceviaCSVbyhour()