import pandas as pd
import matplotlib.pyplot as plt

# Charger les données des maisons depuis le fichier CSV
ville_data = pd.read_csv('utils/generationColi/generationRealisticCity/generateData/city1.csv')

# Calculer la densité en utilisant un histogramme 2D
plt.figure(figsize=(8, 6))
plt.hist2d(ville_data['X'], ville_data['Y'], bins=50, cmap='Blues')
plt.colorbar(label='Nombre de maisons')
plt.title('Répartition des maisons dans la ville')
plt.xlabel('Position X')
plt.ylabel('Position Y')
plt.show()
