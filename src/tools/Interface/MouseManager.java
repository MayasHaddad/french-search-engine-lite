package tools.Interface;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JLabel;

import tools.IOManager;

public class MouseManager implements MouseListener {

	P1 p1;
	List<JLabel> jLabelList;
	JEditorPane pannel;

	public MouseManager(final P1 p1, final JEditorPane pan) {
		this.p1 = p1;
		this.jLabelList = p1.getList();
		this.pannel = pan;
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(final MouseEvent e) {
		System.out.println("Mouse pressed");
		JLabel currentLabel = null;
		for (final JLabel label : this.jLabelList) {
			if (e.getSource() == label) {
				currentLabel = label;
				label.setForeground(Color.red);
			} else {
				// label.setBackground(Color.white);
				label.setForeground(Color.black);
			}
		}
		if (currentLabel != null) {
			// this.pannel.updateUI();
			final File f = new File(
					IOManager.returnFilePathFromPoid(currentLabel.getText()));
			final BufferedReader br = null;
			try {
				// if (currentLabel.getText().split("\\.")[1].equals("txt")) {
				this.pannel.setPage("file://" + f.getAbsolutePath());
				// this.pannel
				// .setPage("http://docs.oracle.com/javase/7/docs/api/javax/swing/JEditorPane.html");
				// }
				String pan = this.pannel.getText();
				pan = pan.replaceAll(
						this.p1.getJTextFieldText(),
						"\n\n\n\n\n\n\n\n\n\n\n\n"
								+ this.p1.getJTextFieldText()
								+ "\n\n\n\n\n\n\n\n\n\n\n");
				this.pannel.setText(pan);

			} catch (final IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
