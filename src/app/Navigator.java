package app;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JLabel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import storage.SkladOdkazu;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Navigator extends JPanel {
	
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;
	private JLabel [] navigatorLabels;
	private SkladOdkazu sklad;
	private Font [] fonty = {
			new Font("Tahoma", Font.PLAIN, 12),
			new Font("Tahoma", Font.PLAIN, 20),
			new Font("Tahoma", Font.BOLD, 14),
			new Font("Tahoma", Font.PLAIN, 13)
	};
	
	/**
	 * Vlastni RGB barvy<br>
	 * new Color(63,63,63),       //0 cerna hlavicka <br>
	 * new Color(88, 88, 87),     //1 mene cerna (pismo)<br>
	 * new Color(98, 98, 98),     //2 mene cerna (pismo)<br>
	 * new Color(106, 200, 235),  //3 azurova (odlhasit)<br>
	 * new Color(112, 216, 255),  //4 azurova svetlejsi (button)<br>
	 * new Color(187, 187, 187),  //5 seda (sipky u navigatoru)<br>
	 * new Color(196, 196, 196),  //6 seda okraje oken<br>
	 * new Color(197, 197, 197),  //7 seda barva (nadpis header, a prihlas, uziv)<br>
	 * new Color(227, 227, 226),  //8 seda pozadi aplikace <br>
	 * new Color(232, 232, 232),  //9 ohraniceni tabulky<br>
	 * new Color(240, 240, 240),  //10 pozadi tlaèítek<br>
	 * new Color(246, 246, 246),  //11 bile pismo v tabulce - novyzakaznik<br>
	 * new Color(249, 249, 249),  //12 Bíle pozadí vedlejsi okna<br>
	 * new Color(59,59,59), 	  //13 cerna barva ve tlaèitku vyhledavat u tabulky<br>
	 * new Color(72,72,72), 	  //14 cerna barva, hlavicka tabulky<br>
	 * new Color(111,111,111),	  //15 ohranièeni tlaèitka<br>
	 * new Color(220,220,220),    //16 selected row color<br>
	 * new Color(232, 232, 232),  //17 pozadí tlaèítka Pøidat <br>
	 * new Color(243, 247, 249),  //18 pozadí tabulky radku (modrejsi)<br>
	 * new Color(155,214,246)	  //19 pozadi tabulky pri zmene Azurova<br>
	 */
	private Color [] barvy;
	
	/**
	 * Prozatím mi staci pouze dve jmena, indexy už znam pøedem (viz <code>MainActionMenuItemListener</code>
	 *  v metode <code>actionPerformed()</code>). V budoucnu muzu rozsirit.
	 */
	public void setNavigatorLabels(String label2, String label4){
		navigatorLabels[2].setText(label2);
		navigatorLabels[4].setText(label4);
	}
	
	/**
	 * Create the panel.
	 */
	public Navigator(SkladOdkazu sklad) {
		this.sklad = sklad;
		this.barvy = sklad.getBarvy();
		navigatorLabels = new JLabel [7];		
		this.sklad.setNavigator(this);
		
		
		setBackground(barvy[8]);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{25, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 17, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.insets = new Insets(15, 0, 0, 5);
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 0;
		add(separator, gbc_separator);
		
		JPanel panel = new JPanel();
		panel.setBackground(barvy[8]);
		
		JLabel lblNewLabel = new JLabel("Sl\u00E9v\u00E1rna Stra\u0161ice");
		lblNewLabel.setForeground(barvy[1]);
		lblNewLabel.setFont(fonty[0]);
		navigatorLabels[0] = lblNewLabel;
		
		JLabel lblNewLabel_1 = new JLabel(">>>");
		lblNewLabel_1.setForeground(barvy[5]);
		navigatorLabels[1] = lblNewLabel_1;
		
		JLabel lblNewLabel_2 = new JLabel("Pl\u00E1nov\u00E1n\u00ED a prohl\u00ED\u017Een\u00ED");
		lblNewLabel_2.setForeground(barvy[1]);
		lblNewLabel_2.setFont(fonty[0]);
		navigatorLabels[2] = lblNewLabel_2;
		
		JLabel lblNewLabel_3 = new JLabel(">>>");
		lblNewLabel_3.setForeground(barvy[5]);
		navigatorLabels[3] = lblNewLabel_3;
		
		JLabel lblNewLabel_4 = new JLabel("Prohl\u00ED\u017Een\u00ED zak\u00E1zek");
		lblNewLabel_4.setForeground(barvy[1]);
		lblNewLabel_4.setFont(fonty[0]);
		navigatorLabels[4] = lblNewLabel_4;
		
		JLabel lblNewLabel_5 = new JLabel(">>>");
		lblNewLabel_5.setForeground(barvy[5]);
		lblNewLabel_5.setVisible(false);
		navigatorLabels[5] = lblNewLabel_5;
		
		JLabel lblNewLabel_6 = new JLabel("Empty");
		lblNewLabel_6.setForeground(barvy[1]);
		lblNewLabel_6.setFont(fonty[0]);
		lblNewLabel_6.setVisible(false);
		navigatorLabels[6] = lblNewLabel_6;
		
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(1, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(5)
					.addComponent(lblNewLabel)
					.addGap(5)
					.addComponent(lblNewLabel_1)
					.addGap(5)
					.addComponent(lblNewLabel_2)
					.addGap(5)
					.addComponent(lblNewLabel_3)
					.addGap(5)
					.addComponent(lblNewLabel_4)
					.addGap(5)
					.addComponent(lblNewLabel_5)
					.addGap(5)
					.addComponent(lblNewLabel_6))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(0)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_1)
						.addComponent(lblNewLabel_2)
						.addComponent(lblNewLabel_3)
						.addComponent(lblNewLabel_4)
						.addComponent(lblNewLabel_5)
						.addComponent(lblNewLabel_6)))
		);
		panel.setLayout(gl_panel);
		
		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.insets = new Insets(0, 0, 0, 5);
		gbc_separator_1.gridx = 1;
		gbc_separator_1.gridy = 2;
		add(separator_1, gbc_separator_1);
	}
}
