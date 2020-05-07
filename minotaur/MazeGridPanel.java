/**
 * A simple maze builder/solver that implements the stack
 * data structure. I decided that I like to be able to see
 * where the advancing cell ambles off of the appropriate path,
 * so I made the solution path and the dead-ends low contrast
 * colors in order to see where diversions started.
 *
 * @author Saxon McIntosh
 * @version February 9, 2020
 */

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class MazeGridPanel extends JPanel {
    private int rows;
    private int cols;
    private Cell[][] maze;

    // Extra credit
    public void generateMazeByDFS() {
	double randArr[] = new double[4];
	int index = -1;
	Stack<Integer> moveOrder = new Stack<Integer>();
	boolean[][] visited;
	Stack<Cell> stack = new Stack<Cell>();
	Cell start = maze[0][0];
	stack.push(start);

	while (!stack.isEmpty()) {
	    // This next bit generates a string of random
	    // doubles to use for randomized directions.
	    for (int i = 0; i < randArr.length; i++) {
		randArr[i] = Math.random();
	    }
	    // The index of the random values determines the
	    // order that the cardinal direction values are
	    // pushed to the moveOrder stack.
	    for (int i = 0; i < randArr.length; i++) {
		double stackMax = -1;
		for (int j = 0; j < randArr.length; j++) {
		    if (randArr[j] > stackMax) {
			stackMax = randArr[j];
			index = j;
		    }
		}
		randArr[index] = -1;
		moveOrder.push(index);
	    }

	    Cell current = stack.peek();

	    // If all of the cardinal directions have been exhausted,
	    // move on.
	    while (!moveOrder.isEmpty() && current == stack.peek()) {
		int nextMove = moveOrder.pop();
		// These cases each test to see if current is too
		// close to a grid boundary or if the direction
		// has already been visited, then erase the
		// combining walls and push the new cell to the stack.
		switch (nextMove) {
		case 0:
		    if (current.row != 0 && !visited(current.row - 1, current.col)) {
			current.northWall = false;
			maze[current.row - 1][current.col].southWall = false;
			stack.push(maze[current.row - 1][current.col]);
		    }
		    break;
		case 1:
		    if (current.col != cols - 1 && !visited(current.row, current.col + 1)) {
			current.eastWall = false;
			maze[current.row][current.col + 1].westWall = false;
			stack.push(maze[current.row][current.col + 1]);
		    }
		    break;
		case 2:
		    if (current.row != rows - 1 && !visited(current.row + 1, current.col)) {
			current.southWall = false;
			maze[current.row + 1][current.col].northWall = false;
			stack.push(maze[current.row + 1][current.col]);
		    }
		    break;
		default:
		    if (current.col != 0 && !visited(current.row, current.col - 1)) {
			current.westWall = false;
			maze[current.row][current.col - 1].eastWall = false;
			stack.push(maze[current.row][current.col - 1]);
		    }
		}
	    }
	    // The visited cell is colored gray.
	    if (current != stack.peek()) {
		stack.peek().setBackground(Color.GRAY);
	    } else {
		stack.pop();
	    }
	}
	// Re-colors the cells white after the walls have been
	// established, otherwise visited() will not work.
	for (int i = 0; i < rows; i++) {
	    for (int j = 0; j < cols; j++) {
		maze[i][j].setBackground(Color.WHITE);
	    }
	}
	maze[0][0].setBackground(Color.GREEN);
	maze[rows - 1][cols - 1].setBackground(Color.RED);
    }

    public void solveMaze() {
	Stack<Cell> stack = new Stack<Cell>();
	Cell start = maze[0][0];
	start.setBackground(Color.GREEN);
	Cell finish = maze[rows - 1][cols - 1];
	finish.setBackground(Color.RED);
	stack.push(start);

	while (stack.peek() != finish) {
	    Cell current = stack.peek();
	    // Sets the visited cells to yellow, provided they are not start (so that it still looks asthetic).
	    if (current != start) {
		current.setBackground(Color.YELLOW);
	    }

	    // If there is an available unvisited path, that direction is pushed to the stack.
	    if (current.northWall == false && !visited(current.row - 1, current.col)) {
		stack.push(maze[current.row - 1][current.col]);
	    } else if (current.eastWall == false && !visited(current.row, current.col + 1)) {
		stack.push(maze[current.row][current.col + 1]);
	    } else if (current.southWall == false && !visited(current.row + 1, current.col)) {
		stack.push(maze[current.row + 1][current.col]);
	    } else if (current.westWall == false && !visited(current.row, current.col - 1)) {
		stack.push(maze[current.row][current.col - 1]);
	    } else {
		// In the event that there are no unvisited cells adjacent, the most recent cell change
		// is popped from the stack. Also, the color of the dead end cell is changed to orange.
		// This has the effect of revealing the exact path traveled.
		if (current != start) {
		    current.setBackground(Color.ORANGE);
		}
		stack.pop();
	    }
	}
    }

    public boolean visited(int row, int col) {
	Cell c = maze[row][col];
	Color status = c.getBackground();
	if (status.equals(Color.WHITE) || status.equals(Color.RED)) {
	    return false;
	}

	return true;
    }

    public void generateMaze() {
	for (int row = 0; row < rows; row++) {
	    for (int col = 0; col < cols; col++) {

		if (row == 0 && col == 0) {
		    continue;
		} else if (row == 0) {
		    maze[row][col].westWall = false;
		    maze[row][col - 1].eastWall = false;
		} else if (col == 0) {
		    maze[row][col].northWall = false;
		    maze[row - 1][col].southWall = false;
		} else {
		    boolean north = Math.random() < 0.5;
		    if (north) {
			maze[row][col].northWall = false;
			maze[row - 1][col].southWall = false;
		    } else {
			maze[row][col].westWall = false;
			maze[row][col - 1].eastWall = false;
		    }
		    maze[row][col].repaint();
		}
	    }
	}

	this.repaint();
    }

    public MazeGridPanel(int rows, int cols) {
	this.setPreferredSize(new Dimension(800, 800));
	this.rows = rows;
	this.cols = cols;
	this.setLayout(new GridLayout(rows, cols));
	this.maze = new Cell[rows][cols];
	for (int row = 0; row < rows; row++) {
	    for (int col = 0; col < cols; col++) {
		maze[row][col] = new Cell(row, col);
		this.add(maze[row][col]);
	    }
	}

	this.generateMazeByDFS();
	this.solveMaze();
    }
}
