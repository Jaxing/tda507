//
// To compile this Java program, type:
//
//	javac GlobalAlignment.java
//
// To run the program, type:
//
//	java GlobalAlignment
//

public class LocalAlignment {

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

        String X = "PAWHEQAE";
        String Y = "HDAGAWGHEQ";

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

                if ( 0 > score) {
                    score = 0;
                    trace[i][j] = STOP;
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
        // Find max value in score matrix
        //

        int max_j = n;
        int max_i = m;
        int max_value = F[max_i][max_j];

        for (int k=m ; k >= 0 ; k--) {
            for (int l=n ; l >= 0 ; l--) {
                if (max_value < F[k][l]) {
                    max_i = k;
                    max_j = l;
                    max_value = F[k][l];
                }
            }
        }

        //
        // Trace back from the max element in score matrix
        //

        i = max_i;
        j = max_j;
        alignmentLength = 0;

        while ( trace[i][j] != STOP ) {
            switch ( trace[i][j] ) {

                case DIAG:
                    alignX[alignmentLength] = X.charAt(i-1);
                    alignY[alignmentLength] = Y.charAt(j-1);
                    bindAlign[alignmentLength] = '|';
                    if (alignX[alignmentLength] == alignY[alignmentLength]) {
                        alignedChars++;
                    }
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
                    break;

                case UP:
                    alignX[alignmentLength] = X.charAt(i-1);
                    alignY[alignmentLength] = '-';
                    bindAlign[alignmentLength] = ' ';
                    i--;
                    alignmentLength++;
            }
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
    }
}
