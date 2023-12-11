import random
import re
import tkinter as tk
from math import sqrt
import time
class City:
    def __init__(self, name, drones, houses,packages):
        self.name = name
        
        self.drones = drones
        self.houses = houses
        self.packages = packages

    def __str__(self):
        return "City " + str(self.name) + " : " + str(self.pos)
    

class House:
    def __init__(self, id, pos, canvas):
        self.id = id
        self.pos = pos
        self.canvas = canvas
        self.canvas.create_rectangle(self.pos[0], self.pos[1], self.pos[0]+5, self.pos[1]+5, fill="blue")

    def __str__(self):
        return "House " + str(self.id) + " : " + str(self.pos)


class Drone:
    def __init__(self, id, pos, canvas):
        self.id = id
        self.pos = pos
        self.isCharging = False
        self.isDelivering = False
        self.packageHolding = None
        self.objectif = None
        
        self.listPackage = []
        
        self.canvas = canvas
        self.canvas.create_oval(self.pos[0], self.pos[1], self.pos[0]+5, self.pos[1]+5, fill="red", tags="drone"+str(self.id))
        print("Drone create")

    def __str__(self):
        return "Drone " + str(self.id) + " : " + str(self.pos)
    
    def addPackage(self):
        if self.packageHolding == None:
            package = self.listPackage[0]
            self.packageHolding = package
            self.objectif = package.houseTarget.pos
            self.isDelivering = True
        
    def removePackage(self):
        self.packageHolding = None
        self.listPackage.remove(self.packageHolding)
        self.objectif = (400,400)
        self.isDelivering = False

    def charge(self):
        self.isCharging = True

    def move_drone(self, pos):
        self.pos = pos
        self.canvas.move("drone"+str(self.id), self.pos[0], self.pos[1])

    def advance(self):
        
        if not(self.isCharging):
            normVector = norm(self.objectif[0] - self.pos[0],self.objectif[1] - self.pos[1])*0.1
            self.move_drone(((self.objectif[0] - self.pos[0])/normVector, (self.objectif[1] - self.pos[1])/normVector))
        elif len(self.listPackage) == 0:
            self.canvas.delete("drone"+str(self.id))

        if self.pos == self.objectif and self.isDelivering:
            self.removePackage()

        #print("Drone advance to " + str(self.pos))
        

class Package:
    def __init__(self, id, byDrone, houseTarget):
        self.id = id
        self.byDrone = byDrone
        self.houseTarget = houseTarget

    def __str__(self):
        return "Package " + str(self.id) + " : by drone " + str(self.byDrone) + " to house " + str(self.houseTarget)


def norm(x,y):
    return sqrt(x**2 + y**2)

def recupCity():
    city = {}
    for i in range(10):
        city[str(i)] = (random.randint(0, 800), random.randint(0, 800))
    return city

filePath = "methods/glpk-solver/solver_drone_cmd_output.log"
filePathCity = ""

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
print("nombre de drone : ", nombreDrone)

city = recupCity()

window = tk.Tk()
window.title("City")
canvas = tk.Canvas(window, width=800, height=800, bg="white")
canvas.pack()

canvas.create_oval(400, 400, 405, 405, fill="green")

listDrones = {}
listHouses = {}


for house in city:
    listHouses[str(int(house)+1)] = House(str(int(house)+1),city[house],canvas)
    print(listHouses[str(int(house)+1)])

for drone in range(1,int(nombreDrone) +1):
    listDrones[drone] = Drone(str(drone),(400,400),canvas)
    for j,i in enumerate(drones):
        if int(i) == drone:
            listDrones[drone].listPackage.append(Package(str(j),str(drone),listHouses[houses[j]]))


globalCity = City("Grenoble",listDrones,listHouses,[])
while True:
    for drone in listDrones:
        listDrones[drone].addPackage()
        listDrones[drone].advance()
    time.sleep(0.1)
    window.update()


window.mainloop()

