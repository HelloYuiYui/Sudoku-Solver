import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Model {
    private final int BOARD_SIZE = 9;
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    private boolean[][] freedomOfMovement = new boolean[BOARD_SIZE][BOARD_SIZE];
    private final int[][] boxesIndex = new int[][]   {{0,0,0,1,1,1,2,2,2},
                                                      {0,0,0,1,1,1,2,2,2},
                                                      {0,0,0,1,1,1,2,2,2},
                                                      {3,3,3,4,4,4,5,5,5},
                                                      {3,3,3,4,4,4,5,5,5},
                                                      {3,3,3,4,4,4,5,5,5},
                                                      {6,6,6,7,7,7,8,8,8},
                                                      {6,6,6,7,7,7,8,8,8},
                                                      {6,6,6,7,7,7,8,8,8}};

    public Model(String fileName) {
        initiate();
        loadGame(fileName);
        editable();
    }

    /**
     * Initiates a new matrix.
     */
    public void initiate(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = j;
            }
        }
    }

    /**
     * Puts a new number to the selected cell in the matrix.
     * @param x coordinate
     * @param y coordinate
     * @param number number to be put into the cell.
     */
    public void put(int x, int y, int number) {
        this.board[x][y] = number;
    }

    /**
     * Solves the cells that only have one possible outcome.
     * @param solutions
     */
    public void solveCertains (LinkedHashMap<int[], ArrayList> solutions) {
        // Places the correct sells.
        Iterator solsIterator = solutions.keySet().iterator();
        while (solsIterator.hasNext()) {
            int[] keys = (int[]) solsIterator.next();
            ArrayList<Integer> sols = solutions.get(keys);
            if (sols.size() == 1) {
                put(keys[0], keys[1], sols.get(0));
            }
        }

        // Loop through the new solutions to recurse.
        LinkedHashMap<int[], ArrayList> newSols = possibleMoves();
        if (hasSingles(newSols))
            solveCertains(newSols);
        else;
            return;
    }

    /**
     * Checks if there are any single-solution cells.
     * @param solutions list of solutions for all editable cells.
     * @return true if there are any single-solution cells, false otherwise.
     */
    public boolean hasSingles (LinkedHashMap<int[], ArrayList> solutions){
        Iterator solsIterator = solutions.keySet().iterator();
        while (solsIterator.hasNext()) {
            int[] keys = (int[]) solsIterator.next();
            ArrayList<Integer> sols = solutions.get(keys);
            if (sols.size() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Uses backtracking to solve the sudoku.
     * @param board the current state of the board.
     * @param row current row number.
     * @param col current column number.
     * @return true if it is solved, false otherwise.
     */
    public boolean backtracking (int[][] board, int row, int col) {

        // Exit the backtracking if reached the end of the board.
        if (row == 8 && col == 9){
            return true;
        }

        // Skip to the next row.
        if (col == 9) {
            row++;
            col = 0;
        }

        // Skip already populated cells.
        if (board[row][col] != 0) {
            return backtracking(board, row, col + 1);
        }

        for (int i = 0; i < 10; i++) {

            if (isSafe(board, row, col, i)) {
                board[row][col] = i;
                if (backtracking(board, row, col + 1)) {
                    return true;
                }
            }

            board[row][col] = 0;

        }
        return false;
    }

    /**
     * Replaces the current matrix with the given matrix.
     * @param newBoard matrix to replace the old matrix.
     */
    public void replace(int[][] newBoard){
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                this.board[i][j] = newBoard[i][j];
            }
        }
    }

    /**
     * Creates an array of boolean arrays to locate all the fields that
     * can be changed by the program, ie. the fields that are not given.
     */
    public void editable () {
        boolean[][] editableFields = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (this.board[i][j] > 0) {
                    editableFields[i][j] = false;
                } else {
                    editableFields[i][j] = true;
                }
                //System.out.print(editableFields[i][j] + " ");
            }
            //System.out.println("");
        }
        freedomOfMovement = editableFields;
    }

    /**
     * Takes the transpose of the given matrix.
     * @param board the matrix to be taken the transpose of.
     * @return transposed matrix.
     */
    public int[][] transpose (int[][] board) {
        int[][] boardTranspose = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardTranspose[i][j] = board[j][i];
            }
        }
        return boardTranspose;
    }

    /**
     * Creates a HashMap of possible numbers that can be put on the fields
     * with the index coded in as the key.
     * @return A HashMap where the key is an array of ints with 2 values,
     * row and column, and the value as an ArrayList of possible numbers.
     */
    public LinkedHashMap<int[], ArrayList> possibleMoves () {
        editable();
        LinkedHashMap<int[], ArrayList> possibleMoves = new LinkedHashMap<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            ArrayList<Integer> horiMoves = horizontalMoves(this.board, i);
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (freedomOfMovement[i][j]) {
                    ArrayList<Integer> moves = new ArrayList<>();
                    ArrayList<Integer> vertiMoves = verticalMoves(this.board, j);
                    for (int k = 1; k < BOARD_SIZE + 1; k++) {
                        if (horiMoves.contains(k) && vertiMoves.contains(k) && boxMoves(board, boxesIndex[i][j]).contains(k)) {
                            moves.add(k);
                        }
                    }
                    possibleMoves.put(new int[]{i, j}, moves);
                } else {
                    possibleMoves.put(new int[]{i, j}, new ArrayList());
                }
            }
        }
        return possibleMoves;
    }

    public boolean isHorizontalValid (int[][] board, int row) {
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        for (int i = 0; i < board[row].length; i++) {
            int currentNum = board[row][i];
            if (numbers.contains(currentNum)){
                numbers.remove(numbers.indexOf(currentNum));
            } else if (currentNum == 0) {
                return false;
            } else {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Integer> horizontalMoves (int[][] board, int row) {
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        for (int i = 0; i < board[row].length; i++) {
            int currentNum = board[row][i];
            if (numbers.contains(currentNum)){
                numbers.remove(numbers.indexOf(currentNum));
            } else if (currentNum == 0) {
                continue;
            } else {
                return new ArrayList<>(Arrays.asList(-1));
            }
        }
        return numbers;
    }

    public boolean isVerticalValid (int[][] board, int row) {
        return isHorizontalValid(transpose(board), row);
    }

    public ArrayList<Integer> verticalMoves (int[][] board, int row) {
        return horizontalMoves(transpose(board), row);
    }

    public int[][] boxes (int[][] board) {
        ArrayList<int[]> separated = new ArrayList<>();
        int[][] boxArray = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < 3; j++) {
                int[] triplet = Arrays.copyOfRange(board[i], j*3, j*3+3);
                separated.add(triplet);
            }
        }

        int countBoxes = 0;
        for (int i = 0; i < 21; i = i + 9) {
            for (int j = 0; j < 3; j++) {
                int a = (i + j);
                int[] box = new int[] {separated.get(a)[0], separated.get(a)[1], separated.get(a)[2],
                                       separated.get(a+3)[0], separated.get(a+3)[1], separated.get(a+3)[2],
                                       separated.get(a+6)[0], separated.get(a+6)[1], separated.get(a+6)[2]};
                boxArray[countBoxes] = box;
                countBoxes++;
            }
        }
        return boxArray;
    }

    public boolean isBoxValid (int[][] board, int boxNo) {
        return isHorizontalValid(boxes(board), boxNo);
    }

    public ArrayList<Integer> boxMoves (int[][] board, int boxNo) {
        return horizontalMoves(boxes(board), boxNo);
    }

    // Check whether it will be legal
    // to assign num to the
    // given row, col
    static boolean isSafe(int[][] grid, int row, int col, int num) {

        // Check if we find the same num
        // in the similar row , we
        // return false
        for (int x = 0; x <= 8; x++)
            if (grid[row][x] == num)
                return false;

        // Check if we find the same num
        // in the similar column ,
        // we return false
        for (int x = 0; x <= 8; x++)
            if (grid[x][col] == num)
                return false;

        // Check if we find the same num
        // in the particular 3*3
        // matrix, we return false
        int startRow = row - row % 3, startCol
                = col - col % 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (grid[i + startRow][j + startCol] == num)
                    return false;

        return true;
    }

    public boolean isValid() {
        boolean isValid = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            isValid = isVerticalValid(this.board, i) && isHorizontalValid(this.board, i) && isBoxValid(this.board, i);
            if (!isValid) {
                return false;
            }
        }
        return isValid;
    }

    public void loadGame (String fileName) {
        try {
            int[][] saveBoard = new int[BOARD_SIZE][BOARD_SIZE];
            FileReader save = new FileReader(fileName);
            Scanner contents = new Scanner(save);
            int i = 0;
            while (contents.hasNextLine()) {
                String row = contents.nextLine();
                for (int j = 0; j < row.length(); j++) {
                    char chr = row.charAt(j);
                    saveBoard[i][j] = Integer.parseInt(String.valueOf(chr));
                }
                i++;
            }
            contents.close();
            save.close();

            replace(saveBoard);
            transpose(board);
        } catch (IOException ignored) { }
    }

    public int[][] getBoard(){
        return this.board;
    }
}
