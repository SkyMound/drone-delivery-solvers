param n;
param m;

param d{i in 1..n, j in 1..m};

var x{i in 1..n, j in 1..m}, integer >= 0;

minimize f: sum {i in 1..n, j in 1..m} (d[i,j]*x[i,j]); #fonction du cout de livraison total

subject to

depot1 : x[1,1] + x[1,2] + x[1,3] = 8;
depot2 : x[2,1] + x[2,2] + x[2,3] = 9;

client1 : x[1,1] + x[2,1] = 4;
client2 : x[1,2] + x[2,2] = 5;
client3 : x[1,3] + x[2,3] = 8;

display: n,m,d;

solve;

display: x;

data;

param n := 2; #nombre de depots
param m := 3; #nombre de clients

param d:  1 2 3 := #cout de livraison de n vers m
      1   5 3 4 
      2   6 7 2;

end;
