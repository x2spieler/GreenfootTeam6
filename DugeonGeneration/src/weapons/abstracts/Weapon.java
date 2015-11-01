package weapons.abstracts;
import AI.Enemy;
import scrollWorld.ScrollActor;

public abstract class Weapon extends ScrollActor
{
	protected int damage = -1;
	protected int reloadTimeInMS = -1;
	protected long lastUsage = -1;
	protected int additionalValue = -1; // value that adds to the enemys value
	protected Enemy user=null;

	public abstract boolean use();
	
	public int getAdditionalValue()
	{
		return additionalValue;
	}
}
