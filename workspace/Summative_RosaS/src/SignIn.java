/* Author: Rosa Sun
 * Date: 2017-06-19
 * Description: This class creates the sign up and log in screen for Sun's Arcade
*/
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class SignIn implements ActionListener{
	public JPanel signInPanel, usernamePanel, passwordPanel, buttonsPanel,topPanel, filler;
	private JTextField username, password;
	private JButton btnLogIn, btnSignUp;
	private JLabel lblLogo,lblUsername,lblPassword, lblOr, lblTitle;
	private static String[] userInfo=new String[5];
	private boolean signingUp=false;
	public static List<List<String>> allUsers = new ArrayList<List<String>>();
	
	public SignIn(){
		updateAllUsers();

//setting up screen...
//=========================================================================================        
		signInPanel=new JPanel();
		signInPanel.setLayout(new BoxLayout(signInPanel, BoxLayout.Y_AXIS));
		signInPanel.setSize(1000,600);
		signInPanel.setBackground(new Color(255,255,190));
		
		topPanel=new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setBackground(null);
		topPanel.setPreferredSize(new Dimension(1000,180));
		topPanel.setMaximumSize(topPanel.getPreferredSize());
		topPanel.setBackground(new Color(255,255,230));
		signInPanel.add(topPanel);
		
		lblLogo = new JLabel(new ImageIcon(new javax.swing.ImageIcon(getClass()
				.getResource(Main.LOGO_PATH)).getImage().getScaledInstance(450,
				170, Image.SCALE_SMOOTH)));
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblLogo.setBorder(new EmptyBorder(0,270,0,10));
		lblLogo.setHorizontalAlignment(JLabel.CENTER);
		topPanel.add(lblLogo);
		
		filler=new JPanel();
		filler.setPreferredSize(new Dimension(3,topPanel.getHeight()));
		filler.setMaximumSize(topPanel.getPreferredSize());
		filler.setBackground(Color.orange);
		filler.setVisible(false);
		topPanel.add(filler);
		
		lblTitle=new JLabel("New User Registration");
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitle.setFont(new Font("Kristen ITC", Font.BOLD, 38));
		lblTitle.setForeground(Color.orange);
		lblTitle.setBorder(new EmptyBorder(0,30,0,30));
		lblTitle.setVisible(false);
		topPanel.add(lblTitle);
		
		usernamePanel=new JPanel();
		usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
		usernamePanel.setBackground(null);
		usernamePanel.setBorder(new EmptyBorder(20,0,20,0));
		signInPanel.add(usernamePanel);
		
		lblUsername=new JLabel("Username:");
		lblUsername.setFont(new Font("Kristen ITC", Font.BOLD, 50));
		lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblUsername.setBorder(new EmptyBorder(0,0,0,20));
		lblUsername.setForeground(Color.ORANGE);
		usernamePanel.add(lblUsername);
		
		username=new JTextField();
		username.addKeyListener(new java.awt.event.KeyAdapter() { //username TextField only accepts chars
	        public void keyTyped(java.awt.event.KeyEvent evt) {
	        	char c=evt.getKeyChar();
	            if(!(Character.isAlphabetic(c) ||  (c==KeyEvent.VK_BACK_SPACE)||  c==KeyEvent.VK_DELETE )){
	                evt.consume();
	            }
	        }
		});
		username.setFont(new Font("Kristen ITC", Font.BOLD, 40));
		username.setMaximumSize(new Dimension(400, username.getPreferredSize().height+10));
		usernamePanel.add(username);
		
		passwordPanel=new JPanel();
		passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
		passwordPanel.setBackground(null);
		passwordPanel.setBorder(new EmptyBorder(0,0,30,0));
		signInPanel.add(passwordPanel);
		
		lblPassword=new JLabel("Password:");
		lblPassword.setFont(new Font("Kristen ITC", Font.BOLD, 50));
		lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblPassword.setBorder(new EmptyBorder(0,0,0,20));
		lblPassword.setForeground(Color.ORANGE);
		passwordPanel.add(lblPassword);
		
		password=new JPasswordField();
		password.addKeyListener(new java.awt.event.KeyAdapter() {//password TextField only accepts chars
	        public void keyTyped(java.awt.event.KeyEvent evt) {
	        	char c=evt.getKeyChar();
	            if(!(Character.isAlphabetic(c) ||  (c==KeyEvent.VK_BACK_SPACE)||  c==KeyEvent.VK_DELETE )){
	                evt.consume();
	            }
	        }
		});
		password.setFont(new Font("Kristen ITC", Font.BOLD, 40));
		password.setMaximumSize(new Dimension(400, password.getPreferredSize().height+10));
		passwordPanel.add(password);
				
		buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setBackground(null);
		buttonsPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		buttonsPanel.setBorder(new EmptyBorder(0,0,20,0));
		signInPanel.add(buttonsPanel);
		
		btnLogIn=new JButton("Login");
		btnLogIn.setFont(new Font("Kristen ITC", Font.BOLD, 50));
		btnLogIn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLogIn.setBackground(Color.orange);
		btnLogIn.setHorizontalTextPosition(JButton.CENTER);
		btnLogIn.setVerticalTextPosition(JButton.CENTER);
		btnLogIn.setForeground(Color.white);
		btnLogIn.addActionListener(this);
		buttonsPanel.add(btnLogIn);
		
		lblOr=new JLabel("OR");
		lblOr.setFont(new Font("Kristen ITC", Font.BOLD, 40));
		lblOr.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblOr.setBorder(new EmptyBorder(0,0,0,20));
		lblOr.setForeground(Color.ORANGE);
		lblOr.setBorder(new EmptyBorder(0,25,0,25));
		buttonsPanel.add(lblOr);

		btnSignUp=new JButton("<html><span style=\"font-size:13px;text-align: center;\">Don't have an account?</span><br />Click here to sign up!</html>");
		btnSignUp.setFont(new Font("Kristen ITC", Font.BOLD, 35));
		btnSignUp.setMaximumSize(new Dimension(500,100));
		btnSignUp.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSignUp.setBackground(Color.orange);
		btnSignUp.setHorizontalTextPosition(JButton.CENTER);
		btnSignUp.setVerticalTextPosition(JButton.CENTER);
		btnSignUp.setForeground(Color.white);
		btnSignUp.addActionListener(this);
		buttonsPanel.add(btnSignUp);
		
		Main.frame.add(signInPanel);
		Main.frame.setVisible(true);
//============================================================================================================
//finish screen setup
	}
	
	//method retrieves data from text file and saves them in a 2D arrayList of Strings
	public static void updateAllUsers(){
		allUsers.clear();
		BufferedReader bufferedReader;
		try {
            bufferedReader = new BufferedReader(new FileReader("users.txt"));
            String line;
            //read through each line of csv file and append them to allUsers[][]
            while((line = bufferedReader.readLine()) != null) {
            	allUsers.add(Arrays.asList(line.split(",")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	//method sorts the arrayList of allUsers[][] based on the total scores (index 2)
	public static void sortTotalScore(){
		// sorting algorithm (bubble sort)
		// |
		// |
		// |
		// V
        for (int i = 0; i < allUsers.size() - 1; i++)
        {
            int index = i;
            for (int j = i + 1; j < allUsers.size(); j++)
                if (Integer.parseInt(allUsers.get(j).get(2)) < Integer.parseInt(allUsers.get(index).get(2))){
                    index = j;
                }
            List<String> smallerNumber = allUsers.get(index);
            allUsers.set(index, allUsers.get(i));
            allUsers.set(i, smallerNumber);
        }
	}
	
	//checks if the username the user entered on the Sign Up screen is unique
	public boolean isUsernameUnique(){
		String s=username.getText();
		boolean usernameIsUnique = true;
        for (int i=0;i<allUsers.size();i++){
        	if (allUsers.get(i).get(0).equals(s)){
        		usernameIsUnique=false;
           		break;
        	}
        }
        return usernameIsUnique;
	}

	//checks if password matches the username on the log in screen
	public boolean doesPasswordMatch(){
		boolean doesMatch=false;
		String u=username.getText();
		String p=password.getText();
		//loops through 2D arrayList containing each user's information, stop at the corresponding username and compare
		//the string from the arrayList and the password entered
        for (int i=0;i<allUsers.size();i++){
        	if(allUsers.get(i).get(0).equals(u) && allUsers.get(i).get(1).equals(p)){
        		doesMatch=true;
        		userInfo=(String[]) allUsers.get(i).toArray();
        		break;
        	}
        }
        return doesMatch;
	}
	
	//this method registers new user by adding their username, password, and scores (all 0s) to the csv file
	public void registerNewUser(){
		String[] temp={username.getText(),password.getText(),"0","0","0"};
		allUsers.add(Arrays.asList(temp));
		System.out.println(Arrays.toString(temp));
		try
		{
		    String filename= "users.txt";
		    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
		    fw.write("\n"+Arrays.toString(temp).replace("[", "").replace("]", "").replace(" ",""));//appends the string to the file
		    fw.close();
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException: " + ioe.getMessage());
		}
	}
	
	//this method sets up the login screen - will be called on when back btn is pressed
	public void setUpLogInScreen(){
		lblLogo.setBorder(new EmptyBorder(0,270,0,10));
		filler.setVisible(false);
		lblTitle.setVisible(false);
		username.setText(null);
		password.setText(null);
		btnSignUp.setFont(new Font("Kristen ITC", Font.BOLD, 35));
		btnSignUp.setText("<html><span style=\"font-size:13px;text-align: center;\">Don't have an account?</span><br />Click here to sign up!</html>");
		btnLogIn.setText("Login");
		signingUp=false;
	}
	
	//this method sets up the sign up screen - will be called on when sign in btn is pressed
	public void setUpSignUpScreen(){
		lblLogo.setBorder(new EmptyBorder(0,0,0,10));
		btnLogIn.setText("Back");
		lblOr.setText("  ");;
		filler.setVisible(true);
		lblTitle.setVisible(true);
		password.setText(null);
		username.setText(null);
		btnSignUp.setText("Sign Up");
		btnSignUp.setFont(new Font("Kristen ITC", Font.BOLD, 50));
		signingUp=true;
	}
	
	//this method is static and will be called from other classes
	//method adds a certain to the total score of the user
	public static void addToTotal(int s, String[] oldUserInfo){
		userInfo[2]=Integer.toString(Integer.parseInt(userInfo[2])+s);
		try {
			updateLine(Arrays.toString(oldUserInfo).replace("[", "").replace("]", "").replace(" ",""), Arrays.toString(userInfo).replace("[", "").replace("]", "").replace(" ",""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//almost the same as the previous method but with different parameters - this one will be called from other 
	//screens while the method above will be called from the current screen
	public static void addToTotal(int s){
		String[] oldUserInfo=userInfo.clone();
		userInfo[2]=Integer.toString(Integer.parseInt(userInfo[2])+s);
		try {
			updateLine(Arrays.toString(oldUserInfo).replace("[", "").replace("]", "").replace(" ",""), Arrays.toString(userInfo).replace("[", "").replace("]", "").replace(" ",""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getUsername(){
		return userInfo[0];
	}
	
	public static int getTotalScore(){
		return Integer.parseInt(userInfo[2]);
	}
	
	public static int getSnakeScore(){
		return Integer.parseInt(userInfo[3]);
	}
	
	public static int getBirdScore(){
		return Integer.parseInt(userInfo[4]);
	}
	
	public static void setSnakeScore(int s){
		String[] oldUserInfo=userInfo.clone();
		userInfo[3]=Integer.toString(s);
		addToTotal(s, oldUserInfo);
	}
	
	public static void setBirdScore(int s){
		String[] oldUserInfo=userInfo.clone();
		userInfo[4]=Integer.toString(s);
		addToTotal(s, oldUserInfo);
	}
	
	public static int getAllUsersSize(){
		return allUsers.size();
	}
	
	//updateLine() updates a specific chunk of the csv file
	private static void updateLine(String toUpdate, String updated) throws IOException {
	    BufferedReader file = new BufferedReader(new FileReader("users.txt"));
	    String line;
	    String input = "";
	    while ((line = file.readLine()) != null)
	        input += line + System.lineSeparator();
	    
	    input = input.replace(toUpdate, updated);
	    input =input.substring(0, input.length()-2);

	    FileOutputStream os = new FileOutputStream("users.txt");
	    os.write(input.getBytes());

	    file.close();
	    os.close();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==btnSignUp){
			if (signingUp){
				if(username.getText().equals("")||password.getText().equals("")){
					JOptionPane.showMessageDialog(Main.frame,"Please do not leave any of the fields blank.","Warning",
						    JOptionPane.WARNING_MESSAGE);
				}else{
					if(isUsernameUnique()){
						registerNewUser();
						setUpLogInScreen();
					}else{
						JOptionPane.showMessageDialog(Main.frame,"Username is already taken. Please use another username.","Warning",
							    JOptionPane.WARNING_MESSAGE);
					}
				}
				
			}else{
				setUpSignUpScreen();
			}
		}if (e.getSource()==btnLogIn){
			if (signingUp){
				setUpLogInScreen();
			}else{
				if(password.getText().equals("")||username.getText().equals("")){
					JOptionPane.showMessageDialog(Main.frame,"Please do not leave any of the fields blank.","Warning",
						    JOptionPane.WARNING_MESSAGE);
				}else{
					if(!doesPasswordMatch()){
						JOptionPane.showMessageDialog(Main.frame,"You have not entered the correct username or password.","Warning",
							    JOptionPane.WARNING_MESSAGE);
						username.setText(null);
						password.setText(null);
					}else if(doesPasswordMatch()){
						Main.frame.getContentPane().removeAll();
						Main.frame.repaint();
						new HomeScreen();
					}
				}
			}
			
		}
	}
}
