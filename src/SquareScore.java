import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class SquareScore {
	Color color;
	Integer score;
	Point position;
	
	public SquareScore(Color c) {
		color = c; 
		score = 1;
		position = new Point(0, 0);
	}
	
	public void setPosition(CaterpillarGame game) {
		do {
			// create a random position within the board
			int x = (int) (Math.random()*CaterpillarGame.BoardWidth);
			// -1 => space for score display
			int y = (int) (Math.random()*CaterpillarGame.BoardHeight-1);
			Point randomPos = new Point(x, y);
			position = new Point(randomPos);		
		} while(!game.canMove(position));
	}
	
	public void drawSelf(Graphics g) {
		g.setColor(color);
		g.drawString(score.toString(), 5 + CaterpillarGame.SegmentSize * position.x, 
				25 + CaterpillarGame.SegmentSize * position.y);
	}
}
