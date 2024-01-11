#open csv file
import csv

heure = []

#open file
with open('utils/generationColi/generationRealisticCity/generateData/data3.csv', newline='') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=',', quotechar='"')
    for row in spamreader:
        #convert time to hours
        if row[1] == "Heure":
            continue
        time = row[1].split(":")
        time = int(time[0])
        heure.append(time)

#histogramme of the hour
import matplotlib.pyplot as plt
import numpy as np

plt.hist(heure, bins=24)
plt.xticks(np.arange(0, 24, 1))
plt.xlabel("Heure")
plt.ylabel("Nombre de colis")
plt.title("Nombre de colis par heure")
plt.show()
