package world.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HouseTileLoader implements ITileLoader {

	private final static int tilesize = 32;
	private final static String resource = "images/tileset/house/tilesetHouse.png";

	private TilesetParser parser;
	private List<ITile<HouseID>> tiles;

	@Override
	public List<ITileBlock> loadTiles() throws IOException {
		parser = new TilesetParser(resource, tilesize);
		getTiles();
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
		//		ret.add(18, getDoubleCornerCenter());
		return ret;
	}

	//	private ITileBlock getDoubleCornerCenter() {
	//		GreenfootImage dcc11, dcc21, dcc31;
	//		dcc11 = new GreenfootImage(32, 32);
	//		dcc11.drawImage(findTile(HouseID.UpperLeftInnerCorner12).getTileImage(), -16, 0);
	//		dcc11.drawImage(findTile(HouseID.UpperRightInnerCorner11).getTileImage(), 16, 0);
	//		dcc21 = new GreenfootImage(32, 32);
	//		dcc21.drawImage(findTile(HouseID.UpperLeftInnerCorner22).getTileImage(), -16, 0);
	//		dcc21.drawImage(findTile(HouseID.UpperRightInnerCorner21).getTileImage(), 16, 0);
	//		dcc31 = new GreenfootImage(32, 32);
	//		dcc31.drawImage(findTile(HouseID.UpperLeftInnerCorner32).getTileImage(), -16, 0);
	//		dcc31.drawImage(findTile(HouseID.UpperRightInnerCorner31).getTileImage(), 16, 0);
	//		ITile specialWall11 = new MyTile(HouseID.DoubleCornerCenter11, dcc11);
	//		ITile specialWall21 = new MyTile(HouseID.DoubleCornerCenter21, dcc21);
	//		ITile specialWall31 = new MyTile(HouseID.DoubleCornerCenter31, dcc31);
	//		return new TileBlock(7, 3, null, null, null, specialWall11, null, null, null, null, null, null, specialWall21, null, null, null, null, null, null, specialWall31);
	//	}

	private ITileBlock getWallRight() {
		return new TileBlock(2, 1, findTile(HouseID.WallRight11), findTile(HouseID.WallRight12));
	}

	private ITileBlock getWallLeft() {
		return new TileBlock(2, 1, findTile(HouseID.WallLeft11), findTile(HouseID.WallLeft12));
	}

	private ITileBlock getWallBack() {
		return new TileBlock(1, 1, findTile(HouseID.WallBack11));
	}

	private ITileBlock getFloorShadow() {
		return new TileBlock(2, 1, findTile(HouseID.FloorShadow11), findTile(HouseID.FloorShadow12));
	}

	private ITileBlock getFloorExtra() {
		return new TileBlock(2, 1, findTile(HouseID.FloorExtra11), findTile(HouseID.FloorExtra21));
	}

	private ITileBlock getDoubleCornerRight() {
		return new TileBlock(3, 3, findTile(HouseID.UpperLeftInnerCorner11), null, null, findTile(HouseID.DoubleCornerRight11), findTile(HouseID.DoubleCornerRight12), null, findTile(HouseID.DoubleCornerRight21), findTile(HouseID.DoubleCornerRight22), null);
	}

	private ITileBlock getDoubleCornerLeft() {
		return new TileBlock(3, 3, null, null, findTile(HouseID.UpperRightInnerCorner12), null, findTile(HouseID.DoubleCornerLeft11), findTile(HouseID.DoubleCornerLeft12), null, findTile(HouseID.DoubleCornerLeft21), findTile(HouseID.DoubleCornerLeft22));
	}

	private ITileBlock getFloorPattern() {
		return new TileBlock(3, 2, findTile(HouseID.Floor12), findTile(HouseID.Floor13), findTile(HouseID.Floor14), findTile(HouseID.Floor22), findTile(HouseID.Floor23), findTile(HouseID.Floor24));
	}

	private ITileBlock getWallCenter() {
		return new TileBlock(1, 1, findTile(HouseID.WallCenter));
	}

	private ITileBlock getLowerLeftOuterCorner() {
		return new TileBlock(2, 3, findTile(HouseID.LowerLeftOuterCorner11), findTile(HouseID.LowerLeftOuterCorner12), findTile(HouseID.LowerLeftOuterCorner21), findTile(HouseID.LowerLeftOuterCorner22), findTile(HouseID.LowerLeftOuterCorner31), findTile(HouseID.LowerLeftOuterCorner32));
	}

	private ITileBlock getLowerRightOuterCorner() {
		return new TileBlock(2, 3, findTile(HouseID.LowerRightOuterCorner11), findTile(HouseID.LowerRightOuterCorner12), findTile(HouseID.LowerRightOuterCorner21), findTile(HouseID.LowerRightOuterCorner22), findTile(HouseID.LowerRightOuterCorner31), findTile(HouseID.LowerRightOuterCorner32));
	}

	private ITileBlock getWallFront() {
		return new TileBlock(1, 3, findTile(HouseID.WallFront11), findTile(HouseID.WallFront21), findTile(HouseID.WallFront31));
	}

	private ITileBlock getLowerRightInnerCorner() {
		return new TileBlock(2, 1, findTile(HouseID.LowerRightInnerCorner11), findTile(HouseID.LowerRightInnerCorner12));
	}

	private ITileBlock getLowerLeftInnerCorner() {
		return new TileBlock(2, 1, findTile(HouseID.LowerLeftInnerCorner11), findTile(HouseID.LowerLeftInnerCorner12));
	}

	private ITileBlock getUpperRightInnerCorner() {
		return new TileBlock(2, 3, findTile(HouseID.UpperRightInnerCorner11), findTile(HouseID.UpperRightInnerCorner12), findTile(HouseID.UpperRightInnerCorner21), findTile(HouseID.UpperRightInnerCorner22), findTile(HouseID.UpperRightInnerCorner31), findTile(HouseID.UpperRightInnerCorner32));
	}

	@SuppressWarnings("rawtypes")
	private ITileBlock getUpperLeftInnerCorner() {
		ITile[] tiles = { findTile(HouseID.UpperLeftInnerCorner11), findTile(HouseID.UpperLeftInnerCorner12), findTile(HouseID.UpperLeftInnerCorner21), findTile(HouseID.UpperLeftInnerCorner22), findTile(HouseID.UpperLeftInnerCorner31), findTile(HouseID.UpperLeftInnerCorner32) };
		return new TileBlock(2, 3, tiles);
	}

	private ITileBlock getUpperRightOuterCorner() {
		return new TileBlock(2, 2, findTile(HouseID.UpperRightOuterCorner11), findTile(HouseID.UpperRightOuterCorner12), findTile(HouseID.UpperRightOuterCorner21), findTile(HouseID.UpperRightOuterCorner22));
	}

	private ITileBlock getUpperLeftOuterCorner() {
		return new TileBlock(2, 2, findTile(HouseID.UpperLeftOuterCorner11), findTile(HouseID.UpperLeftOuterCorner12), findTile(HouseID.UpperLeftOuterCorner21), findTile(HouseID.UpperLeftOuterCorner22));
	}

	private ITile<HouseID> findTile(HouseID id) {
		for (ITile<HouseID> tile : tiles) {
			if (tile.getID().equals(id)) {
				return tile;
			}
		}
		throw new IllegalArgumentException("No such tile: " + id);
	}

	private void getTiles() {
		List<ITile<HouseID>> tiles = new ArrayList<>();
		int x = 0, y = 0;
		for (HouseID id : HouseID.values()) {
			ITile<HouseID> tile = new HouseTile(id, parser.getImageAt(x, y));
			tiles.add(tile);
			x++;
			if (x > 8) {
				x = 0;
				y++;
			}
		}
		this.tiles = tiles;
		for (ITile<HouseID> tile : this.tiles) {
			if (tile.getID().getCorrespondingID() != null) {
				findTile(tile.getID().getCorrespondingID()).setSpecial(tile.getTileImage());
			}
		}
	}

}
