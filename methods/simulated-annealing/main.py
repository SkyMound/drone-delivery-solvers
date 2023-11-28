###################### DESCRIPTION ######################
#authors : MARTIN Mickaël, BERNE Thomas
#date : 28/11/2023
#version : 1.0
#description : Algorithme de recuit simulé pour le problème de livraison de colis par drone
#              Le but est de trouver la meilleure solution pour livrer tous les colis en un minimum de temps
#              On considère que les colis sont déposés au centre de tri et que les drones sont chargés a 100% au début de la tournée
#              On considère que les drones peuvent charger 2kg de colis maximum



###################### IMPORTS ######################
import random
import numpy as np
import matplotlib.pyplot as plt



###################### CONSTANTES ######################
VITESSE_DRONE = 8.5 #en m/s
NOMBRE_DE_COLIS = 200
NOMBRE_DE_DRONES = 10
TEMPS_DECOLAGE = 5 #en secondes
MAX_POIDS_COLIS = 2 #en kg
POIDS_DRONE = 4 #en kg
MAX_DISTANCE=5000  #en mètres ( le drone peut parcourir 14 km avec une batterie pleine sans colis )


###################### FONCTIONS ######################

#initialisation des coordonnées des colis aléatoirement dans un cercle de rayon MAX_DISTANCE
def init_parametres_colis():
    for i in range(NOMBRE_DE_COLIS):
        #coordonnées du colis
        details_colis[i][0]=np.random.uniform(-MAX_DISTANCE,MAX_DISTANCE)
        details_colis[i][1]=np.random.uniform(-MAX_DISTANCE,MAX_DISTANCE)
        #poids du colis
        details_colis[i][2]=np.random.rand(0,MAX_POIDS_COLIS)


#calcul de la perte de batterie en fonction de la distance et du poids du colis (en %)
def perte_batterie(distance,poids_colis):
    d_max_charge = 15*4/(4+poids_colis)
    perte_energie =distance*100/d_max_charge + distance*100/15
    return perte_energie

def tmp_charge(to_charge):
    #on considère que le drone peut charger de 0 à 100% en 1h30
    return to_charge*1.5*3600/100

#calcul de la distance allée-retour vers la destination du colis a partir du centre de tri
def cal_distance(infos_colis):
    return np.sqrt(infos_colis[0]**2+infos_colis[1]**2)*2

#calcul de l'énergie nécessaire pour livrer le colis en fonction de la distance et du poids du colis (en %)
def calc_energy_needed(distance,poids_colis):
    #perte de batterie pour l'allé retour + perte de batterie pour les décollages
    #on considère que le drone perd 2% de batterie au décollage a vide et que la perte augmente linéairement avec le poids du colis
    return perte_batterie(distance,poids_colis) + (4+poids_colis)*2/4 + 2
     
def calc_temps_livraison(id_drone,coord_next_colis):
    dist_dep_house=cal_distance(coord_next_colis)

#calcul du cout de la solution
def calc_cout(solution,list_colis,VITESSE_DRONE,NOMBRE_DE_COLIS):
    drones_total_time=np.zeros((NOMBRE_DE_DRONES))
    for i in range(NOMBRE_DE_COLIS):
        num_drone=solution[i]
        infos_colis=list_colis[i]
        
        next_travel_energy=calc_energy_needed(cal_distance(infos_colis),infos_colis[2])*1.1

        #calcul du temps d'attente du drone pour atteindre une charge suffisante pour livrer le colis ( +10% de la batterie pour la sécurité)
        if batterie_drones[num_drone]<next_travel_energy:
            drones_total_time[num_drone]+=tmp_charge(next_travel_energy-batterie_drones[num_drone])
        #TODO voir si on fait un cas particulier pour le dernier colis de la tournée (on considère que les livraisons sont finis a partir du moment ou le dernier colis est livré donc on ne prend pas en compte le temps de retour au centre de tri) 
        drones_total_time[num_drone]+=cal_distance(infos_colis)/VITESSE_DRONE+TEMPS_DECOLAGE*2



###################### MAIN ######################

if __name__ == "__main__":
    #coordonnées des colis (x,y) et poids (kg)
    details_colis = np.zeros((NOMBRE_DE_COLIS, 3))
    batterie_drones = np.zeros((NOMBRE_DE_DRONES, 1))
    #TODO : afficher les coordonnées des colis dans un graphique (x,y)

    max_iterations=100
    T=1000
    facteur=0.90

    #solution initiale aléatoire
    #le drone (ID) va livrer le colis (indice)
    solution_initiale=np.random.randint(0,NOMBRE_DE_DRONES,NOMBRE_DE_COLIS)



