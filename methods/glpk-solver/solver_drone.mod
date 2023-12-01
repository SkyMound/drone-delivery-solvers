param nb_c;
param nb_d;
param nb_m;

param dist_max;

param dist {i in 1..nb_m};
param goal {i in 1..nb_c};

var x{i in 1..nb_c, j in 1..nb_d, k in 1..nb_m}, binary;

minimize f: sum{i in 1..nb_c, j in 1..nb_d, k in 1..nb_m} (dist[k]*x[i, j, k]);

subject to

colis_livre_une_fois {i in 1..nb_c}: sum {j in 1..nb_d, k in 1..nb_m} x[i, j, k] = 1;
colis_bien_livre {i in 1..nb_c}: sum {j in 1..nb_d} x[i, j, goal[i]] = 1;
batterie: sum {i in 1..nb_c, j in 1..nb_d, k in 1..nb_m} (dist[k]*x[i, j, k]) >= dist_max;



solve;



for {i in 1..nb_c, j in 1..nb_d, k in  1..nb_m} {
    for{{0}: x[i, j, k] == 1} {
        printf "Le colis %d est livre par le drone %d a la maison %d\n", i, j, k;
    }
    printf "%d",sum {i in 1..nb_c, j in 1..nb_d, k in 1..nb_m} (dist[k]*x[i, j, k]);
}

end;
