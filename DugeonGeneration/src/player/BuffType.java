package player;

public enum BuffType 
{
	SPEED_MULTIPLIER("Speed-Multiplier: "),
	MAX_HP("Max-HP: ");
	
	private String val;
	
	private BuffType(String s)
	{
		val=s;
	}
	
	public String getValue()
	{
		return val;
	}
}
