package world.mapping;

import java.util.function.BiFunction;

import DungeonGeneration.FieldType;
import DungeonGeneration.MapField;

public abstract class BasicDungeonTiler {

	protected MapField[][] map;

	public BasicDungeonTiler(MapField[][] map) {
		super();
		this.map = map;
		int l = map[0].length;
		for (int i = 0; i < map.length; i++) {
			if (map[i].length != l) {
				throw new IllegalArgumentException("cannot work with jagged array");
			}
		}
	}

	public DungeonTile[][] getTilesForMap() {
		DungeonTile[][] ret = new DungeonTile[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				ret[i][j] = getDungeonTileForTile(i, j);
			}
		}
		return ret;
	}

	protected boolean isInMap(int i, int j) {
		if (i >= 0 && i < map.length && j >= 0 && j < map[0].length) {
			return true;
		}
		return false;
	}

	protected boolean isWall(int i, int j) {
		if (!isInMap(i, j))
			return false;
		return map[i][j].getFieldType().equals(FieldType.WALL);
	}

	protected boolean isGround(int i, int j) {
		if (!isInMap(i, j))
			return false;
		return map[i][j].getFieldType().equals(FieldType.GROUND);
	}

	protected boolean forAllAdjacent(int i, int j, BiFunction<Integer, Integer, Boolean> func) {
		return func.apply(i + 1, j) && func.apply(i, j + 1) && func.apply(i - 1, j) && func.apply(i, j - 1);
	}

	protected boolean forAllSurrounding(int i, int j, BiFunction<Integer, Integer, Boolean> func) {
		return forAllDiagonalAdjacent(i, j, func) && forAllAdjacent(i, j, func);
	}

	protected boolean forAllDiagonalAdjacent(int i, int j, BiFunction<Integer, Integer, Boolean> func) {
		return func.apply(i + 1, j + 1) && func.apply(i - 1, j - 1) && func.apply(i + 1, j - 1)
				&& func.apply(i - 1, j + 1);
	}

	protected int countAdjacentGroundTiles(int i, int j) {
		int ret = 0;
		if (isGround(i + 1, j)) {
			ret++;
		}
		if (isGround(i, j + 1)) {
			ret++;
		}
		if (isGround(i - 1, j)) {
			ret++;
		}
		if (isGround(i, j - 1)) {
			ret++;
		}
		return ret;
	}

	protected int countDiagonalAdjacentGroundTiles(int i, int j) {
		int ret = 0;
		if (isGround(i + 1, j + 1)) {
			ret++;
		}
		if (isGround(i - 1, j - 1)) {
			ret++;
		}
		if (isGround(i + 1, j - 1)) {
			ret++;
		}
		if (isGround(i - 1, j + 1)) {
			ret++;
		}
		return ret;
	}

	protected int countAdjacentWallTiles(int i, int j) {
		int ret = 0;
		if (isWall(i + 1, j)) {
			ret++;
		}
		if (isWall(i, j + 1)) {
			ret++;
		}
		if (isWall(i - 1, j)) {
			ret++;
		}
		if (isWall(i, j - 1)) {
			ret++;
		}
		return ret;
	}

	protected int countDiagonalAdjacentWallTiles(int i, int j) {
		int ret = 0;
		if (isWall(i + 1, j + 1)) {
			ret++;
		}
		if (isWall(i - 1, j - 1)) {
			ret++;
		}
		if (isWall(i + 1, j - 1)) {
			ret++;
		}
		if (isWall(i - 1, j + 1)) {
			ret++;
		}
		return ret;
	}

	protected abstract DungeonTile getDungeonTileForTile(int i, int j);
}
