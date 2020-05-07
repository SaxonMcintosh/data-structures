/**
 * @author Saxon McIntosh
 * @version February 27, 2020
 *
 * This program recursively solves the queens problem, wherein 8 queens
 * are placed on a chessboard without being able to take each other in
 * one turn.
 */

import java.util.*;

public class Queens {
    public static void main(String args[]) {
	int board[][] = new int[8][8];
	for (int i = 0; i < 8; i++) {
	    for (int j = 0; j < 8; j++) {
		board[i][j] = 0;
	    }
	}

	recurser(board);
	System.out.println("Solution:");
	for (int i = 0; i < 8; i++) {
	    for (int j = 0; j < 8; j++) {
		System.out.print(board[i][j] + " ");
	    }
	    System.out.println();
	}
    }

    /**
     * Vets whether or not the problem has been solved.
     *
     * @param board the game's matrix representation
     *
     * @return a boolean for the solution's truth value
     */
    public static boolean isSolved(int board[][]) {
	// Since the internal logic for the rest of the program is
	// consistent, all that isSolved needs to do is count the number
	// of queens that have been placed. When it reaches 8, the game
	// has been solved.
	int count = 0;
	for (int i = 0; i < 8; i++) {
	    for (int j = 0; j < 8; j++) {
		if (board[i][j] == 1) {
		    count++;
		}
	    }
	}
	if (count == 8) {
	    return true;
	}
	return false;
    }

    /**
     * The majority of the game logic comes from isValid, which
     * determines whether queen placement in the present cell is a valid
     * choice.
     *
     * @param board the game board matrix
     * @param row the current row number
     * @param col the current column number
     *
     * @param a boolean for truth value of placement validity
     */
    public static boolean isValid(int board[][], int row, int col) {
	// If a queen has already been placed here, move on.
	if (board[row][col] == 1) {
	    return false;
	}

	// Here we're setting the board with the new value for testing.
	for (int i = 0; i < 8; i++) {
	    for (int j = 0; j < 8; j++) {
		if (row == i && col == j) {
		    board[row][col] = 1;
		}
	    }
	}

	// count will be used in the three directional scenarios.
	int count = 0;

	// This section checks against all of the current row values.
	for (int i = 0; i < 8; i++) {
	    for (int j = 0; j < 8; j++) {
		if (board[i][j] == 1) {
		    count++;
		}
	    }
	    if (count > 1) {
		// Present in all the scenarios, this resets the tested
		// value if its placement was invalid.
		board[row][col] = 0;
		return false;
	    }
	    count = 0;
	}

	// This section tests the current column values.
	for (int i = 0; i < 8; i++) {
	    for (int j = 0; j < 8; j++) {
		if (board[j][i] == 1) {
		    count++;
		}
	    }
	    if (count > 1) {
		board[row][col] = 0;
		return false;
	    }
	    count = 0;
	}

	// dRow and dCol are introduced to evaluate whether or not
	// the current queen can reach another diagonally.
	int dRow = row - 1;
	int dCol = col - 1;
	while (dRow >= 0 && dCol >= 0) {
	    if (board[dRow][dCol] == 1) {
		count++;
	    }
	    dRow--;
	    dCol--;
	}

	dRow = row + 1;
	dCol = col + 1;
	while (dRow < 8 && dCol < 8) {
	    if (board[dRow][dCol] == 1) {
		count++;
	    }
	    dRow++;
	    dCol++;
	}

	dRow = row - 1;
	dCol = col + 1;
	while (dRow >= 0 && dCol < 8) {
	    if (board[dRow][dCol] == 1) {
		count++;
	    }
	    dRow--;
	    dCol++;
	}

	dRow = row + 1;
	dCol = col - 1;
	while (dRow < 8 && dCol >= 0) {
	    if (board[dRow][dCol] == 1) {
		count++;
	    }
	    dRow++;
	    dCol--;
	}

	if (count > 0) {
	    board[row][col] = 0;
	    return false;
	}

	// If the current queen cannot reach any others, their placement
	// is accepted and considered valid.
	return true;
    }

    /**
     * recurser uses backtracking to solve the problem by implementing
     * isValid repeatedly and backing out to a different context if
     * it has worked itself into a logical corner.
     *
     * @param board the game's matrix representation
     */
    public static boolean recurser(int board[][]) {
	// If 8 queens have been successfully placed, back out all the
	// way from the lowest context.
	if (isSolved(board)) {
	    return true;
	}

	for (int i = 0; i < 8; i++) {
	    for (int j = 0; j < 8; j++) {
		if (isValid(board, i, j)) {
		    // If a queen has been placed this move, use this
		    // same function again taking the board with the
		    // new parameters.
		    if (recurser(board)) {
			return true;
		    }
		    // If the next level down of recurser returns false,
		    // then the queen that was considered valid within
		    // the present context is actually invalid and
		    // should be removed.
		    board[i][j] = 0;
		}
	    }
	}
	return false;
    }
}
