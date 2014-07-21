import java.awt.Point;

public class CaterpillarPosition extends Point {
	
	// initial state is in x-direction
	boolean isDirInX = true;

	public CaterpillarPosition() {
		super();
	}

	public CaterpillarPosition(Point p) {
		this(p, true);
	}
	
	public CaterpillarPosition(Point p, boolean newDir) {
		super(p);
		isDirInX = newDir;
	}

	public CaterpillarPosition(int x, int y) {
		this(x, y, true);
	}
	
	public CaterpillarPosition(int x, int y, boolean newDir) {
		super(x, y);
		isDirInX = newDir;
	}
}
