import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.event.*;

public class C4GameplayScreen extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
	//properties
	String strLine = "";
	String strGetTheme[];
	String strThemeElements[];
	String strThemeImages[];
	String strTheme;
	String strBackgroundFile = "";
	String strBoardFile = "";
	String strP1File= "";
	String strP2File= "";
	String strChatMessage;
	String strUsername = "";
	BufferedImage thebackground = null;
	BufferedImage theboard = null;
	BufferedImage player1piece = null;
	BufferedImage player2piece = null;
	Timer thetimer = new Timer(1000/60, this);
	boolean blnHoldingPiece = false;
	boolean blnDroppedPiece = false;
	boolean blnPlayedPiece = false;
	boolean blnInRange = false;
	boolean blnHasWon = false;
	boolean blnHasLost = false;
	ConnectPiece newgamepiece = new ConnectPiece();
	moduleBackendBoard arrayboard = new moduleBackendBoard();
	int intColumnDropped;
	int intMouseX;
	int intMouseY;
	int intTurn;
	
	//Networking Properties
	boolean isServer;
	SuperSocketMaster ssm;
	String prefix;

	//JComponents
	JTextArea chatarea;
	JScrollPane chatscroll;
	JTextField typechat = new JTextField ("Type in chat...");
	
	//methods
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource() == thetimer){
			this.repaint();
		}
		if(evt.getSource() == typechat){
			//gets chat message and username, prints it to your own screen, then sends it over to the others
			strChatMessage = typechat.getText();
			chatarea.append(strUsername+" says: " + strChatMessage+"\n");
			chatarea.setCaretPosition(chatarea.getDocument().getLength());
			ssm.sendText("chat,"+strUsername+","+strChatMessage);
			typechat.setText("");
		}
	}
		
	public void paintComponent(Graphics g){
		//drawing themed images
		g.drawImage(thebackground, 0, 0, null);
		g.drawImage(theboard, 50,50, null);
		if(isServer) {
			g.drawImage(player1piece, 920,480, null);
		}else {
			g.drawImage(player2piece, 1050,480, null);
		}
		
		//drawing the game piece when grabbed
		if(blnHoldingPiece == true){
			newgamepiece.strPlayer1File = strP1File;
			newgamepiece.drawIt(g);
		}
		//drawing the animation
		if(blnDroppedPiece == true){
			newgamepiece.intColumn = intColumnDropped;
			if((newgamepiece.intY < newgamepiece.intRowCoords) && newgamepiece.intRow>0){
				newgamepiece.dropAnimation(g);
				
				
			}else{
				blnDroppedPiece = false;
				newgamepiece.blnStay = true;
				blnInRange = false;
				blnPlayedPiece = true;
			}
			
		}
		//drawing the game piece in the board
		for(int intRow = 0; intRow < 6; intRow++){
				for(int intCol = 0; intCol < 7; intCol++){
					if(arrayboard.intBoard[intRow][intCol] == 1){
						g.drawImage(player1piece, intCol*100+50,intRow*100+50, null);
					}else if(arrayboard.intBoard[intRow][intCol] == 2){
						g.drawImage(player2piece, intCol*100+50,intRow*100+50, null);
					}
				}
			
		}
		
		//turn system
		if (blnPlayedPiece == true && blnHasWon == false){
			if(intTurn == 1){
				blnPlayedPiece = false;
			}else if (intTurn == 2){
				blnPlayedPiece = false;
			}
		}

	}
	//loading theme
	public String LoadTheme(){
		strLine = "";
		try{
			BufferedReader themefile = new BufferedReader(new FileReader("themes.txt"));
			while(strLine != null){
				strLine = themefile.readLine();
				if(strLine != null){
					strGetTheme = strLine.split(",");
					if(strGetTheme[0].equalsIgnoreCase("Current")){
						strTheme = strGetTheme[1];
					}
				}
			}
			themefile.close();
		}catch(IOException e){
			System.out.println("Error loading theme");
		}
		return strTheme;
	}
	//loading files into array based on theme
	public String[] LoadBG(){
		strLine = "";
		try{
			BufferedReader themefile = new BufferedReader(new FileReader("themes.txt"));
			while(strLine != null){
				strLine = themefile.readLine();
				if(strLine != null){
					strThemeElements = strLine.split(",");
					if(strThemeElements[0].equalsIgnoreCase(strTheme)){
						strThemeImages = strThemeElements;
					}
				}
			}
			themefile.close();
		}catch(IOException e){
			System.out.println("Error loading theme images");
		}
		return strThemeImages;
	}
	
	public void mouseMoved(MouseEvent evt){
	}
	public void mouseDragged(MouseEvent evt){
		if((isServer && intTurn==1) || (!isServer && intTurn ==2)) {
		//dragging the gamepiece to the board
		if(blnHoldingPiece == true && blnDroppedPiece == false){
			intMouseX = evt.getX();
			intMouseY = evt.getY();
			//gamepiece will follow the mouse
			newgamepiece.intX = intMouseX;
			newgamepiece.intY = intMouseY;
		}
		}
	}
	public void mouseExited(MouseEvent evt){
	}
	public void mouseEntered(MouseEvent evt){
		
	}
	public void mouseReleased(MouseEvent evt){

		if((isServer && intTurn==1) || (!isServer && intTurn ==2)) {
			if(blnHoldingPiece) {
				blnHoldingPiece = false;
				intMouseX = evt.getX();
				intMouseY = evt.getY();
				//if(blnInRange) {
				//checking which column the piece is dropped in
				if(intMouseX >= 50 && intMouseX < 150){
					intColumnDropped = 0;
					ConnectPiece newgamepiece = new ConnectPiece();
					newgamepiece.intColumn = intColumnDropped;
					blnDroppedPiece = true;
					System.out.println("!!! ssm event send out");
					ssm.sendText(prefix+","+intColumnDropped);
					
				}else if(intMouseX >= 150 && intMouseX < 250){
					intColumnDropped = 1;
					ConnectPiece newgamepiece = new ConnectPiece();
					newgamepiece.intColumn = intColumnDropped;
					blnDroppedPiece = true;
					System.out.println("!!! ssm event send out");
					ssm.sendText(prefix+","+intColumnDropped);
				
				}else if(intMouseX >= 250 && intMouseX < 350){
					intColumnDropped = 2;
					ConnectPiece newgamepiece = new ConnectPiece();
					newgamepiece.intColumn = intColumnDropped;
					blnDroppedPiece = true;
					System.out.println("!!! ssm event send out");
					ssm.sendText(prefix+","+intColumnDropped);
					
				}else if(intMouseX >= 350 && intMouseX < 450){
					intColumnDropped = 3;
					ConnectPiece newgamepiece = new ConnectPiece();
					newgamepiece.intColumn = intColumnDropped;
					blnDroppedPiece = true;
					System.out.println("!!! ssm event send out");
					ssm.sendText(prefix+","+intColumnDropped);
					
				}else if(intMouseX >= 450 && intMouseX < 550){
					intColumnDropped = 4;
					ConnectPiece newgamepiece = new ConnectPiece();
					newgamepiece.intColumn = intColumnDropped;
					blnDroppedPiece = true;
					System.out.println("!!! ssm event send out");
					ssm.sendText(prefix+","+intColumnDropped);
					
				}else if(intMouseX >= 550 && intMouseX < 650){
					intColumnDropped = 5;
					ConnectPiece newgamepiece = new ConnectPiece();
					newgamepiece.intColumn = intColumnDropped;
					blnDroppedPiece = true;
					System.out.println("!!! ssm event send out");
					ssm.sendText(prefix+","+intColumnDropped);
					
				}else if(intMouseX >= 650 && intMouseX <= 750){
					intColumnDropped = 6;
					ConnectPiece newgamepiece = new ConnectPiece();
					newgamepiece.intColumn = intColumnDropped;
					blnDroppedPiece = true;
					System.out.println("!!! ssm event send out");
					ssm.sendText(prefix+","+intColumnDropped);
					
				}
		
				//adding to the board array
				if(blnDroppedPiece) {
					arrayboard.addPosition(intColumnDropped);
					newgamepiece.intRow = arrayboard.intCurrentRow;
					//checking if player has won
					blnHasWon = arrayboard.checkWinner(intTurn);
					if(blnHasWon == true){
						ssm.sendText("win"+","+intTurn+","+strUsername);
					}
				}
				if(intMouseX >= 50 && intMouseX <= 750){
                    changeTurn();
                }
			}
		}
	}
	
	private void changeTurn() {
		if(isServer) {
			intTurn = 2;
		}else {
			intTurn =1;
		}
	}
	public void mousePressed(MouseEvent evt){
		if((isServer && intTurn==1) || (!isServer && intTurn ==2)) {
			//getting mouse position
			int intMouseX = evt.getX();
			int intMouseY = evt.getY();
			//checking if player grabs the pieces
			if(SwingUtilities.isLeftMouseButton(evt)&& intMouseX>=920 && intMouseX<=1150 && intMouseY >= 480 && intMouseY <= 580){
				//gamepiece will follow the mouse
				newgamepiece.intX = intMouseX;
				newgamepiece.intY = intMouseY;
				//generate new game piece to put down
				ConnectPiece newgamepiece = new ConnectPiece();
				this.blnHoldingPiece = true;
			}
		}
	}
	public void mouseClicked(MouseEvent evt){
	}

	//constructor
	public C4GameplayScreen(){
		super();
		thetimer.start();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		//loading pictures
		try{
			strTheme = this.LoadTheme();
			strThemeElements = this.LoadBG();
			strBackgroundFile = strThemeElements[1];
			strBoardFile = strThemeElements[2];
			strP1File = strThemeElements[3];
			strP2File = strThemeElements[4];

			thebackground = ImageIO.read(this.getClass().getResourceAsStream(strBackgroundFile));
			theboard = ImageIO.read(this.getClass().getResourceAsStream(strBoardFile));
			player1piece = ImageIO.read(this.getClass().getResourceAsStream(strP1File));
			player2piece = ImageIO.read(this.getClass().getResource(strP2File));

			newgamepiece.player1piece = this.player1piece;
			newgamepiece.player2piece = this.player2piece;
		}catch(IOException e) {
			System.out.println("Error loading image");
		}

		this.setPreferredSize(new Dimension(1280,720));
		this.setLayout(null);
		
		chatarea = new JTextArea();
		chatarea.setEditable(false);
		chatscroll = new JScrollPane(chatarea);
		chatscroll.setSize(380,350);
		chatscroll.setLocation(850,50);
		chatscroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(chatscroll);
		
		typechat.setSize(new Dimension(380,50));
		typechat.setLocation(850,400);
		typechat.addActionListener(this);
		this.add(typechat);
	}

}
