package world.mapping;

import java.util.Set;
import java.util.EnumSet;

public class DungeonTile {
	private final Set<TileAttribute> attributes;

	public DungeonTile() {
		super();
		this.attributes = EnumSet.noneOf(TileAttribute.class);
	}

	public DungeonTile(EnumSet<TileAttribute> attributes) {
		super();
		this.attributes = attributes;
	}

	public DungeonTile(TileAttribute... attribute) {
		super();
		this.attributes = EnumSet.noneOf(TileAttribute.class);
		for (int i = 0; i < attribute.length; i++) {
			attributes.add(attribute[i]);
		}
	}

	public boolean hasAttribute(TileAttribute attribute) {
		return attributes.contains(attribute);
	}

	public Set<TileAttribute> overlap(DungeonTile tile) {
		final Set<TileAttribute> ret = EnumSet.noneOf(TileAttribute.class);
		for (TileAttribute t : tile.attributes) {
			if (attributes.contains(t)) {
				ret.add(t);
			}
		}
		return ret;
	}

	public Set<TileAttribute> difference(DungeonTile tile) {
		final Set<TileAttribute> ret = EnumSet.copyOf(attributes);
		for (TileAttribute t : tile.attributes) {
			if (ret.contains(t)) {
				ret.remove(t);
			} else {
				ret.add(t);
			}
		}
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().isInstance(this.getClass())) {
			return attributes.containsAll(((DungeonTile) obj).attributes);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return attributes.hashCode();
	}

	public Set<TileAttribute> getAttributes() {
		return attributes;
	}

	protected enum TileAttribute {
		wall, ground, corner, edge, center, facingright, facingleft, facingup, facingdown, attachesright, attachesleft, attachesup, attachesdown, pattern, special, unknown;
	}
}
