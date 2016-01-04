package AI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.MapField;
import greenfoot.GreenfootImage;
import greenfoot.World;
import player.DeltaMover;
import weapons.abstracts.Weapon;
import weapons.long_range_weapon.Crossbow;
import weapons.long_range_weapon.LeafThrower;
import weapons.long_range_weapon.LoveWand;
import weapons.long_range_weapon.Sceptre;
import weapons.short_range.AcidBubbles;
import weapons.short_range.Bite;
import weapons.short_range.Bone;
import weapons.short_range.Dagger;
import weapons.short_range.DoubleAxe;
import weapons.short_range.FlameStorm;
import weapons.short_range.Psycho;
import weapons.short_range.Shovel;
import weapons.short_range.Sting;
import weapons.short_range.Sword;
import weapons.short_range.Tongue;
import weapons.short_range.WoodStake;
import world.DungeonMap;

public abstract class Enemy extends DeltaMover implements IDamageable {

	protected final int NUM_FRAMES_CHANGE_WALK_IMAGE;
	protected Weapon weapon = null;
	protected int value = -1;
	protected int hp = -1;

	protected String enemyName = "";
	protected GreenfootImage[][] images;
	protected String[] allowedWeapons;

	private boolean isAttacking = false;
	private boolean cantFindWay = false;
	private boolean couldNotFindWay=false;
	private Node currTargetNode = null;
	private IWorldInterfaceForAI wi = null;
	private Point lastPlayerTile = null;
	private final int REACHED_TARGET_DISTANCE_SQUARED = 25;
	private final int REACHED_PLAYER_DISTANCE_SQUARED = 1024;
	private final int RPD_MULTIPLICATOR_LRW = 30; //REACHED_PLAYER_DISTANCE_MULTIPLICATOR_LONG_RANGE_WEAPONS
	private final int TILE_SIZE = DungeonMap.TILE_SIZE;
	private short walkCounter = 0;
	private boolean isPendingKill = false;
	private int currRotation = 0;

	protected enum ImageIndex {
		IDLE(0), WALK1(1), WALK2(2), ATTACK(0);

		private int val;

		private ImageIndex(int val) {
			this.val = val;
		}

		public int getValue() {
			return val;
		}
	}

	public Enemy() {
		super(0);
		NUM_FRAMES_CHANGE_WALK_IMAGE = getNumFramesChangeWalkImage();
	}

	protected abstract int getNumFramesChangeWalkImage();

	@Override
	public int getHP() {
		return hp;
	}

	@Override
	public void addedToWorld(World w) {
		super.addedToWorld(w);
		wi = (IWorldInterfaceForAI) getWorld();
		if (wi == null) {
			System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");
			return;
		}
		loadImages();
		alterImages();
		createWeapon();
	}

	/*
	 * Can be overriden to change certain animation images.
	 * Default implementation does nothing
	 */
	protected void alterImages() {
	}

	/**
	 * @return True if this enemy has been killed and is only in the world to to
	 *         his death animation
	 */
	public boolean isPendingKill() {
		return isPendingKill;
	}

	private void loadImages() {
		images = new GreenfootImage[4][3];
		for (int i = 0; i < 4; i++) {
			images[i][ImageIndex.IDLE.getValue()] = new GreenfootImage("enemies/" + enemyName + "/" + enemyName + "_" + i + "_base.png");
			images[i][ImageIndex.WALK1.getValue()] = new GreenfootImage("enemies/" + enemyName + "/" + enemyName + "_" + i + "_walk1.png");
			images[i][ImageIndex.WALK2.getValue()] = new GreenfootImage("enemies/" + enemyName + "/" + enemyName + "_" + i + "_walk2.png");
			images[i][ImageIndex.ATTACK.getValue()] = new GreenfootImage("enemies/" + enemyName + "/" + enemyName + "_" + i + "_walk1.png");
		}
	}

