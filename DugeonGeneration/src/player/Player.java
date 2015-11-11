package player;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;

import AI.IDamageable;
import core.FrameType;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.MouseInfo;
import greenfoot.World;
import weapons.abstracts.LongRangeWeapon;
import weapons.abstracts.Weapon;
import weapons.long_range_weapon.Crossbow;
import weapons.long_range_weapon.NinjaStar;
import weapons.short_range.ClubWithSpikes;
import weapons.short_range.Sword;
import world.DungeonMap;

public class Player extends DeltaMover implements IDamageable {

	private boolean forward = false;
	private boolean backward = false;
	private boolean right = false;
	private boolean left = false;

	private boolean lmbClicked=false;
	private boolean wasLmbClicked=false;

	GreenfootImage idleImage;
	GreenfootImage walkImgs[];
	int animTicks;
	private final int CHANGE_WALK_ANIMATION_FRAMES=20;

	private final int MAX_WEAPON_ROTATION=70;
	private ArrayList<Weapon> weapons;
	Weapon currWeapon=null;
	int currWeaponIndx=0;

	private int maxHP=-1;
	private int currHP=-1;

	private int score=0;

	private ArrayList<QueuedBuff> queuedBuffs;
	private HashMap<BuffType, Double> activeWeaponBuffs;
	private HashMap<BuffType, Double> appliedWeaponBuffs;

	private DungeonMap dungeonMap=null;

	boolean mouseWheelListenerRegistered=false;

	public Player(int hp) {
		super(400); 

		this.maxHP=hp;
		this.currHP=hp;

		activeWeaponBuffs=new HashMap<BuffType, Double>();
		appliedWeaponBuffs=new HashMap<BuffType, Double>();

		weapons=new ArrayList<Weapon>();
		addWeapon(new Sword(this));
		addWeapon(new ClubWithSpikes(this));
		addWeapon(new Crossbow(this, 30));
		addWeapon(new NinjaStar(this, 30));

		idleImage=new GreenfootImage("player/player_idle.png");
		walkImgs=new GreenfootImage[2];
		walkImgs[0]=new GreenfootImage("player/player_walk1.png");
		walkImgs[1]=new GreenfootImage("player/player_walk2.png");

		animTicks=0;

		queuedBuffs=new ArrayList<QueuedBuff>();
	}

	@Override
	protected void addedToWorld(World world)
	{
		super.addedToWorld(world);

		if(!(getWorld() instanceof DungeonMap))
			throw new IllegalStateException("Player must only be added to a DungeonMap");

		dungeonMap=(DungeonMap) getWorld();

		for(Weapon w:weapons)
		{
			getWorld().addObject(w, getGlobalX(), getGlobalY());
			w.deactivateWeapon();
		}
		setWeapon(0);
	}

	private boolean setWeapon(int wpnIndx)
	{
		if(currWeapon!=null)
		{
			currWeapon.deactivateWeapon();
			for(BuffType bt:appliedWeaponBuffs.keySet())
			{
				removeWeaponBuff(bt, false);
			}
		}
		wpnIndx%=weapons.size();
		if(wpnIndx<0)
			wpnIndx=weapons.size()-1;
		currWeapon=weapons.get(wpnIndx);
		currWeapon.activateWeapon();
		for(BuffType bt:activeWeaponBuffs.keySet())
		{
			applyWeaponBuffs(bt, activeWeaponBuffs.get(bt), false);
		}
		dungeonMap.updateWeaponName(currWeapon);
		dungeonMap.updateAmmoLabel(currWeapon);
		return true;
	}

	@Override
	public void damage(int dmg)
	{
		System.out.println("Ouch! " + dmg + " damage taken.");
		currHP-=dmg;
		dungeonMap.updateHealthLabel(getHP());
		if(currHP<=0)
		{
			System.out.println("Player died");
			Greenfoot.stop();
		}
	}

