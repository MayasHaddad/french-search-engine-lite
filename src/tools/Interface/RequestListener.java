package tools.Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

import tools.Const;
import tools.Searcher;

public class RequestListener implements ActionListener {
	JTextField jtf;
	JEditorPane jep;
	P1 jp;
	private MouseManager mouseManager = null;

	public RequestListener(final JTextField jtf, final P1 jp,
			final JEditorPane jep) {
		this.jtf = jtf;
		this.jep = jep;
		this.jp = jp;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		System.out.println("Answering request : " + this.jtf.getText());
		try {
			final TreeMap<Double, TreeSet<String>> resultRequest = Searcher
					.getResult(this.jtf.getText(), new File(
							Const.PATH_TO_LITTLE_CORPUS));
			System.out.println("result found");

			// create the list to display
			int cpt = 1;
			// this.jp.removeAll();
			this.jp.clear();
			final List<JLabel> l = this.jp.getList();
			for (final Map.Entry<Double, TreeSet<String>> score : resultRequest
					.entrySet()) {
				for (final String s : score.getValue()) {
					final JLabel j = new JLabel(s/* + " " + score.getKey() */);
					j.addMouseListener(this.mouseManager);
					l.add(j);
					this.jp.add(j);
					cpt++;
					if (cpt > GraphicalInterface.NUMBER_RESULTS) {
						break;
					}
				}
				if (cpt > GraphicalInterface.NUMBER_RESULTS) {
					break;
				}
			}
			if (l.isEmpty()) {
				l.add(new JLabel("Aucun resultat trouves"));
			}
			System.out.println("List done");

			// final File f = new File(
			// IOManager.returnFilePathFromPoid("01987639.txt.poid"));
			// final BufferedReader br = IOManager.returnBufferedReader(f);
			// String s;
			// String pageText = "";
			// while ((s = br.readLine()) != null) {
			// pageText += s;
			// }
			// this.jep.setText(pageText);
			// System.out.println(pageText);

			// allow the user to display the results on the right by
			// clicking page names

			this.jp.updateUI();
			this.jep.updateUI();

		} catch (final IOException e1) {
			e1.printStackTrace();
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
	}

	public void setMouseManager(final MouseManager m) {
		this.mouseManager = m;
	}

}