	private void createWeapon() {
		Random r = new Random();
		switch (allowedWeapons[r.nextInt(allowedWeapons.length)]) {
		case "sword":
			weapon = new Sword(this);
			break;
		case "flame_storm":
			weapon = new FlameStorm(this);
			break;
		case "sting":
			weapon = new Sting(this);
			break;
		case "dagger":
			weapon = new Dagger(this);
			break;
		case "double_axe":
			weapon = new DoubleAxe(this);
			break;
		case "acid_bubbles":
			weapon = new AcidBubbles(this);
			break;
		case "psycho":
			weapon = new Psycho(this);
			break;
		case "bite":
			weapon = new Bite(this);
			break;
		case "bone":
			weapon = new Bone(this);
			break;
		case "tongue":
			weapon = new Tongue(this);
			break;
		case "wood_stake":
			weapon = new WoodStake(this);
			break;
		case "shovel":
			weapon = new Shovel(this);
			break;
		case "leaf_thrower":
			weapon = new LeafThrower(this, Integer.MAX_VALUE);
			break;
		case "love_wand":
			weapon = new LoveWand(this, Integer.MAX_VALUE);
			break;
		case "sceptre":
			weapon = new Sceptre(this, Integer.MAX_VALUE);
			break;
		case "crossbow":
			weapon = new Crossbow(this, Integer.MAX_VALUE);
			break;
		case "ninja_star":
			weapon = new weapons.long_range_weapon.NinjaStar(this, Integer.MAX_VALUE);
			break;
		default:
			throw new IllegalArgumentException("Seems like somebody forgot to update this switch-statement after adding new weapons.. Bad boy!");
		}
		if (weapon != null)
			getWorld().addObject(weapon, 0, 0);
	}

	@Override
	public void damage(int dmg) {
		if (isPendingKill)
			return;
		hp -= dmg;
		if (hp <= 0) {
			setImage("tombstone.png");
			getWorld().removeObject(weapon);
			isPendingKill = true;
			wi.enemyDied(this);
		}
	}

	public int getScore() {
		return value + weapon.getAdditionalValue();
	}

	public void stopAttacking() {
		isAttacking = false;
	}

	private GreenfootImage getCurrentImage(ImageIndex indx) {
		return images[((currRotation + 45) % 360) / 90][indx.getValue()];
	}

	@Override
	public int getRotation() {
		return currRotation;
	}

