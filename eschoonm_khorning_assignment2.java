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

        int emptyCells = boardSize * boardSize; // when game starts, all cells on board are empty
        int p1Score = 0; // counter for Player 1's score
        int p2Score = 0; // counter for Player 1's score

        // keeps track of whether either player has played an invalid perpendicular move
        boolean p1Perp = false;
        boolean p2Perp = false;

        // Player 1 starts- this keeps track of whose turn it is
        int whoseTurn = PLAYERS[0];

        // loop that runs until there are no more moves in the file, or until game board
        // is full
        while (inputMoves.hasNext() && (p1Perp == false || p2Perp == false) && emptyCells != 0) {

            // reads the starting/ending cell coordinates from file
            int sr = inputMoves.nextInt() - 1;
            int sc = inputMoves.nextInt() - 1;
            int er = inputMoves.nextInt() - 1;
            int ec = inputMoves.nextInt() - 1;

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
                } else {
                    // adds the previous slope to the arraylist of slopes
                    addSlope(slopes, currentSlope);
                    // updates the queues, based off of q values, to check start and end values
                    updateQueues(sc, sr, er, ec, startCells, endCells, K);
                    int dr = changeInRow(sr, er);
                    int dc = changeInColumn(sc, ec);
                    ArrayList<Integer[]> valuesToChange = new ArrayList<>();
                    if (dr > dc) {
                        // passing argument as 0 to plot(x,y)
                        valuesToChange = brenshamsAlgorithm(sr, sc, er, ec, dr, dc, 0);
                    }
                    // if slope is greater than or equal to 1
                    else {
                        // passing argument as 1 to plot (y,x)
                        valuesToChange = brenshamsAlgorithm(sc, sr, ec, er, dr, dc, 1);
                    }
                    board.addMove(valuesToChange, whoseTurn);
                }
            }

            whoseTurn = determinePlayerTurn(whoseTurn, PLAYERS);
            emptyCells = countValueOnBoard(board, 0);

            // display current board and results
            board.printGameBoard();
            p1Score = countValueOnBoard(board, PLAYERS[0]);
            p2Score = countValueOnBoard(board, PLAYERS[1]);
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
     * @Author eschoonm
     *         printBoard, prints out the board, with the values inside the Board
     *         object
     * 
     * @param board
     */
    public static void printBoard(Board board) {
        board.printGameBoard();// calls the method in Board
    }

    /**
     * @author KHorning
     *         This function takes the coordinates read in from the file, and
     *         calculates
     *         the slope of the resulting line
     * 
     *         eschoonm
     *         added the checking for vertical slope, to return NaN to the function
     * 
     * @param sr //starting row
     * @param sc //starting column
     * @param er //ending row
     * @param ec //ending column
     * @return
     */
    public static double calculateSlope(int sr, int sc, int er, int ec) {

        boolean isVertical = checkForVertical(sr, sc, er, ec);// checks for vertical slope
        double slope; // initilizes values
        if (!isVertical) { // not vertical slope
            slope = (ec - sc) / (er - sr);// calculate slope
        } else {
            slope = Double.NaN;// else, slope is gonna be set to NaN
        }
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
     *         eschoonm, added vertical line compatability
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
            double perpLine = 0;
            if (lineSlope != Double.NaN && lineSlope != 0) {// if the lines slope is not Nan and not 0
                perpLine = -1 / lineSlope;
            } else if (lineSlope == 0) {// if it is zero
                perpLine = Double.NaN;// slope is vertical
            }

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

        // nested for loops to iterate through entire game board
        for (int i = 0; i < gameBoard.getSize(); i++) {
            for (int j = 0; j < gameBoard.getSize(); j++) {

                int currentCell = gameBoard.getValueAtCell(i, j);

                if (currentCell == 1) {
                    p1Score++;
                } else if (currentCell == 2) {
                    p2Score++;
                }
            }
        }

        System.out.println("Player 1 Score: " + p1Score);
        System.out.println("Player 2 Score: " + p2Score);

    }// displayScore

    /**
     * @author KHorning
     *         Iterates through the game board and counts whatever value is sent in
     *         It can count each player's score, as well as the number of remaining
     *         empty cells
     * 
     * @param gameBoard //the game board with all current moves
     */
    public static int countValueOnBoard(Board gameBoard, int countedValue) {

        int cells = 0; // counter for specified cells on game board

        // iterates through game board and increments cell count when a matching cell is
        // found
        for (int i = 0; i < gameBoard.getSize(); i++) {
            for (int j = 0; j < gameBoard.getSize(); j++) {

                int currentCell = gameBoard.getValueAtCell(i, j);

                if (currentCell == countedValue) {
                    cells++;
                }
            }
        }

        return cells;

    }// countRemainingCells

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

        while (startCells.size() > 2 * k) {// updates the queue to only have the last k values
            startCells.remove();
            startCells.remove();
        }
        while (endCells.size() > 2 * k) {// updates the queue to only have the last k values
            endCells.remove();
            endCells.remove();
        }
    }// updateQueues

    /**
     * This function checks for a vertical slope and returns a boolean
     * 
     * @param sr
     * @param sc
     * @param er
     * @param ec
     * @return
     */
    public static Boolean checkForVertical(int sr, int sc, int er, int ec) {
        if (changeInRow(sr, er) == 0) {
            return true; // checks for vertical slope
        }
        return false;
    }

    public static int changeInRow(int sr, int er) {
        return Math.abs((er - sr));
    }// changeInRow

    public static int changeInColumn(int sc, int ec) {
        return Math.abs((ec - sc));
    }// changeInColumn

    public static ArrayList<Integer[]> brenshamsAlgorithm(int sr, int sc, int er,
            int ec, int changeInRow, int changeInColumn, int decide) {
        ArrayList<Integer[]> pointsToHighlight = new ArrayList<>();
        int pk = 2 * changeInColumn - changeInRow;
        for (int i = 0; i <= changeInRow; i++) {
            Integer[] point = { sr, sc };// creates a integer array with the values
            pointsToHighlight.add(point);// adds that to the arrayList
            // checking either to decrement or increment the
            // value if we have to plot from (0,100) to
            // (100,0)
            if (sr < er) // if the starting row is less
                sr++;// increment that value
            else
                sr--;// else, decrement the value
            if (pk < 0) {
                // decision value will decide to plot
                // either x1 or y1 in x's position
                if (decide == 0) {
                    pk = pk + 2 * changeInColumn;
                } else
                    pk = pk + 2 * changeInColumn;
            } else {
                if (sc < ec)
                    sc++;// increment the column value
                else
                    sc--;// decrement the column value
                pk = pk + 2 * changeInColumn - 2 * changeInRow;// value
            }
        }

        return pointsToHighlight;
    }// brenshamsAlgorithm

}// assignment2

