/* Author: Rosa Sun
 * Date: 2017-06-19
 * Description: This class creates the classic Snake Game - a part of the Sun's Arcade
*/
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
	private final int BOARD_WIDTH, BOARD_HEIGHT;
	private JPanel snakeGamePanel, scorePanel, playPanel;
	private JLabel username, lblScore;
	private String direction;
	private int score, currentX, currentY, foodX, foodY;
	private boolean timerHasStarted = false, dead=false;
	
	private JLabel[][] allLabels;
	private List<List<Integer>> snakePositions;

	private BufferedImage keys, gameOver;
	private Timer timer;
	private Random rand = new Random();

	//constructor method
	public SnakeGame() {
		this(80, 40);
	}

	//constructor method
	public SnakeGame(int w, int h) {
		BOARD_WIDTH = w;
		BOARD_HEIGHT = h;
		score = 0;
		direction = "up";
		
		Main.frame.addKeyListener(this);
		
		URL resource = getClass().getResource("imgs/keys.png");
        try {
           	keys = ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        resource=getClass().getResource("imgs/gameOver.png");
        try {
           	gameOver = ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
//setting up screen...
//=======================================================================================================           
		snakeGamePanel = new JPanel();
		snakeGamePanel.setLayout(new BoxLayout(snakeGamePanel, BoxLayout.Y_AXIS));
		snakeGamePanel.setSize(1000, 600);
		snakeGamePanel.setBackground(new Color(255, 255, 190));
		

		playPanel = new JPanel(new GridLayout(BOARD_HEIGHT, BOARD_WIDTH, 0, 0)){
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				//same piece of code as paintComponent() from the flappy bird screen
				if(dead){
					g.setColor(Color.orange);
					g.setFont(new Font("Kristen ITC", Font.BOLD, 60));
					if (hasBeatHighScore()){
						SignIn.setSnakeScore(score);
						g.setColor(Color.green);
						g.setFont(new Font("Kristen ITC", Font.BOLD, 40));
						g.drawString("YOU BEAT YOUR HIGH SCORE!", 150, 220);
					}else{
						SignIn.addToTotal(score);
						g.drawString("BETTER LUCK NEXT TIME!", 30, 220);
					}
					g.setColor(Color.orange);
					g.drawImage(gameOver, 200, 20, this);
					g.setFont(new Font("Kristen ITC", Font.BOLD, 30));
					g.drawString("SCORE: "+score, 420, 300);
					g.drawString("BEST SCORE: "+SignIn.getSnakeScore(), 370, 360);
					g.drawString("PRESS \"ENTER\" TO CONTINUE", 235, 450);
				}
			}
		};
		playPanel.setBackground(new Color(255, 255, 190));
		snakeGamePanel.add(playPanel);

		scorePanel = new JPanel();
		scorePanel.setBackground(new Color(255, 255, 230));
		scorePanel.setBorder(new EmptyBorder(0, 0, -490, 0));
		snakeGamePanel.add(scorePanel);

		lblScore = new JLabel("Score: " + score,SwingConstants.RIGHT);
		lblScore.setFont(new Font("Kristen ITC", Font.BOLD, 40));
		lblScore.setForeground(Color.orange);
		scorePanel.add(lblScore);
		playPanel.repaint();

		//generate a label for each cell in the GridLayout, these labels will be contained in a 2D array of labels
		//called "allLabels[][]"
		allLabels = new JLabel[BOARD_HEIGHT][BOARD_WIDTH];
		
		//use nested for loops to initialize labels in the 2D array of labels
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; j++) {
				//first, set all their visibilities to false
				allLabels[i][j] = new JLabel();
				allLabels[i][j].setOpaque(true);
				allLabels[i][j].setVisible(false);
				playPanel.add(allLabels[i][j], i, j);
				
				//if the label is on either of the four sides of the gridLayout, the label will be visible and
				//the background color of the label will be orange
				if (i == 0 || i == BOARD_HEIGHT - 1 || j == 0
						|| j == BOARD_WIDTH - 1) {
					allLabels[i][j].setBackground(Color.ORANGE);
					allLabels[i][j].setVisible(true);
				}
			}
		}

		//generate new values for the x and y positions of the red square and the green square
		//will continue to generate new values if the food and the snake collides, or if one of them is on the border
		do {
			currentX = rand.nextInt(BOARD_WIDTH-4)+2;
			currentY = rand.nextInt(BOARD_HEIGHT-4)+2;
			foodX = rand.nextInt(BOARD_WIDTH-4)+2;
			foodY = rand.nextInt(BOARD_HEIGHT-4)+2;
		} while ((currentX ==foodX && currentY == foodY)&&allLabels[foodY][foodX].getBackground()!=Color.ORANGE && allLabels[currentY][currentX].getBackground()!=Color.ORANGE);
		
		//set the background color of the snake and the food, and make them visible
		allLabels[currentY][currentX].setBackground(new Color(34, 139, 34));
		allLabels[currentY][currentX].setVisible(true);
		allLabels[foodY][foodX].setBackground(Color.red);
		allLabels[foodY][foodX].setVisible(true);
		
		//snakePositions is a 2D arrayList containing all the x and y values of the pieces of the snake
		snakePositions = new ArrayList<List<Integer>>();
		snakePositions.add(Arrays.asList(currentY, currentX)); //add the newly generated position of the snake into this 2D arrayList
		
		Main.frame.add(snakeGamePanel);
		Main.frame.setVisible(true);
		