	@Override
	public void act() {
		if (isPendingKill) {
			int transp = getImage().getTransparency();
			transp -= 1;
			if (transp == 0) {
				getWorld().removeObject(this);
			} else
				getImage().setTransparency(transp);
			return;
		}

		super.act();

		if (getImage() == null)
			setImage(getCurrentImage(ImageIndex.IDLE));

		Point currPlayerTile = wi.getPlayerPosition();
		currPlayerTile.x /= TILE_SIZE;
		currPlayerTile.y /= TILE_SIZE;
		Point currTile = new Point(getGlobalX() / TILE_SIZE, getGlobalY() / TILE_SIZE);

		if (currTargetNode == null) {
			int squaredDistance = squaredDistance(wi.getPlayerPosition().x, wi.getPlayerPosition().y, getGlobalX(), getGlobalY());
			if (!isAttacking && ((!weapon.isLongRangeWeapon() && squaredDistance > REACHED_PLAYER_DISTANCE_SQUARED) || (weapon.isLongRangeWeapon() && squaredDistance > RPD_MULTIPLICATOR_LRW * REACHED_PLAYER_DISTANCE_SQUARED))) {
				currTargetNode = findPath(currTile, currPlayerTile, true);
				if(couldNotFindWay)
				{
					//Once more were spawned in a room that is not connected to any other room *Sigh*
					//Get us another location that is not that anti-social [i.e. get a location that is connected to the rest of the map]
					MapField[][] mf=wi.getMap();
					Random r = new Random(wi.getSeed());
					int x = r.nextInt(DungeonGenerator.MAP_WIDTH);
					int y = r.nextInt(DungeonGenerator.MAP_HEIGHT);
					while(!mf[x][y].walkable())
					{
						x = r.nextInt(DungeonGenerator.MAP_WIDTH);
						y = r.nextInt(DungeonGenerator.MAP_HEIGHT);
					}
					setGlobalLocation(x*TILE_SIZE+TILE_SIZE/2, y*TILE_SIZE+TILE_SIZE/2);
					couldNotFindWay=false;
				}
			} else {
				if (getImage() != getCurrentImage(ImageIndex.IDLE) && !isAttacking) {
					setImage(getCurrentImage(ImageIndex.IDLE));
				}
				turnTowardsGlobalLocation(wi.getPlayerPosition().x, wi.getPlayerPosition().y);
				currRotation = super.getRotation();
				setRotation(0);
				if (weapon.use()) {
					isAttacking = true;
					setImage(getCurrentImage(ImageIndex.ATTACK));
				}
			}
		} else {
			if (!currPlayerTile.equals(lastPlayerTile)) {
				//If the player changed his position, go to his new location
				updatePathToPlayer(currPlayerTile);
				lastPlayerTile = currPlayerTile;
			}
		}

		if (currTargetNode != null) {
			turnTowardsGlobalLocation(currTargetNode.x, currTargetNode.y);
			currRotation = super.getRotation();
			setRotation(0);
			if (!isAttacking)
				moveAtAngle(currRotation);
			int squaredDistToTarget = squaredDistance(currTargetNode.x, currTargetNode.y, getGlobalX(), getGlobalY());
			int squaredDistanceToPlayer = squaredDistance(wi.getPlayerPosition().x, wi.getPlayerPosition().y, getGlobalX(), getGlobalY());
			if (isTouchingWall()) {
				//We ran into a wall and missed our target or can't reach it - just get a new target next frame
				currTargetNode = null;
			} else if (weapon.isLongRangeWeapon() && squaredDistanceToPlayer <= RPD_MULTIPLICATOR_LRW * REACHED_PLAYER_DISTANCE_SQUARED && canSee(new Point(getGlobalX(), getGlobalY()), wi.getPlayerPosition())) {
				//We have a LongRangeWeapon, we are close enough
				currTargetNode = null;
			} else if ((currTargetNode.prev == null && squaredDistToTarget <= REACHED_PLAYER_DISTANCE_SQUARED) //Target is player
					|| (squaredDistToTarget <= REACHED_TARGET_DISTANCE_SQUARED)) //Target is a tile
			{
				currTargetNode = currTargetNode.prev;
				if (currTargetNode != null) {
					turnTowardsGlobalLocation(currTargetNode.x, currTargetNode.y);
					currRotation = super.getRotation();
					setRotation(0);
				}
			}
			//Animate the enemie
			if (walkCounter <= NUM_FRAMES_CHANGE_WALK_IMAGE) {
				setImage(getCurrentImage(ImageIndex.WALK1));
			} else if (walkCounter < 2 * NUM_FRAMES_CHANGE_WALK_IMAGE) {
				setImage(getCurrentImage(ImageIndex.WALK2));
			}
			if (walkCounter == 2 * NUM_FRAMES_CHANGE_WALK_IMAGE) {
				walkCounter = 0;
			} else
				walkCounter++;
		}
	}

	private void updatePathToPlayer(Point end) {
		Node n = currTargetNode;
		Node shortestDistance = null;
		int minDist = Integer.MAX_VALUE;
		int limit = 8 + new Random().nextInt(6);
		while ((n = n.prev) != null) {
			int manDist = (int) manhattanDistance(n.x / TILE_SIZE, n.y / TILE_SIZE, end.x, end.y);
			if (manDist < minDist) {
				minDist = manDist;
				shortestDistance = n;

			}
		}
		if (shortestDistance != null) {
			Point start = new Point(shortestDistance.x / TILE_SIZE, shortestDistance.y / TILE_SIZE);
			if (manhattanDistance(start, end.x, end.y) > limit)
				currTargetNode = findPath(new Point(getGlobalX() / TILE_SIZE, getGlobalY() / TILE_SIZE), end, true);
			else
				shortestDistance.prev = findPath(start, end, true);

		}

	}

	/**
	 * Super simple raytracer, might miss edges. However, suitable for our needs
	 */
	private boolean canSee(Point p1, Point p2) {
		double xDir = p1.x - p2.x;
		double yDir = p1.y - p2.y;
		double length = Math.sqrt(xDir * xDir + yDir * yDir);
		double xNorm = xDir / length;
		double yNorm = yDir / length;
		double currX = p2.x;
		double currY = p2.y;
		double add = TILE_SIZE / 4;
		DungeonMap dm = (DungeonMap) getWorld();
		while (!(Math.abs(p1.x - currX) < 1 && Math.abs(p1.y - currY) < 1)) {
			currX += xNorm * add;
			currY += yNorm * add;
			if (!dm.isInAccessibleTile((int) currX, (int) currY))
				return false;
		}
		return true;
	}

