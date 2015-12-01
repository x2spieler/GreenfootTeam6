package camera;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.MouseInfo;
import world.DungeonMap;

public class MouseController {
	private static final int RESTINGXINMAP = DungeonMap.VIEWPORT_WIDTH / 2;
	private static final int RESTINGYINMAP = DungeonMap.VIEWPORT_HEIGHT / 2;
	private static final int MAX_ANCHOR_DISTANCE = 900;
	private int restingXinPane;
	private int restingYinPane;
	private int cursorCenter;
	private Robot robot;
	private DungeonMap world;
	private JScrollPane viewPortPane;
	private GreenfootImage cursor;
	private GreenfootImage emptyCursor;

	private boolean active;

	public MouseController(DungeonMap world) throws AWTException {
		this.world = world;
		this.cursor = new GreenfootImage("default_cursor.png");
		this.emptyCursor = new GreenfootImage(cursor.getWidth(), cursor.getHeight());
		robot = new Robot();
		cursorCenter = cursor.getHeight() / 2;
	}

	public void start() {

	}

	public void stop() {

	}

	public void act() {
		MouseInfo m = Greenfoot.getMouseInfo();
		if (m != null) {
			int mouseX = m.getX();
			int mouseY = m.getY();
			int deltaX = mouseX - RESTINGXINMAP;
			int deltaY = mouseY - RESTINGYINMAP;

			if (mouseX != RESTINGXINMAP || mouseY != RESTINGYINMAP) {
				setMousePosition(restingXinPane, restingYinPane);
				//getWorld().repaint();
			}
			//centerCamera();
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
			viewPortPane.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty_cursor"));
		} else {
			viewPortPane.setCursor(Cursor.getDefaultCursor());
		}
	}

	//	@Override
	//	protected void addedToWorld(World world) {
	//		this.viewPortPane = ((DungeonMap) world).getViewPort();
	//
	//		Dimension viewPortSize = viewPortPane.getSize();
	//		restingXinPane = viewPortSize.width / 2;
	//		restingYinPane = viewPortSize.height / 2;
	//		setMousePosition(restingXinPane, restingYinPane);
	//	}

	//	@Override
	//	public void setLocation(int x, int y) {
	//		if (anchor == null)
	//			return;
	//		int anchorX = anchor.getX();
	//		int anchorY = anchor.getY();
	//		int dX = x - anchorX;
	//		int dY = y - anchorY;
	//		double delta = Math.sqrt(x * x + y * y);
	//		if ((delta > MAX_ANCHOR_DISTANCE)) {
	//			double factor = MAX_ANCHOR_DISTANCE / delta;
	//			dX = (int) Math.sqrt(dX * dX * factor);
	//			dY = (int) Math.sqrt(dY * dY * factor);
	//			super.setLocation(anchorX + cursorCenter, anchorY + cursorCenter);
	//		} else {
	//			super.setLocation(x, y);
	//		}
	//	}

	private boolean isInViewport(int x, int y) {
		return x >= 0 && y >= 0 && x < DungeonMap.VIEWPORT_WIDTH && y < DungeonMap.VIEWPORT_HEIGHT;
	}

}
