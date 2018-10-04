package Utils;

import java.util.ArrayList;
import java.util.HashSet;

import models.Point;

public class ColorPair implements Comparable<ColorPair>
{
	int pairID;
	int edgeSize;
	int color1, color2;
	float domination;
	public HashSet<Point> points1;
	public HashSet<Point> points2;
	public HashSet<Point> largestComponent;
	public ArrayList<Point> allPoints;

	public int getPairID()
	{
		return pairID;
	}

	public void setPairID(int pairID)
	{
		this.pairID = pairID;
	}

	public int getEdgeSize()
	{
		return edgeSize;
	}

	public void setEdgeSize(int edgeSize)
	{
		this.edgeSize = edgeSize;
	}

	public void incredemntEdgeSize()
	{
		edgeSize++;
	}

	@Override
	public int compareTo(ColorPair a2)
	{
		return edgeSize - a2.getEdgeSize();

	}

	public void setValues(int i, int j, int k)
	{
		pairID = i;
		color1 = j;
		color2 = k;
	}

	public void generateFullBipartite(HashSet<Point> points1, HashSet<Point> points2)
	{
		allPoints = new ArrayList<>();
		this.points1 = new HashSet<>();
		this.points2 = new HashSet<>();
		largestComponent = new HashSet<>();
		this.points1.addAll(points1);
		this.points2.addAll(points2);
		allPoints.addAll(points1);
		allPoints.addAll(points2);

		for (Point point : points1)
		{
			point.initilaizeBipartiteNeigbhor();
			ArrayList<Point> neigbhorPoints = point.getNeighbourPoints();
			for (Point neighbor : neigbhorPoints)
			{
				if (neighbor.getColor() == color2)
				{
					point.addToBipartite(neighbor, color2);
				}
			}
		}
		for (Point point : points2)
		{
			point.initilaizeBipartiteNeigbhor();
			ArrayList<Point> neigbhorPoints = point.getNeighbourPoints();
			for (Point neighbor : neigbhorPoints)
			{
				if (neighbor.getColor() == color1)
				{
					point.addToBipartite(neighbor, color1);
				}
			}
		}
	}

}
