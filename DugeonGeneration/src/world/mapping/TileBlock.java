package world.mapping;

import greenfoot.GreenfootImage;

public class TileBlock implements ITileBlock {

	private final ITile[][] block;

	private final int x, y;

	public TileBlock(int x, int y, ITile... tiles) {
		this.x = x;
		this.y = y;
		block = new ITile[x][y];
		int k = 0;
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				block[i][j] = tiles[k];
				k++;
			}
		}
	}

	public boolean transcribe(int x, int y, GreenfootImage[][] map) {
		if (x < 0 || x + this.x >= map.length || y < 0 || y + this.y >= map[0].length)
			return false;
		for (int i = 0; i < this.y; i++) {
			for (int j = 0; j < this.x; j++) {
				map[x + j][y + i] = block[j][i].getTileImage();
			}
		}
		return true;
	}

	// public boolean attachesTo(TileBlock tb) {
	// for (ITile[] tiles : block) {
	// for (ITile tile : tiles) {
	// if (tb.getAttachingTileFor(tile) != null) {
	// return true;
	// }
	// }
	// }
	// return false;
	// }
	//
	// public ITile getAttachingTileFor(ITile tile) {
	// for (ITile[] tiles : block) {
	// for (ITile myTile : tiles) {
	// if (myTile.attachesTo(tile)) {
	// return myTile;
	// }
	// }
	// }
	// return null;
	// }

}
