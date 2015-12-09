package DungeonGeneration;

//import greenfoot.World;

//Branching

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;


import objects.Crate;
import objects.DestroyableObject;
import objects.Grave;
import objects.Vase;
import world.DungeonMap;

public class DungeonGenerator {

	public static final int ROOM_POOL = 3;
	public static final int MAP_WIDTH = 120;
	public static final int MAP_HEIGHT = 120;
	
	public static final int MAX_ROOM_SIZE= 15;
	public static final int MIN_ROOM_SIZE= 5;
	
	public static final int MAX_PATH_WIDTH= 5;
	public static final int MIN_PATH_WIDTH= 3;
	
	private MapField[][] mapBlocks = new MapField[MAP_WIDTH][MAP_HEIGHT];
		
	private Room[] rooms = new Room[ROOM_POOL];
	
	private int mapSeed = 0;
	
	MegaRandom rand;
	MegaRandom randomSeed;
	
	private DungeonMap dm = null;
	
	boolean couldnNotFindWay=false;
	boolean startPointBlocked=false;
	boolean endPointBlocked=false;
	
	
	public DungeonGenerator(DungeonMap dm)
	{
		randomSeed = new MegaRandom();
		mapSeed = randomSeed.randomInt(0, Integer.MAX_VALUE-1);
		rand=new MegaRandom(mapSeed);
		
		this.dm = dm;
		initGen();
	}
	
	public DungeonGenerator(DungeonMap dm, int seed)
	{
		mapSeed = seed;
		rand=new MegaRandom(mapSeed);
		this.dm = dm;
		initGen();

	}
	
	public MapField[][] getMap() {
		
		return mapBlocks;
		
	}
	
	public int getSeed()
	{
		return mapSeed;
	}
	
	public void setMapFieldsType(int x, int y, FieldType fieldType) {
		mapBlocks[x][y].setFieldType(fieldType);
	}
	
	public void initGen(){
		clearMap();
		removeCells();
		generateRooms();
		calculateClosestNeighbour();
		showMap();
	}
	
	
	//Clears the map by setting every single field to fieldType wall
	public void clearMap() {
		
		for(int y = 0; y < MAP_HEIGHT; y++) {
			
			for(int x = 0; x < MAP_WIDTH; x++) {
				
				mapBlocks[x][y] = new MapField(FieldType.WALL); 
				
			}
		}
		
		for(int y = 1; y < MAP_HEIGHT-1; y++) {
					
					for(int x = 1; x < MAP_WIDTH-1; x++) {
						
						mapBlocks[x][y] = new MapField(FieldType.CELL); 
						
					}
				}
	}
	
	//Populates the "rooms" array with random sized rectangular rooms. 
	public void generateRooms() {
		
		int attempts = 0;
		
		for(int i = 0; i < ROOM_POOL && attempts < ROOM_POOL*20; i++) {
			
			attempts++;

			int randomWidth = rand.randomInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
			int randomHeight = rand.randomInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
			rooms[i] = new Room(randomWidth, randomHeight);
		
			boolean roomFree = true;
			int posX = rand.randomInt(3, MAP_WIDTH - 3 - rooms[i].getSizeX());
			int posY = rand.randomInt(3, MAP_HEIGHT - 3 - rooms[i].getSizeY());
			
			
			if (randomWidth % 2 == 0 || randomHeight % 2 == 0){
				roomFree = false;
			}
			rooms[i].setPosition(posX, posY);
			
			//Check horizontally if desired place is free
			for (int y=-1; y<rooms[i].getSizeY()+1 && roomFree; y++ ){ 
				
				for (int x=-1; x<rooms[i].getSizeX()+1 && roomFree; x++ ) {
					
					//prevents going outOfBounds
					if (rooms[i].getPosition().x + x > MAP_WIDTH || rooms[i].getPosition().y + y > MAP_HEIGHT)
						roomFree = false;
					if (rooms[i].getPosition().x + x < 2 || rooms[i].getPosition().y + y < 2)
						roomFree = false;
					//
					if (mapBlocks[rooms[i].getPosition().x + x ][rooms[i].getPosition().y + y].getFieldType() == FieldType.GROUND){
						roomFree = false;
						//System.out.println(roomFree);
					}	
				}
			}
			
		
				for (int y=0; y<rooms[i].getSizeY(); y++ ){
					
					for (int x=0; x<rooms[i].getSizeX() && roomFree; x++ ) {
						mapBlocks[rooms[i].getPosition().x + x ][rooms[i].getPosition().y + y].setFieldType(FieldType.GROUND);
					}
				}
				
				
			if (!roomFree){
				i--;
			} else {
				mapBlocks[rooms[i].getCenter().x  ][rooms[i].getCenter().y].setFieldType(FieldType.CENTER);
			}
		}
			
	}
	
