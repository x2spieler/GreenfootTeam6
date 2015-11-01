package weapons.abstracts;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import AI.Enemy;
import AI.IDamageable;
import AI.IWorldInterfaceForAI;
import greenfoot.GreenfootImage;
import greenfoot.World;
import player.Player;
import weapons.bullets.EntityType;

public abstract class ShortRangeWeapon extends Weapon
{
	protected String weaponName="";
	protected Point2D weaponOffsetToPlayer=null;

	private int currRotation=0;
	private int animTicks=0;
	private final int TICKS_PER_ANIM_IMG=10;
	private boolean playAnimation=false;
	private GreenfootImage[] animImages;
	private IWorldInterfaceForAI wi = null;
	private ArrayList<IDamageable> hitEntities;
	private EntityType typeToIgnore=null;
	
	public ShortRangeWeapon()
	{
		animImages=new GreenfootImage[4];
		hitEntities=new ArrayList<IDamageable>();
	}

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
		super.addedToWorld(world);
		wi=(IWorldInterfaceForAI) world;
		if(wi==null)
		{
			System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");
		}
		weaponOffsetToPlayer=new Point2D.Double(wi.getTileSize()/2,0);
		typeToIgnore=(owner instanceof Enemy ? EntityType.ENEMY : EntityType.PLAYER);
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
				if(owner instanceof Enemy)
					((Enemy)owner).stopAttacking();
				hitEntities.clear();
			}
			else
			{
				rotatePoint(weaponOffsetToPlayer, owner.getRotation()-currRotation);
				setGlobalLocation(owner.getGlobalX()+(int)weaponOffsetToPlayer.getX(), owner.getGlobalY()+(int)weaponOffsetToPlayer.getY());
				setRotation(owner.getRotation());
				currRotation=owner.getRotation();
				dealDamage();
			}
		}
	}

	/**
	 * @return Returns true if we hit a player/enemy
	 */
	public void dealDamage()
	{
		List<?> intersectingObjects = getIntersectingObjects(null);
		if (intersectingObjects.size() != 0)
		{
			for (Object o : intersectingObjects)
			{
				if(!(o instanceof IDamageable))
					continue;
				
				if(typeToIgnore==EntityType.ENEMY&&(o instanceof Enemy)
						||typeToIgnore==EntityType.PLAYER&&(o instanceof Player))
					continue;

				IDamageable id=(IDamageable) o;
				if(hitEntities.contains(id))
					continue;
				id.damage(damage);
				hitEntities.add(id);
				return;
			}
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
		}
		else
			return false;
		return true;
	}

}
