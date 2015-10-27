package camera;

import greenfoot.Greenfoot;

import java.awt.AWTException;

import scrollWorld.FPS;
import scrollWorld.ScrollWorld;

/**
 * A little demo world to show you how this works.
 * 
 * @author Sven van Nigtevecht
 */
public class DemoWorld extends ScrollWorld {
	/**
	 * Constructor for objects of class DemoWorld.
	 */
	public DemoWorld() {
		super(800, 600, 1, 6000, 6000);

		try {
			addCameraFollower(new MyCursor(), 0, 0);
		} catch (AWTException ex) {
			Greenfoot.stop();
			System.err.println("no Robot #_#");
		}

		addObject(new FPS(), 85, 15); // FPS isn't a subclass of
		// ScrollActor, so it will looklike it's a camera follower
	}

	@Override
	public void act() {
		if (Greenfoot.getKey() == "escape") {
			Greenfoot.stop();
		}
	}
}