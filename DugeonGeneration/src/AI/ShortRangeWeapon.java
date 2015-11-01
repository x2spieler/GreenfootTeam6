package AI;

import java.awt.geom.Point2D;

import greenfoot.GreenfootImage;
import greenfoot.World;

public abstract class ShortRangeWeapon extends Weapon
{
	protected String weaponName="";
	protected Point2D weaponOffsetToPlayer=null;
	
	private int currRotation=0;
	private int animTicks=0;
	private final int TICKS_PER_ANIM_IMG=10;
	private boolean playAnimation=false;
	private GreenfootImage[] animImages=new GreenfootImage[4];
	private IWorldInterfaceForAI wi = null;

	protected void loadImages()
	{
		for(int i=0;i<4;i++)
		{
			animImages[i]=new GreenfootImage("enemies/weapons/"+weaponName+"/"+weaponName+i+".png");
		}
		setImage("empty.png");
	}

	@Override
	protected void addedToWorld(World world) 
	{
		wi=(IWorldInterfaceForAI) world;
		if(wi==null)
		{
			System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");
		}
		weaponOffsetToPlayer=new Point2D.Double(wi.getTileSize()/2,0);
	}

	@Override
	public void act()
	{
		if(playAnimation)
		{
			if(animTicks%TICKS_PER_ANIM_IMG==0)
				setImage(animImages[animTicks/TICKS_PER_ANIM_IMG]);
			animTicks++;
			if(animTicks==4*TICKS_PER_ANIM_IMG)
			{
				animTicks=0;
				playAnimation=false;
				setImage("empty.png");
				user.stopAttacking();
			}
			rotatePoint(weaponOffsetToPlayer, user.getRotation()-currRotation);
			setGlobalLocation(user.getGlobalX()+(int)weaponOffsetToPlayer.getX(), user.getGlobalY()+(int)weaponOffsetToPlayer.getY());
			setRotation(user.getRotation());
			currRotation=user.getRotation();
		}
	}

	//Implementation credits: http://stackoverflow.com/questions/2259476/rotating-a-point-about-another-point-2d
	private void rotatePoint(Point2D point, int angle)
	{
		double s=Math.sin(Math.toRadians(angle));
		double c=Math.cos(Math.toRadians(angle));

		double xNew=point.getX()*c-point.getY()*s;
		double yNew=point.getX()*s+point.getY()*c;
		point.setLocation(xNew, yNew);
	}

	@Override
	public boolean use()
	{
		long millisNow = System.currentTimeMillis();
		if (lastUsage + reloadTimeInMS < millisNow)
		{
			lastUsage = millisNow;
			playAnimation=true;
			wi.getPlayer().damage(damage);
		}
		else
			return false;
		return true;
	}

}
