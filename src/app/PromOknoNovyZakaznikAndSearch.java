package app;

import iListeners.VyberOrUpravListener;

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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import sablony.MyJButton;
import sablony.ParametryFiltr;
import sablony.tabulka.ColorCellTable;
import sablony.tabulka.TableColumnAdjuster;
import storage.SkladOdkazu;

import javax.swing.LayoutStyle.ComponentPlacement;

public class PromOknoNovyZakaznikAndSearch extends JPanel {
	
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;
	private SkladOdkazu sklad;
	private MainFrame hlavniOkno;
	
	
	private String [][] actionComands;
	
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
		
	private JTextField noveJmenoZakaznikaTextField;
	private ColorCellTable table;
	private JPanel panelVyhledej;
	private JPanel idZakaznikaPanel;
	private JLabel nadpis;
	private JButton pridatZakaznika;
	
	private ParametryFiltr panelFiltr; //JPanel
	//listener
	private VyberOrUpravListener vyberlistener;
	private MyJButton vyberUprav; // dulezity button
	private JScrollPane panelTabulka;
	private JLabel idZakaznikaText;
	private JLabel idZakaznika_1;
	private JLabel pridatZakaznikaLabel;
	private JLabel jmenoZakaznikaPridejLabel;
	private JPanel jpanelFitr;
	
	/**
	 * Nastaveni okna <code>PromOknoNovyZakaznikAndSearch</code> pro daný pøíkaz (JMenuItem). Zobrazí/skryje podle daných indexu JPanely v tomto JPanelu.
	 * @param i index <code>JMenuItem</code> v postraním menu
	 * @param j index <code>JMenuItem</code> v postraním menu
	 */
	public void nastavOkno(int i, int j){
		jpanelFitr.setVisible(true);
		table.setVisible(true);
		if(i == 2 && j == 1){ // vyjimka pro zmenu Odhadovane hmotnosti, je to vlastne uprava modelu.
			i = 5;
			j = 1;
		}
		vyberUprav.setActionCommand(actionComands[i][j]);
		if(i == 1 || i == 4 || i == 3 && j != 0){ // 1 = planovani, 4 = vypisy, 3 lici plany zakladni a planovaci
			setVisibleProhlizeny();
		}else {
			if(i == 0 && j == 0 || i == 6){ // pouze pro pridani noveho Zakaznika, vinika nebo vady
				setVisiblePridej();
			}else{
				setVisibleVyhledej();
			}
		}
		if(i == 7){ // 7 = smazani zakazky neni implemetovano
			//setVisibleProhlizeny();
			setVisibleVyhledej();
		}
		if(i == 8){ // zaloha DB
			setVisibleVyhledej();
		}
	}
	
	public void addListeners(){
		this.vyberlistener = new VyberOrUpravListener(this.sklad);
		this.vyberUprav.addActionListener(vyberlistener);
	}
	
	private void setVisibleProhlizeny(){
		panelVyhledej.setVisible(false);
		idZakaznikaPanel.setVisible(false);
	}
	
	private void setVisiblePridej(){
		panelVyhledej.setVisible(false);
		idZakaznikaPanel.setVisible(true);
	}
	private void setVisibleVyhledej(){
		panelVyhledej.setVisible(true);
		idZakaznikaPanel.setVisible(false);
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
		pridatZakaznika.setActionCommand("PridatZakaznikaAction");
		idZakaznikaText.setVisible(false);
		idZakaznika_1.setVisible(false);
		pridatZakaznikaLabel.setText("P\u0159idat nov\u00E9ho z\u00E1kazn\u00EDka");
		jmenoZakaznikaPridejLabel.setText("Jm\u00E9no z\u00E1kazn\u00EDka: ");
		//panelFiltr.setHledejZakazniky();	
		panelFiltr.setParametryFiltr(ParametryFiltr.HledejZakazniky);
	}
	
	public void setPridejVinika(){
		noveJmenoZakaznikaTextField.setText("");
		nadpis.setText("Seznam viníkù:");
		pridatZakaznika.setText("P\u0159idat");
		pridatZakaznika.setActionCommand("PridatVinikaAction");
		idZakaznikaText.setVisible(false);
		idZakaznika_1.setVisible(false);
		pridatZakaznikaLabel.setText("P\u0159idat nov\u00E9ho viníka");
		jmenoZakaznikaPridejLabel.setText("Jm\u00E9no viníka: ");
		
		//panelFiltr.setHledejViniky();	
		panelFiltr.setParametryFiltr(ParametryFiltr.HledejViniky);
	}
	
