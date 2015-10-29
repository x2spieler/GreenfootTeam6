package AI;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.MapField;
import greenfoot.GreenfootImage;
import greenfoot.GreenfootSound;
import scrollWorld.ScrollActor;
import world.DungeonMap;

public abstract class Enemy extends ScrollActor implements IDamageable
{

	protected int stepsPerTick = -1;
	protected Weapon weapon = null;
	protected int value = -1;
	protected int hp = -1;
	protected int viewRangeSquared = -1;		//In Pixels, not tiles
	protected GreenfootImage idleImage=null;
	protected GreenfootImage walk1Image=null;
	protected GreenfootImage walk2Image=null;
	
	private boolean cantFindWay=false;
	private boolean seesPlayer=false;
	private boolean sawPlayer=false;
	private Node currTargetNode=null;
	private IWorldInterfaceForAI wi = null;
	private Point lastPlayerTile=null;
	private TargetShowActor tsa=null;
	private final int REACHED_TARGET_DISTANCE_SQUARED=2;
	private final int TILE_SIZE=DungeonMap.TILE_SIZE;
	private static GreenfootSound encounterSound=new GreenfootSound("encounterPlayer.wav");
	private short walkCounter=0;
	private final int NUM_FRAMES_CHANGE_WALK_IMAGE=10;
	

	public Enemy()
	{

	}

	private boolean isInRangeOfPlayer()
	{
		Point p=wi.getPlayerPosition();
		double xDist=getGlobalX()-p.x;
		double yDist=getGlobalY()-p.y;
		return ((xDist*xDist)+(yDist*yDist))<viewRangeSquared;
	}

	public void damage(int dmg)
	{
		hp -= dmg;
		if (hp <= 0)
		{
			wi.addPlayerScore(value);
		}
	}

	@Override
	public void act()
	{
		if(getImage()==null)
			setImage(idleImage);
		if(wi==null)
		{
			wi = (IWorldInterfaceForAI) getWorld();
			if (wi == null)
			{
				System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");
				return;
			}
		}

		seesPlayer=isInRangeOfPlayer();
		Point currPlayerTile=wi.getPlayerPosition();
		currPlayerTile.x/=TILE_SIZE;
		currPlayerTile.y/=TILE_SIZE;
		Point currTile=new Point(getGlobalX()/TILE_SIZE, getGlobalY()/TILE_SIZE);
		
		if(currTargetNode==null)
		{
			if(tsa!=null){
				getWorld().removeObject(tsa);
			}
			if(seesPlayer)
			{
				if(!currPlayerTile.equals(currTile))
					currTargetNode=findPath(currTile, currPlayerTile);
				else if(getImage()!=idleImage)
				{
					setImage(idleImage);
					turnTowardsGlobalLocation(wi.getPlayerPosition().x, wi.getPlayerPosition().y);
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
						currTargetNode=findPath(currTile, new Point(x, y));
						if(currTargetNode!=null)
							break;
					}	
				}

				tsa=new TargetShowActor();
				getWorld().addObject(tsa, x*TILE_SIZE+TILE_SIZE/2, y*TILE_SIZE+TILE_SIZE/2);
			}
		}
		else
		{
			if(seesPlayer&&!sawPlayer)
			{
				//Sees the player - didn't see him in the last tick
				//We can see the player now - CHASE HIM!
				currTargetNode=findPath(currTile, currPlayerTile);
				lastPlayerTile=currPlayerTile;
				encounterSound.play();
				if(tsa!=null)
					getWorld().removeObject(tsa);
			}
			else if(seesPlayer)
			{
				if(!currPlayerTile.equals(lastPlayerTile))
				{
					//As long as we see him, always go straight to the player
					currTargetNode=findPath(currTile, currPlayerTile);
					lastPlayerTile=currPlayerTile;
					if(tsa!=null)
						getWorld().removeObject(tsa);
				}
			}
		}

		if(currTargetNode!=null)
		{
			turnTowardsGlobalLocation(currTargetNode.x*TILE_SIZE+TILE_SIZE/2, currTargetNode.y*TILE_SIZE+TILE_SIZE/2);
			for(int i=0;i<stepsPerTick;i++)
			{
				move(1);
				if(squaredDistance(currTargetNode.x*TILE_SIZE+TILE_SIZE/2, currTargetNode.y*TILE_SIZE+TILE_SIZE/2, getGlobalX(), getGlobalY())<=REACHED_TARGET_DISTANCE_SQUARED)
				{
					currTargetNode=currTargetNode.prev;
					if(currTargetNode==null)
						break;
					turnTowardsGlobalLocation(currTargetNode.x*TILE_SIZE+TILE_SIZE/2, currTargetNode.y*TILE_SIZE+TILE_SIZE/2);
				}	
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

	private int squaredDistance(int x1, int y1, int x2, int y2)
	{
		int distX=x1-x2;
		int distY=y1-y2;
		return (distX*distX)+(distY*distY);
	}

	private Node findPath(Point start, Point end)
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
				System.out.println("Couldn't find a way");
				break;
			}
		}

		return endNode.prev;
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