//============================================================================================================
//finish screen setup		
		
		
		//OptionPane for instructions
		JOptionPane.showMessageDialog(null, "Press arrow keys to move. \nRemember, you're the green square!", "Instructions", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(new ImageIcon(getClass()
				.getResource("imgs/keys.png")).getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH)));
		
		timer = new Timer(60, this);
	}

	//addRectangle() adds one other piece to the snake based on the direction the snake is going
	public void addRectangle() {
		
		//if the direction is up, the new piece of the snake will be added to the top of the leading piece (the head of the snake)
		//the leading piece of the snake is set to (currentX, currentY)
		//if the direction is up, the new piece will be at one unit higher, so (currentX, currentY+1)
		if (direction == "up") {
			//if the new piece won't be on top of the border...
			if ((currentY + 1) != BOARD_HEIGHT) {
				//if the snake runs into itself
				if (allLabels[currentY + 1][currentX].isVisible()
						&& allLabels[currentY + 1][currentX].getBackground() != Color.red) {
					//snake dies
					die();
				
				//otherwise add new piece (set the label on that corresponding cell to visible and set the background
				//color to green)
				} else {
					currentY++; //set the new currentY variable to be used later on
					allLabels[currentY][currentX].setBackground(new Color(34, 139, 34)); //set color to green
					allLabels[currentY][currentX].setVisible(true); //set visible to true
					snakePositions.add(Arrays.asList(currentY, currentX)); //add this position to 2D arraylist "snakePositions"
				}
			} else {
				//dies if the new piece collides with the border
				die();
			}
			
			//above code applies in the same way for the remaining three directions
			// |
			// |
			// |
			// V	
		
		//if snake is going down
		} else if (direction == "down") {
			if ((currentY - 1) != -1) {
				if (allLabels[currentY - 1][currentX].isVisible()
						&& allLabels[currentY - 1][currentX].getBackground() != Color.red) {
					die();
				} else {
					currentY--;
					allLabels[currentY][currentX].setBackground(new Color(34,
							139, 34));
					allLabels[currentY][currentX].setVisible(true);
					snakePositions.add(Arrays.asList(currentY, currentX));
				}
			} else {
				die();
			}
		
		//if snake is going left	
		} else if (direction == "left") {
			if ((currentX - 1) != -1) {
				if (allLabels[currentY][currentX - 1].isVisible()
						&& allLabels[currentY][currentX - 1].getBackground() != Color.red) {
					die();
				} else {
					currentX--;
					allLabels[currentY][currentX].setBackground(new Color(34,
							139, 34));
					allLabels[currentY][currentX].setVisible(true);
					snakePositions.add(Arrays.asList(currentY, currentX));
				}
			} else {
				die();
			}
			
		//if snake is going right	
		} else if (direction == "right") {
			if ((currentX + 1) != BOARD_WIDTH) {
				if (allLabels[currentY][currentX + 1].isVisible()
						&& allLabels[currentY][currentX + 1].getBackground() != Color.red) {
					die();
				} else {
					currentX++;
					allLabels[currentY][currentX].setBackground(new Color(34,
							139, 34));
					allLabels[currentY][currentX].setVisible(true);
					snakePositions.add(Arrays.asList(currentY, currentX));
				}
			} else {
				die();
			}
		}
	}
	
	//stop() is called when the user loses
	public void stop(){
		//set all labels in each cell to invisible to leave space for the game over message
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; j++) {
				allLabels[i][j].setVisible(false);
			}
		}
		playPanel.repaint();
	}
	
	//remove the last rectangle (called on every time the timer ticks)
	public void removeLastRectangle() {
		allLabels[snakePositions.get(0).get(0)][snakePositions.get(0).get(1)]
				.setVisible(false);
		snakePositions.remove(0);
	}

	//called on when the snake collides with the border or itself
	public void die() {
		timer.stop(); //timer stops, stop adding and removing pieces from the snake
		snakeGamePanel.remove(scorePanel); //remove score panel to leave space for game over message
		stop(); 
		dead=true;
		playPanel.repaint();
	}

	//boolean method check if the snake died
	public boolean hasDied() {
		if (currentY == BOARD_HEIGHT || currentY == -1
				|| currentX == BOARD_WIDTH || currentX == -1) {
			return true;
		} else {
			return false;
		}
	}

	//boolean method hasFood() checks if the added rectangles reach the food
	public boolean hasFood() {
		if (currentY == foodY && currentX == foodX) {
			return true;
		} else {
			return false;
		}
	}

	//method called when the timer ticks
	public void timerTick() {
		snakeGamePanel.repaint();
		if (hasFood()) {
			score+=2; //score increments by 2 every time the snake gets the food
			lblScore.setText("Score: " + score); //update text for lblScore
			
			//make the food position part of the snake
			allLabels[foodY][foodX].setBackground(new Color(34, 139, 34));
			snakePositions.add(Arrays.asList(foodY, foodX));
			addRectangle(); //add another rectangle (add two when the snake eats the food)
			
			//generate new x and y values for the food cell
			do {
				foodX = rand.nextInt(BOARD_WIDTH - 4)+2;
				foodY = rand.nextInt(BOARD_HEIGHT - 4)+2;
			} while (allLabels[foodY][foodX].isVisible());
			
			//set this cell to visible and color to red
			allLabels[foodY][foodX].setBackground(Color.red);
			allLabels[foodY][foodX].setVisible(true);
		} else if (hasDied()) {
			die();
		} else {
			//if nothing special is happening, add a new leading rectangle and remove the trailing one (just playing around
			//with the visibility
			addRectangle();
			removeLastRectangle();
		}
		playPanel.repaint();
	}

	//boolean method checks if the the user has beaten its high score
	public boolean hasBeatHighScore() {
		if (score > SignIn.getSnakeScore()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == timer){
			timerTick();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (timerHasStarted == false) {
			timer.start();
			timerHasStarted = true;
		}
		// "UP" key is pressed, direction = up, 
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			
			//if direction = down and the snake is longer than 1 square, the snake cannot go in the opposite
			//direction so the direction does not change
			if (!direction.equals("down") || snakePositions.size() == 1) {
				direction = "up";
			}
		
		//above code applies in the same way for the remaining three directions
		// |
		// |
		// |
		// V	
			
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (!direction.equals("up") || snakePositions.size() == 1) {
				direction = "down";
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (!direction.equalsIgnoreCase("right")
					|| snakePositions.size() == 1) {
				direction = "left";
			}
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (!direction.equals("left") || snakePositions.size() == 1) {
				direction = "right";
			}
		} else if(e.getKeyCode()==KeyEvent.VK_ENTER){
			if (dead)
			{
				Main.frame.getContentPane().removeAll();
				Main.frame.setSize(1000, 600);
				Main.frame.repaint();
				new HomeScreen();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
