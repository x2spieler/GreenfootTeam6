package AI;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.MapField;
import greenfoot.GreenfootImage;
import greenfoot.GreenfootSound;
import greenfoot.World;
import player.DeltaMover;
import weapons.abstracts.Weapon;
import weapons.long_range_weapon.Crossbow;
import weapons.short_range.ClubWithSpikes;
import weapons.short_range.Sword;
import world.DungeonMap;

public abstract class Enemy extends DeltaMover implements IDamageable
{

	protected final int NUM_FRAMES_CHANGE_WALK_IMAGE;
	protected Weapon weapon = null;
	protected int value = -1;
	protected int hp = -1;
	protected int viewRangeSquared = -1;		//In Pixels, not tiles
	protected String enemyName="";
	protected GreenfootImage idleImage=null;
	protected GreenfootImage walk1Image=null;
	protected GreenfootImage walk2Image=null;
	protected GreenfootImage attackImage=null;
	protected String[] allowedWeapons;

	private boolean isAttacking=false;
	private boolean cantFindWay=false;
	private boolean seesPlayer=false;
	private boolean sawPlayer=false;
	private Node currTargetNode=null;
	private IWorldInterfaceForAI wi = null;
	private Point lastPlayerTile=null;
	private final int REACHED_TARGET_DISTANCE_SQUARED=25;
	private final int REACHED_PLAYER_DISTANCE_SQUARED=1024;
	private final int RPD_MULTIPLICATOR_LRW=30;			//REACHED_PLAYER_DISTANCE_MULTIPLICATOR_LONG_RANGE_WEAPONS
	private final int TILE_SIZE=DungeonMap.TILE_SIZE;
	private static GreenfootSound encounterSound=new GreenfootSound("encounterPlayer.wav");
	private short walkCounter=0;
	boolean isPendingKill=false;

	public Enemy()
	{
		super(0);
		NUM_FRAMES_CHANGE_WALK_IMAGE=getNumFramesChangeWalkImage();
	}

	protected abstract int getNumFramesChangeWalkImage();
	
	@Override
	public int getHP() 
	{
		return -1;
	}

	@Override
	public void addedToWorld(World w)
	{
		super.addedToWorld(w);
		wi = (IWorldInterfaceForAI) getWorld();
		if (wi == null)
		{
			System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");
			return;
		}
		loadImages();
		createWeapon();
	}
	
	/**
	 * @return True if this enemy has been killed and is only in the world to to his death animation
	 */
	public boolean isPendingKill()
	{
		return isPendingKill;
	}

	private void loadImages()
	{
		idleImage=new GreenfootImage("enemies/"+enemyName+"/"+enemyName+"_idle.png");
		walk1Image=new GreenfootImage("enemies/"+enemyName+"/"+enemyName+"_walk1.png");
		walk2Image=new GreenfootImage("enemies/"+enemyName+"/"+enemyName+"_walk2.png");
		attackImage=new GreenfootImage("enemies/"+enemyName+"/"+enemyName+"_attack.png");
	}

	private void createWeapon()
	{
		Random r=new Random();
		switch(allowedWeapons[r.nextInt(allowedWeapons.length)])
		{
		case "sword":
			weapon=new Sword(this);
			break;
		case "club_spikes":
			weapon=new ClubWithSpikes(this);
			break;
		case "crossbow":
			weapon=new Crossbow(this);
			break;
		default:
			System.out.println("Seems like somebody forgot to update this switch-statement after adding new weapons.. Bad boy!");
		}
		if(weapon!=null)
			getWorld().addObject(weapon, 0, 0);
	}

	private boolean isInRangeOfPlayer()	
	{
		Point p=wi.getPlayerPosition();
		double xDist=getGlobalX()-p.x;
		double yDist=getGlobalY()-p.y;
		return ((xDist*xDist)+(yDist*yDist))<viewRangeSquared;
	}

	@Override
	public void damage(int dmg)
	{
		if(isPendingKill)
			return;
		System.out.println("Graaaaar, ouuuuhhh: "+dmg);
		hp -= dmg;
		if (hp <= 0)
		{
			setImage("tombstone.png");
			wi.addPlayerScore(value+weapon.getAdditionalValue());
			getWorld().removeObject(weapon);
			isPendingKill=true;
		}
	}

	public void stopAttacking()
	{
		isAttacking=false;
	}

