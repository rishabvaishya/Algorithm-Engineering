package Utils;

// https://stackoverflow.com/questions/6849151/bar-chart-in-java

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import models.Degree;

public class Histogram extends JPanel
{
	private double[] values;
	private String[] names;
	private String title;

	public Histogram(double[] v, String[] n, String t)
	{
		names = n;
		values = v;
		title = t;
	}

	public Histogram(ArrayList<Degree> degrees, String string)
	{

		names = new String[degrees.size()];
		values = new double[degrees.size()];
		title = "";
		for (int i = 0; i < degrees.size(); i++)
		{
			names[i] = String.valueOf(degrees.get(i).getSize());
			values[i] = degrees.get(i).getValues();
		}

	}

	public Histogram(Degree[] degrees, String string)
	{
		names = new String[degrees.length];
		values = new double[degrees.length];
		title = "";
		for (int i = 0; i < degrees.length; i++)
		{
			names[i] = String.valueOf(degrees[i].getDegreePoints().size());
			values[i] = degrees[i].getValues();
		}

	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (values == null || values.length == 0)
		{
			return;
		}
		double minValue = 0;
		double maxValue = 0;
		for (double value : values)
		{
			if (minValue > value)
			{
				minValue = value;
			}
			if (maxValue < value)
			{
				maxValue = value;
			}
		}

		Dimension d = getSize();
		int clientWidth = d.width;
		int clientHeight = d.height;
		int barWidth = clientWidth / values.length;

		Font titleFont = new Font("SansSerif", Font.BOLD, 20);
		FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
		Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
		FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

		int titleWidth = titleFontMetrics.stringWidth(title);
		int y = titleFontMetrics.getAscent();
		int x = (clientWidth - titleWidth) / 2;
		g.setFont(titleFont);
		g.drawString(title, x, y);

		int top = titleFontMetrics.getHeight();
		int bottom = labelFontMetrics.getHeight();
		if (maxValue == minValue)
		{
			return;
		}
		double scale = (clientHeight - top - bottom) / (maxValue - minValue);
		y = clientHeight - labelFontMetrics.getDescent();
		g.setFont(labelFont);

		for (int i = 0; i < values.length; i++)
		{
			int valueX = i * barWidth + 1;
			int valueY = top;
			int height = (int) (values[i] * scale);
			if (values[i] >= 0)
			{
				valueY += (int) ((maxValue - values[i]) * scale);
			}
			else
			{
				valueY += (int) (maxValue * scale);
				height = -height;
			}

			g.setColor(Color.red);
			g.fillRect(valueX, valueY, barWidth - 2, height);
			g.setColor(Color.black);
			g.drawRect(valueX, valueY, barWidth - 2, height);
			g.drawString(values[i] + "", valueX, valueY - 5);

			int labelWidth = labelFontMetrics.stringWidth(names[i]);
			x = i * barWidth + (barWidth - labelWidth) / 2;
			g.drawString(names[i], x, y);
		}
	}

	public void initialize()
	{
		JFrame f = new JFrame();
		f.setSize(1200, 1000);
		f.getContentPane().add(new Histogram(values, names, title));

		WindowListener wndCloser = new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		};
		f.toBack();
		f.setExtendedState(f.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		f.addWindowListener(wndCloser);
		f.setVisible(true);
	}
}