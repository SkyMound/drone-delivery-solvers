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

import pandas as pd

###################### CONSTANTES ######################
VITESSE_DRONE = 8.5 #en m/s
NOMBRE_DE_COLIS = 10
NOMBRE_DE_DRONES = 10
TEMPS_DECOLAGE = 5 #en secondes
MAX_POIDS_COLIS = 2 #en kg
POIDS_DRONE = 4 #en kg
MAX_DISTANCE=5000  #en mètres ( le drone peut parcourir 14 km avec une batterie pleine sans colis )
nameFile = "data3.csv" #name of the file generated
fileCity = "city1.csv" #name of the file of the city used

###################### FONCTIONS ######################

#initialisation des coordonnées des colis aléatoirement dans un cercle de rayon MAX_DISTANCE 
def init_list_colis(nameFile = "dataTestSA.csv",fileCity = "city1.csv"):
    df_ville = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + fileCity)
    df_commandes = pd.read_csv("utils/generationColi/generationRealisticCity/generateData/" + nameFile)
    details_colis = np.zeros((NOMBRE_DE_COLIS, 3))
    for i in range(NOMBRE_DE_COLIS):
        #coordonnées du colis
        details_colis[i][0]=df_ville.loc[df_commandes['idMaison'][i], 'X']
        details_colis[i][1]=df_ville.loc[df_commandes['idMaison'][i], 'Y']
        #poids du colis
        details_colis[i][2]=df_commandes['Weight'][i]/1000
    
    #coordonnées du centre de tri (on considère que le centre de tri est au centre des coordonnées)
    coord_depot=(0,0)
    coord_depot=(np.mean(np.array(df_ville['X'])),np.mean(np.array(df_ville['Y'])))

    return details_colis,coord_depot


#calcul de la perte de batterie en fonction de la distance et du poids du colis (en %)
def perte_batterie(distance,poids_colis):
    d_max_charge = 15*4/(4+poids_colis)
    perte_energie =distance*100/d_max_charge + distance*100/15
    return perte_energie

def tmp_charge(to_charge):
    #on considère que le drone peut charger de 0 à 100% en 1h30
    return to_charge*1.5*3600/100

#calcul de la distance allée-retour vers la destination du colis a partir du centre de tri positionné au centre des
def cal_distance(infos_colis,coord_depot=(0,0)):
    return np.sqrt((infos_colis[0]-coord_depot[0])**2+(infos_colis[1]-coord_depot[1])**2)*2

#calcul de l'énergie nécessaire pour livrer le colis en fonction de la distance et du poids du colis (en %)
def calc_energy_needed(distance,poids_colis):
    #perte de batterie pour l'allé retour + perte de batterie pour les décollages
    #on considère que le drone perd 2% de batterie au décollage a vide et que la perte augmente linéairement avec le poids du colis
    return perte_batterie(distance,poids_colis) + (4+poids_colis)*2/4 + 2

#calcul du cout de la solution
def calc_cout(solution,list_colis,batterie_drones,coord_depot=(0,0)):
    drones_total_time=np.zeros((NOMBRE_DE_DRONES))
    drones_theorique_time=np.zeros((NOMBRE_DE_DRONES))
    for i in range(NOMBRE_DE_COLIS):
        num_drone=solution[i]
        infos_colis=list_colis[i]
        dist_colis=cal_distance(infos_colis,coord_depot)
        #calcul du temps de livraison du colis par le drone
        drones_theorique_time[num_drone]+=dist_colis/VITESSE_DRONE+TEMPS_DECOLAGE*2
        next_travel_energy=calc_energy_needed(dist_colis,infos_colis[2])*1.1

        #calcul du temps d'attente du drone pour atteindre une charge suffisante pour livrer le colis ( +10% de la batterie pour la sécurité)
        if batterie_drones[num_drone]<next_travel_energy:
            #TODO vérifier que c'est possible de charger le drone
            drones_total_time[num_drone]+=tmp_charge(next_travel_energy-batterie_drones[num_drone])
        #TODO voir si on fait un cas particulier pour le dernier colis de la tournée (on considère que les livraisons sont finis a partir du moment ou le dernier colis est livré donc on ne prend pas en compte le temps de retour au centre de tri) 
        drones_total_time[num_drone]+=dist_colis/VITESSE_DRONE+TEMPS_DECOLAGE*2
    
    return np.max(drones_total_time-drones_theorique_time)

def voisinage(solution):
    #on choisit un colis au hasard et on change de drone
    solution_voisine=solution
    colis_a_changer=np.random.randint(0,NOMBRE_DE_COLIS)
    solution_voisine[colis_a_changer]=np.random.randint(0,NOMBRE_DE_DRONES)
    return solution_voisine


###################### MAIN ######################

if __name__ == "__main__":

    fig, (colis_plots, cout_plot) = plt.subplots(1, 2, sharey=False, figsize=(15, 5))
    fig.suptitle('Simulation de livraison de colis par drone')
    colis_plots.set_title('Position des colis')
    cout_plot.set_title('Evolution du cout')

    #coordonnées des colis (x,y) et poids (kg)
    details_colis,coord_depot = init_list_colis(nameFile,fileCity)
    batterie_drones = np.zeros((NOMBRE_DE_DRONES, 1))
    batterie_drones.fill(100)

    colis_plots.scatter(details_colis[:, 0], details_colis[:, 1])
    colis_plots.scatter(coord_depot[0], coord_depot[1], color='red')

    iterations=1000
    batch_size=10
    T=1000
    facteur=0.90

    #solution initiale aléatoire
    #le drone (ID) va livrer le colis (indice)
    solution=np.random.randint(0,NOMBRE_DE_DRONES,NOMBRE_DE_COLIS)
    cout0 = calc_cout(solution,details_colis,batterie_drones,coord_depot)
    couts= np.zeros(iterations)
    couts[0]=cout0
    min_sol=solution
    cout_min_sol=cout0

    for i in range(iterations):
        #print('la ',i,'è_me solution = ',solution,' donne le temps maximum de livraison = ',cout0,' la température actuelle =',T)
        T=T*facteur

        couts[i]=cout0
        for j in range(batch_size):
            #on génère une solution voisine
            solution_voisine=voisinage(solution)
            #on calcule le cout de la solution voisine
            cout1 = calc_cout(solution_voisine,details_colis,batterie_drones,coord_depot)

            delta_cout=cout1-cout0

            if delta_cout<=0:
                cout0=cout1
                solution=solution_voisine
                if cout1<cout_min_sol:
                    cout_min_sol=cout1
                    min_sol=solution
            else:
                x=np.random.uniform()
                if x<np.exp(-(delta_cout/T)):
                    cout0=cout1
                    solution=solution_voisine
    
    cout_plot.plot(couts)
    print('la meilleure solution est ',min_sol,' avec un temps maximum de livraison de ',cout_min_sol,' secondes')
    plt.show()

    


