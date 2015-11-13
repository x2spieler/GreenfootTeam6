package world;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseWheelListener;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import AI.Enemy;
import AI.IDamageable;
import AI.IWorldInterfaceForAI;
import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.FieldType;
import DungeonGeneration.MapField;
import core.FrameType;
import core.GodFrame;
import enemies.Werewolf;
import greenfoot.Actor;
import greenfoot.GreenfootImage;
import player.BuffType;
import player.DungeonMover;
import player.Player;
import scrollWorld.FPS;
import scrollWorld.ScrollWorld;
import weapons.abstracts.Bullet;
import weapons.abstracts.Weapon;

public class DungeonMap extends ScrollWorld implements IWorldInterfaceForAI {

	public static final int VIEWPORT_WIDTH = 1024;
	public static final int VIEWPORT_HEIGHT = 768;
	public static final int TILE_SIZE = 32;
	private static final int viewportXTiles = (VIEWPORT_WIDTH / TILE_SIZE);
	private static final int viewportYTiles = (VIEWPORT_HEIGHT / TILE_SIZE);
	private static int greenfootTime=0;
	private long lastTicks;
	private static int ticksAtEndOfLastRound=0;
	
	private int numAliveEnemies=0;

	private DungeonGenerator gen;
	private MapField[][] map;
	// private final boolean[][] fastMap;

	private GreenfootImage ground, wall, back, empty, pickup, destructible;
	//private GreenfootImage[][] tileMap;

	private Player player;

	private GodFrame godFrame = null;

	FPS fps;
	
	
	public DungeonMap() {
		super(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, 1, DungeonGenerator.MAP_WIDTH * TILE_SIZE,
				DungeonGenerator.MAP_HEIGHT * TILE_SIZE);
		gen = new DungeonGenerator(this);
		back = getBackground();
		ground = new GreenfootImage("grass.png");
		wall = new GreenfootImage("wood.png");
		empty = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		pickup = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		destructible = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		initTiles();
		fps = new FPS();
	}
	
	public void playerDied()
	{
		endGame();
		godFrame.changeToFrame(FrameType.GAME_OVER);
	}
	
	public void startNewGame()
	{
		generateNewMap();
		/*try
		{
			tileMap = new DungeonMapper(map).getImageForTilesetHouse();
		} catch (IOException e)
		{
			e.printStackTrace();
		}*/
		player = new Player(100);
		addObject(player, 0, 0);
		lastTicks=System.currentTimeMillis();
		greenfootTime=0;
		addObject(fps, 100, 20);
		spawnEnemies();
	}
	
	public void startNewRound()
	{
		lastTicks=System.currentTimeMillis();
		spawnEnemies();
	}
	
	@SuppressWarnings("unchecked")
	public void endRound()
	{
		List<Object> l=getObjects(null);
		for(Object o:l.toArray())
		{
			if(o instanceof Enemy||o instanceof Bullet)
				removeObject((Actor)o);
		}
		changeToFrame(FrameType.NEXT_ROUND);
		ticksAtEndOfLastRound=getGreenfootTime();
	}
	
	public void endGame()
	{
		removeObjects(getObjects(null));
		player=null;
		numAliveEnemies=0;
	}
	
	private void spawnEnemies()
	{
		Random r = new Random(gen.getSeed());
		spawnWerewolfs(10, r);
		//Increase numAlivEenemies here , spawnWerefols does so
	}
	
	@Override
	public void act()
	{
		super.act();
		long currTicks=System.currentTimeMillis();
		greenfootTime+=currTicks-lastTicks;
		lastTicks=currTicks;
		godFrame.updateTimeLabel(getRoundTime());
		
	}

	public void createGodFrame(JFrame frame) {
		godFrame = new GodFrame(frame, this);
		changeToFrame(FrameType.MAIN_MENU);
		godFrame.updateSeedLabel(gen.getSeed());
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

	private final void generateNewMap() {
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

		return (i >= 0 && j >= 0 && i < DungeonGenerator.MAP_WIDTH && j < DungeonGenerator.MAP_HEIGHT)
				? getTileForFieldType(map[i][j].getFieldType()) : empty;
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

	private void spawnWerewolfs(int num, Random r) {
		for (int k = 0; k < num; k++) {
			int x = r.nextInt(DungeonGenerator.MAP_WIDTH);
			int y = r.nextInt(DungeonGenerator.MAP_HEIGHT);
			Werewolf z = new Werewolf();
			addObject(z, x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2);
			numAliveEnemies++;
		}

	}
	
	@Override
	public void enemyDied(Enemy e)
	{
		numAliveEnemies--;
		addPlayerScore(e.getScore());
		if(numAliveEnemies==0)
		{
			endRound();
		}
	}

	private void addPlayerScore(int score) {
		player.addScore(score);
		updateScoreLabel(player.getScore());
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
					if (Math.sqrt(dX * dX + dY * dY) < Math.sqrt(smallestDX * smallestDX + smallestDY * smallestDY)) {
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

	/**
	 * @return The elapsed time in milliseconds since this game was started - takes pauses into account, only counts really played time
	 */
	public static int getGreenfootTime()
	{
		return greenfootTime;
	}
	
	/**
	 * @return The elapsed time in milliseconds this round ist running - takes pauses into account, only counts really played time
	 */
	public static int getRoundTime()
	{
		return greenfootTime-ticksAtEndOfLastRound;
	}
	
	//////////////JUST FORWARDING FUNCTIONS FOR GOD_FRAME
	
	public void addMouseListenerToContentPane(MouseWheelListener listener) {
		godFrame.addScrollListener(listener);
	}

	public void changeToFrame(FrameType type) {
		godFrame.changeToFrame(type);
	}

	public void updateHealthLabel(int health) {
		godFrame.updateHealthLabel(health);
	}

	public void updateScoreLabel(int score) {
		godFrame.updateScoreLabel(score);
	}

	public void updateAmmoLabel(Weapon w) {
		if (godFrame != null)
			godFrame.updateAmmoLabel(w);
	}

	public void updateWeaponName(Weapon w) {
		if (godFrame != null)
			godFrame.updateWeaponName(w);
	}
	
	public void addOrUpdateBuffLabel(BuffType b, double param, int remainingTime)
	{
		godFrame.addOrUpdateBuffLabel(b, param, remainingTime);
	}

	public void removeBuffLabel(BuffType b)
	{
		godFrame.removeBuffLabel(b);
	}
	
}
