package world.mapping;

import greenfoot.GreenfootImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import world.mapping.BasicTileMapper.ITileset;
import DungeonGeneration.MapField;

public class DungeonMapper {

	private final MapField[][] map;

	private Map<Class<? extends Enum<? extends ITileset>>, GreenfootImage[][]> tiledMaps;

	public DungeonMapper(MapField[][] map) {
		this.map = map;
		tiledMaps = new HashMap<Class<? extends Enum<? extends ITileset>>, GreenfootImage[][]>();
	}

	public GreenfootImage[][] getImageForTilesetHouse() throws IOException {
		return null;
	}
}
