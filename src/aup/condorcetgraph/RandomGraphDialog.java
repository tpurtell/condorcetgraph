package aup.condorcetgraph;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

public class RandomGraphDialog extends JDialog implements ActionListener{
	JRadioButton undirected_button;
	JRadioButton directed_button;
	ButtonGroup directed_group;
	JSlider probability_slider;
	JButton cancel_button;
	JButton generate_button;
	JCheckBox random_votes;
	GridBagLayout layout;
	boolean canceled = false;
	
	public RandomGraphDialog(Frame frame) {
		super(frame, "Random Graph Parameters", true);
		undirected_button = new JRadioButton("Undirected",true);
		directed_button = new JRadioButton("Directed");
		directed_group = new ButtonGroup();
		directed_group.add(undirected_button);
		directed_group.add(directed_button);

		probability_slider = new JSlider(0, 100, 1);
		probability_slider.setMajorTickSpacing(25);
		probability_slider.setMinorTickSpacing(5);
		probability_slider.setPaintLabels(true);
		probability_slider.setPaintTicks(true);
		probability_slider.setValue(50);
		
		cancel_button = new JButton("Cancel");
		cancel_button.addActionListener(this);
		generate_button = new JButton("Generate");
		generate_button.addActionListener(this);
		
		random_votes = new JCheckBox("Randomize Votes");
		
		layout = new GridBagLayout();
		getContentPane().setLayout(layout);
		getContentPane().add(undirected_button);
		getContentPane().add(directed_button);
		getContentPane().add(probability_slider);
		getContentPane().add(random_votes);
		getContentPane().add(cancel_button);
		getContentPane().add(generate_button);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = GridBagConstraints.RELATIVE;
		layout.setConstraints(undirected_button, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(directed_button, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(probability_slider, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(random_votes, c);
		c.gridwidth = GridBagConstraints.RELATIVE;
		layout.setConstraints(cancel_button, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(generate_button, c);
		
		pack();
	}
	public boolean getCanceled() {
		return canceled;
	}
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource() == generate_button) {
			canceled = false;
			setVisible(false);
		}
		if(ev.getSource() == cancel_button) {
			canceled = true;
			setVisible(false);
		}
	}
	
	public int getPercentage() {
		return probability_slider.getValue();
	}
	public boolean getDirected() {
		return directed_button.isSelected();
	}
	public boolean getRandomVotes() {
		return random_votes.isSelected();
	}
}
