package AI;
import greenfoot.Actor;

public abstract class Weapon extends Actor implements IWeapon
{
	protected int damage = -1;
	protected int reloadTimeInMS = -1;
	protected long lastUsage = -1;
	protected int additionalValue = -1; // value that adds to the enemys value

	public abstract void use();
}
