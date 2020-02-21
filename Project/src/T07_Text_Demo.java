import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import java.util.Random;

public class T07_Text_Demo {
	private final static int W_KEY = 87;
	private final static int A_KEY = 65;
	private final static int S_KEY = 83;
	private final static int D_KEY = 68;
	private final static int ESC_KEY = 27;
	
	private static long startTime = System.currentTimeMillis();


	
	private static Random random = new Random();
	

	private enum playerState {
		FORWARD, BACKWARD, LEFT, RIGHT, STOPPED
	};

	private static playerState currentPlayerState = playerState.STOPPED;

	public static void main(String[] args) {

		// Initialize the frame settings
		JFrame frame = new JFrame("KeyListener");
		Container contentPane = frame.getContentPane();
		

		// Initialize the keyboard listener
		KeyListener listener = new KeyListener() {

			// NOT IMPORTANT FOR NOW, IGNORE
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			// Test for keys being pressed
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == W_KEY || keyCode == A_KEY || keyCode == S_KEY || keyCode == D_KEY)
					System.out.print("Moving: ");
				
				long elapsedTime = System.currentTimeMillis() - startTime;
				long elapsedSeconds = elapsedTime / 1000;

				// Simple switch statement for basic keys we will be testing for.
				switch (keyCode) {
				case W_KEY:
					System.out.println("Forward");
					currentPlayerState = playerState.FORWARD;
					break;
				case S_KEY:
					System.out.println("Backward");
					currentPlayerState = playerState.BACKWARD;
					break;
				case A_KEY:
					System.out.println("Left");
					currentPlayerState = playerState.FORWARD;
					break;
				case D_KEY:
					System.out.println("Right");
					currentPlayerState = playerState.FORWARD;
					break;

				case ESC_KEY:
					System.out.println();
					System.out.println("Game Stopped");
					System.exit(0);
				}
				
				if(elapsedSeconds == random.nextInt(100)) {
					startTime = System.currentTimeMillis();
					System.out.println("");
					System.out.println("Battle Started");
					System.out.println("");
					System.out.println("Battle Ended");
					System.out.println("");
				}

			}

			// Test if the key is released
			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == W_KEY || keyCode == A_KEY || keyCode == S_KEY || keyCode == D_KEY) {
					System.out.println("Player Stopped");
					currentPlayerState = playerState.STOPPED;
				}
			}

		};

		// initialize the frame

		Dimension d = new Dimension(400, 400);
		frame.addKeyListener(listener);
		frame.setPreferredSize(d);

		frame.pack();

		frame.setVisible(true);
		

		
		
	}

}
