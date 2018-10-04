package models;

import java.util.ArrayList;
import java.util.HashSet;

public class Point
{
	private int X;
	private int Y;
	private int degreeWhenDeleted;
	private int color = -1;
	public boolean visited = false;
	private ArrayList<Point> neighbourPoints = new ArrayList<>();
	private ArrayList<Point> neighbourPointsForDeletion = new ArrayList<>();

	private HashSet<Point> bipartiteNeighbourPoints0 = new HashSet<>();
	private HashSet<Point> bipartiteNeighbourPoints1 = new HashSet<>();
	private HashSet<Point> bipartiteNeighbourPoints2 = new HashSet<>();
	private HashSet<Point> bipartiteNeighbourPoints3 = new HashSet<>();
	private HashSet<Point> bipartiteNeighbourPoints4 = new HashSet<>();
	private HashSet<Point> bipartiteNeighbourPoints5 = new HashSet<>();

	public HashSet<Point> getBipartiteNeighbourPoints(int i)
	{
		switch (i)
		{
		case 0:
			return bipartiteNeighbourPoints0;
		case 1:
			return bipartiteNeighbourPoints1;
		case 2:
			return bipartiteNeighbourPoints2;
		case 3:
			return bipartiteNeighbourPoints3;
		case 4:
			return bipartiteNeighbourPoints4;
		case 5:
			return bipartiteNeighbourPoints5;

		}
		return null;

	}

	public int label;

	public ArrayList<Point> getNeighbourPointsForDeletion()
	{
		return neighbourPointsForDeletion;
	}

	public void setNeighbourPointsForDeletion(ArrayList<Point> neighbourPointsForDeletion)
	{
		this.neighbourPointsForDeletion = neighbourPointsForDeletion;
	}

	public int getColor()
	{
		return color;
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	public int getDegreeWhenDeleted()
	{
		return degreeWhenDeleted;
	}

	public void setDegreeWhenDeleted(int degreeWhenDeleted)
	{
		this.degreeWhenDeleted = degreeWhenDeleted;
	}

	public ArrayList<Point> getNeighbourPoints()
	{
		return neighbourPoints;
	}

	public void setNeighbourPoints(ArrayList<Point> neighbourPoints)
	{
		this.neighbourPoints = neighbourPoints;
	}

	public void addNeighbour(Point p)
	{
		neighbourPoints.add(p);
	}

	public Point(int x2, int y2)
	{
		X = x2;
		Y = y2;
	}

	public int getX()
	{
		return X;
	}

	public void setX(int x)
	{
		X = x;
	}

	public int getY()
	{
		return Y;
	}

	public void setY(int y)
	{
		Y = y;
	}

	public HashSet<Integer> getNeigbhorColorSet()
	{
		HashSet<Integer> colorArray = new HashSet<>();
		for (Point neigbhor : neighbourPoints)
		{
			colorArray.add(neigbhor.getColor());
		}
		return colorArray;
	}

	public void initilaizeBipartiteNeigbhor()
	{

	}

	public void addToBipartite(Point point, int color)
	{
		if (!getBipartiteNeighbourPoints(color).contains(point))
		{
			getBipartiteNeighbourPoints(color).add(point);
		}
	}

}
