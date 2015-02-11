package app;

import interfaces.VyberOrUpravListener;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import java.awt.Dimension;

import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;

import sablony.MyJButton;
import sablony.ParametryFiltr;
import sablony.tabulka.ColorCellTable;
import sablony.tabulka.TableColumnAdjuster;
import storage.SkladOdkazu;








import javax.swing.border.LineBorder;

import java.sql.Connection;

import myinterface.NastavOkno;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ScrollPaneConstants;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Copy_2_of_PromOknoNovyZakaznikAndSearch extends JPanel implements NastavOkno {
	
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;
	private SkladOdkazu sklad;
	private MainFrame hlavniOkno;
	
	
	private String [][] actionComands;
	private String [][] nadpisy;
	
	private Color [] barvy = {
	  	    new Color(63,63,63),       //0 cerna hlavicka
	        new Color(88, 88, 87),     //1 mene cerna (pismo)
	        new Color(98, 98, 98),     //2 mene cerna (pismo)
	        new Color(106, 200, 235),  //3 azurova (odlhasit)
	        new Color(112, 216, 255),  //4 azurova svetlejsi (button)
	        new Color(187, 187, 187),  //5 seda (sipky u navigatoru)
	        new Color(196, 196, 196),  //6 seda okraje oken
	        new Color(197, 197, 197),  //7 seda barva (nadpis header, a prihlas, uziv)
	        new Color(227, 227, 226),  //8 seda pozadi aplikace 
			new Color(232, 232, 232),  //9 ohraniceni tabulky
	        new Color(240, 240, 240),  //10 pozadi tlaèítek
	        new Color(246, 246, 246),  //11 bile pismo v tabulce - novyzakaznik
			new Color(249, 249, 249),  //12 Bíle pozadí vedlejsi okna
	  	    new Color(59,59,59), 	   //13 cerna barva ve tlaèitku vyhledavat u tabulky
	  	    new Color(72,72,72), 	   //14 cerna barva, hlavicka tabulky
		    new Color(111,111,111),	   //15 ohranièeni tlaèitka
		    new Color(220,220,220),    //16 selected row color
		    new Color(232, 232, 232),  //17 pozadí tlaèítka Pøidat 
	  	    new Color(243, 247, 249),  //18 pozadí tabulky radku (modrejsi)
	  	    new Color(155,214,246)	   //19 pozadi tabulky pri zmene Azurova
	};
	
	private Font [] fonty;
	
	private Connection conn;
	
	private JTextField noveJmenoZakaznikaTextField;
	private JTextField textField_1;
	private ColorCellTable table;
	private JPanel panelVyhledej;
	private JPanel panelPridej;
	private JLabel nadpis;
	private JButton pridatZakaznika;
	
	private ParametryFiltr panelFiltr; //JPanel
	//listener
	private VyberOrUpravListener vyberlistener;
	private MyJButton vyberUprav; // dulezity button
	
	public void nastavOkno(int i, int j){
		vyberUprav.setActionCommand(actionComands[i][j]);
		System.out.println("nastaveno "+actionComands[i][j]);
		if(i == 1){
			setVisibleProhlizeny();
		}else {
			if(i == 0 && j == 0){
				setVisiblePridej();
			}else{
				setVisibleVyhledej();
			}
		}
	}
	
	private void setVisibleProhlizeny(){
		panelVyhledej.setVisible(false);
		panelPridej.setVisible(false);
	}
	
	private void setVisiblePridej(){
		panelVyhledej.setVisible(false);
		panelPridej.setVisible(true);
	}
	private void setVisibleVyhledej(){
		panelVyhledej.setVisible(true);
		panelPridej.setVisible(false);
	}
	
	public void setNovaZakazkaVyberModel(){
		setVyhledejModel();
		setVisibleVyhledej();
		vyberUprav.setText("Vybrat model");
		vyberUprav.setActionCommand("NovaZakazkaVyberModel");
	}
	
	public void setNovaZakazkaVyberZakaznika(){
		setVyhledejUpravZakaznika();
		setVisibleVyhledej();
		vyberUprav.setText("Vybrat zákazníka");
		vyberUprav.setActionCommand("NovaZakazkaVyberZakaznika");
	}
	
	public void setPridejZakaznika(){
		noveJmenoZakaznikaTextField.setText("");
		nadpis.setText("Seznam z\u00E1kazn\u00EDk\u016F:");
		pridatZakaznika.setText("P\u0159idat");
		panelFiltr.setZakaznik();		
	}
	
	public void setVyhledejZakaznika(){
		panelFiltr.setZakaznik();
		nadpis.setText("Seznam z\u00E1kazn\u00EDk\u016F:");
		this.vyberUprav.setText("Vyber z\u00E1kazn\u00EDka");
	}
	
	public void setVyhledejModel(){
		panelFiltr.setModel();
		nadpis.setText("Seznam model\u016F:");
		this.vyberUprav.setText("Vyber model");
	}
	
	public void setVyhledejZakazku(){
		panelFiltr.setZakazka();
		nadpis.setText("Seznam zak\u00E1zek:");
		this.vyberUprav.setText("Vyber zak\u00E1zku");
	}
	
	public void setVyhledejFyzKusy(){
		panelFiltr.setFyzKusy();
		nadpis.setText("Seznam kus\u016F:");
		this.vyberUprav.setText("Vyber zak\u00E1zku");
	}
	
	public void setVyhledejZmetky(){
		panelFiltr.setZmetky();
		nadpis.setText("Seznam kus\u016F:");
		this.vyberUprav.setText("Vyber zak\u00E1zku");
	}
	
	public void setPlanovani(){
		this.setVyhledejUpravZakazku();
		setVisibleVyhledej();
		this.vyberUprav.setText("Pl\u00E1novat zak\u00E1zku");
	}

	public void setVyhledejUpravZakaznika(){
		this.setVyhledejZakaznika();
		vyberUprav.setText("Upravit vybran\u00E9ho z\u00E1kazn\u00EDka");
	}

	public void setVyhledejUpravModel(){
		this.setVyhledejModel();
		vyberUprav.setText("Upravit vybran\u00FD model");		
	}

	public void setVyhledejUpravZakazku(){
		this.setVyhledejZakazku();
		vyberUprav.setText("Upravit vybranou zak\u00E1zku");
	}
	
	public void setUpravZakaznika(String jmeno){
		this.setPridejZakaznika();
		setVisiblePridej();
		this.noveJmenoZakaznikaTextField.setText(jmeno);
		pridatZakaznika.setText("Uprav z\u00E1kazn\u00EDka");
	}
	
	public void setUpravCisloTavby(){
		this.setVyhledejUpravZakazku();
		vyberUprav.setText("Zadat èíslo tavby");
	}
	
	public void setZadejOdlitek(){
		this.setVyhledejZakazku();
	}
	
	public void setZadejVycistenyKus(){
		this.setVyhledejZakazku();
	}
	
	
	public void addListeners(){
		this.vyberlistener = new VyberOrUpravListener(this.sklad);
		this.vyberUprav.addActionListener(vyberlistener);
	}
	
	/**
	 * Create the panel.
	 */
		
	public Copy_2_of_PromOknoNovyZakaznikAndSearch(MainFrame hlavniOkno) {
		this.hlavniOkno = hlavniOkno;
		this.sklad = hlavniOkno.getSklad();
		this.conn = sklad.getConn();
		//this.barvy = sklad.getBarvy();
		this.fonty = sklad.getFonty();
		this.actionComands = sklad.getCommands();
		setBorder(new LineBorder(barvy[6]));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel obal = new JPanel();
		obal.setBackground(barvy[12]);
		add(obal);
		obal.setBorder(new EmptyBorder(20, 20, 20, 20));
		GridBagLayout gbl_obal = new GridBagLayout();
		gbl_obal.columnWidths = new int[]{910, 0, 0};
		gbl_obal.rowHeights = new int[]{0, 0, 0, 0, 226, 0};
		gbl_obal.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_obal.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		obal.setLayout(gbl_obal);
		
		panelPridej = new JPanel();
		panelPridej.setPreferredSize(new Dimension(10, 130));
		panelPridej.setBackground(barvy[12]);
		panelPridej.setMinimumSize(new Dimension(10, 130));
		panelPridej.setMaximumSize(new Dimension(32767, 130));
		GridBagConstraints gbc_panelPridej = new GridBagConstraints();
		gbc_panelPridej.fill = GridBagConstraints.BOTH;
		gbc_panelPridej.insets = new Insets(0, 0, 5, 5);
		gbc_panelPridej.gridx = 0;
		gbc_panelPridej.gridy = 0;
		obal.add(panelPridej, gbc_panelPridej);
		
		JLabel lblNewLabel = new JLabel("Jm\u00E9no z\u00E1kazn\u00EDka: ");
		
		noveJmenoZakaznikaTextField = new JTextField();
		noveJmenoZakaznikaTextField.setColumns(10);
		sklad.setNoveJmenoZakaznikaTextField(noveJmenoZakaznikaTextField);
		
		pridatZakaznika = new MyJButton("P\u0159idat",16,1, sklad);
		pridatZakaznika.setPreferredSize(new Dimension(160, 27));
		pridatZakaznika.setActionCommand("PridatZakaznikaAction");
		pridatZakaznika.addActionListener(sklad.getNovyZakModelZakazka());
		
		
		JLabel lblNezapomenZeChce = new JLabel("Nezapomen ze chce\u0161 aby se v uprave zakazky apod menilo pozadi kdy\u017E se n\u011Bco zm\u011Bn\u00ED");
		
		JLabel lblPidatNovhoZkaznka = new JLabel("P\u0159idat nov\u00E9ho z\u00E1kazn\u00EDka");
		lblPidatNovhoZkaznka.setFont(new Font("Tahoma", Font.BOLD, 12));
		

		GroupLayout gl_panelPridej = new GroupLayout(panelPridej);
		gl_panelPridej.setHorizontalGroup(
			gl_panelPridej.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelPridej.createSequentialGroup()
					.addGroup(gl_panelPridej.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelPridej.createSequentialGroup()
							.addGap(2)
							.addComponent(lblNewLabel)
							.addGap(10)
							.addComponent(noveJmenoZakaznikaTextField, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
							.addGap(62)
							.addComponent(lblNezapomenZeChce))
						.addComponent(lblPidatNovhoZkaznka)
						.addComponent(pridatZakaznika, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(393, Short.MAX_VALUE))
		);
		gl_panelPridej.setVerticalGroup(
			gl_panelPridej.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelPridej.createSequentialGroup()
					.addGap(8)
					.addComponent(lblPidatNovhoZkaznka)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelPridej.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(lblNewLabel)
						.addComponent(noveJmenoZakaznikaTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNezapomenZeChce))
					.addGap(18)
					.addComponent(pridatZakaznika, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(31))
		);
		panelPridej.setLayout(gl_panelPridej);
		
				panelFiltr = new ParametryFiltr(this.sklad, table);
				
				JPanel panelFitr = new JPanel();
				panelFitr.setMaximumSize(new Dimension(32767, 150));
				panelFitr.setBackground(barvy[11]);
				GridBagConstraints gbc_panelFitr = new GridBagConstraints();
				gbc_panelFitr.gridwidth = 2;
				gbc_panelFitr.fill = GridBagConstraints.BOTH;
				gbc_panelFitr.insets = new Insets(0, 0, 5, 5);
				gbc_panelFitr.gridx = 0;
				gbc_panelFitr.gridy = 1;
				obal.add(panelFitr, gbc_panelFitr);
				panelFitr.setLayout(new BorderLayout(0, 0));
				
				JPanel panel = new JPanel();
				panel.setPreferredSize(new Dimension(150, 22));
				panel.setBackground(barvy[0]);
				panelFitr.add(panel, BorderLayout.WEST);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.columnWidths = new int[] {30, 50, 10};
				gbl_panel.columnWeights = new double[]{0.0};
				gbl_panel.rowWeights = new double[]{0.0};
				panel.setLayout(gbl_panel);
				
				JButton btnNewButton_1 = new JButton("");
				btnNewButton_1.setFocusable(false);
				btnNewButton_1.setFocusPainted(false);
				btnNewButton_1.setBorder(null);
				btnNewButton_1.setPreferredSize(new Dimension(20, 9));
				
				btnNewButton_1.setContentAreaFilled(false);
				btnNewButton_1.setIcon(new ImageIcon(Copy_2_of_PromOknoNovyZakaznikAndSearch.class.getResource("/app/lupa.jpg")));
				GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
				gbc_btnNewButton_1.anchor = GridBagConstraints.EAST;
				gbc_btnNewButton_1.fill = GridBagConstraints.VERTICAL;
				gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
				gbc_btnNewButton_1.gridx = 0;
				gbc_btnNewButton_1.gridy = 0;
				panel.add(btnNewButton_1, gbc_btnNewButton_1);
				
				JLabel lblNewLabel_1 = new JLabel("Filtr vyhled\u00E1v\u00E1n\u00ED ");
				lblNewLabel_1.setFont(fonty[0]);
				lblNewLabel_1.setForeground(barvy[11]);
				GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
				gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
				gbc_lblNewLabel_1.fill = GridBagConstraints.BOTH;
				gbc_lblNewLabel_1.gridx = 1;
				gbc_lblNewLabel_1.gridy = 0;
				panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
				panelFitr.add(panelFiltr, BorderLayout.SOUTH);
		
		
		
		
		
		
		
		panelVyhledej = new JPanel();
		panelVyhledej.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelVyhledej.setBackground(barvy[12]);
		panelVyhledej.setPreferredSize(new Dimension(10, 45));
		panelVyhledej.setMaximumSize(new Dimension(32767, 40));
		GridBagConstraints gbc_panelVyhledej = new GridBagConstraints();
		gbc_panelVyhledej.fill = GridBagConstraints.BOTH;
		gbc_panelVyhledej.insets = new Insets(0, 0, 5, 5);
		gbc_panelVyhledej.gridx = 0;
		gbc_panelVyhledej.gridy = 2;
		obal.add(panelVyhledej, gbc_panelVyhledej);
		
		vyberUprav = new MyJButton("Vyber z\u00E1kazn\u00EDka",16,1, sklad);
		vyberUprav.setPreferredSize(new Dimension(170, 30));
		GroupLayout gl_panelVyhledej = new GroupLayout(panelVyhledej);
		gl_panelVyhledej.setHorizontalGroup(
			gl_panelVyhledej.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelVyhledej.createSequentialGroup()
					.addComponent(vyberUprav, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(740, Short.MAX_VALUE))
		);
		gl_panelVyhledej.setVerticalGroup(
			gl_panelVyhledej.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelVyhledej.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(vyberUprav, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		panelVyhledej.setLayout(gl_panelVyhledej);
		
		
		
		
		JScrollPane panelTabulka = new JScrollPane();
		
		//panelTabulka.setPreferredSize(new Dimension(200, 150));
		panelTabulka.setWheelScrollingEnabled(false); 
		panelTabulka.addMouseWheelListener(new MouseWheelListener() {
		    @Override
		    public void mouseWheelMoved(MouseWheelEvent e) {
		    	panelTabulka.getParent().dispatchEvent(e);
		    }
		});
		
		
		
		
		JPanel panelNadpis = new JPanel();
		panelNadpis.setMaximumSize(new Dimension(32767, 50));
		panelNadpis.setPreferredSize(new Dimension(10, 50));
		panelNadpis.setBackground(barvy[12]);
		GridBagConstraints gbc_panelNadpis = new GridBagConstraints();
		gbc_panelNadpis.fill = GridBagConstraints.BOTH;
		gbc_panelNadpis.insets = new Insets(0, 0, 5, 5);
		gbc_panelNadpis.gridx = 0;
		gbc_panelNadpis.gridy = 3;
		obal.add(panelNadpis, gbc_panelNadpis);
		
		nadpis = new JLabel("Seznam z\u00E1kazn\u00EDk\u016F:");
		nadpis.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GroupLayout gl_panelNadpis = new GroupLayout(panelNadpis);
		gl_panelNadpis.setHorizontalGroup(
			gl_panelNadpis.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelNadpis.createSequentialGroup()
					.addComponent(nadpis, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(994, Short.MAX_VALUE))
		);
		gl_panelNadpis.setVerticalGroup(
			gl_panelNadpis.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelNadpis.createSequentialGroup()
					.addContainerGap(23, Short.MAX_VALUE)
					.addComponent(nadpis)
					.addContainerGap())
		);
		panelNadpis.setLayout(gl_panelNadpis);
		GridBagConstraints gbc_panelTabulka = new GridBagConstraints();
		gbc_panelTabulka.gridwidth = 2;
		gbc_panelTabulka.insets = new Insets(0, 0, 0, 5);
		gbc_panelTabulka.fill = GridBagConstraints.BOTH;
		gbc_panelTabulka.gridx = 0;
		gbc_panelTabulka.gridy = 4;
		obal.add(panelTabulka, gbc_panelTabulka);
		
		//moje Interaktivni tabulka, jen dodat model do new ColorTabulka(qtm)
		table = new ColorCellTable(sklad.getPrazdneTabulky()[3], panelTabulka, false, sklad);
		sklad.setPromOknoNovyZakaznikSearchColorTable(table);
		
		TableColumnAdjuster pom = table.getColumAdjuster();
		sklad.setPromOknoNovyZakaznikAndSearchColumAdjuster(pom);				
						
						
		
		
		
		
		
		
		panelTabulka.setViewportView(table);
		
		
		panelTabulka.setViewportView(table);
		
		
		
		
		
		
		
		
		
		
		
		addListeners();
		
		//status
		//defaultne 
		setVyhledejZakazku();
		setVisibleProhlizeny();
		//setVisiblePridej();
		//zkouska		
		//setUpravZakaznika();		
	}

	public JPanel getPanelVyhledej() {
		return panelVyhledej;
	}
	public JPanel getPanelPridej() {
		return panelPridej;
	}
	public JLabel getNadpis() {
		return nadpis;
	}
	public JPanel getListParametru() {
		return panelFiltr;
	}
}

