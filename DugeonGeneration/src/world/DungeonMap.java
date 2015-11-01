package world;

import enemies.Werewolf;
import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import menu.BasicWorldWithMenu;
import menu.Menu;
import player.DungeonMover;
import player.Player;
import scrollWorld.FPS;
import AI.Bullet;
import AI.IDamageable;
import AI.IWorldInterfaceForAI;
import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.MapField;

public class DungeonMap extends BasicWorldWithMenu implements IWorldInterfaceForAI {

	public static final int VIEWPORT_WIDTH = 1024;
	public static final int VIEWPORT_HEIGHT = 768;
	public static final int TILE_SIZE = 32;
	public static final int viewportXTiles = (VIEWPORT_WIDTH % TILE_SIZE == 0) ? (VIEWPORT_WIDTH / TILE_SIZE)
			: (VIEWPORT_WIDTH / TILE_SIZE + 1);
	public static final int viewportYTiles = (VIEWPORT_HEIGHT % TILE_SIZE == 0) ? (VIEWPORT_HEIGHT / TILE_SIZE)
			: (VIEWPORT_HEIGHT / TILE_SIZE + 1);

	private DungeonGenerator gen;
	private MapField[][] map;
	private final boolean[][] fastMap;

	private final GreenfootImage passable, impassable, back, empty;

	private Player player;

	public DungeonMap(Menu menu) {
		super(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, 1, DungeonGenerator.MAP_WIDTH * TILE_SIZE,
				DungeonGenerator.MAP_HEIGHT * TILE_SIZE, menu);
		initDungeonMap();
		fastMap = new boolean[map.length][map[0].length];
		transcribeMap();
		back = getBackground();
		passable = new GreenfootImage("grass.png");
		passable.scale(TILE_SIZE, TILE_SIZE);
		impassable = new GreenfootImage("wood.png");
		impassable.scale(TILE_SIZE, TILE_SIZE);
		empty = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		empty.setColor(Color.BLACK);
		empty.fill();
		player = new Player();
		player.setMode(Player.MOVE_MODE_8_DIRECTIONS);
		addObject(player, 0, 0);
		addObject(new FPS(), 100, 20);
		// player.setNoclip(true);
		// spawnWerewolfs(100);
	}

	private final void initDungeonMap() {
		gen = new DungeonGenerator();
		gen.clearMap();
		gen.generateRooms();
		gen.placeRooms();
		gen.buildPaths();
		map = gen.getMap();
	}

	private void transcribeMap() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				fastMap[i][j] = map[i][j].walkable();
			}
		}
	}

	private void renderMap(int x, int y) {
		int xCorrect = x % TILE_SIZE;
		int yCorrect = y % TILE_SIZE;
		int tileX = x / TILE_SIZE;
		int tileY = y / TILE_SIZE;
		for (int i = tileX; i <= tileX + viewportXTiles; i++) {
			for (int j = tileY; j <= tileY + viewportYTiles; j++) {
				if (i >= 0 && j >= 0 && i < DungeonGenerator.MAP_WIDTH
						&& j < DungeonGenerator.MAP_HEIGHT) {
					back.drawImage((fastMap[i][j]) ? passable : impassable, (i * TILE_SIZE)
							- (tileX * TILE_SIZE) - xCorrect, (j * TILE_SIZE) - (tileY * TILE_SIZE)
							- yCorrect);
				} else {
					back.drawImage(empty, (i * TILE_SIZE) - (tileX * TILE_SIZE) - xCorrect,
							(j * TILE_SIZE) - (tileY * TILE_SIZE) - yCorrect);
				}
			}
		}
	}

	@Override
	public void setCameraLocation(int x, int y) {
		renderMap(x - VIEWPORT_WIDTH / 2, y - VIEWPORT_HEIGHT / 2);
		super.setCameraLocation(x, y);
	}

	public Point getTile(int x, int y) {
		Point ret = new Point();
		ret.x = (x < 0) ? 0 : (x >= getFullWidth()) ? DungeonGenerator.MAP_WIDTH : (x / TILE_SIZE);
		ret.y = (y < 0) ? 0 : (y >= getFullHeight()) ? DungeonGenerator.MAP_HEIGHT
				: (y / TILE_SIZE);
		return ret;
	}

	private void spawnWerewolfs(int num) {
		Random r = new Random();
		MapField[][] map = getMap();
		for (int k = 0; k < num; k++) {
			int x = r.nextInt(DungeonGenerator.MAP_WIDTH);
			int y = r.nextInt(DungeonGenerator.MAP_HEIGHT);
			if (map[x][y].walkable()) {
				Werewolf z = new Werewolf();
				addObject(z, x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2);
			}
		}

	}

	@Override
	public void addPlayerScore(int score) {

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
		// DungeonTestMain.toggleCursor(false);
		Greenfoot.setWorld(this);
	}

	public boolean isInAccessibleTile(int x, int y) {
		if (isInMap(x, y)) {
			return fastMap[x / TILE_SIZE][y / TILE_SIZE];
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
