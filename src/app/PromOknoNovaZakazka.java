package app;

import iListeners.PridatZakazkuButtonListener;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;

import sablony.MyJButton;
import sablony.MyJDateChooser;
import sablony.errorwin.ExceptionWin;
import sablony.storage.DateStor;
import storage.SkladOdkazu;

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JList;

import com.toedter.calendar.JDateChooser;

import javax.swing.ListSelectionModel;

public class PromOknoNovaZakazka extends JPanel implements ActionListener {
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;
	private SkladOdkazu sklad;
	private MainFrame hlavniOkno;
	
	private JComboBox<String> textVyberZakaznika;
	private JComboBox<String> textVyberModel;
	private JTextField textCisloObjednavky;
	private JTextField textCena;
	private JTextField kurzEuCzk;
	private JTextField textPocetKusu;
	private JTextField textPaganyrka;
	private JTextArea textPoznamka;
	
	private JButton [] jButtonNajdi= new JButton [2];
	
	private Color [] barvy;
	//private Font [] fonty;
	private JButton pridejUpravZakazku;
	private JRadioButton zaKus;
	private JRadioButton zaKg;
	private JRadioButton czk;
	private JRadioButton eur;
	private JLabel idZakazkyLabel;
	private JLabel popisIdZakazkyLabel;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	private JComponent [][] listComponents;
	private DefaultComboBoxModel<String> zakaznikComboModel;
	private DefaultComboBoxModel<String> modelComboModel;
	private String [] obsahComboBoxu = {"","","",""};
	private MyJDateChooser datumObjednavky;
	
	private int idIntAktualnihoZakaznika = -1;
	private int idIntAktualnihoModelu = -1;
	private JLabel nadpisTextLabel;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel_5;
	private JLabel lblmax;
	private JLabel lblDatumExpedice;
	private MyJDateChooser terminExpedice;
	private JCheckBox uzavrenaCheck;
	private JList<DateStor> seznamDilcichTerminu;
	private DefaultListModel<DateStor> model;
	private JButton pridejTermin;
	private JDateChooser dateDilciTerminy;
	private JButton smazTermin;
	private JLabel lblNewLabel_6;
	private JLabel lblPoetKus_1;
	private JTextField pocetKusuDilciTermin;
	private JLabel lblUpozornnCelkovCena;
	private JScrollPane scrollPane_1;

	public void addListeners(){
		PridatZakazkuButtonListener list = new PridatZakazkuButtonListener(hlavniOkno, this);
		for(int i = 0; i < jButtonNajdi.length; i++){
			jButtonNajdi[i].addActionListener(list);
		}
		pridejUpravZakazku.addActionListener(list);
	}
	
	private void initListComponents(){
		listComponents = new JComponent [23][];
		
		listComponents[0] =  new JComponent[1];
		listComponents[0][0] = idZakazkyLabel;
		listComponents[1] =  new JComponent[1];
		listComponents[1][0] = textVyberZakaznika; //jmeno zakaznika
		listComponents[2] =  new JComponent[1];
		listComponents[2][0] = textCisloObjednavky;
		listComponents[3] =  new JComponent[1];
		listComponents[3][0] = textVyberModel;  //Cislo modelu
		listComponents[4] =  new JComponent[1];
		listComponents[4][0] = datumObjednavky;
		listComponents[5] =  new JComponent[1];
		listComponents[5][0] = textPocetKusu;
		listComponents[6] =  new JComponent[1];
		listComponents[6][0] = terminExpedice;
		listComponents[7] =  new JComponent[1];
		listComponents[7][0] = textCena; 	    
		listComponents[8] =  new JComponent[2];
		listComponents[8][0] = czk; 
		listComponents[8][1] = eur; 
		listComponents[9] =  new JComponent[2];
		listComponents[9][0] = zaKus; 
		listComponents[9][1] = zaKg;
		listComponents[10] =  new JComponent[1];
		listComponents[10][0] = kurzEuCzk;
		listComponents[11] =  new JComponent[1];
		listComponents[11][0] = textVyberModel; //id modelu
		listComponents[12] =  new JComponent[1];
		listComponents[12][0] = textVyberModel; //jmeno modelu
		listComponents[13] =  new JComponent[1];
		listComponents[13][0] = null; //material
		listComponents[14] =  new JComponent[1];
		listComponents[14][0] = null; //vlastni material
		listComponents[15] =  new JComponent[1];
		listComponents[15][0] = null; //Formovna
		listComponents[16] =  new JComponent[1];
		listComponents[16][0] = null; //hmotnost
		listComponents[17] =  new JComponent[1];
		listComponents[17][0] = null; //is odhadova hmotnost
		listComponents[18] =  new JComponent[1];
		listComponents[18][0] = null; //norma slevac
		listComponents[19] =  new JComponent[1];
		listComponents[19][0] = textVyberZakaznika; //id zakaznika
		listComponents[20] =  new JComponent[1];
		listComponents[20][0] = uzavrenaCheck;
		listComponents[21] =  new JComponent[1];
		listComponents[21][0] = textPoznamka; //poznamka
		listComponents[22] =  new JComponent[1];
		listComponents[22][0] = textPaganyrka;
		
		
	}
	
