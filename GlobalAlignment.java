//
// To compile this Java program, type:
//
//	javac GlobalAlignment.java
//
// To run the program, type:
//
//	java GlobalAlignment
//


import java.util.*;
class TraceNode {
    List<TraceNode> children = new ArrayList();
    int score;
    int direction;
    char X;
    char Y;
    char binding;


    public TraceNode(int d) {
        direction = d;
    }

    public TraceNode() {
        score = 0;
        direction = 0;
    }
}
public class GlobalAlignment {

    public static int nbr_of_optimal = 1;
    public static final int MAX_LENGTH	= 100;

    public static final int MATCH_SCORE	= 2;
    public static final int MISMATCH_SCORE	= -1;
    public static final int GAP_PENALTY	= -2;

    public static final int STOP		= 0;
    public static final int UP		= 1;
    public static final int LEFT		= 2;
    public static final int DIAG		= 3;

    public static void main(String[] args) {

        int i, j;
        int alignmentLength, score, tmp;

        String X = "ATTA";
        String Y = "ATTTTA";

        int F[][] = new int[MAX_LENGTH+1][MAX_LENGTH+1];     /* score matrix */
        int trace[][] = new int[MAX_LENGTH+1][MAX_LENGTH+1]; /* trace matrix */
        char[] alignX = new char[MAX_LENGTH*2];	/* aligned X sequence */
        char[] alignY = new char[MAX_LENGTH*2];	/* aligned Y sequence */
        char[] bindAlign = new char[MAX_LENGTH*2];
        int alignedChars = 0;
        int hammingDistance = 0;
        int numberOfSolutions = 0;

        int m = X.length();
        int n = Y.length();
        int shortest = 0;

        if (m < n) {
            shortest = m;
        } else {
            shortest = n;
        }

        //
        // Initialise matrices
        //

        F[0][0] = 0;
        trace[0][0] = STOP;
        for ( i=1 ; i<=m ; i++ ) {
            F[i][0] = F[i-1][0] + GAP_PENALTY;
            trace[i][0] = STOP;
        }
        for ( j=1 ; j<=n ; j++ ) {
            F[0][j] = F[0][j-1] + GAP_PENALTY;
            trace[0][j] = STOP;
        }

        //
        // Fill matrices
        //

        for ( i=1 ; i<=m ; i++ ) {

            for ( j=1 ; j<=n ; j++ ) {

                if ( X.charAt(i-1)==Y.charAt(j-1) ) {
                    score = F[i-1][j-1] + MATCH_SCORE;
                } else {
                    score = F[i-1][j-1] + MISMATCH_SCORE;
                }
                trace[i][j] = DIAG;

                tmp = F[i-1][j] + GAP_PENALTY;
                if ( tmp>score ) {
                    score = tmp;
                    trace[i][j] = UP;
                }

                tmp = F[i][j-1] + GAP_PENALTY;
                if( tmp>score ) {
                    score = tmp;
                    trace[i][j] = LEFT;
                }

                F[i][j] = score;
            }
        }


        //
        // Print score matrix
        //

        System.out.println("Score matrix:");
        System.out.print("      ");
        for ( j=0 ; j<n ; ++j ) {
            System.out.print("    " + Y.charAt(j));
        }
        System.out.println();
        for ( i=0 ; i<=m ; i++ ) {
            if ( i==0 ) {
                System.out.print(" ");
            } else {
                System.out.print(X.charAt(i-1));
            }
            for ( j=0 ; j<=n ; j++ ) {
                System.out.format("%5d", F[i][j]);
            }
            System.out.println();
        }
        System.out.println();


        //
        // Trace back from the lower-right corner of the matrix
        //

        TraceNode traceTree = new TraceNode();
        createTraceTree(traceTree, X, Y, F, m, n);

        print(traceTree, new StringBuilder(), new StringBuilder(), new StringBuilder(), shortest);

        System.out.println("Number of optimal paths");
        System.out.println(count(traceTree));

        // Calculates the percent identity as number of aligned characters
        // divided by the shortest sequences, which would be the probabillity
        // that a character in the short sequence is correct.
//        System.out.println("Percent identity");
//        System.out.println(100.0 * alignedChars / shortest + "%");
//        System.out.println();

        int k = 0;
        int l = 0;
        while (k < n && l < m) {
            if (X.charAt(l) != Y.charAt(k)) {
                hammingDistance++;
            }
            k++;
            l++;
        }
        System.out.println("Hamming distance:");
        System.out.println(hammingDistance);
        System.out.println();
    }

