package models;

import java.util.ArrayList;

public class Cell
{
	ArrayList<Point> points = new ArrayList<>();

	public ArrayList<Point> getPoints()
	{
		if (points == null)
		{
			points = new ArrayList<>();
		}
		return points;
	}

}
