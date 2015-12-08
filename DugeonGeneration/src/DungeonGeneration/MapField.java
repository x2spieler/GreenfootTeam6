package DungeonGeneration;

public class MapField {

	boolean isWalkable = false;
	
	private FieldType fieldType = FieldType.GROUND;
	
	public MapField(FieldType ftype) {
		
		setFieldType(ftype);
		
	}
	
	
	public MapField (boolean isWalkable) {
		
		this.isWalkable = isWalkable;
		fieldType = FieldType.GROUND;
		
	}
	
	public MapField () {
		
		this.isWalkable = false;
		fieldType = FieldType.WALL;
	}
	
	public boolean walkable()
	{
		return isWalkable;
	}
	
	public FieldType getFieldType() {
		return fieldType;
	}
	
	public void setFieldType(FieldType ftype) {
		fieldType = ftype;
		
		switch (fieldType) {
		
			case GROUND:
				isWalkable = true;
				break;
			case DESTRUCTABLE:
				isWalkable = false;
				break;
			case PICKUP:
				isWalkable = true;
				break;
			case WALL:
				isWalkable = false;
				break;
			case CELL:
				isWalkable = false;
				break;
			
			}	
	}
	
}
