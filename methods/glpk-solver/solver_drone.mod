param nb_p; # Number of packages
param nb_d; # Number of drones
param nb_h; # Number of houses

param autonomy_max;         # Maximum autonomy time during which a drone can fly
param autonomy_initial;     # Starting autonomy time
param time_upper_bound;     # Maximum time to deliver all packages (At most 1 day)

param delivery_time{i in 1..nb_h};  # List of the times it took for the drone to deliver a specific house
param orders{i in 1..nb_p};         # List of the orders


var x{p in 1..nb_p, d in 1..nb_d, h in 1..nb_h}, binary;    # Is true if the package p has been delivered to the house h by the drone d
var t{d in 1..nb_d}, integer;                               # List of the time took by each drone to deliver his packages
var time_max, integer;                                      # The maximum of the list t defined above

minimize f: time_max;

subject to

package_deliverd_once {p in 1..nb_p}: sum{d in 1..nb_d, h in 1..nb_h} x[p, d, h] = 1;
package_well_delivered {p in 1..nb_p}: sum{d in 1..nb_d} x[p, d, orders[p]] = 1;

autonomy_not_exceeded {d in 1..nb_d, p in 1..nb_p, h in 1..nb_h}: (delivery_time[h]*x[p, d, h]) <= autonomy_max;
autonomy_used {d in 1..nb_d}: t[d] = sum{p in 1..nb_p, h in 1..nb_h} (delivery_time[h]*x[p, d, h]);
check_autonomy {d in 1..nb_d}: t[d] <= (autonomy_initial + time_max) / 2;

# Add recharge time for each delivery based on delivery time  

check_maximum_time {d in 1..nb_d}: time_max >= t[d];

# Optimization purpose
drone_time_upper_bound {d in 1..nb_d}: t[d] <= time_upper_bound;
time_max_upper_bound: time_max <= time_upper_bound;

solve;

for {p in 1..nb_p, d in 1..nb_d, h in  1..nb_h} {
    for{{0}: x[p, d, h] == 1} {
        printf "The package %d is delivered by the drone %d to the house %d\n", p, d, h;
    }
}

end;
