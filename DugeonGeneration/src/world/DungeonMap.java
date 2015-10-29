package world;

import enemies.Werewolf;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

import java.awt.Point;
import java.util.Random;

import menu.BasicWorldWithMenu;
import menu.Menu;
import player.Player;
import AI.Bullet;
import AI.IDamageable;
import AI.IWorldInterfaceForAI;
import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.MapField;

public class DungeonMap extends BasicWorldWithMenu implements
IWorldInterfaceForAI {

	public static final int VIEWPORT_WIDTH = 800;
	public static final int VIEWPORT_HEIGHT = 600;
	public static final int TILE_SIZE = 32;

	private DungeonGenerator gen;

	private GreenfootImage passable;
	private GreenfootImage impassable;

	private Player player;

	public DungeonMap(Menu menu) {
		super(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, 1, DungeonGenerator.MAP_WIDTH
				* TILE_SIZE, DungeonGenerator.MAP_HEIGHT * TILE_SIZE, menu);
		gen = new DungeonGenerator();
		gen.clearMap();
		gen.generateRooms();
		gen.placeRooms();
		gen.buildPaths();
		passable = new GreenfootImage("grass.png");
		passable.scale(TILE_SIZE, TILE_SIZE);
		impassable = new GreenfootImage("wood.png");
		impassable.scale(TILE_SIZE, TILE_SIZE);
		renderMap();
		player = new Player();
		addObject(player, 0, 0);

		spawnWerewolfs(100);
	}

	private void renderMap() {
		GreenfootImage background = new GreenfootImage(
				DungeonGenerator.MAP_WIDTH * TILE_SIZE,
				DungeonGenerator.MAP_HEIGHT * TILE_SIZE);
		MapField[][] map = gen.getMap();
		for (int i = 0; i < DungeonGenerator.MAP_WIDTH; i++) {
			for (int j = 0; j < DungeonGenerator.MAP_HEIGHT; j++) {
				if (map[i][j].walkable()) {
					background
					.drawImage(passable, i * TILE_SIZE, j * TILE_SIZE);
				} else {
					background.drawImage(impassable, i * TILE_SIZE, j
							* TILE_SIZE);
				}
			}
		}
		setNewBackground(background);
	}

	private void spawnWerewolfs(int num) {
		Random r=new Random();
		MapField[][] map = getMap();
		for (int k = 0; k < num; k++) {
			int x=r.nextInt(DungeonGenerator.MAP_WIDTH);
			int y=r.nextInt(DungeonGenerator.MAP_HEIGHT);
			if (map[x][y].walkable()) {
				Werewolf z = new Werewolf();
				addObject(z, x * TILE_SIZE + TILE_SIZE / 2, y
						* TILE_SIZE + TILE_SIZE / 2);
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
		return gen.getMap();
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

}