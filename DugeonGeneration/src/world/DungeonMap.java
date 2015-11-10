package world;

import enemies.Werewolf;
import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.Random;

import menu.BasicWorldWithMenu;
import menu.Menu;
import player.DungeonMover;
import player.Player;
import scrollWorld.FPS;
import weapons.abstracts.Bullet;
import AI.IDamageable;
import AI.IWorldInterfaceForAI;
import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.FieldType;
import DungeonGeneration.MapField;

public class DungeonMap extends BasicWorldWithMenu implements IWorldInterfaceForAI {

	public static final int VIEWPORT_WIDTH = 1024;
	public static final int VIEWPORT_HEIGHT = 768;
	public static final int TILE_SIZE = 32;
	private static final int viewportXTiles = (VIEWPORT_WIDTH / TILE_SIZE);
	private static final int viewportYTiles = (VIEWPORT_HEIGHT / TILE_SIZE);

	private DungeonGenerator gen;
	private MapField[][] map;
	// private final boolean[][] fastMap;

	private GreenfootImage ground, wall, back, empty, pickup, destructible;

	private Player player;

	FPS fps;

	public DungeonMap(Menu menu) throws IOException {
		super(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, 1, DungeonGenerator.MAP_WIDTH * TILE_SIZE,
				DungeonGenerator.MAP_HEIGHT * TILE_SIZE, menu);
		initDungeonMap();

		back = getBackground();
		ground = new GreenfootImage("grass.png");
		wall = new GreenfootImage("wood.png");
		empty = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		pickup = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		destructible = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		initTiles();
		player = new Player(10);
		//player.setMode(Player.MOVE_MODE_8_DIRECTIONS);
		addObject(player, 0, 0);
		fps = new FPS();
		addObject(fps, 100, 20);
		 //player.setNoclip(true);

		//spawnWerewolfs(10);

	}

	private final void initTiles() {
		ground.scale(TILE_SIZE, TILE_SIZE);
		wall.scale(TILE_SIZE, TILE_SIZE);
		empty.setColor(Color.BLACK);
		empty.fill();
		pickup.setColor(Color.YELLOW);
		pickup.fill();
		destructible.setColor(Color.RED);
		destructible.fill();

	}

	private final void initDungeonMap() {
		gen = new DungeonGenerator(this);
		gen.clearMap();
		gen.generateRooms();
		gen.placeRooms();
		gen.buildPaths();
		gen.thickenWalls();
		gen.placeDestructable();
		map = gen.getMap();
	}

	private void renderMap(int x, int y) {
		Point startingTile = getTileOfPixel(x, y);
		Point startingPixel = getStartingPixel(x, y);

		int pixelX = startingPixel.x;
		int pixelY;

		for (int i = startingTile.x; i <= startingTile.x + viewportXTiles; i++) {
			pixelY = startingPixel.y;
			for (int j = startingTile.y; j <= startingTile.y + viewportYTiles; j++) {
				back.drawImage(getImageForTile(i, j), pixelX, pixelY);
				pixelY += TILE_SIZE;
			}
			pixelX += TILE_SIZE;
		}
	}

	private boolean cameraPositionChanged(int x, int y) {
		return !(getCameraX() == x && getCameraY() == y);
	}

	private GreenfootImage getImageForTile(int i, int j) {

		return (i >= 0 && j >= 0 && i < DungeonGenerator.MAP_WIDTH && j < DungeonGenerator.MAP_HEIGHT) ? getTileForFieldType(map[i][j]
				.getFieldType()) : empty;
	}

	private GreenfootImage getTileForFieldType(FieldType type) {
		switch (type) {
		case GROUND:
			return ground;
		case WALL:
			return wall;
		case DESTRUCTABLE:
			return destructible;
		case PICKUP:
			return pickup;
		default:
			throw new IllegalArgumentException("missing tile");
		}
	}

	@Override
	public double getFPS() {
		return fps.getFPS();
	}

