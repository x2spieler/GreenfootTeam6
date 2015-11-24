package player;

public enum BuffType 
{
	SPEED_MULTIPLIER("RUN\nSPD"),
	MAX_HP("MAX\nHP"),
	MELEE_DAMAGE("MLE\nDMG"),
	RELOAD_TIME("RLD\nTME"),
	WEAPON_SPEED("WPN\nSPD");
	
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