	public void removeCells(){
		
		for (int y = 1; y < MAP_HEIGHT - 1; y++) {
					
			for(int x = 1; x < MAP_WIDTH - 1; x++) {
						
				if (mapBlocks[x][y].getFieldType() == FieldType.CELL){
						
					if (mapBlocks[x-1][y].isWalkable && mapBlocks[x+1][y].isWalkable){
						mapBlocks[x][y].setFieldType(FieldType.OPENING);
						}
					if (mapBlocks[x][y-1].isWalkable && mapBlocks[x][y+1].isWalkable){
						mapBlocks[x][y].setFieldType(FieldType.OPENING);
					}	
						
				}
			}
		}
			
			
	}
	
public void calculateClosestNeighbour(){
		
		int distances [][] = new int [ROOM_POOL][ROOM_POOL];
		int deltaX = 0;
		int deltaY = 0;
		int absDeltaX = 0;
		int absDeltaY = 0;
		double delta = 0;
		int absDelta = 0;
		int temp [] = new int [3]; //[distance, from, to]
		ArrayList<Integer> visited = new ArrayList<Integer>();
		
		
		for (int r1 = 0; r1 < ROOM_POOL-1; r1++){
			temp [0] = 300;
			for (int r2 = r1+1; r2 < ROOM_POOL; r2++){
				
				
				//calculate upper half distance matrix
				deltaX = rooms[r1].getCenter().x - rooms[r2].getCenter().x;
				deltaY = rooms[r1].getCenter().y - rooms[r2].getCenter().y;
				absDeltaX = Math.abs(deltaX);
				absDeltaY = Math.abs(deltaY);
				
				delta = Math.sqrt((absDeltaX*absDeltaX)+(absDeltaY*absDeltaY));
				absDelta = (int)Math.round(Math.abs(delta));
				
				//distances[r1].equals(visited.indexOf(r1));

				if (absDelta < temp[0] && visited.indexOf(r2) < 0 && visited.indexOf(r1) < 0 ){
					temp [0]= absDelta;
					temp [1]= r1;
					temp[2] = r2;
					visited.add(r1);
				}
				
				distances [r1][r2] = absDelta;
				//System.out.print(distances [r1][r2] + " ");
			}
			//System.out.print("| " + temp[0] + " from " + temp[1] + " to " +temp[2]);
			
			//System.out.println();
			buildPath(temp[1], temp[2], 0);
		}
		//System.out.println(visited);
	}
	
public void buildPath(int r1, int r2, int status){
	
	do {
		
	Point startPos = new Point(randomShift(rooms[r1]));
	Point endPos = new Point(randomShift(rooms[r2]));
	
	status = createWay(startPos, endPos ,3);

	} while (status != 0);
	
//	
//		//System.out.println("Status:" + status);
//		switch (status){
//			
//			case 1:		//Change start point
//				System.out.println("Change start point");
//				startPos = randomShift(rooms[r1]);
//				status = createWay(startPos, endPos ,3);
//				break;
//				
//			case 2:		//Change end point
//				System.out.println("Change end point");
//				endPos = randomShift(rooms[r2]);
//				status = createWay(startPos, endPos ,3);
//				break;
//				
//			
//			}
//		status = createWay(startPos, endPos ,3);
//		
//	
//		

	}
	

	
	public Point randomShift(Room room){
		
		int direction = rand.randomInt(1,4);
		Point pos = new Point(room.getCenter().x, room.getCenter().y);
		
		switch (direction){
		
		case 1:
			pos.translate(0, (-1)*room.getSizeY()/2 - 1);
			//System.out.println("Direction 4 for " + room + " :" + pos);			
			break;
		
		case 2:
			pos.translate(room.getSizeX()/2 + 2, 0);
			//System.out.println("Direction 2 for " + room + " :" + pos);
			break;
			
		case 3:
			pos.translate(0, room.getSizeY()/2 + 1);
			//System.out.println("Direction 3 for " + room + " :" + pos);
			break;
			
		case 4:
			pos.translate((-1)*room.getSizeX()/2 - 2, 0);
			//System.out.println("Direction 4 for " + room + " :" + pos);

			break;
		
		}
		return pos;
	}
	
