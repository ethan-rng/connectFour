import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class C4ThemeSelectionScreen extends JPanel{
	//PROPERTIES
	//File Names
	String strBackgroundFile = "Connect4BG.jpg";
	String strChristmasTheme = "christmasTheme.png";
	String strOriginalTheme = "originalTheme.png";
	String strEasterTheme = "easterTheme.png";
	
	//Background
	BufferedImage imgBackground;
	
	//UI Elements and Images
	JButton christmasButton = new JButton("Christmas");
	JButton originalButton = new JButton("Original");
	JButton easterButton = new JButton("Easter");
	JButton backButton = new JButton("Press to Go Back");

	BufferedImage imgChristmas;
	BufferedImage imgOriginal;
	BufferedImage imgEaster;

	//METHODS
	public void paintComponent(Graphics g){
		g.drawImage(imgBackground, 0, 0, null);
		g.drawImage(imgChristmas, 115, 215, null);
		g.drawImage(imgOriginal, 515, 215, null);
		g.drawImage(imgEaster, 915, 215, null);
	}
	public void actionPerformed(ActionEvent evt){}

	//CONSTRUCTOR
	public C4ThemeSelectionScreen(){
		super();
		try{
			imgBackground = ImageIO.read(this.getClass().getResourceAsStream(strBackgroundFile));
			imgChristmas = ImageIO.read(this.getClass().getResourceAsStream(strChristmasTheme));
			imgOriginal = ImageIO.read(this.getClass().getResourceAsStream(strOriginalTheme));
			imgEaster = ImageIO.read(this.getClass().getResourceAsStream(strEasterTheme));
		}catch(IOException e){
			System.out.println("Error loading image");
		}

		//Boiler Plate Code
		this.setPreferredSize(new Dimension(1280,720));
		this.setLayout(null);

		//UX Elements
		christmasButton.setFont(new Font("Serif", Font.PLAIN, 24));
		christmasButton.setSize(250, 50);
		christmasButton.setLocation(115, 450);
		this.add(christmasButton);

		originalButton.setFont(new Font("Serif", Font.PLAIN, 24));
		originalButton.setSize(250, 50);
		originalButton.setLocation(515, 450);
		this.add(originalButton);

		easterButton.setFont(new Font("Serif", Font.PLAIN, 24));
		easterButton.setSize(250, 50);
		easterButton.setLocation(915, 450);
		this.add(easterButton);

		backButton.setFont(new Font("Serif", Font.PLAIN, 24));
		backButton.setSize(255, 50);
		backButton.setLocation(50, 50);
		this.add(backButton);
	}
}
