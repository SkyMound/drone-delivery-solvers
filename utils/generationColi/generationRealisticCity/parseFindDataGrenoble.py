import csv
import random
import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.patches import Circle
from matplotlib.animation import FuncAnimation
from datetime import datetime, timedelta

# Données ville => lattitude, longitude, population, surface
# Département : numéro du département.
# Slug : identifiant unique en minuscule, sans accent et sans espace. Peut servir pour faire les URLs d’un site web.
# Nom : nom en majuscule et sans accent.
# Nom simple : nom en minuscule, sans accent et avec les tirets remplacés par des espaces. Peut être utilisé pour faire une recherche lorsqu’on ne sait pas si le nom de ville possède un tiret ou des espaces (ex : “Saint-Étienne” possède un tiret comme séparateur, tandis que “Le Havre” possède un espace comme séparateur)
# Nom reel : nom correct avec les accents
# Nom soundex : soundex du nom de la ville (permet de trouver des villes qui se prononcent presque pareil) [ajouté le 31/10/2013]
# Nom metaphone : metaphone du nom de la ville (même utilité que le soundex) [ajouté le 31/10/2013]
# Code postal : code postal de la ville. Si la ville possède plusieurs code postaux, ils sont tous listés et séparés par un tiret [ajouté le 31/10/2013]
# Numéro de commune : numéro de la commune dans le département. Combiné avec le numéro de département, il permet de créer le code INSEE sous 5 caractères.
# Code commune (ou code INSEE) : identifiant unique sous 5 caractères
# Arrondissement : arrondissement de la ville
# Canton : canton de la ville
# Population en 2010 : nombre d’habitants lors du recensement de 2010
# Population en 1999 : nombre d’habitants lors du recensement de 1999
# Population en 2012 (approximatif) : valeur exprimée en centaine
# Densité en 2010 : nombre d’habitants au kilomètre carré arrondie à l’entier. Calculé à partir du nombre d’habitant et de la surface de la ville [corrigé le 02/07/2014]
# Surface / superficie : surface de la ville en kilomètre carrée [corrigé le 02/07/2014]
# Longitude/latitude en degré : géolocalisation du centre de la ville. Permet de localiser la ville sur une carte (exemple : carte Google Maps) [ajouté le 31/10/2013, corrigé le 07/11/2013]
# Longitude/latitude en GRD : géolocalisation exprimée en GRD
# Longitude/latitude en DMS (Degré Minute Seconde) : géolocalisation exprimée en Degré Minute Seconde
# Altitude minimale/maximale : hauteur minimum et maximum de la ville par rapport au niveau de l’eau

#"id","Departement","Slug","Nom","Nom_simple","Nom_reel","Nom_soundex","Nom_metaphone","Code_postal","Numero_de_commune","Code_commune_INSEE","Arrondissement","Canton","inc","Population_2010","Population_1999","Population_2012","Densite_2010","Surface","Longitude_degre","Latitude_degré","Longitude_GRD","Latitude_GRD","Longitude_DMS","Latitude_DMS","Altitude_minimale","Altitude_maximale"

# Données densité de population

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

#« CRS » pour « coordinate reference system » + code_crs (code projection EPSG) 
# + « RES » pour « résolution » + « 200m / 1000m » 
# + « N » pour Nord + coordonnée_y_coin_inférieur_gauche 
# + « E » pour Est + coordonnée_x_coin_inférieur_gauche.

#open csv file et retourne un dictionnaire
import csv

def open_csv(file):
    with open(file, 'r') as f:
        reader = csv.DictReader(f, delimiter=',', quotechar='"')
        return list(reader)

#retourne une liste de dictionnaire avec les données de densité de population
def get_data_densite():
    data = open_csv('utils/generationColi/generationRealisticCity/dataUsed/carreaux_200m_met.csv')
    #creation d'un dictionnaire avec les données de densité de population
    return data
    

#retourne une liste de dictionnaire avec les données des villes (pas utilisé au final)
def get_data_ville():
    return open_csv('utils/generationColi/generationRealisticCity/dataUsed/villes_france.csv')

def print_csv_headers(filename):
    reader = csv.reader(open(filename, 'r'))
    headers = next(reader)
    print(headers)


from geopy.geocoders import Nominatim
from pyproj import Transformer
import re

def get_lat_lon(data):
    match = re.match(r'CRS3035RES200mN(\d+)E(\d+)', data)
    if match:
        lat = int(match.group(1))
        lon = int(match.group(2))
        return lat, lon
    else:
        return None, None

liste_grenoble = []
datas = get_data_densite()
nb_data = len(datas)


#Recherche les villes avec le code géographique 38185
for i,data in enumerate(datas):
    if data['lcog_geo'] == "38185":
        liste_grenoble.append(data)
    #pourcentage de progression
    #print(f"{i/nb_data*100:.2f}%", end="\r")

print(len(liste_grenoble))

#add liste_grenoble to csv file

with open('utils/generationColi/generationRealisticCity/dataUsed/grenoble.csv', 'w', newline='') as csvfile:
    fieldnames = liste_grenoble[0].keys()
    writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

    writer.writeheader()
    for data in liste_grenoble:
        writer.writerow(data)


