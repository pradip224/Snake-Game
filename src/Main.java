import java.util.*;
import javax.swing.*;

public class Main {

  public static void main(String[] args) {
    // Set the dimensions of the game board
    int boardWidth = 600;
    int boardHeight = 600;

    // Create a JFrame (window) for the game and configure its properties
    JFrame frame = new JFrame("My Snake Game");
    frame.setVisible(true);
    frame.setSize(boardWidth, boardHeight);
    frame.setLocationRelativeTo(null); // Center the frame on the screen
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit application when the window is closed

    // Print a message to the console to indicate that the game has started
    System.out.println("Game Started!");

    // Create an instance of the snakeGame class
    snakeGame snakeGame = new snakeGame(boardWidth, boardHeight);

    // Add the snakeGame JPanel to the frame
    frame.add(snakeGame);

    // Pack the frame to ensure its components are sized correctly
    frame.pack();

    // Request focus for the snakeGame, so it can receive user input
    snakeGame.requestFocus();
  }
}
