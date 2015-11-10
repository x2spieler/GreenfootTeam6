package world.mapping;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import greenfoot.GreenfootImage;
import world.mapping.BasicTileMapper.ITileset;

public abstract class BasicTileMapper<E extends Enum<E> & ITileset> {

	protected final GreenfootImage[][] map;
	protected final GreenfootImage unknown;

	public BasicTileMapper(DungeonTile[][] dungeon, String resource, Class<E> enumClass) throws IOException {
		super();
		map = mapTiles(dungeon, new TilesetParser(resource, enumClass).getTiles());
		unknown = new GreenfootImage("tileset/UnknownTile.png");
	}

	protected abstract GreenfootImage[][] mapTiles(DungeonTile[][] dungeon, Map<E, GreenfootImage> map);

	public GreenfootImage[][] getMappedTiles() {
		return map;
	}

	protected interface ITileset {
		public int x();

		public int y();

		public int getTileSize();

		public DungeonTile getDungeonTile();
	}

	protected class TilesetParser {

		private Map<E, GreenfootImage> tiles;
		private Class<E> enumClass;

		public TilesetParser(String resource, Class<E> enumClass) throws IOException {
			tiles = new HashMap<>();
			this.enumClass = enumClass;
			URL file = getClass().getClassLoader().getResource(resource);
			BufferedImage tileset = ImageIO.read(file);
			parseTiles(tileset);
		}

		private void parseTiles(BufferedImage tileset) {
			int tilesize = enumClass.getEnumConstants()[0].getTileSize();
			for (E tile : enumClass.getEnumConstants()) {
				BufferedImage img = tileset.getSubimage(tile.x() * tilesize, tile.y() * tilesize, tilesize, tilesize);
				GreenfootImage image = new GreenfootImage(tilesize, tilesize);
				image.getAwtImage().getGraphics().drawImage(img, 0, 0, tilesize, tilesize, null);
				tiles.put(tile, image);
			}
		}

		public Map<E, GreenfootImage> getTiles() {
			return tiles;
		}

	}
}
