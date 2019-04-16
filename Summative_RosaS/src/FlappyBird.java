/* Author: Rosa Sun
 * Date: 2017-06-19
 * Description: This class creates the game Flappy Bird - a part of the Sun's Arcade
*/
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
 
public class FlappyBird implements ActionListener, KeyListener{
	private List<List<Integer>> pipes=new ArrayList<List<Integer>>();
	private JPanel flappyBirdPanel, playPanel, scorePanel;
	private int score,y,gap=0,height=0, counter=0, yx;
	private	BufferedImage bird, gameOver;
	private Random rand=new Random();
	private Timer timerP, timerB;
	private boolean jumping=false;
	private JLabel username, lblScore;
	private boolean dead=false, timerHasStarted=false;
	
	//constructor method
	public FlappyBird(){
		Main.frame.addKeyListener(this);
		
		URL resource = getClass().getResource("imgs/bird.png");
        try {
            bird = ImageIO.read(resource);
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
//=========================================================================================        
		flappyBirdPanel = new JPanel();
		flappyBirdPanel.setLayout(new BoxLayout(flappyBirdPanel, BoxLayout.Y_AXIS));
		flappyBirdPanel.setSize(1000, 600);
		flappyBirdPanel.setBackground(new Color(255, 255, 190));
		
		y=600/2;
		playPanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				if(dead){
					//draws the screen for when the user dies (tells them their best score and the current score
					g.setColor(Color.orange);
					g.setFont(new Font("Kristen ITC", Font.BOLD, 60));
					if (hasBeatHighScore()){
						SignIn.setBirdScore(score); //call SignIn.setBirdScore which will do everything needed when
													//when the user has beaten their high score
						g.setColor(Color.green);
						g.setFont(new Font("Kristen ITC", Font.BOLD, 40));
						g.drawString("YOU BEAT YOUR HIGH SCORE!", 150, 220); //write on the screen that the user has
																			 //beaten their high score to let them know
					}else{ //if the user hasn't beaten their high score
						SignIn.addToTotal(score); //call SignIn.addToTotal() to add their current score to their total score
						g.drawString("BETTER LUCK NEXT TIME!", 30, 220); //wish them better luck next time
					}
					
					g.setColor(Color.orange);
					g.drawImage(gameOver, 200, 20, this);
					g.setFont(new Font("Kristen ITC", Font.BOLD, 30));
					g.drawString("SCORE: "+score, 420, 300); //draw their score on the screen
					g.drawString("BEST SCORE: "+SignIn.getBirdScore(), 370, 360); //draw their best score so that
																				  //they can compare
					g.drawString("PRESS \"ENTER\" TO CONTINUE", 235, 450); //tell them to press "ENTER" to continue
				}else{//if the user hasn't died yet...	
					//keep drawing the bird and the pipes
					
					/* program will draw the bird at the same x position with the pipes moving to make it seem 
					 * like the bird is going forward when in reality the bird is only going up and down.
					 * The bird will always be drawn at x=100, but its y value depends on the variable called "y"
					 */
					g.drawImage(bird, 100, y, 50, 50, this); 
					g.setColor(new Color(34,139,34));
					for(int i=0;i<pipes.size();i++){
						//drawing the rectangles
						g.fillRect(pipes.get(i).get(0), pipes.get(i).get(1), pipes.get(i).get(2), pipes.get(i).get(3));

						//as each rectangle is drawn, the below piece of code will check if the rectangles contain any of the
						//four sides of the bird, if yes, that means there has been a collision between the 
						//bird and the pipes and the game will end.
						Rectangle r=new Rectangle(pipes.get(i).get(0), pipes.get(i).get(1), pipes.get(i).get(2), pipes.get(i).get(3));
						if (r.contains(115,y+15)||r.contains(115,y+35)||r.contains(135,y+15)||r.contains(135,y+35)||y+35>=600||y+15<=0){
							timerP.stop();
							timerB.stop();
							g.clearRect(0, 0, 1000, 600);
							die();
							break;
						}
					}
				}
			}
		};
		playPanel.setBackground(new Color(255, 255, 190));
		flappyBirdPanel.add(playPanel);
		
		scorePanel = new JPanel();
		scorePanel.setBackground(new Color(255, 255, 230));
		scorePanel.setBorder(new EmptyBorder(0, 0, -490, 0));
		flappyBirdPanel.add(scorePanel);

		lblScore = new JLabel("Score: " + score);
		lblScore.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblScore.setHorizontalAlignment(JLabel.CENTER);
		lblScore.setFont(new Font("Kristen ITC", Font.BOLD, 40));
		lblScore.setForeground(Color.orange);
		lblScore.setPreferredSize(new Dimension(300,100));
		lblScore.setSize(lblScore.getPreferredSize());
		scorePanel.add(lblScore);
		
