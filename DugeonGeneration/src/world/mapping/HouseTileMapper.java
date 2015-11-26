package world.mapping;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import world.MapElement;
import DungeonGeneration.FieldType;
import DungeonGeneration.MapField;
import greenfoot.GreenfootImage;

public class HouseTileMapper {
	private enum Direction {
		up, down, left, right;
	}

	public final static DungeonGeneration.FieldType[][] UpperLeftInner, UpperRightInner, LowerLeftInner, LowerRightInner, UpperLeftOuter, UpperRightOuter, LowerLeftOuter, LowerRightOuter, WallFront, WallBack, WallLeft, WallRight, DoubleCornerLeft, DoubleCornerRight, DoubleCornerCenter;

	static {
		UpperLeftInner = new FieldType[][] { { FieldType.WALL, FieldType.WALL }, { FieldType.WALL, FieldType.WALL }, { FieldType.WALL, FieldType.GROUND } };
		UpperRightInner = new FieldType[][] { { FieldType.WALL, FieldType.WALL }, { FieldType.WALL, FieldType.WALL }, { FieldType.GROUND, FieldType.WALL } };
		LowerLeftInner = new FieldType[][] { { FieldType.WALL, FieldType.GROUND }, { FieldType.WALL, FieldType.WALL } };
		LowerRightInner = new FieldType[][] { { FieldType.GROUND, FieldType.WALL }, { FieldType.WALL, FieldType.WALL } };
		UpperLeftOuter = new FieldType[][] { { FieldType.GROUND, FieldType.GROUND }, { FieldType.GROUND, FieldType.WALL } };
		UpperRightOuter = new FieldType[][] { { FieldType.GROUND, FieldType.GROUND }, { FieldType.WALL, FieldType.GROUND } };
		LowerLeftOuter = new FieldType[][] { { FieldType.GROUND, FieldType.WALL }, { FieldType.GROUND, FieldType.WALL }, { FieldType.GROUND, FieldType.GROUND } };
		LowerRightOuter = new FieldType[][] { { FieldType.WALL, FieldType.GROUND }, { FieldType.WALL, FieldType.GROUND }, { FieldType.GROUND, FieldType.GROUND } };
		WallFront = new FieldType[][] { { FieldType.WALL }, { FieldType.WALL }, { FieldType.GROUND } };
		WallBack = new FieldType[][] { { FieldType.GROUND }, { FieldType.WALL } };
		WallLeft = new FieldType[][] { { FieldType.GROUND, FieldType.WALL } };
		WallRight = new FieldType[][] { { FieldType.WALL, FieldType.GROUND } };
		DoubleCornerLeft = new FieldType[][] { { null, FieldType.WALL, FieldType.WALL }, { FieldType.GROUND, FieldType.WALL, FieldType.WALL }, { FieldType.GROUND, FieldType.GROUND, FieldType.WALL }, { null, FieldType.GROUND, FieldType.GROUND } };
		DoubleCornerRight = new FieldType[][] { { FieldType.WALL, FieldType.WALL, null }, { FieldType.WALL, FieldType.WALL, FieldType.GROUND }, { FieldType.WALL, FieldType.GROUND, FieldType.GROUND }, { FieldType.GROUND, FieldType.GROUND, null } };
		DoubleCornerCenter = new FieldType[][] { { FieldType.GROUND, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.GROUND }, { FieldType.GROUND, FieldType.GROUND, FieldType.WALL, FieldType.WALL, FieldType.WALL, FieldType.GROUND, FieldType.GROUND }, { null, null, FieldType.GROUND, FieldType.GROUND, FieldType.GROUND, null, null } };
	}

	private ITileLoader loader = new HouseTileLoader();

	private List<ITileBlock> blocks;

	private Map<FieldType[][], ITileBlock> blockMap;
	private Map<FieldType[][], FieldType[][]> tileMappings;

	private MapField[][] map;

	private GreenfootImage[][] images;
	private MapElement[][] specialTiles;

