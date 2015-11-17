package menu;

public enum BuyItem 
{
	MEDI_PACK("Medi Pack"),
	BULLET_CROSSBOW_ARROW("Crossbow Arrow"),
	BULLET_NINJA_STAR("Ninja Star (bullet)"),
	WEAPON_CROSSBOW("Crossbow"),
	WEAPON_NINJA_STAR("Ninja star (weapon)"),
	WEAPON_CLUB_WITH_SPIKES("Club with spikes"),
	WEAPON_SWORD("Sword");
	
	String val="";
	
	private BuyItem(String s)
	{
		val=s;
	}
	
	public String getValue()
	{
		return val;
	}
}
