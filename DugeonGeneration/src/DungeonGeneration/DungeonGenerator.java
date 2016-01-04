package DungeonGeneration;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

//import greenfoot.World;

//Branching

import greenfoot.Actor;
import objects.Crate;
import objects.DestroyableObject;
import objects.Grave;
import objects.Vase;
import world.DungeonMap;

public class DungeonGenerator {

	public static final int ROOM_POOL = 40;
	public static int usedRooms = ROOM_POOL;

	public static final int MAP_WIDTH = 150;
	public static final int MAP_HEIGHT = 150;

	public static final int MAX_ROOM_SIZE = 15;
	public static final int MIN_ROOM_SIZE = 10;
	public static final int ROOM_BORDER = 13;

	public static final int MAX_PATH_WIDTH = 5;
	public static final int MIN_PATH_WIDTH = 3;

	public static final int MAP_BORDER = ROOM_BORDER + 2;

	private final int SPACE_NEXT_TO_PATH = 3;
	private final int MIN_SUB_PATHS_LENGTH = 3;

	private MapField[][] mapBlocks = new MapField[MAP_WIDTH][MAP_HEIGHT];
	int[][] distances = new int[usedRooms][usedRooms];

	private Room[] rooms = new Room[ROOM_POOL];

	ArrayList<Integer> visited = new ArrayList<Integer>();
	ArrayList<Integer> ignored = new ArrayList<Integer>();

	private int mapSeed = 0;

	MegaRandom rand;
	MegaRandom randomSeed;

	private DungeonMap dm = null;

	boolean couldnNotFindWay = false;
	boolean startPointBlocked = false;
	boolean endPointBlocked = false;

	int unreachableRooms = 0;

	private Node lastWayBuffer = null;

	public DungeonGenerator(DungeonMap dm) {
		randomSeed = new MegaRandom();
		mapSeed = randomSeed.randomInt(0, Integer.MAX_VALUE - 1);
		rand = new MegaRandom(mapSeed);

		this.dm = dm;
		initGen();
	}

	public DungeonGenerator(DungeonMap dm, int seed) {
		mapSeed = seed;
		rand = new MegaRandom(mapSeed);
		this.dm = dm;
		initGen();

	}

	//	public MapField[][] getMap() {
	//
	//		return mapBlocks;
	//
	//	}

	public int getSeed() {
		return mapSeed;
	}

	public void setMapFieldsType(int x, int y, FieldType fieldType) {
		mapBlocks[x][y].setFieldType(fieldType);
	}

	public void initGen() {
		clearMap();
		//removeCells();
		generateRooms();
		connectClosestRooms();
		removeSmallerSets();
		convertCells();
		dm.setMap(mapBlocks);
		placeDestructable();
		//showMap();
	}

	//Clears the map by setting every single field to fieldType wall
	public void clearMap() {

		for (int y = 0; y < MAP_HEIGHT; y++) {

			for (int x = 0; x < MAP_WIDTH; x++) {

				mapBlocks[x][y] = new MapField(FieldType.WALL);

			}
		}

		for (int y = 1; y < MAP_HEIGHT - 1; y++) {

			for (int x = 1; x < MAP_WIDTH - 1; x++) {

				mapBlocks[x][y] = new MapField(FieldType.CELL);

			}
		}
	}

	public void convertCells() {

		for (int y = 1; y < MAP_HEIGHT - 1; y++) {

			for (int x = 1; x < MAP_WIDTH - 1; x++) {

				if (mapBlocks[x][y].getFieldType() == FieldType.CELL) {
					mapBlocks[x][y].setFieldType(FieldType.WALL);
				} else {
					mapBlocks[x][y].setFieldType(FieldType.GROUND);
				}

			}
		}
	}

