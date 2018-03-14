import os
import math
import matplotlib.pyplot as plot
DIR_PATH = "/home/jaxing/bioinf/p4"

class Atom(object):

    def __init__(self, x, y, z, residue, residue_num@!,chain_number, type):
        self.x = float(x)
        self.y = float(y)
        self.z = float(z)
        self.residue = residue
        self.chain_number = int(chain_number)
        self.type = type
        self.residue_num = int(residue_num)

    def get_position(self):
        return list([self.x, self.y, self.z])

def read_data(file_name):
    X = 6
    Y = 7
    Z = 8
    DATA_TYPE = 0
    ATOM_TYPE = 2
    RESIDUE = 3
    RESIDUE_NUM = 5
    ID = 1

    parsed_lines = []
    with open(file_name, 'r') as file:
        line = file.readline()

        while "END " not in line:
            line = list(filter(None, line.split(" ")))
            if line[DATA_TYPE] == "ATOM" or \
                    line[DATA_TYPE] == "HETATM":
                parsed_lines.append(Atom(line[X], line[Y], line[Z], line[RESIDUE],
                                         line[RESIDUE_NUM], line[ID], line[ATOM_TYPE]))

            line = file.readline()
    return parsed_lines


def distance(a1, a2):
    """Calculates the euclidian distance between two atoms"""
    [x1, y1, z1] = a1
    [x2, y2, z2] = a2

    return math.sqrt((x1 - x2)**2 + (y1 - y2)**2 + (z1 - z2)**2)


def main():
    """"
        This is a brute force algorithm that compares each atom in the first
        chain with all the other atoms in the second chain. To prune the search
        space a little, all the atoms that collied with another atom are removed.
        the algorithm is O(NM), N being the length of the first chain and M being
        the length of the second chain.
    """

    min_length_segment = 10

    file_name = input("Give path to first PDB file:")
    file_name = os.path.join(DIR_PATH, "data-files/{}.pdb"
                             .format(file_name.upper()))
    atoms_1 = read_data(file_name)

    file_name = input("Give path to second PDB file:")
    file_name = os.path.join(DIR_PATH, "data-files/{}.pdb"
                             .format(file_name.upper()))
    atoms_2 = read_data(file_name)

    clashes = 0
    comparisons = 0
    for i in range(len(atoms_1)):
        j = 0
        while j < len(atoms_2):
            comparisons += 1
            if distance(atoms_1[i].get_position(), atoms_2[j].get_position()) < 4:
                print("{}\t{}\t{}\t{}".
                      format(atoms_2[j].chain_number, atoms_2[j].residue,
                             atoms_2[j].residue_num, atoms_2[j].type))
                clashes += 1
                del atoms_2[j]
                continue
            j += 1

    print("Number of clashing atoms:", clashes)
    print("Number of comparisons:", comparisons)
if __name__ == '__main__':
    main()