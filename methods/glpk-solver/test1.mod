set Houses;  # Ensemble des maisons
param Capacity;  # Capacité du drone
param MaxFlightTime;  # Temps de vol maximal du drone

set HousesSet := {1..5};  # Exemple d'ensemble de maisons (1 à 5)
param Distance{depot in 1..1, h in HousesSet} :=
  1 1 10
  1 2 15
  1 3 12
  1 4 8
  1 5 20;


var Delivery{depot in 1..1, h in HousesSet} binary;  # 1 si le drone livre à cette maison, 0 sinon

minimize TotalCost:
    sum {h in HousesSet} Distance[1, h] * Delivery[1, h];

subject to CapacityConstraint:
    sum {h in HousesSet} Delivery[1, h] <= Capacity;

subject to TimeConstraint:
    sum {h in HousesSet} Distance[1, h] * Delivery[1, h] <= MaxFlightTime;
