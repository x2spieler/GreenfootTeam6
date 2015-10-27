package player;

public enum Direction {
	UP("w"), DOWN("s"), RIGHT("d"), LEFT("a");

	public final String key;

	private Direction(String key) {
		this.key = key;
	}
}