	public HouseTileMapper(MapField[][] map) throws IOException {
		this.map = map;
		images = new GreenfootImage[map.length][map[0].length];
		specialTiles = new MapElement[map.length][map[0].length];
		blocks = loader.loadTiles();
		blockMap = new LinkedHashMap<>();
		initBlockMap();
		tileMappings = new HashMap<>();
		// initTileMappings();
		// Collection<CircleGraph<Point>> graph = fixMapGeometry();
		// final MapField[][] mf = new MapField[map.length][map[0].length];
		// forEachMapField((i, j) -> mf[i][j] = new
		// MapField(map[i][j].getFieldType()));
		// for (int k = 0; k < mf.length; k++) {
		// for (int k2 = 0; k2 < mf.length; k2++) {
		// assert mf[k][k2].getFieldType().equals(map[k][k2].getFieldType());
		// }
		// }
		// fixWalls();
		mapTiles();
		// for (int k = 0; k < mf.length; k++) {
		// for (int k2 = 0; k2 < mf.length; k2++) {
		// assert mf[k][k2].getFieldType().equals(map[k][k2].getFieldType());
		// }
		// }
		// markNodes(graph);
		// testTiles();
	}

	private void initTileMappings() {
		tileMappings.put(UpperLeftInner, UpperRightInner);
		tileMappings.put(UpperLeftInner, LowerLeftInner);
		tileMappings.put(UpperLeftInner, WallFront);
		tileMappings.put(UpperLeftInner, WallLeft);
		tileMappings.put(UpperRightInner, LowerRightInner);
		tileMappings.put(UpperRightInner, WallRight);
		tileMappings.put(LowerLeftInner, LowerRightInner);
		tileMappings.put(LowerLeftInner, WallBack);
	}

	private void fixWalls() {
		forEachMapField((i, j) -> fillIfEncircled(i, j));
		forEachMapField((i, j) -> removeIfSingleCorner(i, j));
		forEachMapField((i, j) -> deepenIndentsIfNeeded(i, j));
		forEachMapField((i, j) -> removeIfStickingOut(i, j));
		// forEachMapField((i, j) -> thickenWallIfTooThin(i, j));
	}

	private void fillIfEncircled(Integer i, Integer j) {
		if (countAdjacentGroundTiles(i, j) <= 1) {
			placeWall(i, j);
		}
	}

	private void deepenIndentsIfNeeded(Integer i, Integer j) {
		if (isWall(i, j) && (countSurroundingGroundTiles(i, j) == 2) && isNearCorner(i, j)) {
			Direction dir = getDirectionOfIndent(i, j);
			if (dir != null) {
				clearRow(i, j, dir);
			} else {
				placeWall(i, j);
			}
		}
	}

	private void clearRow(Integer i, Integer j, Direction dir) {
		int x = i;
		int y = j;
		do {
			removeWall(x, y);
			switch (dir) {
			case down:
				y++;
				break;
			case left:
				x--;
				break;
			case right:
				x++;
				break;
			case up:
				y--;
				break;
			}
		} while (!isNearCorner(x, y));

	}

	private Direction getDirectionOfIndent(Integer i, Integer j) {
		if ((isWall(i - 1, j + 1) && isGround(i + 1, j + 1)) || (isWall(i - 1, j - 1) && isGround(i + 1, j - 1))) {
			return Direction.right;
		}
		if ((isWall(i + 1, j + 1) && isGround(i - 1, j + 1)) || (isWall(i + 1, j - 1) && isGround(i - 1, j - 1))) {
			return Direction.left;
		}
		if ((isWall(i + 1, j + 1) && isGround(i + 1, j - 1)) || (isWall(i - 1, j + 1) && isGround(i - 1, j - 1))) {
			return Direction.up;
		}
		if ((isWall(i + 1, j - 1) && isGround(i + 1, j + 1)) || (isWall(i - 1, j - 1) && isGround(i - 1, j + 1))) {
			return Direction.down;
		}
		return null;
	}