	public void placeDestructable(){
		Random random=new Random(mapSeed);
		for (int i = 0; i < ROOM_POOL; i++){
			
			int numberOfCrates = rand.randomInt(0, 3);
			
				for (int j=0; j<numberOfCrates;j++){
					int randomOffsetX = rand.randomInt(0, rooms[i].getSizeX()-1);
					int randomOffsetY = rand.randomInt(0, rooms[i].getSizeY()-1);
					
					int posX = rooms[i].getPosition().x + randomOffsetX;
					int posY = rooms[i].getPosition().y + randomOffsetY;
										
					
					if (mapBlocks[posX][posY].getFieldType() == FieldType.GROUND){
						mapBlocks[posX][posY].setFieldType(FieldType.DESTRUCTABLE);
						DestroyableObject dO=null;
						if(random.nextBoolean())
							dO = new Crate(100, new Point(rooms[i].getPosition().x + randomOffsetX, rooms[i].getPosition().y + randomOffsetY), this);
						else if(random.nextBoolean())
							dO = new Vase(100, new Point(rooms[i].getPosition().x + randomOffsetX, rooms[i].getPosition().y + randomOffsetY), this);
						else 
							dO = new Grave(100, new Point(rooms[i].getPosition().x + randomOffsetX, rooms[i].getPosition().y + randomOffsetY), this);
						dm.addObject(dO, (rooms[i].getPosition().x + randomOffsetX) * 32 + DungeonMap.TILE_SIZE/2, (rooms[i].getPosition().y + randomOffsetY) * 32 + DungeonMap.TILE_SIZE/2);
					}
				}
				
		}					
	}

	//Displays the world map in the console for debugging purposes.
	public void showMap() {
			
		for(int y = 0; y < MAP_HEIGHT; y++) {
		
			for(int x = 0; x < MAP_WIDTH; x++) {
			
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
				case OPENING:
					System.out.print("o");
					break;
				case CELL:
					System.out.print("c");
					break;
				case CENTER:
					System.out.print("^");

				}
		
			}
				System.out.println();
		}
		
