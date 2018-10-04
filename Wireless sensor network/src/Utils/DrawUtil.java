package Utils;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JFrame;

import models.Cell;
import models.ColorPoints;
import models.Degree;
import models.Point;

// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

public class DrawUtil extends Canvas
{
	private static final long serialVersionUID = 1L;
	private ArrayList<Point> points;
	private Point maxPoint;
	private int maxDegree = 0;
	private int sumDegree = 0;
	private int maxColor = 0;
	private Point minPoint;
	private int minDegree = 1000;
	private Degree degrees[];
	private ColorPair colorPairList[];
	private int expectedAverageDegree;
	private double sensorRadius;
	private long startTime;
	private float totalTime;
	private int topology;
	private ArrayList<Point> coloredList = new ArrayList<>();
	private Color[] colorSet =
	{ Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };
	private int terminalClique = -1;
	private int maxDegreeWhenDeleted = 0;
	private int numberOfSensors;
	private ColorPoints[] colorPoints;
	private JFrame DegreeDeletedframe;
	private Histogram histogram;
	private JFrame Colorframe;
	static int unitSizeForSquare = 1000;
	static int unitSizeForCircle = 500;
	static HashSet<String> results = new HashSet<>();

	public void setRemainingData(float totalTime, Cell[][] cell, int expectedAverageDegree, double sensorRadius, int topology, int numberOfSensors, ArrayList<Point> points2)
	{
		this.expectedAverageDegree = expectedAverageDegree;
		this.sensorRadius = sensorRadius;
		this.topology = topology;
		points = points2;
		this.numberOfSensors = numberOfSensors;
		this.totalTime = totalTime;
	}

	public void endTime(Graphics g)
	{

		float endTime = (float) (System.currentTimeMillis() - startTime) / 1000;
		g.setColor(Color.BLACK);
		g.setFont(new Font("default", Font.PLAIN, 24));
		g.drawString("Ended in " + endTime + " seconds", 1200, 270);
		totalTime = totalTime + endTime;
		System.out.println("Ended in " + endTime + " seconds\n");
	}

	public void startTime(String string, Graphics g)
	{

		g.setFont(new Font("default", Font.PLAIN, 24));
		g.setColor(Color.BLACK);
		g.clearRect(1199, 200, 100000, 100000);
		g.drawString("starting " + string + "...", 1200, 235);
		System.out.println("starting " + string + "...");
		startTime = System.currentTimeMillis();
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setFont(new Font("default", Font.PLAIN, 24));
		plotInitialPoints(g);

		startTime("Plotting Edges", g);
		plotEdges(g);
		initializeDegrees();

		startTime("generating Degrees", g);
		generateMinMaxAndDegrees();
		endTime(g);

		plotMinMax(g);
		testWait(2000000000);

		initializeColorPoints();
		showHistogram();

		startTime("Generating Colors & related data", g);
		generateColors();
		endTime(g);

		removePointsDegreeWise(g);

		startTime("Plotting Points", g);
		plotPoints(g);
		endTime(g);

		startTime("Plotting Min Max", g);
		plotMinMax(g);
		endTime(g);

		startTime("Generating Bipartite Graphs", g);
		generateBipartiteGraphs();
		endTime(g);

		startTime("Deleting Minor Components", g);
		deleteMinorComponents();
		endTime(g);

		startTime("Removing Tail", g);
		removeTail();
		endTime(g);

		startTime("fetching Two Largest Bipartite Graphs", g);
		fetchTwoLargestBipartiteGraphs();
		endTime(g);

		startTime("Calculating Domination", g);
		CalculateDomination();
		endTime(g);

		g.clearRect(1199, 200, 100000, 100000);
		plotBipartite(g);
		plotPoints(g);
		plotMinMax(g);
		g.clearRect(1199, 200, 100000, 100000);
		showColorGraph();
		showLineGraph();
		showDataInConsole();
		g.clearRect(1199, 200, 100000, 100000);
		plotData(g);
	}

