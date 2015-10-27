package camera;

import greenfoot.Greenfoot;
import greenfoot.MouseInfo; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.awt.AWTException;
import java.awt.Robot;

import scrollWorld.ScrollActor;

/**
 * Write a description of class MyCursor here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class MyCursor extends ScrollActor {
	private static final int SPEED = 100;
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
		System.out.println(delta);
		time = System.nanoTime();

		MouseInfo m = Greenfoot.getMouseInfo();

		if (m != null) {
			if (Greenfoot.mouseMoved(this.getWorld())) {
				targetX = m.getX();
				targetY = m.getY();
				moving = true;
			}
		}

		if (moving) {
			int distX = targetX - getXFromCamera();
			int distY = targetY - getYFromCamera();
			double dist = Math.sqrt(distX * distX + distY * distY);
			if (dist > 2 * SPEED * delta) {
				double vX = distX / dist * SPEED * delta;
				double vY = distY / dist * SPEED * delta;
				getWorld().setCameraLocation(
						getWorld().getCameraX() + (int) vX,
						getWorld().getCameraY() + (int) vY);
				// System.out.println(vX + ", " + vY);
			} else {
				getWorld().setCameraLocation(targetX, targetY);
				moving = false;
			}
		}
		// System.out.println(targetX + ", " + targetY);

		/*
		 * if(m!=null){ setLocation(m.getX(), m.getY());
		 * getWorld().setCameraLocation(getGlobalX(), getGlobalY()); Point p1 =
		 * new Point(getXFromCamera(),getYFromCamera()); Point p = new
		 * Point(300,200); SwingUtilities.convertPointToScreen(p,
		 * WorldHandler.getInstance().getWorldCanvas());
		 * //System.out.println(p.getX()+", "+p.getY());
		 * //if(WorldHandler.getInstance().getWorldCanvas().contains(p1)){
		 * robot.mouseMove((int)p.getX(), (int)p.getY()); //}
		 */

	}
}
