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
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sablony.DateStor;
import sablony.MyJButton;
import sablony.MyJDateChooser;
import sablony.errorwin.ExceptionWin;
import sablony.tabulka.ColorCellTable;
import sablony.tabulka.QueryTableModel;
import sqlstorage.SQLStor;
import sqlstorage.TransClass;
import storage.SkladOdkazu;

import javax.swing.ListSelectionModel;

import myinterface.NastavOkno;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JList;

public class Planovani extends JPanel implements NastavOkno, ActionListener, ListSelectionListener, KeyListener {
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;
	private SkladOdkazu sklad;
	private MainFrame hlavniOkno;
	
	private ColorCellTable tableFyzkusy;
	private ColorCellTable tableGenericka;
	
	private final int radekZacinajiciTabulkaFyzkusy = 14;
	private static final int maxVyskaFyzKusyTabulky = 300;
	private static final String acesDenied = "execute command denied to user";
	
	private JLabel [] textLabels;
	private JLabel [] popisLabels;
	
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
	private JLabel textCena;
	
	private JTextPane textPoznamka;
	
	private MyJDateChooser pridatDatumLiti;
	
	private SimpleDateFormat sdf;
	
	private Font [] fonty; 
	private Font f = new Font("Tahoma", Font.PLAIN, 14);
	private Font fb = new Font("Tahoma", Font.BOLD, 15);
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
	private JScrollPane scrollPane_1;
	private JLabel textIsCzk;
	private JLabel lblCena;
	private JLabel pocetNeplanovanychKusu;
	private JLabel textLabelOznaceno;
	private JLabel textKurz;
	private JLabel lblKurz;
	private JLabel lblJeTatoZakzka;
	private JLabel textIsUzavrena;
	private MyJButton pridatDatum;
	private MyJButton dokonciZadavani;
	private boolean isPlanovaniLiti = true;
	private JLabel nadpis;
	private JScrollPane scrollPaneGeneric;
	private JLabel pocetNeplanKus;
	private KapacitniPropocet dialog;
	private JButton btnPropoet;
	
	private String [] parametryZakazky;
	private JLabel lblTermnExpedice;
	private JLabel textLabelTerminExpedice;
	private JLabel lblPesnCena;
	private JLabel textLblPresnaCena;
	private JLabel lblCzk;
	private JList list;
	private JLabel labelSeznamDilcichTermin;
	
	
	@Override
	public void nastavOkno(int i, int j) {
		// TODO Auto-generated method stub
		
	}
	
	public ColorCellTable getTableFyzkusy() {
		return tableFyzkusy;
	}
	
	public void setZadatVycistenyKus(String [] parametryZakazky, ResultSet fyzKusyZakazky) throws Exception{
		this.dokonciZadavani.setActionCommand("DokonciZadavaniVycisteni");
		pocetNeplanKus.setText("Celkov\u00FD po\u010Det kusù bez datumu vyèištìní:");
		dokonciZadavani.setText("Ukonèi zadávání vyèištìných kusù");
		isPlanovaniLiti = false;
		nadpis.setText("Tabulka vyèištìno:");
		pridatDatum.setText("Zadej datum vyèištìní");
		pridatDatum.setActionCommand("ZadejVycistenyKus");
		setTabulky(parametryZakazky,  fyzKusyZakazky);
		this.parametryZakazky = parametryZakazky;
	}
	
	public void setPlanovani(String [] parametryZakazky, ResultSet fyzKusyZakazky, int idZakazky) throws Exception{
		this.dokonciZadavani.setActionCommand("DokoncitPlanovani");
		dokonciZadavani.setText("Ukon\u010Dit pl\u00E1nov\u00E1n\u00ED");
		pocetNeplanKus.setText("Celkov\u00FD po\u010Det nenapl\u00E1novan\u00FDch kus\u016F t\u00E9to zak\u00E1zky:");
		this.isPlanovaniLiti = true;
		pridatDatum.setActionCommand("PridatDatumnLiti");
		nadpis.setText("Pl\u00E1nov\u00E1n\u00ED lit\u00ED:");
		setTabulky(parametryZakazky,  fyzKusyZakazky);
		this.parametryZakazky = parametryZakazky;
		
		
		ResultSet rs = sklad.getSql().vyberDilciTerminy(idZakazky);
		DefaultListModel<DateStor> mod = createListModel(rs);
		list.setModel(mod);
	}
	
	/**
	 * 
	 * @param parametryZakazky
	 * @param fyzKusyZakazky
	 * @throws Exception
	 */
	public void setTabulky(String [] parametryZakazky, ResultSet fyzKusyZakazky) throws Exception{
		for(int i = 0; i < textLabels.length; i++){
			if(i==8){
				if(parametryZakazky[i].equalsIgnoreCase("Ano")){
					textIsCzk.setText("CZK");
				} else if(parametryZakazky[i].equalsIgnoreCase("Ne")){
					textIsCzk.setText("EUR");
				} else {
					JOptionPane.showMessageDialog(hlavniOkno, "Error setPlanovani planovani.class");
				}
				continue;
			}
			if(i == 9){
				if(parametryZakazky[i].equalsIgnoreCase("Ano")){
					lblCena.setText("Cena za kus:");
				} else if(parametryZakazky[i].equalsIgnoreCase("Ne")){
					lblCena.setText("Cena za kg:");
				} else {
					JOptionPane.showMessageDialog(hlavniOkno, "Error setPlanovani planovani.class");
				}
				continue;
			}
			textLabels[i].setText(parametryZakazky[i]);
		}
		//System.out.println("Parametry lenght "+parametryZakazky.length);
		textPoznamka.setText(parametryZakazky[21]);
		textPaganyrka.setText(parametryZakazky[22]);
		textLblPresnaCena.setText(parametryZakazky[23]);
		
		QueryTableModel model = new QueryTableModel(fyzKusyZakazky);
		this.tableFyzkusy.setModel(model); 
		
		
		Dimension s = scrollPane_1.getPreferredSize();  // prefered size se nastavi v ColorCellTable
		if(s.height > maxVyskaFyzKusyTabulky){s.height = maxVyskaFyzKusyTabulky;}
		// pres GridBagConstants
		GridBagLayout layout = ((GridBagLayout)this.getLayout());
		layout.rowHeights[radekZacinajiciTabulkaFyzkusy] = s.height;				
		// konec1
		//scrollPane_1.setPreferredSize(s);		
		tableFyzkusy.getColumAdjuster().adjustColumns();
		generujRozvrh(isPlanovaniLiti);
		
		
		
		int width = 100;
		for(int i = 0; i < tableFyzkusy.getColumnCount() - 1; i++){
			width += tableFyzkusy.getColumnModel().getColumn(i).getWidth();
		}
		s = this.hlavniOkno.getObalVedlejsihoOkna().getPreferredSize();
		s.width = width;
		s.height = 800 + tableGenericka.getRowCount()*tableGenericka.getRowHeight();
		this.hlavniOkno.getObalVedlejsihoOkna().setPreferredSize(s);
	}
	/**
	 * Metoda pro naplnìní pole textLabels odkazama a popisky tøídy JLabel a 
	 * posléze projde pole textLabels a popisLabels a upraví jejich font na new Font("Tahoma", Font.PLAIN, 14) nebo	
	 * na new Font("Tahoma", Font.BOLD, 15) podle toho jesti to je nadpis nebo jen popisek
	 */
	public void initLabels(){
		this.textLabels = new JLabel [21];
		textLabels[0] = textIdZakazky;
		textLabels[1] = textJmenoZakaznika;
		textLabels[2] = textCisloObjednavky;
		textLabels[3] = textCisloModelu;		
		textLabels[4] = textDatumPrijetiZakazky;
		textLabels[5] = textPocetKusu;
		textLabels[6] = textLabelTerminExpedice;
		textLabels[7] = textCena;
		textLabels[8] = textIsCzk;
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
		textLabels[20] = textIsUzavrena;
		
		for(int i = 0; i < textLabels.length; i++){
			textLabels[i].setFont(f);
		}
		textPoznamka.setFont(f);
		textPaganyrka.setFont(f);
		textLblPresnaCena.setFont(f);
		
		for(int i = 0; i < popisLabels.length; i++){
			popisLabels[i].setFont(f);
		}
		popisLabels[0].setFont(fb);	
		nadpis.setFont(fb);

	}
	

