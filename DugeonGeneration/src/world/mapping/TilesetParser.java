package world.mapping;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import greenfoot.GreenfootImage;

class TilesetParser {

	private final int tilesize;

	private final BufferedImage tileset;

	public TilesetParser(String resource, int tilesize) throws IOException {
		this.tilesize = tilesize;
		URL file = getClass().getClassLoader().getResource(resource);
		tileset = ImageIO.read(file);
	}

	public GreenfootImage getImageAt(int x, int y) {
		BufferedImage img = tileset.getSubimage(x * tilesize, y * tilesize, tilesize, tilesize);
		GreenfootImage image = new GreenfootImage(tilesize, tilesize);
		image.getAwtImage().getGraphics().drawImage(img, 0, 0, tilesize, tilesize, null);
		return image;
	}

}
