package DungeonGeneration;

//import greenfoot.World;

//Branching

import java.awt.Point;

import java.util.Random;
import objects.Crate;
import objects.DestroyableObject;
import objects.Grave;
import objects.Vase;
import world.DungeonMap;

public class DungeonGenerator {

	public static final int ROOM_POOL = 10;
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
		generateRooms();
		removeCells();
		showMap();
		calculatePaths();
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
	
	public void calculatePaths(){
		
		int distances [][] = new int [ROOM_POOL][ROOM_POOL];
		int deltaX = 0;
		int deltaY = 0;
		int absDeltaX = 0;
		int absDeltaY = 0;
		double delta = 0;
		int absDelta = 0;
		int temp = 0;
		
		
		for (int r1 = 0; r1 < ROOM_POOL; r1++){
			for (int r2 = r1+1; r2 < ROOM_POOL; r2++){

				//calculate upper half distance matrix
				deltaX = rooms[r1].getCenter().x - rooms[r2].getCenter().x;
				deltaY = rooms[r1].getCenter().y - rooms[r2].getCenter().y;
				absDeltaX = Math.abs(deltaX);
				absDeltaY = Math.abs(deltaY);
				
				delta = Math.sqrt((absDeltaX*absDeltaX)+(absDeltaY*absDeltaY));
				absDelta = (int)Math.round(Math.abs(delta));

				distances [r1][r2] = absDelta;
				System.out.print(distances [r1][r2] + " ");
			}
			System.out.println();
			
		}
	}
	
	public void buildPaths(){
		
		for(int r1 = 0; r1 < ROOM_POOL -1; r1++) {
			
		}
	}
	
	
	
	
//	public void buildPaths() {
//			
//			for(int r1 = 0; r1 < ROOM_POOL -1; r1++) {
//				
//				int buildStartOffsetX = rand.randomInt(4, (rooms[r1].getSizeX()-4));
//				int buildStartOffsetY = rand.randomInt(4, (rooms[r1].getSizeY()-4));
//				
//				int buildDestinationOffsetX = rand.randomInt(4, (rooms[r1 +1].getSizeX()-4));
//				int buildDestinationOffsetY = rand.randomInt(4, (rooms[r1 +1].getSizeY()-4));
//				
//				Point buildPosition = new Point(rooms[r1].getPosition().x + buildStartOffsetX,rooms[r1].getPosition().y + buildStartOffsetY);
//				Point buildDestination = new Point(rooms[r1 +1].getPosition().x + buildDestinationOffsetX,rooms[r1 +1].getPosition().y + buildDestinationOffsetY);
//				
//				int deltaX = buildPosition.x - buildDestination.x;
//				int deltaY = buildPosition.y - buildDestination.y;
//				
//				int absDeltaX = Math.abs(deltaX);
//				int absDeltaY = Math.abs(deltaY);
//			
//				int buildStep = 0;
//				int randomPathWidth = rand.randomInt(MIN_PATH_WIDTH, MAX_PATH_WIDTH);
//				
//				while(absDeltaX > 0 || absDeltaY > 0) {
//				
//					int minRandom = 1;
//					int maxRandom = 2;
//					
//					if(absDeltaX <= 0 )
//						minRandom = 2;
//					if(absDeltaY <= 0)
//						maxRandom = 1;
//					
//					int randomChance = rand.randomInt(minRandom, maxRandom);
//									
//					if(randomChance == 1) {
//						
//						for(int b=0;b<4;b++){
//							for(int i=0; i < randomPathWidth; i++) {
//								int upperBound = buildPosition.y - randomPathWidth/2;
//								if((upperBound + i) > 1 && (upperBound + i) < MAP_HEIGHT -3 )
//									
//									mapBlocks[buildPosition.x + (int)Math.signum(deltaX*-1)][upperBound + i].setFieldType(FieldType.GROUND);
//							}
//							
//							buildPosition.translate((int)Math.signum(deltaX*-1), 0);
//							absDeltaX -= 1;
//						}
//					}
//					else
//					{
//						for(int b=0;b<4;b++){
//							for(int i=0; i < randomPathWidth; i++) {
//								int leftBound = buildPosition.x - randomPathWidth/2;
//								if((leftBound + i) > 1 && (leftBound + i) < MAP_WIDTH -3)
//									mapBlocks[leftBound + i][buildPosition.y + (int)Math.signum(deltaY*-1)].setFieldType(FieldType.GROUND);
//							}
//							
//							buildPosition.translate(0, (int)Math.signum(deltaY*-1));
//							absDeltaY -= 1;
//						}
//					}
//					
//					buildStep++;
//					
//					//Added random Int for modulo devision, makes paths looks more random and neater 
//					if(buildStep % (rand.randomInt(3, 5)) == 0) {
//						randomPathWidth = rand.randomInt(MIN_PATH_WIDTH, MAX_PATH_WIDTH);
//	
//					}
//				}
//			}
//					
//						
//	}
	
	
	
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
}