	/**
	 * Create the panel.
	 */
	public Planovani(MainFrame hlavniOkno) {
		setBackground(barvy[12]);
		setBorder(new LineBorder(barvy[6]));
		this.hlavniOkno = hlavniOkno;
		this.sklad = hlavniOkno.getSklad();
		this.popisLabels = new JLabel [23];
		this.sdf = new SimpleDateFormat("dd.MM.yyyy");
		
		dialog = new KapacitniPropocet(sklad);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{20, 109, 86, 20, 75, 0, 48, 0, 20, 90, 30, 40, 30, 20, 10, 88, 40, 55, 20, 123, 0, 20, 0};
		gridBagLayout.rowHeights = new int[]{15, 0, 0, 12, 0, 0, 0, 0, 0, 0, 25, 30, 30, 0, 100, 30, 25, 0, 20, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblParametryZakzky = new JLabel("Parametry zak\u00E1zky:");
		lblParametryZakzky.setFont(fb);
		popisLabels[0] = lblParametryZakzky;
		GridBagConstraints gbc_lblParametryZakzky = new GridBagConstraints();
		gbc_lblParametryZakzky.anchor = GridBagConstraints.WEST;
		gbc_lblParametryZakzky.gridwidth = 2;
		gbc_lblParametryZakzky.insets = new Insets(0, 0, 15, 5);
		gbc_lblParametryZakzky.gridx = 1;
		gbc_lblParametryZakzky.gridy = 1;
		add(lblParametryZakzky, gbc_lblParametryZakzky);
		
		JLabel lblIdZakaznika = new JLabel("Id z\u00E1kazn\u00EDka:");
		lblIdZakaznika.setFont(new Font("Tahoma", Font.BOLD, 11));
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
		gbc_lblZkaznik.insets = new Insets(0, 0, 5, 5);
		gbc_lblZkaznik.anchor = GridBagConstraints.WEST;
		gbc_lblZkaznik.gridx = 4;
		gbc_lblZkaznik.gridy = 2;
		add(lblZkaznik, gbc_lblZkaznik);
		
		textJmenoZakaznika = new JLabel("XXXXXXXXXXXXX");
		GridBagConstraints gbc_textJmenoZakaznika = new GridBagConstraints();
		gbc_textJmenoZakaznika.gridwidth = 3;
		gbc_textJmenoZakaznika.anchor = GridBagConstraints.WEST;
		gbc_textJmenoZakaznika.insets = new Insets(0, 0, 5, 5);
		gbc_textJmenoZakaznika.gridx = 5;
		gbc_textJmenoZakaznika.gridy = 2;
		add(textJmenoZakaznika, gbc_textJmenoZakaznika);
		
		lblJeTatoZakzka = new JLabel("Zak\u00E1zka uzav\u0159ena:");
		popisLabels[22] = lblJeTatoZakzka;
		GridBagConstraints gbc_lblJeTatoZakzka = new GridBagConstraints();
		gbc_lblJeTatoZakzka.gridwidth = 2;
		gbc_lblJeTatoZakzka.anchor = GridBagConstraints.WEST;
		gbc_lblJeTatoZakzka.insets = new Insets(0, 0, 5, 5);
		gbc_lblJeTatoZakzka.gridx = 9;
		gbc_lblJeTatoZakzka.gridy = 2;
		add(lblJeTatoZakzka, gbc_lblJeTatoZakzka);
		
		textIsUzavrena = new JLabel("ANO/NE");
		GridBagConstraints gbc_textIsUzavrena = new GridBagConstraints();
		gbc_textIsUzavrena.insets = new Insets(0, 0, 5, 5);
		gbc_textIsUzavrena.gridx = 11;
		gbc_textIsUzavrena.gridy = 2;
		add(textIsUzavrena, gbc_textIsUzavrena);
		
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
		gbc_lblIdModelu.gridx = 9;
		gbc_lblIdModelu.gridy = 4;
		add(lblIdModelu, gbc_lblIdModelu);
		
		textIdModelu = new JLabel("xxx");
		GridBagConstraints gbc_textIdModelu = new GridBagConstraints();
		gbc_textIdModelu.anchor = GridBagConstraints.WEST;
		gbc_textIdModelu.gridwidth = 3;
		gbc_textIdModelu.insets = new Insets(0, 0, 5, 5);
		gbc_textIdModelu.gridx = 10;
		gbc_textIdModelu.gridy = 4;
		add(textIdModelu, gbc_textIdModelu);
		
		labelSeznamDilcichTermin = new JLabel("Seznam d\u00EDl\u010D\u00EDch term\u00EDn\u016F");
		labelSeznamDilcichTermin.setFont(fb);
		GridBagConstraints gbc_labelSeznamDilcichTermin = new GridBagConstraints();
		gbc_labelSeznamDilcichTermin.insets = new Insets(0, 0, 5, 5);
		gbc_labelSeznamDilcichTermin.gridx = 19;
		gbc_labelSeznamDilcichTermin.gridy = 4;
		add(labelSeznamDilcichTermin, gbc_labelSeznamDilcichTermin);
		
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
		
		JLabel lblDatumPijetZakzky = new JLabel("Datum objedn\u00E1vky:");
		popisLabels[6] = lblDatumPijetZakzky;
		GridBagConstraints gbc_lblDatumPijetZakzky = new GridBagConstraints();
		gbc_lblDatumPijetZakzky.gridwidth = 2;
		gbc_lblDatumPijetZakzky.anchor = GridBagConstraints.WEST;
		gbc_lblDatumPijetZakzky.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatumPijetZakzky.gridx = 4;
		gbc_lblDatumPijetZakzky.gridy = 5;
		add(lblDatumPijetZakzky, gbc_lblDatumPijetZakzky);
		
		textDatumPrijetiZakazky = new JLabel("XXx");
		GridBagConstraints gbc_textDatumPrijetiZakazky = new GridBagConstraints();
		gbc_textDatumPrijetiZakazky.gridwidth = 2;
		gbc_textDatumPrijetiZakazky.anchor = GridBagConstraints.WEST;
		gbc_textDatumPrijetiZakazky.insets = new Insets(0, 0, 5, 5);
		gbc_textDatumPrijetiZakazky.gridx = 6;
		gbc_textDatumPrijetiZakazky.gridy = 5;
		add(textDatumPrijetiZakazky, gbc_textDatumPrijetiZakazky);
		
		JLabel lblCisloModelu = new JLabel("\u010C\u00EDslo modelu:");
		popisLabels[7] = lblCisloModelu;
		GridBagConstraints gbc_lblCisloModelu = new GridBagConstraints();
		gbc_lblCisloModelu.anchor = GridBagConstraints.WEST;
		gbc_lblCisloModelu.insets = new Insets(0, 0, 5, 5);
		gbc_lblCisloModelu.gridx = 9;
		gbc_lblCisloModelu.gridy = 5;
		add(lblCisloModelu, gbc_lblCisloModelu);
		
		textCisloModelu = new JLabel("XXXXXXXXXXXXX");
		GridBagConstraints gbc_textCisloModelu = new GridBagConstraints();
		gbc_textCisloModelu.anchor = GridBagConstraints.WEST;
		gbc_textCisloModelu.gridwidth = 3;
		gbc_textCisloModelu.insets = new Insets(0, 0, 5, 5);
		gbc_textCisloModelu.gridx = 10;
		gbc_textCisloModelu.gridy = 5;
		add(textCisloModelu, gbc_textCisloModelu);
		
		JLabel lblNazevModelu = new JLabel("N\u00E1zev modelu:");
		popisLabels[8] = lblNazevModelu;
		GridBagConstraints gbc_lblNazevModelu = new GridBagConstraints();
		gbc_lblNazevModelu.gridwidth = 3;
		gbc_lblNazevModelu.anchor = GridBagConstraints.WEST;
		gbc_lblNazevModelu.insets = new Insets(0, 0, 5, 5);
		gbc_lblNazevModelu.gridx = 14;
		gbc_lblNazevModelu.gridy = 5;
		add(lblNazevModelu, gbc_lblNazevModelu);
		
		textJmenoModelu = new JLabel("XXXXXXXXXXXXX");
		GridBagConstraints gbc_textJmenoModelu = new GridBagConstraints();
		gbc_textJmenoModelu.anchor = GridBagConstraints.WEST;
		gbc_textJmenoModelu.insets = new Insets(0, 0, 5, 5);
		gbc_textJmenoModelu.gridx = 17;
		gbc_textJmenoModelu.gridy = 5;
		add(textJmenoModelu, gbc_textJmenoModelu);
		
		list = new JList<DateStor>();
		list.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		list.setFont(f);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridheight = 5;
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 19;
		gbc_list.gridy = 5;
		add(list, gbc_list);
		
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
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 4;
		gbc_lblNewLabel.gridy = 6;
		add(lblNewLabel, gbc_lblNewLabel);
		
		textPocetKusu = new JLabel("XXX");
		GridBagConstraints gbc_textPocetKusu = new GridBagConstraints();
		gbc_textPocetKusu.gridwidth = 2;
		gbc_textPocetKusu.anchor = GridBagConstraints.WEST;
		gbc_textPocetKusu.insets = new Insets(0, 0, 5, 5);
		gbc_textPocetKusu.gridx = 6;
		gbc_textPocetKusu.gridy = 6;
		add(textPocetKusu, gbc_textPocetKusu);
		
		JLabel lblMaterial = new JLabel("Materi\u00E1l:");
		popisLabels[11] = lblMaterial;
		GridBagConstraints gbc_lblMaterial = new GridBagConstraints();
		gbc_lblMaterial.anchor = GridBagConstraints.WEST;
		gbc_lblMaterial.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaterial.gridx = 9;
		gbc_lblMaterial.gridy = 6;
		add(lblMaterial, gbc_lblMaterial);
		
		textMaterial = new JLabel("XXXXXXX");
		GridBagConstraints gbc_textMaterial = new GridBagConstraints();
		gbc_textMaterial.anchor = GridBagConstraints.WEST;
		gbc_textMaterial.gridwidth = 3;
		gbc_textMaterial.insets = new Insets(0, 0, 5, 5);
		gbc_textMaterial.gridx = 10;
		gbc_textMaterial.gridy = 6;
		add(textMaterial, gbc_textMaterial);
		
		JLabel lblMaterialVlastn = new JLabel("Material vlastn\u00ED:");
		popisLabels[12] = lblMaterialVlastn;
		GridBagConstraints gbc_lblMaterialVlastn = new GridBagConstraints();
		gbc_lblMaterialVlastn.gridwidth = 3;
		gbc_lblMaterialVlastn.anchor = GridBagConstraints.WEST;
		gbc_lblMaterialVlastn.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaterialVlastn.gridx = 14;
		gbc_lblMaterialVlastn.gridy = 6;
		add(lblMaterialVlastn, gbc_lblMaterialVlastn);
		
		textMaterialVlastni = new JLabel("XXXXXXX");
		GridBagConstraints gbc_textMaterialVlastni = new GridBagConstraints();
		gbc_textMaterialVlastni.anchor = GridBagConstraints.WEST;
		gbc_textMaterialVlastni.insets = new Insets(0, 0, 5, 5);
		gbc_textMaterialVlastni.gridx = 17;
		gbc_textMaterialVlastni.gridy = 6;
		add(textMaterialVlastni, gbc_textMaterialVlastni);
		
		lblCena = new JLabel("Cena:");
		GridBagConstraints gbc_lblCena = new GridBagConstraints();
		gbc_lblCena.anchor = GridBagConstraints.WEST;
		gbc_lblCena.insets = new Insets(0, 0, 5, 5);
		gbc_lblCena.gridx = 1;
		gbc_lblCena.gridy = 7;
		add(lblCena, gbc_lblCena);
		
		textCena = new JLabel("xxxx");
		GridBagConstraints gbc_textCena = new GridBagConstraints();
		gbc_textCena.anchor = GridBagConstraints.WEST;
		gbc_textCena.insets = new Insets(0, 0, 5, 5);
		gbc_textCena.gridx = 2;
		gbc_textCena.gridy = 7;
		add(textCena, gbc_textCena);
		
		textIsCzk = new JLabel("CZK");
		GridBagConstraints gbc_textIsCzk = new GridBagConstraints();
		gbc_textIsCzk.anchor = GridBagConstraints.WEST;
		gbc_textIsCzk.insets = new Insets(0, 0, 5, 5);
		gbc_textIsCzk.gridx = 3;
		gbc_textIsCzk.gridy = 7;
		add(textIsCzk, gbc_textIsCzk);
		
		lblKurz = new JLabel("Kurz CZK/EU:");
		GridBagConstraints gbc_lblKurz = new GridBagConstraints();
		gbc_lblKurz.gridwidth = 2;
		gbc_lblKurz.anchor = GridBagConstraints.WEST;
		gbc_lblKurz.insets = new Insets(0, 0, 5, 5);
		gbc_lblKurz.gridx = 4;
		gbc_lblKurz.gridy = 7;
		add(lblKurz, gbc_lblKurz);
		
		textKurz = new JLabel("xxx");
		GridBagConstraints gbc_textKurz = new GridBagConstraints();
		gbc_textKurz.gridwidth = 2;
		gbc_textKurz.anchor = GridBagConstraints.WEST;
		gbc_textKurz.insets = new Insets(0, 0, 5, 5);
		gbc_textKurz.gridx = 6;
		gbc_textKurz.gridy = 7;
		add(textKurz, gbc_textKurz);
		
		lblTermnExpedice = new JLabel("Term\u00EDn expedice:");
		popisLabels[20] = lblTermnExpedice;
		GridBagConstraints gbc_lblTermnExpedice = new GridBagConstraints();
		gbc_lblTermnExpedice.anchor = GridBagConstraints.WEST;
		gbc_lblTermnExpedice.insets = new Insets(0, 0, 5, 5);
		gbc_lblTermnExpedice.gridx = 1;
		gbc_lblTermnExpedice.gridy = 8;
		add(lblTermnExpedice, gbc_lblTermnExpedice);
		
		textLabelTerminExpedice = new JLabel("xxx");
		GridBagConstraints gbc_textLabelTerminExpedice = new GridBagConstraints();
		gbc_textLabelTerminExpedice.anchor = GridBagConstraints.WEST;
		gbc_textLabelTerminExpedice.insets = new Insets(0, 0, 5, 5);
		gbc_textLabelTerminExpedice.gridx = 2;
		gbc_textLabelTerminExpedice.gridy = 8;
		add(textLabelTerminExpedice, gbc_textLabelTerminExpedice);
		
		lblPesnCena = new JLabel("P\u0159esn\u00E1 cena:");
		popisLabels[21] = lblPesnCena;
		GridBagConstraints gbc_lblPesnCena = new GridBagConstraints();
		gbc_lblPesnCena.anchor = GridBagConstraints.WEST;
		gbc_lblPesnCena.gridwidth = 2;
		gbc_lblPesnCena.insets = new Insets(0, 0, 5, 5);
		gbc_lblPesnCena.gridx = 4;
		gbc_lblPesnCena.gridy = 8;
		add(lblPesnCena, gbc_lblPesnCena);
		
		textLblPresnaCena = new JLabel("xxx");
		GridBagConstraints gbc_textLblPresnaCena = new GridBagConstraints();
		gbc_textLblPresnaCena.anchor = GridBagConstraints.WEST;
		gbc_textLblPresnaCena.insets = new Insets(0, 0, 5, 5);
		gbc_textLblPresnaCena.gridx = 6;
		gbc_textLblPresnaCena.gridy = 8;
		add(textLblPresnaCena, gbc_textLblPresnaCena);
		
		lblCzk = new JLabel("CZK");
		GridBagConstraints gbc_lblCzk = new GridBagConstraints();
		gbc_lblCzk.insets = new Insets(0, 0, 5, 5);
		gbc_lblCzk.gridx = 7;
		gbc_lblCzk.gridy = 8;
		add(lblCzk, gbc_lblCzk);
		
		JLabel lblFotmovna = new JLabel("Formovna:");
		popisLabels[14] = lblFotmovna;
		GridBagConstraints gbc_lblFotmovna = new GridBagConstraints();
		gbc_lblFotmovna.anchor = GridBagConstraints.WEST;
		gbc_lblFotmovna.insets = new Insets(0, 0, 5, 5);
		gbc_lblFotmovna.gridx = 9;
		gbc_lblFotmovna.gridy = 8;
		add(lblFotmovna, gbc_lblFotmovna);
		
		textFormovna = new JLabel("XXXXXX");
		GridBagConstraints gbc_textFormovna = new GridBagConstraints();
		gbc_textFormovna.anchor = GridBagConstraints.WEST;
		gbc_textFormovna.gridwidth = 3;
		gbc_textFormovna.insets = new Insets(0, 0, 5, 5);
		gbc_textFormovna.gridx = 10;
		gbc_textFormovna.gridy = 8;
		add(textFormovna, gbc_textFormovna);
		
		JLabel lblHmotnost = new JLabel("Hmotnost:");
		popisLabels[15] = lblHmotnost;
		GridBagConstraints gbc_lblHmotnost = new GridBagConstraints();
		gbc_lblHmotnost.gridwidth = 3;
		gbc_lblHmotnost.anchor = GridBagConstraints.WEST;
		gbc_lblHmotnost.insets = new Insets(0, 0, 5, 5);
		gbc_lblHmotnost.gridx = 14;
		gbc_lblHmotnost.gridy = 8;
		add(lblHmotnost, gbc_lblHmotnost);
		
		textHmotnost = new JLabel("XXXXX");
		GridBagConstraints gbc_textHmotnost = new GridBagConstraints();
		gbc_textHmotnost.anchor = GridBagConstraints.WEST;
		gbc_textHmotnost.insets = new Insets(0, 0, 5, 5);
		gbc_textHmotnost.gridx = 17;
		gbc_textHmotnost.gridy = 8;
		add(textHmotnost, gbc_textHmotnost);
		
		JLabel lblPoznamka = new JLabel("Pozn\u00E1mka k zak\u00E1zce:");
		popisLabels[13] = lblPoznamka;
		GridBagConstraints gbc_lblPoznamka = new GridBagConstraints();
		gbc_lblPoznamka.anchor = GridBagConstraints.WEST;
		gbc_lblPoznamka.insets = new Insets(0, 0, 5, 5);
		gbc_lblPoznamka.gridx = 1;
		gbc_lblPoznamka.gridy = 9;
		add(lblPoznamka, gbc_lblPoznamka);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setWheelScrollingEnabled(false); 
		scrollPane.addMouseWheelListener(new MouseWheelListener() {
		    @Override
		    public void mouseWheelMoved(MouseWheelEvent e) {
		    	scrollPane.getParent().dispatchEvent(e);
		    }
		});
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 5;
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
		popisLabels[16] = lblNorma;
		GridBagConstraints gbc_lblNorma = new GridBagConstraints();
		gbc_lblNorma.anchor = GridBagConstraints.WEST;
		gbc_lblNorma.insets = new Insets(0, 0, 5, 5);
		gbc_lblNorma.gridx = 9;
		gbc_lblNorma.gridy = 9;
		add(lblNorma, gbc_lblNorma);
		
		textNorma = new JLabel("New label");
		GridBagConstraints gbc_textNorma = new GridBagConstraints();
		gbc_textNorma.anchor = GridBagConstraints.WEST;
		gbc_textNorma.gridwidth = 3;
		gbc_textNorma.insets = new Insets(0, 0, 5, 5);
		gbc_textNorma.gridx = 10;
		gbc_textNorma.gridy = 9;
		add(textNorma, gbc_textNorma);
		
		JLabel lblOdhadovan = new JLabel("Odhadovan\u00E1 hmotnost:");
		popisLabels[17] = lblOdhadovan;
		GridBagConstraints gbc_lblOdhadovan = new GridBagConstraints();
		gbc_lblOdhadovan.gridwidth = 3;
		gbc_lblOdhadovan.anchor = GridBagConstraints.WEST;
		gbc_lblOdhadovan.insets = new Insets(0, 0, 5, 5);
		gbc_lblOdhadovan.gridx = 14;
		gbc_lblOdhadovan.gridy = 9;
		add(lblOdhadovan, gbc_lblOdhadovan);
		
		textIsOdhadLabel = new JLabel("ANO/NE");
		GridBagConstraints gbc_textIsOdhadLabel = new GridBagConstraints();
		gbc_textIsOdhadLabel.anchor = GridBagConstraints.WEST;
		gbc_textIsOdhadLabel.insets = new Insets(0, 0, 5, 5);
		gbc_textIsOdhadLabel.gridx = 17;
		gbc_textIsOdhadLabel.gridy = 9;
		add(textIsOdhadLabel, gbc_textIsOdhadLabel);
		
		JLabel lblSeznamKus = new JLabel("Seznam kus\u016F:");
		popisLabels[18] = lblSeznamKus;
		GridBagConstraints gbc_lblSeznamKus = new GridBagConstraints();
		gbc_lblSeznamKus.anchor = GridBagConstraints.WEST;
		gbc_lblSeznamKus.insets = new Insets(0, 0, 5, 5);
		gbc_lblSeznamKus.gridx = 1;
		gbc_lblSeznamKus.gridy = 12;
		add(lblSeznamKus, gbc_lblSeznamKus);
		
		JButton btnSkrt = new MyJButton("Aktualizovat", 16, 1, sklad);
		btnSkrt.addActionListener(this);
		btnSkrt.setActionCommand("updateComand");
		btnSkrt.setPreferredSize(new Dimension(140, 25));
		GridBagConstraints gbc_btnSkrt = new GridBagConstraints();
		gbc_btnSkrt.gridwidth = 2;
		gbc_btnSkrt.fill = GridBagConstraints.BOTH;
		gbc_btnSkrt.insets = new Insets(0, 0, 5, 5);
		gbc_btnSkrt.gridx = 4;
		gbc_btnSkrt.gridy = 12;
		add(btnSkrt, gbc_btnSkrt);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setMaximumSize(new Dimension(32767, 200));
		scrollPane_1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		scrollPane_1.setWheelScrollingEnabled(false); 
		scrollPane_1.addMouseWheelListener(new MouseWheelListener() {
		    @Override
		    public void mouseWheelMoved(MouseWheelEvent e) {
		    	scrollPane_1.getParent().dispatchEvent(e);
		    }
		});
		
		pridatDatumLiti = new MyJDateChooser();
		GridBagConstraints gbc_dateChooser = new GridBagConstraints();
		gbc_dateChooser.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooser.fill = GridBagConstraints.BOTH;
		gbc_dateChooser.gridx = 9;
		gbc_dateChooser.gridy = 12;
		add(pridatDatumLiti, gbc_dateChooser);
		
		pridatDatum = new MyJButton("P\u0159idat datum pl\u00E1nov\u00E1n\u00ED",16,1, sklad);
		pridatDatum.addActionListener(this);
		pridatDatum.setPreferredSize(new Dimension(130, 25));
		GridBagConstraints gbc_pridatDatum = new GridBagConstraints();
		gbc_pridatDatum.gridwidth = 5;
		gbc_pridatDatum.fill = GridBagConstraints.BOTH;
		gbc_pridatDatum.insets = new Insets(0, 0, 5, 5);
		gbc_pridatDatum.gridx = 11;
		gbc_pridatDatum.gridy = 12;
		add(pridatDatum, gbc_pridatDatum);
		
		JLabel lblOznaenoKus = new JLabel("Ozna\u010Deno kus\u016F:");
		GridBagConstraints gbc_lblOznaenoKus = new GridBagConstraints();
		gbc_lblOznaenoKus.anchor = GridBagConstraints.WEST;
		gbc_lblOznaenoKus.insets = new Insets(0, 0, 5, 5);
		gbc_lblOznaenoKus.gridx = 1;
		gbc_lblOznaenoKus.gridy = 13;
		add(lblOznaenoKus, gbc_lblOznaenoKus);
		
		textLabelOznaceno = new JLabel("0");
		GridBagConstraints gbc_textLabelOznaceno = new GridBagConstraints();
		gbc_textLabelOznaceno.insets = new Insets(0, 0, 5, 5);
		gbc_textLabelOznaceno.gridx = 2;
		gbc_textLabelOznaceno.gridy = 13;
		add(textLabelOznaceno, gbc_textLabelOznaceno);
		
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.gridwidth = 20;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 14;
		add(scrollPane_1, gbc_scrollPane_1);
		
		tableFyzkusy = new ColorCellTable(sklad.getPrazdneTabulky()[3], scrollPane_1, false, sklad);
		tableFyzkusy.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableFyzkusy.getSelectionModel().addListSelectionListener(this);
		
		
		scrollPane_1.setViewportView(tableFyzkusy);
		
		scrollPaneGeneric = new JScrollPane();
		scrollPaneGeneric.setWheelScrollingEnabled(false); 
		scrollPaneGeneric.addMouseWheelListener(new MouseWheelListener() {
		    @Override
		    public void mouseWheelMoved(MouseWheelEvent e) {
		    	scrollPaneGeneric.getParent().dispatchEvent(e);
		    }
		});
		
		dokonciZadavani = new MyJButton("Ukon\u010Dit pl\u00E1nov\u00E1n\u00ED", 16, 1, sklad);
		dokonciZadavani.setActionCommand("DokoncitPlanovani");
		dokonciZadavani.addActionListener(this);
		GridBagConstraints gbc_dokonciZadavani = new GridBagConstraints();
		gbc_dokonciZadavani.insets = new Insets(0, 0, 5, 5);
		gbc_dokonciZadavani.gridwidth = 2;
		gbc_dokonciZadavani.fill = GridBagConstraints.BOTH;
		gbc_dokonciZadavani.gridx = 1;
		gbc_dokonciZadavani.gridy = 15;
		add(dokonciZadavani, gbc_dokonciZadavani);
		
		btnPropoet = new MyJButton("Kapacitn\u00ED propo\u010Det t\u00FDdne (F1)", 16, 1, sklad);
		btnPropoet.setActionCommand("KapacitPropocet");
		btnPropoet.addActionListener(this);
		GridBagConstraints gbc_btnPropoet = new GridBagConstraints();
		gbc_btnPropoet.fill = GridBagConstraints.BOTH;
		gbc_btnPropoet.gridwidth = 5;
		gbc_btnPropoet.insets = new Insets(0, 0, 5, 5);
		gbc_btnPropoet.gridx = 6;
		gbc_btnPropoet.gridy = 15;
		add(btnPropoet, gbc_btnPropoet);
		
		nadpis = new JLabel("Pl\u00E1nov\u00E1n\u00ED lit\u00ED:");
		popisLabels[19] = nadpis;
		GridBagConstraints gbc_nadpis = new GridBagConstraints();
		gbc_nadpis.gridwidth = 2;
		gbc_nadpis.anchor = GridBagConstraints.WEST;
		gbc_nadpis.insets = new Insets(0, 0, 5, 5);
		gbc_nadpis.gridx = 1;
		gbc_nadpis.gridy = 16;
		add(nadpis, gbc_nadpis);
		
		pocetNeplanKus = new JLabel("Celkov\u00FD po\u010Det nenapl\u00E1novan\u00FDch kus\u016F t\u00E9to zak\u00E1zky:");
		GridBagConstraints gbc_pocetNeplanKus = new GridBagConstraints();
		gbc_pocetNeplanKus.anchor = GridBagConstraints.WEST;
		gbc_pocetNeplanKus.gridwidth = 6;
		gbc_pocetNeplanKus.insets = new Insets(0, 0, 5, 5);
		gbc_pocetNeplanKus.gridx = 3;
		gbc_pocetNeplanKus.gridy = 16;
		add(pocetNeplanKus, gbc_pocetNeplanKus);
		
		pocetNeplanovanychKusu = new JLabel("XXX");
		GridBagConstraints gbc_pocetNeplanovanychKusu = new GridBagConstraints();
		gbc_pocetNeplanovanychKusu.anchor = GridBagConstraints.WEST;
		gbc_pocetNeplanovanychKusu.insets = new Insets(0, 0, 5, 5);
		gbc_pocetNeplanovanychKusu.gridx = 9;
		gbc_pocetNeplanovanychKusu.gridy = 16;
		add(pocetNeplanovanychKusu, gbc_pocetNeplanovanychKusu);
		GridBagConstraints gbc_scrollPaneGeneric = new GridBagConstraints();
		gbc_scrollPaneGeneric.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneGeneric.gridwidth = 20;
		gbc_scrollPaneGeneric.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneGeneric.gridx = 1;
		gbc_scrollPaneGeneric.gridy = 17;
		add(scrollPaneGeneric, gbc_scrollPaneGeneric);
		
		
		tableGenericka = new ColorCellTable(sklad.getPrazdneTabulky()[4], scrollPaneGeneric, true, sklad);	
		tableGenericka.addKeyListener(this);
		tableGenericka.getColumnModel().getColumn(3).setMinWidth(60);
		
		tableGenericka.getColumnModel().getColumn(5).setMinWidth(60);
		
		tableGenericka.getColumnModel().getColumn(7).setMinWidth(60);
		
		tableGenericka.getColumnModel().getColumn(9).setMinWidth(60);
		
		tableGenericka.getColumnModel().getColumn(11).setMinWidth(60);
		scrollPaneGeneric.setViewportView(tableGenericka);
		
		initLabels();

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			String com = e.getActionCommand();
			int i;
			if (com.equalsIgnoreCase("updateComand")) {
				ResultSet fyzKusy = sklad.getSql().vyberFyzKusy(Integer.parseInt(textIdZakazky.getText())).rs;
				this.setTabulky(parametryZakazky, fyzKusy);
				
			} else if(com.equalsIgnoreCase("KapacitPropocet")){
				i = this.tableGenericka.getSelectedRow();
				if(i == -1){
					JOptionPane.showMessageDialog(hlavniOkno,"Vyberte nìjaký øádek ve spodní tabulce");
					return;
				}
				dialog.setVisible(true);
				String cisloTydne = (String) tableGenericka.getValueAt(i, 1);
				String rok = (String) tableGenericka.getValueAt(i, 12);
				char f = this.textFormovna.getText().charAt(0);
				dialog.setTable(Integer.parseInt(cisloTydne), Integer.parseInt(rok), f);
				
			} else if (com.equalsIgnoreCase("PridatDatumnLiti")) {
				if (tableFyzkusy.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(hlavniOkno,"Vyberte nìjaky odlitek");
					return;
				}
				this.pridatDatumLiti(1);
				tableFyzkusy.getColumAdjuster().adjustColumns();

			} /*else if(com.equalsIgnoreCase("ZadejVycistenyKus")){
				if (tableFyzkusy.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(hlavniOkno, "Vyberte nìjaky odlitek");
					return;
				}
				this.pridatDatumLiti(9);
				tableFyzkusy.getColumAdjuster().adjustColumn(1);

			} */ /*else if(com.equalsIgnoreCase("DokonciZadavaniVycisteni")){
				boolean[][] upraveno = tableFyzkusy.getZmeneno();
				SQLStor sql = sklad.getSql();
				int x = 9, y = 6;
				for (int m = 0; m < upraveno.length; m++) {
					if (upraveno[m][x] || upraveno[m][y]) {
						String datumLitiStr = (String) tableFyzkusy.getValueAt(m, x);
						String dilciTerminStr = (String) tableFyzkusy.getValueAt(m, y);
						Date datumLiti = null;
						Date dilciTermin = null;
						if(datumLitiStr != null){
							if(!datumLitiStr.equalsIgnoreCase("")){
								datumLiti = sdf.parse((String) tableFyzkusy.getValueAt(m, x));
							}
						}
						if(dilciTerminStr != null){
							if(!dilciTerminStr.equalsIgnoreCase("")){
								dilciTermin = sdf.parse((String) tableFyzkusy.getValueAt(m, y));
							}
						}					
						sql.zadejDatumVycistenyKusDilciTermin(
								Integer.parseInt((String) tableFyzkusy.getValueAt(m, 0)),
								datumLiti, 
								dilciTermin);
					}
				}
				JOptionPane.showMessageDialog(hlavniOkno, "Zadávání vyèištìných kusù úspìšnì dokonèeno");
				
				
			} */ else if (com.equalsIgnoreCase("DokoncitPlanovani")) {
				boolean[][] upraveno = tableFyzkusy.getZmeneno();
				SQLStor sql = sklad.getSql();
				for (int m = 0; m < upraveno.length; m++) {
					if (upraveno[m][1] || upraveno[m][6]) {
						String datumLitiStr = (String) tableFyzkusy.getValueAt(m, 1);
						Date datumLiti = null;
						if(datumLitiStr != null){
							if(!datumLitiStr.equalsIgnoreCase("")){
								datumLiti = sdf.parse((String) tableFyzkusy.getValueAt(m, 1));
							}
						}
						sql.zadejPlanovanyDatumLiti(
								Integer.parseInt((String) tableFyzkusy.getValueAt(m, 0)),
								datumLiti);
					}
				}
				JOptionPane.showMessageDialog(hlavniOkno, "Plánování úspìšnì dokonèeno");
			}
		} catch (Exception e1) {
			if(e1.getLocalizedMessage() != null){
				if(e1.getLocalizedMessage().startsWith(acesDenied)){
					JOptionPane.showMessageDialog(hlavniOkno, "Na tuto operaci nemáte pravomoce");
				} else {
					ExceptionWin.showExceptionMessage(e1);
					e1.printStackTrace();
				}
			} else {
				ExceptionWin.showExceptionMessage(e1);
				e1.printStackTrace();
			}
		}
	}
	
