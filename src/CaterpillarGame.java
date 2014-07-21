import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class CaterpillarGame extends Frame {

	final static int BoardWidth = 60;
	final static int BoardHeight = 40;
	final static int SegmentSize = 10;
	final static int SLEEP_TIME = 200;
	static int drawCount = 0;
	
	private Caterpillar playerOne = new Caterpillar (Color.blue, new Point(20, 10));
	private Caterpillar playerTwo = new Caterpillar (Color.red, new Point(20, 30));
	private SquareScore score = new SquareScore(Color.black);

	
	public static void main(String[] args) {
		CaterpillarGame world = new CaterpillarGame();
		world.setVisible(true);
		world.run();
	}
	
	public CaterpillarGame() {
		setTitle ("Caterpillar Game");
		setSize ((BoardWidth+2)*SegmentSize, BoardHeight*SegmentSize+30);
		addKeyListener (new KeyReader());
		addWindowListener(new CloseQuit());
	}
	
	public void run()  {
		// the initial position is (0, 0)
		score.setPosition(this);
		// now start the game
		while(!(playerOne.isDead() || playerTwo.isDead())) {
			movePieces();
			if(catchScore()) {
				score.setPosition(this);
				score.score++;
			}
			repaint();
			try {
				Thread.sleep(SLEEP_TIME);
			} catch(Exception e) {}
			drawCount++;
		}
	}
	
	public void paint(Graphics g) {
		playerOne.drawSelf(g);
		playerTwo.drawSelf(g);
		score.drawSelf(g);
		drawScore(g);
		if((playerOne.isDead() || playerTwo.isDead()))
			drawGameOver(g);
	}
	
	public void movePieces() {
		// this ==> world
		playerOne.move(this);
		playerTwo.move(this);
	}
	
	// only cares the head position
	public boolean catchScore() {
		// check if a caterpillar catches the score
		// if so, add the score
		if(playerOne.getHeadPos().equals(score.position)) {
			playerOne.addScore(score.score);
			return true;
		}
		if(playerTwo.getHeadPos().equals(score.position)) {
			playerTwo.addScore(score.score);
			return true;
		}
		return false;
	}

	// main game knows the position of each caterpillar
	// caterpillar asks main game if it can move to next position
	public boolean canMove(Point np) {
		// get x, y coordinate
		int x = np.x;
		int y = np.y;
		
		// test playing board
		if((x <= 0) || (y <= 0))
			return false;
		// -1 => space for score display
		if((x >= BoardWidth) || (y >= BoardHeight-1))
			return false;

		// not on caterpillar one's body
		if(playerOne.inPosition(np))
			return false;
		// not on caterpillar two's body
		if(playerTwo.inPosition(np))
			return false;
		
		// OK, safe square
		return true;
	}
	
	private void drawScore(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString("BLUE: " + playerOne.getScore(), 10, BoardHeight*SegmentSize+20);
		g.drawString("RED: " + playerTwo.getScore(), BoardWidth*SegmentSize-30, BoardHeight*SegmentSize+20);
	}
	
	private void drawGameOver(Graphics g) {
		Font helvetica = new Font ("Helvetica", Font.BOLD, 20);
		g.setFont(helvetica);
		g.setColor(Color.black);
		g.drawString("Game Over", (CaterpillarGame.BoardWidth*CaterpillarGame.SegmentSize)/2-60,
				(CaterpillarGame.BoardHeight*CaterpillarGame.SegmentSize)/2);
	}
	
	private class KeyReader extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			char c = e.getKeyChar(); 
			switch(c) {
				case 'q': playerOne.setDirection('Z'); break;
				case 'a': playerOne.setDirection('W'); break; 
				case 'd': playerOne.setDirection('E'); break; 
				case 'w': playerOne.setDirection('N'); break; 
				case 's': playerOne.setDirection('S'); break;
				
				case 'p': playerTwo.setDirection('Z'); break; 
				case 'j': playerTwo.setDirection('W'); break; 
				case 'l': playerTwo.setDirection('E'); break; 
				case 'i': playerTwo.setDirection('N'); break; 
				case 'k': playerTwo.setDirection('S'); break;
			}
		}
	}
	
	// inner class for intercepting window action
	private class CloseQuit extends WindowAdapter{  
        public void windowClosing(WindowEvent e){  
            System.exit(0);  
        }
	}
	
}
