package DungeonGeneration;

import greenfoot.World;

import java.awt.Point;

import objects.Crate;
import world.DungeonMap;

public class DungeonGenerator {

	public static final int ROOM_POOL = 10;
	public static final int MAP_WIDTH = 150;
	public static final int MAP_HEIGHT = 150;
	
	public static final int MAX_ROOM_SIZE= 20;
	public static final int MIN_ROOM_SIZE= 10;
	
	public static final int MAX_PATH_WIDTH= 6;
	public static final int MIN_PATH_WIDTH= 2;
	
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
	
	public DungeonGenerator(int seed)
	{
		mapSeed = seed;
		rand=new MegaRandom(mapSeed);
	}
	
	public MapField[][] getMap() {
		
		return mapBlocks;
		
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
			
			//System.out.println("Raum " + i +" -> Width: " + rooms[i].getSizeX() + ", Height: " + rooms[i].getSizeY());
		}
		
	}
	
	//Randomly places the rooms onto the map by setting the respective fields to fieldType GROUND
	public void placeRooms() {
		
		for(int i = 0; i < ROOM_POOL; i++) {
			
			rooms[i].setPosition(rand.randomInt(1, MAP_WIDTH - 1 - rooms[i].getSizeX()), rand.randomInt(1, MAP_HEIGHT - 1 - rooms[i].getSizeY()));
			
			for (int y=0; y<rooms[i].getSizeY(); y++ ){
				
				for (int x=0; x<rooms[i].getSizeX(); x++ ) {
					mapBlocks[rooms[i].getPosition().x + x][rooms[i].getPosition().y + y].setFieldType(FieldType.GROUND);
				}
			}
						
		}
		
		
		
		
	}
	
	public void placeDestructable(){
			
			for (int i = 0; i < ROOM_POOL; i++){
				int numberOfCrates = (rooms[i].getSizeX()+rooms[i].getSizeY())/10;
				
				// Debug: System.out.println(numberOfCrates);
				int stepsize = ((rooms[i].getSizeX()*rooms[i].getSizeY())/(numberOfCrates+1));
				int steps=0;
				
				for (int y = 0; y<rooms[i].getSizeY(); y++ ){
					
					for (int x=0; x<rooms[i].getSizeX(); x++){
						steps++;
						if (steps == stepsize){
									
							int randomOffset = rand.randomInt(0, rooms[i].getSizeX());
							
							mapBlocks[rooms[i].getPosition().x + randomOffset][rooms[i].getPosition().y + y].setFieldType(FieldType.DESTRUCTABLE);
							
							Crate crate = new Crate(100);
							dm.addObject(crate, (rooms[i].getPosition().x + randomOffset) * 32 + 32/2, (rooms[i].getPosition().y + y) * 32 + 32/2);
							
							steps = 0;
						}
						
					}
					steps++;
				}
			}
			
			
		}
	
	//Connects all rooms by building paths from room1 to room 2, from room2 to room3, from room3 to room4 and so on to make sure every room can be reached.
	//TODO: Fix single wall-tiles appearing without being connected to a wall
	public void buildPaths() {
		
		for(int r1 = 0; r1 < ROOM_POOL -1; r1++) {
			
			int buildStartOffsetX = rand.randomInt(0, (rooms[r1].getSizeX()-1));
			int buildStartOffsetY = rand.randomInt(0, (rooms[r1].getSizeY()-1));
			
			int buildDestinationOffsetX = rand.randomInt(0, (rooms[r1 +1].getSizeX()-1));
			int buildDestinationOffsetY = rand.randomInt(0, (rooms[r1 +1].getSizeY()-1));
			
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
					
					
					for(int i=0; i < randomPathWidth; i++) {
						if((buildPosition.y - randomPathWidth/2 + i) > 0 && (buildPosition.y - randomPathWidth/2 + i) < MAP_WIDTH)
							mapBlocks[buildPosition.x + (int)Math.signum(deltaX*-1)][buildPosition.y - randomPathWidth/2 + i].setFieldType(FieldType.GROUND);
					}
					
					//old system
					/*for(int i=1; i <= randomPathWidth/2; i++) {
						mapBlocks[buildPosition.x + (int)Math.signum(deltaX*-1)][buildPosition.y + i].isWalkable = true;
						mapBlocks[buildPosition.x + (int)Math.signum(deltaX*-1)][buildPosition.y - i].isWalkable = true;
					}
					*/
					
					//mapBlocks[buildPosition.x + (int)Math.signum(deltaX*-1)][buildPosition.y].isWalkable = true;
					buildPosition.translate((int)Math.signum(deltaX*-1), 0);
					absDeltaX -= 1;
				}
				else
				{
					
					for(int i=0; i < randomPathWidth; i++) {
						if((buildPosition.x - randomPathWidth/2 + i) > 0 && (buildPosition.x - randomPathWidth/2 + i) < MAP_HEIGHT)
							mapBlocks[buildPosition.x - randomPathWidth/2 + i][buildPosition.y + (int)Math.signum(deltaY*-1)].setFieldType(FieldType.GROUND);
					}
					
					//old system
					/*for(int i=1; i <= randomPathWidth/2; i++) {
						mapBlocks[buildPosition.x + i][buildPosition.y + (int)Math.signum(deltaY*-1)].isWalkable = true;
						mapBlocks[buildPosition.x - i][buildPosition.y + (int)Math.signum(deltaY*-1)].isWalkable = true;
					}
					*/
										
					//mapBlocks[buildPosition.x][buildPosition.y + (int)Math.signum(deltaY*-1)].isWalkable = true;
					buildPosition.translate(0, (int)Math.signum(deltaY*-1));
					absDeltaY -= 1;
				}
				
				buildStep++;
				
				//Added random Int for modulo devision, makes paths looks more random and neater imho
				if(buildStep % (rand.randomInt(3, 9)) == 0) {
					randomPathWidth = rand.randomInt(MIN_PATH_WIDTH, MAX_PATH_WIDTH);

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
			
				
				}
		
			}
				System.out.println();
		}
		
		System.out.println("\n Map Seed: " + mapSeed);
		
	}
}
