package camera;

import greenfoot.World;

import java.awt.AWTException;
import java.awt.Polygon;

import javax.swing.JScrollPane;

import player.DeltaMover;
import scrollWorld.ScrollWorld;

public class ActorWithCursor extends DeltaMover {

	protected static final Polygon CORNER, LEFT, UP, RIGHT, DOWN;
	static {
		int xQuarter = MyCursor.RESTINGXINMAP / 2;
		int yQuarter = MyCursor.RESTINGYINMAP / 2;
		CORNER = new Polygon(new int[] { 0, xQuarter, xQuarter, 0 }, new int[] { 0, 0, yQuarter, yQuarter }, 4);
		UP = new Polygon(new int[] { 0, MyCursor.RESTINGXINMAP, xQuarter }, new int[] { 0, 0, yQuarter }, 3);
		DOWN = new Polygon(new int[] { 0, MyCursor.RESTINGXINMAP, xQuarter }, new int[] { 0, 0, -yQuarter }, 3);
		RIGHT = new Polygon(new int[] { 0, xQuarter, xQuarter }, new int[] { yQuarter, 0, MyCursor.RESTINGYINMAP }, 3);
		LEFT = new Polygon(new int[] { 0, -xQuarter, -xQuarter }, new int[] { yQuarter, 0, MyCursor.RESTINGYINMAP }, 3);
	}

	protected MyCursor cursor;

	public ActorWithCursor(JScrollPane viewPortPane, int speed, boolean sliding, int... extent) throws AWTException {
		super(speed, sliding, extent);
		this.cursor = new MyCursor(viewPortPane);
	}

	@Override
	protected void addedToWorld(World world) {
		super.addedToWorld(world);
		((ScrollWorld) world).addCameraFollower(cursor, 0, 0);
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		cursor.updateAnchorLocation(getGlobalX(), getGlobalY());
	}

}
