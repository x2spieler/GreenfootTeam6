package AI;



public interface IWorldInterfaceForAI
{
	public void addPlayerScore(int score);
	
	public void addBulletToWorld(Bullet bullet);
	
	public double[] GetPlayerCoordinates();
	
	public IDamageable getPlayer();
}
