import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

import Utils.DrawUtil;
import models.Cell;
import models.Degree;
import models.Point;

public class MainThread
{
	/*
	 * According to the benchmark table in section III, you are provided the
	 * following input data:
	 * • N = number of sensors (vertices)
	 * • A = expected density (average degree) of the sensor network
	 * • T = network topology (unit square, unit disk, or unit sphere).
	 */

	static DrawUtil draw = new DrawUtil();
	private static ArrayList<Point> points;
	private static int numberOfSensors;
	private static int averageDensity;
	private static int topology;
	private static double sensorRadius;
	private static Cell cell[][];
	static int unitSizeForSquare = 1000;
	static int unitSizeForCircle = 500;

	private static int noOfCellRowsCoulmns;
	private static long startTime;
	private static Degree[] degrees = new Degree[250];
	private static float totalTime;

	public static void main(String[] args)
	{
		InputValuesFromsUser();
		startTime("Generating Points");
		if (topology == 1)
		{
			calculateRadiusforSquare();
			generatePointsForSquare();
		}
		else
		{
			calculateRadiusforCircle();
			generatePointsForCircle();
		}
		endTime();
		initiliazeArray();
		startTime("Generating Neighbour For Points");
		generateNeighbourForPoints();
		endTime();
		initializeGraph();

	}

	public static void endTime()
	{
		float endTime = (float) (System.currentTimeMillis() - startTime) / 1000;
		totalTime = totalTime + endTime;
		System.out.println("Ended in " + endTime + " seconds\n");
	}

	public static void startTime(String string)
	{
		System.out.println("starting " + string + "...");
		startTime = System.currentTimeMillis();
	}

	private static void initiliazeArray()
	{
		for (int i = 0; i < degrees.length; i++)
		{
			degrees[i] = new Degree();
		}
	}

	public static void generateNeighbourForPoints()
	{
		for (Point point : points)
		{
			int X = (int) (point.getX() / sensorRadius);
			int Y = (int) (point.getY() / sensorRadius);
			Y++;
			ArrayList<Point> neigbhorPoints = cell[X][Y].getPoints();
			for (Point neigbhorPoint : neigbhorPoints)
			{
				if ((((float) Math.sqrt((point.getX() - neigbhorPoint.getX()) * (point.getX() - neigbhorPoint.getX())
						+ (point.getY() - neigbhorPoint.getY()) * (point.getY() - neigbhorPoint.getY())))) <= sensorRadius)
				{
					neigbhorPoint.addNeighbour(point);
				}

			}

			neigbhorPoints = cell[X][Y + 1].getPoints();
			for (Point neigbhorPoint : neigbhorPoints)
			{
				if ((((float) Math.sqrt((point.getX() - neigbhorPoint.getX()) * (point.getX() - neigbhorPoint.getX())
						+ (point.getY() - neigbhorPoint.getY()) * (point.getY() - neigbhorPoint.getY())))) <= sensorRadius)
				{
					point.addNeighbour(neigbhorPoint);
					neigbhorPoint.addNeighbour(point);
				}
			}
			neigbhorPoints = cell[X + 1][Y - 1].getPoints();
			for (Point neigbhorPoint : neigbhorPoints)
			{
				if ((((float) Math.sqrt((point.getX() - neigbhorPoint.getX()) * (point.getX() - neigbhorPoint.getX())
						+ (point.getY() - neigbhorPoint.getY()) * (point.getY() - neigbhorPoint.getY())))) <= sensorRadius)
				{
					point.addNeighbour(neigbhorPoint);
					neigbhorPoint.addNeighbour(point);
				}
			}
			neigbhorPoints = cell[X + 1][Y].getPoints();
			for (Point neigbhorPoint : neigbhorPoints)
			{
				if ((((float) Math.sqrt((point.getX() - neigbhorPoint.getX()) * (point.getX() - neigbhorPoint.getX())
						+ (point.getY() - neigbhorPoint.getY()) * (point.getY() - neigbhorPoint.getY())))) <= sensorRadius)
				{
					point.addNeighbour(neigbhorPoint);
					neigbhorPoint.addNeighbour(point);
				}
			}
			neigbhorPoints = cell[X + 1][Y + 1].getPoints();
			for (Point neigbhorPoint : neigbhorPoints)
			{
				if ((((float) Math.sqrt((point.getX() - neigbhorPoint.getX()) * (point.getX() - neigbhorPoint.getX())
						+ (point.getY() - neigbhorPoint.getY()) * (point.getY() - neigbhorPoint.getY())))) <= sensorRadius)
				{
					point.addNeighbour(neigbhorPoint);
					neigbhorPoint.addNeighbour(point);
				}
			}
		}
	}

