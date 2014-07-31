package aup.condorcetgraph;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
public class DoubleTextField extends JTextField {
	final static String badchars = "`~!@#$%^&*()_+=\\|\"':;?/><, ";
	public DoubleTextField(int size) {
		super(size);
	}
	public DoubleTextField(int size, double value) {
		super(size);
		setText(Double.toString(value));
	}
	public void processKeyEvent(KeyEvent ev) {
		char c = ev.getKeyChar();
		if ((Character.isLetter(c) && !ev.isAltDown())
				|| badchars.indexOf(c) > -1) {
			ev.consume();
			return;
		}
		if (c == '-' && (getCaretPosition()  != 0 || getText().indexOf("-") != -1)) {
			ev.consume();
		}
		else if(c == '.' && getText().indexOf(".") != -1 && getText().indexOf("-") < getCaretPosition()) {
			ev.consume();			
		}
		else {
			super.processKeyEvent(ev);
		}
	}
}