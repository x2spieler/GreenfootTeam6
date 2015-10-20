package DungeonGeneration;

public class DungeonGenerator {

	public static final int ROOM_POOL = 8;
	public static final int MAP_WIDTH = 10;
	public static final int MAP_HEIGHT = 10;
	
	public static final int MAX_ROOM_SIZE= 6;
	public static final int MIN_ROOM_SIZE= 2;
	
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
			
			System.out.println("Raum " + i +" -> Width: " + rooms[i].getSizeX() + ", Height: " + rooms[i].getSizeY());
		}
		
	}
	
	public void placeRooms() {
		//Raum wird erzeugt und platziert
		
		for (int i=0; i<ROOM_POOL;i++){
		
		rooms[i].setPosition(MegaRandom.randomInt(1,5), MegaRandom.randomInt(1,5));
		
		System.out.println("x=" + rooms[i].getPosition().x +" y= "+ rooms[i].getPosition().y);
		
		for (int y=0; y<rooms[i].getPosition().y; y++ ){
		
			for (int x=0; x<rooms[i].getPosition().x; x++ ) {
				mapBlocks[rooms[i].getPosition().x+x][rooms[i].getPosition().y+y].isWalkable = true;
			}
		}
		}
}
	
	public void showMap() {
		
	
		
		for(int y = 0; y < MAP_HEIGHT; y++) {
		
		for(int x = 0; x < MAP_WIDTH; x++) {
			 
			System.out.print(mapBlocks[x][y].isWalkable + " ");
		}
		
		System.out.println();
	}
		
		
}
}
