package world.mapping;

import java.util.EnumSet;

import DungeonGeneration.MapField;
import world.mapping.DungeonTile.TileAttribute;

/**
 * @author opitzju, oertelt
 *
 */
public class HouseTiler extends BasicDungeonTiler {

	public HouseTiler(MapField[][] map) {
		super(map);
	}

	@Override
	protected DungeonTile getDungeonTileForTile(int i, int j) {
		EnumSet<TileAttribute> attributes = EnumSet.noneOf(TileAttribute.class);
		if (isGround(i, j)) {
			attributes.add(TileAttribute.ground);
		} else if (isWall(i, j)) {
			attributes.add(TileAttribute.wall);
		} else {
			attributes.add(TileAttribute.unknown);
			return new DungeonTile(attributes);
		}
		return new DungeonTile(attributes);
	}

}