	@Override
	public void act()
	{
		if(isPendingKill)
		{
			int transp=getImage().getTransparency();
			transp-=1;
			if(transp==0)
			{
				getWorld().removeObject(this);
			}
			else
				getImage().setTransparency(transp);
			return;
		}
		
		super.act();
		
		if(getImage()==null)
			setImage(idleImage);

		seesPlayer=isInRangeOfPlayer();
		Point currPlayerTile=wi.getPlayerPosition();
		currPlayerTile.x/=TILE_SIZE;
		currPlayerTile.y/=TILE_SIZE;
		Point currTile=new Point(getGlobalX()/TILE_SIZE, getGlobalY()/TILE_SIZE);

		if(currTargetNode==null)
		{
			if(seesPlayer)
			{
				int squaredDistance=squaredDistance(wi.getPlayerPosition().x, wi.getPlayerPosition().y, getGlobalX(), getGlobalY());
				if(!isAttacking&&
						((!weapon.isLongRangeWeapon()&&squaredDistance>REACHED_PLAYER_DISTANCE_SQUARED)
								||(weapon.isLongRangeWeapon()&&squaredDistance>RPD_MULTIPLICATOR_LRW*REACHED_PLAYER_DISTANCE_SQUARED)))
				{
					currTargetNode=findPath(currTile, currPlayerTile, true);
				}
				else
				{
					if(getImage()!=idleImage&&!isAttacking)
					{
						setImage(idleImage);
						turnTowardsGlobalLocation(wi.getPlayerPosition().x, wi.getPlayerPosition().y);
					}
					if(weapon.use())
					{
						isAttacking=true;
						setImage(attackImage);
					}	
				}

			}
			else
			{
				Random random=new Random();
				MapField[][] map=wi.getMap();
				int x, y;
				while(true)		//Assumes that there is always at least one tile to walk on
				{
					x=random.nextInt(DungeonGenerator.MAP_WIDTH);
					y=random.nextInt(DungeonGenerator.MAP_HEIGHT);
					if(map[x][y].walkable())
					{
						currTargetNode=findPath(currTile, new Point(x, y), false);
						if(currTargetNode!=null)
							break;
					}	
				}
			}
		}
		else
		{
			if(seesPlayer&&!sawPlayer)
			{
				//Sees the player - didn't see him in the last tick
				//We can see the player now - CHASE HIM!
				currTargetNode=findPath(currTile, currPlayerTile, true);
				lastPlayerTile=currPlayerTile;
				//TODO: Maybe add this back in
				//encounterSound.play();
			}
			else if(seesPlayer)
			{
				if(!currPlayerTile.equals(lastPlayerTile))
				{
					//As long as we see him, always go straight to the player
					currTargetNode=findPath(currTile, currPlayerTile, true);
					lastPlayerTile=currPlayerTile;
				}
			}
		}

		if(currTargetNode!=null)
		{
			turnTowardsGlobalLocation(currTargetNode.x, currTargetNode.y);
			if(!isAttacking)
				move();
			int squaredDistToTarget=squaredDistance(currTargetNode.x, currTargetNode.y, getGlobalX(), getGlobalY());
			int squaredDistanceToPlayer=squaredDistance(wi.getPlayerPosition().x, wi.getPlayerPosition().y, getGlobalX(), getGlobalY());
			if(isTouchingWall())
			{
				//We ran into a wall and missed our target or can't reach it - just get a new target next frame
				currTargetNode=null;
			}
			else if(seesPlayer&&weapon.isLongRangeWeapon()&&
					squaredDistanceToPlayer<=RPD_MULTIPLICATOR_LRW*REACHED_PLAYER_DISTANCE_SQUARED
					&&canSee(new Point(getGlobalX(), getGlobalY()), wi.getPlayerPosition()))
			{
				//We have a LongRangeWeapon, we are close enough
				currTargetNode=null;		
			}
			else if((seesPlayer&&currTargetNode.prev==null&&squaredDistToTarget<=REACHED_PLAYER_DISTANCE_SQUARED)		//Target is player
					||(squaredDistToTarget<=REACHED_TARGET_DISTANCE_SQUARED))										//Target is a tile
			{
				currTargetNode=currTargetNode.prev;
				if(currTargetNode!=null)
					turnTowardsGlobalLocation(currTargetNode.x, currTargetNode.y);
			}	
			//Animate the enemie
			if(walkCounter==NUM_FRAMES_CHANGE_WALK_IMAGE)
			{
				setImage(walk1Image);
			}
			else if(walkCounter==2*NUM_FRAMES_CHANGE_WALK_IMAGE)
			{
				setImage(walk2Image);
				walkCounter=0;
			}
			walkCounter++;
		}
		sawPlayer=seesPlayer;
	}