	private void plotBipartite(Graphics g)
	{
		int count = 1;

		for (ColorPair element : colorPairList)
		{
			g.clearRect(0, 0, 1500, 1500);
			g.setFont(new Font("default", Font.PLAIN, 24));
			g.setColor(Color.BLACK);
			g.clearRect(1199, 200, 100000, 100000);
			g.drawString("Plotting Bipartite " + count++ + "...", 1200, 235);
			for (Point point : element.largestComponent)
			{
				g.setColor(colorSet[point.getColor()]);
				g.fillRoundRect(point.getX() - 3, point.getY() - 3, 6, 6, 16, 16);
				HashSet<Point> neigbhorPoints;

				neigbhorPoints = point.getBipartiteNeighbourPoints(element.color2);

				for (Point neighbor : neigbhorPoints)
				{
					g.setColor(colorSet[neighbor.getColor()]);
					g.fillRoundRect(neighbor.getX() - 3, neighbor.getY() - 3, 6, 6, 16, 16);
					g.setColor(Color.BLACK);
					g.drawLine(point.getX(), point.getY(), neighbor.getX(), neighbor.getY());
					testWait(500);

				}
			}
			testWait(500000000);
		}
		g.clearRect(0, 0, 1500, 1500);
	}

	private void removePointsDegreeWise(Graphics g)
	{
		g.setColor(getBackground());

		if (numberOfSensors > 8000)
		{
			for (Point point : coloredList)
			{
				g.fillRoundRect(point.getX() - 3, point.getY() - 3, 6, 6, 16, 16);
				testWait(500);
			}
		}
		else
		{
			for (Point point : coloredList)
			{
				g.fillRoundRect(point.getX() - 3, point.getY() - 3, 6, 6, 16, 16);
				for (Point neighbor : point.getNeighbourPoints())
				{
					g.drawLine(point.getX(), point.getY(), neighbor.getX(), neighbor.getY());
				}
				testWait(500);
			}
		}

	}

	public void testWait(long INTERVAL)
	{
		long start = System.nanoTime();
		long end = 0;
		do
		{
			end = System.nanoTime();
		} while (start + INTERVAL >= end);
	}

	private void plotInitialPoints(Graphics g)
	{
		g.setColor(Color.BLACK);
		for (int i = 0; i < points.size(); i++)
		{
			g.fillRoundRect(points.get(i).getX() - 3, points.get(i).getY() - 3, 6, 6, 16, 16);
			testWait(500);
		}

	}

	private void CalculateDomination()
	{
		HashSet<Point> hs = new HashSet<>();
		for (Point point : colorPairList[5].largestComponent)
		{
			for (Point neigbhor : point.getNeighbourPoints())
			{
				hs.add(neigbhor);
			}
		}
		colorPairList[5].domination = (float) hs.size() / numberOfSensors;
		hs.clear();
		for (Point point : colorPairList[4].largestComponent)
		{
			for (Point neigbhor : point.getNeighbourPoints())
			{
				hs.add(neigbhor);
			}
		}
		colorPairList[4].domination = (float) hs.size() / numberOfSensors;
	}

	private void removeTail()
	{
		for (ColorPair element : colorPairList)
		{
			Degree[] bipartiteDegrees = new Degree[10];
			for (int i1 = 0; i1 < bipartiteDegrees.length; i1++)
			{
				bipartiteDegrees[i1] = new Degree();
			}
			for (Point point : element.largestComponent)
			{
				if (point.getColor() == element.color1)
				{
					bipartiteDegrees[point.getBipartiteNeighbourPoints(element.color2).size()].addPoint(point);
				}
				else
				{
					bipartiteDegrees[point.getBipartiteNeighbourPoints(element.color1).size()].addPoint(point);
				}

			}

			while (bipartiteDegrees[0].getDegreePoints().size() != 0 || bipartiteDegrees[1].getDegreePoints().size() != 0)
			{
				int inDegree = 0;
				Point point = bipartiteDegrees[0].getNextPoint();
				if (point == null)
				{
					point = bipartiteDegrees[1].getNextPoint();
					inDegree = 1;
				}
				if (point == null)
				{
					break;
				}
				element.largestComponent.remove(point);
				bipartiteDegrees[inDegree].removePoint(point);
				if (point.getColor() == element.color1)
				{
					HashSet<Point> neigbhorPoints = point.getBipartiteNeighbourPoints(element.color2);
					for (Point neighbor : neigbhorPoints)
					{
						bipartiteDegrees[neighbor.getBipartiteNeighbourPoints(element.color1).size()].removePoint(neighbor);
						neighbor.getBipartiteNeighbourPoints(element.color1).remove(point);
						bipartiteDegrees[neighbor.getBipartiteNeighbourPoints(element.color1).size()].addPoint(neighbor);

					}
				}
				else
				{
					HashSet<Point> neigbhorPoints = point.getBipartiteNeighbourPoints(element.color1);
					for (Point neighbor : neigbhorPoints)
					{
						bipartiteDegrees[neighbor.getBipartiteNeighbourPoints(element.color2).size()].removePoint(neighbor);
						neighbor.getBipartiteNeighbourPoints(element.color2).remove(point);
						bipartiteDegrees[neighbor.getBipartiteNeighbourPoints(element.color2).size()].addPoint(neighbor);

					}
				}
			}
		}
	}

