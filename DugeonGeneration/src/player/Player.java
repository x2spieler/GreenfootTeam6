package player;

import greenfoot.Greenfoot;
import AI.IDamageable;

public class Player extends DeltaTimer implements IDamageable {

	private int speed = 500;
	private double epsilonMove = 0.0;

	@Override
	public void damage(int dmg) {

	}

	@Override
	public void act() {
		super.act();

		double move = speed * getDelta() / getWorld().getCellSize();
		if (Greenfoot.isKeyDown(Direction.UP.key)) {
			setGlobalLocation(getGlobalX(), getGlobalY()
					- ensureEventualMove(move));
		} else if (Greenfoot.isKeyDown(Direction.LEFT.key)) {
			setGlobalLocation(getGlobalX() - ensureEventualMove(move),
					getGlobalY());

		} else if (Greenfoot.isKeyDown(Direction.DOWN.key)) {
			setGlobalLocation(getGlobalX(), getGlobalY()
					+ ensureEventualMove(move));

		} else if (Greenfoot.isKeyDown(Direction.RIGHT.key)) {
			setGlobalLocation(getGlobalX() + ensureEventualMove(move),
					getGlobalY());

		}

		centerCamera();
	}

	private int ensureEventualMove(double d) {
		epsilonMove += d;
		if (Math.abs(epsilonMove) > 1) {
			d = epsilonMove;
			if (epsilonMove > 0) {
				epsilonMove -= (int) epsilonMove;
			} else {
				epsilonMove += (int) epsilonMove;
			}
			return (int) d;
		} else {
			return 0;
		}
	}

	private void centerCamera() {
		getWorld().setCameraLocation(getGlobalX(), getGlobalY());
	}

}
