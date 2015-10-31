package world;

import enemies.Werewolf;
import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

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

	private DungeonGenerator gen;
	private MapField[][] map;

	private GreenfootImage passable;
	private GreenfootImage impassable;

	private Player player;

	public DungeonMap(Menu menu) {
		super(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, 1, DungeonGenerator.MAP_WIDTH * TILE_SIZE,
				DungeonGenerator.MAP_HEIGHT * TILE_SIZE, menu);
		initDungeonMap();
		passable = new GreenfootImage("grass.png");
		passable.scale(TILE_SIZE, TILE_SIZE);
		// passable = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		// passable.setColor(Color.GREEN);
		// passable.fill();
		impassable = new GreenfootImage("wood.png");
		impassable.scale(TILE_SIZE, TILE_SIZE);
		// impassable = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		// impassable.setColor(Color.RED);
		// impassable.fill();
		renderMap();
		player = new Player();
		player.setMode(Player.MOVE_MODE_8_DIRECTIONS);
		addObject(player, 0, 0);
		addObject(new FPS(), 100, 20);
		// player.setNoclip(true);
		// spawnWerewolfs(100);
	}

	private void initDungeonMap() {
		gen = new DungeonGenerator();
		gen.clearMap();
		gen.generateRooms();
		gen.placeRooms();
		gen.buildPaths();
		map = gen.getMap();
	}

	private void renderMap() {
		GreenfootImage background = new GreenfootImage(DungeonGenerator.MAP_WIDTH * TILE_SIZE,
				DungeonGenerator.MAP_HEIGHT * TILE_SIZE);
		MapField[][] map = gen.getMap();
		for (int i = 0; i < DungeonGenerator.MAP_WIDTH; i++) {
			for (int j = 0; j < DungeonGenerator.MAP_HEIGHT; j++) {
				if (map[i][j].walkable()) {
					background.drawImage(passable, i * TILE_SIZE, j * TILE_SIZE);
				} else {
					background.drawImage(impassable, i * TILE_SIZE, j * TILE_SIZE);
				}
			}
		}
		setNewBackground(background);
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
