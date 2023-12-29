param nb_p; # Number of packages
param nb_d; # Number of drones
param nb_h; # Number of houses

param autonomy_max;         # Maximum autonomy time during which a drone can fly
param autonomy_initial;     # Starting autonomy time
param recharge_time;
param time_upper_bound;     # Maximum time to deliver all packages (At most 1 day)

param delivery_time{i in 1..nb_h};  # List of the times it takes for the drone to deliver a specific house
param orders{i in 1..nb_p};         # List of the orders

# TODO use coordinate for city instead of distance (easier for multideposit)

var x{p in 1..nb_p, d in 1..nb_d, h in 1..nb_h}, binary;    # Is true if the package p has been delivered by the drone d to the house h
var t{d in 1..nb_d}, integer;                               # List of the time took by each drone to deliver his packages
var time_max, integer;                                      # max(t), t defined above

minimize f : time_max;  # The goal of this program is to find the minimum max(t) possible

subject to # Constraints definition

# Basic problem constraints

package_deliverd_once {p in 1..nb_p}: sum{d in 1..nb_d, h in 1..nb_h} x[p, d, h] = 1;   # Check if the package p is delivered once (and only once)
package_well_delivered {p in 1..nb_p}: sum{d in 1..nb_d} x[p, d, orders[p]] = 1;        # Check if the order is respected

# Autonomy constraints
time_spent_flying {d in 1..nb_d}: t[d] = sum{p in 1..nb_p, h in 1..nb_h} (delivery_time[h]*x[p, d, h]) ;        # Compute time spent flying for each drone
autonomy_not_exceeded {d in 1..nb_d, p in 1..nb_p, h in 1..nb_h}: (delivery_time[h]*x[p, d, h]) <= autonomy_max;# Check if delivery is possible
check_autonomy {d in 1..nb_d}: t[d] <= (autonomy_initial + time_max)/2;

solve;

# Print the result on the console

for {p in 1..nb_p, d in 1..nb_d, h in  1..nb_h} {
    for{{0}: x[p, d, h] == 1} {
        printf "The package %d is delivered by the drone %d to the house %d\n", p, d, h;
    }
}

end;
