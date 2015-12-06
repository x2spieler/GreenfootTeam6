package menu;

public enum BuyItem 
{
	MEDI_PACK("Medi Pack"),
	BULLET_CROSSBOW_ARROW("Crossbow Arrow"),
	BULLET_NINJA_STAR("Ninja Star (bullet)"),
	WEAPON_CROSSBOW("Crossbow"),
	WEAPON_NINJA_STAR("Ninja star (weapon)"),
	WEAPON_SWORD("Sword"),
	WEAPON_AXE("Axe"),
	WEAPON_SPEAR("Spear"),
	WEAPON_HAMMER("Hammer");
	
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