	private boolean isNearCorner(Integer i, Integer j) {
		final boolean[] ret = new boolean[1];
		forEachSurrounding(i, j, (x, y) -> {
			if (isWallCorner(x, y)) {
				ret[0] = true;
			}
		});
		return ret[0];
	}

	private boolean isWallCorner(Integer i, Integer j) {
		return (isWall(i, j) && countAdjacentGroundTiles(i, j) >= 2);
	}

	private void thickenWallIfTooThin(Integer i, Integer j) {
		if (isWallFacingDown(i, j)) {
			fillWallTwoAbove(i, j);
		}
	}

	private void fillWallTwoAbove(Integer i, Integer j) {
		placeWall(i, j - 1);
		placeWall(i, j - 2);
	}

	private boolean isWallFacingDown(Integer i, Integer j) {
		return (isWallEdge(i, j) && isGround(i, j + 1));
	}

	private boolean isFixed() {
		return isForEachMapfield((i, j) -> !isShortenedCorner(i, j));
	}

	private boolean isShortenedCorner(Integer i, Integer j) {
		if (!isWall(i, j))
			return false;
		return (countAdjacentGroundTiles(i, j) == 2) && (countSurroundingGroundTiles(i, j) < 5);
	}

	private boolean isStickingOut(Integer i, Integer j) {
		if (!isWall(i, j))
			return false;
		return (countAdjacentGroundTiles(i, j) > 2);
	}

	private boolean isSingleCorner(Integer i, Integer j) {
		if (!isWall(i, j))
			return false;
		return (countAdjacentGroundTiles(i, j) == 2) && (countSurroundingGroundTiles(i, j) <= 3);
	}

	private boolean isForEachMapfield(BiFunction<Integer, Integer, Boolean> func) {
		final boolean[] ret = new boolean[1];
		ret[0] = true;
		forEachMapField((i, j) -> {
			boolean b = func.apply(i, j);
			if (!b)
				ret[0] = b;
		});
		return ret[0];
	}

	private void fixWall(Integer i, Integer j) {
		removeIfStickingOut(i, j);
		removeIfSingleCorner(i, j);
		cleanIfShortenedCorner(i, j);

	}

	private void removeIfStickingOut(Integer i, Integer j) {
		if (isStickingOut(i, j)) {
			removeWall(i, j);
		}
	}

	private void removeIfSingleCorner(int i, int j) {
		if (isSingleCorner(i, j)) {
			removeWall(i, j);
		}
	}

	private void cleanIfShortenedCorner(Integer i, Integer j) {
		if (isShortenedCorner(i, j)) {
			cleanCorner(i, j);
		}
	}

	private void cleanCorner(Integer i, Integer j) {
		forEachSurrounding(i, j, (x, y) -> removeWall(x, y));
		forEachSurrounding(i, j, (x, y) -> {
			if (countAdjacentWallTiles(x, y) > 2) {
				placeWall(x, y);
			}
		});
	}

	private void placeWall(Integer x, Integer y) {
		if (isWall(x, y))
			return;
		map[x][y].setFieldType(FieldType.WALL);
	}

	private void removeWall(Integer i, Integer j) {
		if (!isWall(i, j))
			return;
		map[i][j].setFieldType(FieldType.GROUND);
		assert !isWall(i, j) : "removeWall not working";
	}

	@SuppressWarnings("unused")
	private void testTiles() {
		blocks.get(14).transcribe(10, 10, images, null);
		blocks.get(0).transcribe(0, 0, images, null);
		blocks.get(1).transcribe(2, 0, images, null);
		blocks.get(2).transcribe(0, 3, images, null);
		blocks.get(3).transcribe(2, 3, images, null);
		blocks.get(4).transcribe(4, 0, images, null);
		blocks.get(5).transcribe(6, 0, images, null);
		blocks.get(6).transcribe(4, 2, images, null);
		blocks.get(7).transcribe(6, 2, images, null);
		blocks.get(8).transcribe(0, 4, images, null);
		blocks.get(9).transcribe(1, 4, images, null);
		blocks.get(9).transcribe(2, 4, images, null);
		blocks.get(9).transcribe(3, 4, images, null);
		blocks.get(10).transcribe(0, 7, images, null);
		blocks.get(11).transcribe(0, 8, images, null);

	}

