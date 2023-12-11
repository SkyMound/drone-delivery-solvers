import random
import re
import tkinter as tk
from math import sqrt
import time
from PIL import Image, ImageTk 




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
    def __init__(self, id, pos, canvas, package):
        self.id = id
        self.pos = pos
        self.isCharging = False
        self.isDelivering = False
        self.packageHolding = None
        self.objectif = None
        self.destroy = False
        self.battery = 100
        
        self.listPackage = package
        
        self.canvas = canvas
        self.canvas.create_oval(self.pos[0], self.pos[1], self.pos[0]+5, self.pos[1]+5, fill="red", tags="drone"+str(self.id))
        print("Drone create")

        self. canvas.create_text(830, int(self.id)*100 - 90, text="Drone "+str(self.id))
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
        self.objectif = (400,400)
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
                self.battery -= 0.1
                normVector = norm(self.objectif[0] - self.pos[0],self.objectif[1] - self.pos[1])
                self.move_drone(((self.objectif[0] - self.pos[0])/normVector, (self.objectif[1] - self.pos[1])/normVector))
                if self.isAtObjectif() and self.isDelivering:
                    self.removePackage()

                if self.battery <=30 and self.atSpawn():
                    self.charge()

            if self.isCharging:
                self.battery += 0.2

            if self.battery >= 95 and self.isCharging:
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
        if self.pos[0] >= 400-5 and self.pos[0] <= 400+5 and self.pos[1] >= 400-5 and self.pos[1] <= 400+5:
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
print("Numbers of drone : ", nombreDrone)

city = recupCity()

window = tk.Tk()
window.title("City")
canvas = tk.Canvas(window, width=1200, height=800, bg="white")
canvas.pack()

canvas.create_oval(400-10, 400-10, 400+10, 400+10, outline="black")
canvas.create_rectangle(0, 0, 800, 800, outline="black")

im = tk.PhotoImage(file = "utils/DataPrint/charge.png",master=window)


listDrones = {}
listHouses = {}




for house in city:
    listHouses[str(int(house)+1)] = House(str(int(house)+1),city[house],canvas)
    print(listHouses[str(int(house)+1)])

for drone in range(1,int(nombreDrone) +1):
    package = []
    for k in range(3):
        for j,i in enumerate(drones):
            if int(i) == drone:
                package.append(Package(str(j)+str(k),str(drone),listHouses[houses[j]]))

    listDrones[drone] = Drone(str(drone),(400,400),canvas, package)


globalCity = City("Grenoble",listDrones,listHouses,[])
while True:
    for drone in listDrones:
        listDrones[drone].addPackage()
        listDrones[drone].advance()
    time.sleep(0.005)
    window.update()


window.mainloop()

