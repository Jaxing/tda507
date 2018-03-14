import os
import math
import random
import matplotlib.pyplot as plot
DIR_PATH = "/home/jaxing/bioinf/p2"

def read_data(file_name):
    X = 6
    Y = 7
    Z = 8
    DATA_TYPE = 0
    ATOM_TYPE = 11
    ANNOTATED_ATOM_TYPE = 2

    parsed_lines = []
    with open(file_name, 'r') as file:
        line = file.readline()

        while "END " not in line:
            line = list(filter(None, line.split(" ")))
            if line[DATA_TYPE] == "ATOM" and \
                line[ATOM_TYPE] == "C" and \
                    line[ANNOTATED_ATOM_TYPE] == "CA":
                parsed_lines.append((float(line[X]),
                                     float(line[Y]),
                                     float(line[Z])))

            line = file.readline()
    return parsed_lines


def distance(a1, a2):
    """Calculates the euclidian distance between two atoms"""
    [x1, y1, z1] = a1
    [x2, y2, z2] = a2

    return math.sqrt((x1 - x2)**2 + (y1 - y2)**2 + (z1 - z2)**2)


def count_collisions(atom, segment):
    threshold = 7
    nbr_collisions = 0

    for atom_b in segment:
        dist = distance(atom, atom_b)

        if dist < threshold:
            nbr_collisions += 1

    return nbr_collisions


def count_external_collisions(atoms_a, atoms_b):
    nbr_collisions = 0
    for atom_a in atoms_a:
        nbr_collisions += count_collisions(atom_a, atoms_b)
    for atom_b in atoms_b:
        nbr_collisions += count_collisions(atom_b, atoms_a)
    return nbr_collisions


def count_internal_collisions(atoms):
    nbr_collisions = 0
    for atom in atoms:
        nbr_collisions += count_collisions(atom, atoms)

    return nbr_collisions


def next_pair_to_swap(U, V):
    max_u_collision = 0
    index_u = 0
    max_v_collision = 0
    index_v = 0

    for u in range(len(U)):
        collisions = count_collisions(U[u], V)
        if collisions > max_u_collision:
            max_u_collision = collisions
            index_u = u
    for v in range(len(V)):
        collisions = count_collisions(V[v], U)
        if collisions > max_v_collision:
            max_v_collision = collisions
            index_v = v
    return index_u, index_v


def find_best_partition(U, V):
    tmp = [(v, False) for v in V]
    visited = dict(tmp)

    while not all(visited.values()):
        u, v = next_pair_to_swap(U, V)

        if v and u:
            U.append(V[v])
            V.append(U[u])
            visited[V[v]] = True
            del V[v]
            del U[u]
        else:
            return U, V


def main():
    min_length_segment = 10
    file_name = input("Give a path to PDB file:")
    file_name = os.path.join(DIR_PATH, "data-files/{}.pdb"
                             .format(file_name.upper()))
    atoms = read_data(file_name)

    U = atoms[:min_length_segment]
    V = atoms[min_length_segment:]

    best_score = 0
    best_U = U
    best_V = V
    print(len(atoms))

    for i in range(6):
        rand = random.randint(0, len(atoms) - 1)

        if rand > len(atoms) // 2:
            U_seed = [atoms[x] for x in range(min_length_segment, rand+1)]
            V_seed = [atoms[x] for x in range(min_length_segment)] + \
                     [atoms[x] for x in range(rand+1, len(atoms))]
        else:
            U_seed = [atoms[x] for x in range(rand, rand+min_length_segment+1)]
            V_seed = [atoms[x] for x in range(rand)] + \
                     [atoms[x] for x in range(min_length_segment+1, len(atoms))]
        U_new, V_new = find_best_partition(U_seed, V_seed)


    print(best_U)
    print(best_V)
    print(len(best_U), len(best_V))

if __name__ == '__main__':
    main()