	@Override
	public int getHP()
	{
		return currHP;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getScore() {
		return score;
	}

	public void addScore(int s)
	{
		score+=s;
	}

	@Override
	public void act() {
		super.act();

		if(!mouseWheelListenerRegistered)
		{
			//Can't use addedToWorld for this
			dungeonMap.addMouseListenerToContentPane((MouseWheelEvent e)->{
				currWeaponIndx+=e.getWheelRotation();
				setWeapon(currWeaponIndx);
			});
			mouseWheelListenerRegistered=true;

			dungeonMap.updateHealthLabel(getHP());
			dungeonMap.updateAmmoLabel(currWeapon);
			dungeonMap.updateWeaponName(currWeapon);
			dungeonMap.updateScoreLabel(getScore());
		}

		getKeysDown();

		moveInOneOf8Directions();

		animatePlayer();

		processQueuedBuffs();

		centerCamera();

		if(lmbClicked)
			if(currWeapon.use())
				dungeonMap.updateAmmoLabel(currWeapon);
	}

	private void moveInOneOf8Directions() {
		int walkRot=-1;
		if (forward && !right && !left)
			walkRot=270;
		if (backward && !right && !left)
			walkRot=90;
		if (right && !forward && !backward)
			walkRot=0;
		if (left && !forward && !backward)
			walkRot=180;
		if (right && forward)
			walkRot=315;
		if (right && backward)
			walkRot=45;
		if (left && forward)
			walkRot=225;
		if (left && backward)
			walkRot=135;

		if(walkRot!=-1)
		{
			setRotation(walkRot);
			move();
		}
		faceMouse();
		if(walkRot!=-1)
		{
			int currRot=getRotation();
			int minAngle=walkRot-MAX_WEAPON_ROTATION;
			int maxAngle=walkRot+MAX_WEAPON_ROTATION;

			if(walkRot-currRot<-180)		//Compensate the "jump" from 0� - 360� and vice versa
				currRot-=360;
			else if(walkRot-currRot>180)
				currRot+=360;

			if(currRot<minAngle)
				setRotation(minAngle);
			else if(currRot>maxAngle)
				setRotation(maxAngle);
		}
	}

	private void animatePlayer()
	{
		if(forward||backward||right||left)
		{
			if(animTicks==0)
				setImage(walkImgs[0]);
			else if(animTicks==CHANGE_WALK_ANIMATION_FRAMES)
				setImage(walkImgs[1]);

			animTicks=(++animTicks)%(2*CHANGE_WALK_ANIMATION_FRAMES);
		}
		else 
		{
			if(!currWeapon.isPlayingAnimation()&&getImage()!=idleImage)
				setImage(idleImage);
			else if(currWeapon.isPlayingAnimation()&&getImage()!=walkImgs[0])
				setImage(walkImgs[0]);
		}
	}

	private void getKeysDown() {
		forward = Greenfoot.isKeyDown(Direction.FORWARD.key);
		backward = Greenfoot.isKeyDown(Direction.BACKWARD.key);
		right = Greenfoot.isKeyDown(Direction.RIGHT.key);
		left = Greenfoot.isKeyDown(Direction.LEFT.key);
		if (forward && backward) {
			forward = false;
			backward = false;
		}
		if (left && right) {
			left = false;
			right = false;
		}

		MouseInfo info=Greenfoot.getMouseInfo();
		if(info!=null&&info.getButton()==1)		//1 is LMB
		{
			if(!wasLmbClicked)
				lmbClicked=true;
			else
				lmbClicked=false;
		}
		else
			lmbClicked=false;
		wasLmbClicked=(info!=null ? info.getButton()==1 : false);

		if(Greenfoot.isKeyDown("escape"))
			dungeonMap.changeToFrame(FrameType.PAUSE_MENU);
	}

	private void faceMouse() {
		MouseInfo info = Greenfoot.getMouseInfo();
		if (info != null) {
			int x = info.getX();
			int y = info.getY();
			turnTowards(x, y);
		}
	}

	private void centerCamera() {
		getWorld().setCameraLocation(getGlobalX(), getGlobalY());
	}

	///////////////////////// BUFFS AND POWERUPS

	public void heal(int hp)
	{
		currHP+=hp;
		if(currHP>maxHP)
			currHP=maxHP;
		dungeonMap.updateHealthLabel(getHP());
	}

	/**
	 * 
	 * @param bullet
	 * @param num
	 * @return True if ammo was added / false if the player has no weapon that uses the given Bullets
	 */
	@SuppressWarnings("rawtypes")
	public boolean addAmmo(Class bullet, int num)
	{
		for(Weapon w:weapons)
		{
			if(!w.isLongRangeWeapon())
				continue;
			LongRangeWeapon lrw=(LongRangeWeapon)w;
			if(lrw.areBulletsClassOf(bullet))
			{
				lrw.addAmmo(num);
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds the weapon to the players weapon list
	 * If he already has the weapon and it is a LongRangeWeapon, only the ammo weapon
	 * @param weapon
	 */
	public void addWeapon(Weapon weapon)
	{
		for(Weapon w:weapons)
		{
			if(w.getWeaponName().equals(weapon.getWeaponName()))
			{
				if(w.isLongRangeWeapon())
				{
					LongRangeWeapon lrw=(LongRangeWeapon)w;
					lrw.addAmmo(((LongRangeWeapon)weapon).getAmmo());
				}
				return;
			}
		}
		weapons.add(weapon);
	}

	/**
	 * 
	 * @param buff
	 * @param param
	 * @param durationInMs	-1 for perma buffs, >0 for temp buffs,  -2 for reverting temp buffs (only used internally, do not use -2!)
	 */
	public void addBuff(BuffType buff, double param, int durationInMs)
	{
		if(durationInMs>=-1)
			dungeonMap.addOrUpdateBuffLabel(buff, param);
		else
			dungeonMap.removeBuffLabel(buff);

		switch(buff)
		{
		case SPEED_MULTIPLIER:
			setSpeed((int)(getSpeed()*param));
			if(durationInMs>=0)
			{
				queuedBuffs.add(new QueuedBuff(System.currentTimeMillis()+durationInMs, buff, 1.d/param));
			}
			break;
		case MAX_HP:
			maxHP+=(int)param;
			if(durationInMs>=0)
			{
				queuedBuffs.add(new QueuedBuff(System.currentTimeMillis()+durationInMs, buff, -param));
			}
			break;
		case MELEE_DAMAGE:
		case RELOAD_TIME:
		case WEAPON_SPEED:
			if(durationInMs>=-1)
			{
				applyWeaponBuffs(buff, param, true);
				if(durationInMs>=0)
					queuedBuffs.add(new QueuedBuff(System.currentTimeMillis()+durationInMs, buff, -1));
			}
			else {
				removeWeaponBuff(buff, true);
			}
			break;

		}
	}

	@SuppressWarnings("incomplete-switch")
	private void applyWeaponBuffs(BuffType b, double param, boolean addToActive)
	{
		double multiplier=1;
		if(addToActive)
		{
			Object o=activeWeaponBuffs.get(b);
			if(o!=null)
				multiplier=(double)o;
		}
		multiplier*=param;
		double realMult=multiplier;
		
		switch(b)
		{
		case MELEE_DAMAGE:
		{
			int dmg=currWeapon.getDamage();
			if((int)(realMult*dmg)==0)
				realMult=1.d/dmg;
			dmg*=realMult;
			currWeapon.setDamage(dmg);
			break;
		}
		case RELOAD_TIME:
		{
			final int TICK_DURATION=1000/60;	//1000ms / 60FPS
			int reloadTime=currWeapon.getReloadTimeInMs();
			if((int)(realMult*reloadTime)==0)
				realMult=1.d/reloadTime;
			if((int)(realMult*reloadTime)<(currWeapon.geTicksPerAnimImg()*4*TICK_DURATION))
				realMult=(4.d*TICK_DURATION*currWeapon.geTicksPerAnimImg())/reloadTime;
			reloadTime*=realMult;
			currWeapon.setReloadtimeInMs(reloadTime);
			break;
		}
		case WEAPON_SPEED:
		{
			final int TICK_DURATION=1000/60;	//1000ms / 60FPS
			int ticksPerImg=currWeapon.geTicksPerAnimImg();
			if((int)(realMult*ticksPerImg)==0)
				realMult=1.d/ticksPerImg;
			if((int)(realMult*ticksPerImg*TICK_DURATION*4)>currWeapon.getReloadTimeInMs())
				realMult=currWeapon.getReloadTimeInMs()/(ticksPerImg*TICK_DURATION*4);
			ticksPerImg*=realMult;
			currWeapon.setTicksPerAnimImg(ticksPerImg);
			break;
		}
		}
		
		appliedWeaponBuffs.put(b, realMult);
		if(addToActive)
			activeWeaponBuffs.put(b, multiplier);
		dungeonMap.addOrUpdateBuffLabel(b, multiplier);
	}

	@SuppressWarnings("incomplete-switch")
	private void removeWeaponBuff(BuffType buff, boolean removeFromList)
	{
		double mult=appliedWeaponBuffs.get(buff);
		mult=1.d/mult;
		switch(buff)
		{
		case MELEE_DAMAGE:
			currWeapon.setDamage((int)(currWeapon.getDamage()*mult));
			break;
		case RELOAD_TIME:
			currWeapon.setReloadtimeInMs((int)(currWeapon.getReloadTimeInMs()*mult));
			break;
		case WEAPON_SPEED:
			currWeapon.setTicksPerAnimImg((int)(currWeapon.geTicksPerAnimImg()*mult));
			break;
		}
		if(removeFromList)
		{
			appliedWeaponBuffs.remove(buff);
			activeWeaponBuffs.remove(buff);
		}
	}

	private void processQueuedBuffs()
	{
		for(int i=0;i<queuedBuffs.size();)
		{
			QueuedBuff qb=queuedBuffs.get(i);
			if(qb.timeStamp<System.currentTimeMillis())
			{
				addBuff(qb.buff, qb.param, -2);
				queuedBuffs.remove(i);
			}
			else
				i++;
		}
	}


	class QueuedBuff
	{
		long timeStamp=-1;
		BuffType buff;
		double param;

		public QueuedBuff(long timeStamp, BuffType buff, double param)
		{
			this.timeStamp=timeStamp;
			this.buff=buff;
			this.param=param;
		}
	}

	///////////////////////////////////TO BE DELETED

	/*
	 * 

	 	public static final int MOVE_MODE_FOLLOW_MOUSE = 0;
	public static final int MOVE_MODE_8_DIRECTIONS = 1;
	public static final int MOVE_MODE_8_DIRECTIONS_POINT_AT_MOUSE = 2;
	public static final int MOVE_MODE_MOUSE_KEYBOARD_HYBRID = 3;

	private int mode = MOVE_MODE_FOLLOW_MOUSE;

	 	private Direction buttonPressed = null;
	private Direction currentDirection = null; 


	 if (mode == MOVE_MODE_FOLLOW_MOUSE) {
			moveAccordingToMouse();
		} else if (mode == MOVE_MODE_8_DIRECTIONS) {
			moveInOneOf8Directions();
		} else if (mode == MOVE_MODE_8_DIRECTIONS_POINT_AT_MOUSE) {
			moveInOneOf8DirectionsFacingMouse();
		} else if (mode == MOVE_MODE_MOUSE_KEYBOARD_HYBRID) {
			moveMouseKeyboardHybrid();
		}


	private void moveMouseKeyboardHybrid() {
		faceMouse();
		getDominantDirection();
		if ((forward && currentDirection == Direction.FORWARD)) {
			move();
		}
		if ((backward && currentDirection == Direction.BACKWARD)) {
			moveBackwards();
		}
		if ((right && currentDirection == Direction.RIGHT)) {
			int move = getTickMove();
			moveAtAngle(move - move / 2, getAngleForRotation(getRotation(), Direction.RIGHT));
		}
		if ((left && currentDirection == Direction.LEFT)) {
			int move = getTickMove();
			moveAtAngle(move - move / 2, getAngleForRotation(getRotation(), Direction.LEFT));
		}
	}

	private void moveInOneOf8DirectionsFacingMouse() {
		faceMouse();
		if (forward && !right && !left) {
			moveAtAngle(270);
		}
		if (backward && !right && !left) {
			moveAtAngle(90);
		}
		if (right && !forward && !backward) {
			moveAtAngle(0);
		}
		if (left && !forward && !backward) {
			moveAtAngle(180);
		}
		if (right && forward) {
			moveAtAngle(315);
		}
		if (right && backward) {
			moveAtAngle(45);
		}
		if (left && forward) {
			moveAtAngle(225);
		}
		if (left && backward) {
			moveAtAngle(135);
		}
	}


	private void moveAccordingToMouse() {
		faceMouse();
		getDominantDirection();
		if ((forward && currentDirection == Direction.FORWARD)) {
			move();
		}
		if ((backward && currentDirection == Direction.BACKWARD)) {
			moveBackwards();
		}
		if ((right && currentDirection == Direction.RIGHT)) {
			int move = getTickMove();
			moveAtAngle(move - (move / 4), addAngles(getRotation(), 90));
		}
		if ((left && currentDirection == Direction.LEFT)) {
			int move = getTickMove();
			moveAtAngle(move - (move / 4), addAngles(getRotation(), 270));
		}
	}

	private void getDominantDirection() {
		buttonPressed = getLastKeyPressed();
		if (buttonPressed != null) {
			currentDirection = buttonPressed;
		} else {
			if (forward && !right && !left) {
				currentDirection = Direction.FORWARD;
			}
			if (backward && !right && !left) {
				currentDirection = Direction.BACKWARD;
			}
			if (!forward && !backward && right) {
				currentDirection = Direction.RIGHT;
			}
			if (!forward && !backward && left) {
				currentDirection = Direction.LEFT;
			}
		}
	}

	private int getAngleForRotation(int rotation, Direction dir) {
		if (rotation <= 45 || rotation > 315 || (rotation <= 225 && rotation > 135)) {
			return (dir == Direction.RIGHT) ? 90 : 270;
		}
		if ((rotation > 45 && rotation <= 135) || (rotation <= 315 && rotation > 225)) {
			return (dir == Direction.RIGHT) ? 0 : 180;
		}
		return 0;
	}

	private Direction getLastKeyPressed() {
		String key = Greenfoot.getKey();
		for (Direction direction : Direction.values()) {
			if (key != null && key.equals(direction.key)) {
				return direction;
			}
		}
		return null;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		if (mode >= 0 && mode < 4)
			this.mode = mode;
	}


	 */


}