	private void generateBipartiteGraphs()
	{
		colorPairList = new ColorPair[6];

		for (int i = 0; i < colorPairList.length; i++)
		{
			colorPairList[i] = new ColorPair();
		}

		colorPairList[0].setValues(0, 0, 1);
		colorPairList[1].setValues(1, 0, 2);
		colorPairList[2].setValues(2, 0, 3);
		colorPairList[3].setValues(3, 1, 2);
		colorPairList[4].setValues(4, 1, 3);
		colorPairList[5].setValues(5, 2, 3);

		for (ColorPair element : colorPairList)
		{
			element.generateFullBipartite(colorPoints[element.color1].getDegreePoints(), colorPoints[element.color2].getDegreePoints());
		}
	}

	private void deleteMinorComponents()
	{
		for (int i = 0; i < colorPairList.length; i++)
		{
			while (!colorPairList[i].allPoints.isEmpty())
			{
				HashSet<Point> largestComponent = new HashSet<>();
				Point point = colorPairList[i].allPoints.get(0);

				Queue<Point> queue = new LinkedList<>();
				queue.add(point);
				while (!queue.isEmpty())
				{
					Point node = queue.remove();
					largestComponent.add(node);
					colorPairList[i].allPoints.remove(node);
					Point child = null;
					while ((child = getUnvisitedChildNode(node, i)) != null)
					{
						child.visited = true;
						queue.add(child);
					}
				}

				for (Point component : largestComponent)
				{
					component.visited = false;
				}
				if (largestComponent.size() > colorPairList[i].largestComponent.size())
				{
					colorPairList[i].largestComponent = largestComponent;
				}
			}

		}
	}

	private Point getUnvisitedChildNode(Point node, int i)
	{
		if (node.getColor() == colorPairList[i].color1)
		{
			for (Point point : node.getBipartiteNeighbourPoints(colorPairList[i].color2))
			{
				if (!point.visited)
				{
					return point;
				}
			}
		}
		else
		{
			for (Point point : node.getBipartiteNeighbourPoints(colorPairList[i].color1))
			{
				if (!point.visited)
				{
					return point;
				}
			}
		}
		return null;
	}

	private void plotFinalBipartiteGraph()
	{
		plotGraph(colorPairList[5], 1);
		plotGraph(colorPairList[4], 2);
	}

	private void fetchTwoLargestBipartiteGraphs()
	{
		for (int i = 0; i < colorPairList.length; i++)
		{
			int totalEdges = 0;
			HashSet<Point> visitedPoints = new HashSet<>();
			for (Point point : colorPairList[i].largestComponent)
			{
				if (point.getColor() == colorPairList[i].color1)
				{
					for (Point neibghor : point.getBipartiteNeighbourPoints(colorPairList[i].color2))
					{
						if (!visitedPoints.contains(neibghor))
						{
							totalEdges++;
						}
					}
				}
				else
				{
					for (Point neibghor : point.getBipartiteNeighbourPoints(colorPairList[i].color1))
					{
						if (!visitedPoints.contains(neibghor))
						{
							totalEdges++;
						}
					}
				}
				visitedPoints.add(point);
			}
			colorPairList[i].edgeSize = totalEdges;
		}
		Arrays.sort(colorPairList);
	}

