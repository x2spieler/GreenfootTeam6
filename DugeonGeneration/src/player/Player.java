package player;

import greenfoot.Greenfoot;
import AI.IDamageable;

public class Player extends TickIndependentMover implements IDamageable {

	private int speed = 500;

	@Override
	public void damage(int dmg) {

	}

	@Override
	public void act() {
		super.act();

		int move = (int) (speed * getDelta());

		if (Greenfoot.isKeyDown("w")) {
			setGlobalLocation(getGlobalX(), getGlobalY() - move);
		} else if (Greenfoot.isKeyDown("a")) {
			setGlobalLocation(getGlobalX() - move, getGlobalY());

		} else if (Greenfoot.isKeyDown("s")) {
			setGlobalLocation(getGlobalX(), getGlobalY() + move);

		} else if (Greenfoot.isKeyDown("d")) {
			setGlobalLocation(getGlobalX() + move, getGlobalY());

		}

		centerCamera();
	}

	private void centerCamera() {
		getWorld().setCameraLocation(getGlobalX(), getGlobalY());
	}

}
