package aup.condorcetgraph;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.filechooser.FileFilter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class DrawGraphPanel extends JPanel implements TableModelListener, ChangeListener, ActionListener{
	JPanel draw_panel; 
	JSplitPane split_pane;
	JScrollPane scroll_pane;
	JScrollPane option_scroll;
	GraphView graph_view;
	JPanel option_bar;
	JTable graph_table;
	JSlider vertex_slider;
	JLabel vertex_label;
	JButton distances_button;
	MatrixGraph graph;
	JButton random_button;
	JButton tally_button;
	IntegerTextField k_depth;
	JButton load_button;

	
	public DrawGraphPanel() {
		int inital_vertices = 8;
		int max_vertices = 32;
		int min_vertices = 2;

		graph = new MatrixGraph(8);
		
		graph_view = new GraphView(graph);
				
		graph_table = new JTable(new GraphTableModel(graph));
		graph_table.getModel().addTableModelListener(this);
		
		scroll_pane = new JScrollPane(graph_table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		split_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graph_view, scroll_pane);
		
		vertex_slider = new JSlider(JSlider.HORIZONTAL, min_vertices, max_vertices, inital_vertices);
		vertex_slider.setMajorTickSpacing(5);
		vertex_slider.setMinorTickSpacing(1);
		vertex_slider.setPaintTicks(true);
		vertex_slider.setPaintLabels(true);
		vertex_slider.addChangeListener(this);
		
		vertex_label = new JLabel(vertex_slider.getValue() + " vertices");
		
		random_button = new JButton("Random");
		random_button.addActionListener(this);
		distances_button = new JButton("Distances");
		distances_button.addActionListener(this);
		tally_button = new JButton("Tally");
		tally_button.addActionListener(this);
		load_button = new JButton("Load");
		load_button.addActionListener(this);
		
		k_depth = new IntegerTextField(5,2);
		
		option_bar = new JPanel();
		option_bar.add(random_button);
		option_bar.add(distances_button);
		option_bar.add(new JLabel("K Depth"));
		option_bar.add(k_depth);
		option_bar.add(tally_button);
		option_bar.add(vertex_slider);
		option_bar.add(vertex_label);
		option_bar.add(load_button);
		
		option_scroll = new JScrollPane(option_bar, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		setLayout(new BorderLayout());
		add(split_pane, BorderLayout.CENTER);
		add(option_scroll, BorderLayout.SOUTH);
	}
	public void stateChanged(ChangeEvent ev) {
		if(ev.getSource() == vertex_slider) {
			int value = vertex_slider.getValue();
			vertex_label.setText(value + " vertices"); 
			while(value < graph.votes.length) {
				graph.removeVertex(graph.votes.length - 1); 
			}
			if(value > graph.votes.length) {
				graph.addVertices(value - graph.votes.length);
			}
			graph_table.getModel().removeTableModelListener(this);
			graph_table.setModel(new GraphTableModel(graph));
			graph_table.getModel().addTableModelListener(this);
			graph_view.repaint(); 
		}
	}
	public void tableChanged(TableModelEvent arg0) {
		graph_view.repaint();
	}
	public void actionPerformed(ActionEvent ev) {
    Container c = this.getParent();
    while (c != null) {
      if (c instanceof Frame) {
      	break;
      }
      c = c.getParent();
    }
		Frame frame = (Frame)c;
		if(ev.getSource() == load_button) {
			JFileChooser chooser = new JFileChooser();
			FileFilter f = new FileFilter() {
				public String getDescription() {
					return "Graphs";
				}
				public boolean accept(File f) {
					if(f.getName().endsWith(".txt")) {
						return true;
					}
					return false;
				}
			};
	
	    chooser.setFileFilter(f);
	    int returnVal = chooser.showOpenDialog(frame);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File file = chooser.getSelectedFile();
	    	String gr_data;
				try {
					BufferedReader in = new BufferedReader(new FileReader(file));
					gr_data = in.readLine();
					in.close();
		    	graph = (MatrixGraph)Base64.decodeToObject(gr_data);
					graph_view.setGraph(graph);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    }
		graph_table.getModel().removeTableModelListener(this);
		graph_table.setModel(new GraphTableModel(graph));
		graph_table.getModel().addTableModelListener(this);

	    
		}
		if(ev.getSource() == random_button) {

	    RandomGraphDialog dlg = new RandomGraphDialog(frame);
	    dlg.setVisible(true);
	    if(!dlg.canceled) {
	    	graph.randomize(dlg.getDirected(), (double)dlg.getPercentage() / 100.0, dlg.getRandomVotes());
	    	graph_view.repaint();
	    	scroll_pane.repaint();
	    }
		}
		if(ev.getSource() == distances_button) {
			
			AbstractTableModel model = new AbstractTableModel(){
				IntegerMatrix matrix;
				public void init() {
					if(matrix == null)
					matrix = new IntegerMatrix(graph.edges).minOfPowers();
				}
				public int getRowCount() {
					init();
					return matrix.getRows();
				}
				public int getColumnCount() {
					init();
					return matrix.getColumns();
				}
				public Object getValueAt(int r, int c) {
					init();
					if(matrix.getEntry(r,c) == Integer.MAX_VALUE) {
						return new String("-");
					}
					return new Integer(matrix.getEntry(r,c));
				}
			};
			
			ShowTableDialog dlg = new ShowTableDialog(frame, "Path Lengths", model);
			dlg.setVisible(true);	
		}
		if(ev.getSource() == tally_button) {
			TallyGraph g = new TallyGraph(graph);
			int[] winners = g.tallyCycles(Integer.parseInt(k_depth.getText()));
			String win_string = "The winners are {" + winners[0];
			for(int i = 1; i < winners.length; i++) {
				win_string += ", " + winners[i];
			}
			win_string += "}";
			JOptionPane.showMessageDialog(frame, win_string);
		}
	}
	public MatrixGraph getGraph() {
		return graph;
	}
	
}
