import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.Queue;

public class eschoonm_khorning_assignment2 {
    public static void main(String[] args) throws IOException {

        final int[] PLAYERS = { 1, 2 }; // constant containing the players' corresponding numbers

        ArrayList<Double> slopes = new ArrayList<>(); // arraylist for the slopes previously calculated
        Queue<Integer> startCells = new LinkedList<Integer>();// Queue containing values to check double starting values
        Queue<Integer> endCells = new LinkedList<Integer>();
        // create and open the test file for reading
        String fileName = "p2-1.txt";
        File moves = new File(fileName);
        Scanner inputMoves = new Scanner(moves);

        int boardSize = inputMoves.nextInt(); // gives the NxN board dimensions
        int K = inputMoves.nextInt(); // past # of turns we are comparing start/end cells
        Board board = new Board(boardSize); // creates the game board with given dimensions

        // keeps track of whether either player has played an invalid perpendicular move
        boolean p1Perp = false;
        boolean p2Perp = false;

        // Player 1 starts- this keeps track of whose turn it is
        int whoseTurn = PLAYERS[0];

        // loop that runs until there are no more moves in the file
        while (inputMoves.hasNext() && (p1Perp == false || p2Perp == false)) {

            // reads the starting/ending cell coordinates from file
            int sr = inputMoves.nextInt();
            int sc = inputMoves.nextInt();
            int er = inputMoves.nextInt();
            int ec = inputMoves.nextInt();

            // test queues, need to write function that includes k however
            // startCells.offer(2);
            // startCells.offer(1);
            // endCells.offer(4);
            // endCells.offer(5);

            boolean validCell = isValidCell(sr, sc, er, ec, K, startCells, endCells);

            if (validCell) {
                double currentSlope = calculateSlope(sr, sc, er, ec);
                boolean perp = isPerpendicular(currentSlope, slopes);

                // records if a player attempted a perpendicular line
                // this could be a little method too
                if (perp) {
                    if (whoseTurn == PLAYERS[0]) {
                        p1Perp = perp;
                    } else {
                        p2Perp = perp;
                    }
                }
            }
            updateQueues(sc, sr, er, ec, startCells, endCells, K);

            whoseTurn = determinePlayerTurn(whoseTurn, PLAYERS);

            // display current board and results
            board.printGameBoard();
            displayScore(board);
            System.out.println();
        }

        inputMoves.close();
    }

