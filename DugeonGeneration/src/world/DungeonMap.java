package world;

import enemies.Werewolf;
import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import menu.BuyItem;
import player.BuffType;
import player.DungeonMover;
import player.Player;
import scrollWorld.FPS;
import scrollWorld.ScrollWorld;
import weapons.abstracts.Bullet;
import weapons.abstracts.Weapon;
import weapons.bullets.CrossbowArrow;
import weapons.long_range_weapon.Crossbow;
import weapons.long_range_weapon.NinjaStar;
import weapons.short_range.ClubWithSpikes;
import weapons.short_range.Sword;
import world.mapping.DungeonMapper;
import AI.Enemy;
import AI.IWorldInterfaceForAI;
import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.FieldType;
import DungeonGeneration.MapField;
import core.FrameType;
import core.GodFrame;

public class DungeonMap extends ScrollWorld implements IWorldInterfaceForAI {

	public static final int VIEWPORT_WIDTH = 1024;
	public static final int VIEWPORT_HEIGHT = 768;
	public static final int TILE_SIZE = 32;
	private static final int viewportXTiles = (VIEWPORT_WIDTH / TILE_SIZE);
	private static final int viewportYTiles = (VIEWPORT_HEIGHT / TILE_SIZE);
	private static int greenfootTime = 0;
	private long lastTicks;
	private static int ticksAtEndOfLastRound = 0;

	private int numAliveEnemies = 0;

	private DungeonGenerator gen;
	private MapField[][] map;

	private final GreenfootImage back, empty, outOfMap;
	private GreenfootImage[][] tileMap;

	private Player player;

	private GodFrame godFrame = null;

	private FPS fps;

	private boolean testing = false;
	private boolean running = false;

	// TODO: Change animation system to not top down
	// TODO: Change enemies and player images accordingly
	// TODO: Fancy up the HUD
	// TODO: Implement medi pack

	public DungeonMap() {
		super(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, 1, DungeonGenerator.MAP_WIDTH * TILE_SIZE, DungeonGenerator.MAP_HEIGHT * TILE_SIZE);
		back = getBackground();
		empty = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		outOfMap = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		outOfMap.setColor(Color.BLACK);
		outOfMap.fill();
		fps = new FPS();
	}

	public void playerDied() {
		endGame();
		godFrame.changeToFrame(FrameType.GAME_OVER);
	}

	public void startNewGame(int seed) {
		generateNewMap(seed);
		new Thread(() -> {
			try {
				tileMap = new DungeonMapper(map).getImageForTilesetHouse();
			} catch (IOException e) {
				System.err.println("tileset not loaded");
				e.printStackTrace();
			}
		}).start();
		godFrame.updateSeedLabel(gen.getSeed());
		player = new Player(100);
		addObject(player, 0, 0);
		lastTicks = System.currentTimeMillis();
		greenfootTime = 0;
		addObject(fps, 100, 20);
		spawnEnemies();
	}

	public void startNewRound() {
		lastTicks = System.currentTimeMillis();
		spawnEnemies();
	}

	@SuppressWarnings("unchecked")
	public void endRound() {
		List<Object> l = getObjects(null);
		for (Object o : l.toArray()) {
			if (o instanceof Enemy || o instanceof Bullet)
				removeObject((Actor) o);
		}
		changeToFrame(FrameType.NEXT_ROUND);
		ticksAtEndOfLastRound = getGreenfootTime();
	}

	public void endGame() {
		removeObjects(getObjects(null));
		player = null;
		numAliveEnemies = 0;
	}

	private void spawnEnemies() {
		if (testing)
			return;
		Random r = new Random(gen.getSeed());
		spawnWerewolfs(10, r);
		// Increase numAlivEenemies here , spawnWerewolfs does so
	}

	@Override
	public void act() {
		super.act();
		long currTicks = System.currentTimeMillis();
		greenfootTime += currTicks - lastTicks;
		lastTicks = currTicks;
		godFrame.updateTimeLabel(getRoundTime());
	}

	public void createGodFrame(JFrame frame) {
		godFrame = new GodFrame(frame, this);
		changeToFrame(FrameType.MAIN_MENU);
	}

