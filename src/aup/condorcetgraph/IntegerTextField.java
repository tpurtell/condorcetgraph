package aup.condorcetgraph;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
public class IntegerTextField extends JTextField {
	final static String badchars = "`~!@#$%^&*()_+=\\|\"':;?/><, ";
	public IntegerTextField(int size) {
		super(size);
	}
	public IntegerTextField(int size, int value) {
		super(size);
		setText(Integer.toString(value));
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
		else {
			super.processKeyEvent(ev);
		}
	}
}