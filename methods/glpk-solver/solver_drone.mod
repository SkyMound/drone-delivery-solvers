param nb_c;
param nb_d;
param nb_m;

param dist_max;
param time_upper_bound;

param dist{i in 1..nb_m};
param goal{i in 1..nb_c};

var x{i in 1..nb_c, j in 1..nb_d, k in 1..nb_m}, binary;
var t{j in 1..nb_d}, integer;
var temps_max, integer;

minimize f: temps_max;

subject to

colis_livre_une_fois {i in 1..nb_c}: sum{j in 1..nb_d, k in 1..nb_m} x[i, j, k] = 1;
colis_bien_livre {i in 1..nb_c}: sum{j in 1..nb_d} x[i, j, goal[i]] = 1;
#batterie {j in 1..nb_d}: sum{i in 1..nb_c, k in 1..nb_m} (dist[k]*x[i, j, k]) <= dist_max;

temps_drone_check {j in 1..nb_d}: t[j] = sum{i in 1..nb_c, k in 1..nb_m} (dist[k]*x[i, j, k]);
temps_max_check {j in 1..nb_d}: temps_max >= t[j];
temps_drone_upper_bound {j in 1..nb_d}: t[j] <= time_upper_bound;
temps_max_upper_bound: temps_max <= time_upper_bound;

solve;

for {i in 1..nb_c, j in 1..nb_d, k in  1..nb_m} {
    for{{0}: x[i, j, k] == 1} {
        printf "Le colis %d est livre par le drone %d a la maison %d\n", i, j, k;
    }
}

end;