	//Populates the "rooms" array with random sized rectangular rooms. 
	public void generateRooms() {

		int attempts = 0;

		for (int i = 0; i < ROOM_POOL; i++) {

			if (attempts++ >= ROOM_POOL * 500) {
				usedRooms = i;
				break;
			}
			;

			int randomWidth = rand.randomInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
			int randomHeight = rand.randomInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
			rooms[i] = new Room(randomWidth, randomHeight);

			boolean roomFree = true;
			int posX = rand.randomInt(MAP_BORDER, MAP_WIDTH - MAP_BORDER - rooms[i].getSizeX());
			int posY = rand.randomInt(MAP_BORDER, MAP_HEIGHT - MAP_BORDER - rooms[i].getSizeY());

			if (randomWidth % 2 == 0 || randomHeight % 2 == 0) {
				roomFree = false;
			}
			rooms[i].setPosition(posX, posY);

			//Check horizontally if desired place is free
			for (int y = -ROOM_BORDER; y < rooms[i].getSizeY() + ROOM_BORDER && roomFree; y++) {

				for (int x = -ROOM_BORDER; x < rooms[i].getSizeX() + ROOM_BORDER && roomFree; x++) {

					//prevents going outOfBounds
					//					if (rooms[i].getPosition().x + x > MAP_WIDTH || rooms[i].getPosition().y + y > MAP_HEIGHT)
					//						roomFree = false;
					//					if (rooms[i].getPosition().x + x < ROOM_BORDER || rooms[i].getPosition().y + y < ROOM_BORDER)
					//						roomFree = false;
					//					//
					if (mapBlocks[rooms[i].getPosition().x + x][rooms[i].getPosition().y + y].getFieldType() == FieldType.RESERVED) {
						roomFree = false;
						//System.out.println(roomFree);
					}
				}
			}

			for (int y = 0; y < rooms[i].getSizeY(); y++) {

				for (int x = 0; x < rooms[i].getSizeX() && roomFree; x++) {
					mapBlocks[rooms[i].getPosition().x + x][rooms[i].getPosition().y + y].setFieldType(FieldType.RESERVED);
				}
			}

			if (!roomFree) {
				i--;
			} else {
				mapBlocks[rooms[i].getCenter().x][rooms[i].getCenter().y].setFieldType(FieldType.CENTER);
			}

		}

		System.out.println("Used rooms: " + usedRooms);
	}

	public void removeCells() {

		for (int y = 1; y < MAP_HEIGHT - 1; y++) {

			for (int x = 1; x < MAP_WIDTH - 1; x++) {

			}

		}
	}

	public void removeSmallerSets() {
		if (getConnectedRooms().size() > 1) {
			//int keep = 0;

			//			for(HashSet<Room> hs:getConnectedRooms()){
			//				
			//			
			//			}
			System.out.println("Too many networks!!!");
		}

	}

	public void connectClosestRooms() {

		Point startPos = new Point();
		Point endPos = new Point();

		int deltaX = 0;
		int deltaY = 0;
		int absDeltaX = 0;
		int absDeltaY = 0;
		double delta = 0;
		int absDelta = 0;
		int buildFirst[] = new int[3];

		int first = Integer.MAX_VALUE;

		int rad = 3;

		for (int r0 = 0; r0 < usedRooms; r0++) {
			for (int r1 = 1; r1 != r0 && r1 < usedRooms; r1++) {

				deltaX = rooms[r1].getCenter().x - rooms[r0].getCenter().x;
				deltaY = rooms[r1].getCenter().y - rooms[r0].getCenter().y;
				absDeltaX = Math.abs(deltaX);
				absDeltaY = Math.abs(deltaY);

				delta = Math.sqrt((absDeltaX * absDeltaX) + (absDeltaY * absDeltaY));
				absDelta = (int) Math.round(Math.abs(delta));
				//System.out.println(absDelta);

				if (absDelta < first) {

					first = absDelta;

					buildFirst[0] = first;
					buildFirst[1] = r0;
					buildFirst[2] = r1;
				}
			}

			first: for (int s = 0; s < 4; s++) {
				for (int e = 0; e < 4; e++) {

					startPos.setLocation(randomShift(buildFirst[1], s));
					endPos.setLocation(randomShift(buildFirst[2], e));

					//System.out.println("tried: " + );

					BufferWayWrapper bwr = bufferWay(startPos, endPos, rad);
					if (bwr.succesful && bwr.length < 40) {
						reserveBufferedWay(rad);
						break first;
					}

				}
			}

			//TODO: prevent circles
			//TODO: remove smaller hashsets			

			first = Integer.MAX_VALUE;

		}

		for (int r0 = 0; r0 < usedRooms; r0++) {
			for (int r1 = 0; r1 != r0 && r1 < usedRooms; r1++) {

				//System.out.println("trying " + r0 + " with " + r1);

				deltaX = rooms[r1].getCenter().x - rooms[r0].getCenter().x;
				deltaY = rooms[r1].getCenter().y - rooms[r0].getCenter().y;
				absDeltaX = Math.abs(deltaX);
				absDeltaY = Math.abs(deltaY);

				delta = Math.sqrt((absDeltaX * absDeltaX) + (absDeltaY * absDeltaY));
				absDelta = (int) Math.round(Math.abs(delta));

				outerfor: for (int s = 0; s < 4; s++) {
					for (int e = 0; e < 4; e++) {

						startPos.setLocation(randomShift(r0, s));
						endPos.setLocation(randomShift(r1, e));

						//System.out.println("tried: " + );

						BufferWayWrapper bwr = bufferWay(startPos, endPos, rad);
						if (bwr.succesful && bwr.length < 50) {
							reserveBufferedWay(rad);
							break outerfor;
						}

					}
				}
			}

		}

	}

