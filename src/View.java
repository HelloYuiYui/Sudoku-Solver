import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class View {

    public View() {

    }

    public static void displayBoardSimple(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.print("\n");
        }
    }

    /**
     * Displays the board.
     * @param board the board to be displayed.
     */
    public void displayBoard(int[][] board){
        StringBuilder sb = new StringBuilder();
        String breakString = "\u001B[34m+-------+-------+-------+\u001B[0m\n";
        String breakLine = "\u001B[34m| \u001B[0m";
        //sb.append(breakString);
        for (int i = 0; i < board.length; i++) {
            if ((i) % 3 == 0) {
                sb.append(breakString);
            }
            //sb.append("|");
            for (int j = 0; j < board[i].length; j++) {
                if (j % 3 == 0) {
                    sb.append(breakLine);
                }
                //System.out.println(j);
                if (board[i][j] == 0) {
                    sb.append(". ");
                } else {
                    sb.append(board[i][j] + " ");
                }
            }
            sb.append(breakLine + "\n");
        }
        sb.append(breakString);
        System.out.println(sb);
    }

    /**
     * Displays the possible solutions hashmap.
     * @param matrix the hashmap with the indices and potential solutions as the values.
     */
    public void displayPossibleMoves (LinkedHashMap<int[], ArrayList> matrix) {
        Iterator keysIterator = matrix.keySet().iterator();
        Iterator vals = matrix.values().iterator();
        while (keysIterator.hasNext()) {
            int[] index = (int[]) keysIterator.next();
            ArrayList<Integer> nums = (ArrayList<Integer>) vals.next();
            if (!nums.isEmpty() && nums.size() == 1) System.out.println(index[0] + " " + index[1] + " " + nums);
        }
    }
}
