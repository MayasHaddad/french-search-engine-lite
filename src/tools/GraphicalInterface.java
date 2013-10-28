package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
				if (Const.NB_FILES_IN_CORPUS == null) {
					// throw new Exception("Nb of documents not calculated !");
					Const.NB_FILES_IN_CORPUS = IOManager.getNbFiles(new File(
							Const.PATH_TO_LITTLE_CORPUS));
				}
				System.out.println("nbFiles: " + Const.NB_FILES_IN_CORPUS);
				Searcher.getSimilarDocuments(this.j.getText(), new File(
						Const.PATH_TO_INVERTED_FILE_FROM_MERGER),
						Const.PATH_TO_WEIGHT_FILES, Const.NB_FILES_IN_CORPUS);
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
