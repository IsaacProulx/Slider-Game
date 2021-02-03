package exercise;
/**
 * Description: Practice program for Slider Game
 * Date: November 29th, 2018
 * Original Author: Unknown
 * Modifications: Mr. Roller
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.*;

public class SlidingPractice extends JFrame implements ActionListener, ComponentListener {
	int length;
	int width;
    int sec=0;
    int min=0;
    private JFrame frame;
    private JLayeredPane screen;
    private JPanel gameboardPanel, pauseMenu, winScreen; //create a panel to add to JFRAME
	private JButton[] buttons;  //Make room for 16 button objects
	private JLabel[] labels;
	private int emptyIndex; //Variable will track the empty spot
    JButton resume, newGame;
	int moves=0;
	
	public void componentHidden(ComponentEvent e) {}
    public void componentMoved(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {
    	/*
        Dimension newSize = e.getComponent().getBounds().getSize();
        screen.setBounds(0,0,newSize.width,newSize.height);
        for(int i=0;i<screen.getComponentCount();i++) {
        	screen.getComponent(i).setBounds(0,0,newSize.width,newSize.height);
        }
        screen.setBounds(0,0,newSize.width,newSize.height);
        System.out.println(newSize);
        */
    }
        
    public static void main(String[] args) {
    	SlidingPractice game = new SlidingPractice();
    	game.clearScreen();
    	game.frame(game);
	}
    
    public void frame(SlidingPractice game) {
    	frame = game;
    	frame.setTitle("SlidingPractice");
    	frame.setResizable(true);
    	frame.setMinimumSize(new Dimension(600, 600));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	gameboard();
    	frame.setVisible(true);
    	frame.addComponentListener(this);
    }
	
	public void gameboard(){
		min = 0;
		sec = 0;
		width = Integer.parseInt(JOptionPane.showInputDialog("Enter Width (Length will be the same)"));
        if(width<2){
        	width=2;
        }
        if(width>7){
        	width=7;
        }
        length=width;
        buttons= new JButton[length*width];
               
        //is resizeable by default. Layout
        //Manager takes care of the necessary scaling.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font f = new Font("Arial", Font.BOLD, 26);
		Color[] colours = {Color.orange, Color.white};
		JLabel move = new JLabel(moves+" Moves");
    	move.setBounds(450,450,10,100);
    	move.setFont(f);
    	move.setForeground(Color.BLACK);
		
		gameboardPanel = new JPanel();
        gameboardPanel.setEnabled(false);
		gameboardPanel.setLayout(new GridLayout(length,width,5,5)); // length x width grid with 5 pixel padding										   // vert/horz dividers
		gameboardPanel.setBackground(Color.black); //Allows empty space to be black
		gameboardPanel.setBounds(0,0,595,595);
		//gameboardPanel.add(move);
		int j=0;
		for (int i = 0; i < buttons.length; i++) { 		//From i is 0 to 15
			
			int colourIndex = 0;						//Start with Orange
			
			buttons[i] = new JButton("" + i);			//Constructor sets text on new buttons
			
			buttons[i].setSize(100/width,100/length);   //Button size, but don't really need this line
														//since we are using Layout Manager
			
			colourIndex = 0; 							//default colour of square is orange
			
			if(width%2 == 0 && (j<width && j%2 != 0||j >= width && j%2 == 0) ) {
				colourIndex = 1; //make white if necessary
            }
			
			if(width%2 != 0 && i%2 != 0) {
				colourIndex = 1; //make white if necessary
			}
			
			j++;
			if(j==width*2) {
				j=0;
			}
                        
			buttons[i].setBackground(colours[colourIndex]);
			buttons[i].setForeground(Color.blue); //Text colour
			buttons[i].setFont(f);
			buttons[i].addActionListener(this); //Set up ActionListener on each button
			gameboardPanel.add(buttons[i]); //Add buttons to the grid layout of gameboardPanel
		}
		
		buttons[length*width-1].setVisible(false); //Will show the black background without a
												   //visible button here
		emptyIndex=length*width-1;
		
		addToScreen(gameboardPanel, 1);
        //setContentPane(gameboardPanel); //Add gameboardPanel to JFrame
        shuffle();
	}
	
	public void addToScreen(JPanel panel, int layer) {
		panel.setEnabled(true);
		screen.setEnabled(true);
		screen.add(panel, layer);
		setContentPane(screen);
		setVisible(true); //Turn on JFrame
	}
	
	public void addToScreen(JButton panel, int layer) {
		panel.setEnabled(true);
		screen.setEnabled(true);
		screen.add(panel, layer);
		setContentPane(screen);
		setVisible(true); //Turn on JFrame
	}
	
	public void clearScreen() {
		screen = new JLayeredPane();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newGame) {
			gameboard();
		}
        for(int i=0; i<buttons.length; i++){	
            if(e.getSource() == buttons[i]){
                if(isValid(i)){
                	swapPieces(i);
                	moves++;
                	System.out.println(moves);
                }
                //Swap with blank space
            }
        }
        win();
        
}
	
	private void swapPieces(int btnIndex)   //Send along button that was pushed
	{
		buttons[emptyIndex].setText(buttons[btnIndex].getText());
		buttons[btnIndex].setText(width*width-1+"");//Move over text
		buttons[emptyIndex].setVisible(true);		//to blank button
		buttons[emptyIndex].setBackground(buttons[btnIndex].getBackground());
        buttons[btnIndex].setVisible(false); //Turn off visibility of button that was pushed
                							 //and background will show through as black
		//buttons[emptyIndex].setVisible(true);//Turn on visibility of the old blank button
		buttons[btnIndex].setBackground(buttons[emptyIndex].getBackground());
		emptyIndex = btnIndex;		     //Update the emptyIndex to the button that was
						     			 //pushed.
        
	}	
	
        public void shuffle(){
            for(int i=0; i<Math.pow(length*width,2)*5; i++){
               int j =(int)(Math.random()*buttons.length);
                if(isValid(j)){
                    swapPieces(j);
                }
            }
        }
        
        public boolean win(){
        	
            for(int i=0; i<buttons.length; i++){
            	if(i<length*width) {
            		if(!buttons[i].getText().equals(""+i)){
            			return false;
            		}
            	}
            }
            
            for(int i=0; i<buttons.length; i++){
            	buttons[i].setEnabled(false);
            }
            
            newGame = new JButton("New Game");
            //newGame.setBounds(screen.getWidth()/2 -75,screen.getHeight()/2 -25, 150, 50);
            newGame.setBounds(225, 225, 150, 50);
            newGame.addActionListener(this);
            newGame.setVisible(true);
            winScreen = new JPanel();
            winScreen.setBounds(0,0,600,600);
            winScreen.setOpaque(false);
            winScreen.setLayout(null);
            winScreen.setEnabled(true);
            winScreen.add(newGame);
            addToScreen(winScreen, 0);
            return true;
        }
        
        public boolean isValid(int button){
            if(emptyIndex+width==button||emptyIndex-width==button||emptyIndex+1==button&&button%width!=0||emptyIndex-1==button&&(button+1)%width!=0){
                return true;
            }else{
                return false;
            }
        }
		
        
       }