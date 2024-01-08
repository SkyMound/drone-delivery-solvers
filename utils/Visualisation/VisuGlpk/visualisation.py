import random
import re
import tkinter as tk
from math import sqrt
import time
from PIL import Image, ImageTk 
from datetime import timedelta
import itertools

depot = (2461228,3984254)

class City:
    def __init__(self, name, drones, houses,packages, canvas, depot):
        self.name = name
        
        self.drones = drones
        self.houses = houses
        self.packages = packages
        self.seconds = 28800

        self.canvas = canvas

        self.canvas.create_text(390, 50, text=timedelta(self.seconds), font=("Arial", 20), tags="time")


    def __str__(self):
        return "City " + str(self.name) + " : " + str(self.pos)
    
    def execution(self):
        for drone in self.drones:
            self.drones[drone].addPackage()
            self.drones[drone].advance()

            window.update()
        time.sleep(0.0001)
        self.seconds += 0.5
        self.canvas.itemconfigure("time", text=timedelta(seconds=int(self.seconds)))

class House:
    def __init__(self, id, pos, canvas):
        self.id = id
        self.pos = pos
        self.canvas = canvas
        self.canvas.create_rectangle(self.pos[0], self.pos[1], self.pos[0]+5, self.pos[1]+5, fill="blue")

    def __str__(self):
        return "House " + str(self.id) + " : " + str(self.pos)


