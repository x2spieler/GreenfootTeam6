package DungeonGeneration;

public class MapField {

	boolean isWalkable = false;
	
	public MapField (boolean isWalkable) {
		
		this.isWalkable = isWalkable;
	
	}
	
	public MapField () {
		
		this.isWalkable = false;
	
	}
	
	public boolean walkable()
	{
		return isWalkable;
	}
	
}