		System.out.println("\n Map Seed: " + mapSeed);
		
	}
	
	private int createWay(Point start, Point end, int radius)
	{
		couldnNotFindWay=false;
		endPointBlocked=false;
		startPointBlocked=false;
		
		if(mapBlocks[end.x][end.y].getFieldType()!=FieldType.CELL)
		{
			endPointBlocked=true;
			couldnNotFindWay=true;
			System.out.println("End point is not a cell");
			
			return 2;
		}
		if(mapBlocks[start.x][start.y].getFieldType()!=FieldType.CELL)
		{
			startPointBlocked=true;
			couldnNotFindWay=true;
			System.out.println("Start point is not a cell");
			return 1;
		}
		
		ArrayList<Node>closedList=new ArrayList<Node>();
		ArrayList<Node>openList=new ArrayList<Node>();

		openList.add(new Node(manhattanDistance(start, end.x, end.y),0, end.x, end.y, null));

		Node endNode=null;
		while(endNode==null&&couldnNotFindWay==false)
		{
			endNode=step(closedList, openList, start,radius);
		}
		if(endNode!=null)
		{
			Node n=endNode;
			while(n.prev!=null)
			{
				int x=n.x;
				int y=n.y;
				for(int i=x-radius/2;i<=x+radius/2;i++)
				{
					for(int j=y-radius/2;j<=y+radius/2;j++)
					{
						mapBlocks[i][j].setFieldType(FieldType.GROUND);
					}
				}
				n=n.prev;
			}
		}
		return 0;
	}
	

	private Node step(ArrayList<Node>closedList, ArrayList<Node>openList, Point start, int radius)
	{
		if(openList.size()==0)
		{
			couldnNotFindWay=true;
			System.out.println("Couldn't find a way");
			return null;
		}
		Node closest=openList.get(0);
		for(Node node:openList)
		{
			if(node.cost<closest.cost)
			{
				closest=node;
			}
		}

		if(closest.x==start.x&&closest.y==start.y)
		{
			return closest;
		}
		int x=-1,y=-1;
		int addX=1;
		int addY=0;
		outerFor:
		for(int i=0;i<4;i++)
		{
			if(i==1||i==3)
			{
				addX*=-1;
				addY*=-1;
			}
			if(i==2)
			{
				addX=0;
				addY=1;
			}
			//1. Loop: 1 , 0
			//2. Loop: -1, 0
			//3. Loop: 0, 1l
			//4. Loop: 0, -1
			x=closest.x+addX;	
			y=closest.y+addY;
			if(x<0||y<0||x>=mapBlocks.length||y>=mapBlocks[0].length)
				continue;
			Node currNode=new Node(manhattanDistance(start, x,y)+closest.movementCost+1,closest.movementCost+1, x,y, closest);
			if(addY!=0)
			{
				for(int k=x-radius/2;k<=x+radius/2;k++)
				{
					for(int l=y;l!=y+addY*(radius/2)+addY;l+=addY)
					{
						if(mapBlocks[k][l].getFieldType()!=FieldType.CELL)
							continue outerFor;
					}
				}
			}
			else
			{
				for(int k=x;k!=x+addX*(radius/2)+addX;k+=addX)
				{
					for(int l=y-radius/2;l<=y+radius/2;l++)
					{
						if(mapBlocks[k][l].getFieldType()!=FieldType.CELL)
							continue outerFor;
					}
				}
			}
			if(!closedList.contains(currNode))
			{
				int indx=openList.indexOf(currNode);
				if(indx==-1)
				{
					openList.add(currNode);
				}
				else
				{
					Node inList=openList.get(indx);
					if(currNode.cost<=inList.cost)
					{
						inList.cost=currNode.cost;
						inList.prev=currNode.prev;
						inList.movementCost=currNode.movementCost;
					}
				}
			}
		}

		openList.remove(closest);
		closedList.add(closest);

		return null;
	}

	private double manhattanDistance(Point start, int x, int y)
	{
		return Math.abs(start.x-x)+Math.abs(start.y-y);
	}

	class Node
	{
		double cost;
		double movementCost;
		int x;
		int y;
		Node prev;

		public Node(double cost,double movementCost, int x, int y, Node prev)
		{
			this.movementCost=movementCost;
			this.cost=cost;
			this.x=x;
			this.y=y;
			this.prev=prev;
		}

		public Node(int x, int y)
		{
			this.x=x;
			this.y=y;
		}

		@Override
		public boolean equals(Object v)
		{
			if(v instanceof Node)
			{
				Node n=(Node)v;
				return n.x==x&&n.y==y;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return x*100000+y;
		}
	}
	
}
