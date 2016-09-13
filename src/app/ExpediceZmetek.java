package app;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import sablony.MyCellrendererCheckBox;
import sablony.MyJButton;
import sablony.errorwin.ExceptionWin;
import sablony.storage.DateStor;
import sablony.tabulka.ColorCellTable;
import sablony.tabulka.QueryTableModel;
import sqlstorage.SQLStor;
import sqlstorage.TransClass;
import storage.SkladOdkazu;

import javax.swing.ListSelectionModel;

import com.toedter.calendar.JDateChooser;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import javax.swing.JTabbedPane;

public class ExpediceZmetek extends JPanel implements ActionListener //, ChangeListener
{
	
	/**
	 *  Verze
	 */
	private static final long serialVersionUID = 1L;
	private SkladOdkazu sklad;
	private SQLStor sql;
	private MainFrame hlavniOkno;
	private JScrollPane scrollPane_1;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private static final String acesDenied = "execute command denied to user";

	private ColorCellTable tableFyzkusyEx;
	
	private JLabel [] textLabels;
	private JLabel [] popisLabels;
	/**
	 * 1. Set visible/enable pridej Odlito<br>
	 * 2. Set visible/enable pridej Zmetek<br>
	 * 3. Set visible/enable pridej Cislo tavby
	 */
	private JComponent [][] nastavRezimPole;
	
	private JLabel textIdZakazky;
	private JLabel textJmenoZakaznika;
	private JLabel textCisloObjednavky;
	private JLabel textDatumPrijetiZakazky;
	private JLabel textPocetKusu;
	private JLabel textIdModelu;
	private JLabel textJmenoModelu;
	private JLabel textCisloModelu;
	private JLabel textPaganyrka;
	private JLabel textMaterial;
	private JLabel textMaterialVlastni;
	private JLabel textFormovna;
	private JLabel textHmotnost;
	private JLabel textIsOdhadLabel;
	private JLabel textNorma;
	private JLabel textIdZakaznika;
	private JLabel textPoznamkaModel;
	
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
	
	private JTextPane textPoznamka;
	
	private Font [] fonty; 
	private Font f = new Font("Tahoma", Font.PLAIN, 14);
	private Font fb = new Font("Tahoma", Font.BOLD, 15);
	private JTextField textTavba;
	private JLabel textCena;
	private JLabel textIsCZK;
	private JLabel lblCena;
	private JComboBox comboBoxVinik;
	private JComboBox comboBoxVada;
	private MyJButton oznacOdlito;
	private MyJButton oznacVycisteno;
	private MyJButton oznacZmetek;
	private MyJButton doplnUdaje;
	private MyJButton dokonciZadavani;
	private JDateChooser datumVycisteni;
	private JLabel lblVinik;
	private JLabel lblVada;
	private JLabel lblZadatTavbu;
	
	private JLabel lblNewLabel_2;
	private JLabel textKurz;
	private JButton btnExpedovno;
	private JLabel pocetNeplanZmetku;
	private JButton aktualizovatBut;
	private JButton generovatNoveKusy;
	private JButton btnUzavtZakzku;
	private JLabel lblTermnExpedice_1;
	private JLabel terminExpedice;
	private JLabel lblUzaveno;
	private JLabel uzavreno;
	private JDateChooser dateOdliti;
	private JDateChooser dateExpedice;
	private JList<DateStor> list;
	private JButton zadejCisloTavby;
	private JTextField textCisloFaktury;
	private JButton zadatCisloFaktury;
	private JLabel lblsloFaktury;
	private JDateChooser dateZadaniZmetku;
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPane_2;
	private ColorCellTable stavZakazkyTable;
	private JScrollPane scrollPane_3;
	
	/**
	 * Pro cteni tabulky zakazek
	 */
	private static final int pozicePoznamkyVParametrech = 21;
	private static final int pozicePaganyrkyVParametech = 22;
	private static final int poziceUzavreneZakazkyVParametrech = 20;
	private static final int pozicePoznamkyModeluVParametrech = 24;
	/**
	 * Pro zadavani odlitku
	 */
	private static final int poziceCislaTavbyVTabulce = 2;
	private static final int poziceCislaFakturyVTabulce = 8;
	private static final int poziceTeplotyLitiVTabulce = 17;
	
	private String[] parametryZakazky;
	private JLabel lblTeplotaLit;
	private JTextField teplotaLitiText;
	private JButton pridejTeplotuLiti;


	/**
	 * <p>Metoda, která si podle idZakazky vyhledá v databázi, všechny fyzické kusy, dílèí termíny a stav
	 * zakázky (odlito, vyèištìno,...) a pak to zobrazí do tabulek. </p>
	 * <p>Nastaví všude aktuální hodnoty a nastaví JScrollPane hlavního okna na výšku aby se to tam vešlo.</p>
	 * <p>Dále nastaví i listener pro</p>
	 * @param parametryZakazky
	 * @param idZakazky
	 * @throws Exception
	 */
	public void setZadejOdlitekZmetek(String[] parametryZakazky, int idZakazky) throws Exception {
		this.parametryZakazky = parametryZakazky;
		TransClass m = sql.vyberFyzKusy(idZakazky);
		ResultSet fyzKusyZakazky = m.rs;
		int pocetNeplanZmet = m.pocetNenaplanovanychKusu;
		
		ResultSet rs = sklad.getSql().vyberDilciTerminy(idZakazky);
		DefaultListModel<DateStor> mod = createListModel(rs);
		list.setModel(mod);
		
		ResultSet stavZakazky = sql.vypisStavuNeuzavrenychZakazekShort(idZakazky, null, null, null, 0, null, null);
		QueryTableModel tm = new QueryTableModel(stavZakazky);
		stavZakazkyTable.setModel(tm);
		
		this.doplnUdaje.removeMouseListener(sklad.getMyJButonnListener()); // smaze listener aby se pøi pøejetí nemìnila barva
		pocetNeplanZmetku.setText(Integer.toString(pocetNeplanZmet));
		for(int i = 0; i < nastavRezimPole[0].length; i++){ // možná by to šlo napsat do dvou for cyklu
			nastavRezimPole[0][i].setVisible(true);
		}
		for(int i = 0; i < nastavRezimPole[1].length; i++){
			nastavRezimPole[1][i].setVisible(true);
		}
		for(int i = 0; i < nastavRezimPole[2].length; i++){
			nastavRezimPole[2][i].setVisible(true);
		}
		
		for (int i = 0; i < textLabels.length; i++) {
			if (i == 8) {
				if (parametryZakazky[i].equalsIgnoreCase("Ano")) {
					textIsCZK.setText("CZK");
				} else if (parametryZakazky[i].equalsIgnoreCase("Ne")) {
					textIsCZK.setText("EUR");
				} else{
					JOptionPane.showMessageDialog(hlavniOkno, "Error setPlanovani planovani.class");
				}
				continue;
			}
			if (i == 9) {
				if (parametryZakazky[i].equalsIgnoreCase("Ano")) {
					lblCena.setText("Cena za [kus]:");
				} else if (parametryZakazky[i].equalsIgnoreCase("Ne")) {
					lblCena.setText("Cena za [kg]:");
				} else{
					JOptionPane.showMessageDialog(hlavniOkno, "Error setPlanovani planovani.class");
				}
				continue;
			}
			textLabels[i].setText(parametryZakazky[i]);
		}		
		
		textPaganyrka.setText(parametryZakazky[pozicePaganyrkyVParametech]);
		textPoznamka.setText(parametryZakazky[pozicePoznamkyVParametrech]);
		textPoznamka.validate();
		if(parametryZakazky[poziceUzavreneZakazkyVParametrech].equalsIgnoreCase("Ne")){
			btnUzavtZakzku.setActionCommand("UzavriZakazku");
			btnUzavtZakzku.setText("Uzav\u0159\u00EDt zak\u00E1zku (P\u0159esnos do archivu)");
		} else if(parametryZakazky[poziceUzavreneZakazkyVParametrech].equalsIgnoreCase("Ano")){
			btnUzavtZakzku.setActionCommand("ObnovZakazku");
			btnUzavtZakzku.setText("Obnov zak\u00E1zku (P\u0159esnos z archivu)");
		}
		textPoznamkaModel.setText(parametryZakazky[pozicePoznamkyModeluVParametrech]);
		
		
		String [][] vadyVinici = sklad.getVadyVinici();
		if(vadyVinici[0] == null){
			rs = sql.vyberVady(null);
			rs.last();
			int index = rs.getRow();
			rs.first();
			if(index <= 0) {
				vadyVinici[0] = new String [1];
				vadyVinici[0][0] = "Error";
			} else {
				vadyVinici[0] = new String [index + 1]; // chci na prvni misto umistit null retezec
				for(int i = 0; i < vadyVinici[0].length; i++){
					if(i == 0){
						vadyVinici[0][i] = null;
						continue;
					}
					vadyVinici[0][i] = rs.getString(1)+" "+rs.getString(2);
					rs.next();
				}
			}
			sklad.setVadyVinici(vadyVinici);
			rs.close();
		}
		comboBoxVada.setModel(new DefaultComboBoxModel(vadyVinici[0]));
		if(vadyVinici[1] == null){
			rs = sql.vyberViniky(null);
			rs.last();
			int index = rs.getRow();
			rs.first();
			if(index <= 0){
				vadyVinici[1] = new String [1];
				vadyVinici[1][0] = "Error";
			}else {
				vadyVinici[1] = new String [index + 1]; // chci umistit na prvni prazdny retezec
				for(int i = 0; i < vadyVinici[1].length; i++){
					if(i == 0){
						vadyVinici[0][i] = null;
						continue;
					}
					vadyVinici[1][i] = rs.getString(1)+" "+rs.getString(2);
					rs.next();
				}
			}
			sklad.setVadyVinici(vadyVinici);
			rs.close();
		}
		comboBoxVinik.setModel(new DefaultComboBoxModel(vadyVinici[1]));
		
		QueryTableModel model = new QueryTableModel(fyzKusyZakazky);
		this.tableFyzkusyEx.setModel(model); 
		tableFyzkusyEx.getColumAdjuster().adjustColumns();
		
		
		
		int width = 130;
		for(int i = 0; i < tableFyzkusyEx.getColumnCount() - 1; i++){
			width += tableFyzkusyEx.getColumnModel().getColumn(i).getWidth();
		}
		Dimension s = this.hlavniOkno.getObalVedlejsihoOkna().getPreferredSize();
		s.width = width;
		s.height = 500 + tableFyzkusyEx.getRowCount()*tableFyzkusyEx.getRowHeight();
		this.hlavniOkno.getObalVedlejsihoOkna().setPreferredSize(s);
		
		this.setPreferredSize(s);
		hlavniOkno.getScrollPane().revalidate();
		hlavniOkno.getScrollPane().repaint();
	}
	
