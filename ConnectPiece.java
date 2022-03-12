import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ConnectPiece{
	//properties
	int intX;
	int intY = 0;
	int intDefY = 20;
	String strPlayer1File;
	String strPlayer2File;
	int intTurn;
	int intColumn;
	int intRow = 5; 
	int intColumnCoords = intColumn*100+50;
	int intRowCoords = intRow*100+50;
	int intB;
	boolean blnStay = false;

	BufferedImage player1piece = null;
	BufferedImage player2piece = null;
	
	//method
	public void nextLoc(){
		intRowCoords = intRow*100+50;
		if(intRowCoords >= intY){
			intY = intY+intDefY;
		}else{
			intDefY = 0;
		}
		
	}
	public void drawOnBoard(Graphics g){
		blnStay = true;
		g.setColor(Color.BLACK);
		g.fillOval(intX, intY, 100,100);
		
	}
	public void drawIt(Graphics g){
		g.setColor(Color.BLACK);
		g.fillOval(intX-50, intY-50, 100,100);
		
	}
	public void dropAnimation(Graphics g){
		
		if(intRow>0) {
			intColumnCoords = intColumn*100+50;
			g.setColor(Color.BLACK);
			g.fillOval(intColumnCoords, intY, 100,100);
			this.nextLoc();
		}else {
			intDefY = 20;
		}
	}
	public void dropAnimationHelp(Graphics g){
		intColumnCoords = (intColumn*100)+800;
		g.setColor(Color.RED);
		g.fillOval(intColumnCoords, intY, 100,100);
		this.nextLoc();
	}
	public void resetAnimation(){
		intX = 0;
		intY = 0;
		intDefY = 20;
		intColumn = 0;
		intColumnCoords = intColumn+50;
	
	}
	
	//constructor
	public ConnectPiece(){

	}

}
