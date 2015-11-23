package DungeonGeneration;

//import greenfoot.World;

import java.awt.Point;

import objects.Crate;
import world.DungeonMap;

public class DungeonGenerator {

	public static final int ROOM_POOL = 10;
	public static final int MAP_WIDTH = 150;
	public static final int MAP_HEIGHT = 150;
	
	public static final int MAX_ROOM_SIZE= 20;
	public static final int MIN_ROOM_SIZE= 10;
	
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
	}
	
	public DungeonGenerator(DungeonMap dm, int seed)
	{
		mapSeed = seed;
		rand=new MegaRandom(mapSeed);
		this.dm = dm;
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
	
	
	
	
	//Clears the map by setting every single field to fieldType wall
	public void clearMap() {
		
		for(int y = 0; y < MAP_HEIGHT; y++) {
			
			for(int x = 0; x < MAP_WIDTH; x++) {
				
				mapBlocks[x][y] = new MapField(FieldType.WALL); 
				
			}
		}
		
	}
	
	//Populates the "rooms" array with random sized rectangular rooms. 
	public void generateRooms() {
				
		for(int i = 0; i < ROOM_POOL; i++) {
			

			int randomWidth = rand.randomInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
			int randomHeight = rand.randomInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
			rooms[i] = new Room(randomWidth, randomHeight);
		
			boolean roomFree = true;
			int posX = rand.randomInt(3, MAP_WIDTH - 3 - rooms[i].getSizeX());
			int posY = rand.randomInt(3, MAP_HEIGHT - 3 - rooms[i].getSizeY());
			
			rooms[i].setPosition(posX, posY);
			
			//Check horizontally if desired place is free
			for (int y=-2; y<rooms[i].getSizeY()+3&& roomFree; y++ ){
				
				for (int x=-2; x<rooms[i].getSizeX()+3 && roomFree; x++ ) {
					
					//prevents going outOfBounds
					if (rooms[i].getPosition().x + x > MAP_WIDTH || rooms[i].getPosition().y + y > MAP_HEIGHT)
						roomFree = false;
					if (rooms[i].getPosition().x + x < 2 || rooms[i].getPosition().y + y < 2)
						roomFree = false;
					//
					if (mapBlocks[rooms[i].getPosition().x + x ][rooms[i].getPosition().y + y].getFieldType() == FieldType.GROUND){
						roomFree = false;
						System.out.println(roomFree);
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
			}
		}
			
	}
	
	
	
	
	
	public void placeDestructable(){
		
		for (int i = 0; i < ROOM_POOL; i++){
			
			int numberOfCrates = rand.randomInt(0, 3);
			
				for (int j=0; j<numberOfCrates;j++){
					int randomOffsetX = rand.randomInt(0, rooms[i].getSizeX()-1);
					int randomOffsetY = rand.randomInt(0, rooms[i].getSizeY()-1);
					
					int posX = rooms[i].getPosition().x + randomOffsetX;
					int posY = rooms[i].getPosition().y + randomOffsetY;
										
					
					if (mapBlocks[posX][posY].getFieldType() == FieldType.GROUND){
						mapBlocks[posX][posY].setFieldType(FieldType.DESTRUCTABLE);
						Crate crate = new Crate(100, new Point(rooms[i].getPosition().x + randomOffsetX, rooms[i].getPosition().y + randomOffsetY), this);
						dm.addObject(crate, (rooms[i].getPosition().x + randomOffsetX) * 32 + DungeonMap.TILE_SIZE/2, (rooms[i].getPosition().y + randomOffsetY) * 32 + DungeonMap.TILE_SIZE/2);
					}
				}
				
		}					
	}
				
		
	//Connects all rooms by building paths from room1 to room 2, from room2 to room3, from room3 to room4 and so on to make sure every room can be reached.
	public void buildPaths() {
		
		for(int r1 = 0; r1 < ROOM_POOL -1; r1++) {
			
			int buildStartOffsetX = rand.randomInt(4, (rooms[r1].getSizeX()-4));
			int buildStartOffsetY = rand.randomInt(4, (rooms[r1].getSizeY()-4));
			
			int buildDestinationOffsetX = rand.randomInt(4, (rooms[r1 +1].getSizeX()-4));
			int buildDestinationOffsetY = rand.randomInt(4, (rooms[r1 +1].getSizeY()-4));
			
			Point buildPosition = new Point(rooms[r1].getPosition().x + buildStartOffsetX,rooms[r1].getPosition().y + buildStartOffsetY);
			Point buildDestination = new Point(rooms[r1 +1].getPosition().x + buildDestinationOffsetX,rooms[r1 +1].getPosition().y + buildDestinationOffsetY);
			
			int deltaX = buildPosition.x - buildDestination.x;
			int deltaY = buildPosition.y - buildDestination.y;
			
			int absDeltaX = Math.abs(deltaX);
			int absDeltaY = Math.abs(deltaY);
		
			int buildStep = 0;
			int randomPathWidth = rand.randomInt(MIN_PATH_WIDTH, MAX_PATH_WIDTH);
			
			while(absDeltaX > 0 || absDeltaY > 0) {
			
				int minRandom = 1;
				int maxRandom = 2;
				
				if(absDeltaX <= 0 )
					minRandom = 2;
				if(absDeltaY <= 0)
					maxRandom = 1;
				
				int randomChance = rand.randomInt(minRandom, maxRandom);
								
				if(randomChance == 1) {
					
					for(int b=0;b<4;b++){
						for(int i=0; i < randomPathWidth; i++) {
							int upperBound = buildPosition.y - randomPathWidth/2;
							if((upperBound + i) > 1 && (upperBound + i) < MAP_HEIGHT -3 )
								
								mapBlocks[buildPosition.x + (int)Math.signum(deltaX*-1)][upperBound + i].setFieldType(FieldType.GROUND);
						}
						
						buildPosition.translate((int)Math.signum(deltaX*-1), 0);
						absDeltaX -= 1;
					}
				}
				else
				{
					for(int b=0;b<4;b++){
						for(int i=0; i < randomPathWidth; i++) {
							int leftBound = buildPosition.x - randomPathWidth/2;
							if((leftBound + i) > 1 && (leftBound + i) < MAP_WIDTH -3)
								mapBlocks[leftBound + i][buildPosition.y + (int)Math.signum(deltaY*-1)].setFieldType(FieldType.GROUND);
						}
						
						buildPosition.translate(0, (int)Math.signum(deltaY*-1));
						absDeltaY -= 1;
					}
				}
				
				buildStep++;
				
				//Added random Int for modulo devision, makes paths looks more random and neater 
				if(buildStep % (rand.randomInt(3, 5)) == 0) {
					randomPathWidth = rand.randomInt(MIN_PATH_WIDTH, MAX_PATH_WIDTH);

				}
			}
		}
				
					
}
				

	
	public void cleanUp() {
		
		
		//Clean up single wall tiles
		boolean found;
		
		do{
			found = false;
			
			for (int y= 1; y < MAP_HEIGHT-3; y++){
				
				for (int x= 1; x < MAP_WIDTH-3; x++) {
				
					int freeSides =0;
					
					if ((mapBlocks[x][y].getFieldType() == FieldType.WALL)){
						if (mapBlocks[x-1][y].getFieldType() == FieldType.GROUND)
							freeSides += 1;
						if (mapBlocks[x+1][y].getFieldType() == FieldType.GROUND)
							freeSides += 1;
						if (mapBlocks[x][y-1].getFieldType() == FieldType.GROUND)
							freeSides += 1;
						if (mapBlocks[x][y+1].getFieldType() == FieldType.GROUND)
							freeSides += 1;
					}
					
					if (freeSides>=3){
						found = true;
						System.out.println("removed tile at " + x + " " + y + " " + found);
						
						mapBlocks[x][y].setFieldType(FieldType.GROUND);
					
					} 
					
					
				} 
			}
			
		} while (found);
		
	}
	
	public void removeShapes(){
		
		 // Removes illegal shapes that cause rendering problems.
		
		boolean found;
		
		do {
			found = false;
			

			for (int y= 1; y < MAP_HEIGHT/3; y++){
				
				for (int x= 1; x < MAP_WIDTH/3; x++) {
			
				/*	for (int i=0;i<3;i++){
						Scanner checks for Shape
						rotates scanner
					}
					move scanner 3x ->
					
			
					*/
				}
			}
			
		} while (found);
		 
		
	}
	

	
	public void thickenWalls(){
		
		boolean build;
		
		do {
			build = false;
			
			for (int y= 1; y < MAP_HEIGHT-1; y++){
				
				for (int x= 1; x < MAP_WIDTH-1; x++) {
					//checks if wall has a ground tile above AND below, places wall tile to the left if true
					if (mapBlocks[x][y].getFieldType() == FieldType.WALL && mapBlocks[x][y-1].getFieldType() == FieldType.GROUND && mapBlocks[x][y+1].getFieldType() == FieldType.GROUND){
						mapBlocks[x][y-1].setFieldType(FieldType.WALL);
						//System.out.println("placed wall at  " + x + " " + y);
						build = true;
					}
					if (mapBlocks[x][y].getFieldType() == FieldType.WALL && mapBlocks[x-1][y].getFieldType() == FieldType.GROUND && mapBlocks[x+1][y].getFieldType() == FieldType.GROUND){
						mapBlocks[x-1][y].setFieldType(FieldType.WALL);
						//System.out.println("placed wall at  " + x + " " + y);
						build = true;

					}
					
				}
			}
		} while (build);
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
			
				
				}
		
			}
				System.out.println();
		}
		
		System.out.println("\n Map Seed: " + mapSeed);
		
	}
}
