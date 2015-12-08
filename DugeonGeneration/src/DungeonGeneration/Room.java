package DungeonGeneration;

import java.awt.Point;

public class Room {

	private int sizeX = 5;
	private int sizeY = 5;

	private Point pos = new Point();
	private Point center = new Point();

	public Room (int sizeX, int sizeY) {

		this.sizeX = sizeX;
		this.sizeY = sizeY;


	}

	public void setPosition(int x, int y) {

		pos.x = x;
		pos.y = y;
		center.x = pos.x + sizeX/2;
		center.y = pos.y + sizeY/2;

	}

	public void setPosition(Point pos) {

		this.pos = new Point(pos.x, pos.y);

	}

	public Point getCenter() {

		return center;

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
