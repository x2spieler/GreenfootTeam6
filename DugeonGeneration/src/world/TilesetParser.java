package world;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import greenfoot.GreenfootImage;
import images.tileset.house.House;

public class TilesetParser {

	private Map<House, GreenfootImage> tiles;

	public TilesetParser(String resource) throws IOException {
		tiles = new HashMap<>();
		URL file = getClass().getClassLoader().getResource(resource);
		BufferedImage tileset = ImageIO.read(file);
		parseTiles(tileset);
	}

	private void parseTiles(BufferedImage tileset) {
		for (House tile : House.values()) {
			BufferedImage img = tileset.getSubimage(tile.getX() * House.TILE_SIZE, tile.getY() * House.TILE_SIZE,
					House.TILE_SIZE, House.TILE_SIZE);
			GreenfootImage image = new GreenfootImage(House.TILE_SIZE, House.TILE_SIZE);
			image.getAwtImage().getGraphics().drawImage(img, 0, 0, House.TILE_SIZE, House.TILE_SIZE, null);
			tiles.put(tile, image);
			System.out.println(img);
			System.out.println(image);
		}
	}

	public Map<House, GreenfootImage> getTiles() {
		return tiles;
	}

}