	public void setNovaZakazka(){
		this.nadpisTextLabel.setText("P\u0159idat novou zak\u00E1zku");
		pridejUpravZakazku.setActionCommand("PridejZakazku");
		pridejUpravZakazku.setText("Pøidej novou zakázku");
		popisIdZakazkyLabel.setVisible(false);
		idZakazkyLabel.setVisible(false);
		zakaznikComboModel.removeAllElements();
		modelComboModel.removeAllElements();
		idIntAktualnihoZakaznika = 0;
		idIntAktualnihoModelu = 0;
		uzavrenaCheck.setVisible(false);
		this.model.removeAllElements();
		//smazTermin.setVisible(true);
		smazTermin.setActionCommand("SmazDilciTermin");
		
		for(int i = 0; i < listComponents.length; i++){
			for(int j = 0; j < listComponents[i].length; j++){
				if(listComponents[i][j] instanceof JTextField){
					((JTextField)listComponents[i][j]).setText("");
				} else if(listComponents[i][j] instanceof JTextArea){
					((JTextArea)listComponents[i][j]).setText("");
				} else if(listComponents[i][j] instanceof MyJDateChooser){
					((MyJDateChooser)listComponents[i][j]).setDate(new Date());
				}
			}
		}
		pocetKusuDilciTermin.setText("");
		dateDilciTerminy.setDate(new Date());
	}
	
	public void setUpravZakazku(String [] parametryZakazky) throws SQLException{
		this.nadpisTextLabel.setText("Upravit vybranou zak\u00E1zku");
		pridejUpravZakazku.setActionCommand("UpravZakazku");
		pridejUpravZakazku.setText("Uprav zakázku");
		popisIdZakazkyLabel.setVisible(true);
		idZakazkyLabel.setVisible(true);
		zakaznikComboModel.removeAllElements();
		modelComboModel.removeAllElements();
		uzavrenaCheck.setVisible(true);
		//smazTermin.setVisible(true);
		smazTermin.setActionCommand("SmazDilciTerminUprava");

		
		for(int i = 0; i < listComponents.length; i++){
			for(int j = 0; j < listComponents[i].length; j++){				
				if(listComponents[i][j] != null){
					if(listComponents[i][j] instanceof JTextComponent){
						((JTextComponent) listComponents[i][j]).setText(parametryZakazky[i]);
					} 
					else if(listComponents[i][j] instanceof MyJDateChooser){
						Date date;
						try {
							date = sdf.parse(parametryZakazky[i]);
							((MyJDateChooser) listComponents[i][j]).setDate(date);
						} catch (ParseException e) {
							ExceptionWin.showExceptionMessage(e);
						} 
					} 
					else if (listComponents[i][j] instanceof JLabel){
							((JLabel)listComponents[i][j]).setText(parametryZakazky[i]);
						}	
					else if (listComponents[i][j] instanceof JComboBox){
						setComboBoxy(i, parametryZakazky[i]);
					}	
					else if (listComponents[i][j] instanceof JCheckBox){
						boolean pom = parametryZakazky[i].equalsIgnoreCase("Ano");
						if(pom) uzavrenaCheck.setSelected(true);
						else uzavrenaCheck.setSelected(true);
					}	
					else if (i == 8 || i == 9){
						boolean pom = parametryZakazky[i].equalsIgnoreCase("Ano");
						if(pom)((JRadioButton) listComponents[i][0]).setSelected(true);
						else ((JRadioButton) listComponents[i][1]).setSelected(true);
					}
				}
			}
		}
		
		int idZakazky = Integer.parseInt(parametryZakazky[0]);
		ResultSet rs = sklad.getSql().vyberDilciTerminy(idZakazky);
		model = createListModel(rs);
		seznamDilcichTerminu.setModel(model);
	}
	
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
	 * Metoda, kterou použijeme pøi výbìru zákazníka a výbìru modelu. nastaví nam vyhledané parametry do Combo boxu
	 * @param idZakaznika
	 * @param jmenoZakaznika
	 * @param idModelu
	 * @param cisloModelu
	 */
	public void setComboBoxy(String idZakaznika, String jmenoZakaznika, String idModelu, String cisloModelu){
		if(idZakaznika != null && jmenoZakaznika != null){
			zakaznikComboModel.removeAllElements();
			/**
			 * pozor ty mezery ' "+idZakaznika+" ', tam musí být!
			 * viz metoda novaZakazka() v tride PridatZakazkuButtonListener (slouži pro kontrolu)
			 */
			zakaznikComboModel.addElement("Id zákazníka: "+idZakaznika+"   Jméno: "+jmenoZakaznika);
			this.idIntAktualnihoZakaznika = Integer.parseInt(idZakaznika);
		}
		if(idModelu != null && cisloModelu != null){
			modelComboModel.removeAllElements();
			/**
			 * pozor ty mezery ' "+idModelu+" ', tam musí být!
			 * viz metoda novaZakazka() v tride PridatZakazkuButtonListener (slouži pro kontrolu)
			 */
			modelComboModel.addElement("Id modelu: "+idModelu+"    Èíslo modelu: "+cisloModelu);
			this.idIntAktualnihoModelu = Integer.parseInt(idModelu);
		}
	}

