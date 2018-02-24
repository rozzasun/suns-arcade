/* Author: Rosa Sun
 * Date: 2017-06-19
 * Description: This class generates the screen listing the best scores of the individual player as well as all the users
*/
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class HighScore implements ActionListener{

	private JPanel highScorePanel, topPanel, filler, userPanel, allPanel, mainPanel;
	private JLabel lblLogo, lblTitle;
	private JLabel[] scores=new JLabel[3];
	private JLabel lblUsername, lblLeaders;
	private JTable leaderBoardTable;
	private JButton back;

	public HighScore(){
//setting up screen...
//===================================================================================================        		
		highScorePanel=new JPanel();
		highScorePanel.setLayout(new BoxLayout(highScorePanel, BoxLayout.Y_AXIS));
		highScorePanel.setSize(1000,600);
		highScorePanel.setBackground(new Color(255,255,190));
		
		topPanel=new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setBackground(null);
		topPanel.setPreferredSize(new Dimension(1000,180));
		topPanel.setMaximumSize(topPanel.getPreferredSize());
		topPanel.setBackground(new Color(255,255,230));
		highScorePanel.add(topPanel);
		
		lblLogo = new JLabel(new ImageIcon(new javax.swing.ImageIcon(getClass()
				.getResource(Main.LOGO_PATH)).getImage().getScaledInstance(450,
				170, Image.SCALE_SMOOTH)));
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblLogo.setHorizontalAlignment(JLabel.CENTER);
		lblLogo.setBorder(new EmptyBorder(0,40,0,40));
		topPanel.add(lblLogo);
		
		filler=new JPanel();
		filler.setPreferredSize(new Dimension(3,topPanel.getHeight()));
		filler.setMaximumSize(topPanel.getPreferredSize());
		filler.setBackground(Color.orange);
		topPanel.add(filler);
		
		lblTitle=new JLabel("Leaderboard");
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitle.setFont(new Font("Kristen ITC", Font.BOLD, 50));
		lblTitle.setForeground(Color.orange);
		lblTitle.setBorder(new EmptyBorder(0,80,0,80));
		topPanel.add(lblTitle);
		
		mainPanel=new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.setBackground(new Color(255,255,190));
		highScorePanel.add(mainPanel);
		
		mainPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		
		userPanel=new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
		userPanel.setBackground(new Color(255,255,190));
		userPanel.setBorder(new EmptyBorder(0,20,0,20));
		mainPanel.add(userPanel);

		userPanel.add(Box.createRigidArea(new Dimension(0,20)));
		
		allPanel=new JPanel();
		allPanel.setLayout(new BoxLayout(allPanel, BoxLayout.Y_AXIS));
		allPanel.setBackground(null);
		mainPanel.add(allPanel);

		
		lblUsername=new JLabel("USERNAME: " + SignIn.getUsername());
		lblUsername.setBackground(Color.orange);
		lblUsername.setOpaque(true);
		lblUsername.setFont(new Font("Kristen ITC", Font.BOLD, 30));
		lblUsername.setForeground(Color.white);
		lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblUsername.setBorder(new EmptyBorder(2,2,2,2));
		userPanel.add(lblUsername);
		
		userPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		JPanel container=new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBackground(new Color(255,240,180));
		container.setAlignmentX(Component.CENTER_ALIGNMENT);
		userPanel.add(container);
		
		//scores[] contain labels showing the total score and highest scores for both games
		scores[0]=new JLabel("Total Score: "+SignIn.getTotalScore());
		scores[1]=new JLabel("Snake Game High Score: "+SignIn.getSnakeScore());
		scores[2]=new JLabel("Flappy Bird High Score: "+SignIn.getBirdScore());
		for (int i=0;i<3;i++){
			scores[i].setFont(new Font("Kristen ITC", Font.BOLD, 30));
			scores[i].setOpaque(true);
			scores[i].setForeground(Color.orange);
			scores[i].setBorder(new EmptyBorder(10,0,10,0));
			scores[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			scores[i].setBackground(new Color(255,240,180));
			container.add(scores[i]);
		}
		
		userPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		back=new JButton("BACK");
		back.setFont(new Font("Kristen ITC",Font.BOLD, 40));
		back.setBackground(Color.orange);
		back.setForeground(Color.white);
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		back.setHorizontalAlignment(JButton.CENTER);
		back.addActionListener(this);
		userPanel.add(back);
		
		mainPanel.add(Box.createRigidArea(new Dimension(35, 0)));
		
		lblLeaders=new JLabel("Points Leaders");
		lblLeaders.setBackground(Color.orange);
		lblLeaders.setOpaque(true);
		lblLeaders.setFont(new Font("Kristen ITC", Font.BOLD, 30));
		lblLeaders.setForeground(Color.white);
		lblLeaders.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblLeaders.setBorder(new EmptyBorder(2,2,2,2));
		allPanel.add(lblLeaders);
		
		allPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		//setting up table displaying the data retrieved from the txt file
		SignIn.updateAllUsers();
		SignIn.sortTotalScore();
		String[] columnNames = {"Rank", "Username", "Score"};
		Object[][] data=new Object[6][3];
		data[0][0]="RANK";
		data[0][1]="USERNAME";
		data[0][2]="SCORE";
		for (int i=1;i<=5;i++){
			data[i][0]=i;
			data[i][1]=SignIn.allUsers.get(SignIn.getAllUsersSize()-i).get(0);
			data[i][2]=SignIn.allUsers.get(SignIn.getAllUsersSize()-i).get(2);
		}

		leaderBoardTable=new JTable(data, columnNames);
		leaderBoardTable.setFont(new Font("Kristen ITC", Font.PLAIN,20));
		leaderBoardTable.setColumnSelectionAllowed(false);
		leaderBoardTable.setGridColor(Color.orange);
		leaderBoardTable.setRowHeight(40);
		leaderBoardTable.setBackground(new Color(255,255,230));
		leaderBoardTable.setForeground(Color.orange);
		leaderBoardTable.setEnabled(false);
		leaderBoardTable.setBorder(new EmptyBorder(10,10,10,10));
		allPanel.add(leaderBoardTable);
		mainPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		
		Main.frame.add(highScorePanel);
		Main.frame.setVisible(true);
//============================================================================================================
//finish screen setup
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==back){
			//open home screen when back button is pressed
			Main.frame.getContentPane().removeAll();
			Main.frame.repaint();
			new HomeScreen();
		}
	}
}
