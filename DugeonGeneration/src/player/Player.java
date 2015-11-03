package player;

import greenfoot.Greenfoot;
import greenfoot.MouseInfo;
import AI.IDamageable;

public class Player extends DeltaMover implements IDamageable {

	public static final int MOVE_MODE_FOLLOW_MOUSE = 0;
	public static final int MOVE_MODE_8_DIRECTIONS = 1;
	public static final int MOVE_MODE_8_DIRECTIONS_POINT_AT_MOUSE = 2;
	public static final int MOVE_MODE_MOUSE_KEYBOARD_HYBRID = 3;

	private int mode = MOVE_MODE_FOLLOW_MOUSE;

	private Direction buttonPressed = null;
	private Direction currentDirection = null;

	private boolean forward = false;
	private boolean backward = false;
	private boolean right = false;
	private boolean left = false;

	public Player() {
		super(400);
	}

	@Override
	public void damage(int dmg)
	{
		System.out.println("Ouch! " + dmg + " damage taken.");
	}
	
	@Override
	public int getHP()
	{
		return -1;
	}

	@Override
	public void act() {
		super.act();

		getKeysDown();

		if (mode == MOVE_MODE_FOLLOW_MOUSE) {
			moveAccordingToMouse();
		} else if (mode == MOVE_MODE_8_DIRECTIONS) {
			moveInOneOf8Directions();
		} else if (mode == MOVE_MODE_8_DIRECTIONS_POINT_AT_MOUSE) {
			moveInOneOf8DirectionsFacingMouse();
		} else if (mode == MOVE_MODE_MOUSE_KEYBOARD_HYBRID) {
			moveMouseKeyboardHybrid();
		}

		centerCamera();
	}

	private void moveMouseKeyboardHybrid() {
		faceMouse();
		getDominantDirection();
		if ((forward && currentDirection == Direction.FORWARD)) {
			move();
		}
		if ((backward && currentDirection == Direction.BACKWARD)) {
			moveBackwards();
		}
		if ((right && currentDirection == Direction.RIGHT)) {
			int move = getTickMove();
			moveAtAngle(move - move / 2, getAngleForRotation(getRotation(), Direction.RIGHT));
		}
		if ((left && currentDirection == Direction.LEFT)) {
			int move = getTickMove();
			moveAtAngle(move - move / 2, getAngleForRotation(getRotation(), Direction.LEFT));
		}
	}

	private int getAngleForRotation(int rotation, Direction dir) {
		if (rotation <= 45 || rotation > 315 || (rotation <= 225 && rotation > 135)) {
			return (dir == Direction.RIGHT) ? 90 : 270;
		}
		if ((rotation > 45 && rotation <= 135) || (rotation <= 315 && rotation > 225)) {
			return (dir == Direction.RIGHT) ? 0 : 180;
		}
		return 0;
	}

	private void moveInOneOf8DirectionsFacingMouse() {
		faceMouse();
		if (forward && !right && !left) {
			moveAtAngle(270);
		}
		if (backward && !right && !left) {
			moveAtAngle(90);
		}
		if (right && !forward && !backward) {
			moveAtAngle(0);
		}
		if (left && !forward && !backward) {
			moveAtAngle(180);
		}
		if (right && forward) {
			moveAtAngle(315);
		}
		if (right && backward) {
			moveAtAngle(45);
		}
		if (left && forward) {
			moveAtAngle(225);
		}
		if (left && backward) {
			moveAtAngle(135);
		}
	}

	private void moveInOneOf8Directions() {
		if (forward && !right && !left) {
			setRotation(270);
			move();
		}
		if (backward && !right && !left) {
			setRotation(90);
			move();
		}
		if (right && !forward && !backward) {
			setRotation(0);
			move();
		}
		if (left && !forward && !backward) {
			setRotation(180);
			move();
		}
		if (right && forward) {
			setRotation(315);
			move();
		}
		if (right && backward) {
			setRotation(45);
			move();
		}
		if (left && forward) {
			setRotation(225);
			move();
		}
		if (left && backward) {
			setRotation(135);
			move();
		}
		if (!right && !left && !forward && !backward) {
			faceMouse();
		}
	}

	private void getDominantDirection() {
		buttonPressed = getLastKeyPressed();
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
	}

	private void moveAccordingToMouse() {
		faceMouse();
		getDominantDirection();
		if ((forward && currentDirection == Direction.FORWARD)) {
			move();
		}
		if ((backward && currentDirection == Direction.BACKWARD)) {
			moveBackwards();
		}
		if ((right && currentDirection == Direction.RIGHT)) {
			int move = getTickMove();
			moveAtAngle(move - (move / 4), addAngles(getRotation(), 90));
		}
		if ((left && currentDirection == Direction.LEFT)) {
			int move = getTickMove();
			moveAtAngle(move - (move / 4), addAngles(getRotation(), 270));
		}
	}

	private void getKeysDown() {
		forward = Greenfoot.isKeyDown(Direction.FORWARD.key);
		backward = Greenfoot.isKeyDown(Direction.BACKWARD.key);
		right = Greenfoot.isKeyDown(Direction.RIGHT.key);
		left = Greenfoot.isKeyDown(Direction.LEFT.key);
		if (forward && backward) {
			forward = false;
			backward = false;
		}
		if (left && right) {
			left = false;
			right = false;
		}
	}

	private Direction getLastKeyPressed() {
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

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		if (mode >= 0 && mode < 4)
			this.mode = mode;
	}

}
