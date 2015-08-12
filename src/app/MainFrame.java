package app;

import iListeners.MainActionMenuItemListener;
import iListeners.MainSideJButtonListener;
import iListeners.Odhlasit;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.sql.Connection;
import java.sql.SQLException;

import sablony.MyJButton;
import sablony.MyPopUp;
import storage.SkladOdkazu;

public class MainFrame extends JFrame {	
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;

	private SkladOdkazu sklad;
	
	private JScrollPane scrollPane;
	private JPanel contentPane;
	private JPanel telo;
	private JButton odhlasit = null;
	
	private JLabel [] hlavickaLabels;
	
	private JPanel [] promOkna = null;
	private PromOknoNovyZakaznikAndSearch promOknoNovyZakaznikAndSearch;
	private PromOknoNovyModel promOknoNovyModel;
	private PromOknoNovaZakazka promOknoNovaZakazka;
	private Planovani planovani;
	private ExpediceZmetek expedicezmetek;
	private JPanel vedlejsiOkno = null;
	private JPanel obalVedlejsihoOkna = null;
	
	private MyJButton [] sideListButton;
	private MyPopUp [] sidePopupMenulist;
	private JMenuItem [][] sidePopupMenuItems;
			
	private MainSideJButtonListener sideButtonListener;
	private MainActionMenuItemListener itemList;
	
	/**
	 * new Font("Tahoma", Font.PLAIN, 13), font v hlavicka Labels (odhlasit apod))
	 * new Font("Tahoma", Font.PLAIN, 20),
	 * new Font("Tahoma", Font.BOLD, 14),
	 * new Font("Tahoma", Font.PLAIN, 14), font v postraním menu v tlaèítkách
	 * new Font("Tahoma", Font.PLAIN, 15), font v ColorCellTable, velikost písma v tabulce
	 */
	private Font [] fonty = {
			new Font("Tahoma", Font.PLAIN, 13),
			new Font("Tahoma", Font.PLAIN, 20),
			new Font("Tahoma", Font.BOLD, 14),
			new Font("Tahoma", Font.PLAIN, 14),
			new Font("Tahoma", Font.PLAIN, 15)
			};
	
