import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class controllerMainFrame implements ActionListener,ChangeListener {
	//PROPERTIES
	JFrame theframe = new JFrame("Connect 4");
	int intwinReceived = 0;
	String strWinner;
	boolean blnDisconnect = true;
	
	//Screens
	C4GameplayScreen gameplaypanel = new C4GameplayScreen();
	C4HelpScreen helppanel = new C4HelpScreen();
	C4StartMenu startMenu;
	C4WinnerLoserScreen winnerLoserScreen = new C4WinnerLoserScreen();
	C4ThemeSelectionScreen themeSelectionScreen = new C4ThemeSelectionScreen();

	//Networking Properties
	//boolean isServer;
	//SuperSocketMaster ssm;
	//temporary hardcode, may in configuration file.
	int port = 6112;
	
	//METHODS
	public void changeTheme(String strTheme){
		try{
			PrintWriter txtTheme = new PrintWriter(new FileWriter("themes.txt", false));

			String strFileData = "Original,OriginalBG.jpg,OriginalBoard.png,P1original.png,P2original.png\nChristmas,ChristmasBG.jpg,ChristmasBoard.png,P1christmas.png,P2christmas.png\nEaster,EasterBG.jpg,EasterBoard.png,P1easter.png,P2easter.png\n";
			if(strTheme.equalsIgnoreCase("Christmas")){
				txtTheme.println(strFileData + "Current,christmas");
			}else if(strTheme.equalsIgnoreCase("Original")){
				txtTheme.println(strFileData + "Current,original");
			}else if(strTheme.equalsIgnoreCase("Easter")){
				txtTheme.println(strFileData + "Current,easter");
			}
			txtTheme.close();
		}catch(IOException e){
			System.out.println("File not found");
		}
		startMenu.currentTheme.setText("Current Theme: " + strTheme);
	}
	public String getTheme(){
		try{
			BufferedReader txtTheme = new BufferedReader(new FileReader("themes.txt"));
			while(true){
				String[] strLine = txtTheme.readLine().split(",");
				if(strLine[0].equals("Current")) {
					return strLine[1];
				}
			}
		}catch (IOException e){
			System.out.println("File Not Found");
		}
		return "f";
	}
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource() == startMenu.serverButton){
			startMenu.serverButton.setEnabled(false); 
			startMenu.clientButton.setVisible(false);
			gameplaypanel.isServer = true;
			startMenu.ipAddress.setEnabled(false);
			startMenu.userName.setEnabled(false);
			//startMenu.statusLabel.setText("Server connected");
			gameplaypanel.ssm = new SuperSocketMaster(port, this);
			gameplaypanel.prefix = "Server";
			gameplaypanel.intTurn = 1;
		    boolean gotConnect = gameplaypanel.ssm.connect();
		    if(gotConnect){
		    	System.out.println("got connection!");
		    	startMenu.statusLabel.setText("Server Start");
				startMenu.menuBar.setVisible(true);
			}else{
		    	startMenu.statusLabel.setText("Server Start failed");
		    }
		      
		 }else if(evt.getSource() == startMenu.clientButton) {
			 gameplaypanel.ssm = new SuperSocketMaster(startMenu.ipAddress.getText(), port, this);
			 boolean gotConnect = gameplaypanel.ssm.connect();
			 if(gotConnect){
			     startMenu.statusLabel.setText("Server connected.");
				 gameplaypanel.prefix = "Client";
				 gameplaypanel.intTurn = 2;
				 startMenu.clientButton.setEnabled(false);
				 startMenu.serverButton.setVisible(false);
				 gameplaypanel.isServer = false;
				 startMenu.ipAddress.setEnabled(false);
				 startMenu.userName.setEnabled(false);
				 startMenu.menuBar.setVisible(false);
				 ///
				 startMenu.currentTheme.setText("Current Theme: " + getTheme().substring(0, 1).toUpperCase() + getTheme().substring(1));

			 }else {
				 startMenu.statusLabel.setText("Server is unavilable.");
				 startMenu.clientButton.setEnabled(true);
				 startMenu.ipAddress.setEnabled(true);
			 }
			    
		 }else if(evt.getSource() == startMenu.playButton) {
			 try{
				gameplaypanel.strTheme = gameplaypanel.LoadTheme();
				gameplaypanel.strThemeElements = gameplaypanel.LoadBG();
				gameplaypanel.strBackgroundFile = gameplaypanel.strThemeElements[1];
				gameplaypanel.strBoardFile = gameplaypanel.strThemeElements[2];
				gameplaypanel.strP1File = gameplaypanel.strThemeElements[3];
				gameplaypanel.strP2File = gameplaypanel.strThemeElements[4];

				gameplaypanel.thebackground = ImageIO.read(this.getClass().getResourceAsStream(gameplaypanel.strBackgroundFile));
				gameplaypanel.theboard = ImageIO.read(this.getClass().getResourceAsStream(gameplaypanel.strBoardFile));
				gameplaypanel.player1piece = ImageIO.read(this.getClass().getResourceAsStream(gameplaypanel.strP1File));
				gameplaypanel.newgamepiece.player1piece = gameplaypanel.player1piece;
				gameplaypanel.player2piece = ImageIO.read(this.getClass().getResourceAsStream(gameplaypanel.strP2File));
				gameplaypanel.newgamepiece.player2piece = gameplaypanel.player2piece;
			 
			}catch(IOException e){
				System.out.println("Error loading image");
			}

			 if(startMenu.ipAddress.getText() != null && startMenu.userName.getText() != null 
					 && !startMenu.ipAddress.getText().isEmpty() && !startMenu.userName.getText().isEmpty() 
					 && (!startMenu.serverButton.isVisible() || !startMenu.clientButton.isVisible())) {
				 gameplaypanel.setPreferredSize(new Dimension(1280, 720));
				 gameplaypanel.strUsername = startMenu.userName.getText();
				 theframe.setContentPane(gameplaypanel);
				 theframe.pack();
			 }
			 gameplaypanel.intTurn = 1;
			    
		 }else if(evt.getSource() == startMenu.helpButton) {
			 helppanel.setPreferredSize(new Dimension(1280, 720));
			 theframe.setContentPane(helppanel);
			 theframe.pack();
		 }

		//Menu Items
		else if(evt.getSource() == startMenu.menuItemChristmas){
			changeTheme("Christmas");
			gameplaypanel.ssm.sendText("theme,Christmas");
		}else if(evt.getSource() == startMenu.menuItemOriginal){
			changeTheme("Original");
			gameplaypanel.ssm.sendText("theme,Original");
		}else if(evt.getSource() == startMenu.menuItemEaster){
			changeTheme("Easter");
			gameplaypanel.ssm.sendText("theme,Easter");
		}else if(evt.getSource() == startMenu.menuItemTheme){
			themeSelectionScreen.setPreferredSize(new Dimension(1280, 720));
			theframe.setContentPane(themeSelectionScreen);
			theframe.pack();
		}

		//C4HelpScreen.java
		else if(evt.getSource() == helppanel.backbutton) {
			startMenu.setPreferredSize(new Dimension(1280, 720));
			theframe.setContentPane(startMenu);
			theframe.pack();
		 }
		 
		//C4WinnerLoserScreen.java
		else if(evt.getSource() == winnerLoserScreen.playAgainButton){
			blnDisconnect = false;
			gameplaypanel.ssm.sendText("disconnect"+","+blnDisconnect);
			gameplaypanel.blnHasWon	= false;
			gameplaypanel.setPreferredSize(new Dimension(1280, 720));
			theframe.setContentPane(gameplaypanel);
			theframe.pack();
			//reset the board
			gameplaypanel.arrayboard = new moduleBackendBoard();
			blnDisconnect = true;
			//NEED TO RESET TURNS AND CONNECT PIECES
			intwinReceived = 0;
			gameplaypanel.blnHoldingPiece = false;
			gameplaypanel.blnDroppedPiece = false;
			gameplaypanel.blnPlayedPiece = false;
			gameplaypanel.blnInRange = false;
			gameplaypanel.blnHasWon = false;
			gameplaypanel.blnHasLost = false;
			if(gameplaypanel.prefix.equalsIgnoreCase("client")){
				gameplaypanel.intTurn = 2;
			}else if(gameplaypanel.prefix.equalsIgnoreCase("server")){
				gameplaypanel.intTurn = 1;
			}
		}else if(evt.getSource() == winnerLoserScreen.disconnectButton){
			blnDisconnect = true;
			gameplaypanel.ssm.sendText("disconnect"+","+blnDisconnect);
			gameplaypanel.blnHasWon	= false;
			startMenu.setPreferredSize(new Dimension(1280, 720));
			theframe.setContentPane(startMenu);
			theframe.pack();
			gameplaypanel.ssm.disconnect();
			//resetting the start menu
			startMenu.statusLabel.setText("");
			startMenu.clientButton.setEnabled(true); 
			startMenu.serverButton.setVisible(true);
			startMenu.ipAddress.setEnabled(true);
			startMenu.userName.setEnabled(true);
			startMenu.menuBar.setVisible(true);
			startMenu.clientButton.setVisible(true);
			startMenu.serverButton.setEnabled(true); 
			//reset the board
			gameplaypanel.arrayboard = new moduleBackendBoard();
			blnDisconnect = true;
			//NEED TO RESET TURNS AND CONNECT PIECES
			intwinReceived = 0;
			gameplaypanel.blnHoldingPiece = false;
			gameplaypanel.blnDroppedPiece = false;
			gameplaypanel.blnPlayedPiece = false;
			gameplaypanel.blnInRange = false;
			gameplaypanel.blnHasWon = false;
			gameplaypanel.blnHasLost = false;
			if(gameplaypanel.prefix.equalsIgnoreCase("client")){
				gameplaypanel.intTurn = 2;
			}else if(gameplaypanel.prefix.equalsIgnoreCase("server")){
				gameplaypanel.intTurn = 1;
			}
		}

		//C4ThemeSelectionScreen.java
		else if(evt.getSource() == themeSelectionScreen.christmasButton){
			changeTheme("Christmas");
			gameplaypanel.ssm.sendText("theme,Christmas");
		}else if(evt.getSource() == themeSelectionScreen.originalButton){
			changeTheme("Original");
			gameplaypanel.ssm.sendText("theme,Original");
		}else if(evt.getSource() == themeSelectionScreen.easterButton){
			changeTheme("Easter");
			gameplaypanel.ssm.sendText("theme,Easter");
		}else if(evt.getSource() == themeSelectionScreen.backButton){
			startMenu.setPreferredSize(new Dimension(1280, 720));
			theframe.setContentPane(startMenu);
			theframe.pack();

		//Gameplay SSM
		}else if(evt.getSource() == gameplaypanel.ssm) {
			String strMessage = "";
	    	String ssmText = gameplaypanel.ssm.readText();
	    	String[] textArray = ssmText.split(",");
	    	if(textArray.length>0) {
	    		System.out.println("ssm event from "+textArray[0]);
	    		if(textArray[0].equals("Server")) {
	    			System.out.println("!!! event from server");
	    			if(!gameplaypanel.isServer) {
	    				gameplaypanel.intTurn = 2;

	    				gameplaypanel.blnDroppedPiece = true;
	    				gameplaypanel.intColumnDropped = Integer.parseInt(textArray[1]);
	    			}
	    			//ConnectPiece newgamepiece = new ConnectPiece();
	    			setConnectPiece();
	    			//gameplaypanel.intTurn = 2;
	    			System.out.println("!!! event from server, intTurn is "+gameplaypanel.intTurn);
	    		}else if(textArray[0].equals("Client")) {

	    			if(gameplaypanel.isServer) {
	    				gameplaypanel.intTurn = 1;
	    				gameplaypanel.blnDroppedPiece = true;
	    				gameplaypanel.intColumnDropped = Integer.parseInt(textArray[1]);
	    			}
	    			//ConnectPiece newgamepiece = new ConnectPiece();
	    			setConnectPiece();
	    			//gameplaypanel.intTurn = 1;
	    		}else if (textArray[0].equals("theme")){
					if(textArray[1].equals("Easter")){
						changeTheme("Easter");
						startMenu.currentTheme.setText("Current Theme: Easter");
					}else if(textArray[1].equals("Christmas")){
						changeTheme("Christmas");
						startMenu.currentTheme.setText("Current Theme: Christmas");
					}else if(textArray[1].equals("Original")){
						changeTheme("Original");
						startMenu.currentTheme.setText("Current Theme: Original");
					}
				}else if (textArray[0].equals("win")){
					strWinner = textArray[2];
					intwinReceived = intwinReceived+1;
					gameplaypanel.blnHasWon = true;
						if(intwinReceived <= 1){
							gameplaypanel.ssm.sendText("win"+","+textArray[1]+","+textArray[2]);
						}
					
				}else if (textArray[0].equalsIgnoreCase("chat")){
					//puts username and chat message into the chat box
					gameplaypanel.chatarea.append(textArray[1]+" says: " + textArray[2]+"\n");
					gameplaypanel.chatarea.setCaretPosition(gameplaypanel.chatarea.getDocument().getLength());
				
				}else if (textArray[0].equalsIgnoreCase("disconnect")&& textArray[1].equalsIgnoreCase("false")){
						gameplaypanel.blnHasWon	= false;
						gameplaypanel.setPreferredSize(new Dimension(1280, 720));
						theframe.setContentPane(gameplaypanel);
						theframe.pack();
						//reset the board
						gameplaypanel.arrayboard = new moduleBackendBoard();
						intwinReceived = 0;
						gameplaypanel.blnHoldingPiece = false;
						gameplaypanel.blnDroppedPiece = false;
						gameplaypanel.blnPlayedPiece = false;
						gameplaypanel.blnInRange = false;
						gameplaypanel.blnHasWon = false;
						gameplaypanel.blnHasLost = false;
						if(gameplaypanel.prefix.equalsIgnoreCase("client")){
							gameplaypanel.intTurn = 2;
						}else if(gameplaypanel.prefix.equalsIgnoreCase("server")){
							gameplaypanel.intTurn = 1;
						}
				}else if (textArray[0].equalsIgnoreCase("disconnect")&& textArray[1].equalsIgnoreCase("true")){
	    			gameplaypanel.blnHasWon	= false;
					startMenu.setPreferredSize(new Dimension(1280, 720));
					theframe.setContentPane(startMenu);
					theframe.pack();
					gameplaypanel.ssm.disconnect();
					//resetting the start menu
					startMenu.statusLabel.setText("");
					startMenu.clientButton.setEnabled(true); 
					startMenu.serverButton.setVisible(true);
					startMenu.ipAddress.setEnabled(true);
					startMenu.userName.setEnabled(true);
					startMenu.menuBar.setVisible(true);
					startMenu.clientButton.setVisible(true);
					startMenu.serverButton.setEnabled(true); 
					//reset the board
					gameplaypanel.arrayboard = new moduleBackendBoard();
					blnDisconnect = true;
					//NEED TO RESET TURNS AND CONNECT PIECES
					intwinReceived = 0;
					gameplaypanel.blnHoldingPiece = false;
					gameplaypanel.blnDroppedPiece = false;
					gameplaypanel.blnPlayedPiece = false;
					gameplaypanel.blnInRange = false;
					gameplaypanel.blnHasWon = false;
					gameplaypanel.blnHasLost = false;
					if(gameplaypanel.prefix.equalsIgnoreCase("client")){
						gameplaypanel.intTurn = 2;
					}else if(gameplaypanel.prefix.equalsIgnoreCase("server")){
						gameplaypanel.intTurn = 1;
					}
				}
	    	}	
		}
		if(gameplaypanel.blnHasWon == true){
			winnerLoserScreen.setPreferredSize(new Dimension(1280, 720));
			theframe.setContentPane(winnerLoserScreen);
			theframe.pack();
			winnerLoserScreen.winnerTextArea.setText("\n             "+strWinner+" Wins!");
			if(gameplaypanel.prefix.equalsIgnoreCase("client")){
				winnerLoserScreen.playAgainButton.setEnabled(false);
				winnerLoserScreen.playAgainButton.setText("Waiting for server action...");
			}else if(gameplaypanel.prefix.equalsIgnoreCase("server")){
				winnerLoserScreen.playAgainButton.setEnabled(true);
				winnerLoserScreen.playAgainButton.setText("Play again");
			}
		}
	}
	
	private void setConnectPiece() {
		//ConnectPiece newgamepiece = new ConnectPiece();
		gameplaypanel.newgamepiece.intColumn = gameplaypanel.intColumnDropped;
		//if(blnDroppedPiece) {
		gameplaypanel.arrayboard.addPosition(gameplaypanel.intColumnDropped);
		gameplaypanel.newgamepiece.intRow = gameplaypanel.arrayboard.intCurrentRow;
		for(int intRows = 5; intRows >= 0; intRows--){
			if(gameplaypanel.arrayboard.intBoard[intRows][gameplaypanel.intColumnDropped] != 0){
				gameplaypanel.newgamepiece.intRow = intRows;
			}
		}
	}
	
	public void stateChanged(ChangeEvent evt){}
	
	//constructor
	public controllerMainFrame(){
		startMenu = new C4StartMenu(getTheme().substring(0, 1).toUpperCase() + getTheme().substring(1));
		theframe = new JFrame("Connect 4");
		startMenu.setPreferredSize(new Dimension(1280, 720));
		startMenu.userName.addActionListener(this);
		startMenu.serverButton.addActionListener(this);
		startMenu.clientButton.addActionListener(this);
		startMenu.playButton.addActionListener(this);
		startMenu.helpButton.addActionListener(this);
		startMenu.themeMenu.addActionListener(this);
		startMenu.menuItemChristmas.addActionListener(this);
		startMenu.menuItemEaster.addActionListener(this);
		startMenu.menuItemOriginal.addActionListener(this);
		startMenu.menuItemTheme.addActionListener(this);
		
		//Action Listener for Winner Loser Screen
		winnerLoserScreen.playAgainButton.addActionListener(this);
		winnerLoserScreen.disconnectButton.addActionListener(this);
		
		//Action Listener for Theme Selection
		themeSelectionScreen.christmasButton.addActionListener(this);
		themeSelectionScreen.originalButton.addActionListener(this);
		themeSelectionScreen.easterButton.addActionListener(this);	
		themeSelectionScreen.backButton.addActionListener(this);
		
		//Action listener for Help Menu
		helppanel.backbutton.addActionListener(this);	
		
		//Boiler Plate Code
		theframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theframe.setVisible(true);
		theframe.setResizable(false);
		
		//Used for Swapping Panels
		theframe.setContentPane(startMenu);
		theframe.pack();
	}
	
	//MAIN PROGRAM
	public static void main(String[] args){
		new controllerMainFrame();
	}
}
