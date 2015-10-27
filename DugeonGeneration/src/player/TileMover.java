package player;

import greenfoot.World;
import scrollWorld.ScrollActor;
import AI.IWorldInterfaceForAI;

public class TileMover extends ScrollActor {

	private final int tileSize;
	private Move moveQueue;
	private Direction direction;
	private boolean moving;

	public TileMover(int tileSize) {
		super();
		this.tileSize = tileSize;
	}

	public void moveTiled(Direction dir, int amount) {
		if (amount > 0) {
			Move move = new Move(dir, amount);
			if (moving) {
				queueOrExecute(move);
			} else {
				direction = move.getDirection();
				moving = true;
			}
		}
	}

	public void queueOrExecute(Move move) {
		if (isInTile()) {

		} else {

		}
	}

	private boolean isInTile() {
		if (getGlobalX() % tileSize == 0 && getGlobalY() % tileSize == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void addedToWorld(World world) {
		super.addedToWorld(world);
		if (!(world instanceof IWorldInterfaceForAI)) {
			throw new IllegalArgumentException(
					"TileMover must be added to a world implementing IWorldInterfaceForAI");
		} else if (((IWorldInterfaceForAI) world).getTileSize() != tileSize) {
			throw new IllegalArgumentException(
					"Tilesize of mover differs from tilesize of world");
		}
	}

}
