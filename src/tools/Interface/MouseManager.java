package tools.Interface;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JLabel;

import tools.FrenchStemmer;
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

			// essai firefox
			// final Runtime runtime = Runtime.getRuntime();
			// try {
			// runtime.exec("firefox "
			// + IOManager.returnFilePathFromPoid(currentLabel
			// .getText()));
			// } catch (final IOException e2) {
			// e2.printStackTrace();
			// }

			try {
				// if (currentLabel.getText().split("\\.")[1].equals("txt")) {
				this.pannel.setPage("file://" + f.getAbsolutePath());
				// this.pannel
				// .setPage("http://docs.oracle.com/javase/7/docs/api/javax/swing/JEditorPane.html");
				// }
				String fileText = this.pannel.getText();
				fileText = "<!DOCTYPE html><html><head></head><body style='color:blue'>"
						+ fileText + "</body></html>";
				final FrenchStemmer fs = new FrenchStemmer();
				for (final String motCherche : fs.normalize(this.p1
						.getJTextFieldText())) {
					fileText = fileText.replaceAll("(?i)\\s" + motCherche,
							"<span style='color:red;font-weight: bold;font-size: 2em;'>"
									+ motCherche + "</span>");
				}
				this.pannel.setContentType("text/html");
				this.pannel.setText(fileText);

			} catch (final IOException e1) {
				e1.printStackTrace();
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