	private void plotGraph(ColorPair colorPairList, int i)
	{
		JFrame frame = new JFrame("Bipartite Graphs " + i);
		BipartitePlot plot = new BipartitePlot();
		plot.setRemainingData(colorPairList);
		frame.getContentPane().add(plot);
		frame.setSize(1825, 1045);
		frame.setVisible(true);
		frame.toBack();
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
	}

	private void showColorGraph()
	{
		List<Integer> scores1 = new ArrayList<>();
		for (ColorPoints colorPoint : colorPoints)
		{
			if (colorPoint.getDegreePoints().size() > 0)
			{
				scores1.add(colorPoint.getDegreePoints().size());
			}
		}
		SingleLineGraph mainPanel = new SingleLineGraph(scores1, colorPoints[0].getDegreePoints().size());

		Colorframe = new JFrame("Color Line Graph");
		Colorframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Colorframe.setExtendedState(Colorframe.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		Colorframe.getContentPane().add(mainPanel);
		Colorframe.pack();
		Colorframe.toBack();
		Colorframe.setLocationByPlatform(true);
		Colorframe.setVisible(false);
	}

	private void initializeColorPoints()
	{
		colorPoints = new ColorPoints[maxDegree];
		for (int i = 0; i < colorPoints.length; i++)
		{
			colorPoints[i] = new ColorPoints();
			colorPoints[i].setEdgeNumber(i);
		}
	}

	private void initializeDegrees()
	{
		degrees = new Degree[220];
		for (int i = 0; i < degrees.length; i++)
		{
			degrees[i] = new Degree();
			degrees[i].setSize(i);
		}
	}

	private void generateMinMaxAndDegrees()
	{

		for (Point point : points)
		{
			point.getNeighbourPointsForDeletion().addAll(point.getNeighbourPoints());
			int size = point.getNeighbourPointsForDeletion().size();
			degrees[size].addPoint(point);
			if (maxDegree < size)
			{
				maxDegree = size;
				maxPoint = point;

			}
			if (minDegree > size)
			{
				minDegree = size;
				minPoint = point;
			}
			sumDegree = sumDegree + size;
		}
	}

	private void plotData(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g.setFont(new Font("default", Font.BOLD, 34));
		g.drawString("Benchmark !!!", 1200, 100);
		g2.setColor(Color.BLACK);
		g.setFont(new Font("default", Font.PLAIN, 24));
		if (topology == 1)
		{
			g.drawString("Sensor Radius (R) : " + sensorRadius / unitSizeForSquare, 1200, 200);
		}
		else
		{
			g.drawString("Sensor Radius (R) : " + sensorRadius / unitSizeForCircle, 1200, 200);
		}
		g.drawString("Number of Sensors (N) : " + points.size(), 1200, 235);
		g.drawString("Expected Average Degree : " + expectedAverageDegree, 1200, 270);
		g2.setColor(Color.GREEN);
		g.setFont(new Font("default", Font.BOLD, 26));
		g.drawString("Max degree = " + maxDegree, 1200, 400);
		g2.setColor(Color.RED);
		g.setFont(new Font("default", Font.BOLD, 26));
		g.drawString("Min degree = " + minDegree, 1200, 450);
		g.setFont(new Font("default", Font.PLAIN, 24));
		g2.setColor(Color.BLACK);
		g.drawString("Terminal Clique = " + terminalClique, 1200, 500);
		g.drawString("Max Degree When Deleted = " + maxDegreeWhenDeleted, 1200, 535);
		g.drawString("Max Color = " + maxColor, 1200, 570);
		int averagedegree = sumDegree / points.size();
		g.drawString("Average degree = " + averagedegree, 1200, 700);
		g.drawString("Total Distinct edges : " + sumDegree / 2, 1200, 735);
		g.drawString("Total Execution time : " + totalTime + " Seconds", 1200, 770);

		try
		{
			Thread.sleep(1500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		plotFinalBipartiteGraph();
		histogram.initialize();
		DegreeDeletedframe.setVisible(true);
		Colorframe.setVisible(true);

	}

	private void showDataInConsole()
	{
		System.out.println("\n==============> Done rendering !!!\n");
		System.out.println("Max degree = " + maxDegree);
		System.out.println("Min degree = " + minDegree);
		System.out.println("Average degree = " + sumDegree / points.size());
		if (topology == 1)
		{
			System.out.println("Radius : " + sensorRadius / unitSizeForSquare);
		}
		else
		{
			System.out.println("Radius : " + sensorRadius / unitSizeForCircle);
		}
		System.out.println("Expect Avg : " + expectedAverageDegree);
		System.out.println("Terminal Clique : " + terminalClique);
		System.out.println("Max Degree When Deleted : " + maxDegreeWhenDeleted);
		System.out.println("Max Color : " + maxColor);
		System.out.println("domination 1 : " + colorPairList[5].domination * 100 + "\nEdge Size  1 : " + colorPairList[5].edgeSize);
		System.out.println("domination 2 : " + colorPairList[4].domination * 100 + "\nEdge Size  2 : " + colorPairList[4].edgeSize);
		System.out.println("Order of bipartite 1 : " + colorPairList[5].largestComponent.size());
		System.out.println("Order of bipartite 2 : " + colorPairList[4].largestComponent.size());
		System.out.println("Total Distinct edges : " + sumDegree / 2);
		System.out.println("Total Execution time : " + totalTime + " seconds");
		System.out.println("\n\nFormat to fill in excel table for report: ");
		if (topology == 1)
		{
			System.out.println("1\t" + numberOfSensors + "\t" + expectedAverageDegree + "\t" + "Square" + "\t" + sensorRadius / unitSizeForSquare + "\t" + sumDegree / 2 + "\t" + minDegree + "\t"
					+ maxDegree + "\t" + sumDegree / points.size() + "\t" + maxColor + "\t" + maxDegreeWhenDeleted + "\t" + terminalClique + "\t" + (colorPairList[5].domination * 100) + "\t"
					+ (colorPairList[4].domination * 100) + "\t" + colorPairList[5].edgeSize + "\t" + colorPairList[4].edgeSize + "\t" + colorPairList[5].largestComponent.size() + "\t"
					+ colorPairList[4].largestComponent.size() + "\t" + totalTime);
		}
		else
		{
			System.out.println("1\t" + numberOfSensors + "\t" + expectedAverageDegree + "\t" + "Disk" + "\t" + sensorRadius / unitSizeForCircle + "\t" + sumDegree / 2 + "\t" + minDegree + "\t"
					+ maxDegree + "\t" + sumDegree / points.size() + "\t" + maxColor + "\t" + maxDegreeWhenDeleted + "\t" + terminalClique + "\t" + (colorPairList[5].domination * 100) + "\t"
					+ (colorPairList[4].domination * 100) + "\t" + colorPairList[5].edgeSize + "\t" + colorPairList[4].edgeSize + "\t" + colorPairList[5].largestComponent.size() + "\t"
					+ colorPairList[4].largestComponent.size() + "\t" + totalTime);
		}
	}

	private void plotMinMax(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));

		ArrayList<Point> neighbourPoints = maxPoint.getNeighbourPoints();
		maxDegree = neighbourPoints.size();
		for (Point neighbour : neighbourPoints)
		{
			g2.setColor(Color.GREEN);
			g2.draw(new Line2D.Float(maxPoint.getX(), maxPoint.getY(), neighbour.getX(), neighbour.getY()));
		}
		neighbourPoints = minPoint.getNeighbourPoints();
		minDegree = neighbourPoints.size();
		for (Point neighbour : neighbourPoints)
		{
			g2.setColor(Color.RED);
			g2.draw(new Line2D.Float(minPoint.getX(), minPoint.getY(), neighbour.getX(), neighbour.getY()));
		}

	}

	public void plotPoints(Graphics g)
	{
		for (int i = 0; i < points.size(); i++)
		{
			if (points.get(i).getColor() < 4)
			{
				g.setColor(colorSet[points.get(i).getColor()]);
				g.fillRoundRect(points.get(i).getX() - 3, points.get(i).getY() - 3, 6, 6, 16, 16);
				testWait(30000);
			}
		}
		g.setColor(Color.BLACK);
	}

	public void plotEdges(Graphics g)
	{
		if (points.size() <= 8000)
		{
			int count = 0;
			g.setColor(Color.BLACK);
			for (Point point : points)
			{
				for (Point neighbour : point.getNeighbourPoints())
				{

					g.drawLine(point.getX(), point.getY(), neighbour.getX(), neighbour.getY());
					count++;
				}
			}
			float endTime = (float) (System.currentTimeMillis() - startTime) / 1000;
			System.out.println(
					"Ended in " + endTime + " seconds\nNOTE :- This edge plotting time is not being added to total time to maintain consistancy\n        in project benchmark summary table.\n");
		}
		else
		{
			endTime(g);
		}
	}

	private void showHistogram()
	{
		ArrayList<Degree> histDegrees = new ArrayList<>();
		int count = minDegree;
		for (int i = 0; i < degrees.length; i++)
		{
			if (!degrees[i].isPointsEmpty())

			{
				degrees[i].setSize(count++);
				histDegrees.add(degrees[i]);
			}
		}
		histogram = new Histogram(histDegrees, "Histogrm");
	}

	private void showLineGraph()
	{
		List<Integer> scores1 = new ArrayList<>();
		List<Integer> scores2 = new ArrayList<>();
		for (int i = 0; i < points.size(); i++)
		{
			scores1.add(points.get(i).getNeighbourPoints().size());
			scores2.add(points.get(i).getDegreeWhenDeleted());
		}
		LineGraph mainPanel = new LineGraph(scores1, scores2, maxDegree);

		DegreeDeletedframe = new JFrame("LineGraph");
		DegreeDeletedframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DegreeDeletedframe.getContentPane().add(mainPanel);
		DegreeDeletedframe.setExtendedState(DegreeDeletedframe.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		DegreeDeletedframe.pack();
		DegreeDeletedframe.setLocationByPlatform(true);
		DegreeDeletedframe.setVisible(false);
	}

	private void generateColors()
	{
		try
		{
			for (int z = 0; z < points.size(); z++)
			{
				Degree degree = getNextNonEmptyDegree();
				Point point = degree.getNextPoint();
				degree.removePoint(point);
				point.setDegreeWhenDeleted(point.getNeighbourPointsForDeletion().size());
				if (maxDegreeWhenDeleted < point.getNeighbourPointsForDeletion().size())
				{
					maxDegreeWhenDeleted = point.getNeighbourPointsForDeletion().size();
				}
				lowerNeigbhourDegrees(point);
				coloredList.add(point);
			}
		}
		catch (Exception e)
		{
		}
		for (int z = coloredList.size() - 1; z >= 0; z--)
		{
			Point point = coloredList.get(z);
			colorPoint(point);
		}

	}

	private Degree getNextNonEmptyDegree()
	{
		Degree degree = null;
		for (int i = 0; i < degrees.length; i++)
		{
			if (!degrees[i].isPointsEmpty())
			{
				degree = degrees[i];
				break;
			}
		}
		return degree;
	}

	private void colorPoint(Point point)
	{
		HashSet<Integer> colorList = point.getNeigbhorColorSet();
		colorList.add(-1);
		int color = 0;
		while (colorList.contains(color))
		{
			color++;
		}
		point.setColor(color);
		colorPoints[color].addPoint(point);
		if (maxColor < color)
		{
			maxColor = color;
		}
	}

	private void lowerNeigbhourDegrees(Point point)
	{
		ArrayList<Point> neighbourPoints = point.getNeighbourPointsForDeletion();
		neighbourPoints.remove(point);
		for (Point neibghor : neighbourPoints)
		{
			degrees[neibghor.getNeighbourPointsForDeletion().size()].removePoint(neibghor);
			neibghor.getNeighbourPointsForDeletion().remove(point);
			degrees[neibghor.getNeighbourPointsForDeletion().size()].addPoint(neibghor);

		}
		calculateTerminalClique();
	}

	private void calculateTerminalClique()
	{
		if (terminalClique == -1)
		{
			int count = 0, index = 0;

			for (int i = 0; i < degrees.length; i++)
			{

				if (degrees[i].getDegreePoints().size() != 0)
				{
					index = i;
					count++;
				}
			}
			if (count == 1)
			{
				terminalClique = degrees[index].getDegreePoints().size() - 1;
			}
		}
	}

}