	private int squaredDistance(int x1, int y1, int x2, int y2) {
		int distX = x1 - x2;
		int distY = y1 - y2;
		return (distX * distX) + (distY * distY);
	}

	private Node findPath(Point start, Point end, boolean toPlayer) {
		ArrayList<Node> closedList = new ArrayList<Node>();
		ArrayList<Node> openList = new ArrayList<Node>();

		MapField[][] map = wi.getMap();
		couldNotFindWay=false;

		openList.add(new Node(manhattanDistance(start, end.x, end.y), 0, end.x, end.y, null));

		Node endNode = null;
		while (endNode == null) {
			endNode = step(closedList, openList, map, start);
			if (cantFindWay || !map[end.x][end.y].walkable() || !map[start.x][start.y].walkable()) {
				cantFindWay = false;
				System.out.println("Couldn't find a way");
				couldNotFindWay=true;
				return null;
			}
		}
		if (endNode != null) {
			Node n = endNode;
			while (n.prev != null) {
				n = n.prev;
				n.x = n.x * TILE_SIZE + TILE_SIZE / 2;
				n.y = n.y * TILE_SIZE + TILE_SIZE / 2;
			}
			if (toPlayer) {
				n.x = wi.getPlayerPosition().x;
				n.y = wi.getPlayerPosition().y;
			}
		}

		return (endNode != null ? endNode.prev : null);
	}

	private Node step(ArrayList<Node> closedList, ArrayList<Node> openList, MapField[][] map, Point start) {
		if (openList.size() == 0) {
			cantFindWay = true;
			return null;
		}
		Node closest = openList.get(0);
		for (Node node : openList) {
			if (node.cost < closest.cost) {
				closest = node;
			}
		}

		if (closest.x == start.x && closest.y == start.y) {
			return closest;
		}
		int x = -1, y = -1;
		int addX = 1;
		int addY = 0;
		for (int i = 0; i < 4; i++) {
			if (i == 1 || i == 3) {
				addX *= -1;
				addY *= -1;
			}
			if (i == 2) {
				addX = 0;
				addY = 1;
			}
			//1. Loop: 1 , 0
			//2. Loop: -1, 0
			//3. Loop: 0, 1l
			//4. Loop: 0, -1
			x = closest.x + addX;
			y = closest.y + addY;
			if (x < 0 || y < 0 || x >= map.length || y >= map[0].length)
				continue;
			Node currNode = new Node(manhattanDistance(start, x, y) + closest.movementCost + 1, closest.movementCost + 1, x, y, closest);
			if (map[x][y].walkable() && !closedList.contains(currNode)) {
				if (!(map[x + 1][y].walkable() && map[x - 1][y].walkable() && map[x][y + 1].walkable() && map[x][y - 1].walkable()))
					currNode.movementCost += 1;
				int indx = openList.indexOf(currNode);
				if (indx == -1) {
					openList.add(currNode);
				} else {
					Node inList = openList.get(indx);
					if (currNode.cost <= inList.cost) {
						inList.cost = currNode.cost;
						inList.prev = currNode.prev;
						inList.movementCost = currNode.movementCost;
					}
				}
			}
		}

		openList.remove(closest);
		closedList.add(closest);

		return null;
	}

	private double manhattanDistance(Point start, int x, int y) {
		return Math.abs(start.x - x) + Math.abs(start.y - y);
	}

	private double manhattanDistance(int x2, int y2, int x, int y) {
		return Math.abs(x2 - x) + Math.abs(y2 - y);
	}

	class Node {
		double cost;
		double movementCost;
		int x;
		int y;
		Node prev;

		public Node(double cost, double movementCost, int x, int y, Node prev) {
			this.movementCost = movementCost;
			this.cost = cost;
			this.x = x;
			this.y = y;
			this.prev = prev;
		}

		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object v) {
			if (v instanceof Node) {
				Node n = (Node) v;
				return n.x == x && n.y == y;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return x * 100000 + y;
		}
	}

}
