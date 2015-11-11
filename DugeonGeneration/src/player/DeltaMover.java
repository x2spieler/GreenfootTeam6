package player;

public class DeltaMover extends DungeonMover {

	private long time = System.currentTimeMillis();
	private double delta;

	private int speed;
	private double epsilonMove = 0.0;
	private boolean epsilonSetThisTick = false;

	/**
	 * @param speed
	 *            the speed of the Mover in pixels per second.
	 */
	public DeltaMover(int speed) {
		super();
		this.speed = speed;
	}

	public double getDelta() {
		return delta;
	}

	private void update() {
		delta = ((double) (System.currentTimeMillis() - time)) / 1000;
		time = System.currentTimeMillis();
	}
	
	/**
	 * Called when game is resumed, otherwise the delta time will be way too big due to the game having been paused
	 */
	public void restart()
	{
		time=System.currentTimeMillis();
	}

	/**
	 * When overriding you will need to call super on this, or DeltaMover will
	 * stop working properly.
	 */
	@Override
	public void act() {
		super.act();
		update();
		epsilonSetThisTick = false;
	}

	/**
	 * Only meant to enable slower than normal movement without changing the
	 * movers speed. Will not move any farther than the DeltaMovers speed * the
	 * deltatime in pixels. Use any of the setLocation methods if you want to
	 * move faster without changing the speed.
	 */
	@Override
	public void move(int distance) {
		moveAtAngle(distance, getRotation());
	}

	@Override
	public void moveAtAngle(int distance, int angle) {
		int move = getTickMove();
		if (distance <= move) {
			super.moveAtAngle(distance, angle);
		} else {
			super.moveAtAngle(move, angle);
			;
		}
	}

	public void moveAtAngle(int angle) {
		moveAtAngle(getTickMove(), angle);
	}

	/**
	 * The primary purpose of this class. calling this once per tick will cause
	 * the mover to move at its set speed in whichever direction it is facing.
	 */
	public void move() {
		super.move(getTickMove());
	}

	/**
	 * Conveniance method so it isn't neccesary to use move(int).
	 */
	public void moveBackwards() {
		super.move(-getTickMove());
	}

	protected int getTickMove() {
		return (int) ensureEventualMove(speed * delta);
	}

	private int ensureEventualMove(double d) {
		if (epsilonSetThisTick)
			return (int) d;
		epsilonSetThisTick = true;
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

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
