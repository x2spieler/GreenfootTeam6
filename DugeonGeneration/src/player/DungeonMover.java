package player;

import java.awt.Point;

import scrollWorld.ScrollActor;
import AI.IWorldInterfaceForAI;
import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.MapField;

public class DungeonMover extends ScrollActor {

	private boolean firstRun = true;

	@Override
	public void setLocation(int x, int y) {
		// TODO Auto-generated method stub
		super.setLocation(x, y);
	}

	@Override
	public void setLocationFromCamera(int x, int y) {
		// TODO Auto-generated method stub
		super.setLocationFromCamera(x, y);
	}

	@Override
	public void setGlobalLocation(int x, int y) {
		Point p = new Point(x, y);
		ensureWithinPassableTile(p);
		super.setGlobalLocation(p.x, p.y);
	}

	@Override
	public void move(int distance) {
		// TODO Auto-generated method stub
		super.move(distance);
	}

	private void ensureWithinPassableTile(Point p) {
		if (!isInPassableTile(p)) {
			p.setLocation(getGlobalX(), getGlobalY());
		}
	}

	private boolean isInPassableTile(Point p) {
		IWorldInterfaceForAI world = (IWorldInterfaceForAI) getWorld();
		MapField[][] map = world.getMap();
		int tileSize = world.getTileSize();

		return map[p.x / tileSize][p.y / tileSize].walkable();
	}

	@Override
	public void act() {
		super.act();
		if (firstRun
				&& !isInPassableTile(new Point(getGlobalX(), getGlobalY()))) {
			Point p = getNearestWalkablePoint(new Point(getGlobalX(),
					getGlobalY()));
			super.setGlobalLocation(p.x, p.y);
			firstRun = false;
		}
	}

	private Point ensureWithinWalkableArea(int dX, int dY) {
		Point ret = new Point(getGlobalX(), getGlobalY());
		IWorldInterfaceForAI world = (IWorldInterfaceForAI) getWorld();
		MapField[][] map = world.getMap();
		int tileSize = world.getTileSize();
		if (map[ret.x / tileSize][ret.y / tileSize].walkable()) {
			System.out.println("walk");
			ret.setLocation(dX, dY);
			if (map[ret.x / tileSize
					+ ((ret.x % tileSize + tileSize - 1) / tileSize)][ret.y
					/ tileSize + ((ret.y % tileSize + tileSize - 1) / tileSize)]
					.walkable()) {
				System.out.println("sehr gut");
				return ret;
			} else {
				return getNearestWalkablePoint(ret);
			}
		} else {
			System.out.println("nix gut");
			Point temp = getNearestWalkablePoint(ret);
			super.setGlobalLocation(temp.x, temp.y);
			return ensureWithinWalkableArea(dX, dY);
		}
	}

	private Point getNearestWalkablePoint(Point p) {
		IWorldInterfaceForAI world = (IWorldInterfaceForAI) getWorld();
		MapField[][] map = world.getMap();
		int tileSize = world.getTileSize();
		Point ret = new Point(p.x / tileSize, p.y / tileSize);

		int smallestDX = DungeonGenerator.MAP_WIDTH;
		int smallestDY = DungeonGenerator.MAP_HEIGHT;

		for (int i = 0; i < DungeonGenerator.MAP_WIDTH; i++) {
			for (int j = 0; j < DungeonGenerator.MAP_HEIGHT; j++) {
				if (map[i][j].walkable()) {
					int dX = ret.x - i;
					int dY = ret.y - j;
					if (Math.sqrt(dX * dX + dY * dY) < Math.sqrt(smallestDX
							* smallestDX + smallestDY * smallestDY)) {
						smallestDX = dX;
						smallestDY = dY;
					}
				}
			}
		}
		ret.x -= smallestDX;
		ret.y -= smallestDY;

		ret.x *= tileSize;
		ret.y *= tileSize;
		return ret;
	}

}