	/**
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
	private Color [] barvy = {
	  	    new Color(63,63,63),       //0 cerna hlavicka
	        new Color(51, 51, 51),     //1 mene cerna (pismo)
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
	  	    new Color(155,214,246),	   //19 pozadi tabulky pri zmene Azurova
	  	    new Color(153,153,153) 	   //20 pozadi ve hledej filtru 
	};
			

	public SkladOdkazu getSklad() {
		return sklad;
	}
		
	public void createAndAddListeners(){
		this.sideButtonListener = new MainSideJButtonListener(this.sideListButton,this.sidePopupMenulist, sklad);
		sklad.setListener(sideButtonListener);
		
		for(int i = 0; i < this.sideListButton.length; i++){
			sideListButton[i].addMouseListener(sideButtonListener);
			sideListButton[i].addFocusListener(sideButtonListener);
		}
		
		itemList = new MainActionMenuItemListener(sklad, promOknoNovyZakaznikAndSearch, promOknoNovyModel, promOknoNovaZakazka);
		sklad.setItemList(itemList);
		for(int i = 0; i < sidePopupMenuItems.length; i++){
			for(int j = 0; j < sidePopupMenuItems[i].length; j++){
				sidePopupMenuItems[i][j].addActionListener(itemList);
			}
		}
	}
	
	public void initPopUpMenus(MyPopUp [] sidePopupMenulist, JMenuItem [][] sidePopupMenuItems, SkladOdkazu sklad){
		//sidePopupMenulist = new MyPopUp [sideListButton.length];
		//sidePopupMenuItems = new JMenuItem [sideListButton.length][];
		
		sklad.setSidePopupMenulist(sidePopupMenulist);
		sklad.setSidePopupMenuItems(sidePopupMenuItems);
		int i = 0;
		
		String [] jmena0 = {"Nov\u00FD z\u00E1kazn\u00EDk", "Nov\u00FD model", "Nov\u00E1 zak\u00E1zka"};
		sidePopupMenulist[i] = new MyPopUp(jmena0, sidePopupMenuItems, i, sklad);
		
		i = 1;
		String [] jmena1 = {"Pl\u00E1nov\u00E1n\u00ED lit\u00ED", "Prohl\u00ED\u017Een\u00ED z\u00E1kazn\u00EDk\u016F", "Prohl\u00ED\u017Een\u00ED model\u016F",
				"Prohl\u00ED\u017Een\u00ED zak\u00E1zek", "Prohl\u00ED\u017Een\u00ED fyzick\u00FDch kus\u016F", "Prohl\u00ED\u017Een\u00ED zmetky",
				"Prohlížení viníkù", "Prohlížení vad", "Kapacitní propoèet"}; 
		sidePopupMenulist[i] = new MyPopUp(jmena1, sidePopupMenuItems, i, sklad);
		
		i = 2;
		// String [] jmena2 = {"Zadat nov\u00FD odlitek/zmetek", "Zadat vy\u010Dist\u011Bn\u00FD kus", "Zadat \u010C\u00EDslo tavby", "Zadat odhadovou hmotnost"};
		String [] jmena2 = {"Zadat nov\u00FD odlitek/zmetek", "Zadat odhadovou hmotnost"};
		sidePopupMenulist[i] = new MyPopUp(jmena2, sidePopupMenuItems, i, sklad);
		
		i = 3;
		String [] jmena3 = {"Expedice a \"pøenos do archivu\"", "Licí plán – základní", "Licí plán – plánovací", "Plán expedice"};
		sidePopupMenulist[i] = new MyPopUp(jmena3, sidePopupMenuItems, i, sklad);
		
		i = 4;
		String [] jmena4 = {
				"Výpis stavu neuzavøených zakázek", "Výpis odlitých kusù ke dni", "V\u00FDpis vy\u010Di\u0161t\u011Bn\u00FDch kus\u016F za obdob\u00ED",
				"Mzdy sl\u00E9va\u010D\u016F za období", "V\u00FDpis odlitk\u016F v kg/k\u010D za obdob\u00ED - bez zmetkù", "V\u00FDpis odlit\u00FDch kus\u016F za obdob\u00ED - dle materiálu", 
				"V\u00FDpis polo\u017Eek s odhadovanou hmotnosti", "V\u00FDpis zakázek s term\u00EDnem expedice v daném týdnu",
				 "V\u00FDpis expedice zboží za obdob\u00ED", "V\u00FDpis zpo\u017Ed\u011Bné v\u00FDroby ke dni",
				 "Inventura rozpracovan\u00E9 v\u00FDroby", "V\u00FDpis skladu ke dne\u0161n\u00EDmu dni", "Výpis zmetkù za období",
				 "Výpis viníkù v kg/kè za období - mzdy"}; // , "Výpis zmetkù za období - mzdy"
		sidePopupMenulist[i] = new MyPopUp(jmena4, sidePopupMenuItems, i, sklad);
		
		i = 5;
		String [] jmena5 = {"\u00DAprava z\u00E1kazn\u00EDka", "\u00DAprava modelu", "\u00DAprava zak\u00E1zky"}; // , "Zadat \u010C\u00EDslo tavby"
		sidePopupMenulist[i] = new MyPopUp(jmena5, sidePopupMenuItems, i, sklad);
		
		i = 6;
		String [] jmena6 = {"Pøidat viníky", "Pøidat druhy vad", "Upravit viníka", "Upravit vadu"};
		sidePopupMenulist[i] = new MyPopUp(jmena6, sidePopupMenuItems, i, sklad);
		
		i = 7;
		//String [] jmena7 = {"Smazat z\u00E1kazn\u00EDka", "Smazat model", "Smazat zak\u00E1zku"};
		String [] jmena7 = {"Smazat fyzické kusy"};
		sidePopupMenulist[i] = new MyPopUp(jmena7, sidePopupMenuItems, i, sklad);
		
		i = 8;
		String [] jmena8 = {"Záloha databáze", "Obnova databáze"};
		sidePopupMenulist[i] = new MyPopUp(jmena8, sidePopupMenuItems, i, sklad);
	}
	
	public MyJButton [] initJButtons(SkladOdkazu sklad){
		MyJButton [] sideListButton = new MyJButton [9];
		sideListButton[0] = new MyJButton("Nov\u00E1 zak\u00E1zka",10,1, sklad);
		sideListButton[1] = new MyJButton("Pl\u00E1nov\u00E1n\u00ED a prohl\u00ED\u017Een\u00ED",10,1, sklad);	
		sideListButton[2] = new MyJButton("Zad\u00E1v\u00E1n\u00ED a \u00FAprava odlitk\u016F", 10, 1, sklad);
		sideListButton[3] = new MyJButton("Expedice a licí plány", 10, 1, sklad);
		sideListButton[4] = new MyJButton("V\u00FDpisy", 10, 1, sklad);
		sideListButton[5] = new MyJButton("\u00DAprava zak\u00E1zky",10,1, sklad);
		sideListButton[6] = new MyJButton("Pøidat vady nebo viníky", 10, 1, sklad);
		sideListButton[7] = new MyJButton("Smazat fyz. kusy", 10, 1, sklad);
		sideListButton[8] = new MyJButton("Záloha a obnova databáze", 10, 1, sklad);
		
		sklad.setSideListButton(sideListButton);
		return sideListButton;
	}
	
	private void initWindows(JProgressBar progresBar) throws SQLException{
		promOkna = new JPanel [5];
		sklad.setPromOkna(promOkna);
		
		
		planovani = new Planovani(this);
		progresBar.setValue(55);
		expedicezmetek = new ExpediceZmetek(this);
		progresBar.setValue(61);
		sklad.setExpedicezmetek(expedicezmetek);
		sklad.setPlanovani(planovani);
		
		promOknoNovyZakaznikAndSearch = new PromOknoNovyZakaznikAndSearch(this);
		progresBar.setValue(69);
		promOknoNovyModel = new PromOknoNovyModel(this);
		progresBar.setValue(75);
		promOknoNovaZakazka = new PromOknoNovaZakazka(this);
		progresBar.setValue(85);
		promOkna[0] = promOknoNovyZakaznikAndSearch;
		promOkna[1] = promOknoNovyModel;
		promOkna[2] = promOknoNovaZakazka;
		promOkna[3] = planovani;
		promOkna[4] = expedicezmetek;
		
		
		
		obalVedlejsihoOkna.add(promOkna[0]);
		promOkna[0].setVisible(false);
		obalVedlejsihoOkna.add(promOkna[1]);
		promOkna[1].setVisible(false);
		obalVedlejsihoOkna.add(promOkna[2]);
		promOkna[2].setVisible(false);
		obalVedlejsihoOkna.add(promOkna[3]);
		promOkna[3].setVisible(false);
		obalVedlejsihoOkna.add(promOkna[4]);
		promOkna[4].setVisible(false);
		
	}
	
	/**
	 * Nastav dané okno
	 * @param index od 0 do 4<br>
	 *   i = 0 PromOknoNovyZakaznikAndSearch JPanel<br>
	 *   i = 1 PromOknoNovyModel JPanel<br>
	 *   i = 2 PromOknoNovaZakazka JPanel<br>
	 *   i = 3 Planovani JPanel<br>
	 *   i = 4 ExpediceZmetek JPanel
	 */
	public void setWindow(int index){
		if(index < 0 || index > 4){
			JOptionPane.showMessageDialog(this, "Špatnì nastavene okno v metodì setWindow v MainFrame");
			return;
		} else {
			this.vedlejsiOkno.setVisible(false);
			this.vedlejsiOkno = this.promOkna[index];
			this.vedlejsiOkno.setVisible(true);
		}		
	}
	
