package aup.condorcetgraph;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CondorcetGraph implements ChangeListener {
	JTabbedPane tabbed_pane;
	GeneratePlotsPanel plot_panel;
	DrawGraphPanel draw_graph_panel;
	JFrame frame;
	
	public CondorcetGraph() {
		int initial_vertices = 8;
		
		plot_panel = new GeneratePlotsPanel();
		draw_graph_panel = new DrawGraphPanel();
		
		
		tabbed_pane = new JTabbedPane();
		tabbed_pane.add("Draw Graph", draw_graph_panel);
		tabbed_pane.add("Generate Plots", plot_panel);
		
		frame = new JFrame("Condorcet Graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(tabbed_pane);
		frame.pack();
		tabbed_pane.addChangeListener(this);
	}
	public void stateChanged(ChangeEvent ev) {
		if(ev.getSource() == tabbed_pane) {
			if(tabbed_pane.getSelectedComponent() == plot_panel) {
				plot_panel.setGraph(draw_graph_panel.getGraph());
			}
		}
	}
	public static void main(String[] args) {
		CondorcetGraph cg = new CondorcetGraph();
		cg.frame.setVisible(true);
	}
}
    