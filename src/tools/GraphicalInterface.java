package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class GraphicalInterface extends JFrame {

	private final JSplitPane split;
	private final int WIDTH = 1400;
	private final int HEIGHT = 800;
	Searcher s;

	public GraphicalInterface() {

		// this.setLocationRelativeTo(null);
		this.setTitle("Gérer vos conteneur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(this.WIDTH, this.HEIGHT);

		// On crée deux conteneurs de couleurs différentes
		final P1 pan = new P1();
		// pan.setBackground(Color.blue);

		final JEditorPane pan2 = new JEditorPane("text/html", "");
		pan2.setText("Mon fichier et son contenu...");
		// pan2.setBackground(Color.red);

		this.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pan, pan2);
		this.split.setDividerLocation(this.WIDTH / 2);

		this.getContentPane().add(this.split, BorderLayout.CENTER);
		this.setVisible(true);
	}

	public class P1 extends JPanel {

		public P1() {
			final JTextField jtf = new JTextField();
			final RequestListener rl = new RequestListener(jtf);
			jtf.addActionListener(rl);
			jtf.setPreferredSize(new Dimension(400, 25));
			this.add(jtf, BorderLayout.NORTH);
		}
	}

	public class RequestListener implements ActionListener {
		JTextField j;

		public RequestListener(final JTextField jtf) {
			this.j = jtf;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			System.out.println("Answering request : " + this.j.getText());
			try {
				final TreeMap<Double, TreeSet<String>> resultRequest = Searcher
						.getResult(this.j.getText(), new File(
								Const.PATH_TO_LITTLE_CORPUS));
				System.out.println(resultRequest);

				// create the list to display
				int cpt = 1;
				final List l = new LinkedList();
				for (final Map.Entry<Double, TreeSet<String>> score : resultRequest
						.entrySet()) {
					for (final String s : score.getValue()) {
						l.add(s);
						cpt++;
						if (cpt > 10) {
							break;
						}
					}
				}

				// display the list in p1
				// allow the user to display the results on the right by
				// clicking page names

			} catch (final IOException e1) {
				e1.printStackTrace();
			} catch (final Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	public static void main(final String[] args) {
		final GraphicalInterface f = new GraphicalInterface();
	}
}
