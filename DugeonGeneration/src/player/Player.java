package player;

import greenfoot.Greenfoot;
import greenfoot.MouseInfo;
import AI.IDamageable;

public class Player extends DeltaMover implements IDamageable {

	private Direction buttonPressed = null;
	private Direction currentDirection = null;

	public Player() {
		super(400);
	}

	@Override
	public void damage(int dmg) {

	}

	@Override
	public void act() {
		super.act();
		faceMouse();

		boolean forward = Greenfoot.isKeyDown(Direction.FORWARD.key);
		boolean backward = Greenfoot.isKeyDown(Direction.BACKWARD.key);
		boolean right = Greenfoot.isKeyDown(Direction.RIGHT.key);
		boolean left = Greenfoot.isKeyDown(Direction.LEFT.key);

		if (forward && backward) {
			forward = false;
			backward = false;
		}
		if (left && right) {
			left = false;
			right = false;
		}

		buttonPressed = getCurrentDirection();
		if (buttonPressed != null) {
			currentDirection = buttonPressed;
		} else {
			if (forward && !right && !left) {
				currentDirection = Direction.FORWARD;
			}
			if (backward && !right && !left) {
				currentDirection = Direction.BACKWARD;
			}
			if (!forward && !backward && right) {
				currentDirection = Direction.RIGHT;
			}
			if (!forward && !backward && left) {
				currentDirection = Direction.LEFT;
			}
		}

		if ((forward && currentDirection == Direction.FORWARD)) {
			move();
		}
		if ((backward && currentDirection == Direction.BACKWARD)) {
			moveBackwards();
		}
		if ((right && currentDirection == Direction.RIGHT)) {
			int move = getTickMove();
			moveAtAngle(move - (move / 4), 90);
		}
		if ((left && currentDirection == Direction.LEFT)) {
			int move = getTickMove();
			moveAtAngle(move - (move / 4), 270);
		}

		centerCamera();
	}

	private Direction getCurrentDirection() {
		String key = Greenfoot.getKey();
		for (Direction direction : Direction.values()) {
			if (key != null && key.equals(direction.key)) {
				return direction;
			}
		}
		return null;
	}

	private void faceMouse() {
		MouseInfo info = Greenfoot.getMouseInfo();
		if (info != null) {
			int x = info.getX();
			int y = info.getY();
			turnTowards(x, y);
		}
	}

	private void centerCamera() {
		getWorld().setCameraLocation(getGlobalX(), getGlobalY());
	}

}
