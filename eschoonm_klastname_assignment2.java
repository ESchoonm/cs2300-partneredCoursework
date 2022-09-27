import java.util.ArrayList;

public class eschoonm_klastname_assignment2 {
    public static void main(String[] args) {
        Board board = new Board(5);
        board.printGameBoard();
        // printBoard(board);
    }

    public static void printBoard(Board board) {
        board.printGameBoard();
    }

}

class Board {
    int[][] gameBoard; // my thoughts we to use and int array with 0 being, initial, 1, "O", 2, "X"
    int size;
    ArrayList<Double> slopes; // we could always move it outside the board object

    Board(int size) {
        gameBoard = new int[size][size];
        this.size = size;
        slopes = new ArrayList<>();
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

    public ArrayList<Double> getSlopes() {
        return slopes;
    }

    public void addSlope(Double slope) {
        slopes.add(slope);
    }

    public void addMove(int[][] values, int user) {
        // changes the board to be with the current user
        // adds their line, after already being verified
        // unless we want to do verification in this method
    }
}