package player;

public class Move {

	private Direction direction;

	public Direction getDirection() {
		return direction;
	}

	public int getMove() {
		return move;
	}

	private int move;

	public Move(Direction direction, int move) {
		this.direction = direction;
		this.move = move;
	}
}
