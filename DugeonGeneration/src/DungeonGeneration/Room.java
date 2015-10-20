package DungeonGeneration;

import java.awt.Point;

public class Room {

	private int sizeX = 5;
	private int sizeY = 5;
	
	private Point pos = new Point();
		
	public Room (int sizeX, int sizeY) {
		
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		
	
	}
	
	public void setPosition(int x, int y) {
		
		pos.x = x;
		pos.y = y;
		
	}
	
	public void setPosition(Point pos) {
		
		this.pos = new Point(pos.x, pos.y);
		
	}
	
	public Point getPosition() {
		
		return pos;
		
	}
		
	public int getSizeX() {
		
		return sizeX;
	}
	
	public int getSizeY() {
		
		return sizeY;
	}
	
}
