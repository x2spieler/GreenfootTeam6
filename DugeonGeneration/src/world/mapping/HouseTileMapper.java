package world.mapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DungeonGeneration.FieldType;
import DungeonGeneration.MapField;
import greenfoot.GreenfootImage;

public class HouseTileMapper {

	public final static DungeonGeneration.FieldType[][] UpperLeftInner, UpperRightInner, LowerLeftInner,
			LowerRightInner, UpperLeftOuter, UpperRightOuter, LowerLeftOuter, LowerRightOuter, WallFront, WallBack,
			WallLeft, WallRight;

	static {
		UpperLeftInner = new FieldType[][] { { FieldType.WALL, FieldType.WALL }, { FieldType.WALL, FieldType.WALL },
				{ FieldType.WALL, FieldType.GROUND } };
		UpperRightInner = new FieldType[][] { { FieldType.WALL, FieldType.WALL }, { FieldType.WALL, FieldType.WALL },
				{ FieldType.GROUND, FieldType.WALL } };
		LowerLeftInner = new FieldType[][] { { FieldType.WALL, FieldType.GROUND }, { FieldType.WALL, FieldType.WALL } };
		LowerRightInner = new FieldType[][] { { FieldType.GROUND, FieldType.WALL },
				{ FieldType.WALL, FieldType.WALL } };
		UpperLeftOuter = new FieldType[][] { { FieldType.GROUND, FieldType.GROUND },
				{ FieldType.GROUND, FieldType.WALL } };
		UpperRightOuter = new FieldType[][] { { FieldType.GROUND, FieldType.GROUND },
				{ FieldType.WALL, FieldType.GROUND } };
		LowerLeftOuter = new FieldType[][] { { FieldType.GROUND, FieldType.WALL }, { FieldType.GROUND, FieldType.WALL },
				{ FieldType.GROUND, FieldType.GROUND } };
		LowerRightOuter = new FieldType[][] { { FieldType.WALL, FieldType.GROUND },
				{ FieldType.WALL, FieldType.GROUND }, { FieldType.GROUND, FieldType.GROUND } };
		WallFront = new FieldType[][] { { FieldType.WALL }, { FieldType.WALL }, { FieldType.GROUND } };
		WallBack = new FieldType[][] { { FieldType.GROUND }, { FieldType.WALL } };
		WallLeft = new FieldType[][] { { FieldType.GROUND, FieldType.WALL } };
		WallRight = new FieldType[][] { { FieldType.WALL, FieldType.GROUND } };
	}

	private ITileLoader loader = new HouseTileLoader();

	private List<ITileBlock> blocks;

	private Map<FieldType[][], ITileBlock> blockMap;

	private MapField[][] map;

	private GreenfootImage[][] images;

	public HouseTileMapper(MapField[][] map) throws IOException {
		this.map = map;
		images = new GreenfootImage[map.length][map[0].length];
		blocks = loader.loadTiles();
		blockMap = new HashMap<>();
		initMap();
		mapTiles();
	}

	private void initMap() {
		blockMap.put(UpperLeftInner, blocks.get(0));
		blockMap.put(UpperRightInner, blocks.get(1));
		blockMap.put(LowerLeftInner, blocks.get(2));
		blockMap.put(LowerRightInner, blocks.get(3));
		blockMap.put(UpperLeftOuter, blocks.get(4));
		blockMap.put(UpperRightOuter, blocks.get(5));
		blockMap.put(LowerLeftOuter, blocks.get(6));
		blockMap.put(LowerRightOuter, blocks.get(7));
		blockMap.put(WallFront, blocks.get(8));
		blockMap.put(WallBack, blocks.get(9));
		blockMap.put(WallLeft, blocks.get(10));
		blockMap.put(WallRight, blocks.get(11));
	}

	public GreenfootImage[][] getImageArray() {
		return images;
	}

	private void mapTiles() {
		for (int k = 0; k < 2; k++) {
			for (int i = 0; i < images.length; i++) {
				for (int j = 0; j < images[0].length; j++) {
					findBlockFor(i, j);
				}
			}
		}
	}

	private void findBlockFor(int i, int j) {
		if (images[i][j] != null)
			return;
		for (FieldType[][] tb : blockMap.keySet()) {
			if (fitsMapAt(i, j, tb)) {
				blockMap.get(tb).transcribe(i, j, images);
			}
		}
	}

	private boolean fitsMapAt(int i, int j, FieldType[][] field) {
		if (i >= 0 || j >= 0 || (i + field.length) < map.length || (j + field[0].length) < map[0].length)
			return false;
		for (int k = 0; k < field.length; k++) {
			for (int k2 = 0; k2 < field[k].length; k2++) {
				if (!(field[k][k2] == map[i + k][j + k2].getFieldType())) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isInMap(int i, int j) {
		if (i >= 0 && i < map.length && j >= 0 && j < map[0].length) {
			return true;
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