	/**
	 * Velmi citliva metoda nemenit poøadi v metode initList(), slouží pouze pøi upravìZakazky pro pøenesení údajù z tabulky
	 * do comboBoxu
	 * @param index
	 * @param text
	 */
	public void setComboBoxy(int index, String text) {
		if(index == 19) obsahComboBoxu[0] = text;
		if(index == 1) obsahComboBoxu[1] = text;
		if(index == 11) obsahComboBoxu[2] = text;
		if(index == 3) obsahComboBoxu[3] = text;
		
		zakaznikComboModel.removeAllElements();
		/**
		 * pozor ty mezery ' "+idModelu+" ', tam musí být! viz metoda novaZakazka() v tride PridatZakazkuButtonListener
		 */
		zakaznikComboModel.addElement("Id zákazníka: " + obsahComboBoxu[0] + "   Jméno: " + obsahComboBoxu[1]);
		if(!obsahComboBoxu[0].equalsIgnoreCase("")){
			this.idIntAktualnihoZakaznika = Integer.parseInt(obsahComboBoxu[0]);
		}

		modelComboModel.removeAllElements();
		/**
		 * pozor ty mezery ' "+idModelu+" ', tam musí být! viz metoda novaZakazka() v tride PridatZakazkuButtonListener
		 */
		modelComboModel.addElement("Id modelu: " + obsahComboBoxu[2] + "  Èíslo modelu: " + obsahComboBoxu[3]);
		if(!obsahComboBoxu[2].equalsIgnoreCase("")){
			this.idIntAktualnihoModelu = Integer.parseInt(obsahComboBoxu[2]);
		}
	}
	
	public void initComboModels(){
		zakaznikComboModel = new DefaultComboBoxModel<String>();
		modelComboModel = new DefaultComboBoxModel<String>();
	}
	
