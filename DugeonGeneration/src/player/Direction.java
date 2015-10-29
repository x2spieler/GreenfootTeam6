package player;

public enum Direction {
	UP("w"), LEFT("a"), DOWN("s"), RIGHT("d");

	public final String key;

	private Direction(String key) {
		this.key = key;
	}
}
