import javax.swing.*;

public class Maze extends JFrame {

    /*
     * You can overload the default constructor so that
     * it accepts two integers for the maze size
     */
    public Maze() {
	this.add(new MazeGridPanel(100, 100));
	this.setSize(800, 800);

	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.pack();
	this.setVisible(true);
    }

    public static void main(String[] args) {
	new Maze();
    }
}