	/**
	 * 
	 * @param hlavniOkno
	 */
	public PromOknoNovaZakazka(MainFrame hlavniOkno) {
		setBorder(null);
		
		this.hlavniOkno = hlavniOkno;
		this.sklad = hlavniOkno.getSklad();
		//this.fonty = sklad.getFonty();
		this.barvy = sklad.getBarvy();
		initComboModels();
		setBorder(new LineBorder(barvy[6]));
		setBackground(barvy[12]);
		
		setPreferredSize(new Dimension(1042, 762));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{35, 44, 30, 30, 0, 30, 78, 0, 60, 20, 50, 70, 20, 72, 0, 50, 40, 70, 0, 20, 0};
		gridBagLayout.rowHeights = new int[]{34, 0, 30, 30, 0, 30, 0, 0, 0, 20, 0, 23, 30, 0, 0, 30, 30, 90, 30, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		nadpisTextLabel = new JLabel("P\u0159idat novou zak\u00E1zku");
		nadpisTextLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_nadpisTextLabel = new GridBagConstraints();
		gbc_nadpisTextLabel.gridwidth = 5;
		gbc_nadpisTextLabel.anchor = GridBagConstraints.WEST;
		gbc_nadpisTextLabel.insets = new Insets(0, 0, 5, 5);
		gbc_nadpisTextLabel.gridx = 1;
		gbc_nadpisTextLabel.gridy = 1;
		add(nadpisTextLabel, gbc_nadpisTextLabel);
		
		JLabel lblNewLabel = new JLabel("Vyberte z\u00E1kazn\u00EDka:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 2;
		add(lblNewLabel, gbc_lblNewLabel);
		
		textVyberZakaznika = new JComboBox<String>();
		textVyberZakaznika.setModel(zakaznikComboModel);
		GridBagConstraints gbc_textVyberZakaznika = new GridBagConstraints();
		gbc_textVyberZakaznika.fill = GridBagConstraints.HORIZONTAL;
		gbc_textVyberZakaznika.gridwidth = 6;
		gbc_textVyberZakaznika.insets = new Insets(0, 0, 5, 5);
		gbc_textVyberZakaznika.gridx = 4;
		gbc_textVyberZakaznika.gridy = 2;
		add(textVyberZakaznika, gbc_textVyberZakaznika);
		
		JButton najdiZakaznika = new MyJButton("Naj\u00EDt z\u00E1kazn\u00EDka", 16, 1, sklad);
		jButtonNajdi[0] = najdiZakaznika;
		GridBagConstraints gbc_najdiZakaznika = new GridBagConstraints();
		gbc_najdiZakaznika.gridwidth = 2;
		gbc_najdiZakaznika.fill = GridBagConstraints.BOTH;
		gbc_najdiZakaznika.insets = new Insets(0, 0, 5, 5);
		gbc_najdiZakaznika.gridx = 10;
		gbc_najdiZakaznika.gridy = 2;
		najdiZakaznika.setActionCommand("NajdiZakaznika");
		add(najdiZakaznika, gbc_najdiZakaznika);
		
		JLabel lblNewLabel_2 = new JLabel("\u010C\u00EDslo objedn\u00E1vky:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.gridwidth = 2;
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 13;
		gbc_lblNewLabel_2.gridy = 2;
		add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		textCisloObjednavky = new JTextField();
		GridBagConstraints gbc_textCisloObjednavky = new GridBagConstraints();
		gbc_textCisloObjednavky.gridwidth = 2;
		gbc_textCisloObjednavky.insets = new Insets(0, 0, 5, 5);
		gbc_textCisloObjednavky.fill = GridBagConstraints.HORIZONTAL;
		gbc_textCisloObjednavky.gridx = 16;
		gbc_textCisloObjednavky.gridy = 2;
		add(textCisloObjednavky, gbc_textCisloObjednavky);
		textCisloObjednavky.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Vyber model:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.gridwidth = 2;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 3;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textVyberModel = new JComboBox<String>();
		textVyberModel.setModel(modelComboModel);
		GridBagConstraints gbc_textVyberModel = new GridBagConstraints();
		gbc_textVyberModel.gridwidth = 6;
		gbc_textVyberModel.insets = new Insets(0, 0, 5, 5);
		gbc_textVyberModel.fill = GridBagConstraints.HORIZONTAL;
		gbc_textVyberModel.gridx = 4;
		gbc_textVyberModel.gridy = 3;
		add(textVyberModel, gbc_textVyberModel);
		
		JButton najdiModel = new MyJButton("Naj\u00EDt model",16,1, sklad);
		jButtonNajdi[1] = najdiModel;
		GridBagConstraints gbc_najdiModel = new GridBagConstraints();
		gbc_najdiModel.gridwidth = 2;
		gbc_najdiModel.fill = GridBagConstraints.BOTH;
		gbc_najdiModel.insets = new Insets(0, 0, 5, 5);
		gbc_najdiModel.gridx = 10;
		gbc_najdiModel.gridy = 3;
		najdiModel.setActionCommand("NajdiModel");
		add(najdiModel, gbc_najdiModel);
		
		JLabel lblNewLabel_3 = new JLabel("Datum objedn\u00E1vky: ");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.gridwidth = 2;
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 13;
		gbc_lblNewLabel_3.gridy = 3;
		add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		datumObjednavky = new MyJDateChooser();
		GridBagConstraints gbc_datumPrijetiZakazky = new GridBagConstraints();
		gbc_datumPrijetiZakazky.gridwidth = 2;
		gbc_datumPrijetiZakazky.insets = new Insets(0, 0, 5, 5);
		gbc_datumPrijetiZakazky.fill = GridBagConstraints.HORIZONTAL;
		gbc_datumPrijetiZakazky.gridx = 16;
		gbc_datumPrijetiZakazky.gridy = 3;
		add(datumObjednavky, gbc_datumPrijetiZakazky);
		
		popisIdZakazkyLabel = new JLabel("Id zak\u00E1zky:");
		GridBagConstraints gbc_popisIdZakazky = new GridBagConstraints();
		gbc_popisIdZakazky.anchor = GridBagConstraints.WEST;
		gbc_popisIdZakazky.insets = new Insets(0, 0, 5, 5);
		gbc_popisIdZakazky.gridx = 1;
		gbc_popisIdZakazky.gridy = 4;
		add(popisIdZakazkyLabel, gbc_popisIdZakazky);
		
		idZakazkyLabel = new JLabel("555");
		GridBagConstraints gbc_idZakazky = new GridBagConstraints();
		gbc_idZakazky.anchor = GridBagConstraints.WEST;
		gbc_idZakazky.gridwidth = 3;
		gbc_idZakazky.insets = new Insets(0, 0, 5, 5);
		gbc_idZakazky.gridx = 4;
		gbc_idZakazky.gridy = 4;
		add(idZakazkyLabel, gbc_idZakazky);
		
		JLabel lblNewLabel_4 = new JLabel("* Cena:");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 1;
		gbc_lblNewLabel_4.gridy = 6;
		add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		textCena = new JTextField();
		GridBagConstraints gbc_textCena = new GridBagConstraints();
		gbc_textCena.gridwidth = 3;
		gbc_textCena.insets = new Insets(0, 0, 5, 5);
		gbc_textCena.fill = GridBagConstraints.HORIZONTAL;
		gbc_textCena.gridx = 2;
		gbc_textCena.gridy = 6;
		add(textCena, gbc_textCena);
		textCena.setColumns(10);
		
		lblDatumExpedice = new JLabel("Term\u00EDn expedice:");
		GridBagConstraints gbc_lblDatumExpedice = new GridBagConstraints();
		gbc_lblDatumExpedice.gridwidth = 3;
		gbc_lblDatumExpedice.anchor = GridBagConstraints.WEST;
		gbc_lblDatumExpedice.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatumExpedice.gridx = 13;
		gbc_lblDatumExpedice.gridy = 6;
		add(lblDatumExpedice, gbc_lblDatumExpedice);
		
		terminExpedice = new MyJDateChooser();
		GridBagConstraints gbc_datumExpedice = new GridBagConstraints();
		gbc_datumExpedice.gridwidth = 2;
		gbc_datumExpedice.insets = new Insets(0, 0, 5, 5);
		gbc_datumExpedice.fill = GridBagConstraints.BOTH;
		gbc_datumExpedice.gridx = 16;
		gbc_datumExpedice.gridy = 6;
		add(terminExpedice, gbc_datumExpedice);
		
		zaKus = new JRadioButton("Za kus");
		zaKus.setBackground(barvy[12]);
		GridBagConstraints gbc_zaKus = new GridBagConstraints();
		gbc_zaKus.anchor = GridBagConstraints.WEST;
		gbc_zaKus.insets = new Insets(0, 0, 5, 5);
		gbc_zaKus.gridx = 1;
		gbc_zaKus.gridy = 7;
		add(zaKus, gbc_zaKus);
		
		zaKg = new JRadioButton("Za kg");
		zaKg.setBackground(barvy[12]);
		GridBagConstraints gbc_zaKg = new GridBagConstraints();
		gbc_zaKg.gridwidth = 4;
		gbc_zaKg.anchor = GridBagConstraints.WEST;
		gbc_zaKg.insets = new Insets(0, 0, 5, 5);
		gbc_zaKg.gridx = 2;
		gbc_zaKg.gridy = 7;
		add(zaKg, gbc_zaKg);
		
		ButtonGroup colorButtonGroup1 = new ButtonGroup();
	    colorButtonGroup1.add(zaKus);
	    colorButtonGroup1.add(zaKg);
		zaKus.setSelected(true);
		
		JLabel lblPaganrka = new JLabel("Pagan\u00FDrka:");
		GridBagConstraints gbc_lblPaganrka = new GridBagConstraints();
		gbc_lblPaganrka.gridwidth = 2;
		gbc_lblPaganrka.anchor = GridBagConstraints.WEST;
		gbc_lblPaganrka.insets = new Insets(0, 0, 5, 5);
		gbc_lblPaganrka.gridx = 13;
		gbc_lblPaganrka.gridy = 7;
		add(lblPaganrka, gbc_lblPaganrka);
		
		textPaganyrka = new JTextField();
		GridBagConstraints gbc_textPaganyrka = new GridBagConstraints();
		gbc_textPaganyrka.gridwidth = 2;
		gbc_textPaganyrka.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPaganyrka.insets = new Insets(0, 0, 5, 5);
		gbc_textPaganyrka.gridx = 16;
		gbc_textPaganyrka.gridy = 7;
		add(textPaganyrka, gbc_textPaganyrka);
		textPaganyrka.setColumns(10);
		
		czk = new JRadioButton("CZK");
		czk.setBackground(barvy[12]);
		GridBagConstraints gbc_czk = new GridBagConstraints();
		gbc_czk.anchor = GridBagConstraints.WEST;
		gbc_czk.insets = new Insets(0, 0, 5, 5);
		gbc_czk.gridx = 1;
		gbc_czk.gridy = 8;
		add(czk, gbc_czk);
		
		eur = new JRadioButton("EUR");
		eur.setBackground(barvy[12]);
		GridBagConstraints gbc_eur = new GridBagConstraints();
		gbc_eur.gridwidth = 4;
		gbc_eur.anchor = GridBagConstraints.WEST;
		gbc_eur.insets = new Insets(0, 0, 5, 5);
		gbc_eur.gridx = 2;
		gbc_eur.gridy = 8;
		add(eur, gbc_eur);
		
		ButtonGroup colorButtonGroup2 = new ButtonGroup();
	    colorButtonGroup2.add(czk);
	    colorButtonGroup2.add(eur);
	    czk.setSelected(true);
		
		JLabel lblKurzEuczk = new JLabel("* Kurz EU/CZK:");
		GridBagConstraints gbc_lblKurzEuczk = new GridBagConstraints();
		gbc_lblKurzEuczk.anchor = GridBagConstraints.EAST;
		gbc_lblKurzEuczk.insets = new Insets(0, 0, 5, 5);
		gbc_lblKurzEuczk.gridx = 6;
		gbc_lblKurzEuczk.gridy = 8;
		add(lblKurzEuczk, gbc_lblKurzEuczk);
		
		kurzEuCzk = new JTextField();
		GridBagConstraints gbc_kurzEuCzk = new GridBagConstraints();
		gbc_kurzEuCzk.gridwidth = 3;
		gbc_kurzEuCzk.insets = new Insets(0, 0, 5, 5);
		gbc_kurzEuCzk.fill = GridBagConstraints.HORIZONTAL;
		gbc_kurzEuCzk.gridx = 7;
		gbc_kurzEuCzk.gridy = 8;
		add(kurzEuCzk, gbc_kurzEuCzk);
		kurzEuCzk.setColumns(10);
		
		JLabel lblPoznmka = new JLabel("Pozn\u00E1mka k zak\u00E1zce:");
		GridBagConstraints gbc_lblPoznmka = new GridBagConstraints();
		gbc_lblPoznmka.gridwidth = 2;
		gbc_lblPoznmka.anchor = GridBagConstraints.EAST;
		gbc_lblPoznmka.insets = new Insets(0, 0, 5, 5);
		gbc_lblPoznmka.gridx = 10;
		gbc_lblPoznmka.gridy = 8;
		add(lblPoznmka, gbc_lblPoznmka);
		
		scrollPane = new JScrollPane();
		scrollPane.setMaximumSize(new Dimension(32767, 50));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 13;
		gbc_scrollPane.gridy = 8;
		add(scrollPane, gbc_scrollPane);
		
		textPoznamka = new JTextArea();
		textPoznamka.setWrapStyleWord(true);
		textPoznamka.setLineWrap(true);
		scrollPane.setViewportView(textPoznamka);
		
		JLabel lblPoetKus = new JLabel("Po\u010Det kus\u016F:");
		GridBagConstraints gbc_lblPoetKus = new GridBagConstraints();
		gbc_lblPoetKus.anchor = GridBagConstraints.EAST;
		gbc_lblPoetKus.insets = new Insets(0, 0, 5, 5);
		gbc_lblPoetKus.gridx = 1;
		gbc_lblPoetKus.gridy = 10;
		add(lblPoetKus, gbc_lblPoetKus);
		
		textPocetKusu = new JTextField();
		GridBagConstraints gbc_textPocetKusu = new GridBagConstraints();
		gbc_textPocetKusu.gridwidth = 3;
		gbc_textPocetKusu.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPocetKusu.insets = new Insets(0, 0, 5, 5);
		gbc_textPocetKusu.gridx = 2;
		gbc_textPocetKusu.gridy = 10;
		add(textPocetKusu, gbc_textPocetKusu);
		textPocetKusu.setColumns(10);
		
		lblmax = new JLabel("(max 1500)");
		GridBagConstraints gbc_lblmax = new GridBagConstraints();
		gbc_lblmax.anchor = GridBagConstraints.WEST;
		gbc_lblmax.gridwidth = 2;
		gbc_lblmax.insets = new Insets(0, 0, 5, 5);
		gbc_lblmax.gridx = 5;
		gbc_lblmax.gridy = 10;
		add(lblmax, gbc_lblmax);
		
		uzavrenaCheck = new JCheckBox("Uzav\u0159en\u00E1 zak\u00E1zka (je v archivu)");
		uzavrenaCheck.setEnabled(false);
		uzavrenaCheck.setBackground(barvy[12]);
		GridBagConstraints gbc_uzavrenaCheck = new GridBagConstraints();
		gbc_uzavrenaCheck.anchor = GridBagConstraints.WEST;
		gbc_uzavrenaCheck.gridwidth = 3;
		gbc_uzavrenaCheck.insets = new Insets(0, 0, 5, 5);
		gbc_uzavrenaCheck.gridx = 14;
		gbc_uzavrenaCheck.gridy = 10;
		add(uzavrenaCheck, gbc_uzavrenaCheck);
		
		pridejTermin = new MyJButton("P\u0159idej dil\u010D\u00ED term\u00EDn", 16, 1, sklad);
		pridejTermin.setActionCommand("PridejDilciTermin");
		pridejTermin.addActionListener(this);
		
		lblNewLabel_5 = new JLabel("* Cena se zaokrouhluje na  3 desetinn\u00E1 m\u00EDsta a kurz na 4 des. m\u00EDsta.\r\n\r\n");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_5.gridwidth = 9;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 1;
		gbc_lblNewLabel_5.gridy = 11;
		add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		lblUpozornnCelkovCena = new JLabel("Upozorn\u011Bn\u00ED: Celkov\u00E1 cena za jeden kus p\u0159epo\u010Dten\u00E1  na \u010Desk\u00E9 koruny, nesm\u00ED p\u0159esahovat 1 000 000 000 (cca 1 miliarda) a celkov\u00E1 cena zak\u00E1zky by nem\u011Bla p\u0159esahovat 6 miliard. (aplikace nekontroluje)");
		GridBagConstraints gbc_lblUpozornnCelkovCena = new GridBagConstraints();
		gbc_lblUpozornnCelkovCena.anchor = GridBagConstraints.WEST;
		gbc_lblUpozornnCelkovCena.gridwidth = 18;
		gbc_lblUpozornnCelkovCena.insets = new Insets(0, 0, 5, 5);
		gbc_lblUpozornnCelkovCena.gridx = 1;
		gbc_lblUpozornnCelkovCena.gridy = 12;
		add(lblUpozornnCelkovCena, gbc_lblUpozornnCelkovCena);
		
		lblNewLabel_6 = new JLabel("Tabulka d\u00EDl\u010D\u00EDch term\u00EDn\u016F");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.gridwidth = 4;
		gbc_lblNewLabel_6.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 10;
		gbc_lblNewLabel_6.gridy = 14;
		add(lblNewLabel_6, gbc_lblNewLabel_6);
		GridBagConstraints gbc_pridejTermin = new GridBagConstraints();
		gbc_pridejTermin.fill = GridBagConstraints.BOTH;
		gbc_pridejTermin.gridwidth = 3;
		gbc_pridejTermin.insets = new Insets(0, 0, 5, 5);
		gbc_pridejTermin.gridx = 1;
		gbc_pridejTermin.gridy = 15;
		add(pridejTermin, gbc_pridejTermin);
		
		dateDilciTerminy = new JDateChooser(new Date());
		GridBagConstraints gbc_dateDilciTerminy = new GridBagConstraints();
		gbc_dateDilciTerminy.gridwidth = 2;
		gbc_dateDilciTerminy.insets = new Insets(0, 0, 5, 5);
		gbc_dateDilciTerminy.fill = GridBagConstraints.HORIZONTAL;
		gbc_dateDilciTerminy.gridx = 5;
		gbc_dateDilciTerminy.gridy = 15;
		add(dateDilciTerminy, gbc_dateDilciTerminy);
		
		
		smazTermin = new MyJButton("Sma\u017E vybran\u00FD d\u00EDl\u010D\u00ED term\u00EDn", 16, 1, sklad);
		smazTermin.setActionCommand("SmazDilciTermin");
		smazTermin.addActionListener(this);
		
		lblPoetKus_1 = new JLabel("Po\u010Det kus\u016F:");
		GridBagConstraints gbc_lblPoetKus_1 = new GridBagConstraints();
		gbc_lblPoetKus_1.anchor = GridBagConstraints.EAST;
		gbc_lblPoetKus_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblPoetKus_1.gridx = 7;
		gbc_lblPoetKus_1.gridy = 15;
		add(lblPoetKus_1, gbc_lblPoetKus_1);
		
		pocetKusuDilciTermin = new JTextField();
		GridBagConstraints gbc_pocetKusuDilciTermin = new GridBagConstraints();
		gbc_pocetKusuDilciTermin.insets = new Insets(0, 0, 5, 5);
		gbc_pocetKusuDilciTermin.fill = GridBagConstraints.HORIZONTAL;
		gbc_pocetKusuDilciTermin.gridx = 8;
		gbc_pocetKusuDilciTermin.gridy = 15;
		add(pocetKusuDilciTermin, gbc_pocetKusuDilciTermin);
		pocetKusuDilciTermin.setColumns(10);
		model = new DefaultListModel<DateStor>();
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 3;
		gbc_scrollPane_1.gridwidth = 3;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 10;
		gbc_scrollPane_1.gridy = 15;
		add(scrollPane_1, gbc_scrollPane_1);
		
		seznamDilcichTerminu = new JList<DateStor>();
		scrollPane_1.setViewportView(seznamDilcichTerminu);
		seznamDilcichTerminu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		seznamDilcichTerminu.setBorder(new LineBorder(Color.LIGHT_GRAY));
		seznamDilcichTerminu.setModel(model);
		GridBagConstraints gbc_smazTermin = new GridBagConstraints();
		gbc_smazTermin.gridwidth = 3;
		gbc_smazTermin.fill = GridBagConstraints.BOTH;
		gbc_smazTermin.insets = new Insets(0, 0, 5, 5);
		gbc_smazTermin.gridx = 1;
		gbc_smazTermin.gridy = 16;
		add(smazTermin, gbc_smazTermin);
		
		pridejUpravZakazku = new MyJButton("Pøidej novou zakázku", 16, 1, sklad);
		GridBagConstraints gbc_pridejUpravZakazku = new GridBagConstraints();
		gbc_pridejUpravZakazku.gridwidth = 3;
		gbc_pridejUpravZakazku.fill = GridBagConstraints.BOTH;
		gbc_pridejUpravZakazku.insets = new Insets(0, 0, 5, 5);
		gbc_pridejUpravZakazku.gridx = 1;
		gbc_pridejUpravZakazku.gridy = 18;
		add(pridejUpravZakazku, gbc_pridejUpravZakazku);
		
		initListComponents();
		addListeners();
	}


	public int getIdIntAktualnihoZakaznika() {
		return idIntAktualnihoZakaznika;
	}

	public int getIdIntAktualnihoModelu() {
		return idIntAktualnihoModelu;
	}

	/**
	 * 
		listComponents[0][0] = idZakazkyLabel;<br>
		listComponents[1][0] = textVyberZakaznika; //jmeno zakaznika<br>
		listComponents[2][0] = textCisloObjednavky;<br>
		listComponents[3][0] = textVyberModel;  //Cislo modelu<br>
		listComponents[4][0] = datumPrijetiZakazky;<br>
		listComponents[5][0] = textPocetKusu;<br>
		listComponents[6][0] = datumExpedice;<br>
		listComponents[7][0] = textCena; 	    <br>
		listComponents[8][0] = czk; <br>
		listComponents[8][1] = eur; <br>
		listComponents[9][0] = zaKus; <br>
		listComponents[9][1] = zaKg; <br>
		listComponents[10][0] = kurzEuCzk;<br>
		listComponents[11][0] = textVyberModel; //id modelu<br>
		listComponents[12][0] = textVyberModel; //jmeno modelu<br>
		listComponents[13][0] = null; //material<br>
		listComponents[14][0] = null; //vlastni material<br>
		listComponents[15][0] = null; //Formovna<br>
		listComponents[16][0] = null; //hmotnost<br>
		listComponents[17][0] = null; //is odhadova hmotnost<br>
		listComponents[18][0] = null; //norma slevac<br>
		listComponents[19][0] = textVyberZakaznika; //id zakaznika<br>
		listComponents[20][0] = uzavrenaCheck; // je uzavrena zakazka?<br>
		listComponents[21][0] = textPoznamka; //poznamka<br>
		listComponents[22][0] = textPaganyrka; //paganýrka<br>
	 *
	 */
	public JComponent[][] getListComponents() {
		return listComponents;
	}
	
	public DefaultListModel<DateStor> getModel(){
		return this.model;
	}
	
	public DefaultComboBoxModel<String> getZakaznikComboModel(){
		return this.zakaznikComboModel;
	}
	
	public DefaultComboBoxModel<String> getModelNoveZakazkyComboModel(){
		return this.modelComboModel;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comand = e.getActionCommand();
		try {
			try {
				if (comand.equalsIgnoreCase("PridejDilciTermin")) {
					Date novyDilciTermin = this.dateDilciTerminy.getDate();
					String pocet = pocetKusuDilciTermin.getText();
					int pocetKusu = Integer.parseInt(pocet);
					if(pocetKusu < 0){
						JOptionPane.showMessageDialog(hlavniOkno, "Poèet kusù nesmí být menší než nula");
						return;
					}
					DateStor pom;
					DateStor novyTermin = new DateStor(novyDilciTermin, pocetKusu, sdf, false);
					int i = 0;
					for(i = 0; i < model.size(); i++){
						pom = model.get(i);
						if(novyTermin.isEqualDate(pom)){
							model.remove(i);
							model.add(i, novyTermin);
							return;
						}
					}
					if(pocetKusu != 0){
						model.add(i, novyTermin);
					}
				} else if (comand.equalsIgnoreCase("SmazDilciTermin")) {
					int selected = this.seznamDilcichTerminu.getSelectedIndex();
					if (selected < 0 || selected > model.getSize()) {
						JOptionPane.showMessageDialog(hlavniOkno, "Vyberte nìjaký prvek z tabulky dílèích termínù");
						return;
					} else {
						model.remove(selected);
					}
				} else if (comand.equalsIgnoreCase("SmazDilciTerminUprava")){
					int selected = this.seznamDilcichTerminu.getSelectedIndex();
					if (selected < 0 || selected > model.getSize()) {
						JOptionPane.showMessageDialog(hlavniOkno, "Vyberte nìjaký prvek z tabulky dílèích termínù");
						return;
					}
					DateStor pom = model.get(selected);
					DateStor novyTermin = new DateStor(pom.getDate(), 0, sdf, false);

					if (novyTermin.isEqualDate(pom)) {
						model.remove(selected);
						model.add(selected, novyTermin);
					}

				}
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatnì zapsaný poèet kusù u dílèího termínu");
				return;
			}
		} catch (Exception e2) {
			ExceptionWin.showExceptionMessage(e2);

		}

	}
	
	
}
