package player;

import AI.IDamageable;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.MouseInfo;
import greenfoot.World;
import weapons.abstracts.Weapon;
import weapons.long_range_weapon.Crossbow;

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
	private final int CHANGE_WALK_ANIMATION_FRAMES=40;
	
	private final int MAX_WEAPON_ROTATION=70;

	//TODO: LongRangeWEapons call parameterless constructor atm
	Weapon w=null;

	public Player() {
		super(400);
		w=new Crossbow(this);

		idleImage=new GreenfootImage("player/player_idle.png");
		walkImgs=new GreenfootImage[2];
		walkImgs[0]=new GreenfootImage("player/player_walk1.png");
		walkImgs[1]=new GreenfootImage("player/player_walk2.png");

		animTicks=0;
	}

	@Override
	protected void addedToWorld(World world)
	{
		super.addedToWorld(world);
		getWorld().addObject(w, getGlobalX(), getGlobalY());
	}

	@Override
	public void damage(int dmg)
	{
		System.out.println("Ouch! " + dmg + " damage taken.");
	}

	@Override
	public int getHP()
	{
		return -1;
	}

	@Override
	public void act() {
		super.act();

		getKeysDown();

		moveInOneOf8Directions();

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
			if(getImage()!=idleImage)
				setImage(idleImage);
		}

		centerCamera();

		if(lmbClicked)
			w.use();
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
			
			if(walkRot-currRot<-180)		//Compensate the "jump" from 0° - 360° and vice versa
				currRot-=360;
			else if(walkRot-currRot>180)
				currRot+=360;
			
			if(currRot<minAngle)
				setRotation(minAngle);
			else if(currRot>maxAngle)
				setRotation(maxAngle);
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
