package camera;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;

import javax.swing.SwingUtilities;

import greenfoot.Greenfoot;
import greenfoot.MouseInfo;
import greenfoot.core.WorldHandler;
import scrollWorld.ScrollActor;

public class MyCursor extends ScrollActor {
	private static final int SPEED = 800;
	private Robot robot;
	private boolean moving = false;
	private long time = System.nanoTime();
	private int targetX = 0;
	private int targetY = 0;

	public MyCursor() throws AWTException {
		super();
		robot = new Robot();
	}

	public void act() {
		double delta = (System.nanoTime() - time) * 0.000000001;
		time = System.nanoTime();

		MouseInfo m = Greenfoot.getMouseInfo();

		if (m != null) {
			if (Greenfoot.mouseMoved(null) && !moving) {
				int x = m.getX();
				int y = m.getY();
				targetX = getWorld().getCameraX() + x - getWorld().getWidth()
						/ 2;
				targetY = getWorld().getCameraY() + y - getWorld().getHeight()
						/ 2;
				setLocation(x, y);
				moving = true;
				// System.out.println(targetX + " " + targetY);
			}

			if (moving && !Greenfoot.mouseMoved(null)) {
				if (targetInBounds()) {
					int distX = targetX - getGlobalX();
					int distY = targetY - getGlobalY();
					// System.out.println(getGlobalX() + " " + getGlobalY());
					double dist = Math.sqrt(distX * distX + distY * distY);
					if (dist > 1.5 * SPEED * delta) {
						double vX = distX / dist * SPEED * delta;
						double vY = distY / dist * SPEED * delta;
						getWorld().setCameraLocation(
								getWorld().getCameraX() + (int) vX,
								getWorld().getCameraY() + (int) vY);

						Point p = new Point(getXFromCamera()
								+ (getWorld().getWidth() / 2), getYFromCamera()
								+ (getWorld().getHeight() / 2));
						// System.out.println(getXFromCamera() + " "
						// + getYFromCamera());
						p.setLocation(p.getX() - vX, p.getY() - vY);
						SwingUtilities.convertPointToScreen(p, WorldHandler
								.getInstance().getWorldCanvas());
						robot.mouseMove((int) p.getX(), (int) p.getY());

						// System.out.println(vX + ", " + vY);
					} else {
						getWorld().setCameraLocation(targetX, targetY);
						moving = false;
					}
				} else {
					moving = false;
				}
			}
		}
		// if (m != null) {
		// setLocation(m.getX(), m.getY());
		// getWorld().setCameraLocation(getGlobalX(), getGlobalY());
		// Point p = new Point(getXFromCamera(), getYFromCamera());
		// SwingUtilities.convertPointToScreen(p, WorldHandler.getInstance()
		// .getWorldCanvas());
		// // System.out.println(p.getX()+", "+p.getY());
		// // if(WorldHandler.getInstance().getWorldCanvas().contains(p1)){
		// }

	}

	private boolean targetInBounds() {
		if (targetX - (getWorld().getWidth() / 2) >= 0
				&& targetY - (getWorld().getHeight() / 2) >= 0
				&& targetX - (getWorld().getWidth() / 2) <= getWorld()
						.getFullWidth() - (getWorld().getWidth() / 2)
				&& targetY - (getWorld().getHeight() / 2) <= getWorld()
						.getFullHeight() - (getWorld().getHeight() / 2)) {
			return true;
		} else {
			return false;
		}
	}
}
