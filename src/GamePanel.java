import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int applex;
	int appley;
	
	
	int appleWidth = 2 * UNIT_SIZE;
	int appleHeight = 2 * UNIT_SIZE;
	int adjustedApplex = applex;
	int adjustedAppley = appley;
	
	public Image apple;
	BackgroundMusic bgMusic = new BackgroundMusic();
	BackgroundMusic biteSound =  new BackgroundMusic();

	
	// = Toolkit.getDefaultToolkit().getImage("apple.png");
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
	}

	// NEw STUFF ADDED
	public void startGame() {
		newApple();
		running = true;
		bodyParts = 6; // Reset body parts
		applesEaten = 0; // Reset apples eaten
		direction = 'R'; // Reset direction
		for (int i = 0; i < bodyParts; i++) {
			x[i] = 0; // Reset snake's x positions
			y[i] = 0; // Reset snake's y positions
		}
		loadImages();
		
		bgMusic.playMusic("Audio/bg1.wav");
		timer = new Timer(DELAY, this);
		timer.start();
	}

	//NEW
	private void loadImages() {
        // Load the apple image
        ImageIcon img = new ImageIcon("Image/apple.png");
        apple = img.getImage();
    }
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if(running) {
			for (int i=0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*25, 0, i*UNIT_SIZE, SCREEN_HEIGHT);//(x1,y1,x2,y2)
				g.drawLine(0, i*25, SCREEN_WIDTH, i*UNIT_SIZE);
			}

			//NEW STUFF
			g.drawImage(apple, adjustedApplex, adjustedAppley, appleWidth, appleHeight, null);
			//g.drawImage(apple, applex, appley, UNIT_SIZE, UNIT_SIZE, null);
			//g.setColor(Color.red);
			//g.fillOval(applex, appley, UNIT_SIZE, UNIT_SIZE);
		
			for(int i=0; i< bodyParts; i++) {
				if (i==0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}
				else {
					g.setColor(new Color (45, 180, 0));
					g.setColor(new Color (random.nextInt(255),random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			
			}
			g.setColor(Color.red);
			//g.drawImage(apple, applex, appley, UNIT_SIZE, UNIT_SIZE, null);
			g.setFont(new Font("Papyrus",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+ applesEaten))/2, g.getFont().getSize());
		}
		else {
			
			gameOver(g);
		}
	
	}
	public void newApple() {
		applex = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appley = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

		adjustedApplex = applex - UNIT_SIZE / 2; // Adjusted x-coordinate
		adjustedAppley = appley - UNIT_SIZE / 2; // Adjusted y-coordinate
	}
	
	public void move() {
		for(int i = bodyParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
			
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
			
		}
	}
	public void checkApple() {
		if((x[0] == applex) && (y[0] == appley)) {
			biteSound.playSoundEffect("Audio/apple_bite.wav");
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		//checks if head collides with body
		for (int i = bodyParts; i>0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//checks if head collides with left border
		if (x[0] < 0) {
			running = false;
		}
		//checks if head collides with right border
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//checks if head collides with top screen?
		if (y[0] < 0) {
			running = false;
		}
		//checks if head collides with bottom screen?
		if (y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
		
	}
	public void gameOver(Graphics g) {
		//Score
	
		running = false;
		bgMusic.stopMusic();
		
		g.setColor(Color.red);
		g.setFont(new Font("Papyrus",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+ applesEaten))/2, g.getFont().getSize());
		
		//GAME OVER text
		g.setColor(Color.red);
		g.setFont(new Font("Papyrus",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
	
		Timer delayTimer = new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				promptPlayAgain();
				((Timer) e.getSource()).stop(); // Stop the timer after executing once
			}
		});
		delayTimer.setRepeats(false); // Execute the action only once
		delayTimer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
			
			
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

	//NEW STUFF
	private void promptPlayAgain() {
        int yesNo = JOptionPane.showConfirmDialog(null, "Play Again?","Yes or No", JOptionPane.YES_NO_OPTION);
        if(yesNo == JOptionPane.YES_OPTION){
        	
            startGame();
			draw(getGraphics());
            }
        else{
            System.exit(JFrame.EXIT_ON_CLOSE);
        }
    }
}