	public void setPridejVadu(){
		noveJmenoZakaznikaTextField.setText("");
		nadpis.setText("Seznam vad:");
		pridatZakaznika.setText("P\u0159idat");
		pridatZakaznika.setActionCommand("PridatVaduAction");
		idZakaznikaText.setVisible(false);
		idZakaznika_1.setVisible(false);
		pridatZakaznikaLabel.setText("P\u0159idat novou vadu");
		jmenoZakaznikaPridejLabel.setText("Jm\u00E9no vady: ");
		
		//panelFiltr.setHledejVady();
		panelFiltr.setParametryFiltr(ParametryFiltr.HledejVady);
	}
	
	public void setVyhledejZakaznika(){
		//panelFiltr.setHledejZakazniky();
		panelFiltr.setParametryFiltr(ParametryFiltr.HledejZakazniky);
		nadpis.setText("Seznam z\u00E1kazn\u00EDk\u016F:");
		this.vyberUprav.setText("Vyber z\u00E1kazn\u00EDka");
	}
	
	public void setVyhledejModel(){
		//panelFiltr.setHledejModely();
		panelFiltr.setParametryFiltr(ParametryFiltr.HledejModely);
		nadpis.setText("Seznam model\u016F:");
		this.vyberUprav.setText("Vyber model");
	}
	
	public void setVyhledejZakazku(){
		//panelFiltr.setHledejZakazky();
		panelFiltr.setParametryFiltr(ParametryFiltr.HledejZakazky);
		nadpis.setText("Seznam zak\u00E1zek:");
		this.vyberUprav.setText("Vyber zak\u00E1zku");
	}
	
	public void setVyhledejFyzKusy(){
		//panelFiltr.setHledejFyzKusy();
		panelFiltr.setParametryFiltr(ParametryFiltr.HledejFyzKusy);
		nadpis.setText("Seznam kus\u016F:");
		this.vyberUprav.setText("Vyber zak\u00E1zku");
	}
	
	public void setVyhledejZmetky(){
		//panelFiltr.setHledejZmetky();
		panelFiltr.setParametryFiltr(ParametryFiltr.HledejZmetky);
		nadpis.setText("Seznam kus\u016F:");
		this.vyberUprav.setText("Vyber zak\u00E1zku");
	}
	
	public void setVyhledejViniky(){
		//panelFiltr.setHledejViniky();
		panelFiltr.setParametryFiltr(ParametryFiltr.HledejViniky);
	}
	
	public void setVyhledejVady(){
		//panelFiltr.setHledejVady();
		panelFiltr.setParametryFiltr(ParametryFiltr.HledejVady);
	}
	
	public void setVyhledejKapPropocet(){
		//panelFiltr.setKapPropocet();
		panelFiltr.setParametryFiltr(ParametryFiltr.HledejKapacitniProcet);
	}
	
	public void setPlanovani(){
		this.setVyhledejUpravZakazku();
		setVisibleVyhledej();
		this.vyberUprav.setText("Pl\u00E1novat zak\u00E1zku");
	}

	public void setVyhledejUpravZakaznika(){
		this.setVyhledejZakaznika();
		vyberUprav.setText("Upravit z\u00E1kazn\u00EDka");
	}

	public void setVyhledejUpravModel(){
		this.setVyhledejModel();
		vyberUprav.setText("Upravit model");		
	}

	public void setVyhledejUpravZakazku(){
		this.setVyhledejZakazku();
		vyberUprav.setText("Upravit zak\u00E1zku");
	}
	
	public void setUpravZakaznika(String idZakaznika,String jmeno){
		this.setPridejZakaznika();
		setVisiblePridej();
		this.noveJmenoZakaznikaTextField.setText(jmeno);
		pridatZakaznika.setText("Uprav z\u00E1kazn\u00EDka");
		pridatZakaznika.setActionCommand("UpravZakaznikaAction");
		idZakaznikaText.setText(idZakaznika);
		idZakaznikaText.setVisible(true);
		idZakaznika_1.setVisible(true);
	}
	
