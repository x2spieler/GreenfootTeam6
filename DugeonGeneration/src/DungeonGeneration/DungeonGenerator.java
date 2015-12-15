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

	public static final int ROOM_POOL =20;
	public static final int MAP_WIDTH = 150;
	public static final int MAP_HEIGHT = 150;

	public static final int MAX_ROOM_SIZE= 15;
	public static final int MIN_ROOM_SIZE= 10;
	public static final int ROOM_BORDER = 5;

	public static final int MAX_PATH_WIDTH= 5;
	public static final int MIN_PATH_WIDTH= 3;

<<<<<<< HEAD

	public static final int MAP_BORDER = 15;

	

=======
>>>>>>> branch 'DifferentDungeonGenerationApproach' of https://github.com/x2spieler/GreenfootTeam6.git
	private final int SPACE_NEXT_TO_PATH=3;
	private final int MIN_SUB_PATHS_LENGTH=3;


	private MapField[][] mapBlocks = new MapField[MAP_WIDTH][MAP_HEIGHT];

	private Room[] rooms = new Room[ROOM_POOL];
	
	ArrayList<Integer> visited = new ArrayList<Integer>();
	ArrayList<Integer> ignored = new ArrayList<Integer>();


	private int mapSeed = 0;

	MegaRandom rand;
	MegaRandom randomSeed;

	private DungeonMap dm = null;

	boolean couldnNotFindWay=false;
	boolean startPointBlocked=false;
	boolean endPointBlocked=false;

	int unreachableRooms = 0;

	
	private Node lastWayBuffer=null;



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
		removeUnusedCells();
		removeUnreachable();
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
			int posX = rand.randomInt(MAP_BORDER, MAP_WIDTH - MAP_BORDER - rooms[i].getSizeX());
			int posY = rand.randomInt(MAP_BORDER, MAP_HEIGHT - MAP_BORDER - rooms[i].getSizeY());


			if (randomWidth % 2 == 0 || randomHeight % 2 == 0){
				roomFree = false;
			}
			rooms[i].setPosition(posX, posY);

			//Check horizontally if desired place is free
			for (int y=-ROOM_BORDER; y<rooms[i].getSizeY()+ROOM_BORDER && roomFree; y++ ){ 

				for (int x=-ROOM_BORDER; x<rooms[i].getSizeX()+ROOM_BORDER && roomFree; x++ ) {

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
	//TODO: r+1 not always correct
	//TODO: check if room is near to a path, then keep it
	
	
	
	public void removeUnreachable(){
		//boolean found = false;
		int sizeX = 0;
		int sizeY = 0;
		Point pos = new Point();
		
		for (int r = 0; r < ROOM_POOL; r++){
			
			if ((!rooms[r].hasEndPoint() && !rooms[r].hasStartingPoint())){
				//found = true;
				System.out.println(r);
				sizeX = rooms[r].getSizeX();
				sizeY = rooms[r].getSizeY();
				pos.setLocation(rooms[r].getPosition());
				
				for (int y = 0; y< sizeY; y++){
					for (int x = 0; x< sizeX; x++){
						
						mapBlocks[x+pos.x][y+pos.y].setFieldType(FieldType.CENTER);
						
					}

				}
				
				System.out.println("Removed room: " + (r));
				
			}
		}
	}
			
		
		//TODO: Remove thin walls
		

	
		
			
	
	public void calculateClosestNeighbour(){

		
		int deltaX = 0;
		int deltaY = 0;
		int absDeltaX = 0;
		int absDeltaY = 0;
		double delta = 0;
		int absDelta = 0;
		int temp [] = new int [3]; //[distance, from, to]
		int status = 0;
		boolean found = false;
		boolean build = true;
		//int status = 0;
		int counter=0;

		

		for (int r0 = 0; r0 < ROOM_POOL; r0++){
			
			temp [0] = Integer.MAX_VALUE;
			int rad=rand.randomInt(3, 5);

			
			for (int r1 = 0; r1 != r0 && r1 < ROOM_POOL; r1++){


				//calculate closest neighbor from r0
				deltaX = rooms[r0].getCenter().x - rooms[r1].getCenter().x;
				deltaY = rooms[r0].getCenter().y - rooms[r1].getCenter().y;
				absDeltaX = Math.abs(deltaX);
				absDeltaY = Math.abs(deltaY);

				delta = Math.sqrt((absDeltaX*absDeltaX)+(absDeltaY*absDeltaY));
				absDelta = (int)Math.round(Math.abs(delta));
				
				if (absDelta < temp[0] && !ignored.contains(r1) && canBuildPath(r0, r1, rad)){
						temp[0] = absDelta;
						temp[1] = r0;
						temp[2] = r1;
						
						
				}
					ignored.add(temp[1]);
					//ignored.add(temp[2]);	
					
					if (canBuildPath(r0, r1, rad)){
						createBufferedWay(rad);
					}
			}
			
			
		 
			System.out.println("build path from " + temp[1] + " to " + temp[2] + "!" );
			
			}
		
	}
				

	public boolean canBuildPath(int r0, int r1, int rad){

		boolean found = false;
		int counter=0;
		
		do {
			Point startPos = new Point(randomShift(rooms[r0]));
			Point endPos = new Point(randomShift(rooms[r1]));

			rooms[r0].setStartingPoint(startPos);
			rooms[r1].setEndPoint(endPos);
			
			
			
			
			if(bufferWay(startPos, endPos ,rad)){
				found = true;
			}

			if(counter++==32)
			{
				//System.out.println("FORCE BREAK");
				return false;
			}
				
			} while (!found);	

			return true;
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
			pos.translate(room.getSizeX()/2 + 1, 0);
			//System.out.println("Direction 2 for " + room + " :" + pos);
			break;

		case 3:
			pos.translate(0, room.getSizeY()/2 + 1);
			//System.out.println("Direction 3 for " + room + " :" + pos);
			break;

		case 4:
			pos.translate((-1)*room.getSizeX()/2 - 1, 0);
			//System.out.println("Direction 4 for " + room + " :" + pos);

			break;

		}
		return pos;
	}

	public void removeUnusedCells(){
		for (int y = 0; y < MAP_HEIGHT; y++ ){
			for (int x = 0; x < MAP_WIDTH; x++){
				if (mapBlocks[x][y].getFieldType() == FieldType.CELL){
					mapBlocks[x][y].setFieldType(FieldType.WALL);
				}
			}
		}
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
	

	private boolean bufferWay(Point start, Point end, int radius)
	{
		couldnNotFindWay=false;
		endPointBlocked=false;
		startPointBlocked=false;
		if(!canLeavePosition(end.x, end.y, radius))
		{
			endPointBlocked=true;
			couldnNotFindWay=true;

			//System.out.println("Can't leave end point");

			lastWayBuffer=null;
			//System.out.println("Can't leave end point");


			return false;
		}
		if(!canLeavePosition(start.x, start.y, radius))
		{
			startPointBlocked=true;
			
			couldnNotFindWay=true;

			//System.out.println("Can't leave start point");

			lastWayBuffer=null;
			//System.out.println("Can't leave start point");
			return false;

		}
		int shiftX=0;
		int shiftY=0;
		if(mapBlocks[start.x-1][start.y].getFieldType()==FieldType.GROUND)
			shiftX=1;
		if(mapBlocks[start.x+1][start.y].getFieldType()==FieldType.GROUND)
			shiftX=-1;
		if(mapBlocks[start.x][start.y-1].getFieldType()==FieldType.GROUND)
			shiftY=1;
		if(mapBlocks[start.x][start.y+1].getFieldType()==FieldType.GROUND)
			shiftY=-1;
		start.translate(shiftX*radius/2,shiftY*radius/2);
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
			lastWayBuffer=endNode;
			return true;
		}
		return false;
	}
	
	private void createBufferedWay(int radius)
	{
		Node n=lastWayBuffer;
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
		lastWayBuffer=null;
	}

	private boolean canLeavePosition(int x, int y, int radius)
	{
		int addX=1;
		int addY=1;
		for(int i=0;i<2;i++)
		{
			boolean obstructed=false;
			outer:
				for(int l=y;l!=y+addY+addY*(radius/2);l+=addY)
				{
					for(int k=x-radius/2;k<=x+radius/2;k++)
					{
						if(mapBlocks[k][l].getFieldType()!=FieldType.CELL)
						{	
							obstructed=true;
							break outer;
						}
					}
				}
			if(!obstructed)
				return true;
			obstructed=false;
			outer:
				for(int k=x;k!=x+addX+addX*(radius/2);k+=addX)
				{
					for(int l=y-radius/2;l<=y+radius/2;l++)
					{
						if(mapBlocks[k][l].getFieldType()!=FieldType.CELL)
						{	
							obstructed=true;
							break outer;
						}
					}
				}
			if(!obstructed)
				return true;
			addX=-1;
			addY=-1;
		}
		return false;
	}

	private Node step(ArrayList<Node>closedList, ArrayList<Node>openList, Point start, int radius)
	{
		if(openList.size()==0)
		{
			couldnNotFindWay=true;
			//System.out.println("Couldn't find a way");
			//
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
				subPathLengthFor:
				for(int spLength=1;spLength<=MIN_SUB_PATHS_LENGTH;spLength++)
				{
					x=closest.x+spLength*addX;	
					y=closest.y+spLength*addY;
					boolean isTarget=(x==start.x&&y==start.y);
					if(x<0||y<0||x>=mapBlocks.length||y>=mapBlocks[0].length)
						continue;
					if(addY!=0)
					{
						boolean valid=true;
						for(int l=y;l!=y+addY+(isTarget ? 0 : addY*(radius/2+SPACE_NEXT_TO_PATH));l+=addY)
						{
							for(int k=x-radius/2-SPACE_NEXT_TO_PATH;k<=x+radius/2+SPACE_NEXT_TO_PATH;k++)
							{
								if(k<0||k>=mapBlocks.length)
									continue outerFor;
								if(l<0||l>=mapBlocks[0].length)
									continue outerFor;
								if(mapBlocks[k][l].getFieldType()!=FieldType.CELL)
								{	
									valid=false;
								}
							}
							if(x==start.x&&l==start.y&&valid)
								break subPathLengthFor;
						}
						if(!valid&&!isTarget)
							continue outerFor;
					}
					else
					{
						boolean valid=true;
						for(int k=x;k!=x+addX+(isTarget ? 0 : addX*(radius/2+SPACE_NEXT_TO_PATH));k+=addX)
						{
							for(int l=y-radius/2-SPACE_NEXT_TO_PATH;l<=y+radius/2+SPACE_NEXT_TO_PATH;l++)
							{
								if(l<0||l>=mapBlocks[0].length)
									continue outerFor;
								if(k<0||k>=mapBlocks.length)
									continue outerFor;
								if(mapBlocks[k][l].getFieldType()!=FieldType.CELL)
								{	
									valid=false;
								}
							}
							if(k==start.x&&y==start.y&&valid)
								break subPathLengthFor;
						}
						if(!valid&&!isTarget)
							continue outerFor;
					}
				}
				Node prevNode=closest;
				for(int spLength=1;spLength<=MIN_SUB_PATHS_LENGTH;spLength++)
				{
					x=closest.x+spLength*addX;	
					y=closest.y+spLength*addY;
					boolean isTarget=(x==start.x&&y==start.y);
					Node currNode=new Node(manhattanDistance(start, x,y)+closest.movementCost+spLength*1,closest.movementCost+spLength*1, x,y, prevNode);
					prevNode=currNode;
					if(isTarget||spLength==MIN_SUB_PATHS_LENGTH)
					{
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
					if(isTarget)
					{
						break;
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
