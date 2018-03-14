import random


class HiddenMarkovModel(object):
    class State(object):

        def __init__(self, a, c, g, t, max_prob):
            self.PROB_A = a
            self.PROB_C = c+a
            self.PROB_G = g+c+a
            self.PROB_T = t+g+c+a
            self.MAX = max_prob

        def predict(self):
            r = random.randint(1, self.MAX)

            if r <= self.PROB_A:
                return "A"
            if r <= self.PROB_C:
                return "C"
            if r <= self.PROB_G:
                return "G"
            if r <= self.PROB_T:
                return "T"

    def __init__(self):
        self.states = []

    def add_state(self, state):
        self.states.append(state)

    def get_state(self, i):
        return self.states[i]

def main():


if __name__ == '__main__':

