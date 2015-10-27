package DungeonGeneration;

import java.awt.Point;

public class DungeonGenerator {

	public static final int ROOM_POOL = 10;
	public static final int MAP_WIDTH = 80;
	public static final int MAP_HEIGHT = 80;
	
	public static final int MAX_ROOM_SIZE= 10;
	public static final int MIN_ROOM_SIZE= 10;
	
	private MapField[][] mapBlocks = new MapField[MAP_WIDTH][MAP_HEIGHT];
		
	private Room[] rooms = new Room[ROOM_POOL];
	
	MegaRandom rand;
	
	public DungeonGenerator()
	{
		rand=new MegaRandom();
	}
	
	//Clears the map by setting every single field to an empty (walkable) one
	public void clearMap() {
		
		for(int y = 0; y < MAP_HEIGHT; y++) {
			
			for(int x = 0; x < MAP_WIDTH; x++) {
				
				mapBlocks[x][y] = new MapField(false); 
				
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
	
	//Randomly places the rooms onto the map by setting the respective fields to wall (non-walkable) fields.
	public void placeRooms() {
		
		for(int i = 0; i < ROOM_POOL; i++) {
			
			rooms[i].setPosition(rand.randomInt(1, MAP_WIDTH - 1 - rooms[i].getSizeX()), rand.randomInt(1, MAP_HEIGHT - 1 - rooms[i].getSizeY()));
			
			for (int y=0; y<rooms[i].getSizeY(); y++ ){
				
				for (int x=0; x<rooms[i].getSizeX(); x++ ) {
					mapBlocks[rooms[i].getPosition().x + x][rooms[i].getPosition().y + y].isWalkable = true;
				}
			}
						
		}
		
		
		
		
	}
	
	//Connects all rooms by building paths from room1 to room 2, from room2 to room3, from room3 to room4 and so on to make sure every room can be reached.
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
			
			//Point buildPosition = new Point(rooms[r1].getPosition().x,rooms[r1].getPosition().y);
			
			while(absDeltaX > 0 || absDeltaY > 0) {
			
				int minRandom = 1;
				int maxRandom = 2;
				
				if(absDeltaX <= 0 )
					minRandom = 2;
				if(absDeltaY <= 0)
					maxRandom = 1;
				
				int randomChance = rand.randomInt(minRandom, maxRandom);
				
				if(randomChance == 1) {
					mapBlocks[buildPosition.x + (int)Math.signum(deltaX*-1)][buildPosition.y].isWalkable = true;
					buildPosition.translate((int)Math.signum(deltaX*-1), 0);
					absDeltaX -= 1;
				}
				else
				{
					mapBlocks[buildPosition.x][buildPosition.y + (int)Math.signum(deltaY*-1)].isWalkable = true;
					buildPosition.translate(0, (int)Math.signum(deltaY*-1));
					absDeltaY -= 1;
				}
				
			}
			
			
		}
		
	}
	
	//Displays the world map in the console for debugging purposes.
	public void showMap() {
			
		
		for(int y = 0; y < MAP_HEIGHT; y++) {
		
			for(int x = 0; x < MAP_WIDTH; x++) {
			
				if (mapBlocks[x][y].isWalkable == true){
			 
					System.out.print("#");
					
					} else {
						System.out.print(".");
					}
		
		
				}
				System.out.println();
			}
		
	}
}
