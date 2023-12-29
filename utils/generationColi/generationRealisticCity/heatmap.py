import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.widgets import Slider
import matplotlib.colors as mcolors

ville_data = pd.read_csv('utils/generationColi/generationRealisticCity/generateData/city1.csv')
commandes_data = pd.read_csv('utils/generationColi/generationRealisticCity/generateData/data2.csv')

commandes_data['Heure'] = pd.to_datetime(commandes_data['Heure'])

heures_uniques = commandes_data['Heure'].dt.hour.unique()

fig, ax = plt.subplots()
plt.subplots_adjust(bottom=0.25)

ax_slider = plt.axes([0.25, 0.1, 0.65, 0.03], facecolor='lightgoldenrodyellow')
slider = Slider(ax_slider, 'Heure', heures_uniques.min(), heures_uniques.max(), valinit=heures_uniques.min(), valstep=1)

heatmap = ax.imshow(np.zeros((50, 50)), origin='lower', extent=[0, 1, 0, 1])
plt.colorbar(heatmap, label='Nombre de colis')
plt.title(f'Heatmap des colis à {heures_uniques.min()}:00')
plt.xlabel('Position X')
plt.ylabel('Position Y')



def update(val):
    heure_selectionnee = int(slider.val)
    commandes_heure = commandes_data[commandes_data['Heure'].dt.hour == heure_selectionnee]

    colis_par_maison = commandes_heure['idMaison'].value_counts().reset_index()
    colis_par_maison.columns = ['Maison', 'Nombre de colis']

    colis_maison_positions = pd.merge(colis_par_maison, ville_data, on='Maison')

    heatmap_data, xedges, yedges = np.histogram2d(
        colis_maison_positions['X'],
        colis_maison_positions['Y'],
        bins=50
    )

    heatmap_data = heatmap_data.T

    min_val = max(np.min(heatmap_data),0)
    max_val = max(np.max(heatmap_data),1)

    cmap = mcolors.LinearSegmentedColormap.from_list("CustomMap", ["white", "red"])

    heatmap.set_data(heatmap_data)
    heatmap.set_cmap(cmap)
    heatmap.set_clim(vmin=min_val, vmax=max_val)  # Ajuster la légende
    plt.title(f'Heatmap des colis à {heure_selectionnee}:00')
    plt.draw()
    
update(heures_uniques.min())
slider.on_changed(update)

plt.show()
