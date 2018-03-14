//
// To compile this Java program, type:
//
//	javac GlobalAlignment.java
//
// To run the program, type:
//
//	java GlobalAlignment
//

public class Levenshtein {

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

        String X = "ATCGAT";
        String Y = "ATACGT";

        int F[][] = new int[MAX_LENGTH+1][MAX_LENGTH+1];     /* score matrix */
        int trace[][] = new int[MAX_LENGTH+1][MAX_LENGTH+1]; /* trace matrix */
        char[] alignX = new char[MAX_LENGTH*2];	/* aligned X sequence */
        char[] alignY = new char[MAX_LENGTH*2];	/* aligned Y sequence */
        char[] bindAlign = new char[MAX_LENGTH*2];
        int alignedChars = 0;
        int hammingDistance = 0;

        int m = X.length();
        int n = Y.length();
        int shortest = 0;
        int count = 0;

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

        i = m;
        j = n;
        alignmentLength = 0;

        while ( trace[i][j] != STOP ) {

            switch ( trace[i][j] ) {

                case DIAG:
                    if (X.charAt(i-1) != Y.charAt(j-1)) {
                        count++; // Change
                    } else {
                        alignedChars++;
                    }
                    alignX[alignmentLength] = X.charAt(i-1);
                    alignY[alignmentLength] = Y.charAt(j-1);
                    bindAlign[alignmentLength] = '|';
                    i--;
                    j--;
                    alignmentLength++;
                    break;

                case LEFT:
                    alignX[alignmentLength] = '-';
                    alignY[alignmentLength] = Y.charAt(j-1);
                    bindAlign[alignmentLength] = ' ';
                    j--;
                    alignmentLength++;
                    count++; // insert
                    break;

                case UP:
                    alignX[alignmentLength] = X.charAt(i-1);
                    alignY[alignmentLength] = '-';
                    bindAlign[alignmentLength] = ' ';
                    i--;
                    alignmentLength++;
                    count++; // insert
            }
        }


        //
        // Unaligned beginning
        //

        while ( i>0 ) {
            alignX[alignmentLength] = X.charAt(i-1);
            alignY[alignmentLength] = '-';
            count++; // delete
            i--;
            alignmentLength++;
        }

        while ( j>0 ) {
            alignX[alignmentLength] = '-';
            alignY[alignmentLength] = Y.charAt(j-1);
            count++; // delete
            j--;
            alignmentLength++;
        }


        //
        // Print alignment
        //

        for ( i=alignmentLength-1 ; i>=0 ; i-- ) {
            System.out.print(alignX[i]);
        }
        System.out.println();
        for ( i=alignmentLength-1 ; i>=0 ; i--) {
            System.out.print(bindAlign[i]);
        }
        System.out.println();
        for ( i=alignmentLength-1 ; i>=0 ; i-- ) {
            System.out.print(alignY[i]);
        }
        System.out.println();
        System.out.println();

        // Calculates the percent identity as number of aligned characters
        // divided by the shortest sequences.
        System.out.println("Percent identity");
        System.out.println(1.0 * alignedChars / shortest);
        System.out.println();

        for ( i=alignmentLength-1 ; i > 0 ; i--) {
            if (alignX[i] != alignY[i]) {
                hammingDistance++;
            }
        }
        System.out.println("Hamming distance:");
        System.out.println(hammingDistance);
        System.out.println();

        System.out.println("Levenshtein distance:");
        System.out.println(count);
        System.out.println();
    }
}
