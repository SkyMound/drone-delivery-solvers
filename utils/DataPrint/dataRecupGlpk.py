import random
import re
import tkinter as tk

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
        self.packageHolding = None
        self.listPackage = []
        self.canvas = canvas
        self.canvas.create_circle(self.pos[0], self.pos[1], self.pos[0]+5, self.pos[1]+5, fill="red", tags="drone"+str(self.id))

    def __str__(self):
        return "Drone " + str(self.id) + " : " + str(self.pos)
    
    def addPackage(self, package):
        self.packageHolding = package
        self.isCharging = True
    
    def move(self, pos):
        self.pos = pos
        self.canvas.move("drone"+str(self.id), self.pos[0], self.pos[1])

    def advance(self):
        if self.isCharging:
            
        else:
            self.move((0,0))


class Package:
    def __init__(self, id, byDrone, houseTarget):
        self.id = id
        self.byDrone = byDrone
        self.houseTarget = houseTarget

    def __str__(self):
        return "Package " + str(self.id) + " : by drone " + str(self.byDrone) + " to house " + str(self.houseTarget)


def recupCity():
    city = {}
    for i in range(10):
        city[str(i)] = (random.randint(0, 1000), random.randint(0, 1000))
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
canvas = tk.Canvas(window, width=1100, height=1100, bg="white")
canvas.pack()
listDrones = {}
listHouses = {}
for drone in range(1,int(nombreDrone) +1):
    listDrones[drone] = Drone(str(drone),(0,0))
    for j,i in enumerate(drones):
        if int(i) == drone:
            listDrones[drone].listPackage.append(Package(str(j),str(drone),str(houses[j])))

for house in city:
    listHouses[house] = House(str(house),city[house],canvas)

globalCity = City("Grenoble",listDrones,listHouses,[])

window.mainloop()

