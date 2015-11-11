package player;

public enum BuffType 
{
	SPEED_MULTIPLIER("Speed-Multiplier: "),
	MAX_HP("Max-HP: "),
	MELEE_DAMAGE("Melee-Damage: "),
	RELOAD_TIME("Reload-Time: "),
	WEAPON_SPEED("Weapon-Speed: ");
	
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