class Board {
    private int[][] gameBoard; // my thoughts we to use and int array with 0 being initial, 1, "O", 2, "X"
    private int size;

    Board(int size) {
        this.size = size;
        gameBoard = new int[size][size];
    }

    /**
     * @author eschoonm
     *         printGameBoard, prints out the gameBoard using "O", "X", and "-"
     */
    public void printGameBoard() {
        System.out.println("GameBoard: ");
        // print out the gameBoard
        for (int i = 0; i < size; i++) { // for each row
            for (int j = 0; j < size; j++) { // for every column
                if (gameBoard[i][j] == 1) { // if the value is 1, print out "O "
                    System.out.print("O ");
                }
                if (gameBoard[i][j] == 2) {// if the value is 2, print out "X "
                    System.out.print("X ");
                } else {
                    System.out.print("- "); // otherwise -, means no value has been assigned
                }
            }
            System.out.println("");// new line indicating a new row has started
        }
    }// printGameBoard

    /**
     * @author eschoonm
     *         This function will take the integer array, containing the location of
     *         values
     *         to be
     *         highlighted, and change all the values in that array to be the value
     *         of the
     *         user making the
     *         move. At this point, validation has already been done.
     * 
     *         if user = 1, "O", if user = 2, "X"
     * 
     * @param valuesToChange
     * @param user
     */
    public void addMove(ArrayList<Integer[]> valuesToChange, int user) {
        // changes the board to be with the current user
        // adds their line, after already being verified

        // valuesToChange[10][2] 10 pairs of boxes to highligh
        for (int i = 0; i < valuesToChange.size(); i++) {
            int row = valuesToChange.get(i)[0]; // reads in the first value as a row
            int column = valuesToChange.get(i)[1];// reads in the second value as the column

            gameBoard[row][column] = user; // changes the gameboard to be the current user
        }

    }// addMove

    // returns the value at given indices
    public int getValueAtCell(int row, int column) {
        return gameBoard[row][column];
    }

    public int getSize() {
        return size;
    }
}
