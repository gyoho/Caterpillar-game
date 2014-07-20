import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;


public class Caterpillar {
	private Color color;
	private int score = 0;
	private int lifeTime = 20;
	private boolean isScored = false;
	// headPos == head headPos
	// set headPos to property in order to reference and update in methods
	private Point headPos;
	private char direction = 'E';
	private Queue body = new LinkedList();
	private Queue commands = new LinkedList();
	
	public Caterpillar(Color c, Point sp) {
		color = c;
		// ten circles represents a body
		for (int i=0; i<10; i++) {
			headPos = new Point(sp.x + i, sp.y);
			body.add(headPos);
		}
	}
	
	public boolean isDead() {
		return body.isEmpty();
	}
	
	public int getScore() {
		return score;
	}
	
	public Point getHeadPos() {
		return headPos;
	}
	
	public void addScore(int upScore) {
		score += upScore;
		lifeTime = 20;
		isScored = true;
	}
	
	public void setDirection (char d) {
		commands.add(new Character(d));
	}
	
	public void move(CaterpillarGame game) {
		// first see if we should change direction
		if(!commands.isEmpty()) {
			Character c = (Character) commands.peek(); // just peek
			commands.remove();
			
			// if try to move opposite direction, ignore the command
			if(!isOppositeDir(c))
				direction = c.charValue();	// Character wrapper to char
			
			// quit command
			if (direction == 'Z')
				System.exit(0);
		}
		
		// then find new headPos
		Point np = newPosition();
		// if the caterpillar is still in the window
		// and not run over each other
		if (game.canMove(np)) {
			// remove tail and add new one to head
			/** instead of adding new segment, just don't remove the tail when catches a score **/
			if(!isScored) {
				body.remove();
			}
			else { // reset isScored
				isScored = false;
			}
			body.add(np);
			headPos = np;
		}
		
		// decrease life time
		--lifeTime;
		// remove its tail if life time turns to 0
		if(lifeTime == 0) {
			body.remove();
			lifeTime = 20;
		}
	}
	
	private boolean isOppositeDir(Character c) {
		if(direction == 'E' && c == 'W')
			return true;
		if(direction == 'W' && c == 'E')
			return true;
		if(direction == 'N' && c == 'S')
			return true;
		if(direction == 'S' && c == 'N')
			return true;
		
		return false;
	}
	
	private Point newPosition() { 
		int x = headPos.x;
		int y = headPos.y;
		
		if (direction == 'E')
			x++;
		else if (direction == 'W')
			x--;
		else if (direction == 'N')
			y--; 
		else if (direction == 'S')
			y++;
		
		return new Point(x, y);
	}
	
	// test if a caterpillar's head is hitting its body
	public boolean inPosition (Point np) { 
		ListIterator itr = ((LinkedList) body).listIterator(); 
		while (itr.hasNext()) {
			Point location = (Point) itr.next(); 
			if (np.equals(location)) 
				return true;
		}
		return false; 
	}
		
	public void drawSelf(Graphics g) {	
		g.setColor(color);
		ListIterator itr = ((LinkedList) body).listIterator();
	
		// iterator stuff
		// 15: height of title
		while (itr.hasNext()) {
			Point p = (Point) itr.next();
			g.fillOval(5 + CaterpillarGame.SegmentSize * p.x, 
						15 + CaterpillarGame.SegmentSize * p.y, 
						CaterpillarGame.SegmentSize, 
						CaterpillarGame.SegmentSize);
		}
	}
}
