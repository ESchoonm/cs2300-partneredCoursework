import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.io.FileWriter;

public class eschoonm_khorning_assignment2 {
    public static void main(String[] args) throws IOException {

        // constants for file names
        final String FILE_P2_1 = "p2-1.txt";
        final String FILE_P2_2 = "p2-2.txt";
        final String FILE_P2_3 = "p2-3.txt";
        final String FILE_P2_4 = "p2-4.txt";
        final String FILE_P2_5 = "p2-5.txt";

        Queue<Double> slopes = new LinkedList<Double>(); // arraylist for the slopes previously calculated
        ArrayList<Integer[]> startCells = new ArrayList<>();// Queue containing values to check double starting values
        ArrayList<Integer[]> endCells = new ArrayList<>();

        // output for the result
        System.out.println("Please enter the full pathname, or just the filename, and it will be created in this file");
        Scanner scanner = new Scanner(System.in);
        String destinationFile = scanner.nextLine().trim();
        File destFile = new File(destinationFile);
        scanner.close();

        // create and open the test file for reading
        File moves = new File(FILE_P2_5);
        Scanner inputMoves = new Scanner(moves);

        int boardSize = inputMoves.nextInt(); // gives the NxN board dimensions
        int K = inputMoves.nextInt(); // past # of turns we are comparing start/end cells
        Board board = new Board(boardSize); // creates the game board with given dimensions

        // create player1 & player 2; player 1 has the first turn
        Player player1 = new Player(1, false);
        Player player2 = new Player(2, true);

        boolean isGameOver = false; // flag that changes when one or both end conditions are met

        // loop that runs until there are no more moves in the file, or until game board
        // is full
        while (inputMoves.hasNext() && !isGameOver) {

            // all gameplay logic is located inside controlBoard method
            isGameOver = controlBoard(inputMoves, player1, player2, K, board, startCells, endCells, slopes);

        }

        // compare player scores and display winner
        int winner = determineWinner(player1.getScore(), player2.getScore());
        saveToFile(destFile, board, winner);

        inputMoves.close();

    }// main

    /**
     * @author KHorning
     *         This method contains all the logic of a player drawing a line on
     *         their turn.
     * 
     * @param moves      //Scanner that reads from the file
     * @param player1    //Player object representing player 1
     * @param player2    //Player object representing player 2
     * @param K          //the number of previous turns we have to check against
     * @param gameBoard  //the board we are playing the game on
     * @param startCells //Integer array of starting cells that have been played in
     * @param endCells   //Integer array of ending cells that have been played in
     * @param slopes     //Queue that holds the slopes of the previous lines that
     *                   have been played
     * @return isGameOver //flag that indicates if gameplay should continue
     */
    public static boolean controlBoard(Scanner moves, Player player1, Player player2, int K, Board gameBoard,
            ArrayList<Integer[]> startCells, ArrayList<Integer[]> endCells, Queue<Double> slopes) {

        // Player 1 starts- this keeps track of whose turn it is
        int whoseTurn = determinePlayerTurn(player1, player2);

        int emptyCells = gameBoard.getSize() * gameBoard.getSize(); // when game starts, all cells on board are empty

        // reads the starting/ending cell coordinates from file
        int sr = moves.nextInt() - 1;
        int sc = moves.nextInt() - 1;
        int er = moves.nextInt() - 1;
        int ec = moves.nextInt() - 1;

        boolean validCell = isValidCell(sr, sc, er, ec, K, startCells, endCells);

        if (validCell) {

            double currentSlope = calculateSlope(sr, sc, er, ec);
            boolean perp = isPerpendicular(currentSlope, slopes);

            // records if a player attempted a perpendicular line
            // this could be a little method too
            if (perp) {
                System.out.println("SLOPE IS PERPENDICULAR, NO CHANGE______");
                if (player1.isTurn()) {
                    player1.attemptedPerpendicular();
                } else {
                    player2.attemptedPerpendicular();
                }

            } else {
                // adds the previous slope to the arraylist of slopes
                addSlope(slopes, currentSlope, K);
                // updates the queues, based off of q values, to check start and end values
                updateListswithValidPoint(sr, sc, er, ec, startCells, endCells);
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
                    valuesToChange = brenshamsAlgorithm(sc, sr, ec, er, dc, dr, 1);
                }
                gameBoard.addMove(valuesToChange, whoseTurn);
            }
        }

        // whoseTurn = determinePlayerTurn(player1, player2);
        emptyCells = countValueOnBoard(gameBoard, 0);

        // display current board
        gameBoard.printGameBoard();

        // calculate current scores and update them
        int p1Score = countValueOnBoard(gameBoard, player1.getPlayerValue());
        int p2Score = countValueOnBoard(gameBoard, player2.getPlayerValue());
        player1.updateScore(p1Score);
        player2.updateScore(p2Score);

        System.out.println();

