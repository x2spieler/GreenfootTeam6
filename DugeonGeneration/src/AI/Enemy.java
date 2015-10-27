package AI;
import java.awt.Point;
import java.util.ArrayList;

import DungeonGeneration.DungeonGenerator;
import DungeonGeneration.MapField;

public /*abstract*/ class Enemy /*extends Actor*/ implements IDamageable
{

	protected double velocity = -1;
	Weapon weapon = null;
	protected int value = -1;
	protected int hp = -1;
	protected int viewRange = -1;
	boolean cantFindWay=false;
	private Node currTargetNode=null;

	public Enemy()
	{
		
	}

	public void damage(int dmg)
	{
		hp -= dmg;
		if (hp <= 0)
		{
			/*IWorldInterfaceForAI wi = (IWorldInterfaceForAI) getWorld();
			if (wi != null)
				wi.addPlayerScore(value);
			else
				System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");*/
		}
	}

	//@Override
	public void act()
	{

	}

	public Node findPath(Point start, Point end)
	{
		/*IWorldInterfaceForAI wi = (IWorldInterfaceForAI) getWorld();
		if (wi == null)
			System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");*/

		ArrayList<Node>closedList=new ArrayList<Node>();
		ArrayList<Node>openList=new ArrayList<Node>();

	//	MapField[][] map=wi.getMap();
		
		//For testing
		DungeonGenerator dungeonGen = new DungeonGenerator();
		
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
				if(map[i][j].walkable())
				{
					start.x=i;
					start.y=j;
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
				if(map[i][j].walkable())
				{
					end.x=i;
					end.y=j;
					br=true;
					break;
				}
			}
		}
		
		/*MapField[][] map=new MapField[height][width];
		Random r=new Random();
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				map[i][j]=new MapField(r.nextInt(100)<60);
			}
		}*/
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
		}

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

		Node n=endNode;
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
		}

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
			//3. Loop: 0, 1
			//4. Loop: 0, -1
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