	public PromOknoNovyZakaznikAndSearch getVyhledavac (){
		return promOknoNovyZakaznikAndSearch;	
	}
	
	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public MainFrame(Connection conn, String jmenoUzivatele,JProgressBar progresBar) throws SQLException {	
		progresBar.setValue(1);
		
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/app/dbSlevarnaIco.png")));
		setMinimumSize(new Dimension(1276, 668));
		this.sklad = new SkladOdkazu(conn, barvy, fonty);
		sklad.setHlavniOkno(this);
		
		Odhlasit odhlasClass = new Odhlasit(sklad);
		this.addWindowListener(odhlasClass);
		
		
		progresBar.setValue(10);
		
		//hlavicka - nazev aplikace, jmeno uživatele, tlacitko odhlasit
		hlavickaLabels = new JLabel [4];		
		
		setTitle("Datab\u00E1ze Sl\u00E9v\u00E1rna Stra\u0161ice");
		setBounds(new Rectangle(100, 100, 1266, 668));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel jPanelHeader = new Hlavicka();
		jPanelHeader.setBackground(barvy[0]);
		
		contentPane.add(jPanelHeader);
		
		hlavickaLabels[0] = new JLabel("Sl\u00E9v\u00E1rna Stra\u0161ice");
		hlavickaLabels[0].setFont(new Font("Tahoma", Font.PLAIN, 25));
		hlavickaLabels[0].setForeground(barvy[7]);
		
		hlavickaLabels[1] = new JLabel("P\u0159ihl\u00E1\u0161en\u00FD u\u017Eivatel:");
		hlavickaLabels[1].setForeground(barvy[7]);
		hlavickaLabels[1].setFont(fonty[0]);
		
		hlavickaLabels[2] = new JLabel(jmenoUzivatele);
		hlavickaLabels[2].setForeground(Color.WHITE);
		hlavickaLabels[2].setFont(fonty[0]);
		
		hlavickaLabels[3] = new JLabel("|");
		hlavickaLabels[3].setForeground(barvy[7]);
		hlavickaLabels[3].setFont(fonty[0]);
		
		odhlasit = new JButton("odhl\u00E1sit");
		odhlasit.setActionCommand("OdhlasitComand");
		odhlasit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		odhlasit.setBorder(new EmptyBorder(4, 9, 4, 9));
		odhlasit.setForeground(barvy[3]);
		odhlasit.setContentAreaFilled(false);
		odhlasit.addActionListener(odhlasClass);
		odhlasit.setFont(fonty[0]);
		
		sklad.setOdhlasit(odhlasit);
		
		JLabel obrazek = new JLabel();
		obrazek.setIcon(new ImageIcon(MainFrame.class.getResource("/app/worker.gif")));

		
		GroupLayout gl_jPanelHeader = new GroupLayout(jPanelHeader);
		gl_jPanelHeader.setHorizontalGroup(
			gl_jPanelHeader.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanelHeader.createSequentialGroup()
					.addGap(23)
					.addComponent(hlavickaLabels[0])
					.addPreferredGap(ComponentPlacement.RELATED, 939, Short.MAX_VALUE)
					.addComponent(hlavickaLabels[1])
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(hlavickaLabels[2])
					.addGap(2)
					.addComponent(hlavickaLabels[3])
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(odhlasit)
					.addGap(20)
					.addComponent(obrazek)
					.addGap(60))
		);
		gl_jPanelHeader.setVerticalGroup(
			gl_jPanelHeader.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_jPanelHeader.createSequentialGroup()
					.addGroup(gl_jPanelHeader.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jPanelHeader.createSequentialGroup()
							.addGap(15)
							.addGroup(gl_jPanelHeader.createParallelGroup(Alignment.BASELINE)
								.addComponent(hlavickaLabels[3])
								.addComponent(hlavickaLabels[2])
								.addComponent(hlavickaLabels[1])
								.addComponent(odhlasit)
								.addComponent(obrazek)
								))
						.addGroup(gl_jPanelHeader.createSequentialGroup()
							.addGap(10)
							.addComponent(hlavickaLabels[0], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addGap(14))
		);
		jPanelHeader.setLayout(gl_jPanelHeader);
		
		
		//navigace 2
		JPanel navigace2 = new Navigator(this.sklad);
		navigace2.setBackground(barvy[8]);
		navigace2.setPreferredSize(new Dimension(10, 35));
		navigace2.setMinimumSize(new Dimension(10, 35));
		navigace2.setMaximumSize(new Dimension(32767, 35));
		contentPane.add(navigace2);
		
		progresBar.setValue(20);
		
		//telo
		telo = new JPanel();
		contentPane.add(telo);
		telo.setLayout(new BoxLayout(telo, BoxLayout.X_AXIS));
		
		
		// postrani menu
		JPanel sideMenu = new JPanel();
		sideMenu.setBorder(null);
		sideMenu.setBackground(barvy[8]);
		FlowLayout flowLayout = (FlowLayout) sideMenu.getLayout();
		flowLayout.setVgap(20);
		sideMenu.setMinimumSize(new Dimension(220, 10));
		sideMenu.setPreferredSize(new Dimension(220, 10));
		sideMenu.setMaximumSize(new Dimension(220, 32767));
		telo.add(sideMenu);
		
		
		// tlacitka v menu
		this.sideListButton = initJButtons(sklad);
		for(int i = 0; i < sideListButton.length; i++ ){
			sideMenu.add(sideListButton[i]);
		}
		
		progresBar.setValue(30);
		// popup menus 
		sidePopupMenulist = new MyPopUp [sideListButton.length];
		sidePopupMenuItems = new JMenuItem [sideListButton.length][];
		initPopUpMenus(sidePopupMenulist, sidePopupMenuItems, sklad);
		
		progresBar.setValue(40);
		
		
		
		/*
		 * Promenne dynamicke okno 
		 */
		
		// vytvoøit promenne okna
		
		//Nejdrive vytvorim Scrollpane kdyby se mi to tma neveslo
		
		scrollPane = new JScrollPane();
		telo.add(scrollPane);
		scrollPane.setBackground(barvy[8]);
		sklad.setScrollPane(scrollPane);
		scrollPane.setBorder(new EmptyBorder(20, 0, 20, 20));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(14);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		//scrollPane.setViewportView(obalVedlejsihoOkna);
		progresBar.setValue(50);
		
		obalVedlejsihoOkna = new JPanel();
		obalVedlejsihoOkna.setBorder(new EmptyBorder(0, 0, 20, 20));
		obalVedlejsihoOkna.setBackground(barvy[8]);
		scrollPane.setViewportView(obalVedlejsihoOkna);
		//telo.add(obalVedlejsihoOkna);
		obalVedlejsihoOkna.setLayout(new BoxLayout(obalVedlejsihoOkna, BoxLayout.X_AXIS));

		initWindows(progresBar);
		
		//dat uvodni okno
		vedlejsiOkno = promOkna[0];
		promOkna[0].setVisible(true);
		progresBar.setValue(90);
		// create listeners
		createAndAddListeners();
		
		progresBar.setValue(100);
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public JPanel getObalVedlejsihoOkna() {
		return obalVedlejsihoOkna;
	}

	public void odhlasit() {
		contentPane.setVisible(false);
	}
	
}
