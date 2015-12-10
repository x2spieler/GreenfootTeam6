package world;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import AI.Enemy;
import AI.IWorldInterfaceForAI;
import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.FieldType;
import DungeonGeneration.MapField;
import camera.MyCursor;
import core.FrameType;
import core.GodFrame;
import database.DatabaseHandler;
import database.HighscoreEntry;
import enemies.Bee;
import enemies.BlueFlower;
import enemies.Goblin;
import enemies.Mummy;
import enemies.Orc;
import enemies.PurpleDemon;
import enemies.PurpleEyeGhost;
import enemies.PurpleWorm;
import enemies.RedDragon;
import enemies.RedWitch;
import enemies.SkeletonCape;
import enemies.Snake;
import enemies.Vampire;
import enemies.Zombie;
import greenfoot.Actor;
import greenfoot.GreenfootImage;
import javafx.util.Pair;
import menu.BuyItem;
import objects.Crate;
import objects.DestroyableObject;
import objects.Grave;
import objects.StairsToHeaven;
import objects.Vase;
import player.BuffType;
import player.DungeonMover;
import player.Player;
import scrollWorld.FPS;
import scrollWorld.ScrollActor;
import scrollWorld.ScrollWorld;
import weapons.abstracts.Bullet;
import weapons.abstracts.Weapon;
import weapons.bullets.CrossbowArrow;
import weapons.long_range_weapon.Crossbow;
import weapons.long_range_weapon.NinjaStar;
import weapons.short_range.Axe;
import weapons.short_range.Hammer;
import weapons.short_range.Spear;
import weapons.short_range.Sword;
import world.mapping.DungeonMapper;

public class DungeonMap extends ScrollWorld implements IWorldInterfaceForAI {

	public static final int VIEWPORT_WIDTH = 1024;
	public static final int VIEWPORT_HEIGHT = 768;
	public static final int TILE_SIZE = 32;
	public static final Point PLAYER_START_POS = new Point(VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2);
	private static final int viewportXTiles = (VIEWPORT_WIDTH / TILE_SIZE);
	private static final int viewportYTiles = (VIEWPORT_HEIGHT / TILE_SIZE);
	private static final int fullWidth = DungeonGenerator.MAP_WIDTH * TILE_SIZE;
	private static final int fullHeight = DungeonGenerator.MAP_HEIGHT * TILE_SIZE;
	private static int greenfootTime = 0;
	private long lastTicks;
	private static int ticksAtEndOfLastRound = 0;

	private int numAliveEnemies = 0;

	private DungeonGenerator gen;
	private MapField[][] map;

	private final GreenfootImage back, empty, outOfMap;
	private GreenfootImage[][] tileMap;
	private MapElement[][] specialTiles;

	private Player player;

	private GodFrame godFrame = null;

	private FPS fps;

	private boolean testing = false;
	private boolean running = false;
	private boolean debugging = false;

	boolean enemiesSpawned = false;
	boolean stairsToHeavenSpawned = false;

	PrintStream logger;

	final int BASE_SCORE_FOR_NO_DAMAGE = 100;
	final int BASE_SCORE_FOR_IN_TIME = 100;
	final int BASE_TIME_PER_ROUND = 120/*seconds*/ * 1000/*convert to milliseconds*/;

	private int round = 1;

	private DatabaseHandler dbHandler;

	// TODO: Set custom image in task bar
	// TODO: Balance gameplay
	// TODO: Balance waves
	// TODO: Spawn new destroyable objects after 5 rounds?

