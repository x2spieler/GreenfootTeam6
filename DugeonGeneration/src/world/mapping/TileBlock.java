package world.mapping;

import greenfoot.GreenfootImage;
import world.MapElement;

@SuppressWarnings("rawtypes")
public class TileBlock implements ITileBlock {

	private final ITile[][] block;
	private final GreenfootImage[][] specials;
	private final int x, y;

	public TileBlock(int x, int y, ITile... tiles) {
		this.x = x;
		this.y = y;
		block = new ITile[x][y];
		specials = new GreenfootImage[x][y];
		int k = 0;
		outerloop: for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				block[j][i] = tiles[k];
				if (tiles[k] != null)
					specials[j][i] = tiles[k].getSpecial();
				k++;
				if (k >= tiles.length) {
					break outerloop;
				}
			}
		}
	}

	public boolean transcribe(int x, int y, GreenfootImage[][] map, MapElement[][] specials) {
		if (x < 0 || x + this.x > map.length || y < 0 || y + this.y > map[0].length)
			return false;
		for (int j = 0; j < this.x; j++) {
			for (int i = 0; i < this.y; i++) {
				if (block[j][i] == null)
					continue;
				if (map[x + j][y + i] == null)
					map[x + j][y + i] = block[j][i].getTileImage();
				if (specials != null && specials[x + j][y + i] == null && block[j][i].getSpecial() != null) {
					specials[x + j][y + i] = new MapElement(block[j][i].getSpecial());
				}
			}
		}
		return true;
	}

	@Override
	public GreenfootImage getImageAt(int x, int y) {
		if (x >= 0 && x < block.length && y >= 0 && y < block[0].length && block[x][y] != null) {
			return block[x][y].getTileImage();
		} else {
			throw new IllegalArgumentException("not tile at that position");
		}
	}

	@Override
	public GreenfootImage getSpecial(int i, int j) {
		return specials[i][j];
	}

}
