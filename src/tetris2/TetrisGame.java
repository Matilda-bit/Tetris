/**
 * 
 */
package tetris2;

/**
 * @author downt
 * to run the project correctly - you need to use libraries java.awt
 */


import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JFrame;



public class TetrisGame extends JPanel {

	private static final long serialVersionUID = 9294L;
	
	//variable gameOver to know when to stop creating new pieces
	private static boolean gameOver = false;

	/**
	 * There are seven pieces in standard Tetris.
	 */
	private final Point[][][] Tetraminos = {
			
			// T
			//
			//	* * *
			//	  *
			{
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }, // rotated 90
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) }, // rotated 180
				{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }  //rotated 270
			},
			// Square
			//
			// * *
			// * *
			{
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }, // rotated 90
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }, // rotated 180
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }  //rotated 270
			},
			// Stick / Long Skinny One
			//
			// * * * *
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }, // rotated 90
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) }, // rotated 180
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }  //rotated 270
			}, 
			
			// Periscope (Left Isomers)
			//
			// *
			// *
			// * *
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) }, // rotated 90
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) }, // rotated 180
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }  //rotated 270
			},
			// Periscope (Right Isomers)
			//
			//	 *
			//	 *
			// * *
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) }, // rotated 90
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) }, // rotated 180
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }  //rotated 270
			},
			
			// ("Dog" left) 
			//
			// * *
			// 	 * *
			{
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }, // rotated 90
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) }, // rotated 180
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }  //rotated 270
			},
			// ("Dog" right)
			//
			//	 * *
			// * *
			{
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }, // rotated 90
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) }, // rotated 180
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }  //rotated 270
			}
	};
	
	
	/**
	 * import colors for GUI
	 */
	private final Color[] blockColors = {
		Color.white, Color.lightGray, Color.blue, Color.green, Color.yellow, Color.magenta, Color.red
	};
	
	private long score;
	
	private ArrayList<Integer> nextBlock = new ArrayList<Integer>();
	private Point coordinateBlock;
	private int currentBlock;
	private int rotation;

	private Color[][] cube;


	
	/**
	 * draw a border around and initializes the dropping block
	 */
	private void init() {
		cube = new Color[12][24];
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				//border
				if (i == 0 || i == 11 || j == 22) {
					cube[i][j] = Color.gray;
				} else {
					cube[i][j] = Color.black; // background
				}
			}
		}
		
		if(!gameOver) {
			newPiece();
		}
		
	}
	
	/**
	 * create random block into start position
	 */
	public void newPiece() {
		coordinateBlock = new Point(5, 1);
		rotation = 0;
		
		if (nextBlock.isEmpty()) {
			Collections.addAll(nextBlock, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nextBlock);
		}
		currentBlock = nextBlock.get(0);
		nextBlock.remove(0);
		
	}
	

	/**
	 * Collision test
	 * @param x
	 * @param y
	 * @param rotation
	 * @return
	 */
	private boolean collidesAt(int x, int y, int rotation) {
		for (Point p : Tetraminos[currentBlock][rotation]) {
			if (cube[p.x + x][p.y + y] != Color.black) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Rotate the block
	 * @param i 
	 */
	public void rotate(int i) {
		int newRotation = (rotation + i) % 4;
		if (newRotation < 0) {
			newRotation = 3;
		}
		if (!collidesAt(coordinateBlock.x, coordinateBlock.y, newRotation)) {
			rotation = newRotation;
		}
		repaint();
	}
	
	
	/**
	 *  Move the piece left or right
	 * @param i => -1 or +1
	 */
	public void move(int i) {
		if (!collidesAt(coordinateBlock.x + i, coordinateBlock.y, rotation)) {
			coordinateBlock.x += i;	
		}
		repaint();
	}

	/**
	 * Drops down the block  if there is where to fall 
	 * if not then attaches the block to other blocks
	 */
	public void dropDown() {
		
		if (!collidesAt(coordinateBlock.x, coordinateBlock.y + 1, rotation)) {
			coordinateBlock.y += 1;
		} else {
			if ((coordinateBlock.x== 5) && (coordinateBlock.y==1)) {
				gameOver = true;
			}
			else {
				addBlock();
			}
			
		}	
		repaint();
	}
	
	/**
	 * Adds a current block to already added blocks to avoid collisions
	 */
	public void addBlock() {
		for (Point p : Tetraminos[currentBlock][rotation]) {
			cube[coordinateBlock.x + p.x][coordinateBlock.y + p.y] = blockColors[currentBlock];
		}
		
		deleteRows();
		newPiece();
	}
	
	/**
	 * Delete row
	 * @param row 
	 */
	public void deleteRow(int row) {
		for (int j = row-1; j > 0; j--) {
			for (int i = 1; i < 11; i++) {
				cube[i][j+1] = cube[i][j];
			}
		}
	}
	

	/**
	 * Remove the filled lines from the field and award points in score 
	 * with the number of simultaneously cleared lines.
	 */
	public void deleteRows() {
		boolean space;
		int numLines = 0;
		
		for (int j = 21; j > 0; j--) {
			space = false;
			for (int i = 1; i < 11; i++) {
				if (cube[i][j] == Color.BLACK) {
					space = true;
					break;
				}
			}
			if (!space) {
				deleteRow(j);
				j += 1;
				numLines += 1;
			}
		}
		
		//add points to score by clear lines (max 4 lines)
		switch (numLines) {
		case 1:
			score += 100;
			break;
		case 2:
			score += 200;
			break;
		case 3:
			score += 300;
			break;
		case 4:
			score += 400;
			break;
		}
	}
	
	/**
	 * Draw the dropped block
	 * @param g
	 */
	private void drawBlock(Graphics g) {		
		g.setColor(blockColors[currentBlock]);
		for (Point p : Tetraminos[currentBlock][rotation]) {
			g.fillRect((p.x + coordinateBlock.x) * 26, 
					   (p.y + coordinateBlock.y) * 26, 
					   25, 25);
		}
		

	}
	
	@Override 
	public void paintComponent(Graphics g)
	{
		// Paint the well
		g.fillRect(0, 0, 312, 598);
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				g.setColor(cube[i][j]);
				g.fillRect(26*i, 26*j, 25, 25);
			}
		}
		
		// Display the score
		g.setColor(Color.WHITE);
		g.drawString("" + score, 228, 25);
		
		
		//if the game is over, writes a message "GAME OVER"
		if(gameOver)
		{
			String gameOverString = "GAME OVER";
			g.setFont(new Font("Georgia", Font.BOLD, 30));
			g.drawString(gameOverString, 52, 250);
			g.drawString("Your score: ", 60, 280);
			g.drawString("" + score, 120, 310);
		}
		
		// Draw the currently falling piece
		drawBlock(g);
	}
	/**
	 * main program
	 * @param args
	 */
	public static void main(String[] args) {
		//set size for the window
		final int WIDTH = 327;
		final int HEIGHT = 634;
		
		JFrame window = new JFrame("Simple Tetris Game");
		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final TetrisGame game = new TetrisGame();
		//draw background and blocks
		game.init();
		window.add(game);
		
		window.setVisible(true);
		
		
		// keyboard actions/controls
		window.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				//up - rotate
				case KeyEvent.VK_UP:
					game.rotate(+1);
					break;
				//down - drop down
				case KeyEvent.VK_DOWN:
					game.dropDown();
					break;
				// left - move left
				case KeyEvent.VK_LEFT:
					game.move(-1);
					break;
				// right - move right
				case KeyEvent.VK_RIGHT:
					game.move(+1);
					break;
				} 
			}
			
			public void keyReleased(KeyEvent e) {
			}
		});
		
		
		
		/**
		 * drop down the block every second
		 */
		new Thread() {
			@Override public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						game.dropDown();
					} catch ( InterruptedException e ) {}
				}
			}
		}.start();
	}
}