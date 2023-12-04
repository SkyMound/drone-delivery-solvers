param nb_p; # Number of packages
param nb_d; # Number of drones
param nb_h; # Number of houses

param dist_max;         # Maximum distance reachable by a drone
param time_upper_bound; # Maximum time to deliver all packages (At most 1 day)

param dist{i in 1..nb_h};   # List of the distances from the deposit to the houses
param orders{i in 1..nb_p}; # List of the orders


var x{p in 1..nb_p, d in 1..nb_d, h in 1..nb_h}, binary;    # Is true if the package p has been delivered to the house h by the drone d
var t{d in 1..nb_d}, integer;                               # List of the time took by each drone to deliver his packages
var time_max, integer;                                      # The maximum of the list t defined above

minimize f: time_max;

subject to

package_deliverd_once {p in 1..nb_p}: sum{d in 1..nb_d, h in 1..nb_h} x[p, d, h] = 1;
package_well_delivered {p in 1..nb_p}: sum{d in 1..nb_d} x[p, d, orders[p]] = 1;
#battery {d in 1..nb_d}: sum{p in 1..nb_p, h in 1..nb_h} (dist[h]*x[p, d, h]) <= dist_max;

compute_time_spend {d in 1..nb_d}: t[d] = sum{p in 1..nb_p, h in 1..nb_h} (dist[h]*x[p, d, h]);
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
