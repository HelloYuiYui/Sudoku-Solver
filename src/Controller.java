import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Controller {
    private Model model;
    private View view;
    double time = 0.0;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void loop() {
        try {
            int counter = 0;
            int solved = 0;

            FileReader save = new FileReader("sudokus.txt");
            Scanner contents = new Scanner(save);
            while (contents.hasNextLine()) {
                if (contents.nextLine().matches("Grid")) {
                    contents.nextLine();
                } else {
                    String currentLine = contents.next();
                    for (int i = 0; i < 9; i++) {
                        model.put(counter, i, Integer.parseInt(Character.toString(currentLine.charAt(i))));
                    }
                    counter++;
                }
                if (counter == 9){
                    System.out.print(solved + 1 + ". ");
                    boolean attempt = solve();
                    if (attempt) {solved++;}
                    counter = 0;
                    if(contents.hasNextLine()) {
                        contents.nextLine();
                    }
                }
            }
            System.out.println(solved);
        } catch (FileNotFoundException err) {}
    }

    /**
     * Solves the given sudoku in the model.
     * @return true if the sudoku is solved, false if it isn't.
     */
    public boolean solve() {

        LinkedHashMap<int[], ArrayList> moves = model.possibleMoves();

        long startTime = System.nanoTime();
        String strat = "BT";
        boolean usedCertainSolves = false;
        boolean usedBT = false;
        switch (strat) {
            case ("BT"):
                if (model.hasSingles(moves)) {
                    //view.displayPossibleMoves(moves);
                    model.solveCertains(moves);
                    usedCertainSolves = true;
                }

                if (model.isValid()) {break;}

                if (model.backtracking(model.getBoard(), 0, 0)) {
                    usedBT = true;
                } else {
                    view.displayBoard(model.getBoard());
                    System.out.println("No possible solutions.");
                    return false;
                }
            case ("OG"):
                model.solveCertains(moves);
                usedCertainSolves = true;
            }
        long endTime = System.nanoTime();
        double timeTaken = Math.round((endTime-startTime)/100000.0)/10.0;
        time = time + timeTaken;

        boolean isValid = model.isValid();

        if (isValid) System.out.print("\u001B[32m" + timeTaken + " ms\u001B[0m ");
        else System.out.print("\u001B[31m" + timeTaken + " ms\u001B[0m ");

        if (usedCertainSolves) System.out.print(" OG: ✓");
        else System.out.print(" OG: ✖");

        if (usedBT) System.out.print(" BT: ✓");
        else System.out.print(" BT: ✖");

        System.out.println("");
        //view.displayBoard(model.getBoard());
        return model.isValid();
    }

    public double getTime() {
        return time;
    }
}
