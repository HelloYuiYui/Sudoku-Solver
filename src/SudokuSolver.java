public class SudokuSolver {

    public SudokuSolver() {

    }

    public static void main(String[] args) {
        Model model = new Model("save3.txt");

        View view = new View();

        Controller control = new Controller(model, view);

        // loop - loops through 51 sudokus in sudokus.txt file
        // solve - solves the saved sudoku in model class
        control.loop();
        System.out.println("\n" + Math.round(control.getTime()));
    }
}
