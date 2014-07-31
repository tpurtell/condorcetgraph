package aup.condorcetgraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JComponent;


public class GraphView extends JComponent {
	private MatrixGraph graph;
	private Hashtable labels;
	public void clearLabels() {  
		labels.clear();
	}
	public MatrixGraph getGraph() {
		return graph;
	}
	public void setGraph(MatrixGraph g) {
		graph = g;
		if(isVisible()) {
			repaint();
		}
	}
	public Dimension getPreferredSize() {
		return new Dimension(300, 300);
	}
	public Dimension getMinimumSize() {
		return new Dimension(0, 0);
	}
	public BufferedImage render(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		paint(image.getGraphics(),width,height);
		return image;
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
		double r_ratio = .70;
		double t_ratio = .85;
		double shift_ratio = .05;
		double bubble_ratio = .005;
		
		int w = width;
		int h = height; 
		int m = w < h ? w : h;

		
		Color[] colors = new Color[vertices];
		
		float b = .9f;
		float s = 1;
		float hue = 0;
		float dhue = 1.0f/vertices;
		for(int i = 0; i < vertices; i++, hue += dhue) {
			int color = Color.HSBtoRGB(hue,s,b);
			colors[i] = new Color(color);
		}

		
		double[] x = new double[vertices];
		double[] y = new double[vertices];
		
		double r =  r_ratio * m / 2;
		double dt = 2.0 * Math.PI / vertices;
		float t = 0;
		for(int i = 0; i < vertices; i++, t+=dt) {
			x[i] = r * Math.cos(t) + w / 2;
			y[i] = r * Math.sin(t) + h / 2;
			
			g2d.setColor(colors[i]);
			g2d.fillOval((int)x[i] - 5, (int)y[i] - 5, 11, 11);
			g2d.setColor(Color.BLACK);
			g2d.fillOval((int)x[i] - 2, (int)y[i] - 2, 5, 5);
		}
		g2d.setColor(Color.BLACK);
		for(int i = 0; i < vertices; i++) {
			for(int j = i + 1; j < vertices; j++) {
				if(graph.edges[i][j] || graph.edges[j][i]) {
					g2d.drawLine((int)x[i], (int)y[i], (int)x[j], (int)y[j]);
				}
				int xc = (int)(x[i] + x[j]) / 2;
				int yc = (int)(y[i] + y[j]) / 2;
				double dx =  (x[j] - x[i]);
				double dy =  (y[j] - y[i]);
				double mag = Math.sqrt(dx * dx + dy * dy);
				
				dx /=  mag;
				dy /= mag;
				
				double pdx =  -dy;
				double pdy =  dx;

				if(!graph.edges[i][j] && graph.edges[j][i]) {
					g2d.fillPolygon(
							new int[] {xc + (int)(8.0 * dx), xc +  (int)(5.0 * pdx), xc -  (int)(5.0 * pdx)},
							new int[] {yc + (int)(8.0 * dy), yc + (int)(5.0 * pdy), yc - (int)(5.0 * pdy)},
							3);
				}
				if(graph.edges[i][j] && !graph.edges[j][i]) {
					g2d.fillPolygon(
							new int[] {xc - (int)(8.0 * dx), xc +  (int)(5.0 * pdx), xc -  (int)(5.0 * pdx)},
							new int[] {yc - (int)(8.0 * dy), yc + (int)(5.0 * pdy), yc - (int)(5.0 * pdy)},
							3);					
				}
			}
		}
		t = 0;
		for(int i = 0; i < vertices; i++, t+=dt) {
			x[i] = r * Math.cos(t) * t_ratio / r_ratio + w / 2 - r * shift_ratio;
			y[i] = r * Math.sin(t) * t_ratio / r_ratio + h / 2;
		}
		for(int i = 0; i < vertices; i++) {
			String text = (String)labels.get(new Integer(i));
			boolean bold = true;
			if(text == null) {
				bold = false;
				text = Double.toString(graph.votes[i]);
				if(text.length() > 4) {
					text = text.substring(0,4);
				}
			}
			g2d.drawString(text, (int)x[i], (int)y[i]);
			if(bold) {
				g2d.drawString(text, (int)x[i]+1, (int)y[i]);
			}
		}
		g2d.dispose(); //clean up
	}
	public void setLabel(int i, String s) {
		labels.put(new Integer(i), s);
	}
	public GraphView() {
		super();
		labels = new Hashtable();
		this.graph = null;
		setBackground(Color.WHITE);	
		setOpaque(true);
		setBorder(BorderFactory.createTitledBorder("Graph"));
	}
	public GraphView(MatrixGraph graph) { 
		super();
		labels = new Hashtable();
		this.graph = graph;
		setBackground(Color.WHITE);	
		setOpaque(true);
		setBorder(BorderFactory.createTitledBorder("Graph"));
	}
}
