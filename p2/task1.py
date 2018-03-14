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


def distance_map(coordinates):
    threshold = 7
    dm = []

    for i in range(len(coordinates)):
        atom1 = coordinates[i]
        dm.append([])
        for atom2 in coordinates:
            dist = distance(atom1, atom2)

            if dist < threshold:
                dm[i].append(1)
            else:
                dm[i].append(0)
    return dm


def main():
    file_name = input("Give a path to PDB file:")
    file_name = os.path.join(DIR_PATH, "data-files/{}.pdb"
                             .format(file_name.upper()))
    atoms = read_data(file_name)
    dist_map = distance_map(atoms)

    for i in range(len(dist_map)):
        for j in range(len(dist_map)):
            if dist_map[i][j]:
                plot.plot(i, j, 'ko')
    plot.show()

if __name__ == '__main__':
    main()