	public void setUpravCisloTavby(){
		this.setVyhledejUpravZakazku();
		vyberUprav.setText("Zadat èíslo tavby");
	}
	
	public void setZadejOdlitek(){
		this.setVyhledejZakazku();
	}
	
	public void setPlanExpedice(){
		this.setVyhledejZakazku();
	}
	
	public void setZadejVycistenyKus(){
		this.setVyhledejZakazku();
	}
	
	public void setZalohaObnovaDB(boolean isZaloha){
		this.setVyhledejZakazku();
		if(isZaloha){
			vyberUprav.setText("Zálohovat databázi");
		} else {
			vyberUprav.setText("Obnovit databázi");
		}
		jpanelFitr.setVisible(false);
		table.setVisible(false);
	}
	
	public void setSmazFyzKusy(){
		this.setVyhledejZakazku();
		vyberUprav.setText("Vybrat zakázku");
		jpanelFitr.setVisible(true);
		table.setVisible(true);		
	}
	
	/**
	 * 
	 * @param hlavniOkno
	 * @throws SQLException 
	 */
	public PromOknoNovyZakaznikAndSearch(MainFrame hlavniOkno) throws SQLException {
		this.hlavniOkno = hlavniOkno;
		this.sklad = hlavniOkno.getSklad();
		//this.barvy = sklad.getBarvy();
		this.fonty = sklad.getFonty();
		this.actionComands = sklad.getCommands();
		setBorder(new LineBorder(barvy[6]));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel obal = new JPanel();
		obal.setBackground(barvy[12]);
		add(obal);
		obal.setBorder(new EmptyBorder(20, 20, 20, 20));
		obal.setLayout(new BoxLayout(obal, BoxLayout.Y_AXIS));
		
		idZakaznikaPanel = new JPanel();
		idZakaznikaPanel.setPreferredSize(new Dimension(10, 130));
		idZakaznikaPanel.setBackground(barvy[12]);
		idZakaznikaPanel.setMinimumSize(new Dimension(10, 130));
		idZakaznikaPanel.setMaximumSize(new Dimension(32767, 130));
		obal.add(idZakaznikaPanel);
		
		jmenoZakaznikaPridejLabel = new JLabel("Jm\u00E9no z\u00E1kazn\u00EDka: ");
		
		noveJmenoZakaznikaTextField = new JTextField();
		noveJmenoZakaznikaTextField.setColumns(10);
		sklad.setNoveJmenoZakaznikaTextField(noveJmenoZakaznikaTextField);
		
		pridatZakaznika = new MyJButton("P\u0159idat",16,1, sklad);
		pridatZakaznika.setPreferredSize(new Dimension(160, 27));
		pridatZakaznika.setActionCommand("PridatZakaznikaAction");
		pridatZakaznika.addActionListener(sklad.getNovyZakModelZakazka());
		
		pridatZakaznikaLabel = new JLabel("P\u0159idat nov\u00E9ho z\u00E1kazn\u00EDka");
		pridatZakaznikaLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		idZakaznika_1 = new JLabel("Id z\u00E1kazn\u00EDka:");
		
		idZakaznikaText = new JLabel("XX");
		sklad.setIdZakaznikaText(idZakaznikaText);
		
		

		GroupLayout gl_idZakaznikaPanel = new GroupLayout(idZakaznikaPanel);
		gl_idZakaznikaPanel.setHorizontalGroup(
			gl_idZakaznikaPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_idZakaznikaPanel.createSequentialGroup()
					.addGroup(gl_idZakaznikaPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_idZakaznikaPanel.createSequentialGroup()
							.addGap(2)
							.addComponent(jmenoZakaznikaPridejLabel)
							.addGap(10)
							.addComponent(noveJmenoZakaznikaTextField, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
							.addGap(32)
							.addComponent(idZakaznika_1)
							.addGap(18)
							.addComponent(idZakaznikaText))
						.addComponent(pridatZakaznikaLabel)
						.addComponent(pridatZakaznika, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(330, Short.MAX_VALUE))
		);
		gl_idZakaznikaPanel.setVerticalGroup(
			gl_idZakaznikaPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_idZakaznikaPanel.createSequentialGroup()
					.addGap(8)
					.addComponent(pridatZakaznikaLabel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_idZakaznikaPanel.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(jmenoZakaznikaPridejLabel)
						.addComponent(noveJmenoZakaznikaTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(idZakaznika_1)
						.addComponent(idZakaznikaText))
					.addGap(18)
					.addComponent(pridatZakaznika, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(31))
		);
		idZakaznikaPanel.setLayout(gl_idZakaznikaPanel);
		
		jpanelFitr = new JPanel();
		jpanelFitr.setMaximumSize(new Dimension(32767, 150));
		jpanelFitr.setBackground(barvy[11]);
		obal.add(jpanelFitr);
		jpanelFitr.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(150, 22));
		panel.setBackground(barvy[0]);
		jpanelFitr.add(panel, BorderLayout.WEST);
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
		btnNewButton_1.setIcon(new ImageIcon(PromOknoNovyZakaznikAndSearch.class.getResource("/app/lupa.jpg")));
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.anchor = GridBagConstraints.EAST;
		gbc_btnNewButton_1.fill = GridBagConstraints.VERTICAL;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 0;
		panel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JLabel lblNewLabel_1 = new JLabel("Filtr vyhled\u00E1v\u00E1n\u00ED ");
		lblNewLabel_1.setFont(fonty[3]);
		lblNewLabel_1.setForeground(barvy[11]);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 0;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		
		
		
		panelTabulka = new JScrollPane();
		
		//panelTabulka.setPreferredSize(new Dimension(200, 150));
		panelTabulka.setWheelScrollingEnabled(false); 
		panelTabulka.addMouseWheelListener(new MouseWheelListener() {
		    @Override
		    public void mouseWheelMoved(MouseWheelEvent e) {
		    	panelTabulka.getParent().dispatchEvent(e);
		    }
		});
		
		
		
		
		
		
		
		panelVyhledej = new JPanel();
		panelVyhledej.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelVyhledej.setBackground(barvy[12]);
		panelVyhledej.setPreferredSize(new Dimension(10, 45));
		panelVyhledej.setMaximumSize(new Dimension(32767, 40));
		obal.add(panelVyhledej);
		
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
		
		
		
		
		JPanel panelNadpis = new JPanel();
		panelNadpis.setMaximumSize(new Dimension(32767, 50));
		panelNadpis.setPreferredSize(new Dimension(10, 50));
		panelNadpis.setBackground(barvy[12]);
		obal.add(panelNadpis);
		
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
		obal.add(panelTabulka);
		
		//moje Interaktivni tabulka, jen dodat model do new ColorTabulka(qtm)
			
		table = new ColorCellTable(sklad.getPrazdneTabulky()[3], panelTabulka, false, sklad);
		sklad.setPromOknoNovyZakaznikSearchColorTable(table);

		TableColumnAdjuster pom = table.getColumAdjuster();
		sklad.setPromOknoNovyZakaznikAndSearchColumAdjuster(pom);

		panelFiltr = new ParametryFiltr(this.sklad, table);
		jpanelFitr.add(panelFiltr, BorderLayout.SOUTH);
					
		
		
		panelTabulka.setViewportView(table);
		
		
		
		addListeners();
		
		//status
		//defaultne 
		setVyhledejZakazku();
		setVisibleProhlizeny();
	}
	
	public void setPanelFiltrVypisy(int j){
		panelFiltr.setVypisy(j);
	}
	
	public void setPanelFiltrSetPlanyLiti(boolean pom){
		// panelFiltr.setPlanyLiti(pom);
		if(pom){
			panelFiltr.setParametryFiltr(ParametryFiltr.ZaklPlanLiti);
		} else {
			panelFiltr.setParametryFiltr(ParametryFiltr.PlanovaniLiti);
		}
	}
	
	public void setPanelFiltrSetPlanExpedice(int j){
		//panelFiltr.setPlanExpedice(j);
		panelFiltr.setParametryFiltr(ParametryFiltr.PlanExpedice);
	}
	

	// atributy
	public JScrollPane getPanelTabulka() {
		return panelTabulka;
	}
}

