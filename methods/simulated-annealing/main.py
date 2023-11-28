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



#coordonnées des colis (x,y) et poids (kg)
details_colis = np.zeros((NOMBRE_DE_COLIS, 3))
batterie_drones = np.zeros((NOMBRE_DE_DRONES, 1))


#TODO : afficher les coordonnées des colis dans un graphique (x,y)

#initialisation des coordonnées des colis aléatoirement dans un cercle de rayon MAX_DISTANCE
def init_parametres_colis():
    for i in range(NOMBRE_DE_COLIS):
        #coordonnées du colis
        details_colis[i][0]=np.random.uniform(-MAX_DISTANCE,MAX_DISTANCE)
        details_colis[i][1]=np.random.uniform(-MAX_DISTANCE,MAX_DISTANCE)
        #poids du colis
        details_colis[i][2]=np.random.rand(0,MAX_POIDS_COLIS)


#calcul de la perte de batterie en fonction de la distance et du poids du colis (en %)
def calc_pourcentage_perte_batterie(distance,poids_colis):
    d_max_charge = 15*4/(4+poids_colis)
    perte_energie =distance*100/d_max_charge
    return perte_energie

def perte_energie_decolage(poids_colis):
    return 

max_iterations=100
T=1000
facteur=0.90

#solution initiale aléatoire
#le drone (ID) va livrer le colis (indice)
solution_initiale=np.random.randint(1,NOMBRE_DE_DRONES+1,NOMBRE_DE_COLIS)

#calcul de la distance allée-retour vers la destination du colis a partir du centre de tri
def cal_distance(coord_colis):
    return np.sqrt(coord_colis[0]**2+coord_colis[1]**2)*2

def calc_energy_needed(distance,poids_colis):
    pertealler = calc_pourcentage_perte_batterie(distance,poids_colis)
    perte_retour = calc_pourcentage_perte_batterie(distance,0) 


def calc_temps_livraison(id_drone,coord_next_colis):
    dist_dep_house=cal_distance(coord_next_colis)




#calcul du cout de la solution
def calc_cout(solution,coord_destination_colis,VITESSE_DRONE,NOMBRE_DE_COLIS):
    drones_total_time=np.zeros((NOMBRE_DE_DRONES))
    for i in range(NOMBRE_DE_COLIS):
        num_drone=solution[i]
        dest_colis=coord_destination_colis[i]
        drones_total_time[num_drone-1]+=cal_distance(dest_colis)/VITESSE_DRONE+TEMPS_DECOLAGE*2

