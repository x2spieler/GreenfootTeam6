package DungeonGeneration;

public class DungeonGenerator {

	public static final int ROOM_POOL = 50;
	public static final int MAP_WIDTH = 80;
	public static final int MAP_HEIGHT = 80;
	
	public static final int MAX_ROOM_SIZE= 10;
	public static final int MIN_ROOM_SIZE= 10;
	
	private MapField[][] mapBlocks = new MapField[MAP_WIDTH][MAP_HEIGHT];
		
	private Room[] rooms = new Room[ROOM_POOL];
	
	public void clearMap() {
		
		for(int y = 0; y < MAP_HEIGHT; y++) {
			
			for(int x = 0; x < MAP_WIDTH; x++) {
				
				mapBlocks[x][y] = new MapField(false); 
				//System.out.print(mapBlocks[x][y].isWalkable + " ");
			}
			
			//System.out.println();
		}
		
	}
	
	
	public void generateRooms() {
				
		for(int i = 0; i < ROOM_POOL; i++) {
			
			int randomWidth = MegaRandom.randomInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
			int randomHeight = MegaRandom.randomInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
			rooms[i] = new Room(randomWidth, randomHeight);
			
			//System.out.println("Raum " + i +" -> Width: " + rooms[i].getSizeX() + ", Height: " + rooms[i].getSizeY());
		}
		
	}
	
	//Die in dem Rooms-Array generierten Räume werden zufällig auf der Map platziert.
	public void placeRooms() {
		
		for(int i = 0; i < ROOM_POOL; i++) {
			
			rooms[i].setPosition(MegaRandom.randomInt(1, MAP_WIDTH - 1 - rooms[i].getSizeX()), MegaRandom.randomInt(1, MAP_HEIGHT - 1 - rooms[i].getSizeY()));
			
			for (int y=0; y<rooms[i].getSizeY(); y++ ){
				
				for (int x=0; x<rooms[i].getSizeX(); x++ ) {
					mapBlocks[rooms[i].getPosition().x + x][rooms[i].getPosition().y + y].isWalkable = true;
				}
			}
						
		}
		
		
		
		
	}
	
	//Pfad von Raum0 zu Raum1 zu Raum2 zu Raum3 ... zu RaumN erstellen, um sicher zu stellen, dass jeder Raum erreichbar ist.
	//bitte noch fertig machen
	//neuer Commit =)
	public void buildPaths() {
		
		for(int r1 = 0; r1 < ROOM_POOL -1; r1++) {
			
			int deltaX = Math.abs(rooms[r1].getPosition().x - rooms[r1 + 1].getPosition().x);
			int deltaY = Math.abs(rooms[r1].getPosition().y - rooms[r1 + 1].getPosition().y);
			
		}
		
	}
	
	
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