	private void pridatDatumLiti(int indexSloupce) throws ParseException {
		int i = tableFyzkusy.getSelectedRow();
		int[] poleRadku = tableFyzkusy.getSelectedRows();
		for (int k = 0; k < poleRadku.length; k++) {
			i = poleRadku[k];

			// sloupec 1
			Date datum = pridatDatumLiti.getDate();
			Calendar pridavanyDen = Calendar.getInstance();
			Calendar prvniTyden = Calendar.getInstance();
			Calendar posledniTyden;
			pridavanyDen.setTime(datum);
			// System.out.println("Pridavany den "+ sdf.format(datum));
			// System.out.println(sdf.format(pridavanyDen.getTime()));
			
			
			
			if (pridavanyDen.get(Calendar.DAY_OF_WEEK) == 1	|| pridavanyDen.get(Calendar.DAY_OF_WEEK) == 7) {
				JOptionPane.showMessageDialog(hlavniOkno, "O víkendu se nelije");
				return;
			}
			if (indexSloupce == 9) {
				if(tableFyzkusy.getModel().getValueAt(i, 1) == null){
					JOptionPane.showMessageDialog(hlavniOkno, "Nejdøíve zadejte datum lití v sekci plánování");
					return;
				}else if(((String)tableFyzkusy.getModel().getValueAt(i, 1)).equalsIgnoreCase("")){
					JOptionPane.showMessageDialog(hlavniOkno, "Nejdøíve zadejte datum lití v sekci plánování");
					return;
				}
				tableFyzkusy.getModel().setValueAt("Ano", i, 3);
				tableFyzkusy.getModel().setValueAt("Ano", i, 4);
			}
			String puvodniHodnota = (String) tableFyzkusy.getModel().getValueAt(i, indexSloupce);
			String novaHodnota = sdf.format(datum);

			if (novaHodnota.equalsIgnoreCase(puvodniHodnota)) {
				continue;
			}
			if (!novaHodnota.equalsIgnoreCase(puvodniHodnota) && tableGenericka.getRowCount() > 0) {
				prvniTyden.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
				prvniTyden.set(Calendar.YEAR, Integer.parseInt((String) tableGenericka.getValueAt(0, 12)));
				prvniTyden.set(Calendar.WEEK_OF_YEAR, Integer.parseInt((String) tableGenericka.getValueAt(0, 1)));
				prvniTyden.set(Calendar.DAY_OF_WEEK, 2);
				
				LocalDateTime pridavanyDatum = datum.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				Instant instant = Instant.ofEpochMilli(prvniTyden.getTimeInMillis());
				LocalDateTime prvniPlanTyden = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				Duration delka = Duration.between(prvniPlanTyden, pridavanyDatum);
				if (Math.abs(delka.toDays()) > 365) {
					JOptionPane.showMessageDialog(hlavniOkno, "Plánujete na déle než rok, nelze");
					return;
				}
				
				
				if (prvniTyden.after(pridavanyDen) && prvniTyden.get(Calendar.WEEK_OF_YEAR) != pridavanyDen.get(Calendar.WEEK_OF_YEAR)) {
					int reply = JOptionPane.showConfirmDialog(hlavniOkno, "Neplánujete postupnì den po dni za sebou, ale pøeskakujete. Opravdu to zamýšlíte?","Otázka", JOptionPane.YES_NO_OPTION);
					if (reply == 1){
						return;
					}
					JOptionPane.showMessageDialog(hlavniOkno, "Upozornìní, jelikož plánujete zpìtnì, nebudou barvy v dolní tabulce nejspíše souhlasit, ošetøení této chyby by bylo velmi pamìtovì nároèné, takže pokud chcete mít hezké barvièky zvolte si pevný zaèátek :)");
				}
				if (puvodniHodnota != null) { // tento odstavec slouží ke snižení hodnoty v genericke tabulce
					try {
						Date s = sdf.parse(puvodniHodnota);
						Calendar ss = Calendar.getInstance();
						ss.setTime(s);
						int index = 0;
						Calendar pomTyden = (Calendar) prvniTyden.clone();
						pomTyden.set(Calendar.DAY_OF_WEEK, 2);
						while (ss.after(pomTyden)
								&& pomTyden.get(Calendar.WEEK_OF_YEAR) != ss.get(Calendar.WEEK_OF_YEAR)) {
							index++;
							pomTyden.add(Calendar.WEEK_OF_YEAR, 1);
						}
						int puvHod = Integer.parseInt((String) tableGenericka.getValueAt(index,ss.get(Calendar.DAY_OF_WEEK) * 2 - 1));
						puvHod--;
						tableGenericka.getModel().setValueAt(Integer.toString(puvHod), index, ss.get(Calendar.DAY_OF_WEEK) * 2 - 1);
					} catch (ParseException e) {
						ExceptionWin.showExceptionMessage(e);
					}
				}

				posledniTyden = (Calendar) prvniTyden.clone();
				posledniTyden.set(Calendar.DAY_OF_WEEK, 2);
				posledniTyden.add(Calendar.WEEK_OF_YEAR, tableGenericka.getRowCount() - 1);

				if (prvniTyden.after(pridavanyDen) && prvniTyden.get(Calendar.WEEK_OF_YEAR) != pridavanyDen.get(Calendar.WEEK_OF_YEAR)) {

					// zde upravim boolean zmen
					int pocetPridanychTydnu = 0;
					Calendar pomPoslTyden = (Calendar) posledniTyden.clone();
					pomPoslTyden.set(Calendar.DAY_OF_WEEK, 2);
					while (pomPoslTyden.after(pridavanyDen)	&& tableGenericka.getRowCount() < 53) {
						pomPoslTyden.add(Calendar.WEEK_OF_YEAR, -1);
						pocetPridanychTydnu++;
					}
					boolean[][] staryZmen = tableGenericka.getZmeneno();
					boolean[][] novyZmen = new boolean[staryZmen.length
							+ pocetPridanychTydnu][staryZmen[0].length];
					for (int in = novyZmen.length - 1; in >= 0; in--) {
						for (int j = 0; j < novyZmen[in].length; j++) {
							if (in - novyZmen.length + staryZmen.length >= 0) {
								novyZmen[in][j] = staryZmen[in
										- novyZmen.length + staryZmen.length][j];
							} else {
								novyZmen[in][j] = false;
							}
						}
					}
					tableGenericka.setZmeneno(novyZmen);

					while (prvniTyden.after(pridavanyDen) && tableGenericka.getRowCount() < 53) {
						prvniTyden.add(Calendar.WEEK_OF_YEAR, -1);
						String[] data = new String[tableGenericka.getColumnCount()];
						data[0] = nazevMesice(prvniTyden.get(Calendar.MONTH));
						data[1] = Integer.toString(prvniTyden.get(Calendar.WEEK_OF_YEAR));
						prvniTyden.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
						data[12] = Integer.toString(prvniTyden.get(Calendar.YEAR));
						prvniTyden.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
						for (int j = 2; j < 7; j++) { // tyden planujeme na 5 dnu nedìle == 1, planujeme od pondìlí do patku
							data[2 * (j - 1)] = Integer.toString(prvniTyden.get(Calendar.DAY_OF_MONTH));
							prvniTyden.add(Calendar.DAY_OF_WEEK, 1);
						}
						tableGenericka.addRow(0, data, novyZmen);
						Dimension s = this.hlavniOkno.getObalVedlejsihoOkna().getPreferredSize();
						s.height += tableGenericka.getRowHeight();
						this.hlavniOkno.getObalVedlejsihoOkna().setPreferredSize(s);
						prvniTyden.set(Calendar.DAY_OF_WEEK, 2);
					}

				} else if (posledniTyden.before(pridavanyDen) && posledniTyden.get(Calendar.WEEK_OF_YEAR) != pridavanyDen.get(Calendar.WEEK_OF_YEAR)) {
					// zde upravim boolean zmen
					int pocetPridanychTydnu = 0;
					Calendar pomPoslTyden = (Calendar) posledniTyden.clone();
					pomPoslTyden.set(Calendar.DAY_OF_WEEK, 2);
					while (pomPoslTyden.before(pridavanyDen) && tableGenericka.getRowCount() < 53) {
						pocetPridanychTydnu++;
						pomPoslTyden.add(Calendar.WEEK_OF_YEAR, 1);
					}

					boolean[][] staryZmen = tableGenericka.getZmeneno();
					boolean[][] novyZmen = new boolean[staryZmen.length
							+ pocetPridanychTydnu][staryZmen[0].length];
					for (int in = 0; in < novyZmen.length; in++) {
						for (int j = 0; j < novyZmen[in].length; j++) {
							if (in < staryZmen.length) {
								novyZmen[in][j] = staryZmen[in][j];
							} else {
								novyZmen[in][j] = false;
							}
						}
					}
					tableGenericka.setZmeneno(novyZmen);

					while (posledniTyden.before(pridavanyDen) && tableGenericka.getRowCount() < 53) {
						posledniTyden.add(Calendar.WEEK_OF_YEAR, 1);
						posledniTyden.set(Calendar.DAY_OF_WEEK, 2);
						String[] data = new String[tableGenericka
								.getColumnCount()];
						data[0] = nazevMesice(posledniTyden.get(Calendar.MONTH));
						data[1] = Integer.toString(posledniTyden.get(Calendar.WEEK_OF_YEAR));
						for (int j = 2; j < 7; j++) { // tyden planujeme na 5 dnu nedìle == 1, planujeme od pondìlído patku
							data[2 * (j - 1)] = Integer.toString(posledniTyden.get(Calendar.DAY_OF_MONTH));
							posledniTyden.add(Calendar.DAY_OF_WEEK, 1);
						}
						posledniTyden.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY); // každy ètvrtek je vždy ve spravném roku
						data[12] = Integer.toString(posledniTyden.get(Calendar.YEAR));
						
						tableGenericka.addRow(tableGenericka.getRowCount(),
								data, novyZmen);
						Dimension s = this.hlavniOkno.getObalVedlejsihoOkna()
								.getPreferredSize();
						s.height += tableGenericka.getRowHeight();
						this.hlavniOkno.getObalVedlejsihoOkna()	.setPreferredSize(s);
					}
				}
				int index = 0;
				// System.out.println("cisloUpravovanehoTydne "+cisloUpravovanehoTydne);
				// System.out.println("Pridavany den "+ sdf.format(datum));
				// System.out.println(pridavanyDen.toString());
				/*
				 * while(cisloUpravovanehoTydne pridavanyDen.after(prvniTyden) >
				 * cisloTydne ){ index++; cisloTydne++; if(cisloTydne >= 53){
				 * cisloTydne = 1; } }
				 */
				while (pridavanyDen.after(prvniTyden) && prvniTyden.get(Calendar.WEEK_OF_YEAR) != pridavanyDen.get(Calendar.WEEK_OF_YEAR)) {
					index++;
					prvniTyden.add(Calendar.WEEK_OF_YEAR, 1);
				}
				String cislo = null;
				if (index < tableGenericka.getRowCount()) {
					cislo = (String) tableGenericka.getValueAt(index, 2 * pridavanyDen.get(Calendar.DAY_OF_WEEK) - 1);
				}

				if (cislo == null) {
					tableGenericka.setValueAt("1", index, 2 * pridavanyDen.get(Calendar.DAY_OF_WEEK) - 1);
				} else {
					int pom = Integer.parseInt(cislo);
					pom++;
					tableGenericka.setValueAt(Integer.toString(pom), index,	2 * pridavanyDen.get(Calendar.DAY_OF_WEEK) - 1);
				}

			} else {
				// Model je prazdny, pøidej øadek
				Calendar pom = (Calendar) pridavanyDen.clone();
				pom.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
				String[] data = new String[tableGenericka.getColumnCount()];
				data[0] = nazevMesice(pom.get(Calendar.MONTH));
				data[1] = Integer.toString(pom.get(Calendar.WEEK_OF_YEAR));
				data[2 * pridavanyDen.get(Calendar.DAY_OF_WEEK) - 1] = "1";
				
				data[12] = Integer.toString(pom.get(Calendar.YEAR));
				pom.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				for (int j = 2; j < data.length - 2; j += 2) {
					data[j] = Integer.toString(pom.get(Calendar.DAY_OF_MONTH));
					pom.add(Calendar.DAY_OF_WEEK, 1);
				}
				boolean[][] pole = new boolean[1][tableGenericka.getColumnCount()];
				for (int m = 0; m < pole[0].length; m++){
					pole[0][m] = false;
				}
				pole[0][2 * pridavanyDen.get(Calendar.DAY_OF_WEEK) - 1] = true;
				tableGenericka.setZmeneno(pole);
				tableGenericka.addRow(0, data, pole);
				Dimension s = this.hlavniOkno.getObalVedlejsihoOkna().getPreferredSize();
				s.height += tableGenericka.getRowHeight();
				this.hlavniOkno.getObalVedlejsihoOkna().setPreferredSize(s);
				//tableGenericka.getZmeneno()[0][2 * pridavanyDen.get(Calendar.DAY_OF_WEEK) - 1] = true;
			}

			// Yes == 0 No == 1
			// int reply = JOptionPane.showConfirmDialog(hlavniOkno ,
			// "Neplánujete postupnì den po dni za sebou, ale pøeskakujete. Opravdu to zamýšlíte?",
			// "Otázka", JOptionPane.YES_NO_OPTION);

			tableFyzkusy.getModel().setValueAt(novaHodnota, i, indexSloupce);
		}
		
	}

	private void generujRozvrh(boolean isPlanovaniLiti) throws Exception {
		SQLStor sql = sklad.getSql();
		int i = Integer.parseInt(textIdZakazky.getText());
		TransClass ts = null;
		if(isPlanovaniLiti){
			ts = sql.planovaniRozvrh(i);
		} else {
			ts = sql.planovaniRozvrhVycisteno(i);
		}
		QueryTableModel tableModel = new QueryTableModel(ts.rs, sklad.getNazvySloupcuTabulek()[4]);
		pocetNeplanovanychKusu.setText(Integer.toString(ts.pocetNenaplanovanychKusu));
		tableGenericka.setModel(tableModel);
		tableGenericka.getColumAdjuster().adjustColumns();
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting()) {
			return;
		}
		textLabelOznaceno.setText(Integer.toString(tableFyzkusy.getSelectedRowCount()));
	}
	
	public String nazevMesice(int i) {
		String prom;
		switch (i) {
		case Calendar.JANUARY:
			prom = "leden";
			break;
		case Calendar.FEBRUARY:
			prom = "únor";
			break;
		case Calendar.MARCH:
			prom = "bøezen";
			break;
		case Calendar.APRIL:
			prom = "duben";
			break;
		case Calendar.MAY:
			prom = "kvìten";
			break;
		case Calendar.JUNE:
			prom = "èerven";
			break;
		case Calendar.JULY:
			prom = "èervenec";
			break;
		case Calendar.AUGUST:
			prom = "srpen";
			break;
		case Calendar.SEPTEMBER:
			prom = "záøí";
			break;
		case Calendar.OCTOBER:
			prom = "øíjen";
			break;
		case Calendar.NOVEMBER:
			prom = "listopad";
			break;
		case Calendar.DECEMBER:
			prom = "prosinec";
			break;
		case Calendar.UNDECIMBER:
			prom = "chyba, Calendar vratil UNDECIMBER";
			break;
		default:
			prom = "Chyba planovani "+i;
			break;
		}
		return prom;
	}
	
	private DefaultListModel<DateStor> createListModel(ResultSet rs) throws SQLException{
		DefaultListModel<DateStor> mod = new DefaultListModel<DateStor>();
		int pocetKusu = 0;
		Date datum;
		while(rs.next()){
			datum = rs.getDate(1);
			pocetKusu = rs.getInt(2);
			mod.addElement(new DateStor(datum, pocetKusu, sdf));			
		}
		return mod;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		//System.out.println(arg0.getID()+" "+ arg0.getKeyCode());
		
		if (arg0.getKeyCode() == 112) {
			//System.out.println(true);
			int i = this.tableGenericka.getSelectedRow();
			dialog.setVisible(true);
			String cisloTydne = (String) tableGenericka.getValueAt(i, 1);
			String rok = (String) tableGenericka.getValueAt(i, 12);
			char f = this.textFormovna.getText().charAt(0);
			try {
				dialog.setTable(Integer.parseInt(cisloTydne), Integer.parseInt(rok), f);
			} catch (Exception e) {
				ExceptionWin win = new ExceptionWin(e);
				win.nic();
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
