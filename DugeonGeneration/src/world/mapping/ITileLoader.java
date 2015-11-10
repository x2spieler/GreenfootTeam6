package world.mapping;

import java.io.IOException;
import java.util.List;

public interface ITileLoader {
	public List<ITileBlock> loadTiles() throws IOException;
}
