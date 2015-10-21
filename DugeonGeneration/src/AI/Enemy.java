package AI;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import DungeonGeneration.MapField;
import greenfoot.Actor;
import greenfoot.GreenfootImage;

//TODO: Make this abstract, when done with testing
public /*abstract*/ class Enemy extends Actor implements IDamageable
{

	protected double velocity = -1;
	Weapon weapon = null;
	protected int value = -1;
	protected int hp = -1;
	protected int viewRange = -1;
	private Node currTargetNode;

	public void damage(int dmg)
	{
		hp -= dmg;
		if (hp <= 0)
		{
			IWorldInterfaceForAI wi = (IWorldInterfaceForAI) getWorld();
			if (wi != null)
				wi.addPlayerScore(value);
			else
				System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");
		}
	}

	@Override
	public void act()
	{

	}
	
	
	//TODO: Make this private again when done with testing
	public Node findPath(Point start, Point end)
	{
		IWorldInterfaceForAI wi = (IWorldInterfaceForAI) getWorld();
		if (wi == null)
			System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");

		Queue<Node>path=new LinkedList<Node>(); 
		ArrayList<Node>closedList=new ArrayList<Node>();
		ArrayList<Node>openList=new ArrayList<Node>();

		//MapField[][] map=wi.getMap();
		Random r=new Random();
		int width=50;
		int height=50;
		MapField[][] map=new MapField[width][height];
		/*
		 * Create Map, just for testing purposes
		 */
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				map[i][j]=new MapField(r.nextInt(100)<95);
			}
		}
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				if(map[i][j].walkable())
					System.out.print("#");
				else
					System.out.print("-");
			}
		}

		if(!map[end.x][end.y].walkable())
			return null;

		openList.add(new Node(start.distance(end),0, end.x, end.y, null));

		Node endNode=null;
		while(endNode==null)
		{
			endNode=step(closedList, openList, map, start, end);
		}

		return endNode;
	}

	private Node step(ArrayList<Node>closedList, ArrayList<Node>openList,MapField[][] map, Point start, Point end)
	{
		Node closest=openList.get(0);
		for(Node node:openList)
		{
			if(start.distance(new Point(node.x, node.y))<start.distance(new Point(closest.x, closest.y)))
			{
				closest=node;
			}
		}

		if(closest.x==end.x&&closest.y==end.y)
		{
			return closest;
		}
		int x=-1,y=-1;
		for(int i=0;i<4;i++)
		{
			switch(i)
			{
			case 0:
			{
				x=closest.x-1;
				y=closest.y;
			}
			case 1:
			{
				x=closest.x+1;
				y=closest.y;
			}
			case 2:
			{
				x=closest.x;
				y=closest.y-1;
			}
			case 3:
			{
				x=closest.x;
				y=closest.y+1;
			}
			}
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
