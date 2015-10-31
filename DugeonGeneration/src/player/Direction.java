package player;

public enum Direction {
	FORWARD("w"), LEFT("a"), BACKWARD("s"), RIGHT("d");

	public final String key;

	private Direction(String key) {
		this.key = key;
	}
}
