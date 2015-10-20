package main;

public class DungeonGenerator {
//Just a test comment
	//tests
	//nummer 3
	public static final int ROOM_POOL = 8;
	public static final int MAP_WIDTH = 10;
	public static final int MAP_HEIGHT = 10;
	
	public static final int MAX_ROOM_SIZE= 6;
	public static final int MIN_ROOM_SIZE= 2;
	
	private MapField[][] mapBlocks = new MapField[MAP_WIDTH][MAP_HEIGHT];
		
	//Füllt jedes Feld der Map mit einem nicht betretbaren Feld-Objekt.
	public void clearMap() {
		
		for(int y = 0; y < MAP_HEIGHT; y++) {
			
			for(int x = 0; x < MAP_WIDTH; x++) {
				
				mapBlocks[x][y] = new MapField(false); 
				System.out.print(mapBlocks[x][y].isWalkable + " ");
			}
			
			System.out.println();
		}
		
	}
	
	//Erstellt Räume von zufälliger Größe und speichert sie in einem Array
	//(Mindest-Größe und Maximal-Größe sind als Konstante festgelegt.)
	public void generateRooms() {
		
		Room[] rooms = new Room[ROOM_POOL];
		
		for(int i = 0; i < ROOM_POOL; i++) {
			
			int randomWidth = MegaRandom.randomInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
			int randomHeight = MegaRandom.randomInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
			rooms[i] = new Room(randomWidth, randomHeight);
			
			System.out.println("Raum " + i +" -> Width: " + rooms[i].getSizeX() + ", Height: " + rooms[i].getSizeY());
		}
		
	}
	
}
