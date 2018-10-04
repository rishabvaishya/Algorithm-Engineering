package Utils;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;

import models.Degree;
import models.Point;

public class BipartitePlot extends Canvas
{
	private static final long serialVersionUID = 1L;
	ColorPair colorPairList;
	private int color1;
	private int color2;
	private Degree bipartiteDegrees[];
	private Color[] colorSet =
	{ Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };

	public Component setRemainingData(ColorPair colorPairList)
	{
		this.colorPairList = colorPairList;
		color1 = colorPairList.color1;
		color2 = colorPairList.color2;
		return null;
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		plotFullBipartiteGraph(g);
		plotData(g);
	}

	private void plotData(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.BLACK);
		g.setFont(new Font("default", Font.PLAIN, 24));
		g.drawString("Domination : " + colorPairList.domination * 100 + "%", 1200, 235);
		g.drawString("Graph size (edges) : " + colorPairList.edgeSize, 1200, 270);
		g.drawString("Order : " + colorPairList.largestComponent.size(), 1200, 305);

	}

	private void plotFullBipartiteGraph(Graphics g)
	{
		for (Point point : colorPairList.largestComponent)
		{
			g.setColor(colorSet[point.getColor()]);
			g.fillRoundRect(point.getX() - 3, point.getY() - 3, 6, 6, 16, 16);
			HashSet<Point> neigbhorPoints;
			neigbhorPoints = point.getBipartiteNeighbourPoints(color2);

			for (Point neighbor : neigbhorPoints)
			{
				g.setColor(colorSet[neighbor.getColor()]);
				g.fillRoundRect(neighbor.getX() - 3, neighbor.getY() - 3, 6, 6, 16, 16);
				g.setColor(Color.BLACK);
				g.drawLine(point.getX(), point.getY(), neighbor.getX(), neighbor.getY());

			}
		}

	}

}
