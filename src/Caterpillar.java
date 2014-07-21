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
	private int segSize = CaterpillarGame.SegmentSize;
	private int gap = CaterpillarGame.SegmentSize/10;
	private boolean isScored = false;
	private int dirChangeCount = 0;
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
			headPos = new CaterpillarPosition(sp.x + i, sp.y);
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
			// otherwise, the caterpillar will stop moving
			if(!isOppositeDir(c)) {
				// also if not the same direction, then the direction is changed
				if(c != direction) {
					// set the joint number with direction changed count
					dirChangeCount++;
				}
				direction = c.charValue();	// Character wrapper to char
			}

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
		
		// direction is in x-axis
		if(dirChangeCount%2==0)
			return new CaterpillarPosition(x, y);
		
		// direction is in Y-axis
		return new CaterpillarPosition(x, y, false);
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
		
		 // 2*(CaterpillarGame.drawCount%2))-1 ==> switch the sign according with the draw count
		for(int i=0; i<body.size(); i++) {
			CaterpillarPosition p = (CaterpillarPosition) ((LinkedList) body).get(i);
			
			// check segment is in which direction
			if(p.isDirInX) {
				/* wave in y-direction*/
				// element in even index number of the body
				if(i%4 == 0) {
					g.fillOval(5 + segSize * p.x, 
							15 + segSize * p.y + ((2*((CaterpillarGame.drawCount+1)%2))-1)*gap, 
							segSize, segSize);
				} 
				// element in odd index number of the body
				else if(i%2 != 0) {
					g.fillOval(5 + segSize * p.x, 
							15 + segSize * p.y, 
							segSize, segSize);
				} else {
					g.fillOval(5 + segSize * p.x, 
							15 + segSize * p.y + ((2*(CaterpillarGame.drawCount%2))-1)*gap, 
							segSize, segSize);
				}
			} else {
				/* wave in x-direction*/
				if(i%4 == 0) {
					g.fillOval(5 + segSize * p.x + ((2*((CaterpillarGame.drawCount+1)%2))-1)*gap, 
							15 + segSize * p.y, 
							segSize, segSize);
				} else if (i%2 != 0) {
					g.fillOval(5 + segSize * p.x, 
							15 + segSize * p.y, 
							segSize, segSize);
				} else {
					g.fillOval(5 + segSize * p.x + ((2*(CaterpillarGame.drawCount%2))-1)*gap, 
							15 + segSize * p.y, 
							segSize, segSize);
				}
			}
		}
	}
}
