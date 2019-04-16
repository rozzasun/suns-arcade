/* Author: Rosa Sun
 * Date: 2017-06-19
 * Description: This class creates the home screen of Suns's Arcade
*/
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class HomeScreen implements ActionListener{
	private JPanel homeScreenPanel, topPanel, secondPanel, filler;
	private JLabel lblLogo, user;
	private JButton btnSnake, btnFlappyBird, btnHighScore, btnLogOut;
	
	public HomeScreen(){

//setting up screen...
//================================================================================================        		
		homeScreenPanel=new JPanel(){
			protected void paintComponent(Graphics g){
				g.setColor(new Color(255,255,190));
				g.fillRect(0, 0, 1000, 600);
			}
		};
		homeScreenPanel.setLayout(new BoxLayout(homeScreenPanel, BoxLayout.Y_AXIS));
		homeScreenPanel.setSize(1000,600);
		homeScreenPanel.setBackground(new Color(255,255,190));
		
		homeScreenPanel.repaint();
		topPanel=new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setPreferredSize(new Dimension(1000,180));
		topPanel.setMaximumSize(topPanel.getPreferredSize());
		topPanel.setBackground(new Color(255,255,230));
		homeScreenPanel.add(topPanel);
		
		lblLogo=new JLabel(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource(Main.LOGO_PATH)).getImage().getScaledInstance(450,170, Image.SCALE_SMOOTH)));
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(lblLogo);
		
		filler=new JPanel();
		filler.setPreferredSize(new Dimension(3,topPanel.getHeight()));
		filler.setMaximumSize(topPanel.getPreferredSize());
		filler.setBackground(Color.orange);
		topPanel.add(filler);
		
		topPanel.add(Box.createRigidArea(new Dimension(25,0)));

		user=new JLabel();
		String template = "<html><span style=\"text-align: center;\">Username: %s</span><br />Score: %s</html>";
		String text = String.format(template, SignIn.getUsername(), Integer.toString(SignIn.getTotalScore()));
		user.setText(text);
		user.setFont(new Font("Kristen ITC", Font.BOLD, 35));
		user.setForeground(Color.orange);
		user.setBackground(new Color(255,255,230));
		user.setBorder(new EmptyBorder(-3,-3,-3,-3));
		topPanel.add(user);

		homeScreenPanel.add(Box.createRigidArea(new Dimension(0,30)));
		
		btnSnake=new JButton("Snake Game");
		btnSnake.setFont(new Font("Kristen ITC", Font.BOLD, 70));
		btnSnake.setMaximumSize(new Dimension(700, 120));
		btnSnake.setBackground(Color.orange);
		btnSnake.setForeground(Color.white);
		btnSnake.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSnake.addActionListener(this);
		homeScreenPanel.add(btnSnake);
		
		homeScreenPanel.add(Box.createRigidArea(new Dimension(0,20)));
		
		btnFlappyBird=new JButton("Flappy Bird");
		btnFlappyBird.setFont(new Font("Kristen ITC", Font.BOLD, 70));
		btnFlappyBird.setMaximumSize(new Dimension(700, 120));
		btnFlappyBird.setBackground(Color.orange);
		btnFlappyBird.setForeground(Color.white);
		btnFlappyBird.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnFlappyBird.addActionListener(this);
		homeScreenPanel.add(btnFlappyBird);
		
		homeScreenPanel.add(Box.createRigidArea(new Dimension(0,40)));
		
		secondPanel=new JPanel();
		secondPanel.setLayout(new BoxLayout(secondPanel, BoxLayout.X_AXIS));
		secondPanel.setPreferredSize(new Dimension(1000,60));
		secondPanel.setMaximumSize(secondPanel.getPreferredSize());
		secondPanel.setBackground(Color.orange);
		secondPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		homeScreenPanel.add(secondPanel);
		
		secondPanel.add(Box.createRigidArea(new Dimension(50,0)));
		
		btnHighScore=new JButton("View High Scores");
		btnHighScore.setFont(new Font("Kristen ITC", Font.BOLD, 30));
		btnHighScore.setBackground(Color.orange);
		btnHighScore.setForeground(Color.white);
		btnHighScore.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnHighScore.setHorizontalAlignment(JButton.CENTER);
		btnHighScore.addActionListener(this);
		secondPanel.add(btnHighScore);
		
		secondPanel.add(Box.createRigidArea(new Dimension(400,0)));
		
		btnLogOut=new JButton("Log Out");
		btnLogOut.setFont(new Font("Kristen ITC", Font.BOLD, 30));
		btnLogOut.setBackground(Color.orange);
		btnLogOut.setForeground(Color.white);
		btnLogOut.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLogOut.setHorizontalAlignment(JButton.CENTER);
		btnLogOut.addActionListener(this);
		secondPanel.add(btnLogOut);
		
		Main.frame.add(homeScreenPanel);
		Main.frame.setVisible(true);
//============================================================================================================
//finish screen setup		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		//open screen for snake game
		if (e.getSource()==btnSnake){
			Main.frame.getContentPane().removeAll();
			Main.frame.repaint();
			new SnakeGame();
			
		//open screen for flappy bird
		}else if(e.getSource()==btnFlappyBird){
			Main.frame.getContentPane().removeAll();
			Main.frame.repaint();
			new FlappyBird();
			
		//open high score screen
		}else if(e.getSource()==btnHighScore){
			Main.frame.getContentPane().removeAll();
			Main.frame.repaint();
			new HighScore();
		
		//open sign up and log in screen
		}else if(e.getSource()==btnLogOut){
			Main.frame.getContentPane().removeAll();
			Main.frame.repaint();
			new SignIn();
		}
	}
}
