package world.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DungeonGeneration.MapField;
import greenfoot.GreenfootImage;

public class DungeonMapper {

	private final MapField[][] map;

	private List<GreenfootImage[][]> tiledMaps;

	public DungeonMapper(MapField[][] map) {
		this.map = map;
		tiledMaps = new ArrayList<GreenfootImage[][]>();
	}

	public GreenfootImage[][] getImageForTilesetHouse() throws IOException {
		GreenfootImage[][] ret = new HouseTileMapper(map).getImageArray();
		tiledMaps.add(ret);
		return ret;
	}
}