class Drone:
    def __init__(self, id, pos, canvas, package,depot):
        self.id = id
        self.pos = pos
        self.isCharging = False
        self.isDelivering = False
        self.packageHolding = None
        self.objectif = None
        self.destroy = False
        self.battery = 100
        self.depot = depot
        print(self.depot)
        
        self.listPackage = package
        
        self.canvas = canvas
        self.canvas.create_oval(self.pos[0], self.pos[1], self.pos[0]+5, self.pos[1]+5, fill="red", tags="drone"+str(self.id))
        print("Drone create")

        self.canvas.create_text(850, int(self.id)*100 - 90, text="Drone "+str(self.id), font=("Arial", 17))
        self.canvas.create_rectangle(850, int(self.id)*100 - 70, 1050, int(self.id)*100 - 50, fill="green", tags="battery"+str(self.id))
        self.canvas.create_rectangle(850, int(self.id)*100 - 70, 1050, int(self.id)*100 - 50, outline="black")

        self.canvas.create_image(945, int(self.id)*100 - 60, image=im, tags="image_drone"+str(self.id))
        
        self.canvas.itemconfigure("image_drone"+str(self.id), state="hidden")

        print(len(self.listPackage))
        if len(self.listPackage) > 0 and len(self.listPackage) < 10:
            for i in range(len(self.listPackage)):
                self.canvas.create_rectangle(850 + len(self.listPackage) * 20 - i* 20, int(self.id)*100 - 30, 840 + len(self.listPackage) * 20 - i*20, int(self.id)*100 - 20, fill="orange", tags="package"+str(self.id)+str(self.listPackage[i].id))
        elif len(self.listPackage) >= 10:
            for i in range(len(self.listPackage)):
                self.canvas.create_rectangle(850 + len(self.listPackage) * 20 - i* 20, int(self.id)*100 - 30, 840 + len(self.listPackage) * 20 - i*20, int(self.id)*100 - 20, fill="white",outline="white", tags="package"+str(self.id)+str(self.listPackage[i].id))

            self.canvas.create_text(880, int(self.id)*100 - 30, text=str(len(self.listPackage))+" packages left", tags="text"+str(self.id))  
    
    def __str__(self):
        return "Drone " + str(self.id) + " : " + str(self.pos)
    
    def addPackage(self):
        if self.packageHolding == None and len(self.listPackage) > 0 and self.atSpawn() and not(self.isCharging):
            package = self.listPackage[0]
            self.packageHolding = package
            self.objectif = package.houseTarget.pos
            self.isDelivering = True
            self.canvas.itemconfigure("drone"+str(self.id), fill="green")
        
    def removePackage(self):
        self.canvas.delete("package"+str(self.id)+str(self.packageHolding.id))
        if len(self.listPackage) >= 10:
            self.canvas.itemconfigure("text"+str(self.id), text=str(len(self.listPackage)-1)+" packages left")
        
        if len(self.listPackage) == 10:
            self.canvas.delete("text"+str(self.id))
            for i in range(len(self.listPackage)):
                self.canvas.itemconfigure("package"+str(self.id)+str(self.listPackage[i].id), fill="orange",outline="black")
            

        print("package"+str(self.id)+str(self.packageHolding.id))
        self.listPackage.remove(self.packageHolding)
        self.packageHolding = None
        self.objectif = self.depot
        self.isDelivering = False
        self.canvas.itemconfigure("drone"+str(self.id), fill="red")
        

    def charge(self):
        self.isCharging = True
        print("Drone " + str(self.id) + " en charge")
        self.canvas.itemconfigure("drone"+str(self.id), fill="yellow")
        self.canvas.itemconfigure("image_drone"+str(self.id), state="normal")

    def move_drone(self, pos):
        self.canvas.move("drone"+str(self.id), pos[0], pos[1])
        
        self.pos = (self.pos[0] + pos[0], self.pos[1] + pos[1])
        #self.pos = (self.pos[0]+pos[0], self.pos[1]+pos[1])

    def advance(self):

        if len(self.listPackage) == 0 and self.atSpawn() and not(self.destroy):
            print("end of delivery for drone " + str(self.id))
            self.canvas.delete("drone"+str(self.id))
            self.destroy = True
            self.canvas.itemconfigure("battery"+str(self.id), fill="grey")
            self.canvas.itemconfigure("image_drone"+str(self.id), state="hidden")

        if not(self.destroy):
            self.setUpBattery()
            if not(self.isCharging):
                self.battery -= 0.5/17.64
                normVector = norm(self.objectif[0] - self.pos[0],self.objectif[1] - self.pos[1])*0.85
                self.move_drone(((self.objectif[0] - self.pos[0])/normVector, (self.objectif[1] - self.pos[1])/normVector))
                if self.isAtObjectif() and self.isDelivering:
                    self.removePackage()

                if self.battery <=98 and self.atSpawn():
                    self.charge()

            if self.isCharging:
                self.battery += 0.5/52.92 #charging is 3 times slower than decharging

            if self.battery >= 100 and self.isCharging:
                self.isCharging = False
                self.canvas.itemconfigure("drone"+str(self.id), fill="red")
                print("Drone " + str(self.id) + " chargé")
                self.canvas.itemconfigure("image_drone"+str(self.id), state="hidden")

    def coliRestant(self):
        return len(self.listPackage)
    
    def isAtObjectif(self):
        if self.pos[0] >= self.objectif[0]-5 and self.pos[0] <= self.objectif[0]+5 and self.pos[1] >= self.objectif[1]-5 and self.pos[1] <= self.objectif[1]+5:
            return True
        else:
            return False
        
    def atSpawn(self):
        if self.pos[0] >= depot[0]-5 and self.pos[0] <= depot[0]+5 and self.pos[1] >= depot[1]-5 and self.pos[1] <= depot[1]+5:
            return True
        else:
            return False
        
    def setUpBattery(self):
        x0, y0, x1, y1 = self.canvas.coords("battery"+str(self.id))
        self.battery = max(self.battery, 0)
        self.battery = min(self.battery, 100)
        self.canvas.coords("battery"+str(self.id), x0, y0, x0 + (self.battery/100)*200, y1)

class Package:
    def __init__(self, id, byDrone, houseTarget):
        self.id = id
        self.byDrone = byDrone
        self.houseTarget = houseTarget

    def __str__(self):
        return "Package " + str(self.id) + " : by drone " + str(self.byDrone) + " to house " + str(self.houseTarget)