	public static Point getStartingPixel(int x, int y) {
		Point ret = new Point(0, 0);

		ret.x = (x > 0) ? -(x % TILE_SIZE) : (-(TILE_SIZE + x % TILE_SIZE) % TILE_SIZE);
		ret.y = (y > 0) ? -(y % TILE_SIZE) : (-(TILE_SIZE + y % TILE_SIZE) % TILE_SIZE);

		assert (ret.x <= 0);
		assert (ret.y <= 0);
		assert (ret.x > -TILE_SIZE);
		assert (ret.y > -TILE_SIZE);

		return ret;
	}

	public static Point getTileOfPixel(int x, int y) {
		Point ret = new Point();

		ret.x = (x >= 0) ? (x / TILE_SIZE) : (x % TILE_SIZE == 0) ? (x / TILE_SIZE) : (x / TILE_SIZE - 1);
		ret.y = (y >= 0) ? (y / TILE_SIZE) : (y % TILE_SIZE == 0) ? (y / TILE_SIZE) : (y / TILE_SIZE - 1);

		assert (x < 0) ? ret.x < 0 : ret.x >= 0;
		assert (y < 0) ? ret.y < 0 : ret.y >= 0;

		return ret;
	}

	@Override
	public void setCameraLocation(int x, int y) {
		if (cameraPositionChanged(x, y)) {
			renderMap(x - VIEWPORT_WIDTH / 2, y - VIEWPORT_HEIGHT / 2);
		}
		super.setCameraLocation(x, y);
	}

	private void spawnWerewolfs(int num) {
		Random r = new Random();
		for (int k = 0; k < num; k++) {
			int x = r.nextInt(DungeonGenerator.MAP_WIDTH);
			int y = r.nextInt(DungeonGenerator.MAP_HEIGHT);
			Werewolf z = new Werewolf();
			addObject(z, x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2);
		}

	}

	@Override
	public void addPlayerScore(int score) {
		player.addScore(score);
	}

	@Override
	public void addBulletToWorld(Bullet bullet) {

	}

	@Override
	public IDamageable getPlayer() {
		return player;
	}

	@Override
	public MapField[][] getMap() {
		return map;
	}

	@Override
	public Point getPlayerPosition() {
		return new Point(player.getGlobalX(), player.getGlobalY());
	}

	@Override
	public int getTileSize() {
		return TILE_SIZE;
	}

	@Override
	public void switchTo() {
		Greenfoot.setWorld(this);
	}

	public boolean isInAccessibleTile(int x, int y) {
		if (isInMap(x, y)) {
			return map[x / TILE_SIZE][y / TILE_SIZE].walkable();
		}
		return false;
	}

	public boolean isLegalMove(DungeonMover dungeonMover, int x, int y) {
		if (isInAccessibleTile(x, y)) {
			return true;
		} else if (isInMap(x, y) && dungeonMover.noclip()) {
			return true;
		}
		return false;
	}

	public boolean isInMap(int x, int y) {
		if (x >= 0 && y >= 0 && x < getFullWidth() && y < getFullHeight()) {
			return true;
		}
		return false;
	}

	private Point getNearestAccessiblePoint(int x, int y) {
		int smallestDX = getFullWidth();
		int smallestDY = getFullHeight();

		for (int i = 0; i < getFullWidth(); i++) {
			for (int j = 0; j < getFullHeight(); j++) {
				if (isInAccessibleTile(i, j)) {
					int dX = x - i;
					int dY = y - j;
					if (Math.sqrt(dX * dX + dY * dY) < Math.sqrt(smallestDX * smallestDX
							+ smallestDY * smallestDY)) {
						smallestDX = dX;
						smallestDY = dY;
					}
				}
			}
		}
		x -= smallestDX;
		y -= smallestDY;

		return new Point(x, y);
	}

	@Override
	public void addObject(Actor object, int x, int y) {
		if (object instanceof DungeonMover) {
			if (isInAccessibleTile(x, y)) {
				super.addObject(object, x, y);
			} else {
				Point p = getNearestAccessiblePoint(x, y);
				super.addObject(object, p.x, p.y);
			}
		} else {
			super.addObject(object, x, y);
		}
	}

}
