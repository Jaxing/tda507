import os
import math
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
                parsed_lines.append([float(line[X]),
                                     float(line[Y]),
                                     float(line[Z])])

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
    for j in range(len(atoms)):
        nbr_collisions += count_collisions(atoms[j], atoms[:j+1])

    return nbr_collisions


def main():
    min_length_segment = 10
    file_name = input("Give a path to PDB file:")
    file_name = os.path.join(DIR_PATH, "data-files/{}.pdb"
                             .format(file_name.upper()))
    atoms = read_data(file_name)

    seg_a = atoms[:min_length_segment]
    seg_b = atoms[min_length_segment:]
    best_score = 0
    best_length = -1
    length_min_segment = min_length_segment
    to_plot = []
    while len(seg_b) > min_length_segment:
        internal_a = count_internal_collisions(seg_a)
        internal_b = count_internal_collisions(seg_b)
        external = count_external_collisions(seg_a, seg_b)
        score = internal_a * internal_b / external
        if score > best_score:
            best_score = score
            best_length = length_min_segment
        to_plot.append(score)

        length_min_segment += 1
        seg_a = atoms[:length_min_segment]
        seg_b = atoms[length_min_segment:]
    plot.plot(to_plot, 'k-')
    plot.show()
    print(best_length)

if __name__ == '__main__':
    main()