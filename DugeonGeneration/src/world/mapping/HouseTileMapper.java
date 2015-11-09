package world.mapping;

import greenfoot.GreenfootImage;

import java.io.IOException;
import java.util.Map;

import world.mapping.HouseTileMapper.House;
import world.mapping.BasicTileMapper.ITileset;

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

	enum House implements ITileset {
		GROUND(8, 4), WALLRIGHT(7, 2), WALLLEFT(6, 2), WALLFRONT(4, 1), WALLBACK(1, 3), WALLCENTER(
				5, 0);

		private final int x, y;

		House(int x, int y) {
			this.x = x;
			this.y = y;
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
	}
}
