package player;

import scrollWorld.ScrollActor;

public class DeltaTimer extends ScrollActor {
	private long time = System.nanoTime();
	private double delta;

	public double getDelta() {
		return delta;
	}

	private void setDeltaTime() {
		delta = (System.nanoTime() - time) * 0.000000001;
		time = System.nanoTime();
	}

	@Override
	public void act() {
		super.act();
		setDeltaTime();
	}

}
