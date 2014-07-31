 package aup.condorcetgraph;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

 
public class GeneratePlotsPanel extends JPanel implements ActionListener,ChangeListener, ItemListener {
	MatrixGraph graph = new MatrixGraph(1);
	GraphView graph_view;
	JSlider x_node;
	JSlider y_node;
	DoubleTextField x_min_field;
	DoubleTextField x_max_field;
	DoubleTextField y_min_field;
	DoubleTextField y_max_field;
	IntegerTextField k_depth;
	PlotView plot_view;
	JButton plot_button;
	JSplitPane split_pane;
	JPanel tool_bar;
	JScrollPane tool_scroll;
	JButton save_button; 
	JCheckBox plot_cycles;
	
	public GeneratePlotsPanel() {
		super();
	
		graph_view = new GraphView(graph);
		plot_view = new PlotView();
		
		split_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graph_view, plot_view);
		
		k_depth = new IntegerTextField(5,2);
		
		plot_button = new JButton("Plot");
		plot_button.addActionListener(this);
		save_button = new JButton("Save");
		save_button.addActionListener(this);

		x_node = new JSlider(0, 2, 1);
		x_node.setMajorTickSpacing(4);
		x_node.setMinorTickSpacing(1);
		x_node.setPaintTicks(true);
		x_node.setPaintLabels(true);
		x_node.setValue(0);
		x_node.addChangeListener(this);
		y_node = new JSlider(0, 2, 1);	
		y_node.setMajorTickSpacing(4);
		y_node.setMinorTickSpacing(1);
		y_node.setPaintTicks(true);
		y_node.setPaintLabels(true);
		y_node.addChangeListener(this);
		y_node.setValue(1);	
		x_min_field = new DoubleTextField(5,0);
		x_max_field = new DoubleTextField(5,5);
		y_min_field = new DoubleTextField(5,0);
		y_max_field = new DoubleTextField(5,5);
		
		plot_cycles = new JCheckBox();
		plot_cycles.setText("Plot Phase");
		plot_cycles.addItemListener(this);
	
		tool_bar = new JPanel();
		tool_bar.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.gridheight = 1;
		c.gridwidth = 1;
		tool_bar.add(new JLabel("X Axis: Node"),c);
		tool_bar.add(x_node,c);
		tool_bar.add(new JLabel("Min"),c);
		tool_bar.add(x_min_field,c);
		tool_bar.add(new JLabel("Max"),c);
		tool_bar.add(x_max_field,c);

		tool_bar.add(new JLabel("KDepth"),c);
		tool_bar.add(k_depth,c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		tool_bar.add(plot_cycles,c);

		c.gridheight = 1;
		c.gridwidth = 1;
		tool_bar.add(new JLabel("Y Axis: Node"),c);
		tool_bar.add(y_node,c); 
		tool_bar.add(new JLabel("Min"),c);
		tool_bar.add(y_min_field,c);
		tool_bar.add(new JLabel("Max"),c);
		tool_bar.add(y_max_field,c);
		tool_bar.add(plot_button,c);
		tool_bar.add(save_button,c);

		
		tool_scroll = new JScrollPane(tool_bar, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		setLayout(new BorderLayout());
		add(split_pane, BorderLayout.CENTER);
		add(tool_scroll, BorderLayout.SOUTH);
		
	}
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource() == save_button) {
	     String[] pos = { "Graph", "Graph Image", "Plot Image" };
	     String sel = (String)JOptionPane.showInputDialog(null, "What do you want to save?", "Save",
	                 JOptionPane.INFORMATION_MESSAGE, null, pos, pos[0]);
	     if(sel == null) {
	     	
	     }
	     else if(sel.equals("Graph")) {
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
		    int returnVal = chooser.showSaveDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	File file = chooser.getSelectedFile();
		     	String gr_data = graph.toString();
					try {
						BufferedWriter out = new BufferedWriter(new FileWriter(file));
						out.write(gr_data);
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    }
		    
	     }
	     else if(sel.equals("Graph Image")) {
				JFileChooser chooser = new JFileChooser();
				FileFilter f = new FileFilter() {
					public String getDescription() {
						return "Graphs";
					}
					public boolean accept(File f) {
						if(f.getName().endsWith(".png")) {
							return true;
						}
						return false;
					}
				};
		
		    chooser.setFileFilter(f);

				int returnVal = chooser.showSaveDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	File file = chooser.getSelectedFile();
		     	String gr_data = graph.toString();
					try {
						Iterator i = ImageIO.getImageWritersBySuffix("png");
						if(!i.hasNext()) {
							throw new RuntimeException("No image writer found");
						}
						ImageWriter wr = (ImageWriter)i.next();
						FileImageOutputStream out = new FileImageOutputStream(file);
						wr.setOutput(out);
						wr.write(graph_view.render(400,400));
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    }
	     	 
	     }
	     else if(sel.equals("Plot Image")) {
				JFileChooser chooser = new JFileChooser();
				FileFilter f = new FileFilter() {
					public String getDescription() {
						return "Graphs";
					}
					public boolean accept(File f) {
						if(f.getName().endsWith(".png")) {
							return true;
						}
						return false;
					}
				};
		
		    chooser.setFileFilter(f);

				int returnVal = chooser.showSaveDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	File file = chooser.getSelectedFile();
		     	String gr_data = graph.toString();
					try {
						Iterator i = ImageIO.getImageWritersBySuffix("png");
						if(!i.hasNext()) {
							throw new RuntimeException("No image writer found");
						}
						ImageWriter wr = (ImageWriter)i.next();
						FileImageOutputStream out = new FileImageOutputStream(file);
						wr.setOutput(out);
						wr.write(plot_view.render(400,400));
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    }
	     	
	     }

		}
		if(ev.getSource() == plot_button) {
			TallyGraph g = new TallyGraph(graph);
 			plot_view.setKDepth(Integer.parseInt(k_depth.getText()));
			plot_view.setX(x_node.getValue(), Double.parseDouble(x_min_field.getText()), Double.parseDouble(x_max_field.getText()));
			plot_view.setY(y_node.getValue(), Double.parseDouble(y_min_field.getText()), Double.parseDouble(y_max_field.getText()));
			plot_view.setTallyGraph(g);
			plot_view.repaint();
		}
	}
	public void setVisible(boolean arg0) {
		if(arg0) {
		    setGraph(graph);
		}
		super.setVisible(arg0);
	}
	public void stateChanged(ChangeEvent ev) {
			int x = x_node.getValue();
			int y = y_node.getValue();
		if(ev.getSource() == x_node) {
			if(x == y) {
				if(x == 0) {
					y = 1;
				}
				else {
					y--;
				}
			}
		}
		else if(ev.getSource() == y_node) {
			if(x == y) {
				if(y == 0) {
					x = 1;
				}
				else {
					x--;
				}
			}			
		}
		graph_view.clearLabels();
		graph_view.setLabel(x, "x");
		graph_view.setLabel(y, "y");
		graph_view.repaint();
		x_node.setValue(x);
		y_node.setValue(y);
	}
	public void itemStateChanged(ItemEvent ev) {
		if(ev.getSource() == plot_cycles) {
			plot_view.setPlotCycles(plot_cycles.isSelected());
		}
	}
	public void setGraph(MatrixGraph g) {
		graph = g;
        graph_view.setGraph(graph);
        x_node.setMaximum(graph.votes.length - 1);
        y_node.setMaximum(graph.votes.length - 1);
	}
}
