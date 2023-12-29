param nb_p; # Number of packages
param nb_d; # Number of drones
param nb_h; # Number of houses
param nb_dp;# Number of deposits
param nb_action;

param autonomy_max;         # Maximum autonomy time during which a drone can fly
param autonomy_initial;     # Starting autonomy time
param recharge_time;
param time_upper_bound;     # Maximum time to deliver all packages (At most 1 day)

param delivery_time{dp in nb_dp, h in 1..nb_h+nb_dp};  # List of the times it takes for the drone to deliver a specific house from a deposit
param orders{p in 1..nb_p};                         # List of the orders

# Package start at 0 because he can not take any package
# var x{p in 0..nb_p, d in 1..nb_d, h in 1..nb_h}, binary;
var x{p in 0..nb_p, d in 1..nb_d, h in 0..nb_h, dp in 0..nb_dp}, binary;    # Is true if the package p has been delivered by the drone d to the house h
var s{d in 1..nb_d,p in 0..nb_p, h in 0..nb_h, dp in 0..nb_dp}
var t{d in 1..nb_d}, integer;                                               # List of the time took by each drone to deliver his packages
var time_max, integer;                                                      # max(t), t defined above
var autonomy{d in 1..nb_d, a in 1..nb_action}, integer, <= autonomy_max, >= 0;
var position{d in 1..nb_d, a in 1..nb_action}, integer, <= nb_dp, >0;
var time_frame{d in 1..nb_d, a in 1..nb_action}, integer;

minimize f : time_max;  # The goal of this program is to find the minimum max(t) possible

subject to # Constraints definition

# Basic problem constraints

clamp_max_action{d in 1..nb_d} := sum{p in 0..nb_p, h in 0..nb_h, dp in 0..nb_dp} x[d, p, h, dp] <= nb_action;
only_useful_delivery:= sum{d in 1..nb_d, h in 1..nb_h} x[d, 0, h, 0] = 0;                 # Remove the possibiluty to go to a house without package
package_deliverd_once {p in 1..nb_p}: sum{d in 1..nb_d, h in 1..nb_h} x[d, p, h, 0] = 1;   # Check if the package p is delivered once (and only once)
package_well_delivered {p in 1..nb_p}: sum{d in 1..nb_d} x[p, d, orders[p],0] = 1;        # Check if the order is respected

# Autonomy constraints
time_spent_flying {d in 1..nb_d}: t[d] = sum{p in 1..nb_p, h in 1..nb_h} (delivery_times[1,h]*x[d,p,h]) ;           # Compute time spent flying for each drone
autonomy_not_exceeded {d in 1..nb_d, p in 1..nb_p, h in 1..nb_h}: (delivery_times[1,h]*x[d, p, h]) <= autonomy_max; # Check if delivery is possible
check_autonomy {d in 1..nb_d}: t[d] <= (autonomy_initial + time_max)/2;

solve;

# Print the result on the console

for {p in 1..nb_p, d in 1..nb_d, h in  1..nb_h} {
    for{{0}: x[d, p, h] == 1} {
        printf "The package %d is delivered by the drone %d to the house %d\n", p, d, h;
    }
}
printf "Distance %f\n", delivery_times[1,1];

end;
