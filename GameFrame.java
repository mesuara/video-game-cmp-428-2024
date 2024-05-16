import java.awt.KeyboardFocusManager;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
	
	GameFrame(){	
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		if( !isVisible( ) ) {
			this.setVisible( true );
			this.toFront();
			this.setAlwaysOnTop( true );
		}
	  }
	}
