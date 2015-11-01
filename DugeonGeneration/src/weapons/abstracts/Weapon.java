package weapons.abstracts;
import java.awt.geom.Point2D;

import AI.Enemy;
import AI.IWorldInterfaceForAI;
import greenfoot.GreenfootImage;
import greenfoot.World;
import scrollWorld.ScrollActor;
import weapons.EntityType;

public abstract class Weapon extends ScrollActor
{
	protected int damage = -1;
	protected int reloadTimeInMS = -1;
	protected long lastUsage = -1;
	protected int additionalValue = -1; // value that adds to the enemys value
	protected ScrollActor owner=null;
	protected GreenfootImage[] animImages;
	protected String weaponName;
	protected Point2D weaponOffsetToPlayer=null;
	protected final int TICKS_PER_ANIM_IMG;
	protected IWorldInterfaceForAI wi = null;
	protected boolean playAnimation=false;
	protected EntityType typeToIgnore=null;
	GreenfootImage emptyImage=null;
	protected boolean isLongRangeWeapon;
	
	private int currRotation=0;
	private int animTicks=0;

	public Weapon()
	{
		animImages=new GreenfootImage[4];
		TICKS_PER_ANIM_IMG=getTicksPerAnimImg();
	}
	
	public boolean isLongRangeWeapon() {
		return isLongRangeWeapon;
	}
	
	@Override
	protected void addedToWorld(World world) 
	{
		super.addedToWorld(world);
		wi=(IWorldInterfaceForAI) world;
		if(wi==null)
		{
			System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");
		}
		weaponOffsetToPlayer=new Point2D.Double(wi.getTileSize()/2,0);
		loadImages();
		typeToIgnore=(owner instanceof Enemy ? EntityType.ENEMY : EntityType.PLAYER);
	}
	
	protected abstract int getTicksPerAnimImg();
	
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
				setImage(emptyImage);
				if(owner instanceof Enemy)
					((Enemy)owner).stopAttacking();
			}
			else
			{
				rotatePoint(weaponOffsetToPlayer, owner.getRotation()-currRotation);
				setGlobalLocation(owner.getGlobalX()+(int)weaponOffsetToPlayer.getX(), owner.getGlobalY()+(int)weaponOffsetToPlayer.getY());
				setRotation(owner.getRotation());
				currRotation=owner.getRotation();
			}
		}
	}

	public boolean use()
	{
		long millisNow = System.currentTimeMillis();
		if (lastUsage + reloadTimeInMS < millisNow)
		{
			lastUsage = millisNow;
			playAnimation=true;
			triggerUse();
		}
		else
			return false;
		return true;
	}
	
	/**
	 * Called by use() if the weapon can be used
	 */
	protected abstract void triggerUse();

	public int getAdditionalValue()
	{
		return additionalValue;
	}

	protected void loadImages()
	{
		for(int i=0;i<4;i++)
		{
			animImages[i]=new GreenfootImage("enemies/weapons/"+weaponName+"/"+weaponName+i+".png");
		}
		emptyImage=new GreenfootImage("empty.png");
		setImage(emptyImage);
	}

	//Implementation credits: http://stackoverflow.com/questions/2259476/rotating-a-point-about-another-point-2d
	protected void rotatePoint(Point2D point, int angle)
	{
		double s=Math.sin(Math.toRadians(angle));
		double c=Math.cos(Math.toRadians(angle));

		double xNew=point.getX()*c-point.getY()*s;
		double yNew=point.getX()*s+point.getY()*c;
		point.setLocation(xNew, yNew);
	}

}