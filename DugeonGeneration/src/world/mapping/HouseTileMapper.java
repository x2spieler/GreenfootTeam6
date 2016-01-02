package world.mapping;

import java.awt.Color;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import world.DungeonMap;
import world.MapElement;
import DungeonGeneration.FieldType;
import DungeonGeneration.MapField;
import greenfoot.GreenfootImage;

public class HouseTileMapper {
	public final static DungeonGeneration.FieldType[][] UpperLeftInner, UpperRightInner, LowerLeftInner, LowerRightInner, UpperLeftOuter, UpperRightOuter, LowerLeftOuter, LowerRightOuter, WallFront, WallBack, WallLeft, WallRight, DoubleCornerLeft, DoubleCornerRight, DoubleCornerCenter;

	private static final GreenfootImage unknown;
	private static final GreenfootImage unmapped;

	static {
		UpperLeftInner = new FieldType[][] { { FieldType.WALL, FieldType.WALL }, { FieldType.WALL, FieldType.WALL }, { FieldType.WALL, FieldType.GROUND } };
		UpperRightInner = new FieldType[][] { { FieldType.WALL, FieldType.WALL }, { FieldType.WALL, FieldType.WALL }, { FieldType.GROUND, FieldType.WALL } };
		LowerLeftInner = new FieldType[][] { { FieldType.WALL, FieldType.GROUND }, { FieldType.WALL, FieldType.WALL } };
		LowerRightInner = new FieldType[][] { { FieldType.GROUND, FieldType.WALL }, { FieldType.WALL, FieldType.WALL } };
		UpperLeftOuter = new FieldType[][] { { FieldType.GROUND, FieldType.GROUND }, { FieldType.GROUND, FieldType.WALL } };
		UpperRightOuter = new FieldType[][] { { FieldType.GROUND, FieldType.GROUND }, { FieldType.WALL, FieldType.GROUND } };
		LowerLeftOuter = new FieldType[][] { { FieldType.GROUND, FieldType.WALL }, { FieldType.GROUND, FieldType.WALL }, { FieldType.GROUND, FieldType.GROUND } };
		LowerRightOuter = new FieldType[][] { { FieldType.WALL, FieldType.GROUND }, { FieldType.WALL, FieldType.GROUND }, { FieldType.GROUND, FieldType.GROUND } };
		WallFront = new FieldType[][] { { FieldType.WALL }, { FieldType.WALL }, { FieldType.GROUND } };
		WallBack = new FieldType[][] { { FieldType.GROUND }, { FieldType.WALL } };
		WallLeft = new FieldType[][] { { FieldType.GROUND, FieldType.WALL } };
		WallRight = new FieldType[][] { { FieldType.WALL, FieldType.GROUND } };
		DoubleCornerLeft = new FieldType[][] { { null, FieldType.WALL, FieldType.WALL }, { FieldType.GROUND, FieldType.WALL, FieldType.WALL }, { FieldType.GROUND, FieldType.GROUND, FieldType.WALL }, { null, FieldType.GROUND, FieldType.GROUND } };
		DoubleCornerRight = new FieldType[][] { { FieldType.WALL, FieldType.WALL, null }, { FieldType.WALL, FieldType.WALL, FieldType.GROUND }, { FieldType.WALL, FieldType.GROUND, FieldType.GROUND }, { FieldType.GROUND, FieldType.GROUND, null } };
		DoubleCornerCenter = new FieldType[][] { { FieldType.GROUND, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.GROUND }, { FieldType.GROUND, FieldType.GROUND, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.GROUND, FieldType.GROUND }, { null, null, FieldType.GROUND, FieldType.GROUND, FieldType.GROUND, null, null } };

		unknown = new GreenfootImage("tileset/UnknownTile.png");
		unmapped = new GreenfootImage(DungeonMap.TILE_SIZE, DungeonMap.TILE_SIZE);
		unmapped.setColor(Color.WHITE);
		unmapped.fill();
		unmapped.setColor(Color.BLACK);
		unmapped.setFont(unmapped.getFont().deriveFont(7.0f));
		unmapped.drawString("unmapped", 0, DungeonMap.TILE_SIZE / 2);
	}

	private ITileLoader loader = new HouseTileLoader();