	public boolean playerTriesToBuy(BuyItem item, int price, int amount) {
		if (price > player.getScore())
			return false;
		boolean ret = true;
		switch (item) {
		case MEDI_PACK:
			player.addMediPacks(1);
			ret = true;
			break;
		case WEAPON_CLUB_WITH_SPIKES:
			player.addWeapon(new ClubWithSpikes(player));
			break;
		case WEAPON_CROSSBOW:
			player.addWeapon(new Crossbow(player, amount));
			break;
		case WEAPON_NINJA_STAR:
			player.addWeapon(new NinjaStar(player, amount));
			break;
		case WEAPON_SWORD:
			player.addWeapon(new Sword(player));
			break;
		case BULLET_CROSSBOW_ARROW:
			ret = player.addAmmo(CrossbowArrow.class, amount);
			break;
		case BULLET_NINJA_STAR:
			ret = player.addAmmo(weapons.bullets.NinjaStar.class, amount);
			break;
		}
		if (ret) {
			alterPlayerScore(-price);
		}
		return ret;
	}

	public void updateFeedbackLabel(boolean success, String msg) {
		godFrame.updateFeedbackLabel(success, msg);
	}

	private final void generateNewMap(int seed) {
		gen = new DungeonGenerator(this, seed);
		gen.clearMap();
		gen.generateRooms();
		gen.placeRooms();
		gen.buildPaths();
		gen.thickenWalls();
		gen.placeDestructable();
		map = gen.getMap();
	}

	private void renderMap(int x, int y) {
		if (tileMap == null)
			return;
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

		return (i >= 0 && j >= 0 && i < DungeonGenerator.MAP_WIDTH && j < DungeonGenerator.MAP_HEIGHT) ? (tileMap[i][j] != null) ? tileMap[i][j] : empty : outOfMap;
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
			x = 0;
			y = 0;
			Werewolf z = new Werewolf();
			addObject(z, x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2);
			numAliveEnemies++;
		}
	}

	@Override
	public void enemyDied(Enemy e) {
		numAliveEnemies--;
		alterPlayerScore(e.getScore());
		if (numAliveEnemies == 0) {
			endRound();
		}
	}

	private void alterPlayerScore(int score) {
		player.addScore(score);
		updateScoreLabel(player.getScore());
		godFrame.updateCoinLabelInShop();
	}

	@Override
	public void addBulletToWorld(Bullet bullet) {

	}

	@Override
	public Player getPlayer() {
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

		int count = 2;
		int move;
		int dir;
		do {
			move = count / 2;
			if (move % 2 == 1) {
				dir = 1;
			} else {
				dir = -1;
			}
			if (count % 2 == 0) {
				x += move * dir * TILE_SIZE;
			} else {
				y += move * dir * TILE_SIZE;
			}
			count++;
		} while (!isInAccessibleTile(x, y));

		return new Point(x, y);
	}

	@Override
	public void addObject(final Actor object, final int x, final int y) {
		if (object instanceof DungeonMover) {
			if (isInAccessibleTile(x, y)) {
				super.addObject(object, x, y);
			} else {
				long t = System.currentTimeMillis();
				Point p = getNearestAccessiblePoint(x, y);
				System.out.println(System.currentTimeMillis() - t);
				super.addObject(object, p.x, p.y);
			}
		} else {
			super.addObject(object, x, y);
		}
	}

	/**
	 * @return The elapsed time in milliseconds since this game was started -
	 *         takes pauses into account, only counts really played time
	 */
	public static int getGreenfootTime() {
		return greenfootTime;
	}

	/**
	 * @return The elapsed time in milliseconds this round ist running - takes
	 *         pauses into account, only counts really played time
	 */
	public static int getRoundTime() {
		return greenfootTime - ticksAtEndOfLastRound;
	}

	// ////////////JUST FORWARDING FUNCTIONS FOR GOD_FRAME

	public void updateMediPackLabel(int mediPacks) {
		godFrame.updateMediPackLabel(mediPacks);
	}

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

	public void addOrUpdateBuffLabel(BuffType b, double param, int remainingTime) {
		godFrame.addOrUpdateBuffLabel(b, param, remainingTime);
	}

	public void removeBuffLabel(BuffType b) {
		godFrame.removeBuffLabel(b);
	}

	public void setTestingMode(boolean testingMode) {
		testing = testingMode;
	}

	public boolean isTestingMode() {
		return testing;
	}

	@Override
	public void started() {
		running = true;
		super.started();
	}

	@Override
	public void stopped() {
		running = false;
		super.stopped();
	}
}
