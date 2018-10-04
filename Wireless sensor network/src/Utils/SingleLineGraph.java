package Utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SingleLineGraph extends JPanel
{
	private static int MAX_SCORE = 20;
	private static final int PREF_W = 800;
	private static final int PREF_H = 650;
	private static final int BORDER_GAP = 30;
	private static final Color GRAPH_COLOR1 = Color.BLUE;
	private static final Color GRAPH_POINT_COLOR = Color.RED;
	private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
	private static final int GRAPH_POINT_WIDTH = 10;
	private static final int Y_HATCH_CNT = 10;
	private List<Integer> scores1;

	public SingleLineGraph(List<Integer> scores1, int max_score)
	{
		this.scores1 = scores1;
		MAX_SCORE = max_score + 4;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double xScale1 = ((double) getWidth() - 2 * BORDER_GAP) / (scores1.size() - 1);
		double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

		List<Point> graphPoints1 = new ArrayList<>();
		for (int i = 0; i < scores1.size(); i++)
		{
			int x1 = (int) (i * xScale1 + BORDER_GAP);
			int y1 = (int) ((MAX_SCORE - scores1.get(i)) * yScale + BORDER_GAP);
			graphPoints1.add(new Point(x1, y1));
		}

		// create x and y axes
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

		// create hatch marks for y axis.
		for (int i = 0; i < Y_HATCH_CNT; i++)
		{
			int x0 = BORDER_GAP;
			int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
			int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
			int y1 = y0;
			g2.drawLine(x0, y0, x1, y1);
		}

		// and for x axis
		for (int i = 0; i < scores1.size() - 1; i++)
		{
			int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (scores1.size() - 1) + BORDER_GAP;
			int x1 = x0;
			int y0 = getHeight() - BORDER_GAP;
			int y1 = y0 - GRAPH_POINT_WIDTH;
			g2.drawLine(x0, y0, x1, y1);
		}

		Stroke oldStroke1 = g2.getStroke();
		g2.setColor(GRAPH_COLOR1);
		for (int i = 0; i < graphPoints1.size() - 1; i++)
		{
			int x1 = graphPoints1.get(i).x;
			int y1 = graphPoints1.get(i).y;
			int x2 = graphPoints1.get(i + 1).x;
			int y2 = graphPoints1.get(i + 1).y;
			g2.drawLine(x1, y1, x2, y2);
		}

		g2.setStroke(oldStroke1);
		g2.setColor(GRAPH_POINT_COLOR);
		for (int i = 0; i < graphPoints1.size(); i++)
		{
			int x = graphPoints1.get(i).x - GRAPH_POINT_WIDTH / 2;
			int y = graphPoints1.get(i).y - GRAPH_POINT_WIDTH / 2;
			;
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			g2.fillRoundRect(x, y, ovalW, ovalH, 16, 16);
			g.setFont(new Font("default", Font.BOLD, 15));
			g.drawString("" + i, x, (getHeight() - 15));
		}

	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(PREF_W, PREF_H);
	}

}