package aup.condorcetgraph;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableModel;
public class ShowTableDialog extends JDialog implements ActionListener {
	JTable table;
	JButton ok_button;
	public ShowTableDialog(Frame f, String title, TableModel model) {
		super(f, title, true);
		
		table = new JTable(model);
		ok_button = new JButton("OK");
		ok_button.addActionListener(this);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(table, BorderLayout.CENTER);
		getContentPane().add(ok_button, BorderLayout.SOUTH);
		
		pack();
	}
	
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource() == ok_button) {
			setVisible(false);
		}
	}
	

}