	public Point randomShift(int room, int direction) {

		Point pos = new Point(rooms[room].getCenter().x, rooms[room].getCenter().y);

		switch (direction) {

		case 1:
			pos.translate(0, (-1) * rooms[room].getSizeY() / 2 - 1);
			//System.out.println("Direction 4 for " + room + " :" + pos);			
			break;

		case 2:
			pos.translate(rooms[room].getSizeX() / 2 + 1, 0);
			//System.out.println("Direction 2 for " + room + " :" + pos);
			break;

		case 3:
			pos.translate(0, rooms[room].getSizeY() / 2 + 1);
			//System.out.println("Direction 3 for " + room + " :" + pos);
			break;

		case 4:
			pos.translate((-1) * rooms[room].getSizeX() / 2 - 1, 0);
			//System.out.println("Direction 4 for " + room + " :" + pos);

			break;

		}
		return pos;
	}

	public void placeDestructable() {
		removeDestrucable();
		Random random = new Random();
		for (int i = 0; i < usedRooms; i++) {

			int numberOfCrates = rand.randomInt(0, 3);

			for (int j = 0; j < numberOfCrates; j++) {
				int randomOffsetX = rand.randomInt(1, rooms[i].getSizeX() - 1);
				int randomOffsetY = rand.randomInt(1, rooms[i].getSizeY() - 1);

				int posX = rooms[i].getPosition().x + randomOffsetX;
				int posY = rooms[i].getPosition().y + randomOffsetY;

				if (mapBlocks[posX][posY].getFieldType() == FieldType.GROUND) {
					mapBlocks[posX][posY].setFieldType(FieldType.DESTRUCTABLE);
					DestroyableObject dO = null;
					if (random.nextBoolean())
						dO = new Crate(100, new Point(rooms[i].getPosition().x + randomOffsetX, rooms[i].getPosition().y + randomOffsetY), this);
					else if (random.nextBoolean())
						dO = new Vase(100, new Point(rooms[i].getPosition().x + randomOffsetX, rooms[i].getPosition().y + randomOffsetY), this);
					else
						dO = new Grave(100, new Point(rooms[i].getPosition().x + randomOffsetX, rooms[i].getPosition().y + randomOffsetY), this);
					dm.addObject(dO, (rooms[i].getPosition().x + randomOffsetX) * 32 + DungeonMap.TILE_SIZE / 2, (rooms[i].getPosition().y + randomOffsetY) * 32 + DungeonMap.TILE_SIZE / 2);
				}
			}

		}
	}

	public void removeDestrucable() {
		for (int i = 0; i < mapBlocks.length; i++) {
			for (int j = 0; j < mapBlocks[i].length; j++) {
				if (mapBlocks[i][j].getFieldType() == FieldType.DESTRUCTABLE) {
					mapBlocks[i][j].setFieldType(FieldType.GROUND);
					if (!dm.getObjectsAt(i * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE / 2, j * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE / 2, DestroyableObject.class).isEmpty()) {
						for (Object o : dm.getObjectsAt(i * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE / 2, j * DungeonMap.TILE_SIZE + DungeonMap.TILE_SIZE / 2, DestroyableObject.class)) {
							dm.removeObject((Actor) o);
						}
					}
				}
			}

		}
	}

	//Displays the world map in the console for debugging purposes.
	public void showMap() {

		for (int y = 0; y < MAP_HEIGHT; y++) {

			for (int x = 0; x < MAP_WIDTH; x++) {

				FieldType fType = mapBlocks[x][y].getFieldType();

				switch (fType) {

				case DESTRUCTABLE:
					System.out.print("x");
					break;
				case GROUND:
					System.out.print("#");
					break;
				case PICKUP:
					System.out.print("+");
					break;
				case WALL:
					System.out.print(".");
					break;

				case CELL:
					System.out.print("c");
					break;
				case CENTER:
					System.out.print("^");
					break;
				case RESERVED:
					System.out.print("*");

				}

			}
			System.out.println();
		}

		System.out.println("\n Map Seed: " + mapSeed);

	}

