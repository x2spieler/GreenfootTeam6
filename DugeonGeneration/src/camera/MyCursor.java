package camera;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.MouseInfo;
import greenfoot.World;
import scrollWorld.ScrollActor;
import scrollWorld.ScrollWorld;
import world.DungeonMap;

public class MyCursor extends ScrollActor {
	public static final int RESTINGXINMAP = DungeonMap.VIEWPORT_WIDTH / 2;
	public static final int RESTINGYINMAP = DungeonMap.VIEWPORT_HEIGHT / 2;
	public static final Polygon DEFAULT_BOUNDS = new Polygon(new int[] { 0, RESTINGXINMAP, RESTINGXINMAP, 0 }, new int[] { 0, 0, RESTINGYINMAP, RESTINGYINMAP }, 4);
	private int restingXinPane;
	private int restingYinPane;
	private Robot robot;
	private JScrollPane viewPortPane;
	private GreenfootImage cursor;

	private Polygon bounds;

	private int anchorX;
	private int anchorY;
	private int lastAnchorX;
	private int lastAnchorY;

	public MyCursor(JScrollPane viewPortPane) throws AWTException {
		super();
		this.robot = new Robot();
		setViewPortPane(viewPortPane);
		this.cursor = new GreenfootImage("default_cursor.png");
		setImage(cursor);
		this.bounds = new Polygon(new int[] { 0, RESTINGXINMAP * 2, RESTINGXINMAP * 2, 0 }, new int[] { 0, 0, RESTINGYINMAP * 2, RESTINGYINMAP * 2 }, 4);
	}

	public void act() {
		super.act();
		updateCursor();
	}

	public void updateCursor() {
		super.act();
		int x = getX();
		int y = getY();
		int anctorxInViewport = anchorX - getGlobalX();
		int axtoryInViewport = anchorY - getGlobalY();
		SetBoundsLocation(anctorxInViewport, anctorxInViewport);
		moveTo(RESTINGXINMAP + anchorX - lastAnchorX, RESTINGYINMAP + anchorY - lastAnchorY);
		//		if (!bounds.contains(x, y)) {
		//			moveTo(anchorX, anchorY);
		//		}
		MouseInfo m = Greenfoot.getMouseInfo();
		if (m != null) {
			int mouseX = m.getX();
			int mouseY = m.getY();
			if (mouseX != RESTINGXINMAP || mouseY != RESTINGYINMAP) {
				setMousePosition(restingXinPane, restingYinPane);
				moveTo(mouseX, mouseY);
			}
		}
	}

	private void setMousePosition(int x, int y) {
		if (viewPortPane == null)
			return;
		Point target = new Point(x, y);
		SwingUtilities.convertPointToScreen(target, viewPortPane);
		robot.mouseMove(target.x, target.y);
	}

	private void setMouseHidden(boolean hidden) {
		if (viewPortPane == null)
			return;
		if (hidden) {
			viewPortPane.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty_cursor"));
		} else {
			viewPortPane.setCursor(Cursor.getDefaultCursor());
		}
	}

	protected void setViewPortPane(JScrollPane viewPortPane) {
		this.viewPortPane = viewPortPane;

		restingXinPane = viewPortPane.getWidth() / 2;
		restingYinPane = viewPortPane.getHeight() / 2;
		setMousePosition(restingXinPane, restingYinPane);
		setMouseHidden(true);
	}

	public void moveTo(int x, int y) {
		if (getWorld() == null)
			return;
		//if (bounds.contains(x, y)) {
		ScrollWorld world = getWorld();
		world.setCameraLocation(world.getCameraX() + x - RESTINGXINMAP, world.getCameraY() + y - RESTINGYINMAP);
		bounds.translate(RESTINGXINMAP - x, RESTINGYINMAP - y);
		//}
	}

	public void updateAnchorLocation(int x, int y) {
		if (getWorld() == null)
			return;
		//if (x != anchorX || y != anchorY) {
		//			ScrollWorld world = getWorld();
		//			int xInViewport = x - world.getCameraX() + RESTINGXINMAP;
		//			int yInViewport = y - world.getCameraY() + RESTINGYINMAP;
		//			if (bounds.contains(xInViewport, yInViewport)) {
		//				int deltaX = x - anchorX;
		//				int deltaY = y - anchorY;
		//				bounds.translate(deltaX, deltaY);
		//				moveTo(RESTINGXINMAP + deltaX, RESTINGYINMAP + deltaY);
		//			} else {
		//				SetBoundsLocation(xInViewport, yInViewport);
		//				moveTo(xInViewport, yInViewport);
		//			}
		if (!(lastAnchorX == 0 && lastAnchorY == 0 && anchorX == 0 && anchorY == 0)) {
			this.lastAnchorX = anchorX;
			this.lastAnchorY = anchorY;
			this.anchorX = x;
			this.anchorY = y;
		} else {
			lastAnchorX = anchorX = x;
			lastAnchorY = anchorY = y;
		}
		//}
	}

	private void SetBoundsLocation(int x, int y) {
		Rectangle r = bounds.getBounds();
		if (r.getX() != 0 || r.getY() != 0) {
			bounds.translate((int) (0 - r.getX()), (int) (0 - r.getY()));
		}
		int dy = y - r.height / 2;
		if (bounds.npoints == 3) {
			bounds.translate(anchorX, dy);
		} else if (bounds.npoints == 4) {
			int dx = x - r.width / 2;
			bounds.translate(dx, dy);
		}
	}

	public void setBoundingArea(Polygon bounds) {
		if (bounds.npoints == 3 || bounds.npoints == 4) {
			this.bounds = bounds;
			SetBoundsLocation(RESTINGXINMAP, RESTINGYINMAP);
		} else {
			throw new IllegalArgumentException("cursor bounding area must be a triangle or a rectangle");
		}
	}

	@Override
	public void setLocation(int x, int y) {
	}
}
