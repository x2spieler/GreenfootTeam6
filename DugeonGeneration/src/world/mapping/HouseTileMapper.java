package world.mapping;

import greenfoot.GreenfootImage;

import java.io.IOException;
import java.util.Map;

import world.mapping.HouseTileMapper.House;
import world.mapping.BasicTileMapper.ITileset;
import world.mapping.DungeonTile.TileAttribute;

public class HouseTileMapper extends BasicTileMapper<House> {

	private final static String resource = "images/tileset/house/tilesetHouse.png";

	public HouseTileMapper(DungeonTile[][] dungeon) throws IOException {
		super(dungeon, resource, House.class);
	}

	@Override
	protected GreenfootImage[][] mapTiles(DungeonTile[][] dungeon, Map<House, GreenfootImage> map) {
		// TODO map tiles to semantic map.
		return null;
	}

	// TODO: FAR from finished.
	enum House implements ITileset {
		Tile11(0, 0, TileAttribute.wall), Tile12(1, 0, TileAttribute.wall), Tile13(2, 0,
				TileAttribute.wall), Tile14(3, 0, TileAttribute.wall), Tile15(4, 0,
				TileAttribute.wall), Tile16(5, 0, TileAttribute.wall), Tile17(6, 0,
				TileAttribute.ground), Tile18(7, 0, TileAttribute.ground), Tile19(8, 0), Tile21(0,
				1, TileAttribute.wall), Tile22(1, 1, TileAttribute.wall), Tile23(2, 1,
				TileAttribute.wall), Tile24(3, 1, TileAttribute.wall), Tile25(4, 1,
				TileAttribute.wall), Tile26(5, 1, TileAttribute.ground), Tile27(6, 1,
				TileAttribute.ground), Tile28(7, 1, TileAttribute.ground), Tile29(8, 1,
				TileAttribute.ground), Tile31(0, 2, TileAttribute.wall), Tile32(1, 2,
				TileAttribute.ground), Tile33(2, 2, TileAttribute.ground), Tile34(3, 2,
				TileAttribute.wall), Tile35(4, 2, TileAttribute.ground), Tile36(5, 2,
				TileAttribute.ground), Tile37(6, 2, TileAttribute.wall), Tile38(7, 2,
				TileAttribute.wall), Tile39(8, 2, TileAttribute.ground), Tile41(0, 3,
				TileAttribute.wall), Tile42(1, 3, TileAttribute.ground), Tile43(2, 3,
				TileAttribute.ground), Tile44(3, 3, TileAttribute.wall), Tile45(4, 3,
				TileAttribute.ground), Tile46(5, 3, TileAttribute.ground), Tile47(6, 3,
				TileAttribute.wall), Tile48(7, 3, TileAttribute.wall), Tile49(8, 3,
				TileAttribute.ground), Tile51(0, 4, TileAttribute.wall), Tile52(1, 4,
				TileAttribute.ground), Tile53(2, 4, TileAttribute.ground), Tile54(3, 4,
				TileAttribute.wall), Tile55(4, 4, TileAttribute.ground), Tile56(5, 4,
				TileAttribute.ground), Tile57(6, 4, TileAttribute.wall), Tile58(7, 4,
				TileAttribute.wall), Tile59(8, 4, TileAttribute.ground), Tile61(0, 5,
				TileAttribute.ground), Tile62(1, 5, TileAttribute.ground), Tile63(2, 5,
				TileAttribute.ground), Tile64(3, 5, TileAttribute.ground), Tile65(4, 5), Tile66(5,
				5, TileAttribute.ground), Tile67(6, 5, TileAttribute.ground), Tile68(7, 5,
				TileAttribute.ground), Tile69(8, 5, TileAttribute.ground), Tile71(0, 6), Tile72(1,
				6, TileAttribute.ground), Tile73(2, 6, TileAttribute.ground), Tile74(3, 6,
				TileAttribute.ground), Tile75(4, 6, TileAttribute.ground), Tile76(5, 6,
				TileAttribute.ground), Tile77(6, 6, TileAttribute.wall), Tile78(7, 6,
				TileAttribute.wall), Tile79(8, 6, TileAttribute.ground), Tile81(0, 7), Tile82(1, 7,
				TileAttribute.ground), Tile83(2, 7, TileAttribute.wall), Tile84(3, 7,
				TileAttribute.wall), Tile85(4, 7, TileAttribute.wall), Tile86(5, 7,
				TileAttribute.wall), Tile87(6, 7, TileAttribute.ground), Tile88(7, 7), Tile89(8, 7), Tile91(
				0, 8), Tile92(1, 8, TileAttribute.ground), Tile93(2, 8, TileAttribute.ground), Tile94(
				3, 8, TileAttribute.wall), Tile95(4, 8, TileAttribute.wall), Tile96(5, 8,
				TileAttribute.ground), Tile97(6, 8, TileAttribute.ground), Tile98(7, 8,
				TileAttribute.ground), Tile99(8, 8, TileAttribute.ground);

		private final int x, y;
		private final DungeonTile tile;

		House(int x, int y, TileAttribute... attribute) {
			this.x = x;
			this.y = y;
			tile = new DungeonTile(attribute);
		}

		@Override
		public int x() {
			return x;
		}

		@Override
		public int y() {
			return y;
		}

		@Override
		public int getTileSize() {
			return 32;
		}

		@Override
		public DungeonTile getDungeonTile() {
			return tile;
		}
	}
}
