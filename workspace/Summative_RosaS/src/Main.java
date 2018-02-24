/* Author: Rosa Sun
 * Date: 2017-06-19
 * Description: Main method for Sun's Arcade
 * Title: Sun's Arcade
*/
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;


public class Main {
	public static JFrame frame;
	public JPanel signInPanel=new JPanel();
	public static final String LOGO_PATH="imgs/logo_1.png";
	public UIManager UI;
	
	public static void main(String[] args){
		new Main();
	}
	
	public Main(){
		
		//initializing the JFrame that will contain all the JPanels from each class
		frame=new JFrame("Sun's Arcade");
		frame.setResizable(false);
		frame.setSize(1000,600);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//UIManager will take care of the design of all the OptionPanes later on from each class-
			//I won't have to set up each OptionPane one by one
		UI=new UIManager();
		UI.put("OptionPane.background", new Color(255,255,190));
		UI.put("Panel.background", new Color(255,255,190));
		UI.put("OptionPane.messageFont", new Font("Kristen ITC", Font.PLAIN, 20));
		UI.put("OptionPane.buttonFont", new Font("Kristen ITC", Font.PLAIN, 20));
		
		//call SignIn() - will show the login screen
		new SignIn();
		
		//focusable must be true in order to dispatch KeyEvents (used in flappy bird and snake)
		Main.frame.setFocusable(true);
	}
}