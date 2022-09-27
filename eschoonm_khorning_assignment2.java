import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;


public class eschoonm_khorning_assignment2 {
    public static void main(String[] args) throws IOException {

        ArrayList<Double> slopes; // arraylist for the slopes previously calculated
        
        //create and open the test file for reading 
        String fileName = "p2-1.txt";
        File moves = new File(fileName);
        Scanner inputMoves = new Scanner(moves);
        
        int boardSize = inputMoves.nextInt(); //gives the NxN board dimensions
        int K = inputMoves.nextInt(); // past # of turns we are comparing start/end cells
        
        Board board = new Board(boardSize);
        
        while(inputMoves.hasNext()) {
        	int sr = inputMoves.nextInt();
        	int sc = inputMoves.nextInt();
        	int er = inputMoves.nextInt();
        	int ec = inputMoves.nextInt();
        	
        	
        	
        	
        	board.printGameBoard();
        }
        
      
       
    }

    /**
     * printBoard, prints out the board, with the values inside the Board object
     * 
     * @param board
     */
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
   private int[][] gameBoard; // my thoughts we to use and int array with 0 being, initial, 1, "O", 2, "X"
   private int size;

    Board(int size) {
    	this.size = size;
        gameBoard = new int[size][size];
    }

    /**
     * printGameBoard, prints out the gameBoard using "O", "X", and "-"
     */
    public void printGameBoard() {
        System.out.println("GameBoard: ");
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
