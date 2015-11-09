package world.mapping;

import java.util.EnumSet;
import java.util.function.BiFunction;

import world.mapping.DungeonTile.TileAttribute;
import DungeonGeneration.FieldType;
import DungeonGeneration.MapField;

public class DungeonTiler {

	private MapField[][] map;

	public DungeonTiler(MapField[][] map) {
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

	private DungeonTile getDungeonTileForTile(int i, int j) {
		EnumSet<TileAttribute> attributes = EnumSet.noneOf(TileAttribute.class);
		if (isGround(i, j)) {
			attributes.add(TileAttribute.ground);
		} else if (isWall(i, j)) {
			attributes.add(TileAttribute.wall);
		} else {
			attributes.add(TileAttribute.unknown);
			return new DungeonTile(attributes);
		}
		if (isCorner(i, j)) {
			attributes.add(TileAttribute.corner);
		}
		if (isEdge(i, j)) {
			attributes.add(TileAttribute.edge);
		}
		if (isCenter(i, j)) {
			attributes.add(TileAttribute.center);
		}
		if (isFacingLeft(i, j)) {
			attributes.add(TileAttribute.facingleft);
		}
		if (isFacingRight(i, j)) {
			attributes.add(TileAttribute.facingright);
		}
		if (isFacingUp(i, j)) {
			attributes.add(TileAttribute.facingup);
		}
		if (isFacingDown(i, j)) {
			attributes.add(TileAttribute.facingdown);
		}
		if (isAttachingLeft(i, j)) {
			attributes.add(TileAttribute.attachesleft);
		}
		if (isAttachingRight(i, j)) {
			attributes.add(TileAttribute.attachesright);
		}
		if (isAttachingUp(i, j)) {
			attributes.add(TileAttribute.attachesup);
		}
		if (isAttachingDown(i, j)) {
			attributes.add(TileAttribute.attachesdown);
		}

		return new DungeonTile(attributes);
	}

	private boolean isAttachingDown(int i, int j) {
		if (!isInMap(i, j))
			return false;
		if (isWall(i, j)) {
			if (isGround(i, j + 0) || isCorner(i, j + 1) || (isCorner(i, j) && isWall(i, j + 1))) {
				return true;
			}
		} else if (isGround(i, j)) {
			if (isWall(i, j + 1) || isCorner(i, j) && isWall(i + 1, j + 1) || isWall(i - 1, j + 1)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAttachingUp(int i, int j) {
		if (!isInMap(i, j))
			return false;
		if (isWall(i, j)) {
			if (isGround(i, j - 1) || isGround(i, j + 1) || (isCorner(i, j) && isWall(i, j - 1))
					|| (isCorner(i, j - 1) && (isWall(i + 1, j - 1) || isWall(i - 1, j - 1)))) {
				return true;
			}
		} else if (isGround(i, j)) {
			if (isWall(i, j - 1)
					|| (isCorner(i, j) && isWall(i + 1, j - 1) || isWall(i - 1, j - 1))) {
				return true;
			}
		}
		return false;
	}

	private boolean isAttachingRight(int i, int j) {
		if (!isInMap(i, j))
			return false;
		if (isWall(i, j)) {
			if (isGround(i + 1, j)
					|| (isCorner(i + 1, j) && countAdjacentGroundTiles(i + 1, j) == 0)
					|| (isCorner(i, j) && isWall(i + 1, j) && countAdjacentGroundTiles(i, j) == 0)) {
				return true;
			}
		} else if (isGround(i, j)) {
			if (isWall(i + 1, j)
					|| (isCorner(i, j) && (isWall(i + 1, j + 1) || isWall(i + 1, j - 1)))) {
				return true;
			}
		}
		return false;
	}

	private boolean isAttachingLeft(int i, int j) {
		if (!isInMap(i, j))
			return false;
		if (isWall(i, j)) {
			if (isGround(i - 1, j)
					|| (isCorner(i - 1, j) && countAdjacentGroundTiles(i - 1, j) == 0)
					|| (isCorner(i, j) && isWall(i - 1, j) && countAdjacentGroundTiles(i, j) == 0)) {
				return true;
			}
		} else if (isGround(i, j)) {
			if (isWall(i - 1, j)
					|| (isCorner(i, j) && (isWall(i - 1, j + 1) || isWall(i - 1, j - 1)))) {
				return true;
			}
		}
		return false;
	}

	private boolean isWall(int i, int j) {
		if (!isInMap(i, j))
			return false;
		return map[i][j].getFieldType().equals(FieldType.WALL);
	}

	private boolean isGround(int i, int j) {
		if (!isInMap(i, j))
			return false;
		return map[i][j].getFieldType().equals(FieldType.GROUND);
	}

	private boolean isFacingDown(int i, int j) {
		if (!isInMap(i, j))
			return false;
		if (isWall(i, j) && isGround(i, j + 1)) {
			return true;
		}
		if (isGround(i, j) && isWall(i, j + 1)) {
			return true;
		}
		return false;
	}

	private boolean isFacingUp(int i, int j) {
		if (!isInMap(i, j))
			return false;
		if (isWall(i, j) && isGround(i, j - 1)) {
			return true;
		}
		if (isGround(i, j) && isWall(i, j - 1)) {
			return true;
		}
		return false;
	}

	private boolean isFacingRight(int i, int j) {
		if (!isInMap(i, j))
			return false;
		if (isWall(i, j) && isGround(i + 1, j)) {
			return true;
		}
		if (isGround(i, j) && isWall(i + 1, j)) {
			return true;
		}
		return false;
	}

	private boolean isFacingLeft(int i, int j) {
		if (!isInMap(i, j))
			return false;
		if (isWall(i, j) && isGround(i - 1, j)) {
			return true;
		}
		if (isGround(i, j) && isWall(i - 1, j)) {
			return true;
		}
		return false;
	}

	private boolean isCenter(int i, int j) {
		if (!isInMap(i, j))
			return false;
		;
		if (isWall(i, j) && forAllSurrounding(i, j, (n, m) -> isWall(n, m))) {
			return true;
		}
		if (isGround(i, j) && forAllSurrounding(i, j, (n, m) -> isGround(n, m))) {
			return true;
		}
		return false;
	}

	private boolean isEdge(int i, int j) {
		if (!isInMap(i, j))
			return false;
		if (isGround(i, j) && isGround(i, j + 1) && isGround(i, j - 1)
				&& (isWall(i + 1, j) || isWall(i - 1, j))) {
			return true;
		}
		if (isGround(i, j) && isGround(i + 1, j) && isGround(i - 1, j)
				&& (isWall(i, j + 1) || isWall(i, j - 1))) {
			return true;
		}
		if (isWall(i, j) && isWall(i, j + 1) && isWall(i, j - 1)
				&& (isGround(i + 1, j) || isGround(i - 1, j))) {
			return true;
		}
		if (isWall(i, j) && isWall(i + 1, j) && isWall(i - 1, j)
				&& (isGround(i, j + 1) || isGround(i, j - 1))) {
			return true;
		}
		return false;
	}

	private boolean isCorner(int i, int j) {
		if (!isInMap(i, j))
			return false;
		if (isWall(i, j)) {
			if (countAdjacentGroundTiles(i, j) == 0 && countDiagonalAdjacentGroundTiles(i, j) > 0) {
				return true;
			}
			if (countAdjacentGroundTiles(i, j) >= 2) {
				return true;
			}
		} else if (isGround(i, j)) {
			if (countAdjacentWallTiles(i, j) >= 2) {
				return true;
			}
			if (countAdjacentWallTiles(i, j) == 0 && countDiagonalAdjacentWallTiles(i, j) > 0) {
				return true;
			}
		}
		return false;
	}

	private boolean isInMap(int i, int j) {
		if (i >= 0 && i < map.length && j >= 0 && j < map[0].length) {
			return true;
		}
		return false;
	}

	private boolean forAllAdjacent(int i, int j, BiFunction<Integer, Integer, Boolean> func) {
		return func.apply(i + 1, j) && func.apply(i, j + 1) && func.apply(i - 1, j)
				&& func.apply(i, j - 1);
	}

	private boolean forAllSurrounding(int i, int j, BiFunction<Integer, Integer, Boolean> func) {
		return forAllDiagonalAdjacent(i, j, func) && forAllAdjacent(i, j, func);
	}

	private boolean forAllDiagonalAdjacent(int i, int j, BiFunction<Integer, Integer, Boolean> func) {
		return func.apply(i + 1, j + 1) && func.apply(i - 1, j - 1) && func.apply(i + 1, j - 1)
				&& func.apply(i - 1, j + 1);
	}

	private int countAdjacentGroundTiles(int i, int j) {
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

	private int countDiagonalAdjacentGroundTiles(int i, int j) {
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

	private int countAdjacentWallTiles(int i, int j) {
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

	private int countDiagonalAdjacentWallTiles(int i, int j) {
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
}
