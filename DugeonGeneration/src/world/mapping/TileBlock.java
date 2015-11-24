package world.mapping;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;
import scrollWorld.ScrollActor;
import greenfoot.GreenfootImage;

public class TileBlock implements ITileBlock {

	private final ITile[][] block;
	private final int x, y;

	public TileBlock(int x, int y, ITile... tiles) {
		this.x = x;
		this.y = y;
		block = new ITile[x][y];
		int k = 0;
		outerloop: for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				block[j][i] = tiles[k];
				k++;
				if (k >= tiles.length) {
					break outerloop;
				}
			}
		}
	}

	public boolean transcribe(int x, int y, GreenfootImage[][] map) {
		if (x < 0 || x + this.x >= map.length || y < 0 || y + this.y >= map[0].length)
			return false;
		for (int i = 0; i < this.y; i++) {
			for (int j = 0; j < this.x; j++) {
				if (map[x + j][y + i] == null && block[j][i] != null)
					map[x + j][y + i] = block[j][i].getTileImage();
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

}
