package aup.condorcetgraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JComponent;


public class PlotView extends JComponent {
	TallyGraph graph;
	double x_min;
	double x_max;
	double y_min;
	double y_max;
	boolean cycles;
	int x_node;
	int y_node;
	int k_depth;
	int scalar;
	public void setPlotCycles(boolean c) {
		cycles = c;
	}
	public void setKDepth(int depth) {
		k_depth = depth;
	}
	public void setX(int x, double min, double max) {
		x_node = x;
		x_max = max;
		x_min = min;
	}
	public void setY(int y, double min, double max) {
		y_node = y;
		y_max = max;
		y_min = min;
	}
	public void setTallyGraph(TallyGraph g) {
		graph = g;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(300, 300);
	}
	public Dimension getMinimumSize() {
		return new Dimension(0, 0);
	}
	protected void paintComponent(Graphics g) {
		paint(g,getWidth(), getHeight());
	}
	protected void paint(Graphics g, int width, int height) {
		g.setColor(getBackground());
		g.fillRect(0, 0, width, height);
		if(graph == null) {
			return;
		}
		Graphics2D g2d = (Graphics2D) g.create();
		
		int vertices = graph.votes.length;
		Color[] colors = new Color[vertices];
		
		float b = .9f;
		float s = 1;
		float hue = 0;
		float dhue = 1.0f/vertices;
		for(int i = 0; i < vertices; i++, hue += dhue) {
			int color = Color.HSBtoRGB(hue,s,b);
			colors[i] = new Color(color);
		}
		

		double dx = (x_max - x_min) / width * scalar;
		double dy = (y_max - y_min) / height * scalar;
		
		
		double x_votes = graph.getVotes(x_node);
		double y_votes = graph.getVotes(y_node);
		
		double x = x_min;
		Random random = new Random();
		for(int i = 0; i < width; i+=scalar, x+=dx) {
			graph.setVotes(x_node, x);
			double y = y_min;
			for(int j = 0; j < height; j+=scalar, y+=dy) {
				graph.setVotes(y_node, y);
				int[] winners;
				if(!cycles) {
					winners = graph.tallyCycles(k_depth);
				}
				else {
					winners = graph.tally(k_depth);
				}
				if(winners.length == 0) {
					g2d.setColor(Color.WHITE);
					g2d.fillRect(i,j, scalar, scalar);
					continue;
				}
				int pick = random.nextInt(winners.length);
				
				g2d.setColor(colors[winners[pick]]);
				g2d.fillRect(i,j, scalar, scalar);
			}
		}
		
		graph.setVotes(x_node, x_votes);
		graph.setVotes(y_node, y_votes);
		
		g2d.setColor(Color.BLACK);
		g2d.dispose(); //clean up
	}
	public BufferedImage render(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		scalar = 1;
		paint(image.getGraphics(),width,height);
		scalar = 3;
		return image;
	}
	public PlotView() {
		super();
		scalar = 3;
		graph = null;
		setBackground(Color.WHITE);	
		setOpaque(true);
	}
}
