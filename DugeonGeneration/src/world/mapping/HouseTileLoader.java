package world.mapping;

import greenfoot.GreenfootImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HouseTileLoader implements ITileLoader {

	private final static int tilesize = 32;
	private final static String resource = "images/tileset/house/tilesetHouse.png";

	private final static List<String> tileIDs;
	private TilesetParser parser;
	private List<ITile> tiles;

	static {
		String[] ids = { "UpperLeftInnerCorner11", "UpperLeftInnerCorner12",
				"UpperRightInnerCorner11", "UpperRightInnerCorner12", "WallFront11", "WallCenter",
				"UpperLefttOuterCornerSpecial", "UpperRightOuterCornerSpecial", "empty",
				"UpperLeftInnerCorner21", "UpperLeftInnerCorner22", "UpperRightInnerCorner21",
				"UpperRightInnerCorner22", "WallFront21", "UpperLeftOuterCorner11",
				"UpperLeftOuterCorner12", "UpperRightOuterCorner11", "UpperRightOuterCorner12",
				"UpperLeftInnerCorner31", "UpperLeftInnerCorner32", "UpperRightInnerCorner31",
				"UpperRightInnerCorner32", "WallFront31", "UpperLeftOuterCorner21",
				"UpperLeftOuterCorner22", "UpperRightOuterCorner21", "UpperRightOuterCorner22",
				"LowerLeftInnerCorner11", "LowerLeftInnerCorner12", "LowerRightInnerCorner11",
				"LowerRightInnerCorner12", "WallBack11", "LowerLeftOuterCorner11",
				"LowerLeftOuterCorner12", "LowerRightOuterCorner11", "LowerRightOuterCorner12",
				"LowerLeftInnerCornerSpecial11", "LowerLeftInnerCornerSpecial12",
				"LowerRightInnerCornerSpecial11", "LowerRightInnerCornerSpecial12",
				"WallBackSpecial", "LowerLeftOuterCorner21", "LowerLeftOuterCorner22",
				"LowerRightOuterCorner21", "LowerRightOuterCorner22", "Floor11", "Floor12",
				"Floor13", "Floor14", "empty", "LowerLeftOuterCorner31", "LowerLeftOuterCorner32",
				"LowerRightOuterCorner31", "LowerRightOuterCorner32", "empty", "Floor22",
				"Floor23", "Floor24", "Floor25", "WallLeft11", "WallLeft12", "WallRight11",
				"wallRight12", "empty", "Floor32", "DoubleCornerLeft11", "DoubleCornerLeft12",
				"DoubleCornerRight11", "DoubleCornerRight12", "FloorExtra11", "empty", "empty",
				"empty", "DoubleCornerLeft20", "DoubleCornerLeft21", "DoubleCornerLeft22",
				"DoubleCornerRight21", "DoubleCornerRight22", "FloorExtra21", "FloorShadow11",
				"FloorShadow12" };

		tileIDs = Arrays.asList(ids);
	}

	@Override
	public List<ITileBlock> loadTiles() throws IOException {
		parser = new TilesetParser(resource, tilesize);
		tiles = getTiles();
		return parseTileBlocks();
	}

	private List<ITileBlock> parseTileBlocks() {
		List<ITileBlock> ret = new ArrayList<>();
		ret.add(0, getUpperLeftInnerCorner());
		ret.add(1, getUpperRightInnerCorner());
		ret.add(2, getLowerLeftInnerCorner());
		ret.add(3, getLowerRightInnerCorner());
		ret.add(4, getUpperLeftOuterCorner());
		ret.add(5, getUpperRightOuterCorner());
		ret.add(6, getLowerLeftOuterCorner());
		ret.add(7, getLowerRightOuterCorner());
		ret.add(8, getWallFront());
		ret.add(9, getWallBack());
		ret.add(10, getWallLeft());
		ret.add(11, getWallRight());
		ret.add(12, getWallCenter());
		ret.add(13, getFloorPattern());
		ret.add(14, getDoubleCornerLeft());
		ret.add(15, getDoubleCornerRight());
		ret.add(16, getFloorExtra());
		ret.add(17, getFloorShadow());
		ret.add(18, getDoubleCornerCenter());
		return ret;
	}

	private ITileBlock getDoubleCornerCenter() {
		GreenfootImage dcc11, dcc21, dcc31;
		dcc11 = new GreenfootImage(32, 32);
		dcc11.drawImage(findTile("UpperLeftInnerCorner12").getTileImage(), -16, 0);
		dcc11.drawImage(findTile("UpperRightInnerCorner11").getTileImage(), 16, 0);
		dcc21 = new GreenfootImage(32, 32);
		dcc21.drawImage(findTile("UpperLeftInnerCorner22").getTileImage(), -16, 0);
		dcc21.drawImage(findTile("UpperRightInnerCorner21").getTileImage(), 16, 0);
		dcc31 = new GreenfootImage(32, 32);
		dcc31.drawImage(findTile("UpperLeftInnerCorner32").getTileImage(), -16, 0);
		dcc31.drawImage(findTile("UpperRightInnerCorner31").getTileImage(), 16, 0);
		ITile specialWall11 = new MyTile("DoubleCornerCenter11", dcc11);
		ITile specialWall21 = new MyTile("DoubleCornerCenter21", dcc21);
		ITile specialWall31 = new MyTile("DoubleCornerCenter31", dcc31);
		return new TileBlock(7, 3, null, null, null, specialWall11, null, null, null, null, null,
				null, specialWall21, null, null, null, null, null, null, specialWall31);
	}

	private ITileBlock getWallRight() {
		return new TileBlock(2, 1, findTile("wallRight11"), findTile("wallRight12"));
	}

	private ITileBlock getWallLeft() {
		return new TileBlock(2, 1, findTile("WallLeft11"), findTile("WallLeft12"));
	}

	private ITileBlock getWallBack() {
		return new TileBlock(1, 1, findTile("WallBack11"));
	}

	private ITileBlock getFloorShadow() {
		return new TileBlock(2, 1, findTile("FloorShadow11"), findTile("FloorShadow12"));
	}

	private ITileBlock getFloorExtra() {
		return new TileBlock(2, 1, findTile("FloorExtra11"), findTile("FloorExtra21"));
	}

	private ITileBlock getDoubleCornerRight() {
		return new TileBlock(3, 3, findTile("UpperLeftInnerCorner11"), null, null,
				findTile("DoubleCornerRight11"), findTile("DoubleCornerRight12"), null,
				findTile("DoubleCornerRight21"), findTile("DoubleCornerRight22"), null);
	}

	private ITileBlock getDoubleCornerLeft() {
		return new TileBlock(3, 3, null, null, findTile("UpperRightInnerCorner12"), null,
				findTile("DoubleCornerLeft11"), findTile("DoubleCornerLeft12"), null,
				findTile("DoubleCornerLeft21"), findTile("DoubleCornerLeft22"));
	}

	private ITileBlock getFloorPattern() {
		return new TileBlock(3, 2, findTile("Floor12"), findTile("Floor13"), findTile("Floor14"),
				findTile("Floor22"), findTile("Floor23"), findTile("Floor24"));
	}

	private ITileBlock getWallCenter() {
		return new TileBlock(1, 1, findTile("WallCenter"));
	}

	private ITileBlock getLowerLeftOuterCorner() {
		return new TileBlock(2, 3, findTile("LowerLeftOuterCorner11"),
				findTile("LowerLeftOuterCorner12"), findTile("LowerLeftOuterCorner21"),
				findTile("LowerLeftOuterCorner22"), findTile("LowerLeftOuterCorner31"),
				findTile("LowerLeftOuterCorner32"));
	}

	private ITileBlock getLowerRightOuterCorner() {
		return new TileBlock(2, 3, findTile("LowerRightOuterCorner11"),
				findTile("LowerRightOuterCorner12"), findTile("LowerRightOuterCorner21"),
				findTile("LowerRightOuterCorner22"), findTile("LowerRightOuterCorner31"),
				findTile("LowerRightOuterCorner32"));
	}

	private ITileBlock getWallFront() {
		return new TileBlock(1, 3, findTile("WallFront11"), findTile("WallFront21"),
				findTile("WallFront31"));
	}

	private ITileBlock getLowerRightInnerCorner() {
		return new TileBlock(2, 1, findTile("LowerRightInnerCorner11"),
				findTile("LowerRightInnerCorner12"));
	}

	private ITileBlock getLowerLeftInnerCorner() {
		return new TileBlock(2, 1, findTile("LowerLeftInnerCorner11"),
				findTile("LowerLeftInnerCorner12"));
	}

	private ITileBlock getUpperRightInnerCorner() {
		return new TileBlock(2, 3, findTile("UpperRightInnerCorner11"),
				findTile("UpperRightInnerCorner12"), findTile("UpperRightInnerCorner21"),
				findTile("UpperRightInnerCorner22"), findTile("UpperRightInnerCorner31"),
				findTile("UpperRightInnerCorner32"));
	}

	private ITileBlock getUpperLeftInnerCorner() {
		ITile[] tiles = { findTile("UpperLeftInnerCorner11"), findTile("UpperLeftInnerCorner12"),
				findTile("UpperLeftInnerCorner21"), findTile("UpperLeftInnerCorner22"),
				findTile("UpperLeftInnerCorner31"), findTile("UpperLeftInnerCorner32") };
		return new TileBlock(2, 3, tiles);
	}

	private ITileBlock getUpperRightOuterCorner() {
		return new TileBlock(2, 2, findTile("UpperRightOuterCorner11"),
				findTile("UpperRightOuterCorner12"), findTile("UpperRightOuterCorner21"),
				findTile("UpperRightOuterCorner22"));
	}

	private ITileBlock getUpperLeftOuterCorner() {
		return new TileBlock(2, 2, findTile("UpperLeftOuterCorner11"),
				findTile("UpperLeftOuterCorner12"), findTile("UpperLeftOuterCorner21"),
				findTile("UpperLeftOuterCorner22"));
	}

	private ITile findTile(String id) {
		for (ITile tile : tiles) {
			if (tile.getID().equalsIgnoreCase(id)) {
				return tile;
			}
		}
		throw new IllegalArgumentException("No such tile: " + id);
	}

	private List<ITile> getTiles() {
		List<ITile> tiles = new ArrayList<ITile>();
		int x = 0, y = 0;
		for (String id : tileIDs) {
			ITile tile = new MyTile(id, parser.getImageAt(x, y));
			tiles.add(tile);
			x++;
			if (x > 8) {
				x = 0;
				y++;
			}
		}
		return tiles;
	}

}
