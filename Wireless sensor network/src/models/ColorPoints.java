package models;

import java.util.HashSet;
import java.util.Iterator;

public class ColorPoints
{
	private int values;
	private HashSet<Point> degreePoints;
	private int edgeNumber;

	public HashSet<Point> getDegreePoints()
	{
		return degreePoints;
	}

	public void setDegreePoints(HashSet<Point> degreePoints)
	{
		this.degreePoints = degreePoints;
	}

	public ColorPoints()
	{
		values = 0;
		degreePoints = new HashSet<>();
	}

	public ColorPoints(Point point)
	{
		values = 1;
		degreePoints = new HashSet<>();
		addPoint(point);

	}

	public double getValues()
	{
		return values;
	}

	public void setValues(int values)
	{
		this.values = values;
	}

	public void addPoint(Point point)
	{

		degreePoints.add(point);
		values++;
	}

	public boolean removePoint(Point point)
	{
		if (degreePoints.remove(point))
		{
			values--;
			return true;
		}
		return false;
	}

	public boolean isPointsEmpty()
	{
		return degreePoints.isEmpty();
	}

	public boolean containsPoints(Point point)
	{
		return degreePoints.contains(point);

	}

	public Point getNextPoint()
	{
		Iterator<Point> iterator = degreePoints.iterator(); 
		if (iterator.hasNext())
		{
			return iterator.next();
		}
		else
		{
			return null;
		}

	}

	public int getEdgeNumber()
	{
		return edgeNumber;
	}

	public void setEdgeNumber(int edgeNumber)
	{
		this.edgeNumber = edgeNumber;
	}

}
