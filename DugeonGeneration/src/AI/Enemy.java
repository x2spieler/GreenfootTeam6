package AI;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.MapField;
import scrollWorld.ScrollActor;

public abstract class Enemy extends ScrollActor implements IDamageable
{

	protected int stepsPerTick = -1;
	protected Weapon weapon = null;
	protected int value = -1;
	protected int hp = -1;
	protected int viewRangeSquared = -1;		//In Pixels, not tiles
	private boolean cantFindWay=false;
	private boolean seesPlayer=false;
	private Node currTargetNode=null;
	private IWorldInterfaceForAI wi = null;
	private Point lastPlayerTile=null;
	private TargetShowActor tsa=null;
	private final int REACHED_TARGET_DISTANCE_SQUARED=64*64;
	private int tileSize=-1;

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
		if(wi==null)
		{
			wi = (IWorldInterfaceForAI) getWorld();
			tileSize=wi.getTileSize();
			if (wi == null)
			{
				System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");
				return;
			}
		}

		Point currPlayerTile=wi.getPlayerPosition();
		currPlayerTile.x/=tileSize;
		currPlayerTile.y/=tileSize;
		Point currTile=new Point(getGlobalX()/tileSize, getGlobalY()/tileSize);
		if(currTargetNode==null)
		{
			if(tsa!=null){
				getWorld().removeObject(tsa);
			}
			//TODO: Fix chasing the player
			seesPlayer=isInRangeOfPlayer();
			if(seesPlayer)
			{
				if(squaredDistance(getGlobalX(), getGlobalY(), currPlayerTile.x*tileSize, currPlayerTile.y*tileSize)>REACHED_TARGET_DISTANCE_SQUARED)
					currTargetNode=findPath(currTile, currPlayerTile);
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
						else
						{
							System.out.println("Start: "+getGlobalX()+" # "+getGlobalY());
							boolean ported=false;
							for(int k=x-2;k<x+3;k++)
							{
								for(int l=y-2;l<y+3;l++)
								{
									if(map[k][l].walkable())
									{
										setGlobalLocation(k*tileSize+tileSize/2, l*tileSize+tileSize/2);
										ported=true;
										break;
									}
								}
								if(ported)
									break;
							}
							if(!ported)
								setGlobalLocation(x*tileSize+tileSize/2, y*tileSize+tileSize/2);
							break;
						}
					}	
				}

				tsa=new TargetShowActor();
				getWorld().addObject(tsa, x*tileSize+tileSize/2, y*tileSize+tileSize/2);
			}
		}
		else
		{
			//TODO: Fix chasing the player
			/*
			if(!seesPlayer&&isInRangeOfPlayer())
			{
				//Sees the player - didn't see him in the last tick
				//We can see the player now - CHASE HIM!
				currTargetNode=findPath(currTile, currPlayerTile);
				lastPlayerTile=currPlayerTile;
				Greenfoot.playSound("encounterPlayer.wav");
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
			}*/
		}

		if(currTargetNode!=null)
		{
			turnTowardsGlobalLocation(currTargetNode.x*tileSize+tileSize/2, currTargetNode.y*tileSize+tileSize/2);
			for(int i=0;i<stepsPerTick;i++)
			{
				move(1);
				if(squaredDistance(currTargetNode.x*tileSize+tileSize/2, currTargetNode.y*tileSize+tileSize/2, getGlobalX(), getGlobalY())<=REACHED_TARGET_DISTANCE_SQUARED)
				{
					currTargetNode=currTargetNode.prev;
					if(currTargetNode==null)
						break;
					turnTowardsGlobalLocation(currTargetNode.x*tileSize+tileSize/2, currTargetNode.y*tileSize+tileSize/2);
				}
			}
		}
	}

	private int squaredDistance(int x1, int y1, int x2, int y2)
	{
		int distX=x1-x2;
		int distY=y1-y2;
		return (distX*distX)+(distY*distY);
	}

	public Node findPath(Point start, Point end)
	{
		ArrayList<Node>closedList=new ArrayList<Node>();
		ArrayList<Node>openList=new ArrayList<Node>();

		MapField[][] map=wi.getMap();

		//For testing
		/*DungeonGenerator dungeonGen = new DungeonGenerator();
		dungeonGen.clearMap();
		dungeonGen.generateRooms();
		dungeonGen.placeRooms();
		dungeonGen.buildPaths();
		dungeonGen.showMap();
		System.out.println();System.out.println();System.out.println();
		MapField[][] map=dungeonGen.getMap();

		int width=DungeonGenerator.MAP_WIDTH;
		int height=DungeonGenerator.MAP_HEIGHT;

		boolean br=false;
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if(map[j][i].walkable())
				{
					start.x=j;
					start.y=i;
					br=true;
					break;
				}
			}
		}
		br=false;
		for(int i=height-1;i>=0;i--)
		{
			for(int j=width-1;j>=0;j--)
			{
				if(map[j][i].walkable())
				{
					end.x=j;
					end.y=i;
					br=true;
					break;
				}
			}
		}*/
		/*MapField[][] map=new MapField[height][width];
		Random r=new Random();
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				map[i][j]=new MapField(r.nextInt(100)<60);
			}
		}
		String[][] mp=new String[height][width];
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if(map[i][j].walkable())
					mp[i][j]="#";
				else
					mp[i][j]=".";
			}
		}*/

		if(!map[end.x][end.y].walkable())
		{
			System.out.println("Target field isn't walkable");
			return null;
		}

		openList.add(new Node(start.distance(end),0, end.x, end.y, null));

		Node endNode=null;
		while(endNode==null)
		{
			endNode=step(closedList, openList, map, start);
			if(cantFindWay||!map[end.x][end.y].walkable())
			{
				cantFindWay=false;
				System.out.println("Couldn't find a way");
				break;
			}
		}
		currTargetNode=endNode;

		/*Node n=endNode;
		while(n!=null)
		{
			mp[n.x][n.y]="0";
			n=n.prev;
		}

		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				System.out.print(mp[j][i]);
			}
			System.out.println();
		}*/

		return endNode;
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
			if(start.distance(new Point(node.x, node.y))<start.distance(new Point(closest.x, closest.y)))
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
			//TODO: Bug finden
			x=closest.x+addX;	
			y=closest.y+addY;
			if(x<0||y<0||x>=map.length||y>=map[0].length)
				continue;
			if(map[x][y].walkable()&&!closedList.contains(new Node(x, y)))
			{
				int indx=openList.indexOf(new Node(x, y));
				Node currNode=new Node(distance(start, x,y)+closest.prevEdgeCost+1,closest.prevEdgeCost+1, x,y, closest);
				if(indx==-1)
				{
					openList.add(currNode);
				}
				else
				{
					if(currNode.cost<openList.get(indx).cost)
					{
						openList.set(indx, currNode);
					}
				}
			}
		}


		openList.remove(closest);
		closedList.add(closest);

		return null;
	}

	private double distance(Point start, int x, int y)
	{
		return start.distance(new Point(x,y));
	}

	class Node
	{
		double cost;
		double prevEdgeCost;
		int x;
		int y;
		Node prev;

		public Node(double cost,double prevEdgeCost, int x, int y, Node prev)
		{
			this.prevEdgeCost=prevEdgeCost;
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
