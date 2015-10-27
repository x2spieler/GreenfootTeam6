package world;

import greenfoot.GreenfootImage;

import java.awt.Color;
import java.awt.Point;

import player.Player;
import scrollWorld.ScrollWorld;
import AI.Bullet;
import AI.IDamageable;
import AI.IWorldInterfaceForAI;
import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.MapField;
import enemies.Zombie;

public class DungeonMap extends ScrollWorld implements IWorldInterfaceForAI {

	public static final int VIEWPORT_WIDTH = 800;
	public static final int VIEWPORT_HEIGHT = 600;
	public static final int TILE_SIZE = 16;

	private DungeonGenerator gen;

	private GreenfootImage passable;
	private GreenfootImage impassable;
	
	Player player;

	public DungeonMap() {
		super(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, 1, DungeonGenerator.MAP_WIDTH
				* TILE_SIZE, DungeonGenerator.MAP_HEIGHT * TILE_SIZE);
		gen = new DungeonGenerator();
		gen.clearMap();
		gen.generateRooms();
		gen.placeRooms();
		gen.buildPaths();
		passable = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		passable.setColor(Color.green);
		passable.fill();
		passable.drawImage(new GreenfootImage("grass.png"), 0, 0);
		impassable = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		impassable.setColor(Color.red);
		impassable.fill();
		impassable.drawImage(new GreenfootImage("wood.png"), 0, 0);
		renderMap();
		player=new Player();
		addObject(player, 0, 0);

		spawnZombies(1);
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

	private void spawnZombies(int num)
	{
		for(int k=0;k<num;k++)
		{
			boolean br=false;
			Point start=new Point();
			MapField[][] map = gen.getMap();
			for(int i=0;i<DungeonGenerator.MAP_HEIGHT;i++)
			{
				for(int j=0;j<DungeonGenerator.MAP_WIDTH;j++)
				{
					if(map[i][j].walkable())
					{
						start.x=i;
						start.y=j;
						br=true;
						break;
					}
				}
			}
			Zombie z=new Zombie();
			addObject(z, start.x*TILE_SIZE+TILE_SIZE/2, start.y*TILE_SIZE+TILE_SIZE/2);
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
		return null;
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

}
