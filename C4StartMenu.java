import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

public class C4StartMenu extends JPanel {
	//PROPERTIES
	//JFrame theFrame;
	JPanel thePanel;
	SuperSocketMaster ssm;
	JLabel welcomeLabel;
	JTextField ipAddress;
	JLabel ipAddressLabel;
	JTextField userName;
	JLabel userNameLabel;
	JLabel statusLabel;;
	JButton serverButton;
	JButton clientButton;
	JButton playButton;
	JButton helpButton;
	JLabel currentTheme;
	BufferedImage theBackGroundImg;

	//Menu Selection
	JMenuBar menuBar = new JMenuBar();
	JMenu themeMenu = new JMenu("----------------------------------------Theme Selection----------------------------------------");
	JMenuItem menuItemChristmas = new JMenuItem("Select: Christmas Theme");
	JMenuItem menuItemOriginal = new JMenuItem("Select: Original Theme");
	JMenuItem menuItemEaster = new JMenuItem("Select: Easter Theme");
	JMenuItem menuItemTheme = new JMenuItem("See Theme Layouts");

	//JFrame theframe1 = new JFrame("C4StartMenu ");
	int intnextpos1 = 0;
	int intnextpos2 = 0;
	Random random = new Random();
	
	//METHODS
	public void paintComponent(Graphics g){		
		g.drawImage(theBackGroundImg, 0, 0, null);
	}
	
	//CONSTRUCTOR
	public C4StartMenu (String strTheme){
		super();
		try{
			theBackGroundImg = ImageIO.read(this.getClass().getResourceAsStream("Connect4BG.jpg"));
		}catch(IOException e){
			System.out.println("Error loading image");
		}
		
		this.setPreferredSize(new Dimension(1280,720));
		this.setLayout(null);
					
		welcomeLabel = new JLabel("Welcome to Connect 4!");
		welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 30));
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setSize(680, 70);
		welcomeLabel.setLocation(300, 50);
		welcomeLabel.setBackground(Color.WHITE);
		this.add(welcomeLabel);
		
		userNameLabel = new JLabel("Enter Your Username:");
		userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userNameLabel.setSize(680, 35);
		userNameLabel.setLocation(300, 150);
		userNameLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		this.add(userNameLabel);
		
		userName = new JTextField();
		userName.setSize(680,35);
		userName.setLocation(300, 190);
		//userName.addActionListener(this);
		this.add(userName);
			
		serverButton = new JButton("Server");
		serverButton.setFont(new Font("Serif", Font.PLAIN, 20));
		serverButton.setHorizontalAlignment(SwingConstants.CENTER);
		serverButton.setSize(300, 40);
		serverButton.setLocation(300, 240);
		this.add(serverButton);
		
		clientButton = new JButton("Client");
		clientButton.setFont(new Font("Serif", Font.PLAIN, 20));
		clientButton.setHorizontalAlignment(SwingConstants.CENTER);
		clientButton.setSize(300, 40);
		clientButton.setLocation(680, 240);
		this.add(clientButton);
			
		ipAddressLabel = new JLabel("Enter IP Address to Play:");
		ipAddressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ipAddressLabel.setSize(680, 35);
		ipAddressLabel.setLocation(300, 300);
		ipAddressLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		this.add(ipAddressLabel);
		
		ipAddress = new JTextField("localhost");
		ipAddress = new JTextField("localhost");
		ipAddress.setSize(680,35);
		ipAddress.setLocation(300, 350);
		this.add(ipAddress);
			
		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statusLabel.setSize(680, 35);
		statusLabel.setLocation(300,385);
		statusLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		this.add(statusLabel);
			
		playButton = new JButton("Play");
		playButton.setFont(new Font("Serif", Font.PLAIN, 20));
		playButton.setHorizontalAlignment(SwingConstants.CENTER);
		playButton.setSize(300, 40);
		playButton.setLocation(300, 430);
		this.add(playButton);
			
		helpButton = new JButton("Help");
		helpButton.setFont(new Font("Serif", Font.PLAIN, 20));
		helpButton.setHorizontalAlignment(SwingConstants.CENTER);
		helpButton.setSize(300, 40);
		helpButton.setLocation(680, 430);
		this.add(helpButton);

		currentTheme = new JLabel("Current Theme: " + strTheme);
		currentTheme.setLocation(500, 570);
		currentTheme.setSize(1000, 40);
		currentTheme.setFont(new Font("Serif", Font.PLAIN, 30));
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(currentTheme);

		themeMenu.add(menuItemChristmas);
		themeMenu.add(menuItemOriginal);
		themeMenu.add(menuItemEaster);
		themeMenu.add(menuItemTheme);
		menuBar.add(themeMenu);
		menuBar.setLocation(0,620);
		menuBar.setSize(1280, 50);
		themeMenu.setFont(new Font("Serif", Font.PLAIN, 40));
		themeMenu.setSize(1280, 50);
		this.add(menuBar);
		menuBar.setVisible(false);
	}
}