	private void initBlockMap() {
		//blockMap.put(DoubleCornerCenter, blocks.get(18));
		blockMap.put(DoubleCornerLeft, blocks.get(14));
		blockMap.put(DoubleCornerRight, blocks.get(15));
		blockMap.put(UpperLeftInner, blocks.get(0));
		blockMap.put(UpperRightInner, blocks.get(1));
		blockMap.put(LowerLeftInner, blocks.get(2));
		blockMap.put(LowerRightInner, blocks.get(3));
		blockMap.put(UpperLeftOuter, blocks.get(4));
		blockMap.put(UpperRightOuter, blocks.get(5));
		blockMap.put(LowerLeftOuter, blocks.get(6));
		blockMap.put(LowerRightOuter, blocks.get(7));
		blockMap.put(WallFront, blocks.get(8));
		blockMap.put(WallBack, blocks.get(9));
		blockMap.put(WallLeft, blocks.get(10));
		blockMap.put(WallRight, blocks.get(11));
	}

	public GreenfootImage[][] getImageArray() {
		return images;
	}

	private void mapTiles() {
		for (int i = 0; i < images.length; i++) {
			for (int j = 0; j < images[0].length; j++) {
				findBlockFor(i, j);
			}
		}
		for (int i = 0; i < images.length; i++) {
			for (int j = 0; j < images[0].length; j++) {
				fill(i, j);
			}
		}
	}

	private Collection<CircleGraph<Point>> fixMapGeometry() {
		// removeNarrowWalls();
		Collection<CircleGraph<Point>> geometry = getGeometryFromMap();
		Iterator<CircleGraph<Point>> iterator = geometry.iterator();
		while (iterator.hasNext()) {
			CircleGraph<Point> circleGraph = iterator.next();
			// if (circleGraph.getNodes().size() < 6) {
			// removeTiles(circleGraph.getNodes());
			// iterator.remove();
			// }
			// fixGraph(circleGraph);
		}
		return geometry;
	}

	private void markNodes(Collection<CircleGraph<Point>> graph) {
		for (CircleGraph<Point> circleGraph : graph) {
			for (Point point : circleGraph) {
				assert isWall(point.x, point.y) : "graph should only contain walls";
				assert isWallEdge(point.x, point.y) : "should be an edge";
				images[point.x][point.y].setColor(Color.RED);
				// images[point.x][point.y].drawString(point.x + "\n" + point.y,
				// 2, 10);
			}
		}
	}

	private void removeTiles(List<Point> nodes) {
		Point point = nodes.get(0);
		removeWithAttachedTiles(point.x, point.y);
	}

	private void removeWithAttachedTiles(int i, int j) {
		if (!isWall(i, j))
			return;
		removeWall(i, j);
		forEachAdjacent(i, j, (x, y) -> {
			if (isWall(x, y))
				removeWithAttachedTiles(x, y);
		});
	}

	private void removeNarrowWalls() {
		forEachMapField((i, j) -> {
			if (isWallEdge(i, j) && countAdjacentGroundTiles(i, j) > 5) {
				removeWall(i, j);
			}
		});
	}

	private void fixGraph(CircleGraph<Point> circleGraph) {
		Point last;
		Point current;
		Direction lastDir = null;
		int stepsInSameDirection = 0;
		// TODO
		do {
			last = circleGraph.peakLast();
			current = circleGraph.get();
			Direction dir = getDirectionFromTo(last, current);
			if (lastDir != null) {
				if (lastDir == dir) {
					stepsInSameDirection++;
				} else {
					stepsInSameDirection = 0;
					lastDir = dir;
				}
			}
			circleGraph.step();
		} while (!circleGraph.isAtStart());
	}

