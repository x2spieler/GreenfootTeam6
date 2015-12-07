package weapons.abstracts;
import java.awt.geom.Point2D;

import AI.Enemy;
import AI.IWorldInterfaceForAI;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.MouseInfo;
import greenfoot.World;
import player.Player;
import scrollWorld.ScrollActor;
import sun.security.util.UntrustedCertificates;
import weapons.EntityType;
import world.DungeonMap;

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
	protected int ticksPerAnimImg=-1;
	protected IWorldInterfaceForAI wi = null;
	protected boolean playAnimation=false;
	protected EntityType typeToIgnore=null;
	GreenfootImage emptyImage=null;
	protected boolean isLongRangeWeapon;
	protected boolean isPlayerWeapon;
	protected boolean launchLongRangeWeapon=false;
	protected String displayName="Unknown name";
	
	private int showWeaponWhileChanging=0;
	private final int SHOW_WEAPON_ON_CHANGE_DUR=30;
	private int prevRotation=0;
	private int animTicks=0;
	private boolean active=false;
	private final int MAX_WEAPON_ROTATION = 22;

	public Weapon()
	{
		animImages=new GreenfootImage[4];
	}
	
	public void activateWeapon()
	{
		active=true;
		setImage(animImages[0]);
		showWeaponWhileChanging=SHOW_WEAPON_ON_CHANGE_DUR;
	}
	
	public void deactivateWeapon()
	{
		playAnimation=false;
		active=false;
		setImage(emptyImage);
		showWeaponWhileChanging=0;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
	
	public String getWeaponName()
	{
		return weaponName;
	}
	
	public boolean isPlayingAnimation()
	{
		return playAnimation;
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
		isPlayerWeapon=typeToIgnore==EntityType.PLAYER;
		loadImages();
	}

	@Override
	public void act()
	{
		if((isPlayerWeapon&&active)||playAnimation)
		{
			rotatePoint(weaponOffsetToPlayer, getRotation()-prevRotation);
			setGlobalLocation(owner.getGlobalX()+(int)weaponOffsetToPlayer.getX(), owner.getGlobalY()+(int)weaponOffsetToPlayer.getY());
			prevRotation=getRotation();
			if(isPlayerWeapon)
			{
				MouseInfo info = Greenfoot.getMouseInfo();
				if (info != null) {
					int x = info.getX();
					int y = info.getY();
					turnTowards(x, y);
				}
			}
			else
			{
				setRotation(owner.getRotation());
			}
			int walkRot=owner.getRotation();
			int currRot = getRotation();
			int minAngle = walkRot - MAX_WEAPON_ROTATION;
			int maxAngle = walkRot + MAX_WEAPON_ROTATION;

			if (walkRot - currRot < -180) //Compensate the "jump" from 0� - 360� and vice versa
				currRot -= 360;
			else if (walkRot - currRot > 180)
				currRot += 360;

			if (currRot < minAngle)
				setRotation(minAngle);
			else if (currRot > maxAngle)
				setRotation(maxAngle);
		}
		if(playAnimation)
		{
			if(animTicks%ticksPerAnimImg==0)
				setImage(animImages[animTicks/ticksPerAnimImg]);
			animTicks++;
			if(animTicks==4*ticksPerAnimImg)
			{
				animTicks=0;
				playAnimation=false;
				if(owner instanceof Enemy)
				{
					((Enemy)owner).stopAttacking();
				}
				setImage(emptyImage);

				launchLongRangeWeapon=true;
			}
		}
		else
		{
			if(getImage()!=emptyImage&&showWeaponWhileChanging==0)
				setImage(emptyImage);
			if(showWeaponWhileChanging>0)
				showWeaponWhileChanging--;
		}
	}

	/**
	 * @return If the weapon was fired (in this term a sword is able to fire, doesn't matter). Also pays attention to the ammo
	 */
	public boolean use()
	{
		long millisNow = DungeonMap.getGreenfootTime();
		if (lastUsage + reloadTimeInMS < millisNow)
		{
			if(triggerUse())
			{
				lastUsage = millisNow;
				playAnimation=true;
				animTicks=0;
				showWeaponWhileChanging=0;
				return true;
			}	
		}
		return false;
	}
	
	public void resetWeapon()
	{
		playAnimation=false;
		animTicks=0;
		launchLongRangeWeapon=false;
	}

	/**
	 * Called by use() if the weapon can be used
	 */
	protected abstract boolean triggerUse();

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
		if(isPlayerWeapon)
			setImage(animImages[0]);
		else
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

	
	//GETTER / SETTER
	
	public int getDamage()
	{
		return damage;
	}
	
	public void setDamage(int dmg)
	{
		damage=dmg;
	}
	
	public int getReloadTimeInMs()
	{
		return reloadTimeInMS;
	}
	
	public void setReloadtimeInMs(int ms)
	{
		reloadTimeInMS=ms;
	}
	
	public void setAdditionalValue(int val)
	{
		additionalValue=val;
	}

	public boolean isLongRangeWeapon() {
		return isLongRangeWeapon;
	}
	
	public void setTicksPerAnimImg(int ticks)
	{
		ticksPerAnimImg=ticks;
	}
	
	public int geTicksPerAnimImg()
	{
		return ticksPerAnimImg;
	}
}
