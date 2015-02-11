package app;

import javax.swing.JPanel;

import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class Hlavicka extends JPanel {

	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public Hlavicka() {
		setPreferredSize(new Dimension(1366, 50));
		setMinimumSize(new Dimension(1366, 50));
		setMaximumSize(new Dimension(32767, 50));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGap(0, 1366, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGap(0, 50, Short.MAX_VALUE)
		);
		setLayout(groupLayout);

	}
}
