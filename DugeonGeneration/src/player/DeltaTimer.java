package player;


public class DeltaTimer {
	private long time = System.nanoTime();
	private double delta;

	public double getDelta() {
		return delta;
	}

	private void setDeltaTime() {
		delta = (System.nanoTime() - time) * 0.000000001;
		time = System.nanoTime();
	}

	public void update() {
		setDeltaTime();
	}

}
