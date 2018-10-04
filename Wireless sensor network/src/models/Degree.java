package models;

import java.util.ArrayList;

public class Degree
{
	private int values;
	private ArrayList<Point> degreePoints;
	private int size;

	public ArrayList<Point> getDegreePoints()
	{
		return degreePoints;
	}

	public void setDegreePoints(ArrayList<Point> degreePoints)
	{
		this.degreePoints = degreePoints;
	}

	public Degree()
	{
		values = 0;
		degreePoints = new ArrayList<Point>();
	}

	public Degree(Point point)
	{
		values = 1;
		degreePoints = new ArrayList<Point>();
		this.addPoint(point);

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
		if (!isPointsEmpty())
		{
			return degreePoints.get(0);
		}
		else
			return null;

	}

	public int getSize()
	{
		// TODO Auto-generated method stub
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

}
