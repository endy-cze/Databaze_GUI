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
import javax.swing.table.TableModel;

import sablony.DateStor;
import sablony.MyJButton;
import sablony.MyJDateChooser;
import sablony.ZmenaHodnoty;
import sablony.errorwin.ExceptionWin;
import sablony.tabulka.ColorCellTable;
import sablony.tabulka.QueryTableModel;
import sqlstorage.SQLStor;
import sqlstorage.TransClass;
import storage.SkladOdkazu;

import javax.swing.ListSelectionModel;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JList;

public class Planovani extends JPanel implements ActionListener, ListSelectionListener, KeyListener {
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
	public static final int CTVRTEK = 5;
	private static final int hodinaDriv = 3;
	private static final int hodinaPozdeji = 8;
	private static final int maxPocetRadkuVGenerickeTabulce = 60;

	public static final int indexSloupcePlanovaneLiti = 1;
	
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
	private Color [] barvy;
	
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
		this.hlavniOkno = hlavniOkno;
		this.sklad = hlavniOkno.getSklad();
		this.barvy = sklad.getBarvy();
		this.popisLabels = new JLabel [23];
		this.sdf = new SimpleDateFormat("dd.MM.yyyy");
		
		setBackground(barvy[12]);
		setBorder(new LineBorder(barvy[6]));
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
				this.pridatDatumLiti();
				tableFyzkusy.getColumAdjuster().adjustColumns();

			} else if (com.equalsIgnoreCase("DokoncitPlanovani")) {
				boolean[][] upraveno = tableFyzkusy.getZmeneno();
				SQLStor sql = sklad.getSql();
				for (int m = 0; m < upraveno.length; m++) {
					if (upraveno[m][1] || upraveno[m][6]) { // tady mozna smazat "|| upraveno[m][6]" je to useless. Menime jen slopec 1
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
	
	private void pridatDatumLiti() throws ParseException{
		ColorCellTable tableFyzkusy = this.tableFyzkusy;
		ColorCellTable tableGenericka= this.tableGenericka;
		int [] selectedRows = this.tableFyzkusy.getSelectedRows();
		Date pridavanyDatum = pridatDatumLiti.getDate();
		
		// 1. krok algoritmu  zda pridavanyDatum neni sobota ci nedele
		if(isSundayOrSaturday(pridavanyDatum)){
			JOptionPane.showMessageDialog(hlavniOkno, "O víkendu se nelije");
			return;
		}
		
		// 2. krok rozdeleni algoritmu na pridavanyDatum == null a pridavanyDatum != null
		ZmenaHodnoty [] zmenyHodnot = null;
		if (pridavanyDatum == null){
			zmenyHodnot = this.pridejDatumWhichIsNullGetZmeneno(tableFyzkusy, tableGenericka, selectedRows);
		} else {
			zmenyHodnot = this.priddejDatumWhichIsNotNullGetZmeneno(tableFyzkusy, tableGenericka, selectedRows, pridavanyDatum);
		}
		
		// 3. krok mam zmeny tabulky fyzKusy ted upravim/vygeneruju tabulku tableGenericka
		// seøazeni pole zmeny hodnot, aby null byli na konci a datumy šli hezky po sobe. chronologicky (normalnì)
		sortZmeneneHodnoty(zmenyHodnot);
		
		// cislo radku v genericke tabulce je jednoznacne identifikováno pomocí roku a èísla týdne
		Calendar zjistiRokDatum = Calendar.getInstance();
		if(pridavanyDatum != null){
			zjistiRokDatum.setTime(pridavanyDatum);
		}
		zjistiRokDatum.setMinimalDaysInFirstWeek(4); // dle ISO 8601 normy prvni tyden musí mít vìtšinu dnù (min 4) v lednu
		zjistiRokDatum.setFirstDayOfWeek(2); // v èechach je prvni den pondìlí
		zjistiRokDatum.set(Calendar.HOUR_OF_DAY, hodinaDriv); // nastaveni casu na pulnoc aby to bylo vsude stejny
		
		// a) zjistime zda budeme pridavat radky pred již vygenerovany rozvrh (pokud je prazdny tak ne). Pokud ano kolik?
		int pocTydnuPredRozvrh = this.radekTydnuPredExistujiciRozvrh(zmenyHodnot, tableGenericka, zjistiRokDatum);
		// b) zjistime jestli budeme pridavat radky za již vygenerovany rozvrh (pokud prazdny tak ano). Pokud ano kolik?
		int pocTydnuZaRozvrh = this.radekTydnuZaExistujiciRozvrh(zmenyHodnot, tableGenericka, zjistiRokDatum);
		
		// Ochrana proti pridani moc radku, respektive aby tabulka rozvrhu nemela moc radku!
		if (maxPocetRadkuVGenerickeTabulce < tableGenericka.getRowCount() + pocTydnuZaRozvrh + pocTydnuPredRozvrh){
			this.zrusZmeneno(tableFyzkusy, zmenyHodnot);
			JOptionPane.showMessageDialog(hlavniOkno, "Pøekroèili jste možný poèet øádkù v rozvrhu. Prosím zvolte Plánované datum lití jinak.");
			return;
		}
		
		// c) vytvorime nove booleanovske pole podle poctu novych radku pred a za. všude, kde bude nova hodnota dame False 
		boolean [][] pole = vytvorBooleanPole(tableGenericka, pocTydnuPredRozvrh, pocTydnuZaRozvrh);
		
		//tableGenericka.addRow(predRadek, data, zmeneno);
		// d) pomocí metody addRow(data, pole); budeme pridavat nove radky s vyplnenými rokem, èíslo tydne, mesic a datem (ano opakujeme a pridavame stale stejne pole)
		
		// d) pridame radky pred rozvrh
		this.pridejRadkyPredRozvrh(zjistiRokDatum, tableGenericka, pocTydnuPredRozvrh, pole);
		
		// e) pridame radky za rozvrh
		this.pridejRadkyZaRozvrh(zjistiRokDatum, tableGenericka, pocTydnuZaRozvrh, pole);
		
		// uprava okna aby se dalo scrolovat
		Dimension s = this.hlavniOkno.getObalVedlejsihoOkna().getPreferredSize();
		s.height += pocTydnuZaRozvrh*tableGenericka.getRowHeight() + pocTydnuPredRozvrh*tableGenericka.getRowHeight();
		this.hlavniOkno.getObalVedlejsihoOkna().setPreferredSize(s);
		
		// h) vyplnime date pomoci metody v Color table
		Calendar pom;
		for(int i = 0; i < zmenyHodnot.length; i++){
			if(zmenyHodnot[i] == null){
				break;
			}
			// nova hodnota
			if(zmenyHodnot[i].getDate() != null){ // jestli tam byla pred tim nula tak ji neodecitej ;)
				zjistiRokDatum.setTime(zmenyHodnot[i].getDate());
				pom = (Calendar) zjistiRokDatum.clone();
				pom.set(Calendar.DAY_OF_WEEK, CTVRTEK); // ctvrtek je vzdy ve spravnem roce
				this.tableGenericka.addValueGenericTableAtYearWeek(pom.get(Calendar.YEAR), pom.get(Calendar.WEEK_OF_YEAR), zjistiRokDatum.get(Calendar.DAY_OF_WEEK), 1);
			}
			// stara hodnota
			if(zmenyHodnot[i].getOldDate() != null){ // jestli tam byla pred tim nula tak ji neodecitej ;)
				zjistiRokDatum.setTime(zmenyHodnot[i].getOldDate());
				pom = (Calendar) zjistiRokDatum.clone();
				pom.set(Calendar.DAY_OF_WEEK, CTVRTEK); // ctvrtek je vzdy ve spravnem roce
				this.tableGenericka.addValueGenericTableAtYearWeek(pom.get(Calendar.YEAR), pom.get(Calendar.WEEK_OF_YEAR), zjistiRokDatum.get(Calendar.DAY_OF_WEEK), -1);
			}
		}
	}
	
	/**
	 * Metoda, která pøidá nový záznam do Tabulky FyzKusy a vrátí seznam zmìnìných hodnot, které použijeme pro generaci, popøípadì
	 * úpravu, tabulky tableGenericka.
	 * @param tableFyzkusy
	 * @param tableGenericka
	 * @param selectedRows seznam radek, kam se da nova hodnota datumu planovani.
	 * @return Seznam zmenenych hodnot. Nektere hodnoty muzou být null.
	 * @throws ParseException 
	 */
	private ZmenaHodnoty [] pridejDatumWhichIsNullGetZmeneno(ColorCellTable tableFyzkusy, ColorCellTable tableGenericka, int [] selectedRows) throws ParseException{
		ZmenaHodnoty [] zmenyHodnot = new ZmenaHodnoty[selectedRows.length];
		TableModel model = tableFyzkusy.getModel();
		String novaHodnota = null;
		
		for(int i = 0; i < selectedRows.length; i++){
			zmenyHodnot[i] = null; // inicializace
			String puvHodnota = (String) model.getValueAt(selectedRows[i], indexSloupcePlanovaneLiti);
			
			if(puvHodnota == null){
				continue; // hodnoty se rovnaj
			} else {
				if(puvHodnota.equalsIgnoreCase("")){
					continue; // hodnoty se rovnaj
				}
			}
			// hodnoty se nerovnaji -> novy zaznam
			zmenyHodnot[i] = new ZmenaHodnoty(novaHodnota, puvHodnota, selectedRows[i], sdf);	
			
			// zápis nové hodnoty do tabulky fyzKusy
			model.setValueAt(novaHodnota, selectedRows[i], indexSloupcePlanovaneLiti);
		}
		return zmenyHodnot;
	}
	
	/**
	 * Metoda, která pøidá nový záznam do Tabulky FyzKusy a vrátí seznam zmìnìných hodnot, které použijeme pro generaci, popøípadì
	 * úpravu, tabulky tableGenericka.
	 * @param tableFyzkusy
	 * @param tableGenericka
	 * @param selectedRows
	 * @param pridavanyDatum
	 * @return Seznam zmenenych hodnot. Nektere hodnoty muzou být null.
	 * @throws ParseException 
	 */
	private ZmenaHodnoty [] priddejDatumWhichIsNotNullGetZmeneno(ColorCellTable tableFyzkusy, ColorCellTable tableGenericka, int [] selectedRows, Date pridavanyDatum) throws ParseException{
		ZmenaHodnoty [] zmenyHodnot = new ZmenaHodnoty[selectedRows.length];
		TableModel model = tableFyzkusy.getModel();
		String novaHodnota = sdf.format(pridavanyDatum);
		
		for(int i = 0; i < selectedRows.length; i++){
			zmenyHodnot[i] = null; // inicializace
			String puvHodnota = (String) model.getValueAt(selectedRows[i], indexSloupcePlanovaneLiti);
			
			if(puvHodnota != null){
				if(puvHodnota.equalsIgnoreCase(novaHodnota)){
					continue; // hodnoty se rovnaj
				}
			}
			// hodnoty se nerovnaji -> novy zaznam
			zmenyHodnot[i] = new ZmenaHodnoty(novaHodnota, puvHodnota, selectedRows[i], sdf);	
			
			// zápis nové hodnoty do tabulky fyzKusy
			model.setValueAt(novaHodnota, selectedRows[i], indexSloupcePlanovaneLiti);
		}
		return zmenyHodnot;
	}
	
	/**
	 * Zda daný datum vychází na sobotu èi nedìli.
	 * @param pridavanyDatum jakýkoliv Date i null
	 * @return true pokud je to sobota èi nedìle, pokud null tak false, jinak false
	 */
	private boolean isSundayOrSaturday(Date pridavanyDatum){
		if (pridavanyDatum != null){
			Calendar pridavanyDen = Calendar.getInstance();
			pridavanyDen.setTime(pridavanyDatum);
			if (pridavanyDen.get(Calendar.DAY_OF_WEEK) == 1	|| pridavanyDen.get(Calendar.DAY_OF_WEEK) == 7) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Metoda pro srovnani hodnot zmenenych. Aby null byli na konci
	 * @param zmenyHodnot
	 */
	private void sortZmeneneHodnoty(ZmenaHodnoty [] zmenyHodnot){
		// seradim pole zmenyHodnot staèí mi buble sort :D
		ZmenaHodnoty pom;
		for (int i = 0; i < zmenyHodnot.length; i++) {
			for (int j = 0; j < zmenyHodnot.length - i - 1; j++) {
				if (zmenyHodnot[j] == null) {
					if (zmenyHodnot[j+1] == null) {
						continue;
					} else { // prohodime null a date
						pom = zmenyHodnot[j];
						zmenyHodnot[j] = zmenyHodnot[j + 1];
						zmenyHodnot[j + 1] = pom;
					}
				} else {
					if (zmenyHodnot[j+1] == null) { // null je za datumem nic se nedeje
						continue; 
					} else {
						if(zmenyHodnot[j].getDate().compareTo(zmenyHodnot[j].getDate()) > 0){
							pom = zmenyHodnot[j];
							zmenyHodnot[j] = zmenyHodnot[j+1];
							zmenyHodnot[j+1] = pom;
						}
					}

				}
			}
		}
	}

	private int radekTydnuPredExistujiciRozvrh(ZmenaHodnoty [] zmenyHodnot, ColorCellTable tableGenericka, Calendar zjistiRokDatum){
		Calendar prvniNoveDatum = (Calendar) zjistiRokDatum.clone();
		Calendar prvniDatumZTabulky = zjistiRokDatum;
		
		if(tableGenericka.getRowCount() <= 0){return 0;} // tabulka genericka je prazdna
		if(zmenyHodnot[0] == null){return 0;}// je to seøazene, takže pokud je prvni null, null jsou všechny
		if(zmenyHodnot[0].getDate() == null){return 0;} // jestlize je null tak ho maji vsichni (protoze najednou pridavame jen jeden datum)

		
		int pocetTydnu = 0;
		
		// nastaveni prvniho noveho(zmeneneho) data
		prvniNoveDatum.setTime(zmenyHodnot[0].getDate()); // prvni nove upravene datum
		this.set(prvniNoveDatum, prvniNoveDatum.get(Calendar.YEAR), prvniNoveDatum.get(Calendar.WEEK_OF_YEAR), CTVRTEK, hodinaPozdeji);
		
		// nastaveni prvniho datumu z generické tabulky
		int rokPrvniRadka = Integer.parseInt((String)tableGenericka.getValueAt(0, 12)); // prvni rok v rozvrhu
		int tydenPrvniRadka = Integer.parseInt((String)tableGenericka.getValueAt(0, 1)); // prvni tyden v rozvrhu
		this.set(prvniDatumZTabulky, rokPrvniRadka, tydenPrvniRadka, CTVRTEK, hodinaDriv);

		if(prvniNoveDatum.after(prvniDatumZTabulky)){ // pokud je nove datum dele nez prvni datum z genericke tabulky je to true 
			return 0; // prvni nove datum je po prvním datu generické tabulky. Nebudeme generovat øady pøed.
		}
		
		while(prvniNoveDatum.before(prvniDatumZTabulky)){
			pocetTydnu++;
			prvniNoveDatum.add(Calendar.WEEK_OF_YEAR, 1);
		}
		
		return pocetTydnu;
	}

	private int radekTydnuZaExistujiciRozvrh(ZmenaHodnoty [] zmenyHodnot, ColorCellTable tableGenericka, Calendar zjistiRokDatum){
 		Calendar posledniNoveDatum = (Calendar) zjistiRokDatum.clone();
		Calendar posledniDatumZTabulky = zjistiRokDatum;
		int i = 0;
		for(i = 0; i < zmenyHodnot.length; i++){
			if(zmenyHodnot[i] == null){
				break;
			}
		}
		i--; // jelikoz slouzi jako index a ten je od 0 do zmenyHodnot.length - 1 
		int pocetTydnu = 0;
		if(zmenyHodnot[0] == null){return 0;}// je to seøazene, takže pokud je prvni null, null jsou všechny a nebude se nic menit-nerealny
		if(zmenyHodnot[0].getDate() == null){return 0;} // jestlize je null tak ho maji vsichni (protoze najednou pridavame jen jeden datum)

		
		
		// nastaveni posledniho noveho(zmeneneho) data
		posledniNoveDatum.setTime(zmenyHodnot[i].getDate()); // posledni nove upravene datum
		this.set(posledniNoveDatum, posledniNoveDatum.get(Calendar.YEAR), posledniNoveDatum.get(Calendar.WEEK_OF_YEAR), CTVRTEK, hodinaDriv);
		
		
		// pokud je tabulka prazdna vratime pocet tydnu mezi prvnim zmeny hodnot a poslednim zmeny hodnot. (minimalne to musi bejt 1!!)
		if(tableGenericka.getRowCount() < 0){
			Calendar prvniNoveDatum = Calendar.getInstance();
			prvniNoveDatum.setTime(zmenyHodnot[0].getDate()); // posledni nove upravene datum
			this.set(prvniNoveDatum, prvniNoveDatum.get(Calendar.YEAR), prvniNoveDatum.get(Calendar.WEEK_OF_YEAR), CTVRTEK, hodinaDriv);
			this.set(posledniNoveDatum, posledniNoveDatum.get(Calendar.YEAR), posledniNoveDatum.get(Calendar.WEEK_OF_YEAR), CTVRTEK, hodinaPozdeji);

			while(prvniNoveDatum.before(posledniNoveDatum)){
				pocetTydnu++;
				prvniNoveDatum.add(Calendar.WEEK_OF_YEAR, 1);
			}
			return pocetTydnu;
		}
		
		// nastaveni posledniho datumu z generické tabulky
		int rokPosledniRadka = Integer.parseInt((String)tableGenericka.getValueAt(tableGenericka.getRowCount() - 1, 12)); // posledni rok v rozvrhu
		int tydenPosledniRadka = Integer.parseInt((String)tableGenericka.getValueAt(tableGenericka.getRowCount() - 1, 1)); // posledni tyden v rozvrhu
		this.set(posledniDatumZTabulky, rokPosledniRadka, tydenPosledniRadka, CTVRTEK, hodinaPozdeji);
		
		

		if(posledniNoveDatum.before(posledniDatumZTabulky)){ // pokud je nove datum driv nez prvni datum z genericke tabulky je to true 
			return 0; // prvni nove datum je driv nez prvním datu generické tabulky. Nebudeme generovat øady pøed.
		}
		
		while(posledniNoveDatum.after(posledniDatumZTabulky)){
			pocetTydnu++;
			posledniNoveDatum.add(Calendar.WEEK_OF_YEAR, -1);
		}
		
		return pocetTydnu;
	}

	private boolean [][] vytvorBooleanPole(ColorCellTable tableGenericka, int radekPred, int radekZa){
		boolean [][] starePole = tableGenericka.getZmeneno();
		if(radekPred == 0 && radekZa == 0){return starePole;}
		TableModel tm = tableGenericka.getModel();
		boolean [][] novePole = new boolean [starePole.length + radekPred + radekZa][tm.getColumnCount()];
		
		for(int i = 0; i < novePole.length; i++){
			for(int j = 0; j < novePole[i].length; j++){
				if(i < radekPred){
					novePole[i][j] = false;
				} else if(i < tm.getRowCount() + radekPred){
					novePole[i][j] = starePole[i - radekPred][j];
				} else {
					novePole[i][j] = false;
				}
			}
		}
		return novePole;
	}
	
	private void pridejRadkyPredRozvrh(Calendar pom, ColorCellTable tableGenericka, int pocTydnuPredRozvrh, boolean [][] zmeneno){
		// nastaveni prvniho datumu z generické tabulky
		int rokPrvniRadka = Integer.parseInt((String)tableGenericka.getValueAt(0, 12)); // prvni rok v rozvrhu
		int tydenPrvniRadka = Integer.parseInt((String)tableGenericka.getValueAt(0, 1)); // prvni tyden v rozvrhu
		this.set(pom, rokPrvniRadka, tydenPrvniRadka, CTVRTEK, hodinaDriv);
		for(int i = 0; i < pocTydnuPredRozvrh; i++){
			pom.set(Calendar.DAY_OF_WEEK, CTVRTEK);
			pom.add(Calendar.WEEK_OF_YEAR, -1);
			String [] data = new String [tableGenericka.getColumnCount()];
			data[0] = nazevMesice(pom.get(Calendar.MONTH));
			data[1] = Integer.toString(pom.get(Calendar.WEEK_OF_YEAR));			
			data[12] = Integer.toString(pom.get(Calendar.YEAR));
			pom.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			for (int j = 2; j < data.length - 2; j += 2) { // pridani datumu (zvyraznenych cerne)
				data[j] = Integer.toString(pom.get(Calendar.DAY_OF_MONTH));
				pom.add(Calendar.DAY_OF_WEEK, 1);
			}
			tableGenericka.addRow(0, data, zmeneno);
		}
	}
	
	private void pridejRadkyZaRozvrh(Calendar pom, ColorCellTable tableGenericka, int pocTydnuZaRozvrh, boolean [][] zmeneno){
		// nastaveni prvniho datumu z generické tabulky
		int rokPosledniRadka = Integer.parseInt((String)tableGenericka.getValueAt(tableGenericka.getRowCount() - 1, 12)); // posledni rok v rozvrhu
		int tydenPosledniRadka = Integer.parseInt((String)tableGenericka.getValueAt(tableGenericka.getRowCount() - 1, 1)); // posledni tyden v rozvrhu
		this.set(pom, rokPosledniRadka, tydenPosledniRadka, CTVRTEK, hodinaPozdeji);
		for(int i = 0; i < pocTydnuZaRozvrh; i++){
			pom.set(Calendar.DAY_OF_WEEK, CTVRTEK);
			pom.add(Calendar.WEEK_OF_YEAR, 1);
			String [] data = new String [tableGenericka.getColumnCount()];
			data[0] = nazevMesice(pom.get(Calendar.MONTH));
			data[1] = Integer.toString(pom.get(Calendar.WEEK_OF_YEAR));			
			data[12] = Integer.toString(pom.get(Calendar.YEAR));
			pom.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			for (int j = 2; j < data.length - 2; j += 2) { // pridani datumu (zvyraznenych cerne)
				data[j] = Integer.toString(pom.get(Calendar.DAY_OF_MONTH));
				pom.add(Calendar.DAY_OF_WEEK, 1);
			}
			tableGenericka.addRow(tableGenericka.getRowCount(), data, zmeneno);
		}
	}
	
	private void zrusZmeneno(ColorCellTable tableFyzkusy, ZmenaHodnoty [] zmenyHodnot){
		for(int i = 0; i < zmenyHodnot.length; i++){
			TableModel model = tableFyzkusy.getModel();
			model.setValueAt(zmenyHodnot[i].getPuvHodn(), zmenyHodnot[i].getRadek(), indexSloupcePlanovaneLiti);
		}
	}
	
	/**
	 * Metoda pro aktualizaci tabulky fyzické kusy a vygenerované tabulky.
	 * @param isPlanovaniLiti
	 * @throws Exception
	 */
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
	
	/**
	 * Nastavi danemu calendari dany parametry a pak jej vrati
	 * @param cal Calendar ktery budeme upravovat
	 * @param year
	 * @param weekOfYear
	 * @param dayOfTheWeek
	 * @param hour
	 * @return
	 */
	private Calendar set(Calendar cal, int year, int weekOfYear, int dayOfTheWeek, int hour){
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		cal.set(Calendar.DAY_OF_WEEK, dayOfTheWeek);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal;
	}
	
	private String nazevMesice(int i) {
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