	/**
	 * Vytvoøí model <code>DefaultListModel(DateStor)</code> z resultSetu z databaze.
	 * Je to seznam dílèích termínù s udaji jestli je již termín splnìný, kolik kusu dodat a kdy
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private DefaultListModel<DateStor> createListModel(ResultSet rs) throws SQLException{
		DefaultListModel<DateStor> mod = new DefaultListModel<DateStor>();
		int pocetKusu = 0;
		Date datum;
		boolean isCompleted;
		while(rs.next()){
			datum = rs.getDate(1);
			pocetKusu = rs.getInt(2);
			isCompleted = rs.getBoolean(3);
			mod.addElement(new DateStor(datum, pocetKusu, sdf, isCompleted));			
		}
		return mod;
	}
	
	/**
	 * Pouze vytvoøí kontejnery (pole) a poskládá do nich všechny popisky (JLabel, apod) a nastaví jim font.
	 */
	private void initLabels(){
		this.textLabels = new JLabel [21];
		textLabels[0] = textIdZakazky;
		textLabels[1] = textJmenoZakaznika;
		textLabels[2] = textCisloObjednavky;
		textLabels[3] = textCisloModelu;
		textLabels[4] = textDatumPrijetiZakazky;
		textLabels[5] = textPocetKusu;
		textLabels[6] = terminExpedice;
		textLabels[7] = textCena;
		textLabels[8] = textIsCZK;
		textLabels[9] = lblCena;
		textLabels[10] = textKurz;
		textLabels[11] = textIdModelu;
		textLabels[12] = textJmenoModelu;
		textLabels[13] = textMaterial;
		textLabels[14] = textMaterialVlastni;
		textLabels[15] = textFormovna;
		textLabels[16] = textHmotnost;
		textLabels[17] = textIsOdhadLabel;
		textLabels[18] = textNorma;
		textLabels[19] = textIdZakaznika;
		textLabels[20] = uzavreno;
		
		
		
		/*
		 * Nastavim správny font
		 */
		for(int i = 0; i < textLabels.length; i++){
			textLabels[i].setFont(f);
		}
		this.textPaganyrka.setFont(f);
		textPoznamka.setFont(f);
		this.textPoznamkaModel.setFont(f);
		
		for(int i = 0; i < popisLabels.length; i++){
			popisLabels[i].setFont(f);
		}
		popisLabels[0].setFont(fb);
		
		
		
		
		
		nastavRezimPole = new JComponent [3][];
		nastavRezimPole[0] = new JComponent [3];
		nastavRezimPole[0][0] = oznacOdlito;
		nastavRezimPole[0][1] = oznacVycisteno;
		nastavRezimPole[0][2] = datumVycisteni;
		
		nastavRezimPole[1] = new JComponent [6];
		nastavRezimPole[1][0] = oznacZmetek;
		nastavRezimPole[1][1] = doplnUdaje;
		nastavRezimPole[1][2] = lblVinik;
		nastavRezimPole[1][3] = comboBoxVinik;
		nastavRezimPole[1][4] = lblVada;
		nastavRezimPole[1][5] = comboBoxVada;
		
		
		nastavRezimPole[2] = new JComponent [3];
		nastavRezimPole[2][0] = lblZadatTavbu;
		nastavRezimPole[2][1] = textTavba;
		nastavRezimPole[2][2] = zadejCisloTavby;
	}

