import csv
import random
import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.patches import Circle
from matplotlib.animation import FuncAnimation
from datetime import datetime, timedelta
import re

#Données csv file
# idcar_200m : Identifiant Inspire du carreau de 200 m
# idcar_1km : Identifiant Inspire du carreau de 1 km auquel appartient le carreau de 200 m
# id_car_nat : Identifiant Inspire du carreau de niveau naturel auquel appartient le carreau de 200 m
# i_est_200 : Vaut 1 si le carreau est imputé par une valeur approchée, 0 sinon.
# i_est_1Km : Vaut 1 si le carreau de 1 km auquel est rattaché le carreau de 200 m est imputé par une valeur approchée, 0 sinon.
# lcog_geo : Code officiel géographique au 1er janvier 2020 de la ou des commune(s) dans laquelle (lesquelles) se trouve le carreau.
# ind : Nombre d’individus
# men : Nombre de ménages
# men_pauv : Nombre de ménages pauvres
# men_1ind : Nombre de ménages d’un seul individu
# men_5ind : Nombre de ménages de 5 individus ou plus
# men_prop : Nombre de ménages propriétaires
# men_fmp : Nombre de ménages monoparentaux
# ind_snv : Somme des niveaux de vie winsorisés des individus
# men_surf : Somme de la surface des logements du carreau
# men_coll : Nombre de ménages en logements collectifs
# men_mais : Nombre de ménages en maison
# log_av45 : Nombre de logements construits avant 1945
# log_45_70 : Nombre de logements construits entre 1945 et 1969
# log_70_90 : Nombre de logements construits entre 1970 et 1989
# log_ap90 : Nombre de logements construits depuis 1990
# log_inc : Nombre de logements dont la date de construction est inconnue
# log_soc : Nombre de logements sociaux
# ind_0_3 : Nombre d’individus de 0 à 3 ans
# ind_4_5 : Nombre d’individus de 4 à 5 ans
# ind_6_10 : Nombre d’individus de 6 à 10 ans
# ind_11_17 : Nombre d’individus de 11 à 17 ans
# ind_18_24 : Nombre d’individus de 18 à 24 ans
# ind_25_39 : Nombre d’individus de 25 à 39 ans
# ind_40_54 : Nombre d’individus de 40 à 54 ans
# ind_55_64 : Nombre d’individus de 55 à 64 ans
# ind_65_79 : Nombre d’individus de 65 à 79 ans
# ind_80p : Nombre d’individus de 80 ans ou plus
# ind_inc : Nombre d’individus dont l’âge est inconnu

def open_csv(file):
    with open(file, 'r') as f:
        reader = csv.DictReader(f, delimiter=',', quotechar='"')
        return list(reader)


#Fonction permettant d'obtenir les lattitude et longitude selon le code CRS
def get_lat_lon(data):
    match = re.match(r'CRS3035RES200mN(\d+)E(\d+)', data)
    if match:
        lat = int(match.group(1))
        lon = int(match.group(2))
        return lat, lon
    else:
        return None, None
    

#génération ville avec densité de grenoble
def genereCSVVille(file,redFactor=1):
    with open(file, 'w', newline='') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        spamwriter.writerow(["Maison", "X", "Y"])
        id = 0
        for i in range(len(data)):
            nb_hab = round(float(data[i]['men']))
            lat, lon = get_lat_lon(data[i]['idcar_200m'])
            lats.append(lat)
            lons.append(lon)
            for j in range(nb_hab//redFactor):
                X = random.uniform(lat, lat+200)
                Y = random.uniform(lon, lon+200)
                spamwriter.writerow([id, X, Y])
                id += 1


#Place sur une graphique les données de densité de population
def graph_densite():
    for i in range(len(data)):
       
        lat, lon = get_lat_lon(data[i]['idcar_200m'])
        lats.append(lat)
        lons.append(lon)
        plt.scatter(lat, lon, s=1, c='blue')
    plt.show()


data = open_csv('utils/generationColi/generationRealisticCity/dataUsed/grenoble.csv')
lats = []
lons = []

graph_densite()
#genereCSVVille('utils/generationColi/generationRealisticCity/dataUsed/grenoble.csv',25)

data = open_csv('utils/generationColi/generationRealisticCity/generateData/city1.csv')
#Place sur une graphique les données de densité de population
X = []
Y = []
for i in data:
    X.append(float(i['X']))
    Y.append(float(i['Y']))

print(len(data))
plt.scatter(X, Y, s=1, c='blue')
plt.show()