	private Direction getDirectionFromTo(Point last, Point current) {
		if (last.x == current.x) {
			if (current.y == last.y + 1)
				return Direction.down;
			if (current.y == last.y - 1)
				return Direction.up;
		} else if (last.y == current.y) {
			if (current.x == last.x + 1)
				return Direction.right;
			if (current.x == last.x - 1)
				return Direction.left;
		}
		throw new IllegalArgumentException("Points aren't connected");
	}

	private Collection<CircleGraph<Point>> getGeometryFromMap() {
		Collection<CircleGraph<Point>> ret = new ArrayList<>();
		List<Point> wallEdges = getWallEdges();
		while (!wallEdges.isEmpty()) {
			Point start = wallEdges.get(0);
			CircleGraph<Point> g = new CircleGraph<Point>(getConnectedTiles(start));
			ret.add(g);
			assert g.getNodes().contains(start) : "graph does not contain starting point";
			wallEdges.removeAll(g.getNodes());
			assert !wallEdges.contains(start);
		}
		return ret;
	}

	private List<Point> getConnectedTiles(final Point start) {
		final List<Point> ret = new ArrayList<>();
		Point current = start;
		do {
			assert isWall(current.x, current.y) : "should be a wall tile";
			ret.add(new Point(current));
			current = getNextEdge(current, ret, start);
		} while (!(start.equals(current)));
		return ret;
	}

	private Point getNextEdge(final Point current, final List<Point> known, final Point start) {
		final Point ret = new Point(current);
		forEachAdjacent(ret.x, ret.y, (i, j) -> {
			Point p = new Point(i, j);
			if (!known.contains(p) && isWallEdge(p.x, p.y))
				ret.setLocation(p);
		});
		if (ret.equals(current)) {
			ret.setLocation(start);
		}
		return ret;
	}

	private List<Point> getAdjacentEdges(Point current) {
		final List<Point> ret = new ArrayList<Point>();
		forEachAdjacent(current.x, current.y, (i, j) -> addIfIsWallEdge(i, j, ret));
		return ret;
	}

	private void addIfIsWallEdge(Integer i, Integer j, List<Point> ret) {
		if (isWallEdge(i, j)) {
			ret.add(new Point(i, j));
		}
	}

	private void forEachAdjacent(int x, int y, BiConsumer<Integer, Integer> func) {
		func.accept(x + 1, y);
		func.accept(x - 1, y);
		func.accept(x, y + 1);
		func.accept(x, y - 1);
	}

	private void forEachDiagonalAdjacent(int x, int y, BiConsumer<Integer, Integer> func) {
		func.accept(x + 1, y + 1);
		func.accept(x - 1, y - 1);
		func.accept(x - 1, y + 1);
		func.accept(x + 1, y - 1);
	}

	private void forEachSurrounding(Integer x, Integer y, BiConsumer<Integer, Integer> func) {
		forEachAdjacent(x, y, func);
		forEachDiagonalAdjacent(x, y, func);
	}

	private List<Point> getWallEdges() {
		final List<Point> ret = new ArrayList<>();
		forEachMapField((i, j) -> addIfWallEdge(ret, i, j));
		return ret;
	}

	private void addIfWallEdge(final Collection<Point> ret, Integer i, Integer j) {
		if (isWallEdge(i, j)) {
			ret.add(new Point(i, j));
		}
	}

	private boolean isWallEdge(Integer i, Integer j) {
		return isWall(i, j) && (countSurroundingGroundTiles(i, j) > 0);
	}

