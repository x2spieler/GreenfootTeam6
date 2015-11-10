package world.mapping;

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
		String[] ids = { "UpperLeftInnerCorner11", "UpperLeftInnerCorner12", "UpperRightInnerCorner11",
				"UpperRightInnerCorner12", "WallFront11", "WallCenter", "UpperLefttOuterCornerSpecial",
				"UpperRightOuterCornerSpecial", "empty", "UpperLeftInnerCorner21", "UpperLeftInnerCorner22",
				"UpperRightInnerCorner21", "UpperRightInnerCorner22", "WallFront21", "UpperLefttOuterCorner11",
				"UpperLefttOuterCorner12", "UpperRightOuterCorner11", "UpperRightOuterCorner12",
				"UpperLeftInnerCorner31", "UpperLeftInnerCorner32", "UpperRightInnerCorner31",
				"UpperRightInnerCorner32", "WallFront31", "UpperLefttOuterCorner21", "UpperLefttOuterCorner22",
				"UpperRightOuterCorner21", "UpperRightOuterCorner22", "LowerLeftInnerCorner11",
				"LowerLeftInnerCorner12", "LowerRightInnerCorner11", "LowerRightInnerCorner12", "WallBack11",
				"LowerLeftOuterCorner11", "LowerLeftOuterCorner12", "LowerRightOuterCorner11", "LowerLeftOuterCorner12",
				"LowerLeftInnerCornerSpecial11", "LowerLeftInnerCornerSpecial12", "LowerRightInnerCornerSpecial11",
				"LowerRightInnerCornerSpecial12", "WallBackSpecial", "LowerLeftOuterCorner21", "LowerLeftOuterCorner22",
				"LowerLeftOuterCorner21", "LowerLeftOuterCorner22", "Floor11", "Floor12", "Floor13", "Floor14", "empty",
				"LowerLeftOuterCorner31", "LowerLeftOuterCorner32", "LowerRightOuterCorner31",
				"LowerRightOuterCorner32", "empty", "Floor22", "Floor23", "Floor24", "Floor25", "WallLeft11",
				"WallLeft12", "WallRight11", "wallRight12", "empty", "Floor32", "DoubleCornerLeft11",
				"DoubleCornerLeft12", "DoubleCornerRight11", "DoubleCornerRight12", "FloorExtra11", "empty", "empty",
				"empty", "DoubleCornerLeft20", "DoubleCornerLeft21", "DoubleCornerLeft22", "DoubleCornerRight21",
				"DoubleCornerRight22", "FloorExtra21", "FloorShadow11", "FloorShadow12" };

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
		ret.add(getUpperLeftInnerCorner());
		ret.add(getUpperRightInnerCorner());
		ret.add(getLowerLeftInnerCorner());
		ret.add(getLowerRightInnerCorner());
		ret.add(getUpperLeftOuterCorner());
		ret.add(getUpperRightOuterCorner());
		ret.add(getLowerLeftOuterCorner());
		ret.add(getLowerRightOuterCorner());
		ret.add(getWallFront());
		ret.add(getWallBack());
		ret.add(getWallLeft());
		ret.add(getWallRight());
		ret.add(getWallCenter());
		ret.add(getFloorPattern());
		ret.add(getDoubleCornerLeft());
		ret.add(getDoubleCornerRight());
		ret.add(getFloorExtra());
		ret.add(getFloorShadow());
		return ret;
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
		return new TileBlock(2, 2, findTile("DoubleCornerRight11"), findTile("DoubleCornerRight12"),
				findTile("DoubleCornerRight21"), findTile("DoubleCornerRight22"));
	}

	private ITileBlock getDoubleCornerLeft() {
		return new TileBlock(2, 2, findTile("DoubleCornerLeft11"), findTile("DoubleCornerLeft12"),
				findTile("DoubleCornerLeft21"), findTile("DoubleCornerLeft22"));
	}

	private ITileBlock getFloorPattern() {
		return new TileBlock(3, 2, findTile("Floor12"), findTile("Floor13"), findTile("Floor14"), findTile("Floor22"),
				findTile("Floor23"), findTile("Floor24"));
	}

	private ITileBlock getWallCenter() {
		return new TileBlock(1, 1, findTile("WallCenter"));
	}

	private ITileBlock getLowerLeftOuterCorner() {
		return new TileBlock(2, 3, findTile("LowerLeftOuterCorner11"), findTile("LowerLeftOuterCorner12"),
				findTile("LowerLeftOuterCorner21"), findTile("LowerLeftOuterCorner22"),
				findTile("LowerLeftOuterCorner31"), findTile("LowerLeftOuterCorner32"));
	}

	private ITileBlock getLowerRightOuterCorner() {
		return new TileBlock(2, 3, findTile("LowerRightOuterCorner11"), findTile("LowerRightOuterCorner12"),
				findTile("LowerRightOuterCorner21"), findTile("LowerRightOuterCorner22"),
				findTile("LowerRightOuterCorner31"), findTile("LowerRightOuterCorner32"));
	}

	private ITileBlock getWallFront() {
		return new TileBlock(1, 3, findTile("WallFront11"), findTile("WallFront21"), findTile("WallFront31"));
	}

	private ITileBlock getLowerRightInnerCorner() {
		return new TileBlock(2, 1, findTile("LowerRightInnerCorner11"), findTile("LowerRightInnerCorner12"));
	}

	private ITileBlock getLowerLeftInnerCorner() {
		return new TileBlock(2, 1, findTile("LowerLeftInnerCorner11"), findTile("LowerLeftInnerCorner12"));
	}

	private ITileBlock getUpperRightInnerCorner() {
		return new TileBlock(2, 3, findTile("UpperRightInnerCorner11"), findTile("UpperRightInnerCorner12"),
				findTile("UpperRightInnerCorner21"), findTile("UpperRightInnerCorner22"),
				findTile("UpperRightInnerCorner31"), findTile("UpperRightInnerCorner32"));
	}

	private ITileBlock getUpperLeftInnerCorner() {
		return new TileBlock(2, 3, findTile("UpperLeftInnerCorner11"), findTile("UpperLeftInnerCorner12"),
				findTile("UpperLeftInnerCorner21"), findTile("UpperLeftInnerCorner22"),
				findTile("UpperLeftInnerCorner31"), findTile("UpperLeftInnerCorner32"));
	}

	private ITileBlock getUpperRightOuterCorner() {
		return new TileBlock(2, 2, findTile("UpperRightOuterCorner11"), findTile("UpperRightOuterCorner12"),
				findTile("UpperRightOuterCorner21"), findTile("UpperRightOuterCorner22"));
	}

	private ITileBlock getUpperLeftOuterCorner() {
		return new TileBlock(2, 2, findTile("UpperLeftOuterCorner11"), findTile("UpperLeftOuterCorner12"),
				findTile("UpperLeftOuterCorner21"), findTile("UpperLeftOuterCorner22"));
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
