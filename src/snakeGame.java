import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.Timer;

public class snakeGame extends JPanel implements ActionListener, KeyListener {

  // Inner class to represent a tile
  private class Tile {

    int x;
    int y;

    public Tile(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  int boardwidth;
  int boardhight;
  int tileSize = 25;
  // snake head
  Tile snakeHead;
  //snake body
  ArrayList<Tile> snakeBody;
  //food
  Tile food;
  Random random;
  // Timer
  Timer gameLoop;
  //direction
  int velocityX;
  int velocityY;

  boolean gameOver = false;
  boolean running = false;

  // Constructor
  snakeGame(int boardwidth, int boardhight) {
    this.boardwidth = boardwidth;
    this.boardhight = boardhight;
    setPreferredSize(new Dimension(this.boardwidth, this.boardhight));
    setBackground(Color.gray);
    addKeyListener(this);
    setFocusable(true);
    //init snake head
    snakeHead = new Tile(5, 5);
    //init food
    food = new Tile(10, 10);
    //init snake body
    snakeBody = new ArrayList<>();

    random = new Random();
    //place the food on frame
    placeFood();
    //init gameloop timer
    gameLoop = new Timer(500, this);
    // gameLoop.start();

    velocityX = 1;
    velocityY = 0;
  }

  // Custom paint method to draw the game
  public void paint(Graphics g) {
    super.paint(g);
    draw(g);
  }

  // Method to draw the game elements
  private void draw(Graphics g) {
    // Draw the food
    g.setColor(Color.red);
    g.fillOval(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

    // Draw the snake head
    g.setColor(Color.green);
    g.fill3DRect(
      snakeHead.x * tileSize,
      snakeHead.y * tileSize,
      tileSize,
      tileSize,
      true
    );

    // Draw the snake body
    for (int i = 0; i < snakeBody.size(); i++) {
      Tile snakePart = snakeBody.get(i);
      g.fill3DRect(
        snakePart.x * tileSize,
        snakePart.y * tileSize,
        tileSize,
        tileSize,
        true
      );
    }
    //set font family and size of messages
    g.setFont(new Font("Arial", Font.PLAIN, 20));

    if (!running) {
      //display message when game not running
      g.drawString(
        "Press any Arrow Key to Start -> ",
        tileSize + 180,
        tileSize
      );
    } else {
      g.drawString("", tileSize + 180, tileSize);
    }

    // Display the game score
    if (gameOver) {
      // Display game over message and option to restart
      g.setColor(Color.red);
      g.drawString(
        "Game Over!! Your Score : " + String.valueOf(snakeBody.size()),
        tileSize - 16,
        tileSize
      );
      g.drawString("Press space to Restart", 210, 250);
    } else {
      // Display the current score
      g.drawString(
        "Score : " + String.valueOf(snakeBody.size()),
        tileSize - 16,
        tileSize
      );
    }
  }

  // Method to place food at a random location
  public void placeFood() {
    food.x = random.nextInt(boardwidth / tileSize);
    food.y = random.nextInt(boardhight / tileSize);
  }

  // Method to check for collision between two tiles
  public boolean collision(Tile tile1, Tile tile2) {
    return tile1.x == tile2.x && tile1.y == tile2.y;
  }

  // Method to move the snake
  public void move() {
    // Eat food and increase snake size
    if (collision(snakeHead, food)) {
      snakeBody.add(new Tile(food.x, food.y));
      placeFood();
    }
    // Move the snake's body
    for (int i = snakeBody.size() - 1; i >= 0; i--) {
      Tile snakePart = snakeBody.get(i);
      if (i == 0) {
        snakePart.x = snakeHead.x;
        snakePart.y = snakeHead.y;
      } else {
        Tile prevSnakePart = snakeBody.get(i - 1);
        snakePart.x = prevSnakePart.x;
        snakePart.y = prevSnakePart.y;
      }
    }

    // Move the snake's head
    snakeHead.x += velocityX;
    snakeHead.y += velocityY;

    // Game over conditions
    //if snake hits other part of the body then game over
    for (int i = 0; i < snakeBody.size(); i++) {
      Tile snakePart = snakeBody.get(i);

      if (collision(snakeHead, snakePart)) {
        gameOver = true;
      }
    }
    //if snake hits wall of the frame then game over
    if (
      snakeHead.x * tileSize < 0 ||
      snakeHead.x * tileSize > boardwidth ||
      snakeHead.y * tileSize < 0 ||
      snakeHead.y * tileSize > boardhight
    ) {
      gameOver = true;
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // Action to be performed on each timer tick
    move();
    repaint();
    if (gameOver) {
      //when game over then stop the gameLoop
      gameLoop.stop();
      //   running = false;
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    //game running
    running = true;
    //when  up arrow key is pressed
    if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
      velocityX = 0;
      velocityY = -1;
      gameLoop.start();
    }
    // when  down arrow key is pressed
    else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
      velocityX = 0;
      velocityY = 1;
      gameLoop.start();
    }
    //when left arrow key is pressed
    else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != -1) {
      velocityX = -1;
      velocityY = 0;
      gameLoop.start();
    }
    //when right arrow key is pressed
    else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
      velocityX = 1;
      velocityY = 0;
      gameLoop.start();
    }

    //when space key is pressed
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      // ReStart the game when the space bar is pressed
      //   gameLoop.start();
      //reset the game
      reset();
      //rePaint the initial snake position
      repaint();
    }
  }

  //resets the game to the initial state
  public void reset() {
    gameOver = false;
    running = false;
    snakeHead = new Tile(5, 5);
    food = new Tile(10, 10);
    snakeBody = new ArrayList<>();
    placeFood();
    velocityX = 1;
    velocityY = 0;
  }

  // Unimplemented KeyListener methods
  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}
}