	private List<ITileBlock> blocks;

	private Map<FieldType[][], ITileBlock> blockMap;

	private MapField[][] map;

	private GreenfootImage[][] images;
	private MapElement[][] specialTiles;

	public HouseTileMapper(MapField[][] map) throws IOException {
		this.map = map;
		images = new GreenfootImage[map.length][map[0].length];
		specialTiles = new MapElement[map.length][map[0].length];
		blocks = loader.loadTiles();
		blockMap = new LinkedHashMap<>();
		initBlockMap();
		mapTiles();
	}

	@SuppressWarnings("unused")
	private void testTiles() {
		blocks.get(14).transcribe(10, 10, images, null);
		blocks.get(0).transcribe(0, 0, images, null);
		blocks.get(1).transcribe(2, 0, images, null);
		blocks.get(2).transcribe(0, 3, images, null);
		blocks.get(3).transcribe(2, 3, images, null);
		blocks.get(4).transcribe(4, 0, images, null);
		blocks.get(5).transcribe(6, 0, images, null);
		blocks.get(6).transcribe(4, 2, images, null);
		blocks.get(7).transcribe(6, 2, images, null);
		blocks.get(8).transcribe(0, 4, images, null);
		blocks.get(9).transcribe(1, 4, images, null);
		blocks.get(9).transcribe(2, 4, images, null);
		blocks.get(9).transcribe(3, 4, images, null);
		blocks.get(10).transcribe(0, 7, images, null);
		blocks.get(11).transcribe(0, 8, images, null);

	}

	private void initBlockMap() {
		//blockMap.put(DoubleCornerCenter, blocks.get(18));
		blockMap.put(DoubleCornerLeft, blocks.get(14));
		blockMap.put(DoubleCornerRight, blocks.get(15));
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
		for (int i = 0; i < images.length; i++) {
			for (int j = 0; j < images[0].length; j++) {
				findBlockFor(i, j);
			}
		}
		for (int i = 0; i < images.length; i++) {
			for (int j = 0; j < images[0].length; j++) {
				fill(i, j);
			}
		}
		for (int i = 0; i < images.length; i++) {
			for (int j = 0; j < images[0].length; j++) {
				if (!isKnownTileType(map[i][j])) {
					images[i][j] = unknown;
				}
				if (images[i][j] == null) {
					images[i][j] = unmapped;
				}
			}
		}
	}

	private boolean isKnownTileType(MapField mapField) {
		return mapField.getFieldType() == FieldType.WALL || mapField.getFieldType() == FieldType.GROUND || mapField.getFieldType() == FieldType.DESTRUCTABLE;
	}

	private void fill(int i, int j) {
		if (images[i][j] != null)
			return;
		if (isGround(i, j)) {
			blocks.get(13).transcribe(i, j, images, null);
		} else if (isWall(i, j)) {
			blocks.get(12).transcribe(i, j, images, null);
		}
	}

	private void findBlockFor(int i, int j) {
		if (images[i][j] != null)
			return;
		for (FieldType[][] tb : blockMap.keySet()) {
			if (fitsMapAt(i, j, tb)) {
				blockMap.get(tb).transcribe(i, j, images, specialTiles);
			}
		}
	}

	private boolean fitsMapAt(int i, int j, FieldType[][] field) {
		if (i < 0 || j < 0 || (i + field[0].length) > map.length || (j + field.length) > map[0].length)
			return false;
		for (int k = 0; k < field.length; k++) {
			for (int k2 = 0; k2 < field[k].length; k2++) {
				if (field[k][k2] == null) {
					continue;
				}
				FieldType f = map[i + k2][j + k].getFieldType();
				if (field[k][k2] != f) {
					if (f == FieldType.DESTRUCTABLE && field[k][k2] == FieldType.GROUND) {
						continue;
					}
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
		return isInMap(i, j) && map[i][j].getFieldType().equals(FieldType.WALL);
	}

	private boolean isGround(int i, int j) {
		if (!isInMap(i, j))
			return false;
		FieldType t = map[i][j].getFieldType();
		return t.equals(FieldType.GROUND) || t.equals(FieldType.DESTRUCTABLE);
	}

	public MapElement[][] getSpecialTiles() {
		return specialTiles;
	}

}
