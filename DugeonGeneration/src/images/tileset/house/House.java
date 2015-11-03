package images.tileset.house;

public enum House {
	GROUND(8, 4), WALLRIGHT(7, 2), WALLLEFT(6, 2), WALLFRONT(4, 1), WALLBACK(1, 3), WALLCENTER(5, 0);

	public static final int TILE_SIZE = 32;
	private int x, y;

	House(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