	/**
	 * Super simple raytracer, might miss edges.
	 * However, suitable for our needs
	 */
	private boolean canSee(Point p1, Point p2)
	{
		double xDir=p1.x-p2.x;
		double yDir=p1.y-p2.y;
		double length=Math.sqrt(xDir*xDir+yDir*yDir);
		double xNorm=xDir/length;
		double yNorm=yDir/length;
		double currX=p2.x;
		double currY=p2.y;
		double add=TILE_SIZE;
		DungeonMap dm=(DungeonMap)getWorld();
		while(!(Math.abs(p1.x-currX)<1&&Math.abs(p1.y-currY)<1))
		{
			currX+=xNorm*add;
			currY+=yNorm*add;
			if(!dm.isInAccessibleTile((int)currX, (int)currY))
				return false;
		}
		return true;
	}

	private int squaredDistance(int x1, int y1, int x2, int y2)
	{
		int distX=x1-x2;
		int distY=y1-y2;
		return (distX*distX)+(distY*distY);
	}

	private Node findPath(Point start, Point end, boolean toPlayer)
	{
		ArrayList<Node>closedList=new ArrayList<Node>();
		ArrayList<Node>openList=new ArrayList<Node>();

		MapField[][] map=wi.getMap();

		openList.add(new Node(manhattanDistance(start, end.x, end.y),0, end.x, end.y, null));

		Node endNode=null;
		while(endNode==null)
		{
			endNode=step(closedList, openList, map, start);
			if(cantFindWay||!map[end.x][end.y].walkable()||!map[start.x][start.y].walkable())
			{
				cantFindWay=false;
				throw new IllegalStateException("Couldn't find a way");
			}
		}
		if(endNode!=null)
		{
			Node n=endNode;
			while(n.prev!=null)
			{
				n=n.prev;
				n.x=n.x*TILE_SIZE+TILE_SIZE/2;
				n.y=n.y*TILE_SIZE+TILE_SIZE/2;
			}
			if(toPlayer)
			{
				n.x=wi.getPlayerPosition().x;
				n.y=wi.getPlayerPosition().y;
			}
		}

		return (endNode!=null ? endNode.prev : null);
	}

	private Node step(ArrayList<Node>closedList, ArrayList<Node>openList,MapField[][] map, Point start)
	{
		if(openList.size()==0)
		{
			cantFindWay=true;
			return null;
		}
		Node closest=openList.get(0);
		for(Node node:openList)
		{
			if(node.cost<closest.cost)
			{
				closest=node;
			}
		}

		if(closest.x==start.x&&closest.y==start.y)
		{
			return closest;
		}
		int x=-1,y=-1;
		int addX=1;
		int addY=0;
		for(int i=0;i<4;i++)
		{
			if(i==1||i==3)
			{
				addX*=-1;
				addY*=-1;
			}
			if(i==2)
			{
				addX=0;
				addY=1;
			}
			//1. Loop: 1 , 0
			//2. Loop: -1, 0
			//3. Loop: 0, 1l
			//4. Loop: 0, -1
			x=closest.x+addX;	
			y=closest.y+addY;
			if(x<0||y<0||x>=map.length||y>=map[0].length)
				continue;
			Node currNode=new Node(manhattanDistance(start, x,y)+closest.movementCost+1,closest.movementCost+1, x,y, closest);
			if(map[x][y].walkable()&&!closedList.contains(currNode))
			{
				int indx=openList.indexOf(currNode);
				if(indx==-1)
				{
					openList.add(currNode);
				}
				else
				{
					Node inList=openList.get(indx);
					if(currNode.cost<=inList.cost)
					{
						inList.cost=currNode.cost;
						inList.prev=currNode.prev;
						inList.movementCost=currNode.movementCost;
					}
				}
			}
		}

		openList.remove(closest);
		closedList.add(closest);

		return null;
	}

	private double manhattanDistance(Point start, int x, int y)
	{
		return Math.abs(start.x-x)+Math.abs(start.y-y);
	}

	class Node
	{
		double cost;
		double movementCost;
		int x;
		int y;
		Node prev;

		public Node(double cost,double movementCost, int x, int y, Node prev)
		{
			this.movementCost=movementCost;
			this.cost=cost;
			this.x=x;
			this.y=y;
			this.prev=prev;
		}

		public Node(int x, int y)
		{
			this.x=x;
			this.y=y;
		}

		@Override
		public boolean equals(Object v)
		{
			if(v instanceof Node)
			{
				Node n=(Node)v;
				return n.x==x&&n.y==y;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return x*100000+y;
		}
	}

}