		Main.frame.add(flappyBirdPanel);
		Main.frame.setVisible(true);
//============================================================================================================
//finish screen setup
		
		//OptionPane for instructions
		JOptionPane.showMessageDialog(null, "Press \"UP\" key to jump in between the pipes", "Instructions", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(new ImageIcon(getClass()
				.getResource("imgs/up.png")).getImage().getScaledInstance(150, 105, Image.SCALE_SMOOTH)));
		
		//initialize the two timers
		timerP=new Timer(8,this); //timerP used to move the pipes
		timerB=new Timer(24,this); //timerB used to move the bird
	}
	
	//addPipes() adds two more corresponding pipes to the right end of the screen
	private void addPipes(){
		gap=rand.nextInt(90)+140; //uses random number generator to generate the size of the gap
		height=rand.nextInt(100)+150; //uses random number generator to generate a height for the top pipe
		pipes.add(Arrays.asList(1000,0,100,height)); //adds a 1D list to the 2D array "pipes",
													 //containing the x, y, width, and height value respectively
													 //which will be used to generate the top pipe
		pipes.add(Arrays.asList(1000, gap+height, 100, 600-gap-height)); //uses the same values to generate the
																		 //bottom pipe
	}
	
	//deletePipes() removes the first two pipes that would have disappeared from the screen to use up less memory
	private void deletePipes(){
		pipes.remove(0);
		pipes.remove(0);
	}
 
	//this method checks if the user has beaten their high score
	public boolean hasBeatHighScore() {
		if (score > SignIn.getBirdScore()) { //compares their current score with the best score retrieved from
											 //users.txt file
			return true; //if their score is higher than their old best score, method returns true
		} else {
			return false; //if not method returns false
		}
	}
	
	//die() method is called when the user dies
	private void die(){
		dead=true; //sets "dead" boolean variable to true
		playPanel.repaint(); //repaint() also calls on paintComponent() - I have overridden this method so that
							 //it will paint something different on the screen when dead=true
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//when timerP ticks...
		if (e.getSource()==timerP){
			//int variable "counter" counts the number of times timerP has ticked
			if (counter%80==0){ //every 80th time the timerP gets triggered, two new pipes will be addded
				addPipes();
			}
			counter++;
			if(pipes.get(0).get(0)<=-100){ //when the x value of one of the first two pipes is less than -100, that means
										   //they are off the screen
				//delete first two pipes as soon as their x values are less than -100 by calling on deletePipes()
				deletePipes();
			}
			if(pipes.get(0).get(0)==0){ //after the first two pipes are deleted, the next "first pipes" are the first ones on the screen
										//If the x value of those pipes = 0, that means they just passed by the bird
										//so int score increases by 1
				score++; //accumulate score
				lblScore.setText("Score: "+score); //reset text of the JLabel keeping track of the score
			}
			
			//use for loop move all of the pipes 5 units to the left by subtracting each of their x values by 5
			for(int i=0;i<pipes.size();i++){
				pipes.get(i).set(0, pipes.get(i).get(0)-5); //sets new x value
			}
			
			//call repaint() to trigger paintComponent()
			playPanel.repaint();
			
		//when timerB ticks...	
		}else if (e.getSource()==timerB){
			//jumping=true when the bird is jumping
			if (jumping==true){
				if(yx==4){ //bird stops jumping when variable keeping track of acceleration = 4
					jumping=false;
				}
				y-=yx; //the y value of the bird decreases(bird goes up) by the value of yx
				yx-=2; //yx keeps decreasing, meaning that the bird slows down in the upward direction (because of previous line)
			
			// if the bird isn't jumping (it's falling)
			}else{
				y+=yx; //the y value of the bird increases by the value of yx
				yx+=2; //yx keeps increasing, making the bird fall faster and faster due to previous line
			}
			
			//repaint() calls on paintComponent which will redraw the position of the bird now that its y-value has changed
			playPanel.repaint();
		}
	}
 
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		//when the "UP" key is pressed...
		if (e.getKeyCode() == KeyEvent.VK_UP){
			jumping=true; //the bird will be jumping, so the "jumping" variable becomes true
			yx=18; //yx=18, because yx will start to decrease by 2 everytime timerB ticks
			
			//when the "UP" key is pressed before the game has started (aka when both timers hasn't started yet)
			//the game begins
			if (!timerHasStarted){
				timerHasStarted=true; //set this variable to true now
				//start the two timers
				timerB.start(); 
				timerP.start();
			}

		//when the enter key is pressed...
		} else if(e.getKeyCode()==KeyEvent.VK_ENTER){
			if (dead){ //if the game has ended...
				
				//open home screen by calling "new HomeScreen()" and removing everything else from Main.frame
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