    public static void createTraceTree(TraceNode currentNode, String X, String Y, int[][] scoreMatrix, int i, int j) {
        if (i-1 < 0 || j-1 < 0) {
            return;
        }
        int diag;
        int maxScore = -100000000;

        if ( X.charAt(i-1)==Y.charAt(j-1) ) {
            diag = scoreMatrix[i-1][j-1] + MATCH_SCORE;
        } else {
            diag = scoreMatrix[i-1][j-1] + MISMATCH_SCORE;
        }

        if (maxScore < diag) {
            maxScore = diag;
        }

        int gapI = scoreMatrix[i-1][j] + GAP_PENALTY;

        if ( gapI>maxScore ) {
            maxScore = gapI;
        }

        int gapJ = scoreMatrix[i][j-1] + GAP_PENALTY;

        if( gapJ>maxScore ) {
            maxScore = gapJ;
        }

        currentNode.score = maxScore;
        if ( diag == maxScore) {
            TraceNode child = new TraceNode(DIAG);
            child.X = X.charAt(i-1);
            child.Y = Y.charAt(j-1);
            child.binding = '|';
            createTraceTree(child, X, Y, scoreMatrix, i-1, j-1);
            currentNode.children.add(child);
        }

        if ( gapI == maxScore) {
            TraceNode child = new TraceNode(UP);
            child.X = X.charAt(i-1);
            child.Y = '-';
            child.binding = ' ';
            createTraceTree(child,X, Y, scoreMatrix, i-1, j);
            currentNode.children.add(child);
        }

        if ( gapJ == maxScore) {
            TraceNode child = new TraceNode(LEFT);
            child.X = '-';
            child.Y = Y.charAt(j-1);
            child.binding = ' ';
            createTraceTree(child, X, Y, scoreMatrix, i, j-1);
            currentNode.children.add(child);
        }
    }

    public static int count(TraceNode traceTree) {

        if (traceTree.children == null || traceTree.children.isEmpty()) {
            return 1;
        }

        int paths = 0;

        for (TraceNode node : traceTree.children) {
            paths += count(node);
        }

        return paths;
    }

    public static void print(TraceNode traceTree, StringBuilder X, StringBuilder Y, StringBuilder bind, int shortest){
        X.append(traceTree.X);
        Y.append(traceTree.Y);
        bind.append(traceTree.binding);
        if (traceTree.children == null || traceTree.children.isEmpty()) {
            System.out.format("Solution %d \n", GlobalAlignment.nbr_of_optimal++);
            System.out.println(X.toString());
            System.out.println(bind.toString());
            System.out.println(Y.toString());

            int correctAlingment = 0;
            for (int i = 1 ; i < X.length() ; i++) {
                    if (X.charAt(i) == Y.charAt(i)) {
                        correctAlingment++;
                    }
            }

            // Calculates the percent identity as number of aligned characters
            // divided by the shortest sequences.
            System.out.println("\nPercent identity: ");

            System.out.println(100.0 * correctAlingment / shortest + "%\n");
        }

        for (TraceNode node : traceTree.children) {
            print(node,
                    new StringBuilder(X.toString()),
                    new StringBuilder(Y.toString()),
                    new StringBuilder(bind.toString()),
                    shortest);
        }
    }
}