        // check to see if end conditions are met
        boolean isGameOver = gameOver(emptyCells, player1.hasPlayedPerpendicular(), player2.hasPlayedPerpendicular());

        return isGameOver;

    }// controlBoard

    /**
     * @author KHorning
     *         This method determines whose turn it currently is and updates each
     *         Player's variables
     *         so the next player gets their turn. It returns the value that
     *         represents whichever player
     *         now gets their turn.
     * 
     * @param player1 //Player object representing our first player
     * @param player2 //Player object representing our second player
     * @return playerValue //value in board array that represents current player( 1
     *         for player 1, 2 for player 2)
     */
    public static int determinePlayerTurn(Player player1, Player player2) {

        int playerValue = 0; // holds the integer value that represents the current player

        // if player1 just had their turn, set player1 turn to false and player2 turn to
        // true
        if (player1.isTurn()) {
            player1.myTurn(false);
            player2.myTurn(true);

            playerValue = player2.getPlayerValue();
        }

        // if player2 just had their turn, set player2 turn to false and player1 turn to
        // true
        else {
            player2.myTurn(false);
            player1.myTurn(true);

            playerValue = player1.getPlayerValue();

        }
        return playerValue;
    }// determinePlayerTurn

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
    public static boolean isValidCell(int sr, int sc, int er, int ec, int K, ArrayList<Integer[]> startCells,
            ArrayList<Integer[]> endCells) {
        boolean valid = true;
        // sr,sc is a point that no other starting value can have. Store in an queue,
        // front[sr1, sc1, sr2, sc2, sr3, sc3] back
        for (int i = 0; i < startCells.size(); i++) {
            if (startCells.get(i)[0] == sr && startCells.get(i)[1] == sc) {
                System.out.println("REPEATED START VALUE_______________");
                return false;
            }
        }
        for (int i = 0; i < endCells.size(); i++) {
            if (endCells.get(i)[0] == sr && endCells.get(i)[1] == sc) {
                System.out.println("REPEATED END VALUE_______________");
                return false;
            }
        }

        return valid;
    }// isValidCell

    /**
     * @author KHorning
     *         This method checks the end conditions for game over and returns a
     *         boolean to indicate
     *         if the game continues or not. If there are no empty cells remaining,
     *         or if both players
     *         have attempted a perpendicular line, the game ends.
     * 
     * @param numEmptyCells   //integer number of empty cells remaining on the board
     * @param p1Perpendicular //boolean that indicates if player1 has attempted a
     *                        perpendicular line
     * @param p2Perpendicular //boolean that indicates if player2 has attempted a
     *                        perpendicular line
     * @return isGameOver //indicates if end conditions have been met and game
     *         should end
     */
    public static boolean gameOver(int numEmptyCells, boolean p1Perpendicular, boolean p2Perpendicular) {

        boolean isGameOver = false;

        // if either of the end conditions are met, set isGameOver to true
        if (numEmptyCells == 0 || (p1Perpendicular && p2Perpendicular)) {
            isGameOver = true;
        }

        return isGameOver;
    }// gameOver

    /**
     * @author KHorning
     *         This function takes the line slope of the player's current move and
     *         checks to see if
     *         it is perpendicular to any other lines on the board
     * 
     *         eschoonm, added vertical line compatability
     * 
     * @param lineSlope     //slope of the line of the player's current move
     * @param slopesToCheck //Queue containing the slopes of all lines played so far
     * @return
     */
    public static boolean isPerpendicular(double lineSlope, Queue<Double> slopesToCheck) {

        boolean isPerp = false;

        // only does comparison if ArrayList is not empty; otherwise, flag is
        // automatically false
        if (slopesToCheck.size() != 0) {

            // perpendicular lines have slopes that are the negative reciprocals of each
            // other
            // so we calculate the negative reciprocal of the player's line
            double perpLine = 0;
            if (lineSlope != Double.NaN && lineSlope != 0) {// if the lines slope is not Nan and not 0
                perpLine = -1 / lineSlope;
            } else if (lineSlope == 0) {// if it is zero
                perpLine = Double.NaN;// slope is vertical
            }

            // checks each slope in the queue against the perpendicular slope
            // sets flag to true if a match is found
            Iterator<Double> slopeIterator = slopesToCheck.iterator();
            while (slopeIterator.hasNext()) {
                if (perpLine == slopeIterator.next()) {
                    isPerp = true;
                }
            }
        }

        return isPerp;
    }// isPerpendicular

    /**
     * @author eschoonm
     *         AddSlope function takes the Queue containing the slopes, and
     *         adds the new slope to the end
     * 
     * @param slopes
     * @param slope
     */
    public static void addSlope(Queue<Double> slopes, Double slope, int k) {
        slopes.add(slope);

        while (slopes.size() > k) {
            slopes.poll();
        }
    }// addSlope

    /**
     * @author KHorning
     *         Compares the players' scores and determines who the winner is
     *         Also accounts for a tie
     * 
     * @param score1 //player1's final score
     * @param score2 //player2's final score
     */
    public static int determineWinner(int score1, int score2) {
        int winner = 0;
        System.out.println("Player 1 Score: " + score1);
        System.out.println("Player 2 Score: " + score2);

        if (score1 > score2) {
            System.out.println("Player 1 Wins");
            winner = 1;

        } else if (score1 < score2) {
            System.out.println("Player 2 Wins");
            winner = 2;

        } else {
            System.out.println("It's a tie");

        }
        return winner;

    }// determineWinner

    /**
     * @author KHorning
     *         Iterates through the game board and counts whatever value is sent in
     *         It can count each player's score, as well as the number of remaining
     *         empty cells
     * 
     * @param gameBoard    //the game board with all current moves
     * @param countedValue //which value on the board should be counted- player1,
     *                     player2 or empty cells
     * @return cells //counter of specified value on board
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

    }// countValueOnBoard

    /**
     * @author eschoonm
     * 
     *         This function adds the two points to arraylist start cells and
     *         arraylist endcells
     * 
     * @param sr         //starting row
     * @param sc         //starting column
     * @param er         //ending row
     * @param ec         //ending column
     * @param startCells //ArrayList containing previous start cells
     * @param endCells   //ArrayList containing previuos end cells
     */
    public static void updateListswithValidPoint(int sr, int sc, int er, int ec, ArrayList<Integer[]> startCells,
            ArrayList<Integer[]> endCells) {
        Integer[] start = { sr, sc };// creates an array with the values
        Integer[] end = { er, ec };// createts an array with the values

        startCells.add(start);// adds them to the start arrayList
        endCells.add(end);// adds them to the end arraylist

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
            if (decide == 0) {
                Integer[] point = { sr, sc };// creates a integer array with the values
                pointsToHighlight.add(point);// adds that to the arrayList
            } else {
                Integer[] point = { sc, sr };// creates a integer array with the values
                pointsToHighlight.add(point);// adds that to the arrayList
            }
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

    public static void saveToFile(File file, Board board, int winner) throws IOException {
        FileWriter fileWriter = new FileWriter(file);

        fileWriter.append("GameBoard: \n");
        // print out the gameBoard
        for (int i = 0; i < board.getSize(); i++) { // for each row
            for (int j = 0; j < board.getSize(); j++) { // for every column
                if (board.getValueAtCell(i, j) == 1) { // if the value is 1, print out "O "
                    fileWriter.append("X ");
                } else if (board.getValueAtCell(i, j) == 2) {// if the value is 2, print out "X "
                    fileWriter.append("O ");
                } else {
                    fileWriter.append("- "); // otherwise -, means no value has been assigned
                }
            }
            fileWriter.append("\n");// new line indicating a new row has started
        }
        // prints out the winner
        if (winner == 1) {
            fileWriter.append("Player 1 Wins\n");
        } else if (winner == 2) {
            fileWriter.append("Player 2 Wins\n");
        } else {
            fileWriter.append("It's a tie\n");

        }
        fileWriter.close();

    }// savetofile
}// assignment2

