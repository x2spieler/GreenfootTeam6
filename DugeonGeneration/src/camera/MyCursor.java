package camera;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.MouseInfo;
import greenfoot.World;
import scrollWorld.ScrollActor;
import world.DungeonMap;

public class MyCursor extends ScrollActor {
	private static final int RESTINGXINMAP = DungeonMap.VIEWPORT_WIDTH / 2;
	private static final int RESTINGYINMAP = DungeonMap.VIEWPORT_HEIGHT / 2;
	private static final int MAX_ANCHOR_DISTANCE = 300;
	private int restingXinPane;
	private int restingYinPane;
	private Robot robot;
	private JScrollPane viewPortPane;
	private GreenfootImage cursor;
	private GreenfootImage emptyCursor;

	private ScrollActor anchor;

	private boolean active;

	private int playerXpos;
	private int playerYpos;

	public MyCursor() throws AWTException {
		super();
		this.cursor = new GreenfootImage("default_cursor.png");
		this.emptyCursor = new GreenfootImage(cursor.getWidth(), cursor.getHeight());
		robot = new Robot();
	}

	public void act() {
		super.act();
		int playerX = anchor.getGlobalX();
		int playerY = anchor.getGlobalY();
		if (playerX != playerXpos || playerY != playerYpos) {
			setGlobalLocation(getGlobalX() + playerX - playerXpos, getGlobalY() + playerX - playerYpos);
		} else {
			MouseInfo m = Greenfoot.getMouseInfo();
			if (m != null) {
				int mouseX = m.getX();
				int mouseY = m.getY();
				int cursorX = getX();
				int cursorY = getY();
				int deltaX = mouseX - RESTINGXINMAP;
				int deltaY = mouseY - RESTINGYINMAP;
				boolean test = true;
				if (mouseX != RESTINGXINMAP || mouseY != RESTINGYINMAP) {
					test = false;
					setMousePosition(restingXinPane, restingYinPane);
					setLocation(cursorX + deltaX, cursorY + deltaY);
					centerCamera();
					//getWorld().repaint();
				}
				System.out.println(test + ", " + getGlobalX() + ", " + getGlobalY());
			}
		}
		playerXpos = playerX;
		playerYpos = playerY;
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
			viewPortPane.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty_cursor"));
		} else {
			viewPortPane.setCursor(Cursor.getDefaultCursor());
		}
	}

	@Override
	protected void addedToWorld(World world) {
		super.addedToWorld(world);
		this.viewPortPane = ((DungeonMap) world).getViewPort();

		Dimension viewPortSize = viewPortPane.getSize();
		restingXinPane = viewPortSize.width / 2;
		restingYinPane = viewPortSize.height / 2;
		setMousePosition(restingXinPane, restingYinPane);
		setLocation(RESTINGXINMAP, RESTINGYINMAP);
		setActive(true);
	}

	@Override
	public void setLocation(int x, int y) {
		if (anchor == null)
			return;
		int anchorX = anchor.getX();
		int anchorY = anchor.getY();
		int dX = x - anchorX;
		int dY = y - anchorY;
		if (dX > MAX_ANCHOR_DISTANCE) {
			dX = MAX_ANCHOR_DISTANCE;
		} else if (dX < -MAX_ANCHOR_DISTANCE) {
			dX = -MAX_ANCHOR_DISTANCE;
		}
		if (dY > MAX_ANCHOR_DISTANCE) {
			dY = MAX_ANCHOR_DISTANCE;
		} else if (dY < -MAX_ANCHOR_DISTANCE) {
			dY = -MAX_ANCHOR_DISTANCE;
		}
		super.setLocation(anchorX + dX, anchorY + dY);

	}

	private boolean isInViewport(int x, int y) {
		return x >= 0 && y >= 0 && x < DungeonMap.VIEWPORT_WIDTH && y < DungeonMap.VIEWPORT_HEIGHT;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		if (active)
			setImage(cursor);
		else
			setImage(emptyCursor);
		this.active = active;
		setMouseHidden(active);
	}

	private void centerCamera() {
		getWorld().setCameraLocation(getGlobalX(), getGlobalY());
		setLocation(RESTINGXINMAP, RESTINGYINMAP);
	}

	@Override
	public int getX() {
		return super.getX();
	}

	@Override
	public int getY() {
		return super.getY();
	}

	public void setAnchor(ScrollActor player) {
		this.anchor = player;
		playerXpos = player.getGlobalX();
		playerYpos = player.getGlobalY();
	}
}
