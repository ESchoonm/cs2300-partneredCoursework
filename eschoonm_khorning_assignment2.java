import java.util.ArrayList;

/*

*/

public class eschoonm_khorning_assignment2 {
    public static void main(String[] args) {

        ArrayList<Double> slopes; // arraylist for the slopes previously calculated

        Board board = new Board(5);
        board.printGameBoard();
        // printBoard(board);
    }

    public static void printBoard(Board board) {
        board.printGameBoard();
    }

    /**
     * Check Perpendicular, checks to see if the play being made is
     * perpendicular to any previous plays
     * 
     * @return
     */
    public static Boolean checkPerpendicular(ArrayList<Double> slopes, double calculatedSlope) {
        // need to implement this
        return false;
    }

    /**
     * This function takes the point read in from the file, and will return the
     * slope
     * 
     * @param sr //starting row
     * @param sc //strarting column
     * @param er //ending row
     * @param ec //ending column
     * @return
     */
    public static double calculateSlope(int sr, int sc, int er, int ec) {
        // need to implement this
        return 0;
    }

    /**
     * AddSlope function takes the arrayList containing the slopes, and
     * adds the new slope to the end
     * 
     * @param slopes
     * @param slope
     */
    public static void addSlope(ArrayList<Double> slopes, Double slope) {
        slopes.add(slope); // adds the slope
    }

}

class Board {
    int[][] gameBoard; // my thoughts we to use and int array with 0 being, initial, 1, "O", 2, "X"
    int size;

    Board(int size) {
        gameBoard = new int[size][size];
        this.size = size;
    }

    public void printGameBoard() {
        // print out the gameBoard
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (gameBoard[i][j] == 1) {
                    System.out.print("O ");
                }
                if (gameBoard[i][j] == 2) {
                    System.out.print("X ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println("");
        }
    }

    public void addMove(int[][] values, int user) {
        // changes the board to be with the current user
        // adds their line, after already being verified
        // unless we want to do verification in this method
    }
}