class Board {
    private int[][] gameBoard; // 2D array that represents the game board
    private int size; // represents the dimensions of the board (size x size)

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
                    System.out.print("X ");
                } else if (gameBoard[i][j] == 2) {// if the value is 2, print out "X "
                    System.out.print("O ");
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
}// Board

/**
 * @author KHorning
 *         Represents a player object. Each player has an integer number that
 *         represents their value on the
 *         board. We keep track of the player's current score, if it is their
 *         turn, and if they have played
 *         a perpendicular move.
 */
class Player {

    private int valueOnBoard; // integer value that represents the player on the game board
    private int score; // player's current score in game
    private boolean turn; // indicates if it is the player's turn or not
    private boolean playedPerpendicular; // indicates if the player has tried a perpendicular line

    // constructor sets score to 0 and playedPerpendicular to false since gameplay
    // hasn't started yet
    // turn is set when Player is created; P1 goes first
    public Player(int valueOnBoard, boolean turn) {
        this.valueOnBoard = valueOnBoard;
        score = 0;
        this.turn = turn;
        playedPerpendicular = false;
    }

    // getters
    public int getPlayerValue() {
        return valueOnBoard;
    }

    public int getScore() {
        return score;
    }

    public boolean isTurn() {
        return turn;
    }

    public boolean hasPlayedPerpendicular() {
        return playedPerpendicular;
    }

    // receives the current score calculated by countValueOnBoard method and updates
    // player's score
    public void updateScore(int currentScore) {
        score = currentScore;
    }

    // sets the perpendicular flag to true if the player attempts to play a
    // perpendicular line
    public void attemptedPerpendicular() {
        playedPerpendicular = true;
    }

    // sets the player's turn to true or false
    public void myTurn(boolean yesOrNo) {
        turn = yesOrNo;
    }

}// Player
