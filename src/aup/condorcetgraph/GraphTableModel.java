/*
 * Created on Apr 7, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package aup.condorcetgraph;

import javax.swing.table.AbstractTableModel;

/**
 * @author tpurtell
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GraphTableModel extends AbstractTableModel {
	MatrixGraph graph;
	public GraphTableModel(MatrixGraph graph) {
		this.graph = graph;
	}

	public String getColumnName(int c) {
		if(c == 0) { 
			return "Votes";
		}
		return "Node " + (c - 1);
	}
	
	public int getRowCount() {
		return graph.votes.length;
	}

	public int getColumnCount() {
		return graph.votes.length + 1;
	}
	public Class getColumnClass(int c) {
		if(c == 0) {
			return Double.class;
		}
		return Boolean.class;
	}
	public Object getValueAt(int r, int c) {
		if(c == 0) {
			return new Double(graph.votes[r]);
		}
		return new Boolean(graph.edges[r][c - 1]);
	}
  public boolean isCellEditable(int r, int c) {
		if(r == c - 1) {
			return false;
		}
  	return true;
  }
  public void setValueAt(Object value, int r, int c) {
    if(c == 0) { 
    	graph.votes[r] = ((Double)value).doubleValue();
      fireTableCellUpdated(r, c);
    	return;
    }
  	graph.edges[r][c - 1] = ((Boolean)value).booleanValue();
    fireTableCellUpdated(r, c);
}
}
