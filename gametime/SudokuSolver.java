/**
 * @author Saxon McIntosh
 * @version February 27, 2020
 *
 * This program recursively solves sudoku puzzles.
 */

import java.util.*;

public class SudokuSolver {
    public static void main(String args[]) {
	int puzzle[][] = {{0, 0, 0, 0, 4, 0, 0, 0, 9},
			  {0, 5, 7, 0, 2, 1, 3, 4, 0},
			  {0, 0, 1, 0, 0, 0, 7, 0, 0},
			  {0, 0, 0, 0, 5, 8, 0, 7, 0},
			  {0, 2, 0, 7, 0, 0, 8, 0, 0},
			  {0, 0, 0, 0, 1, 0, 0, 0, 4},
			  {0, 7, 5, 0, 3, 0, 0, 9, 0},
			  {8, 0, 0, 0, 0, 9, 1, 0, 5},
			  {0, 0, 2, 0, 0, 0, 0, 0, 0}};

	System.out.println("Puzzle:");
	printer(puzzle);

	// squares is a matrix that represents the different quadrants
	// of the puzzle.
	int squares[][] = new int[9][9];
	for (int i = 0; i < 9; i++) {
	    for (int j = 0; j < 9; j++) {
		squares[i][j] = squarer(i, j);
	    }
	}
	
	System.out.println("Solution:");
	recurser(puzzle, squares);
	printer(puzzle);
    }

    /**
     * Just a helper method to print the nested arrays.
     *
     * @param matrix the matrix to be printed
     */
    public static void printer(int matrix[][]) {
	for (int i = 0; i < 9; i++) {
	    for (int j = 0; j < 9; j++) {
		System.out.print(matrix[i][j] + " ");
	    }
	    System.out.println();
	}
	System.out.println();
    }

    /**
     * A helper function to provide control flow for creating a matrix
     * which demarcates the quadrants.
     *
     * @param row the row where the value is found
     * @param col the column where the value is found
     *
     * @return an integer representing the 'index' of the present value
     */
    public static int squarer(int row, int col) {
	if (row < 3) {
	    if (col < 3) {
		return 0;
	    }
	    if (col >= 3 && col < 6) {
		return 1;
	    }
	    return 2;
	}
	if (row >= 3 && row < 6) {
	    if (col < 3) {
		return 3;
	    }
	    if (col >= 3 && col < 6) {
		return 4;
	    }
	    return 5;
	}
	if (col < 3) {
	    return 6;
	}
	if (col >= 3 && col < 6) {
	    return 7;
	}
	return 8;
    }

    /**
     * isSolved is self explanatory - if the puzzle has made it past
     * isValid without complaint then the if it consists of all non-zero
     * values it is complete.
     *
     * @param puzzle the (potentially) solved puzzle
     *
     * @return whether or not the puzzle is actually solved
     */
    public static boolean isSolved(int puzzle[][]) {
	for (int i = 0; i < 9; i++) {
	    for (int j = 0; j < 9; j++) {
		if (puzzle[i][j] == 0) {
		    return false;
		}
	    }
	}
	return true;
    }

    /**
     * The actual sudoku-puzzle-solving logic.
     *
     * @param puzzle the original puzzle
     * @param squares the quadrant representation
     * @param row the row of the tested value
     * @param col the column of the tested value
     * @param value the value to test for validity
     *
     * @return is the value's placement valid?
     */
    public static boolean isValid(int puzzle[][], int squares[][],
				  int row, int col, int value) {
	// puzzle must be copied into fPuzzle (fauxPuzzle) so its
	// contents are not altered.
	int fPuzzle[][] = new int[9][9];
	for (int i = 0; i < 9; i++) {
	    for (int j = 0; j < 9; j++) {
		// The value is inserted for testing.
		if (i == row && j == col) {
		    fPuzzle[i][j] = value;
		} else {
		    fPuzzle[i][j] = puzzle[i][j];
		}
	    }
	}

	// values will be used to store the numbers found on each line
	// of the puzzle.
	ArrayList<Integer> values = new ArrayList<>();

	// This tests against the horizontal lines.
	for (int i = 0; i < 9; i++) {
	    for (int j = 0; j < 9; j++) {
		if (values.contains(fPuzzle[i][j])) {
		    return false;
		}
		if (fPuzzle[i][j] != 0) {
		    values.add(fPuzzle[i][j]);
		}
	    }
	    values.clear();
	}

	// This tests against the vertical lines.
	for (int i = 0; i < 9; i++) {
	    for (int j = 0; j < 9; j++) {
		if (values.contains(fPuzzle[j][i])) {
		    return false;
		}
		if (fPuzzle[j][i] != 0) {
		    values.add(fPuzzle[j][i]);
		}
	    }
	    values.clear();
	}

	// sValues (squareValues) is required to check all of the
	// numbers in the matrix against the numbers that share the
	// same quadrant.
	ArrayList<ArrayList<Integer>> sValues = new ArrayList<>();
	for (int i = 0; i < 9; i++) {
	    sValues.add(new ArrayList<Integer>());
	}

	for (int i = 0; i < 9; i++) {
	    for (int j = 0; j < 9; j++) {
		if (sValues.get(squares[i][j]).contains(fPuzzle[i][j])) {
		    return false;
		}
		if (fPuzzle[i][j] != 0) {
		    sValues.get(squares[i][j]).add(fPuzzle[i][j]);
		}
	    }
	}
	return true;
    }

    /**
     * This finds the solution to the puzzle through sheer processing
     * power.
     *
     * @param puzzle this context's version of the puzzle
     * @param squares the quadrant representation
     *
     * @return whether or not this context of the recursion has been
     * validated
     */
    public static boolean recurser(int puzzle[][], int squares[][]) {
	// If the puzzle is finished, eject.
	if (isSolved(puzzle)) {
	    return true;
	}
        
	for (int i = 0; i < 9; i++) {
	    for (int j = 0; j < 9; j++) {
	        for (int k = 1; k <= 9; k++) {
		    // The puzzle's original value must be zero at the
		    // coordinates, otherwise the recursion will be
		    // endless.
		    if (puzzle[i][j] == 0 &&
			isValid(puzzle, squares, i, j, k)) {
			puzzle[i][j] = k;
			// If the same function evaluates to true,
			// backtrack up from this context.
			if (recurser(puzzle, squares)) {
			    return true;
			}
			// If the subsequent functions proved to be
			// unsuccessful, return the puzzle's original
			// value.
			puzzle[i][j] = 0;
		    }
		}
		// Eject from this context if none of the possible
		// values were a valid option.
		if (puzzle[i][j] == 0) {
		    return false;
		}
	    }
	}
	return false;
    }
}
