# Practical 2
## Task 1
![dist_map_cdh](./data-files/1cdh_distance_map.png)
![dist_map csn](./data-files/2csn_distance_map.png)

The images above shows the distance maps for 1CDH and 2CSN, respectively, when using a threshold of 7 Å.

## Task 2
![domak_cdh](./data-files/1cdh_domak_score.png)
![domak_csn](./data-files/2csn_domak_score.png)

The images above shows the score of IntA * IntB / ExtAB. The horizontal axis represents the length of the first domain.
I have used the constraint that a domain is at least 10 residues.
For 1CDH the optimal split is at the 98th residue.
For 2CSN the optimal split is at the 82nd residue.

## Task 3

I did not get this to work properly. I think that I got the swapping part correct but I don't really understand how the U are generated.
Also it seems as a very unnecessary thing to randomly generate partions since (I think) that could lead to that one set is not continues.
