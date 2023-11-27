# import des librairies
import random
import numpy as np
import matplotlib.pyplot as plt

VITESSE_DRONE = 8.5 #en m/s
NOMBRE_DE_COLIS = 200
NOMBRE_DE_DRONES = 10
TEMPS_DECOLAGE = 5 #en secondes
MAX_POIDS_COLIS = 2 #en kg
POIDS_DRONE = 4 #en kg
MAX_DISTANCE=5000  #en mètres ( le drone peut parcourir 14 km avec une batterie pleine sans colis )



distances = np.zeros((NOMBRE_DE_COLIS))
#coordonnées des colis (x,y)

coord_destination_colis = np.zeros((NOMBRE_DE_COLIS, 2))
#TODO : afficher les coordonnées des colis dans un graphique (x,y)

#calcul de la perte de batterie en fonction de la distance et du poids du colis (en %)
def calc_pourcentage_perte_batterie(distance,poids_colis):
    d_max_charge = 15*4/(4+poids_colis)
    perte_energie =distance*100/d_max_charge
    return perte_energie

#initialisation des coordonnées des colis aléatoirement dans un cercle de rayon MAX_DISTANCE
for i in range(NOMBRE_DE_COLIS):
    coord_destination_colis[i][0]=np.random.uniform(-MAX_DISTANCE,MAX_DISTANCE)
    coord_destination_colis[i][1]=np.random.uniform(-MAX_DISTANCE,MAX_DISTANCE)


max_iterations=100
T=1000
facteur=0.90

#solution initiale aléatoire
#le drone (ID) va livrer le colis (indice)
solution_initiale=np.random.randint(1,NOMBRE_DE_DRONES+1,NOMBRE_DE_COLIS)
print(solution_initiale)

#calcul de la distance allée-retour vers la destination du colis a partir du centre de tri
def cal_distance(coord_colis):
    return np.sqrt(coord_colis[0]**2+coord_colis[1]**2)*2

def calc_energy_needed(distance,poids_colis):
    return energy_decollage*2 +energy_vol*distance *2 


#calcul du cout de la solution initiale
def calc_cout(solution_initiale,coord_destination_colis,VITESSE_DRONE,NOMBRE_DE_COLIS):
    drones_total_time=np.zeros((NOMBRE_DE_DRONES))
    for i in range(NOMBRE_DE_COLIS):
        num_drone=solution_initiale[i]
        dest_colis=coord_destination_colis[i]
        drones_total_time[num_drone-1]+=cal_distance(dest_colis)/VITESSE_DRONE+TEMPS_DECOLAGE*2



    
    
    
#     return cout