	/**
	 * Vytvoøí okno rozšiøující JPanel a rovnou poskládá komponenty podle potøeby, jak jsem nastavil.
	 * @param hlavniOkno
	 */
	public ExpediceZmetek(MainFrame hlavniOkno) {
		setBackground(barvy[12]);
		this.hlavniOkno = hlavniOkno;
		this.sklad = hlavniOkno.getSklad();
		this.popisLabels = new JLabel [27];
		this.sql = sklad.getSql();
		fonty = sklad.getFonty();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{22, 160, 100, 20, 25, 66, 38, 60, 48, 40, 10, 0, 15, 10, 43, 20, 0, 30, 30, 20, 30, 20, 60, 110, 80, 20, 20, 0};
		gridBagLayout.rowHeights = new int[]{21, 0, 0, 12, 0, 0, 0, 0, 0, 0, 30, 25, 32, 32, 0, 32, 0, 32, 0, 20, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblParametryZakzky = new JLabel("Parametry zak\u00E1zky");
		lblParametryZakzky.setFont(fb);
		popisLabels[0] = lblParametryZakzky;
		GridBagConstraints gbc_lblParametryZakzky = new GridBagConstraints();
		gbc_lblParametryZakzky.anchor = GridBagConstraints.WEST;
		gbc_lblParametryZakzky.gridwidth = 2;
		gbc_lblParametryZakzky.insets = new Insets(0, 0, 5, 5);
		gbc_lblParametryZakzky.gridx = 1;
		gbc_lblParametryZakzky.gridy = 1;
		add(lblParametryZakzky, gbc_lblParametryZakzky);
		
		JLabel lblIdZakaznika = new JLabel("Id z\u00E1kazn\u00EDka:");
		popisLabels[1] = lblIdZakaznika;
		GridBagConstraints gbc_lblIdZakaznika = new GridBagConstraints();
		gbc_lblIdZakaznika.anchor = GridBagConstraints.WEST;
		gbc_lblIdZakaznika.insets = new Insets(0, 0, 5, 5);
		gbc_lblIdZakaznika.gridx = 1;
		gbc_lblIdZakaznika.gridy = 2;
		add(lblIdZakaznika, gbc_lblIdZakaznika);
		
		textIdZakaznika = new JLabel("xxxx");
		GridBagConstraints gbc_textIdZakaznika = new GridBagConstraints();
		gbc_textIdZakaznika.anchor = GridBagConstraints.WEST;
		gbc_textIdZakaznika.insets = new Insets(0, 0, 5, 5);
		gbc_textIdZakaznika.gridx = 2;
		gbc_textIdZakaznika.gridy = 2;
		add(textIdZakaznika, gbc_textIdZakaznika);
		
		JLabel lblZkaznik = new JLabel("Z\u00E1kaznik:");
		popisLabels[2] = lblZkaznik;
		GridBagConstraints gbc_lblZkaznik = new GridBagConstraints();
		gbc_lblZkaznik.gridwidth = 3;
		gbc_lblZkaznik.insets = new Insets(0, 0, 5, 5);
		gbc_lblZkaznik.anchor = GridBagConstraints.WEST;
		gbc_lblZkaznik.gridx = 5;
		gbc_lblZkaznik.gridy = 2;
		add(lblZkaznik, gbc_lblZkaznik);
		
		textJmenoZakaznika = new JLabel("XXXXXXXXXXXXX");
		GridBagConstraints gbc_textJmenoZakaznika = new GridBagConstraints();
		gbc_textJmenoZakaznika.gridwidth = 3;
		gbc_textJmenoZakaznika.anchor = GridBagConstraints.WEST;
		gbc_textJmenoZakaznika.insets = new Insets(0, 0, 5, 5);
		gbc_textJmenoZakaznika.gridx = 8;
		gbc_textJmenoZakaznika.gridy = 2;
		add(textJmenoZakaznika, gbc_textJmenoZakaznika);
		
		JLabel lblNewLabel_1 = new JLabel("Id zak\u00E1zky:");
		popisLabels[3] = lblNewLabel_1;
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 4;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textIdZakazky = new JLabel("XXX");
		GridBagConstraints gbc_textIdZakazky = new GridBagConstraints();
		gbc_textIdZakazky.anchor = GridBagConstraints.WEST;
		gbc_textIdZakazky.insets = new Insets(0, 0, 5, 5);
		gbc_textIdZakazky.gridx = 2;
		gbc_textIdZakazky.gridy = 4;
		add(textIdZakazky, gbc_textIdZakazky);
		
		JLabel lblIdModelu = new JLabel("Id modelu:");
		popisLabels[4] = lblIdModelu;
		GridBagConstraints gbc_lblIdModelu = new GridBagConstraints();
		gbc_lblIdModelu.anchor = GridBagConstraints.WEST;
		gbc_lblIdModelu.insets = new Insets(0, 0, 5, 5);
		gbc_lblIdModelu.gridx = 11;
		gbc_lblIdModelu.gridy = 4;
		add(lblIdModelu, gbc_lblIdModelu);
		
		textIdModelu = new JLabel("xxx");
		GridBagConstraints gbc_textIdModelu = new GridBagConstraints();
		gbc_textIdModelu.anchor = GridBagConstraints.WEST;
		gbc_textIdModelu.gridwidth = 2;
		gbc_textIdModelu.insets = new Insets(0, 0, 5, 5);
		gbc_textIdModelu.gridx = 13;
		gbc_textIdModelu.gridy = 4;
		add(textIdModelu, gbc_textIdModelu);
		
		JLabel lblsloObjednvky = new JLabel("\u010C\u00EDslo objedn\u00E1vky:");
		popisLabels[5] = lblsloObjednvky;
		GridBagConstraints gbc_lblsloObjednvky = new GridBagConstraints();
		gbc_lblsloObjednvky.anchor = GridBagConstraints.WEST;
		gbc_lblsloObjednvky.insets = new Insets(0, 0, 5, 5);
		gbc_lblsloObjednvky.gridx = 1;
		gbc_lblsloObjednvky.gridy = 5;
		add(lblsloObjednvky, gbc_lblsloObjednvky);
		
		textCisloObjednavky = new JLabel("xxx)");
		GridBagConstraints gbc_textCisloObjednavky = new GridBagConstraints();
		gbc_textCisloObjednavky.anchor = GridBagConstraints.WEST;
		gbc_textCisloObjednavky.insets = new Insets(0, 0, 5, 5);
		gbc_textCisloObjednavky.gridx = 2;
		gbc_textCisloObjednavky.gridy = 5;
		add(textCisloObjednavky, gbc_textCisloObjednavky);
		
		JLabel lblDatumPijetZakzky = new JLabel("Datum p\u0159ijet\u00ED zak\u00E1zky:");
		popisLabels[6] = lblDatumPijetZakzky;
		GridBagConstraints gbc_lblDatumPijetZakzky = new GridBagConstraints();
		gbc_lblDatumPijetZakzky.gridwidth = 3;
		gbc_lblDatumPijetZakzky.anchor = GridBagConstraints.WEST;
		gbc_lblDatumPijetZakzky.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatumPijetZakzky.gridx = 5;
		gbc_lblDatumPijetZakzky.gridy = 5;
		add(lblDatumPijetZakzky, gbc_lblDatumPijetZakzky);
		
		textDatumPrijetiZakazky = new JLabel("XXx");
		GridBagConstraints gbc_textDatumPrijetiZakazky = new GridBagConstraints();
		gbc_textDatumPrijetiZakazky.anchor = GridBagConstraints.WEST;
		gbc_textDatumPrijetiZakazky.insets = new Insets(0, 0, 5, 5);
		gbc_textDatumPrijetiZakazky.gridx = 8;
		gbc_textDatumPrijetiZakazky.gridy = 5;
		add(textDatumPrijetiZakazky, gbc_textDatumPrijetiZakazky);
		
		JLabel lblCisloModelu = new JLabel("\u010C\u00EDslo modelu:");
		popisLabels[7] = lblCisloModelu;
		GridBagConstraints gbc_lblCisloModelu = new GridBagConstraints();
		gbc_lblCisloModelu.anchor = GridBagConstraints.WEST;
		gbc_lblCisloModelu.insets = new Insets(0, 0, 5, 5);
		gbc_lblCisloModelu.gridx = 11;
		gbc_lblCisloModelu.gridy = 5;
		add(lblCisloModelu, gbc_lblCisloModelu);
		
		textCisloModelu = new JLabel("XXXXXXXXXXXXX");
		GridBagConstraints gbc_textCisloModelu = new GridBagConstraints();
		gbc_textCisloModelu.anchor = GridBagConstraints.WEST;
		gbc_textCisloModelu.gridwidth = 2;
		gbc_textCisloModelu.insets = new Insets(0, 0, 5, 5);
		gbc_textCisloModelu.gridx = 13;
		gbc_textCisloModelu.gridy = 5;
		add(textCisloModelu, gbc_textCisloModelu);
		
		JLabel lblNazevModelu = new JLabel("N\u00E1zev modelu:");
		popisLabels[8] = lblNazevModelu;
		GridBagConstraints gbc_lblNazevModelu = new GridBagConstraints();
		gbc_lblNazevModelu.gridwidth = 3;
		gbc_lblNazevModelu.anchor = GridBagConstraints.WEST;
		gbc_lblNazevModelu.insets = new Insets(0, 0, 5, 5);
		gbc_lblNazevModelu.gridx = 16;
		gbc_lblNazevModelu.gridy = 5;
		add(lblNazevModelu, gbc_lblNazevModelu);
		
		textJmenoModelu = new JLabel("XXXXXXXXXXXXX");
		GridBagConstraints gbc_textJmenoModelu = new GridBagConstraints();
		gbc_textJmenoModelu.anchor = GridBagConstraints.WEST;
		gbc_textJmenoModelu.gridwidth = 2;
		gbc_textJmenoModelu.insets = new Insets(0, 0, 5, 5);
		gbc_textJmenoModelu.gridx = 20;
		gbc_textJmenoModelu.gridy = 5;
		add(textJmenoModelu, gbc_textJmenoModelu);
		
		JLabel lblPaganrka = new JLabel("Pagan\u00FDrka:");
		popisLabels[9] = lblPaganrka;
		GridBagConstraints gbc_lblPaganrka = new GridBagConstraints();
		gbc_lblPaganrka.anchor = GridBagConstraints.WEST;
		gbc_lblPaganrka.insets = new Insets(0, 0, 5, 5);
		gbc_lblPaganrka.gridx = 1;
		gbc_lblPaganrka.gridy = 6;
		add(lblPaganrka, gbc_lblPaganrka);
		
		textPaganyrka = new JLabel("XXXXX");
		GridBagConstraints gbc_textPaganyrka = new GridBagConstraints();
		gbc_textPaganyrka.anchor = GridBagConstraints.WEST;
		gbc_textPaganyrka.insets = new Insets(0, 0, 5, 5);
		gbc_textPaganyrka.gridx = 2;
		gbc_textPaganyrka.gridy = 6;
		add(textPaganyrka, gbc_textPaganyrka);
		
		JLabel lblNewLabel = new JLabel("Po\u010Det kus\u016F:");
		popisLabels[10] = lblNewLabel;
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 3;
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 5;
		gbc_lblNewLabel.gridy = 6;
		add(lblNewLabel, gbc_lblNewLabel);
		
		textPocetKusu = new JLabel("XXX");
		GridBagConstraints gbc_textPocetKusu = new GridBagConstraints();
		gbc_textPocetKusu.anchor = GridBagConstraints.WEST;
		gbc_textPocetKusu.insets = new Insets(0, 0, 5, 5);
		gbc_textPocetKusu.gridx = 8;
		gbc_textPocetKusu.gridy = 6;
		add(textPocetKusu, gbc_textPocetKusu);
		
		JLabel lblMaterial = new JLabel("Materi\u00E1l:");
		popisLabels[11] = lblMaterial;
		GridBagConstraints gbc_lblMaterial = new GridBagConstraints();
		gbc_lblMaterial.anchor = GridBagConstraints.WEST;
		gbc_lblMaterial.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaterial.gridx = 11;
		gbc_lblMaterial.gridy = 6;
		add(lblMaterial, gbc_lblMaterial);
		
		textMaterial = new JLabel("XXXXXXX");
		GridBagConstraints gbc_textMaterial = new GridBagConstraints();
		gbc_textMaterial.anchor = GridBagConstraints.WEST;
		gbc_textMaterial.gridwidth = 2;
		gbc_textMaterial.insets = new Insets(0, 0, 5, 5);
		gbc_textMaterial.gridx = 13;
		gbc_textMaterial.gridy = 6;
		add(textMaterial, gbc_textMaterial);
		
		JLabel lblMaterialVlastn = new JLabel("Materi\u00E1l vlastn\u00ED:");
		popisLabels[12] = lblMaterialVlastn;
		GridBagConstraints gbc_lblMaterialVlastn = new GridBagConstraints();
		gbc_lblMaterialVlastn.gridwidth = 3;
		gbc_lblMaterialVlastn.anchor = GridBagConstraints.WEST;
		gbc_lblMaterialVlastn.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaterialVlastn.gridx = 16;
		gbc_lblMaterialVlastn.gridy = 6;
		add(lblMaterialVlastn, gbc_lblMaterialVlastn);
		
		textMaterialVlastni = new JLabel("XXXXXXX");
		GridBagConstraints gbc_textMaterialVlastni = new GridBagConstraints();
		gbc_textMaterialVlastni.anchor = GridBagConstraints.WEST;
		gbc_textMaterialVlastni.gridwidth = 2;
		gbc_textMaterialVlastni.insets = new Insets(0, 0, 5, 5);
		gbc_textMaterialVlastni.gridx = 20;
		gbc_textMaterialVlastni.gridy = 6;
		add(textMaterialVlastni, gbc_textMaterialVlastni);
		
		lblCena = new JLabel("Cena:");
		GridBagConstraints gbc_lblCena = new GridBagConstraints();
		gbc_lblCena.anchor = GridBagConstraints.WEST;
		gbc_lblCena.insets = new Insets(0, 0, 5, 5);
		gbc_lblCena.gridx = 1;
		gbc_lblCena.gridy = 7;
		add(lblCena, gbc_lblCena);
		
		textCena = new JLabel("xxx");
		GridBagConstraints gbc_textCena = new GridBagConstraints();
		gbc_textCena.anchor = GridBagConstraints.WEST;
		gbc_textCena.insets = new Insets(0, 0, 5, 5);
		gbc_textCena.gridx = 2;
		gbc_textCena.gridy = 7;
		add(textCena, gbc_textCena);
		
		textIsCZK = new JLabel("XCZK");
		this.popisLabels[13] = textIsCZK;
		GridBagConstraints gbc_textIsCZK = new GridBagConstraints();
		gbc_textIsCZK.anchor = GridBagConstraints.WEST;
		gbc_textIsCZK.insets = new Insets(0, 0, 5, 5);
		gbc_textIsCZK.gridx = 4;
		gbc_textIsCZK.gridy = 7;
		add(textIsCZK, gbc_textIsCZK);
		
		lblNewLabel_2 = new JLabel("Kurz CZK/EU:");
		popisLabels[14] = lblNewLabel_2;
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.gridwidth = 2;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 5;
		gbc_lblNewLabel_2.gridy = 7;
		add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		textKurz = new JLabel("Xxx");
		GridBagConstraints gbc_textKurz = new GridBagConstraints();
		gbc_textKurz.anchor = GridBagConstraints.WEST;
		gbc_textKurz.insets = new Insets(0, 0, 5, 5);
		gbc_textKurz.gridx = 8;
		gbc_textKurz.gridy = 7;
		add(textKurz, gbc_textKurz);
		
		lblTermnExpedice_1 = new JLabel("Term\u00EDn expedice:");
		popisLabels[15] = lblTermnExpedice_1;
		GridBagConstraints gbc_lblTermnExpedice_1 = new GridBagConstraints();
		gbc_lblTermnExpedice_1.anchor = GridBagConstraints.WEST;
		gbc_lblTermnExpedice_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblTermnExpedice_1.gridx = 1;
		gbc_lblTermnExpedice_1.gridy = 8;
		add(lblTermnExpedice_1, gbc_lblTermnExpedice_1);
		
		terminExpedice = new JLabel("New label");
		GridBagConstraints gbc_terminExpedice = new GridBagConstraints();
		gbc_terminExpedice.anchor = GridBagConstraints.WEST;
		gbc_terminExpedice.insets = new Insets(0, 0, 5, 5);
		gbc_terminExpedice.gridx = 2;
		gbc_terminExpedice.gridy = 8;
		add(terminExpedice, gbc_terminExpedice);
		
		lblUzaveno = new JLabel("Uzav\u0159eno:");
		popisLabels[16] = lblUzaveno;
		GridBagConstraints gbc_lblUzaveno = new GridBagConstraints();
		gbc_lblUzaveno.anchor = GridBagConstraints.WEST;
		gbc_lblUzaveno.insets = new Insets(0, 0, 5, 5);
		gbc_lblUzaveno.gridx = 5;
		gbc_lblUzaveno.gridy = 8;
		add(lblUzaveno, gbc_lblUzaveno);
		
		uzavreno = new JLabel("xxx");
		GridBagConstraints gbc_uzavreno = new GridBagConstraints();
		gbc_uzavreno.anchor = GridBagConstraints.WEST;
		gbc_uzavreno.insets = new Insets(0, 0, 5, 5);
		gbc_uzavreno.gridx = 8;
		gbc_uzavreno.gridy = 8;
		add(uzavreno, gbc_uzavreno);
		
		JLabel lblFotmovna = new JLabel("Formovna:");
		popisLabels[17] = lblFotmovna;
		GridBagConstraints gbc_lblFotmovna = new GridBagConstraints();
		gbc_lblFotmovna.anchor = GridBagConstraints.WEST;
		gbc_lblFotmovna.insets = new Insets(0, 0, 5, 5);
		gbc_lblFotmovna.gridx = 11;
		gbc_lblFotmovna.gridy = 8;
		add(lblFotmovna, gbc_lblFotmovna);
		
		textFormovna = new JLabel("XXXXXX");
		GridBagConstraints gbc_textFormovna = new GridBagConstraints();
		gbc_textFormovna.anchor = GridBagConstraints.WEST;
		gbc_textFormovna.gridwidth = 2;
		gbc_textFormovna.insets = new Insets(0, 0, 5, 5);
		gbc_textFormovna.gridx = 13;
		gbc_textFormovna.gridy = 8;
		add(textFormovna, gbc_textFormovna);
		
		JLabel lblHmotnost = new JLabel("Hmotnost:");
		popisLabels[18] = lblHmotnost;
		GridBagConstraints gbc_lblHmotnost = new GridBagConstraints();
		gbc_lblHmotnost.gridwidth = 3;
		gbc_lblHmotnost.anchor = GridBagConstraints.WEST;
		gbc_lblHmotnost.insets = new Insets(0, 0, 5, 5);
		gbc_lblHmotnost.gridx = 16;
		gbc_lblHmotnost.gridy = 8;
		add(lblHmotnost, gbc_lblHmotnost);
		
		textHmotnost = new JLabel("XXXXX");
		GridBagConstraints gbc_textHmotnost = new GridBagConstraints();
		gbc_textHmotnost.anchor = GridBagConstraints.WEST;
		gbc_textHmotnost.gridwidth = 2;
		gbc_textHmotnost.insets = new Insets(0, 0, 5, 5);
		gbc_textHmotnost.gridx = 20;
		gbc_textHmotnost.gridy = 8;
		add(textHmotnost, gbc_textHmotnost);
		
		JLabel lblPoznamka = new JLabel("Pozn\u00E1mka k zak\u00E1zce:");
		popisLabels[19] = lblPoznamka;
		GridBagConstraints gbc_lblPoznamka = new GridBagConstraints();
		gbc_lblPoznamka.anchor = GridBagConstraints.WEST;
		gbc_lblPoznamka.insets = new Insets(0, 0, 5, 5);
		gbc_lblPoznamka.gridx = 1;
		gbc_lblPoznamka.gridy = 9;
		add(lblPoznamka, gbc_lblPoznamka);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 7;
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 9;
		add(scrollPane, gbc_scrollPane);
		
		textPoznamka = new JTextPane();
		textPoznamka.setEditable(false);
		scrollPane.setViewportView(textPoznamka);
		
		JLabel lblNorma = new JLabel("Norma:");
		popisLabels[20] = lblNorma;
		GridBagConstraints gbc_lblNorma = new GridBagConstraints();
		gbc_lblNorma.anchor = GridBagConstraints.WEST;
		gbc_lblNorma.insets = new Insets(0, 0, 5, 5);
		gbc_lblNorma.gridx = 11;
		gbc_lblNorma.gridy = 9;
		add(lblNorma, gbc_lblNorma);
		
		textNorma = new JLabel("New label");
		GridBagConstraints gbc_textNorma = new GridBagConstraints();
		gbc_textNorma.anchor = GridBagConstraints.WEST;
		gbc_textNorma.gridwidth = 2;
		gbc_textNorma.insets = new Insets(0, 0, 5, 5);
		gbc_textNorma.gridx = 13;
		gbc_textNorma.gridy = 9;
		add(textNorma, gbc_textNorma);
		
		JLabel lblOdhadovan = new JLabel("Odhadovan\u00E1 hmotnost:");
		popisLabels[21] = lblOdhadovan;
		GridBagConstraints gbc_lblOdhadovan = new GridBagConstraints();
		gbc_lblOdhadovan.anchor = GridBagConstraints.WEST;
		gbc_lblOdhadovan.gridwidth = 3;
		gbc_lblOdhadovan.insets = new Insets(0, 0, 5, 5);
		gbc_lblOdhadovan.gridx = 16;
		gbc_lblOdhadovan.gridy = 9;
		add(lblOdhadovan, gbc_lblOdhadovan);
		
		oznacOdlito = new MyJButton("Ozna\u010Dit jako (ne)odlito",16 ,1, sklad);
		oznacOdlito.addActionListener(this);
		
		JLabel poznamkaModelLabel = new JLabel("Pozn\u00E1mka k modelu:");
		popisLabels[25] = poznamkaModelLabel;
		GridBagConstraints gbc_poznamkaModelLabel = new GridBagConstraints();
		gbc_poznamkaModelLabel.gridwidth = 3;
		gbc_poznamkaModelLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_poznamkaModelLabel.insets = new Insets(0, 0, 5, 5);
		gbc_poznamkaModelLabel.gridx = 11;
		gbc_poznamkaModelLabel.gridy = 10;
		add(poznamkaModelLabel, gbc_poznamkaModelLabel);
		
		textPoznamkaModel = new JLabel("xxxx");
		GridBagConstraints gbc_textPoznamkaModel = new GridBagConstraints();
		gbc_textPoznamkaModel.anchor = GridBagConstraints.NORTHWEST;
		gbc_textPoznamkaModel.gridwidth = 8;
		gbc_textPoznamkaModel.insets = new Insets(0, 0, 5, 5);
		gbc_textPoznamkaModel.gridx = 14;
		gbc_textPoznamkaModel.gridy = 10;
		add(textPoznamkaModel, gbc_textPoznamkaModel);
		oznacOdlito.setActionCommand("OznacOdlito");
		GridBagConstraints gbc_oznacOdlito = new GridBagConstraints();
		gbc_oznacOdlito.fill = GridBagConstraints.BOTH;
		gbc_oznacOdlito.insets = new Insets(0, 0, 5, 5);
		gbc_oznacOdlito.gridx = 1;
		gbc_oznacOdlito.gridy = 12;
		add(oznacOdlito, gbc_oznacOdlito);
		
		textIsOdhadLabel = new JLabel("ANO/NE");
		GridBagConstraints gbc_textIsOdhadLabel = new GridBagConstraints();
		gbc_textIsOdhadLabel.anchor = GridBagConstraints.WEST;
		gbc_textIsOdhadLabel.gridwidth = 2;
		gbc_textIsOdhadLabel.insets = new Insets(0, 0, 5, 5);
		gbc_textIsOdhadLabel.gridx = 20;
		gbc_textIsOdhadLabel.gridy = 9;
		add(textIsOdhadLabel, gbc_textIsOdhadLabel);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 3;
		gbc_tabbedPane.gridheight = 7;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 22;
		gbc_tabbedPane.gridy = 4;
		add(tabbedPane, gbc_tabbedPane);
		
		scrollPane_3 = new JScrollPane();
		tabbedPane.addTab("Dílèí termíny", null, scrollPane_3, null);
		
		list = new JList<DateStor>();	
		scrollPane_3.setViewportView(list);
		
		//tabbedPane.addTab("Seznam dílèích termínù", null, list, null);
		list.setFont(f);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(new LineBorder(new Color(192, 192, 192)));
		list.setEnabled(true);
		MyCellrendererCheckBox renderListener = new MyCellrendererCheckBox();
		list.setCellRenderer(renderListener);
		list. addMouseListener(new MouseAdapter()
         {
            public void mousePressed(MouseEvent e)
            {            	
               int index = list.locationToIndex(e.getPoint());
               if (index != -1) {
                  DateStor checkbox =
                		  list.getModel().getElementAt(index);
                  checkbox.setSelected(
                                     !checkbox.isSelected());
                  repaint();
               }
            }
         });
		
		
		
		
		
		scrollPane_2 = new JScrollPane();
		tabbedPane.addTab("Stav zakázky", null, scrollPane_2, null);
		
		stavZakazkyTable = new ColorCellTable(sklad.getPrazdneTabulky()[0], scrollPane_2, false, sklad);
		scrollPane_2.setViewportView(stavZakazkyTable);
		
		
		
		oznacZmetek = new MyJButton("(Ne)Ozna\u010Dit jako zmetek",16 ,1, sklad);
		oznacZmetek.addActionListener(this);
		oznacZmetek.setActionCommand("OznacZmetek");
		
		dateOdliti = new JDateChooser(new Date());
		dateOdliti.setFont(fonty[0]);
		GridBagConstraints gbc_dateOdliti = new GridBagConstraints();
		gbc_dateOdliti.insets = new Insets(0, 0, 5, 5);
		gbc_dateOdliti.fill = GridBagConstraints.BOTH;
		gbc_dateOdliti.gridx = 2;
		gbc_dateOdliti.gridy = 12;
		add(dateOdliti, gbc_dateOdliti);
		
		oznacVycisteno = new MyJButton("Ozna\u010Dit jako (ne)vy\u010Di\u0161t\u011Bno",16 ,1, sklad);
		oznacVycisteno.addActionListener(this);
		oznacVycisteno.setActionCommand("OznacVycisteno");
		GridBagConstraints gbc_oznacVycisteno = new GridBagConstraints();
		gbc_oznacVycisteno.gridwidth = 3;
		gbc_oznacVycisteno.fill = GridBagConstraints.BOTH;
		gbc_oznacVycisteno.insets = new Insets(0, 0, 5, 5);
		gbc_oznacVycisteno.gridx = 3;
		gbc_oznacVycisteno.gridy = 12;
		add(oznacVycisteno, gbc_oznacVycisteno);
		
		datumVycisteni = new JDateChooser(new Date());
		datumVycisteni.setFont(fonty[0]);
		GridBagConstraints gbc_datumVycisteni = new GridBagConstraints();
		gbc_datumVycisteni.gridwidth = 2;
		gbc_datumVycisteni.insets = new Insets(0, 0, 5, 5);
		gbc_datumVycisteni.fill = GridBagConstraints.BOTH;
		gbc_datumVycisteni.gridx = 6;
		gbc_datumVycisteni.gridy = 12;
		add(datumVycisteni, gbc_datumVycisteni);
		
		btnExpedovno = new MyJButton("Expedov\u00E1no",16 ,1, sklad);
		btnExpedovno.addActionListener(this);
		btnExpedovno.setActionCommand("IsExpedovano");
		GridBagConstraints gbc_btnExpedovno = new GridBagConstraints();
		gbc_btnExpedovno.gridwidth = 3;
		gbc_btnExpedovno.fill = GridBagConstraints.BOTH;
		gbc_btnExpedovno.insets = new Insets(0, 0, 5, 5);
		gbc_btnExpedovno.gridx = 8;
		gbc_btnExpedovno.gridy = 12;
		add(btnExpedovno, gbc_btnExpedovno);
		
		dateExpedice = new JDateChooser(new Date());
		dateExpedice.setFont(fonty[0]);
		GridBagConstraints gbc_dateExpedice = new GridBagConstraints();
		gbc_dateExpedice.gridwidth = 2;
		gbc_dateExpedice.insets = new Insets(0, 0, 5, 5);
		gbc_dateExpedice.fill = GridBagConstraints.BOTH;
		gbc_dateExpedice.gridx = 11;
		gbc_dateExpedice.gridy = 12;
		add(dateExpedice, gbc_dateExpedice);
		GridBagConstraints gbc_oznacZmetek = new GridBagConstraints();
		gbc_oznacZmetek.fill = GridBagConstraints.BOTH;
		gbc_oznacZmetek.insets = new Insets(0, 0, 5, 5);
		gbc_oznacZmetek.gridx = 1;
		gbc_oznacZmetek.gridy = 13;
		add(oznacZmetek, gbc_oznacZmetek);
		/*
		myCheckBox = new JCheckBox("Doplnit \u00FAdaje o vad\u011B");
		myCheckBox.setFont(f);
		myCheckBox.setFocusPainted(false);
		//myCheckBox.addChangeListener(this);
		myCheckBox.setBackground(barvy[12]);
		GridBagConstraints gbc_myCheckBox = new GridBagConstraints();
		gbc_myCheckBox.gridwidth = 3;
		gbc_myCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_myCheckBox.gridx = 3;
		gbc_myCheckBox.gridy = 13;
		add(myCheckBox, gbc_myCheckBox);
		*/
		
		dateZadaniZmetku = new JDateChooser(new Date());
		dateZadaniZmetku.setFont(fonty[0]);
		GridBagConstraints gbc_dateZadaniZmetku = new GridBagConstraints();
		gbc_dateZadaniZmetku.insets = new Insets(0, 0, 5, 5);
		gbc_dateZadaniZmetku.fill = GridBagConstraints.BOTH;
		gbc_dateZadaniZmetku.gridx = 2;
		gbc_dateZadaniZmetku.gridy = 13;
		add(dateZadaniZmetku, gbc_dateZadaniZmetku);
		
		doplnUdaje = new MyJButton("Doplnit \u00FAdaje o zmetku",16 ,1, sklad);
		doplnUdaje.addActionListener(this);
		doplnUdaje.setActionCommand("DoplnUdajeOZmetku");
		GridBagConstraints gbc_doplnUdaje = new GridBagConstraints();
		gbc_doplnUdaje.fill = GridBagConstraints.BOTH;
		gbc_doplnUdaje.gridwidth = 3;
		gbc_doplnUdaje.insets = new Insets(0, 0, 5, 5);
		gbc_doplnUdaje.gridx = 3;
		gbc_doplnUdaje.gridy = 13;
		add(doplnUdaje, gbc_doplnUdaje);
		
		
		lblVinik = new JLabel("Vin\u00EDk:");
		lblVinik.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblVinik = new GridBagConstraints();
		gbc_lblVinik.insets = new Insets(0, 0, 5, 5);
		gbc_lblVinik.anchor = GridBagConstraints.EAST;
		gbc_lblVinik.gridx = 6;
		gbc_lblVinik.gridy = 13;
		add(lblVinik, gbc_lblVinik);
		
		comboBoxVinik = new JComboBox();
		GridBagConstraints gbc_comboBoxVinik = new GridBagConstraints();
		gbc_comboBoxVinik.gridwidth = 4;
		gbc_comboBoxVinik.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxVinik.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxVinik.gridx = 7;
		gbc_comboBoxVinik.gridy = 13;
		add(comboBoxVinik, gbc_comboBoxVinik);
		
		lblVada = new JLabel("Vada:");
		lblVada.setFont(f);
		GridBagConstraints gbc_lblVada = new GridBagConstraints();
		gbc_lblVada.anchor = GridBagConstraints.EAST;
		gbc_lblVada.insets = new Insets(0, 0, 5, 5);
		gbc_lblVada.gridx = 11;
		gbc_lblVada.gridy = 13;
		add(lblVada, gbc_lblVada);
		
		comboBoxVada = new JComboBox();
		GridBagConstraints gbc_comboBoxVada = new GridBagConstraints();
		gbc_comboBoxVada.gridwidth = 4;
		gbc_comboBoxVada.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxVada.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxVada.gridx = 12;
		gbc_comboBoxVada.gridy = 13;
		add(comboBoxVada, gbc_comboBoxVada);
		
		lblZadatTavbu = new JLabel("Zadat \u010D\u00EDslo tavby:");
		lblZadatTavbu.setFont(f);
		GridBagConstraints gbc_lblZadatTavbu = new GridBagConstraints();
		gbc_lblZadatTavbu.anchor = GridBagConstraints.WEST;
		gbc_lblZadatTavbu.insets = new Insets(0, 0, 5, 5);
		gbc_lblZadatTavbu.gridx = 1;
		gbc_lblZadatTavbu.gridy = 15;
		add(lblZadatTavbu, gbc_lblZadatTavbu);
		
		textTavba = new JTextField();
		GridBagConstraints gbc_textTavba = new GridBagConstraints();
		gbc_textTavba.gridwidth = 2;
		gbc_textTavba.insets = new Insets(0, 0, 5, 5);
		gbc_textTavba.fill = GridBagConstraints.HORIZONTAL;
		gbc_textTavba.gridx = 2;
		gbc_textTavba.gridy = 15;
		add(textTavba, gbc_textTavba);
		textTavba.setColumns(10);
		
		zadejCisloTavby = new MyJButton("Zadej \u010D\u00EDslo tavby",16 ,1, sklad);
		zadejCisloTavby.setActionCommand("ZadejCisloTavby");
		zadejCisloTavby.addActionListener(this);
		GridBagConstraints gbc_zadejCisloTavby = new GridBagConstraints();
		gbc_zadejCisloTavby.fill = GridBagConstraints.BOTH;
		gbc_zadejCisloTavby.gridwidth = 3;
		gbc_zadejCisloTavby.insets = new Insets(0, 0, 5, 5);
		gbc_zadejCisloTavby.gridx = 5;
		gbc_zadejCisloTavby.gridy = 15;
		add(zadejCisloTavby, gbc_zadejCisloTavby);
		
		lblsloFaktury = new JLabel("\u010C\u00EDslo faktury:");
		popisLabels[22] = lblsloFaktury;
		GridBagConstraints gbc_lblsloFaktury = new GridBagConstraints();
		gbc_lblsloFaktury.anchor = GridBagConstraints.WEST;
		gbc_lblsloFaktury.gridwidth = 2;
		gbc_lblsloFaktury.insets = new Insets(0, 0, 5, 5);
		gbc_lblsloFaktury.gridx = 8;
		gbc_lblsloFaktury.gridy = 15;
		add(lblsloFaktury, gbc_lblsloFaktury);
		
		textCisloFaktury = new JTextField();
		GridBagConstraints gbc_textCisloFaktury = new GridBagConstraints();
		gbc_textCisloFaktury.gridwidth = 2;
		gbc_textCisloFaktury.insets = new Insets(0, 0, 5, 5);
		gbc_textCisloFaktury.fill = GridBagConstraints.HORIZONTAL;
		gbc_textCisloFaktury.gridx = 10;
		gbc_textCisloFaktury.gridy = 15;
		add(textCisloFaktury, gbc_textCisloFaktury);
		textCisloFaktury.setColumns(10);
		
		zadatCisloFaktury = new MyJButton("Zadat \u010D\u00EDslo faktury", 16 ,1, sklad);
		zadatCisloFaktury.setActionCommand("ZadatCisloFaktury");
		zadatCisloFaktury.addActionListener(this);
		GridBagConstraints gbc_zadatCisloFaktury = new GridBagConstraints();
		gbc_zadatCisloFaktury.fill = GridBagConstraints.BOTH;
		gbc_zadatCisloFaktury.gridwidth = 5;
		gbc_zadatCisloFaktury.insets = new Insets(0, 0, 5, 5);
		gbc_zadatCisloFaktury.gridx = 13;
		gbc_zadatCisloFaktury.gridy = 15;
		add(zadatCisloFaktury, gbc_zadatCisloFaktury);
		
		lblTeplotaLit = new JLabel("Teplota lit\u00ED:");
		popisLabels[26] = lblTeplotaLit;
		GridBagConstraints gbc_lblTeplotaLit = new GridBagConstraints();
		gbc_lblTeplotaLit.anchor = GridBagConstraints.EAST;
		gbc_lblTeplotaLit.insets = new Insets(0, 0, 5, 5);
		gbc_lblTeplotaLit.gridx = 18;
		gbc_lblTeplotaLit.gridy = 15;
		add(lblTeplotaLit, gbc_lblTeplotaLit);
		
		teplotaLitiText = new JTextField();
		GridBagConstraints gbc_teplotaLitiText = new GridBagConstraints();
		gbc_teplotaLitiText.gridwidth = 4;
		gbc_teplotaLitiText.insets = new Insets(0, 0, 5, 5);
		gbc_teplotaLitiText.fill = GridBagConstraints.HORIZONTAL;
		gbc_teplotaLitiText.gridx = 19;
		gbc_teplotaLitiText.gridy = 15;
		add(teplotaLitiText, gbc_teplotaLitiText);
		teplotaLitiText.setColumns(10);
		
		pridejTeplotuLiti = new MyJButton("Zadej teplotu lití", 16 ,1, sklad);
		pridejTeplotuLiti.setActionCommand("PridejTeplotuLiti");
		pridejTeplotuLiti.addActionListener(this);
		GridBagConstraints gbc_pridejTeplotuLiti = new GridBagConstraints();
		gbc_pridejTeplotuLiti.gridwidth = 2;
		gbc_pridejTeplotuLiti.fill = GridBagConstraints.BOTH;
		gbc_pridejTeplotuLiti.insets = new Insets(0, 0, 5, 5);
		gbc_pridejTeplotuLiti.gridx = 23;
		gbc_pridejTeplotuLiti.gridy = 15;
		add(pridejTeplotuLiti, gbc_pridejTeplotuLiti);
		
		JLabel lblSeznamKus = new JLabel("Seznam kus\u016F:");
		popisLabels[23] = lblSeznamKus;
		GridBagConstraints gbc_lblSeznamKus = new GridBagConstraints();
		gbc_lblSeznamKus.anchor = GridBagConstraints.WEST;
		gbc_lblSeznamKus.insets = new Insets(0, 0, 5, 5);
		gbc_lblSeznamKus.gridx = 1;
		gbc_lblSeznamKus.gridy = 16;
		add(lblSeznamKus, gbc_lblSeznamKus);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setWheelScrollingEnabled(false);
		scrollPane_1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		scrollPane_1.addMouseWheelListener(new MouseWheelListener() {
		    @Override
		    public void mouseWheelMoved(MouseWheelEvent e) {
		    	scrollPane_1.getParent().dispatchEvent(e);
		    }
		});
		
		dokonciZadavani = new MyJButton("Dokon\u010Dit zad\u00E1v\u00E1n\u00ED", 16, 1, sklad);
		dokonciZadavani.setActionCommand("DokonciZadavani");
		dokonciZadavani.addActionListener(this);
		GridBagConstraints gbc_dokonciZadavani = new GridBagConstraints();
		gbc_dokonciZadavani.insets = new Insets(0, 0, 5, 5);
		gbc_dokonciZadavani.fill = GridBagConstraints.BOTH;
		gbc_dokonciZadavani.gridx = 1;
		gbc_dokonciZadavani.gridy = 17;
		add(dokonciZadavani, gbc_dokonciZadavani);
		
		pocetNeplanZmetku = new JLabel("XX");
		popisLabels[24] = pocetNeplanZmetku;
		GridBagConstraints gbc_pocetNeplanZmetku = new GridBagConstraints();
		gbc_pocetNeplanZmetku.anchor = GridBagConstraints.WEST;
		gbc_pocetNeplanZmetku.insets = new Insets(0, 0, 5, 5);
		gbc_pocetNeplanZmetku.gridx = 8;
		gbc_pocetNeplanZmetku.gridy = 17;
		add(pocetNeplanZmetku, gbc_pocetNeplanZmetku);
		
		aktualizovatBut = new MyJButton("Aktualizovat", 16, 1, sklad);
		aktualizovatBut.setActionCommand("Aktualizovat");
		aktualizovatBut.addActionListener(this);
		GridBagConstraints gbc_aktualizovatBut = new GridBagConstraints();
		gbc_aktualizovatBut.gridwidth = 3;
		gbc_aktualizovatBut.fill = GridBagConstraints.BOTH;
		gbc_aktualizovatBut.insets = new Insets(0, 0, 5, 5);
		gbc_aktualizovatBut.gridx = 9;
		gbc_aktualizovatBut.gridy = 17;
		add(aktualizovatBut, gbc_aktualizovatBut);
		
		generovatNoveKusy = new MyJButton("Generovat nov\u00E9 kusy m\u00EDsto zmetk\u016F", 16, 1, sklad);
		generovatNoveKusy.setActionCommand("GennerujNoveKusyMistoZmetku");
		generovatNoveKusy.addActionListener(this);
		GridBagConstraints gbc_generovatNoveKusy = new GridBagConstraints();
		gbc_generovatNoveKusy.fill = GridBagConstraints.BOTH;
		gbc_generovatNoveKusy.gridwidth = 7;
		gbc_generovatNoveKusy.insets = new Insets(0, 0, 5, 5);
		gbc_generovatNoveKusy.gridx = 12;
		gbc_generovatNoveKusy.gridy = 17;
		add(generovatNoveKusy, gbc_generovatNoveKusy);
		
		btnUzavtZakzku = new MyJButton("Uzav\u0159\u00EDt zak\u00E1zku (P\u0159esnos do archivu)", 16, 1, sklad);
		btnUzavtZakzku.setActionCommand("UzavriZakazku");
		btnUzavtZakzku.addActionListener(this);
		GridBagConstraints gbc_btnUzavtZakzku = new GridBagConstraints();
		gbc_btnUzavtZakzku.fill = GridBagConstraints.BOTH;
		gbc_btnUzavtZakzku.gridwidth = 5;
		gbc_btnUzavtZakzku.insets = new Insets(0, 0, 5, 5);
		gbc_btnUzavtZakzku.gridx = 20;
		gbc_btnUzavtZakzku.gridy = 17;
		add(btnUzavtZakzku, gbc_btnUzavtZakzku);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.gridwidth = 25;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 18;
		add(scrollPane_1, gbc_scrollPane_1);
		
		tableFyzkusyEx = new ColorCellTable(sklad.getPrazdneTabulky()[3], scrollPane_1, false ,sklad);
		tableFyzkusyEx.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		scrollPane_1.setViewportView(tableFyzkusyEx);
		
		initLabels();

	}

	
	/**
	 * <p>Listener tlaèítek: oznacOdlito, oznacVycisteno, btnExpedovno, oznacZmetek, doplnUdaje, zadejCisloTavby, zadatCisloFaktury,
	 * dokonciZadavani, aktualizovatBut, generovatNoveKusy, btnUzavtZakzku. </p>
	 * <p>Tato tlaèítka jsou vytvoøeny v této tøídì a jsou umístìny v tomto JPanelu.</p>
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			int[] selected = this.tableFyzkusyEx.getSelectedRows();
			String event = arg0.getActionCommand();
			if (event.equalsIgnoreCase("Aktualizovat") || event.equalsIgnoreCase("GennerujNoveKusyMistoZmetku")
					||  event.equalsIgnoreCase("DokonciZadavani") || event.equalsIgnoreCase("UzavriZakazku")){
				// nic
			} else if (selected.length <= 0) {
				JOptionPane.showMessageDialog(hlavniOkno,"Neni oznaèena žádná øada. Oznaète prosím nìjakou øadu v horní tabulce a to i v pøípadì, že oznaèovat nic nepotøebujete (Dokonèení zadávání apod.)");
				return;
			}
			if (event.equalsIgnoreCase("OznacOdlito") || event.equalsIgnoreCase("OznacVycisteno") || event.equalsIgnoreCase("ZadejDatumExpedice") | event.equalsIgnoreCase("OznacZmetek")
					|| event.equalsIgnoreCase("DoplnUdajeOZmetku") || event.equalsIgnoreCase("IsExpedovano")) {
				for (int i = 0; i < selected.length; i++) {
					event = (String) this.tableFyzkusyEx.getValueAt(selected[i], 1); // pomocna promena (abych nemusel deklarovat novy ukazatel)
					if (event == null) {
						JOptionPane.showMessageDialog(hlavniOkno,"Nejdøív musíte naplánovat datum lití, pak mùžete zadavat odlitky/zmetky (nemùžete oznaèit kus jako zmetek, pokud není ještì odlitý)");
						return;
					} else if (event.equalsIgnoreCase("")) {
						JOptionPane.showMessageDialog(hlavniOkno,"Nejdøív musíte naplánovat datum lití, pak mùžete zadavat odlitky/zmetky (nemùžete oznaèit kus jako zmetek, pokud není ještì odlitý)");
						return;
					}
				}
			}
			
			event = arg0.getActionCommand();
			if (event.equalsIgnoreCase("OznacOdlito")) {
				for (int i = 0; i < selected.length; i++) {
					if (((String) this.tableFyzkusyEx.getValueAt(selected[i], 3)).equalsIgnoreCase("Ne")) {
						this.tableFyzkusyEx.setValueAt("Ano", selected[i], 3);
						String datum = sdf.format(dateOdliti.getDate());
						this.tableFyzkusyEx.setValueAt(datum, selected[i], 6);
					} else if (((String) this.tableFyzkusyEx.getValueAt(selected[i], 3)).equalsIgnoreCase("Ano")) {
						this.tableFyzkusyEx.setValueAt("Ne", selected[i], 3);
						this.tableFyzkusyEx.setValueAt(null, selected[i], 6);
						this.tableFyzkusyEx.setValueAt("Ne", selected[i], 4);
						this.tableFyzkusyEx.setValueAt("Ne", selected[i], 7);
						this.tableFyzkusyEx.setValueAt(null, selected[i], 9);
						this.tableFyzkusyEx.setValueAt("Ne", selected[i], 5);
						this.tableFyzkusyEx.setValueAt(null, selected[i], 14);
					}
				}
			} else if (event.equalsIgnoreCase("OznacVycisteno")) {
				for (int i = 0; i < selected.length; i++) {
					if((String) this.tableFyzkusyEx.getValueAt(selected[i], 6) == null){ // neni zadan datum odliti
						JOptionPane.showMessageDialog(hlavniOkno,"Zadejte datum odlití");
						return;
					} else if(((String) this.tableFyzkusyEx.getValueAt(selected[i], 6)).equalsIgnoreCase("")){
						JOptionPane.showMessageDialog(hlavniOkno,"Zadejte datum odlití");
						return;
					}
					this.tableFyzkusyEx.setValueAt("Ano", selected[i], 3);
					if (((String) this.tableFyzkusyEx.getValueAt(selected[i], 4)).equalsIgnoreCase("Ne")) {
						this.tableFyzkusyEx.setValueAt("Ano", selected[i], 4);
						this.tableFyzkusyEx.setValueAt(sdf.format(datumVycisteni.getDate()), selected[i], 9);
					} else if (((String) this.tableFyzkusyEx.getValueAt(selected[i], 4)).equalsIgnoreCase("Ano")) {
						this.tableFyzkusyEx.setValueAt("Ne", selected[i], 4);
						this.tableFyzkusyEx.setValueAt(null, selected[i], 9);
					}
					this.tableFyzkusyEx.setValueAt("Ne", selected[i], 7);
					//this.tableFyzkusyEx.setValueAt(null, selected[i], 9);
					// this.tableFyzkusyEx.setValueAt("Ne", selected[i], 5);
				}
			} else if (event.equalsIgnoreCase("ZadatCisloFaktury")) {
				String cislo = this.textCisloFaktury.getText();
				if(cislo.length() > SQLStor.maxDelkaCislaFaktury){
					JOptionPane.showMessageDialog(hlavniOkno,"Èíslo faktury mùže být maximálnì 19 znakù dlouhé");
					return;
				}
				/*try{
					Long.parseLong(cislo);
				}catch (NumberFormatException e){
					JOptionPane.showMessageDialog(hlavniOkno,"Èíslo faktury není èíslo");
					return;
				}*/
				for (int i = 0; i < selected.length; i++) {
					this.tableFyzkusyEx.setValueAt(cislo, selected[i], poziceCislaFakturyVTabulce);
				}
				tableFyzkusyEx.getColumAdjuster().adjustColumns();

			} else if (event.equalsIgnoreCase("ZadejDatumExpedice")) {
				for (int i = 0; i < selected.length; i++) {
					this.tableFyzkusyEx.setValueAt("Ano", selected[i], 3);
					this.tableFyzkusyEx.setValueAt("Ano", selected[i], 4);
					this.tableFyzkusyEx.setValueAt("Ne", selected[i], 7);
					if (datumVycisteni.getDate() == null)
						this.tableFyzkusyEx.setValueAt(null, selected[i], 9);
					else
						this.tableFyzkusyEx.setValueAt(sdf.format(datumVycisteni.getDate()),selected[i], 9);
					this.tableFyzkusyEx.setValueAt("Ne", selected[i], 5);
				}
				tableFyzkusyEx.getColumAdjuster().adjustColumns();

			} else if (event.equalsIgnoreCase("OznacZmetek")) {
				for (int i = 0; i < selected.length; i++) {
					if((String) this.tableFyzkusyEx.getValueAt(selected[i], 6) == null){ // neni zadan datum odliti
						JOptionPane.showMessageDialog(hlavniOkno,"Zadejte datum odlití");
						return;
					} else if(((String) this.tableFyzkusyEx.getValueAt(selected[i], 6)).equalsIgnoreCase("")){
						JOptionPane.showMessageDialog(hlavniOkno,"Zadejte datum odlití");
						return;
					}
					//this.tableFyzkusyEx.setValueAt("Ano", selected[i], 3);
					this.tableFyzkusyEx.setValueAt("Ne", selected[i], 7);
					if (((String) this.tableFyzkusyEx.getValueAt(selected[i], 5)).equalsIgnoreCase("Ne")) {
						//this.tableFyzkusyEx.setValueAt(null, selected[i], 6);
						this.tableFyzkusyEx.setValueAt("Ano", selected[i], 5);
						this.tableFyzkusyEx.setValueAt(sdf.format(dateZadaniZmetku.getDate()), selected[i], 15);
					} else if (((String) this.tableFyzkusyEx.getValueAt(selected[i], 5)).equalsIgnoreCase("Ano")) {
						this.tableFyzkusyEx.setValueAt(null, selected[i], 10);
						this.tableFyzkusyEx.setValueAt(null, selected[i], 11);
						this.tableFyzkusyEx.setValueAt(null, selected[i], 12);
						this.tableFyzkusyEx.setValueAt(null, selected[i], 13);
						this.tableFyzkusyEx.setValueAt("Ne", selected[i], 5);
						this.tableFyzkusyEx.setValueAt(null, selected[i], 15);
					}
				}
				tableFyzkusyEx.getColumAdjuster().adjustColumns();
			} else if (event.equalsIgnoreCase("DoplnUdajeOZmetku")) {
				String tmp;
				for (int i = 0; i < selected.length; i++) {
					tmp = (String) this.tableFyzkusyEx.getValueAt(selected[i],5);
					if (tmp.equalsIgnoreCase("Ano")) {
						this.tableFyzkusyEx.setValueAt("Ano", selected[i], 3);
						this.tableFyzkusyEx.setValueAt("Ano", selected[i], 5);
						this.tableFyzkusyEx.setValueAt("Ne", selected[i], 7);
						String vinik = (String) this.comboBoxVinik.getSelectedItem();
						String vada = (String) this.comboBoxVada.getSelectedItem();

						String[] idAVinik = null;
						String[] idAVada = null;
						if (vinik == null || vada == null) {
							idAVinik = new String[2];
							idAVinik[0] = "";
							idAVinik[1] = "";
							idAVada = new String[2];
							idAVada[0] = "";
							idAVada[1] = "";
						} else {
							idAVinik = vinik.split(" ");
							idAVada = vada.split(" ");
						}

						this.tableFyzkusyEx.setValueAt(idAVinik[0],
								selected[i], 10);
						vinik = "";
						for (int m = 1; m < idAVinik.length; m++)
							vinik += idAVinik[m];
						this.tableFyzkusyEx.setValueAt(vinik, selected[i], 11);
						this.tableFyzkusyEx.setValueAt(idAVada[0], selected[i],
								12);
						vada = "";
						for (int m = 1; m < idAVada.length; m++)
							vada += idAVada[m];
						this.tableFyzkusyEx.setValueAt(vada, selected[i], 13);
						this.tableFyzkusyEx.getColumAdjuster().adjustColumns();
					} else {
						JOptionPane.showMessageDialog(hlavniOkno,"Nejdøív musíte oznaèit daný kus jako zmetek, pak mužete zadávat údaje. Ukonèuji upravování.");
						break;
					}
				}

			} else if (event.equalsIgnoreCase("IsExpedovano")) {
				boolean upozor = false;
				for (int i = 0; i < selected.length; i++) {
					if (((String) tableFyzkusyEx.getValueAt(selected[i], 7))
							.equalsIgnoreCase("Ne")) {
						if (tableFyzkusyEx.getValueAt(selected[i], 9) == null) {
							upozor = true;
							continue;
						}
						if (((String) tableFyzkusyEx.getValueAt(selected[i], 9)).equalsIgnoreCase("")) {
							upozor = true;
							continue;
						}
					}
					this.tableFyzkusyEx.setValueAt("Ano", selected[i], 3);
					this.tableFyzkusyEx.setValueAt("Ano", selected[i], 4);
					if (((String) tableFyzkusyEx.getValueAt(selected[i], 7)).equalsIgnoreCase("Ne")) {
						this.tableFyzkusyEx.setValueAt("Ano", selected[i], 7);
						this.tableFyzkusyEx.setValueAt(sdf.format(dateExpedice.getDate()), selected[i], 14);
					} else if (((String) tableFyzkusyEx.getValueAt(selected[i], 7)).equalsIgnoreCase("Ano")) {
						this.tableFyzkusyEx.setValueAt("Ne", selected[i], 7);
						this.tableFyzkusyEx.setValueAt(null, selected[i], 14);
					}
					this.tableFyzkusyEx.setValueAt("Ne", selected[i], 5);
				}
				if (upozor) {
					JOptionPane.showMessageDialog(hlavniOkno,"Nejdøíve vyplòte datum vyèištìní potom oznaète jako expedovano");
				}
			} else if (event.equalsIgnoreCase("ZadejCisloTavby")) {
				String text = textTavba.getText();
				if(text.length() > SQLStor.maxDelkaCislaTavby){
					JOptionPane.showMessageDialog(hlavniOkno,"Èíslo tavby mùže být maximálnì 10 znakù dlouhé");
					return;
				}
				for(int i = 0; i < selected.length; i++){
					this.tableFyzkusyEx.setValueAt(text, selected[i], poziceCislaTavbyVTabulce);
				}
			} else if (event.equalsIgnoreCase("PridejTeplotuLiti")) { // TODO
				String text = this.teplotaLitiText.getText();
				if(text.length() > SQLStor.maxDelkaTeplotyLiti){
					JOptionPane.showMessageDialog(hlavniOkno,"Teplota lití mùže být maximálnì 10 znakù dlouhé");
					return;
				}
				for(int i = 0; i < selected.length; i++){
					this.tableFyzkusyEx.setValueAt(text, selected[i], poziceTeplotyLitiVTabulce);
				}
			} else if (event.equalsIgnoreCase("Aktualizovat")) {
				this.setZadejOdlitekZmetek(parametryZakazky, Integer.parseInt(this.textIdZakazky.getText()));

			} else if (event.equalsIgnoreCase("GennerujNoveKusyMistoZmetku")) {
				int confirm = JOptionPane.showConfirmDialog(hlavniOkno,"Toto je nevratná akce. Musíte si být jisti, zmetky již nelze opravit"
					+ " èi uznat.\n Respektive budete mít pro jednu zakázku více odlitkù než je objednáno a napsáno v databázi\n Chcete pokraèovat?","Upozornìní", JOptionPane.YES_NO_OPTION);
				if (confirm != 0) {
					JOptionPane.showMessageDialog(hlavniOkno, "Pøerušeno");
					return;
				}
				boolean[][] zmeneno = tableFyzkusyEx.getZmeneno();
				boolean pomBol = false;
				for (int i = 0; i < zmeneno.length; i++) {
					for (int j = 0; j < zmeneno[i].length; j++) {
						if (zmeneno[i][j]) {
							pomBol = true;
							break;
						}
					}
					if (pomBol)
						break;
				}
				if (pomBol) {
					JOptionPane.showMessageDialog(hlavniOkno,"Prosím, uložte nejdøíve tabulku do databáze a pak ji aktualizujte");
					return;
				}
				sql.gennerujNoveKusy(Integer.parseInt(textIdZakazky.getText()));
				// Aktualizace (viz Aktualizovat JButton)
				this.setZadejOdlitekZmetek(parametryZakazky, Integer.parseInt(this.textIdZakazky.getText())); 
				
			} else if (event.equalsIgnoreCase("DokonciZadavani")) { // TODO
				boolean[][] zmeneno = tableFyzkusyEx.getZmeneno();
				boolean pomBol = false, needOnlyProhlizecPriv = true;
				int upraveno = 0;
				for (int i = 0; i < zmeneno.length; i++) {
					pomBol = false;
					needOnlyProhlizecPriv = true;
					for (int j = 0; j < zmeneno[i].length; j++) {
						if (zmeneno[i][j]) {
							if(j != poziceCislaTavbyVTabulce || j != poziceCislaFakturyVTabulce){
								needOnlyProhlizecPriv = false;
							}
							pomBol = true;
						}
					}
					if (pomBol) { // radek je zmenen musí se aktualizovat
						if(needOnlyProhlizecPriv){ // zmeneno je jen cislo faktury nebo cislo tavby tzn j = 2 nebo j =8
							endZadavaniCislaTavbyCislaFaktury(i);
						} else {
							endZadavaniOdlitku(i);
							if (zmeneno[i][10] || zmeneno[i][12]) {
								if (!endZadavaniVadyAVinika(i)) {
									JOptionPane.showMessageDialog(hlavniOkno,"Nìjakému upravenému kusu se nepodaøilo pøidat vadu a vinika");
									break;
								}
							}
						}
						upraveno++;
					}
				}
				// dílèí termíny, pouze zmenìné
				ListModel<DateStor> mod = list.getModel();
				DateStor pom;
				int zmenenoDatumu = 0;
				for(int i = 0; i < mod.getSize(); i++){
					pom = mod.getElementAt(i);
					if(pom.isZmeneno()){
						zmenenoDatumu++;
						sql.zadejDilciTermin(Integer.parseInt(this.textIdZakazky.getText()), pom.getDate(), pom.getPocetKusu(), pom.isSelected());
					}
				}
				if(upraveno > 0 && zmenenoDatumu > 0){
					JOptionPane.showMessageDialog(hlavniOkno,"Parametry fyzických kusù a splnìných termínù úspìšnì pøeneseny do databáze, upraveno: "+upraveno);
				} else if(upraveno > 0){
					JOptionPane.showMessageDialog(hlavniOkno,"Parametry fyzických kusù úspìšnì pøeneseny do databáze, upraveno: "+upraveno);					
				} else if(zmenenoDatumu > 0){
					JOptionPane.showMessageDialog(hlavniOkno,"Splnìné termíny byly pøeneseny do databáze");					
				} else {
					JOptionPane.showMessageDialog(hlavniOkno,"Nebyly provedeny žádné zmìny v databázi");
				}
			} else if (event.equalsIgnoreCase("UzavriZakazku")){
				int id = Integer.parseInt(textIdZakazky.getText());
				sql.uzavriZakazku(id);
			}  else if (event.equalsIgnoreCase("ObnovZakazku")){
				int id = Integer.parseInt(textIdZakazky.getText());
				sql.obnovZakazku(id);
			}
		} catch (Exception e) {
			if(e.getLocalizedMessage() != null){
				if(e.getLocalizedMessage().startsWith(acesDenied)){
					JOptionPane.showMessageDialog(hlavniOkno, "Na tuto operaci nemáte pravomoce");
				}else {
					ExceptionWin.showExceptionMessage(e);
				}
			} else {
				ExceptionWin.showExceptionMessage(e);
			}
		}
	}
	
	/**
	 * Zadej odlitek
	 * @param i øada ve které je zadávaný odlitek
	 * @throws SQLException
	 * @throws ParseException
	 */
	private void endZadavaniOdlitku(int i) throws SQLException, ParseException{
		int idKusu = Integer.parseInt((String)this.tableFyzkusyEx.getValueAt(i, 0));
		boolean isOdlito;
		if(((String)this.tableFyzkusyEx.getValueAt(i, 3)).equalsIgnoreCase("Ano")){
			isOdlito = true;
		} else if(((String)this.tableFyzkusyEx.getValueAt(i, 3)).equalsIgnoreCase("Ne")){
			isOdlito = false;
		} else {
			JOptionPane.showMessageDialog(hlavniOkno, "Vyskytla se neèekaná chyba ExpediceZmetek.class, endZadavaniOdlitku()");
			return;
		}
		
		boolean isVycisteno;
		if(((String)this.tableFyzkusyEx.getValueAt(i, 4)).equalsIgnoreCase("Ano")){
			isVycisteno = true;
		} else if(((String)this.tableFyzkusyEx.getValueAt(i, 4)).equalsIgnoreCase("Ne")){
			isVycisteno = false;
		} else {
			JOptionPane.showMessageDialog(hlavniOkno, "Vyskytla se neèekaná chyba ExpediceZmetek.class, øadek 1054");
			return;
		}
		
		boolean isZmetek;
		if(((String)this.tableFyzkusyEx.getValueAt(i, 5)).equalsIgnoreCase("Ano")){
			isZmetek = true;
		} else if(((String)this.tableFyzkusyEx.getValueAt(i, 5)).equalsIgnoreCase("Ne")){
			isZmetek = false;
		} else {
			JOptionPane.showMessageDialog(hlavniOkno, "Vyskytla se neèekaná chyba ExpediceZmetek.class, øadek 1064");
			return;
		}
		
		String datum = (String)this.tableFyzkusyEx.getValueAt(i, 6);
		Date datumOdliti = null;
		if (datum != null) {
			if(!datum.equalsIgnoreCase("")){
				datumOdliti = sdf.parse(datum);
			}
		}
		//Date datumExpedice = this.datumExpedice.getDate();
		datum = (String)this.tableFyzkusyEx.getValueAt(i, 9);
		Date datumVycisteni = null;
		if (datum != null) {
			if(!datum.equalsIgnoreCase("")){
				datumVycisteni = sdf.parse(datum);
			}
		}
		
		datum = (String)this.tableFyzkusyEx.getValueAt(i, 14);
		Date datumExpedice = null;
		if (datum != null) {
			if(!datum.equalsIgnoreCase("")){
				datumExpedice = sdf.parse(datum);
			}
		}
		
		datum = (String)this.tableFyzkusyEx.getValueAt(i, 15);
		Date datumZadaniodlitku = null;
		if (datum != null) {
			if(!datum.equalsIgnoreCase("")){
				datumZadaniodlitku = sdf.parse(datum);
			}
		}
		
		boolean isExpedovano;
		if(((String)this.tableFyzkusyEx.getValueAt(i, 7)).equalsIgnoreCase("Ano")){
			isExpedovano = true;
		} else if(((String)this.tableFyzkusyEx.getValueAt(i, 7)).equalsIgnoreCase("Ne")){
			isExpedovano = false;
		} else {
			JOptionPane.showMessageDialog(hlavniOkno, "Vyskytla se neèekaná chyba ExpediceZmetek.class, øadek 1076");
			return;
		}
		String cisloTavby = (String) this.tableFyzkusyEx.getValueAt(i, poziceCislaTavbyVTabulce);
		String cisloFakturyStr = (String) this.tableFyzkusyEx.getValueAt(i, poziceCislaFakturyVTabulce);
		String teplotaLiti = (String) this.tableFyzkusyEx.getValueAt(i, poziceTeplotyLitiVTabulce);
	
		
		
		if(!isOdlito){
			datumOdliti = null;
			isVycisteno = false;
			datumVycisteni = null;
			isExpedovano= false;
			datumExpedice = null;
			isZmetek = false;
		}
		sql.zadejOdlitek(idKusu, isOdlito, datumOdliti, isVycisteno, datumVycisteni, isExpedovano,
				datumExpedice,  isZmetek, datumZadaniodlitku, cisloTavby, cisloFakturyStr, teplotaLiti);
	}

	private boolean endZadavaniVadyAVinika(int i) throws SQLException{
		boolean [][] zmeneno = this.tableFyzkusyEx.getZmeneno();
		if(zmeneno[i][10]  && zmeneno[i][12]){
			if(this.tableFyzkusyEx.getValueAt(i, 10) != null && this.tableFyzkusyEx.getValueAt(i, 12) != null
					&& !((String)tableFyzkusyEx.getValueAt(i, 10)).equalsIgnoreCase("") && !((String)tableFyzkusyEx.getValueAt(i, 12)).equalsIgnoreCase("")){
				int idKusu = Integer.parseInt((String)this.tableFyzkusyEx.getValueAt(i, 0));
				int idVinika = Integer.parseInt((String)this.tableFyzkusyEx.getValueAt(i, 10));
				int idVady = Integer.parseInt((String)this.tableFyzkusyEx.getValueAt(i, 12));
				boolean pom = sql.zadejVadyZmetku(idKusu, idVinika, idVady);
				if(!pom){
					JOptionPane.showMessageDialog(hlavniOkno, "Nepodaøilo se pøidat vadu a viníka");
					return false;
				}
			} else {
				int idKusu = Integer.parseInt((String)this.tableFyzkusyEx.getValueAt(i, 0));
				int idVinika = -1;
				int idVady = -1;
				boolean pom = sql.zadejVadyZmetku(idKusu, idVinika, idVady);
				if(!pom){
					JOptionPane.showMessageDialog(hlavniOkno, "Nepodaøilo se upravit vadu a viníka");
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	
	private void endZadavaniCislaTavbyCislaFaktury(int i) throws SQLException{
		String cisloTavby = (String) tableFyzkusyEx.getValueAt(i, poziceCislaTavbyVTabulce);
		String cisloFaktury = (String) tableFyzkusyEx.getValueAt(i, poziceCislaFakturyVTabulce);
		int idKusu = Integer.parseInt((String)this.tableFyzkusyEx.getValueAt(i, 0));
		sql.zadejCisloTavbyCisloFakturyTeplotuLiti(idKusu, cisloTavby, cisloFaktury);
	}
	
	/*
	private String odlitoDate = null;
	private String vycistenoDate = null;
	private String expedovanoDate = null;
	private String zmetekDate = null;
	private void initStavOdlitek(int row){
		odlitoDate = (String) tableFyzkusyEx.getValueAt(row, 3);
		vycistenoDate = (String) tableFyzkusyEx.getValueAt(row, 3);
		expedovanoDate = (String) tableFyzkusyEx.getValueAt(row, 3);
		zmetekDate = (String) tableFyzkusyEx.getValueAt(row, 3);
		
		tableFyzkusyEx.setValueAt("Ne", row, 3); // odlito
		tableFyzkusyEx.setValueAt(null, row, 6); // datum odlito
		tableFyzkusyEx.setValueAt("Ne", row, 4); // vycisteno
		tableFyzkusyEx.setValueAt(null, row, 9); // datum vycisteno
		tableFyzkusyEx.setValueAt("Ne", row, 7); // expedovano
		tableFyzkusyEx.setValueAt(null, row, 14); // datum expedovano
		tableFyzkusyEx.setValueAt("Ne", row, 5); // zmetek
		tableFyzkusyEx.setValueAt(null, row, 15); // datum zmetek
	}
	private void zadejOdlitoCmd(){
		
	}
	private void zadejOdlito(boolean odlito, String date, int row){
		zadejHodnotu(odlito, date, row, 3, 6); // 3 - odlito, 6 datum odlito
	}
	
	private void zadejVycisteno(boolean vycisteno, String date, int row){
		zadejHodnotu(vycisteno, date, row, 4, 9); // 4 - vycistno, 9 datum vycisteno
	}
	private void zadejExpedovano(boolean expedovano, String date, int row){
		zadejHodnotu(expedovano, date, row, 7, 14); // 7 - expedovano, 14 datum expedovano
	}
	
	private void zadejHodnotu(boolean hodnota, String date, int row, int columnBool, int ColumnDate){
		if(hodnota){
			try{
				sdf.parse(date); // oveøeni že to je regulerni datum
			} catch(Exception e){
				return;
			}
			tableFyzkusyEx.setValueAt("Ano", row, columnBool); // odlito
			tableFyzkusyEx.setValueAt(date, row, ColumnDate); // datum odlito
		} else {
			tableFyzkusyEx.setValueAt("Ne", row, columnBool); // odlito
			tableFyzkusyEx.setValueAt(null, row, ColumnDate); // datum odlito
		}
	}
	*/
		
	
}
