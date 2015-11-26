package world.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import world.MapElement;
import javafx.util.Pair;
import DungeonGeneration.MapField;
import greenfoot.GreenfootImage;

public class DungeonMapper {

	private final MapField[][] map;

	private List<GreenfootImage[][]> tiledMaps;

	public DungeonMapper(MapField[][] map) {
		this.map = map;
		tiledMaps = new ArrayList<GreenfootImage[][]>();
	}

	public Pair<GreenfootImage[][], MapElement[][]> getImageForTilesetHouse() throws IOException {
		HouseTileMapper mapper = new HouseTileMapper(map);
		GreenfootImage[][] ret1 = mapper.getImageArray();
		MapElement[][] ret2 = mapper.getSpecialTiles();
		tiledMaps.add(ret1);
		return new Pair<GreenfootImage[][], MapElement[][]>(ret1, ret2);
	}
}