	// If Topology = unit square
	// A = N * π * R * R
	private static void calculateRadiusforSquare()
	{
		sensorRadius = Math.sqrt(averageDensity / (numberOfSensors * Math.PI));
		sensorRadius = sensorRadius * unitSizeForSquare;
		noOfCellRowsCoulmns = (int) (unitSizeForSquare / sensorRadius);
		cell = new Cell[noOfCellRowsCoulmns + 2][noOfCellRowsCoulmns + 3];
		initializeCell(noOfCellRowsCoulmns + 2, noOfCellRowsCoulmns + 3);
	}

	// If Topology = unit disk
	// A = N * R * R
	private static void calculateRadiusforCircle()
	{
		sensorRadius = Math.sqrt((double) averageDensity / (double) numberOfSensors);
		sensorRadius = sensorRadius * unitSizeForCircle;
		noOfCellRowsCoulmns = (int) (unitSizeForSquare / sensorRadius);
		cell = new Cell[noOfCellRowsCoulmns + 2][noOfCellRowsCoulmns + 3];
		initializeCell(noOfCellRowsCoulmns + 2, noOfCellRowsCoulmns + 3);
	}

	private static void InputValuesFromsUser()
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the number of sensors");
		numberOfSensors = sc.nextInt();
		System.out.println("Enter averege density of vertices");
		averageDensity = sc.nextInt();
		System.out.println("Enter topology# :\n 1. Square.  2.Circle  ");
		topology = sc.nextInt();
		sc.close();
	}

	private static void generatePointsForSquare()
	{
		Random r = new Random();
		points = new ArrayList<>();
		for (int i = 0; i < numberOfSensors; i++)
		{
			int X = r.nextInt(unitSizeForSquare);
			int Y = r.nextInt(unitSizeForSquare);
			Point p = new Point(X, Y);
			p.label = i + 1;
			points.add(p);
			SortIntoCell(p);
		}
	}

	private static void generatePointsForCircle()
	{

		Random r = new Random();
		points = new ArrayList<>();
		for (int i = 0; i < numberOfSensors; i++)
		{
			int X = r.nextInt(unitSizeForSquare);
			int Y = r.nextInt(unitSizeForSquare);
			if (Math.hypot(X - unitSizeForCircle, Y - unitSizeForCircle) < unitSizeForCircle)
			{
				Point p = new Point(X, Y);
				points.add(p);
				SortIntoCell(p);
			}
			else
			{
				i--;
			}

		}
	}

	private static void SortIntoCell(Point p)
	{
		int X = (int) (p.getX() / sensorRadius);
		int Y = (int) (p.getY() / sensorRadius);
		cell[X][Y + 1].getPoints().add(p);
	}

	private static void initializeCell(int rowSize, int columnSize)
	{
		for (int i = 0; i < rowSize; i++)
		{
			for (int j = 0; j < columnSize; j++)
			{
				cell[i][j] = new Cell();
			}
		}
	}

	private static void initializeGraph()
	{
		JFrame frame = new JFrame("WIRELESS SENSOR NETWORK");
		draw.setRemainingData(totalTime, cell, averageDensity, sensorRadius, topology, numberOfSensors, points);
		frame.getContentPane().add(draw);
		frame.setSize(1825, 1045);
		frame.toFront();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setResizable(false);
	}
}
