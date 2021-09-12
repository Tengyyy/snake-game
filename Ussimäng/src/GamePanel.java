import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; // how big will objects in the game appear
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE; // how many objects can we fit one the screen
	static final int DELAY = 75; // timer delay (higher = slow gameplay)
	int x[] = new int[GAME_UNITS]; // this array holds all x coordinates of the snakes body parts
	int y[] = new int[GAME_UNITS]; // holds all y coordinates of the snake

	
	int bodyParts = 6; // how long will the snake be at the start of the game
	int applesEaten; // apples eaten counter, initally 0
	int appleX;
	int appleY;
	char direction = 'R'; // the snakes moving direction (U = up, D = down, L = left, R = right)
	boolean running = false; // is the game running or not
	
	boolean isPaused = false;
	
	int gameCounter = 0;
	
	Timer timer;
	Random random;
	
	//ArrayList<Character> directionQueue = new ArrayList<Character>();
	
	GamePanel(){
		
		
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter()); // creates a new instance of the MyKeyAdapter inner class and passes it as a keylistener
		
		startGame();
	}
	
	public void startGame() {
		spawnApple();
		running = true;
		
		if(gameCounter == 0) {
			timer = new Timer(DELAY, this);
		}
		
		
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if(running) {
					// draws a grid on the game panel to help visualize things
					for(int i = 0; i< SCREEN_WIDTH/ UNIT_SIZE; i++) {
						g.drawLine(0, UNIT_SIZE * i, SCREEN_WIDTH, UNIT_SIZE * i);
					}
					for(int i = 0; i< SCREEN_HEIGHT/ UNIT_SIZE; i++) {
						g.drawLine( UNIT_SIZE * i, 0, UNIT_SIZE * i, SCREEN_HEIGHT);
					}
			
					//drawing the apple:
					g.setColor(Color.RED);
					g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // draws a red circle representing an apple
			
					// drawing the snake
					for(int i = 0; i < bodyParts; i++) {
						if(i==0) {
							g.setColor(new Color(0,100,0));
							g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
						}
						else {
							g.setColor(new Color(50, 205, 50));
							//g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))); // RAINBOW SNAKE!!!
							g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
						}
					}
				

					g.setColor(Color.RED);
					g.setFont(new Font("Ink Free", Font.BOLD, 40));
					FontMetrics metrics = getFontMetrics(g.getFont());
				
					g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
				
				if(isPaused) {
					pauseGame(g);
				}
			}
			
		else {
			gameOver(g);
		}
	}
	
	public void spawnApple() { // spawns apple on the field
		appleX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE; // cast as an int to eliminate possible double values from the division
		appleY = random.nextInt((int) SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
	}
	
	public void move() {
		for(int i = bodyParts; i>0; i--) {
			
			// shifts coordinates over to the spot where the previous bodypart was, excluding the snakes head which is at index 0
			x[i] = x[i-1];
			
			y[i] = y[i-1];
			
		}
		
		switch(direction) {
			case 'U': y[0] -= UNIT_SIZE; // moves the snake head by the unit_size in a direction depending on the char value
			break;
			case 'D': y[0] += UNIT_SIZE;
			break;
			case 'L': x[0] -= UNIT_SIZE;
			break;
			case 'R': x[0] += UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() {
		if(x[0] == appleX && y[0] == appleY) {
			bodyParts++;
			applesEaten++;
			spawnApple();
		}
	}
	
	public void checkCollisions() {
		// checks if the snakes heads coordinates are equal to the coordinates of any body part, in which case the snake collided with its body
		for(int i = bodyParts; i> 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) { 
				running = false;
			}
		}
		// checks if the snake collided with the screen borders
		if(x[0] >= SCREEN_WIDTH || x[0] < 0 || y[0] >= SCREEN_HEIGHT || y[0] < 0) {
			running = false;
		}
		
		if(!running) {
			timer.stop(); // stops the timer if snake collides with sth and the game ends
		}
	}
	
	public void gameOver(Graphics g) {
		
		timer.stop();
		
		
		// Game Over text:
		
		g.setColor(Color.RED);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont()); // stores the font data - size, dimensions whatever
		
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); // draw the text game over in the center of the screen
		g.setColor(Color.RED);
		
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
	
		isPaused = false;
		
		g.setFont(new Font("Ink Free", Font.BOLD, 30));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		
		g.drawString("Press Enter to start a   new game", (SCREEN_WIDTH - metrics3.stringWidth("Press enter to start a new game"))/2, SCREEN_HEIGHT/2 + 200);
		
	}
	
	public void pauseGame(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(25, 25, 25, 100);
		
		g.fillRect(75, 25, 25, 100);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(!isPaused) {
			if(running) { // if game is running
			
				move();
				checkApple();
				checkCollisions();
			}
			
		}
		
		repaint(); // repaints the panel every 75 ms.
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{ // like KeyListener interface but you only have to use the methods that you need for your program
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_UP: 
					
					if(!isPaused) {
						if(direction != 'D') {
							direction = 'U'; // changes the snakes direction depending on the users keypress
						}
					}
					break;
				case KeyEvent.VK_LEFT: 
					
					if(!isPaused) {
						if(direction != 'R') {
							direction = 'L';
						}
					}
					
					break;
				case KeyEvent.VK_DOWN:
					
					if(!isPaused) {
						if(direction != 'U') {
							direction = 'D';
						}
					}
					
					
					break;
				case KeyEvent.VK_RIGHT: 
					
					if(!isPaused) {
						if(direction != 'L') {
							direction = 'R';
						}
					}
					
					break;
					
				case KeyEvent.VK_ESCAPE:
					if(running) {
						if(!isPaused) {
							isPaused = true;
						}
						else if(isPaused) {
							isPaused = false;
						}
					}
					break;
					
				case KeyEvent.VK_ENTER:
					if(!running) {
						isPaused = false;
						gameCounter++;
						
						//reset snake properties:
						
						bodyParts = 6;
						applesEaten = 0;
						direction = 'R';
						
						
						for(int i = 0; i < bodyParts; i++) {
							x[i] = 0;
							y[i] = 0;
						}
						
						startGame();
						
						
						
						System.out.println("A new game has started");
						
					}
					break;
			}
		}
	}

}