	private void forEachMapField(BiConsumer<Integer, Integer> func) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				func.accept(i, j);
			}
		}
	}

	private void fixOuterCorners(int i, int j) {
		GreenfootImage ULO, URO, LLO, LRO;
		ULO = blocks.get(4).getImageAt(1, 0);
		URO = blocks.get(5).getImageAt(0, 0);
		LLO = blocks.get(6).getImageAt(1, 1);
		LRO = blocks.get(7).getImageAt(0, 1);
		if (isWall(i, j) && isWall(i - 1, j) && isWall(i, j - 1) && isGround(i + 1, j) && isGround(i, j + 1)) {
			images[i][j] = LRO;
		}
	}

	private void fill(int i, int j) {
		if (images[i][j] != null)
			return;
		if (isGround(i, j)) {
			blocks.get(13).transcribe(i, j, images, null);
		} else if (isWall(i, j)) {
			blocks.get(12).transcribe(i, j, images, null);
		}
	}

	private void findBlockFor(int i, int j) {
		if (images[i][j] != null)
			return;
		for (FieldType[][] tb : blockMap.keySet()) {
			if (fitsMapAt(i, j, tb)) {
				blockMap.get(tb).transcribe(i, j, images, specialTiles);
			}
		}
	}

	private boolean fitsMapAt(int i, int j, FieldType[][] field) {
		if (i < 0 || j < 0 || (i + field[0].length) > map.length || (j + field.length) > map[0].length)
			return false;
		for (int k = 0; k < field.length; k++) {
			for (int k2 = 0; k2 < field[k].length; k2++) {
				if (field[k][k2] == null) {
					continue;
				}
				FieldType f = map[i + k2][j + k].getFieldType();
				if (field[k][k2] != f) {
					if (f == FieldType.DESTRUCTABLE && field[k][k2] == FieldType.GROUND) {
						continue;
					}
					return false;
				}
			}
		}
		return true;
	}

	private boolean isInMap(int i, int j) {
		if (i >= 0 && i < map.length && j >= 0 && j < map[0].length) {
			return true;
		}
		return false;
	}

	private boolean isWall(int i, int j) {
		return isInMap(i, j) && map[i][j].getFieldType().equals(FieldType.WALL);
	}

	private boolean isGround(int i, int j) {
		if (!isInMap(i, j))
			return false;
		FieldType t = map[i][j].getFieldType();
		return t.equals(FieldType.GROUND) || t.equals(FieldType.DESTRUCTABLE);
	}

	private int countAdjacentGroundTiles(int i, int j) {
		int ret = 0;
		if (isGround(i + 1, j)) {
			ret++;
		}
		if (isGround(i, j + 1)) {
			ret++;
		}
		if (isGround(i - 1, j)) {
			ret++;
		}
		if (isGround(i, j - 1)) {
			ret++;
		}
		return ret;
	}

	private int countDiagonalAdjacentGroundTiles(int i, int j) {
		int ret = 0;
		if (isGround(i + 1, j + 1)) {
			ret++;
		}
		if (isGround(i - 1, j - 1)) {
			ret++;
		}
		if (isGround(i + 1, j - 1)) {
			ret++;
		}
		if (isGround(i - 1, j + 1)) {
			ret++;
		}
		return ret;
	}

	private int countSurroundingGroundTiles(int i, int j) {
		return countAdjacentGroundTiles(i, j) + countDiagonalAdjacentGroundTiles(i, j);
	}

	private int countSurroundingWallTiles(int i, int j) {
		return countAdjacentWallTiles(i, j) + countDiagonalAdjacentWallTiles(i, j);
	}

	private int countAdjacentWallTiles(int i, int j) {
		int ret = 0;
		if (isWall(i + 1, j)) {
			ret++;
		}
		if (isWall(i, j + 1)) {
			ret++;
		}
		if (isWall(i - 1, j)) {
			ret++;
		}
		if (isWall(i, j - 1)) {
			ret++;
		}
		return ret;
	}

	private int countDiagonalAdjacentWallTiles(int i, int j) {
		int ret = 0;
		if (isWall(i + 1, j + 1)) {
			ret++;
		}
		if (isWall(i - 1, j - 1)) {
			ret++;
		}
		if (isWall(i + 1, j - 1)) {
			ret++;
		}
		if (isWall(i - 1, j + 1)) {
			ret++;
		}
		return ret;
	}

	public MapElement[][] getSpecialTiles() {
		return specialTiles;
	}

}