    /**
     * @author KHorning
     *         determines which player's turn it is on the next round
     * 
     * @param currentPlayer //this is the player who just had their turn
     * @param players       //array of our players' numbers
     */
    public static int determinePlayerTurn(int currentPlayer, int[] players) {

        int nextTurn = 0;

        if (currentPlayer == players[0]) {
            nextTurn = players[1];
        } else {
            nextTurn = players[0];
        }
        return nextTurn;
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
     * @author KHorning
     *         This function takes the coordinates read in from the file, and
     *         calculates
     *         the slope of the resulting line
     * 
     * @param sr //starting row
     * @param sc //starting column
     * @param er //ending row
     * @param ec //ending column
     * @return
     */
    public static double calculateSlope(int sr, int sc, int er, int ec) {

        // TODO, double check we arent dividing by 0.
        if (er == sr) {// meaning divide by zero, where slope is undefined
            // What do we want to do here
        }
        double slope = (ec - sc) / (er - sr);
        return slope;
    }

    /**
     * @author eschoonm
     *         This function takes the line read in from the file and checks to see
     *         if
     *         the move is valid by checking it against the starting/ending points
     *         of the
     *         last K turns
     * 
     * @param sr        //starting row
     * @param sc        //strarting column
     * @param er        //ending row
     * @param ec        //ending column
     * @param K         //number of past turns
     * @param cellQueue //queue containing list of values to check starting values
     * @return
     */
    public static boolean isValidCell(int sr, int sc, int er, int ec, int K, Queue<Integer> startCells,
            Queue<Integer> endCells) {
        boolean valid = true;

        // TODO cant figure out this method yet.
        // Gonna need a queue that holds k values, maximum values in the queue is the k
        // value
        // add value to queue if valid// gonna need another method for this. I don't
        // want to do it inside this
        // one

        // sr,sc is a point that no other starting value can have. Store in an queue,
        // front[sr1, sc1, sr2, sc2, sr3, sc3] back
        Iterator<Integer> startIterator = startCells.iterator();// creates an iterator over the startcells queue
        Iterator<Integer> endIterator = endCells.iterator();// creates an iterator over the endcells queue

        while (startIterator.hasNext() && valid) {// while values to iterate over
            Integer var1 = (Integer) startIterator.next(); // read in the first value
            Integer var2 = (Integer) startIterator.next();// read in the second value

            if (var1 == sr && var2 == sc) {// if those two values match
                System.out.print("REPEATED STARTING VALUE_______\n");
                valid = false;
            }
        }
        while (endIterator.hasNext() && valid) {// while values to iterate over
            Integer var1 = (Integer) endIterator.next();// read in the first value from queue
            Integer var2 = (Integer) endIterator.next();// read in the second value from queue

            if (var1 == er && var2 == ec) {// check to see if the values match the ending values
                System.out.print("REPEATED ENDING VALUE___________\n");
                valid = false;// if yes, set to false, double value
            }
        }

        return valid;
    }

    /**
     * @author KHorning
     *         This function takes the line slope of the player's current move and
     *         checks to see if
     *         it is perpendicular to any other lines on the board
     * 
     * @param lineSlope //slope of the line of the player's current move
     * @param allLines  //ArrayList containing the slopes of all lines played so far
     * @return
     */
    public static boolean isPerpendicular(double lineSlope, ArrayList<Double> allLines) {

        boolean isPerp = false;

        // only does comparison if ArrayList is not empty; otherwise, flag is
        // automatically false
        if (allLines.size() != 0) {

            // perpendicular lines have slopes that are the negative reciprocals of each
            // other
            // so we calculate the negative reciprocal of the player's line
            double perpLine = -1 / lineSlope;

            // checks each slope in the array against the perpendicular slope
            // sets flag to true if a match is found
            for (int i = 0; i < allLines.size(); i++) {
                if (perpLine == allLines.get(i)) {
                    isPerp = true;
                }
            }
        }

        return isPerp;
    }// isPerpendicular

    /**
     * @author eschoonm
     *         AddSlope function takes the arrayList containing the slopes, and
     *         adds the new slope to the end
     * 
     * @param slopes
     * @param slope
     */
    public static void addSlope(ArrayList<Double> slopes, Double slope) {
        slopes.add(slope); // adds the slope
    }// addSlope

    /**
     * @author KHorning
     *         Iterates through the game board and counts the scores of both players
     * 
     * @param gameBoard //the game board with all current moves
     */
    public static void displayScore(Board gameBoard) {

        // creates counters for player scores and empty cellls
        int p1Score = 0;
        int p2Score = 0;
        int openCells = 0;

        // nested for loops to iterate through entire game board
        for (int i = 0; i < gameBoard.getSize(); i++) {
            for (int j = 0; j < gameBoard.getSize(); j++) {

                int currentCell = gameBoard.getValueAtCell(i, j);

                if (currentCell == 0) {
                    openCells++;
                } else if (currentCell == 1) {
                    p1Score++;
                } else if (currentCell == 2) {
                    p2Score++;
                }
            }
        }

        System.out.println("Player 1 Score: " + p1Score);
        System.out.println("Player 2 Score: " + p2Score);
        System.out.println("Empty Cells Remaining: " + openCells);

    }// displayScore

    /**
     * @author eschoonm
     * 
     *         This function readjusts the two main queues that contain a list of
     *         previous start and end
     *         cells, and adjusts it with the k value. Includes the update of new
     *         valid values, and removal of
     *         old values
     * 
     * @param sr         //starting row
     * @param sc         //starting column
     * @param er         //ending row
     * @param ec         //ending column
     * @param startCells //Queue containing previous start cells
     * @param endCells   //queue containing previuos end cells
     * @param k          //number of previous cells saved to verify
     */
    public static void updateQueues(int sr, int sc, int er, int ec, Queue<Integer> startCells, Queue<Integer> endCells,
            int k) {
        startCells.offer(sr);// adds values to the queue, as the value has been verified
        startCells.offer(sc);
        endCells.offer(er);
        endCells.offer(ec);

        while (startCells.size() > k) {// updates the queue to only have the last k values
            startCells.remove();
            startCells.remove();
        }
        while (endCells.size() > k) {// updates the queue to only have the last k values
            endCells.remove();
            endCells.remove();
        }
    }// updateQueues

}// assignment2

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

    // returns the value at given indices
    public int getValueAtCell(int row, int column) {
        return gameBoard[row][column];
    }

    public int getSize() {
        return size;
    }
}
