import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.event.*;

public class C4HelpScreen extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
	//properties
	BufferedImage helpbackground = null;
	BufferedImage helpboard = null;
	BufferedImage helpgamepiece = null;
	Timer thetimer = new Timer(1000/60, this);
	boolean blnHoldingPiece = false;
	ConnectPiece newgamepiece = new ConnectPiece();
	moduleBackendBoard helpboardarray = new moduleBackendBoard();
	int intMouseX;
	int intMouseY;
	int intColumnDropped;
	boolean blnDroppedPiece;
	boolean blnPlayedPiece;
	boolean blnInRange;
	
	JTextArea helptextarea;
	JButton backbutton;
	
	//methods
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource() == thetimer){
			this.repaint();
		}
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(helpbackground, 0, 0, null);
		g.setColor(Color.WHITE);
		g.fillRect(750,50,480,600);
		g.drawImage(helpboard, 800, 50, null);
		g.drawImage(helpgamepiece, 1060, 300, null);
		
		if(blnHoldingPiece == true){
			newgamepiece.drawIt(g);
		}
		if(blnDroppedPiece == true){
			System.out.println("!!!!!!!!!!!!! blnDroppedPiece is true");
			newgamepiece.intColumn = intColumnDropped;
			System.out.println("!!!!!!!!!!!!! intRowCoords is "+newgamepiece.intRowCoords);
			System.out.println("!!!!!!!!!!!!! intY is "+newgamepiece.intY);
			if(newgamepiece.intY < newgamepiece.intRowCoords){
				newgamepiece.dropAnimationHelp(g);
			}else{
				blnDroppedPiece = false;
				newgamepiece.blnStay = true;
				blnInRange = false;
				blnPlayedPiece = true;
			}
			
		}
		//drawing the game piece in the board
		for(int intRow = 0; intRow < 6; intRow++){
				for(int intCol = 0; intCol < 2; intCol++){
					if(helpboardarray.intBoard[intRow][intCol] == 1){
						g.drawImage(helpgamepiece, intCol*100+800,intRow*100+50, null);
					}else if(helpboardarray.intBoard[intRow][intCol] == 2){
						g.drawImage(helpgamepiece, intCol*100+800,intRow*100+50, null);
					}
				}
			
		}
	}
	public void mouseMoved(MouseEvent evt){
	}
	public void mouseDragged(MouseEvent evt){
		int intMouseX = evt.getX();
		int intMouseY = evt.getY();
		newgamepiece.intX = intMouseX;
		newgamepiece.intY = intMouseY;
		
	}
	public void mouseExited(MouseEvent evt){
	}
	public void mouseEntered(MouseEvent evt){
	}
	public void mouseReleased(MouseEvent evt){
		System.out.println("!!!!! mouse released");
		if(blnHoldingPiece) {
			blnHoldingPiece = false;
			intMouseX = evt.getX();
			intMouseY = evt.getY();
			//if(blnInRange) {
			//checking which column the piece is dropped in
			if(intMouseX >= 800 && intMouseX < 900){
				intColumnDropped = 0;
				ConnectPiece newgamepiece = new ConnectPiece();
				newgamepiece.intColumn = intColumnDropped;
				blnDroppedPiece = true;
				System.out.println("Column dropped: "+intColumnDropped);
				
			}else if(intMouseX >= 900 && intMouseX < 1000){
				intColumnDropped = 1;
				ConnectPiece newgamepiece = new ConnectPiece();
				newgamepiece.intColumn = intColumnDropped;
				blnDroppedPiece = true;
				System.out.println("Column dropped: "+intColumnDropped);
				
			}
			if(blnDroppedPiece) {
				helpboardarray.addPosition(intColumnDropped);
				newgamepiece.intRow = helpboardarray.intCurrentRow;
				for(int intRows = 6; intRows >= 0; intRows--){
					System.out.println("!!!! intRows are "+intRows);
					if(helpboardarray.intBoard[intColumnDropped][intRows] != 0){
						newgamepiece.intRow = intRows;
					}
				}
			}
		}
		
	}
	public void mousePressed(MouseEvent evt){
		intMouseX = evt.getX();
		intMouseY = evt.getY();
		System.out.println(intMouseX + " , " + intMouseY);
		if(SwingUtilities.isLeftMouseButton(evt)&& intMouseX>=1060 && intMouseX<=1160 && intMouseY >= 300 && intMouseY <= 400){
			newgamepiece.intX = intMouseX;
			newgamepiece.intY = intMouseY;
			ConnectPiece newgamepiece = new ConnectPiece();
			this.blnHoldingPiece = true;
			System.out.println("pressed within range of pieces");
		}
			
	}
	public void mouseClicked(MouseEvent evt){
	}

	//constructor
	public C4HelpScreen(){
		super();
		thetimer.start();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		try{
			helpboard = ImageIO.read(this.getClass().getResourceAsStream("helpboard.png"));
			helpgamepiece = ImageIO.read(this.getClass().getResourceAsStream("P1original.png"));
		}catch(IOException e){
			System.out.println("Error loading image");
		}
		this.setPreferredSize(new Dimension(1280,720));
		this.setLayout(null);

		helptextarea = new JTextArea();
		helptextarea.setText("\n  How to play:\n  Players will take turns dropping their game pieces into the board.\n  Be mindful of gravity!\n  The first player to get 4 game pieces in a row wins.\n  The 4 pieces can be arranged horizontally, vertically, or diagonally.\n\n  Controls:\n  Click and drag the icon of your game piece to the board.\n  Drop the game piece by releasing your mouse.\n  You can drop the piece anywhere in the desired column.\n\n  Test it out on the right -->");
		helptextarea.setEditable(false);
		helptextarea.setSize(620,500);
		helptextarea.setLocation(50,50);
		this.add(helptextarea);
		
		backbutton = new JButton();
		backbutton.setSize(new Dimension(620,60));
		backbutton.setLocation(50,580);
		backbutton.setText("Back to Main Menu");
		backbutton.addActionListener(this);
		this.add(backbutton);
		
	}

}