	//TODO: New methods

	private ArrayList<HashSet<Room>> getConnectedRooms() {
		ArrayList<HashSet<Room>> connected = new ArrayList<>();
		HashSet<Room> used = new HashSet<>();
		while (used.size() < usedRooms) {
			Room currRoom = null;
			for (Room r : rooms) {
				if (!used.contains(r)) {
					currRoom = r;
					break;
				}
			}
			HashSet<Room> currNet = new HashSet<>();
			currNet.add(currRoom);
			for (int i = 0; i < usedRooms; i++) {
				Room r = rooms[i];
				if (r != currRoom && areConnected(r.getCenter(), currRoom.getCenter())) {
					currNet.add(r);
					used.add(r);
				}
			}
			connected.add(currNet);
			used.add(currRoom);
		}
		return connected;
	}

	private boolean areConnected(Point p1, Point p2) {
		ArrayList<Node> closedList = new ArrayList<Node>();
		ArrayList<Node> openList = new ArrayList<Node>();

		openList.add(new Node(manhattanDistance(p1, p2.x, p2.y), 0, p2.x, p2.y, null));

		while (true) {
			if (openList.size() == 0) {
				return false;
			}
			Node closest = openList.get(0);
			for (Node node : openList) {
				if (node.cost < closest.cost) {
					closest = node;
				}
			}

			if (closest.x == p1.x && closest.y == p1.y) {
				return true;
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
				if (x < 0 || y < 0 || x >= mapBlocks.length || y >= mapBlocks[0].length)
					continue;
				Node currNode = new Node(manhattanDistance(p1, x, y) + closest.movementCost + 1, closest.movementCost + 1, x, y, closest);
				if ((mapBlocks[x][y].getFieldType() == FieldType.RESERVED || mapBlocks[x][y].getFieldType() == FieldType.CENTER) && !closedList.contains(currNode)) {
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
		}
	}

	private BufferWayWrapper bufferWay(Point start, Point end, int radius) {
		BufferWayWrapper bwr = new BufferWayWrapper();

		couldnNotFindWay = false;
		endPointBlocked = false;
		startPointBlocked = false;
		if (!canLeavePosition(end.x, end.y, radius)) {
			endPointBlocked = true;
			couldnNotFindWay = true;

			//System.out.println("Can't leave end point");

			lastWayBuffer = null;
			//System.out.println("Can't leave end point");

			return bwr;
		}
		if (!canLeavePosition(start.x, start.y, radius)) {
			startPointBlocked = true;

			couldnNotFindWay = true;

			//System.out.println("Can't leave start point");

			lastWayBuffer = null;
			//System.out.println("Can't leave start point");
			return bwr;

		}
		int shiftX = 0;
		int shiftY = 0;
		if (mapBlocks[start.x - 1][start.y].getFieldType() == FieldType.GROUND)
			shiftX = 1;
		if (mapBlocks[start.x + 1][start.y].getFieldType() == FieldType.GROUND)
			shiftX = -1;
		if (mapBlocks[start.x][start.y - 1].getFieldType() == FieldType.GROUND)
			shiftY = 1;
		if (mapBlocks[start.x][start.y + 1].getFieldType() == FieldType.GROUND)
			shiftY = -1;
		start.translate(shiftX * radius / 2, shiftY * radius / 2);
		ArrayList<Node> closedList = new ArrayList<Node>();
		ArrayList<Node> openList = new ArrayList<Node>();

		openList.add(new Node(manhattanDistance(start, end.x, end.y), 0, end.x, end.y, null));

		Node endNode = null;
		while (endNode == null && couldnNotFindWay == false) {
			endNode = step(closedList, openList, start, radius);
		}
		if (endNode != null) {
			lastWayBuffer = endNode;
			bwr.start = endNode;
			bwr.succesful = true;
			int count = 0;
			Node n = endNode;
			while ((n = n.prev) != null)
				count++;
			bwr.length = count;
			return bwr;
		}
		lastWayBuffer = null;
		return bwr;
	}

	private void reserveBufferedWay(int radius) {
		createWay(radius, FieldType.RESERVED, lastWayBuffer);
	}

	private void createWay(int radius, FieldType ft, Node startNode) {
		Node n = startNode;
		while (n.prev != null) {
			int x = n.x;
			int y = n.y;
			for (int i = x - radius / 2; i <= x + radius / 2; i++) {
				for (int j = y - radius / 2; j <= y + radius / 2; j++) {
					mapBlocks[i][j].setFieldType(ft);
				}
			}
			n = n.prev;
		}
		lastWayBuffer = null;
	}

	private boolean canLeavePosition(int x, int y, int radius) {
		int addX = 1;
		int addY = 1;
		for (int i = 0; i < 2; i++) {
			boolean obstructed = false;
			outer: for (int l = y; l != y + addY + addY * (radius / 2); l += addY) {
				for (int k = x - radius / 2; k <= x + radius / 2; k++) {
					if (mapBlocks[k][l].getFieldType() != FieldType.CELL) {
						obstructed = true;
						break outer;
					}
				}
			}
			if (!obstructed)
				return true;
			obstructed = false;
			outer: for (int k = x; k != x + addX + addX * (radius / 2); k += addX) {
				for (int l = y - radius / 2; l <= y + radius / 2; l++) {
					if (mapBlocks[k][l].getFieldType() != FieldType.CELL) {
						obstructed = true;
						break outer;
					}
				}
			}
			if (!obstructed)
				return true;
			addX = -1;
			addY = -1;
		}
		return false;
	}

	private Node step(ArrayList<Node> closedList, ArrayList<Node> openList, Point start, int radius) {
		if (openList.size() == 0) {
			couldnNotFindWay = true;
			//System.out.println("Couldn't find a way");
			//
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
		outerFor: for (int i = 0; i < 4; i++) {
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
			int subPathLength = MIN_SUB_PATHS_LENGTH;
			if (closest.prev != null && closest.x - closest.prev.x == addX && closest.y - closest.prev.y == addY)
				subPathLength = 1;
			subPathLengthFor: for (int spLength = 1; spLength <= subPathLength; spLength++) {
				x = closest.x + spLength * addX;
				y = closest.y + spLength * addY;
				boolean isTarget = (x == start.x && y == start.y);
				if (x < 0 || y < 0 || x >= mapBlocks.length || y >= mapBlocks[0].length)
					continue;
				if (addY != 0) {
					boolean valid = true;
					for (int l = y; l != y + addY + (isTarget ? 0 : addY * (radius / 2 + SPACE_NEXT_TO_PATH)); l += addY) {
						for (int k = x - radius / 2 - SPACE_NEXT_TO_PATH; k <= x + radius / 2 + SPACE_NEXT_TO_PATH; k++) {
							if (k < 0 || k >= mapBlocks.length)
								continue outerFor;
							if (l < 0 || l >= mapBlocks[0].length)
								continue outerFor;
							if (mapBlocks[k][l].getFieldType() != FieldType.CELL) {
								valid = false;
							}
						}
						if (x == start.x && l == start.y && valid)
							break subPathLengthFor;
					}
					if (!valid && !isTarget)
						continue outerFor;
				} else {
					boolean valid = true;
					for (int k = x; k != x + addX + (isTarget ? 0 : addX * (radius / 2 + SPACE_NEXT_TO_PATH)); k += addX) {
						for (int l = y - radius / 2 - SPACE_NEXT_TO_PATH; l <= y + radius / 2 + SPACE_NEXT_TO_PATH; l++) {
							if (l < 0 || l >= mapBlocks[0].length)
								continue outerFor;
							if (k < 0 || k >= mapBlocks.length)
								continue outerFor;
							if (mapBlocks[k][l].getFieldType() != FieldType.CELL) {
								valid = false;
							}
						}
						if (k == start.x && y == start.y && valid)
							break subPathLengthFor;
					}
					if (!valid && !isTarget)
						continue outerFor;
				}
			}
			Node prevNode = closest;
			for (int spLength = 1; spLength <= subPathLength; spLength++) {
				x = closest.x + spLength * addX;
				y = closest.y + spLength * addY;
				boolean isTarget = (x == start.x && y == start.y);
				Node currNode = new Node(manhattanDistance(start, x, y) + closest.movementCost + spLength * 1, closest.movementCost + spLength * 1, x, y, prevNode);
				prevNode = currNode;
				if (isTarget || spLength == subPathLength) {
					if (!closedList.contains(currNode)) {
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
				if (isTarget) {
					break;
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

	class BufferWayWrapper {
		Node start = null;
		int length = -1;
		boolean succesful = false;
	}

}