	public DungeonMap() {
		super(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, 1, fullWidth, fullHeight);
		back = getBackground();
		empty = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		outOfMap = new GreenfootImage(TILE_SIZE, TILE_SIZE);
		outOfMap.setColor(Color.BLACK);
		outOfMap.fill();
		fps = new FPS();
		setPaintOrder();
		try {
			logger = new PrintStream(new File("Log.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		dbHandler = new DatabaseHandler(this);
	}

	public ArrayList<HighscoreEntry> getTopEntries(int number) {
		return dbHandler.getTopEntries(number);
	}

	public void addHighscore(String name, int highscore) {
		dbHandler.addHighscore(name, highscore);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setPaintOrder(Class... classes) {
		Class[] args = new Class[classes.length + 7];
		args[0] = FPS.class;
		args[1] = MyCursor.class;
		args[2] = MapElement.class;
		args[3] = Enemy.class;
		args[4] = Weapon.class;
		args[5] = Player.class;
		args[6] = DestroyableObject.class;
		for (int i = 0; i < classes.length; i++) {
			args[i + 7] = classes[i];
		}
		super.setPaintOrder(args);
	}

	public void log(String str) {
		logger.println(str);
		logger.flush();
		logger.close();
		System.out.println("Logged: \"" + str + "\"");
	}

	public void logError(String str) {
		logger.println("Error: " + str);
		logger.flush();
		logger.close();
		System.out.println("Logged: Error: \"" + str + "\"");
	}

	public void playerDied() {
		endGame();
		godFrame.changeToFrame(FrameType.GAME_OVER);
	}

	public void startNewGame(int seed) throws AWTException {
		generateNewMap(seed);
		new Thread(() -> {
			try {
				Pair<GreenfootImage[][], MapElement[][]> p = new DungeonMapper(map).getImageForTilesetHouse();
				tileMap = p.getKey();
				specialTiles = p.getValue();
				paintTiles(getCameraX() - VIEWPORT_WIDTH / 2, getCameraY() - VIEWPORT_HEIGHT / 2, 0, 0);
			} catch (IOException e) {
				System.err.println("tileset not loaded");
				e.printStackTrace();
			}
		}).start();
		godFrame.updateSeedLabel(gen.getSeed());
		setPlayer(new Player(100));
		lastTicks = System.currentTimeMillis();
		greenfootTime = 0;
		addObject(fps, 100, 20);
		spawnEnemies();
		log("Seed: " + seed);
		round = 1;
	}

	public void setPlayer(Player player) {
		if (this.player != null) {
			removeObject(this.player);
		}
		this.player = player;
		addObject(player, PLAYER_START_POS.x, PLAYER_START_POS.y);
	}

	public void startNewRound() {
		lastTicks = System.currentTimeMillis();
		stairsToHeavenSpawned = false;

		gen.placeDestructable();
		map = gen.getMap();
		addDestructableObjectsToWorld();

		if (!isInAccessibleTile(player.getGlobalX(), player.getGlobalY())) {
			Point p = getNearestAccessiblePoint(player.getGlobalX(), player.getGlobalY(), player);
			player.setGlobalLocation((int) p.getX(), (int) p.getY());
		}
		spawnEnemies();
	}

	@SuppressWarnings("unchecked")
	public void endRound() {
		if (numAliveEnemies != 0)
			return;

		List<Object> l = getObjects(null);
		for (Object o : l.toArray()) {
			if (o instanceof Enemy || o instanceof Bullet || o instanceof StairsToHeaven)
				removeObject((Actor) o);
			if (o instanceof DestroyableObject) {
				map[((DestroyableObject) o).getGlobalX() / TILE_SIZE][((DestroyableObject) o).getGlobalY() / TILE_SIZE]
						.setFieldType(FieldType.GROUND);
				removeObject((Actor) o);
			}
		}
		changeToFrame(FrameType.NEXT_ROUND);
		ticksAtEndOfLastRound = getGreenfootTime();
		enemiesSpawned = false;
		player.resetWeapons();
		processAbstractGoals();
		round++;
	}

	private void processAbstractGoals() {
		if (!player.getWasDamagedThisRound()) {
			int score = BASE_SCORE_FOR_NO_DAMAGE * round;
			alterPlayerScore(score);
			godFrame.setNoDamageLabelText("You got " + score + " coins for not getting damaged this round!");
		}
		int maxTime = BASE_TIME_PER_ROUND * round;
		if (getGreenfootTime() <= maxTime) {
			int score = BASE_SCORE_FOR_IN_TIME * round;
			alterPlayerScore(score);
			godFrame.setInTimeLabelText("You got " + score + " coins for killing all enemies in time!");
		}
		player.setWasDamagedThisRound(false);
	}

	public void endGame() {
		removeObjects(getObjects(null));
		player = null;
		numAliveEnemies = 0;
		enemiesSpawned = false;
	}

	private void spawnEnemies() {
		if (testing)
			return;
		Random r = new Random(gen.getSeed());
		for (int i = 0; i < 1; i++) {
			int x = r.nextInt(DungeonGenerator.MAP_WIDTH);
			int y = r.nextInt(DungeonGenerator.MAP_HEIGHT);
			x = 0;
			y = 0;
			Enemy e = null;
			switch (r.nextInt(15)) {
			case 0:
				e = new RedDragon();
				break;
			case 1:
				e = new BlueFlower();
				break;
			case 2:
				e = new SkeletonCape();
				break;
			case 3:
				e = new PurpleEyeGhost();
				break;
			case 4:
				e = new PurpleDemon();
				break;
			case 5:
				e = new Goblin();
				break;
			case 6:
				e = new Vampire();
				break;
			case 7:
				e = new RedWitch();
				break;
			case 8:
				e = new RedWitch();
				break;
			case 9:
				e = new Snake();
				break;
			case 10:
				e = new PurpleWorm();
				break;
			case 11:
				e = new Mummy();
				break;
			case 12:
				e = new Zombie();
				break;
			case 13:
				e = new Orc();
				break;
			case 14:
				e = new Bee();
				break;
			}
			addObject(e, x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2);
			numAliveEnemies++;
		}
		// Increase numAlivEenemies here , spawnWerewolfs does so
		enemiesSpawned = true;
	}

	@Override
	public void act() {
		super.act();
		long currTicks = System.currentTimeMillis();
		greenfootTime += currTicks - lastTicks;
		lastTicks = currTicks;
		godFrame.updateTimeLabel(getRoundTime());

		if (enemiesSpawned) {
			if (player.getHP() <= 0) {
				playerDied();
			} else if (numAliveEnemies == 0 && !stairsToHeavenSpawned) {
				stairsToHeavenSpawned = true;
				Random r = new Random();
				StairsToHeaven stairs = new StairsToHeaven();
				int x = 0;
				int y = 0;
				do {
					do {
						x = 1 + r.nextInt(DungeonGenerator.MAP_WIDTH - 2);
						y = 1 + r.nextInt(DungeonGenerator.MAP_HEIGHT - 2);
					} while (!map[x + 1][y].walkable() || !map[x - 1][y].walkable() || !map[x][y + 1].walkable()
							|| !map[x][y - 1].walkable());
				} while (!tryAddObject(stairs, x * TILE_SIZE, y * TILE_SIZE));
				;
				//TODO: Transition sch�ner machen
			}
		}
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
		case WEAPON_CROSSBOW:
			player.addWeapon(new Crossbow(player, amount));
			break;
		case WEAPON_AXE:
			ret = player.addWeapon(new Axe(player));
			break;
		case WEAPON_SPEAR:
			ret = player.addWeapon(new Spear(player));
			break;
		case WEAPON_HAMMER:
			ret = player.addWeapon(new Hammer(player));
			break;
		case WEAPON_NINJA_STAR:
			player.addWeapon(new NinjaStar(player, amount));
			break;
		case WEAPON_SWORD:
			ret = player.addWeapon(new Sword(player));
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
		map = gen.getMap();
		addDestructableObjectsToWorld();
	}

	private void addDestructableObjectsToWorld() {
		Random random = new Random(gen.getSeed());
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (map[x][y].getFieldType() == FieldType.DESTRUCTABLE) {
					DestroyableObject dO = null;
					if (random.nextBoolean())
						dO = new Crate(100, new Point(x, y), gen);
					else if (random.nextBoolean())
						dO = new Vase(100, new Point(x, y), gen);
					else
						dO = new Grave(100, new Point(x, y), gen);
					addObject(dO, x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2);
				}
			}
		}
	}

	//TODO find out why some directions cause more calls to drawImage than others.
	private void paintTiles(final int x, final int y, final int deltax, final int deltay) {
		Point startingTile = getTileOfPixel(x, y);
		Point startingPixel = getStartingPixel(x, y);

		int c = 0;
		int pixelX = startingPixel.x;
		int pixelY;
		ImageObserver io = (a, b, d, e, f, h) -> {
			return false;
		};
		Graphics2D g = back.getAwtImage().createGraphics();
		for (int i = startingTile.x; i <= startingTile.x + viewportXTiles; i++) {
			pixelY = startingPixel.y;
			for (int j = startingTile.y; j <= startingTile.y + viewportYTiles; j++) {
				if (!alreadyDrawn(pixelX, pixelY, deltax, deltay)) {
					g.drawImage(getImageForTile(i, j).getAwtImage(), pixelX, pixelY, io);
					c++;
				}
				pixelY += TILE_SIZE;
			}
			pixelX += TILE_SIZE;
		}
		g.dispose();
		if (debugging)
			log(((deltax == 0 && deltay == 0) ? "full render" : "partial render") + ": " + c + " calls to drawImage");
	}

	private boolean alreadyDrawn(int pixelX, int pixelY, int deltax, int deltay) {
		if (deltax == 0 && deltay == 0)
			return false;
		if (deltax >= 0 && deltay >= 0) {
			return pixelX > deltax + TILE_SIZE && pixelY > deltay + TILE_SIZE;
		}
		if (deltax <= 0 && deltay <= 0) {
			return pixelX < VIEWPORT_WIDTH + deltax - TILE_SIZE && pixelY < VIEWPORT_HEIGHT + deltay - TILE_SIZE;
		}
		if (deltax <= 0 && deltay >= 0) {
			return pixelX < VIEWPORT_WIDTH + deltax - TILE_SIZE && pixelY > deltay + TILE_SIZE;
		}
		if (deltax >= 0 && deltay <= 0) {
			return pixelX > deltax + TILE_SIZE && pixelY < VIEWPORT_HEIGHT + deltay - TILE_SIZE;
		}
		return true;
	}

	private void renderMap(final int x, final int y) {
		if (tileMap == null)
			return;
		int deltax = (getCameraX() - VIEWPORT_WIDTH / 2) - x;
		int deltay = (getCameraY() - VIEWPORT_HEIGHT / 2) - y;

		if (Math.abs(deltax) < VIEWPORT_WIDTH && Math.abs(deltay) < VIEWPORT_HEIGHT) {
			BufferedImage image = back.getAwtImage();
			image.setData(image.getData().createTranslatedChild(deltax, deltay));
			paintTiles(x, y, deltax, deltay);
		} else {
			paintTiles(x, y, 0, 0);
		}
		if (specialTiles != null) {
			for (int i = 0; i < specialTiles.length; i++) {
				for (int j = 0; j < specialTiles[i].length; j++) {
					if (specialTiles[i][j] != null) {
						addObject(specialTiles[i][j], i * TILE_SIZE + TILE_SIZE / 2, j * TILE_SIZE + TILE_SIZE / 2);
					}
				}
			}
			specialTiles = null;
		}
	}

	private boolean cameraPositionChanged(int x, int y) {
		return !(getCameraX() == x && getCameraY() == y);
	}

	private GreenfootImage getImageForTile(int i, int j) {

		return (i >= 0 && j >= 0 && i < DungeonGenerator.MAP_WIDTH && j < DungeonGenerator.MAP_HEIGHT)
				? (tileMap[i][j] != null) ? tileMap[i][j] : empty : outOfMap;
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
		if (x > fullWidth)
			x = fullWidth;
		else if (x < 0)
			x = 0;
		if (y > fullHeight)
			y = fullHeight;
		else if (y < 0)
			y = 0;
		if (cameraPositionChanged(x, y)) {
			renderMap(x - VIEWPORT_WIDTH / 2, y - VIEWPORT_HEIGHT / 2);
		}
		super.setCameraLocation(x, y);
	}

	@Override
	public void enemyDied(Enemy e) {
		numAliveEnemies--;
		alterPlayerScore(e.getScore());
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
		return isInMap(x, y) && map[x / TILE_SIZE][y / TILE_SIZE].walkable();
	}

	public boolean isLegalMove(DungeonMover dungeonMover, int x, int y) {
		return !hasCollision(x, y, dungeonMover) || (isInMap(x, y) && dungeonMover.noclip());
	}

	public boolean hasCollision(int x, int y, ScrollActor sa) {
		if (!(sa instanceof DungeonMover))
			return isInAccessibleTile(x, y);

		DungeonMover dm = (DungeonMover) sa;
		if (dm.getExtent()[0] == -1)
			return !isInAccessibleTile(x, y);
		int rightBound = x + dm.getExtentIn(Direction.RIGHT);
		int lowerBound = y + dm.getExtentIn(Direction.DOWN);
		int leftBound = x - dm.getExtentIn(Direction.LEFT);
		int upperBound = y - dm.getExtentIn(Direction.UP);
		for (int i = leftBound; i <= rightBound; i++) {
			for (int j = upperBound; j <= lowerBound; j++) {
				if (i > leftBound && j > upperBound)
					j = lowerBound;
				if (!isInAccessibleTile(i, j))
					return true;
			}
		}
		return false;
	}

	public boolean isInMap(int x, int y) {
		return (x >= 0 && y >= 0 && x < getFullWidth() && y < getFullHeight());
	}

	private Point getNearestAccessiblePoint(int x, int y, ScrollActor sa) {

		int count = 1;
		int move = 0;
		int dir = ((move % 2) * -1);
		do {
			if (move == 0) {
				count++;
				move = (count / 2);
				dir = (move % 2 == 1) ? -1 : 1;
			}
			if (count % 2 == 0) {
				x += dir;
			} else {
				y += dir;
			}
			if (move > ((DungeonGenerator.MAP_WIDTH + DungeonGenerator.MAP_HEIGHT) * TILE_SIZE * 2)) {
				throw new IllegalStateException("no free tile available x= " + x + " y= " + y);
			}
			move--;
		} while (hasCollision(x, y, sa));
		return new Point(x, y);
	}

	@Override
	public void addObject(final Actor actor, final int x, final int y) {
		if (isInAccessibleTile(x, y)) {
			super.addObject(actor, x, y);
		} else {
			Point p = null;
			if (actor instanceof ScrollActor)
				p = getNearestAccessiblePoint(x, y, (ScrollActor) actor);
			else
				p = new Point(x, y);
			super.addObject(actor, p.x, p.y);
		}

	}

	/**
	 * Checks to see if the chosen location is a legal position for the argument
	 * actor. If it isn't, does nothing and returns false. Always succeeds for
	 * Actors not subclassing DungeonMover.
	 * 
	 * @param object
	 * @param x
	 * @param y
	 * @return returns true only if the actor has been added to this world.
	 */
	public boolean tryAddObject(final Actor actor, final int x, final int y) {
		if (actor instanceof DungeonMover && !isInAccessibleTile(x, y)) {
			return false;
		}
		super.addObject(actor, x, y);
		return true;
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

	public void updateHealthLabel(int health, int maxHealth) {
		godFrame.updateHealthLabel(health, maxHealth);
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

	public void addOrUpdateBuffLabel(BuffType b, double param, int remainingTime, int maxTime) {
		godFrame.addOrUpdateBuffLabel(b, param, remainingTime, maxTime);
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