def norm(x,y):
    return sqrt(x**2 + y**2)


def recupCity(filePathCity):
    
    selected_houses = ["0","83","166","249","332","415","498","581","664","747","830","913","996","1079","1162","1245","1328","1411","1494","1577","1660","1743","1826","1909","1992","2075","2158","2241","2324","2407","2490"]
    city = {}
    x_values = []
    y_values = []
    with open(filePathCity, "r") as file:
        next(file)  # Skip the header
        for line in file:
            line = line.split(",")
            #if line[0] in selected_houses:
            x_values.append(float(line[1]))
            y_values.append(float(line[2].replace("\n","")))

    min_x, max_x = min(x_values), max(x_values)
    min_y, max_y = min(y_values), max(y_values)

    scale = max(max_x - min_x, max_y - min_y)

    depot = int(((2461228 - min_x) / scale) * 800), int(((3984254 - min_y) / scale) * 800)
    
    i = 0
    with open(filePathCity, "r") as file:
        next(file)  # Skip the header
        for line in file:
            line = line.split(",")
            #if line[0] in selected_houses:
            x = int(((float(line[1]) - min_x) / scale) * 800)
            y = int(((float(line[2].replace("\n","")) - min_y) / scale) * 800)
            city[i] = (x, y)
            i += 1

    return city, depot




#filePath = "methods/glpk-solver/solver_drone_cmd_output.log"
filePath = "methods/glpk-solver/cmd_output/solver_drone_cmd_output_20_6.log"
filePathCity = "utils/generationColi/generationRealisticCity/generateData/smallCity_20.csv"

def recupData(filePath):
    with open(filePath, "r") as f:
        data = f.read() # read all lines at once

    data = data.split("\n")
    drones = []
    houses = []

    #exemple data : The package 8 is delivered by the drone 2 to the house 2

    for line in data:
        drone = re.findall(r'drone\s\d+', line)
        house = re.findall(r'house\s\d+', line)
        if drone:
            drones.append(drone[0].split(' ')[1])
            
        if house:
            houses.append(house[0].split(' ')[1])

    return drones, houses

drones, houses = recupData(filePath)

print("drones : ", drones) 
print("houses : ", houses)

nombreDrone = max(drones)

city,depot = recupCity(filePathCity)
print(depot)
window = tk.Tk()
window.title("City")
canvas = tk.Canvas(window, width=1200, height=800, bg="white")
canvas.pack()

canvas.create_oval(depot[0]-10, depot[1]-10, depot[0]+10, depot[1]+10, outline="black")
canvas.create_rectangle(0, 0, 800, 800, outline="black")

im = tk.PhotoImage(file = "utils/Visualisation/VisuGlpk/charge.png",master=window)


listDrones = {}
listHouses = {}

from PIL import Image, ImageSequence
import io
import time

image = []

for house in city:
    listHouses[str(int(house)+1)] = House(str(int(house)+1),city[house],canvas)
    print(listHouses[str(int(house)+1)])

for drone in range(1,int(nombreDrone) +1):
    package = []
    for k in range(1):
        for j,i in enumerate(drones):
            if int(i) == drone:
                package.append(Package(str(j)+str(k),str(drone),listHouses[houses[j]]))
    listDrones[str(drone)] = Drone(str(drone),depot,canvas, package,depot)


globalCity = City("Grenoble",listDrones,listHouses,[],canvas,depot)
time0 = time.time()
i = 0
while time.time() - time0 < 70 :
    globalCity.execution()
    if i%5 == 0:
        ps = canvas.postscript(colormode='color')
        im_ = Image.open(io.BytesIO(ps.encode('utf-8')))
        image.append(im_)
    i+=1
canvas_width = canvas.winfo_width()
canvas_height = canvas.winfo_height()
window.destroy()
import numpy as np

# Create a gif from the list of images
image[0].save('utils/Visualisation/VisuGlpk/animation.gif',
               save_all=True,
               append_images=image[1:],
               duration=100,
               loop=0)

