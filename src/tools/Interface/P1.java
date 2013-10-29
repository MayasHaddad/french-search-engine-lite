package tools.Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class P1 extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final List<JLabel> l = new LinkedList<JLabel>();
	private MouseManager mouseManager = null;
	private final RequestListener rl;
	private final JTextField jtf;

	public P1(final JTextField j, final JEditorPane pan2) {
		this.jtf = j;
		final JEditorPane pan = pan2;
		this.rl = new RequestListener(this.jtf, this, pan);
		this.jtf.addActionListener(this.rl);
		this.jtf.setPreferredSize(new Dimension(400, 25));
		this.add(this.jtf, BorderLayout.NORTH);
	}

	public void clear() {
		for (final JLabel element : this.l) {
			this.remove(element);
		}
	}

	public List<JLabel> getList() {
		return this.l;
	}

	public void setMouseManager(final MouseManager m) {
		this.mouseManager = m;
		this.rl.setMouseManager(m);
	}

	public String getJTextFieldText() {
		return this.jtf.getText();
	}
}
