package sablony;

import iListeners.HledejListener;
import iListeners.PanelFiltrListenerTextField;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JButton;

import app.MainFrame;

import com.toedter.calendar.JDateChooser;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import sablony.tabulka.ColorCellTable;
import sablony.tabulka.TableColumnAdjuster;
import storage.SkladOdkazu;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import javax.swing.JCheckBox;

import com.toedter.calendar.JYearChooser;

/**
 * Tøída, které rozšiøuje JPanel a reprezentuje oddíl v aplikaci, ve které vyplnujeme parametry pro vyhledávání.
 * Nastavují se zde actionComandy pro tisk a také pro vyhledávání zákazníku, modelu, zakázek, atd.
 * @author Ondøej Havlíèek
 *
 */
public class ParametryFiltr extends JPanel {
	
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame hlavniOkno;
	private SkladOdkazu sklad;
	
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
	private Font [] fonty ;
	
	private ColorCellTable table;
	private TableColumnAdjuster columAdjuster;
	
	private JTextField textField;
	private JTextField textField_1;
	private JTextField idZakazky;
	private JTextField textField_4;
	private JTextField textField_6;
	private JTextField textField_7;
	private JButton vyhledej;
	
	/**
	 *  pole[0] = Label Jmeno Zakaznika <br>
	 *  pole[1] = TextFiled Jmeno Zakaznika  <br>
	 *  pole[2] = Label Cislo modelu  <br>
	 *  pole[3] = JtextField Cislo modelu  <br>
	 *  pole[4] = Label Nazev modelu  <br>
	 *  pole[5] = JtextField Nazev modelu  <br>
	 *  pole[6] = Label id modelu  <br>
	 *  pole[7] = JtextField id modelu  <br>
	 *  pole[8] = Label datum pøijetí zakázky  <br>
	 *  pole[9] = DateChooser datum pøijeti zakazky  <br>
	 *  pole[10] = Label Formovna  <br>
	 *  pole[11] = ComboBox formovna  <br>
	 *  pole[12] = Label id Zakazky  <br>
	 *  pole[13] = JtextField id Zakazky  <br>
	 *  pole[14] = Label Èíslo objednávky  <br>
	 *  pole[15] = JtextField Èíslo objednávky  <br>
	 *  pole[16] = checkVcetneUzavZak
	 */
	private Component [] pole= new Component[17];
	
	/**
	 *  vypisy[0] = Label Datum od <br>
	 *  vypisy[1] = JDateChooser Datum od  <br>
	 *  vypisy[2] = Label Datum do  <br>
	 *  vypisy[3] = JDateChooser Datum do  <br>
	 *  vypisy[4] = Label Cislo tydne  <br>
	 *  vypisy[5] = JtextField Cislo tydne  <br>
	 *  vypisy[6] = yearChooser <br>
	 *  vypisy[7] = JtextField napoveda datum <br>
	 *  vypisy[8] = JtextField napoveda cislo tydne <br>
	 *  vypisy[9] = JButton Prevod do PDF  <br>
	 *  
	 */
	private Component [] vypisy = new Component[12];
	private short status = 0;
	private JPanel panel;
	private JLabel idZakazkyLabel;
	private JTextField textField_2;
	private JCheckBox checkVcetneUzavZak;
	private JYearChooser yearChooser;
	private JLabel napovedaDate;
	private JLabel napovedaCisloT;
	private JButton prevodDoPdf;
	private JLabel lblFormovna_1;
	private JComboBox comboBoxFormovna;
	
	public void addListeners(){
		PanelFiltrListenerTextField list = new PanelFiltrListenerTextField(sklad, pole, vypisy);
		for(int i = 0; i < pole.length;i++){
			if(pole[i] instanceof JTextField){
				((JTextField)pole[i]).getDocument().addDocumentListener(list);
			}
		}
		for(int i = 0; i < vypisy.length;i++){
			if(vypisy[i] instanceof JTextField){
				((JTextField)vypisy[i]).getDocument().addDocumentListener(list);
			}
		}
		
		HledejListener lt = new HledejListener(vyhledej, prevodDoPdf, this, table, pole, vypisy, hlavniOkno, columAdjuster);
		vyhledej.addActionListener(lt);
		vyhledej.addMouseListener(lt);
		prevodDoPdf.addActionListener(lt);
		prevodDoPdf.addMouseListener(lt);
	}
	
	public void setZakaznik(){
		if(status == 1){
			return;
		}
		GridBagLayout layout = (GridBagLayout) panel.getLayout();
		GridBagConstraints gbc = layout.getConstraints(vyhledej);				
		layout.removeLayoutComponent(vyhledej);
		gbc.gridx = 4;
		gbc.gridy = 0;
		panel.add(vyhledej, gbc);
		
		for(int i = 0; i < vypisy.length; i++){
			vypisy[i].setVisible(false);
		}	
		
		for(int i = 0; i < pole.length; i++){
			if(i == 0 || i == 1){
				pole[i].setVisible(true);		
				if(pole[i] instanceof JLabel){
					((JLabel)pole[i]).setText("Jm\u00E9no z\u00E1kazn\u00EDka:");
				}
				continue;
			}
			pole[i].setVisible(false);
		}	
		this.status = 1;
		vyhledej.setActionCommand("HledejZakazniky");
	}
	
	public void setModel(){
		if(status == 2){
			return;
		}
		for(int i = 0; i < vypisy.length; i++){
			vypisy[i].setVisible(false);
		}	
		
		if(status != 3){
			GridBagLayout layout = (GridBagLayout) panel.getLayout();
			GridBagConstraints gbc = layout.getConstraints(vyhledej);				
			layout.removeLayoutComponent(vyhledej);
			gbc.gridx = 0;
			gbc.gridy = 5;
			panel.add(vyhledej, gbc);
		}
		
		for(int i = 0; i < pole.length; i++){
			if(i == 14 || i == 15){
				pole[i].setVisible(false);
			}else {
				pole[i].setVisible(true);
			}
		}
		vyhledej.setActionCommand("HledejModely");
		
		this.status = 2;
		
	}
	
	public void setZakazka(){
		if(status == 3){
			return;
		}
		for(int i = 0; i < vypisy.length; i++){
			vypisy[i].setVisible(false);
		}	
		if(status != 2){
			GridBagLayout layout = (GridBagLayout) panel.getLayout();
			GridBagConstraints gbc = layout.getConstraints(vyhledej);				
			layout.removeLayoutComponent(vyhledej);
			gbc.gridx = 0;
			gbc.gridy = 5;
			panel.add(vyhledej, gbc);
		}
		
		for(int i = 0; i < pole.length; i++){
			if(i == 11 || i == 10){
				pole[i].setVisible(false);
			}else {
				pole[i].setVisible(true);
			}
		}
		
		vyhledej.setActionCommand("HledejZakazky");
		this.status = 3;
	}
	
	public void setZmetky(){
		setZakazka();
		status = 5;
		for(int i = 0; i < vypisy.length; i++){
			vypisy[i].setVisible(false);
		}	
		vyhledej.setActionCommand("HledejZmetky");
	}
	
	public void setFyzKusy(){
		if(status == 4){
			return;
		}		
		for(int i = 0; i < vypisy.length; i++){
			vypisy[i].setVisible(false);
		}	
		GridBagLayout layout = (GridBagLayout) panel.getLayout();
		GridBagConstraints gbc = layout.getConstraints(vyhledej);				
		layout.removeLayoutComponent(vyhledej);
		gbc.gridx = 4;
		gbc.gridy = 2;
		panel.add(vyhledej, gbc);
		
		for(int i = 0; i < pole.length; i++){
			if(i == 12 || i == 13){
				pole[i].setVisible(true);
				continue;			
			}
			pole[i].setVisible(false);
		}			
		
		this.status = 4;
		vyhledej.setActionCommand("HledejFyzKusy");
	}
	
	public void setViniky(){
		this.setZakaznik();
		((JLabel)pole[0]).setText("Jm\u00E9no viníka:");
		this.status = 8;
		vyhledej.setActionCommand("HledejViniky");
	}
	
	public void setVady(){
		this.setZakaznik();
		((JLabel)pole[0]).setText("Jm\u00E9no vady:");
		this.status = 8;
		vyhledej.setActionCommand("HledejVady");
	}
	
	public void setKapPropocet(){
		setPlanyLiti(true);
		prevodDoPdf.setVisible(false);
		this.lblFormovna_1.setVisible(true);
		this.comboBoxFormovna.setVisible(true);
		vyhledej.setActionCommand("HledejKapacitniProcet");
	}
	
	/**
	 * 
	 * @param isZakl
	 */
	public void setPlanyLiti(boolean isZakl){
		this.setZakazka();
		for(int i = 0; i < pole.length; i++){
			pole[i].setVisible(false);
		}
		status = 6;
		this.showCisloTydne();
		if(isZakl){
			vyhledej.setActionCommand("ZaklPlanLiti");
			prevodDoPdf.setActionCommand("PDFZaklPlan");
		}else {
			vyhledej.setActionCommand("PlanovaniLiti");
			prevodDoPdf.setActionCommand("PDFPlanovani");
		}
		comboBoxFormovna.setVisible(true);
		//if(isZakl){
			prevodDoPdf.setVisible(true); // uložit jako pdf
		//}
	}
	
	public void setPlanExpedice(int j){
		this.setZakazka();
		for(int i = 0; i < pole.length; i++){
			pole[i].setVisible(false);
		}
		String [] com = sklad.getCommands()[3];
		vyhledej.setActionCommand(com[j]);
		prevodDoPdf.setActionCommand("PDF"+com[j]);
		status = 9;	
		this.showNothing();
		prevodDoPdf.setVisible(true);
	}
	
	/**
	 * Je tu switch blok. Nastavi okno  v Podokne parametry-filtr a zobrazi jednolitve boxy podle cisla zavolaneho vypisu.
	 * Nastavi action comand Jbutonu vyhledej.
	 * @param j
	 */
	public void setVypisy(int j){
		this.setZakazka();
		for(int i = 0; i < pole.length; i++){
			pole[i].setVisible(false);
		}
		status = 6;
		
		String [] com = sklad.getCommands()[4];
		//nastavime ActionComandy, pro vyhledavani a pro tisk
		vyhledej.setActionCommand(com[j]);
		prevodDoPdf.setActionCommand("PDF"+com[j]);
		prevodDoPdf.setVisible(false); // uložit jako pdf
		switch(j){
		case 0: // 1.	Výpis stavu neuzavøené zakázky 
			this.setZakazka();
			status = 6;
			checkVcetneUzavZak.setVisible(false);
			// musim nastavit actionComand ještì jednou paè, setZakazka() se zmenil
			vyhledej.setActionCommand(com[j]); 
			prevodDoPdf.setVisible(true); // uložit jako excel
			this.napovedaDate.setText("Vypíše stav všech neuzavøených zakázek, (maximálnì ale 100 øádkù)");
			break;
		case 1: // 2.	Denní výpis odlitých kusù
			this.showOd();
			prevodDoPdf.setVisible(true);
			this.napovedaDate.setText("Denní výpis odlitkù");
			break;
		case 2: //3.	Výpis vyèištìných kusù za období
			this.showOdDo();
			prevodDoPdf.setVisible(true); // uložit jako excel
			//prevodDoPdf.setActionCommand("PDFSoucetHmotnostiNorem");
			this.napovedaDate.setText("Vypíše souèet hmotností a norem všech odlitkù, které se odlily za toto období");
			break;
		case 3: //4.	Mzdy slévaèù
			this.showOd();
			prevodDoPdf.setVisible(true);
			break;
		case 4: //5.	Výpis odlitkù v kg/Kè
			this.showOdDo();
			prevodDoPdf.setVisible(true);
			break;
		case 5: // 6.	Výpis odlitých (vyrobených) kusù za období
			this.showOdDo();
			prevodDoPdf.setVisible(false);
			break;
		case 6: //7.	Výpis položek s odhadovou hmotností
			this.showNothing();
			prevodDoPdf.setVisible(true);
			break;
		case 7: //8.	Výpis termínu expedice v daném tydnu
			this.showCisloTydne();
			prevodDoPdf.setVisible(false);
			//((JLabel)vypisy[0]).setText("Mezní datum expedice:");
			//this.napovedaDate.setText("Vypíše všechny zakázky, které nejsou uzavøené a maji po termíny expedice po daném datumu");
			break;
		case 8: //9.	Výpis expedice zboží za odbdobí
			this.showOdDo();
			prevodDoPdf.setVisible(true);
			break;
		case 9: //10.	Výpis zpoždìní výroby ke dni
			this.showDatum();
			prevodDoPdf.setVisible(false);
			break;
		case 10: //11.	Inventura rozpracované výroby
			this.showNothing();
			prevodDoPdf.setVisible(true);
			break;
		case 11: //12.	Výpis skladu ke dnešnímu dni
			this.showNothing();
			prevodDoPdf.setVisible(true);
			break;
		case 12: //13.	Výpis zmetku za období
			this.showOdDo();
			prevodDoPdf.setVisible(true);
			break;
		case 13: //14.	Výpis viníku v kg/kè za období
			this.showOdDo();
			prevodDoPdf.setVisible(true);
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Spatny vypis");
			break;
		}
	}
	
	private void showCisloTydne(){
		for(int i = 0; i < vypisy.length; i++)vypisy[i].setVisible(false);
		vypisy[4].setVisible(true);
		vypisy[5].setVisible(true);
		vypisy[6].setVisible(true);
		vypisy[8].setVisible(true);
	}
	
	private void showOdDo(){
		for(int i = 0; i < vypisy.length; i++)vypisy[i].setVisible(false);
		((JLabel)vypisy[0]).setText("Datum od:");
		vypisy[0].setVisible(true);
		vypisy[1].setVisible(true);
		vypisy[2].setVisible(true);
		vypisy[3].setVisible(true);
	}
	
	private void showOd(){
		for(int i = 0; i < vypisy.length; i++)vypisy[i].setVisible(false);
		((JLabel)vypisy[0]).setText("Datum od:");
		vypisy[0].setVisible(true);
		vypisy[1].setVisible(true);
	}
	
	private void showDatum(){
		for(int i = 0; i < vypisy.length; i++)vypisy[i].setVisible(false);
		vypisy[0].setVisible(true);
		vypisy[1].setVisible(true);	
		((JLabel)vypisy[0]).setText("Datum:");
	}
	
	private void showNothing(){
		for(int i = 0; i < vypisy.length; i++)vypisy[i].setVisible(false);
	}

	/**
	 * Create the panel.
	 */
	public ParametryFiltr(SkladOdkazu sklad, ColorCellTable table) {
		this.hlavniOkno = sklad.getHlavniOkno();
		this.sklad = sklad;
		this.table = table;
		this.barvy = sklad.getBarvy();
		this.fonty = sklad.getFonty();
		this.columAdjuster = sklad.getPromOknoNovyZakaznikAndSearchColumAdjuster();
		
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(barvy[0]);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		panel = new JPanel();
		add(panel);
		panel.setBackground(barvy[0]);
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[]{98, 58, 60, 40, 45, 0, 54, 49, 39, 0, 49, 42, 36, 0, 0};
		layout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		layout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(layout);
		
		JLabel lblNewLabel = new JLabel("Jm\u00E9no z\u00E1kazn\u00EDka:");
		pole[0] = lblNewLabel;
		lblNewLabel.setFont(fonty[4]);
		lblNewLabel.setForeground(barvy[11]);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		textField = new JTextField();
		pole[1] = textField;
		textField.setForeground(Color.WHITE);
		textField.setFont(fonty[4]);
		textField.setBorder(new EmptyBorder(2, 0, 2, 0));
		textField.setBackground(barvy[2]);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 3;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblsloModelu = new JLabel("\u010C\u00EDslo modelu");
		pole[2] = lblsloModelu;
		lblsloModelu.setForeground(barvy[11]);
		lblsloModelu.setFont(fonty[4]);
		GridBagConstraints gbc_lblsloModelu = new GridBagConstraints();
		gbc_lblsloModelu.anchor = GridBagConstraints.WEST;
		gbc_lblsloModelu.insets = new Insets(0, 0, 5, 5);
		gbc_lblsloModelu.gridx = 5;
		gbc_lblsloModelu.gridy = 0;
		panel.add(lblsloModelu, gbc_lblsloModelu);
		
		textField_4 = new JTextField();
		pole[3] = textField_4;
		textField_4.setForeground(Color.WHITE);
		textField_4.setFont(fonty[4]);
		textField_4.setColumns(10);
		textField_4.setBorder(null);
		textField_4.setBackground(barvy[2]);
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.gridwidth = 2;
		gbc_textField_4.insets = new Insets(0, 0, 5, 5);
		gbc_textField_4.fill = GridBagConstraints.BOTH;
		gbc_textField_4.gridx = 6;
		gbc_textField_4.gridy = 0;
		panel.add(textField_4, gbc_textField_4);
		
		JLabel lblNzevModelu = new JLabel("N\u00E1zev modelu");
		pole[4] = lblNzevModelu;
		lblNzevModelu.setForeground(barvy[11]);
		lblNzevModelu.setFont(fonty[4]);
		GridBagConstraints gbc_lblNzevModelu = new GridBagConstraints();
		gbc_lblNzevModelu.insets = new Insets(0, 0, 5, 5);
		gbc_lblNzevModelu.anchor = GridBagConstraints.WEST;
		gbc_lblNzevModelu.gridx = 9;
		gbc_lblNzevModelu.gridy = 0;
		panel.add(lblNzevModelu, gbc_lblNzevModelu);
		
		textField_7 = new JTextField();
		pole[5] = textField_7;
		textField_7.setForeground(Color.WHITE);
		textField_7.setFont(fonty[4]);
		textField_7.setColumns(10);
		textField_7.setBorder(null);
		textField_7.setBackground(barvy[2]);
		GridBagConstraints gbc_textField_7 = new GridBagConstraints();
		gbc_textField_7.gridwidth = 3;
		gbc_textField_7.insets = new Insets(0, 0, 5, 5);
		gbc_textField_7.fill = GridBagConstraints.BOTH;
		gbc_textField_7.gridx = 10;
		gbc_textField_7.gridy = 0;
		panel.add(textField_7, gbc_textField_7);
		
		JLabel lblIdModelu = new JLabel("Id modelu:");
		pole[6] = lblIdModelu;
		lblIdModelu.setForeground(barvy[11]);
		lblIdModelu.setFont(fonty[4]);
		GridBagConstraints gbc_lblIdModelu = new GridBagConstraints();
		gbc_lblIdModelu.anchor = GridBagConstraints.WEST;
		gbc_lblIdModelu.insets = new Insets(0, 0, 5, 5);
		gbc_lblIdModelu.gridx = 0;
		gbc_lblIdModelu.gridy = 1;
		panel.add(lblIdModelu, gbc_lblIdModelu);
		
		textField_1 = new JTextField();
		pole[7] = textField_1;
		textField_1.setForeground(Color.WHITE);
		textField_1.setFont(fonty[4]);
		textField_1.setColumns(10);
		textField_1.setBorder(null);
		textField_1.setBackground(barvy[2]);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 2;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.BOTH;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		panel.add(textField_1, gbc_textField_1);
		
		JLabel lblDatumPijetZakzky = new JLabel("Zak\u00E1zka p\u0159ijata po");
		pole[8] = lblDatumPijetZakzky;
		lblDatumPijetZakzky.setForeground(barvy[11]);
		lblDatumPijetZakzky.setFont(fonty[4]);
		GridBagConstraints gbc_lblDatumPijetZakzky = new GridBagConstraints();
		gbc_lblDatumPijetZakzky.anchor = GridBagConstraints.WEST;
		gbc_lblDatumPijetZakzky.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatumPijetZakzky.gridx = 5;
		gbc_lblDatumPijetZakzky.gridy = 1;
		panel.add(lblDatumPijetZakzky, gbc_lblDatumPijetZakzky);
		
		JDateChooser dateChooser = new JDateChooser();
		GridBagConstraints gbc_dateChooser = new GridBagConstraints();
		pole[9] = dateChooser;
		gbc_dateChooser.gridwidth = 2;
		gbc_dateChooser.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooser.fill = GridBagConstraints.BOTH;
		gbc_dateChooser.gridx = 6;
		gbc_dateChooser.gridy = 1;
		panel.add(dateChooser, gbc_dateChooser);
		
		JLabel lblFormovna = new JLabel("Formovna");
		pole[10] = lblFormovna;
		lblFormovna.setForeground(barvy[11]);
		lblFormovna.setFont(fonty[4]);
		GridBagConstraints gbc_lblFormovna = new GridBagConstraints();
		gbc_lblFormovna.anchor = GridBagConstraints.WEST;
		gbc_lblFormovna.insets = new Insets(0, 0, 5, 5);
		gbc_lblFormovna.gridx = 9;
		gbc_lblFormovna.gridy = 1;
		panel.add(lblFormovna, gbc_lblFormovna);
		
		JComboBox comboBox = new JComboBox();
		pole[11] = comboBox;
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"", "T", "S", "M"}));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 10;
		gbc_comboBox.gridy = 1;
		panel.add(comboBox, gbc_comboBox);
		
		idZakazkyLabel = new JLabel("Id zak\u00E1zky:");
		pole[12] = idZakazkyLabel;
		idZakazkyLabel.setForeground(barvy[11]);
		idZakazkyLabel.setFont(fonty[4]);
		GridBagConstraints gbc_lblIdZakzky = new GridBagConstraints();
		gbc_lblIdZakzky.anchor = GridBagConstraints.WEST;
		gbc_lblIdZakzky.insets = new Insets(0, 0, 5, 5);
		gbc_lblIdZakzky.gridx = 0;
		gbc_lblIdZakzky.gridy = 2;
		panel.add(idZakazkyLabel, gbc_lblIdZakzky);
		
		idZakazky = new JTextField();
		idZakazky.setBorder(new EmptyBorder(2, 0, 2, 0));
		pole[13] = idZakazky;
		idZakazky.setForeground(Color.WHITE);
		idZakazky.setFont(fonty[4]);
		idZakazky.setColumns(10);
		idZakazky.setBackground(barvy[2]);
		GridBagConstraints gbc_idZakazky = new GridBagConstraints();
		gbc_idZakazky.gridwidth = 2;
		gbc_idZakazky.insets = new Insets(0, 0, 5, 5);
		gbc_idZakazky.fill = GridBagConstraints.BOTH;
		gbc_idZakazky.gridx = 1;
		gbc_idZakazky.gridy = 2;
		panel.add(idZakazky, gbc_idZakazky);
		
		JLabel lblsloObjednvky = new JLabel("\u010C\u00EDslo objedn\u00E1vky");
		pole[14] = lblsloObjednvky;
		lblsloObjednvky.setForeground(barvy[11]);
		lblsloObjednvky.setFont(fonty[4]);
		GridBagConstraints gbc_lblsloObjednvky = new GridBagConstraints();
		gbc_lblsloObjednvky.anchor = GridBagConstraints.WEST;
		gbc_lblsloObjednvky.insets = new Insets(0, 0, 5, 5);
		gbc_lblsloObjednvky.gridx = 5;
		gbc_lblsloObjednvky.gridy = 2;
		panel.add(lblsloObjednvky, gbc_lblsloObjednvky);
		
		textField_6 = new JTextField();
		pole[15] = textField_6;
		textField_6.setForeground(Color.WHITE);
		textField_6.setFont(fonty[4]);
		textField_6.setColumns(10);
		textField_6.setBorder(null);
		textField_6.setBackground(barvy[2]);
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.gridwidth = 2;
		gbc_textField_6.insets = new Insets(0, 0, 5, 5);
		gbc_textField_6.fill = GridBagConstraints.BOTH;
		gbc_textField_6.gridx = 6;
		gbc_textField_6.gridy = 2;
		panel.add(textField_6, gbc_textField_6);
		
		checkVcetneUzavZak = new JCheckBox("Pouze uzav\u0159en\u00E9 zak\u00E1zky");
		checkVcetneUzavZak.setBorder(null);
		pole[16] = checkVcetneUzavZak;
		checkVcetneUzavZak.setFont(new Font("Tahoma", Font.PLAIN, 12));
		checkVcetneUzavZak.setForeground(barvy[11]);
		checkVcetneUzavZak.setBackground(barvy[0]);
		GridBagConstraints gbc_checkVcetneUzavZak = new GridBagConstraints();
		gbc_checkVcetneUzavZak.anchor = GridBagConstraints.WEST;
		gbc_checkVcetneUzavZak.gridwidth = 4;
		gbc_checkVcetneUzavZak.insets = new Insets(0, 0, 5, 5);
		gbc_checkVcetneUzavZak.gridx = 9;
		gbc_checkVcetneUzavZak.gridy = 2;
		panel.add(checkVcetneUzavZak, gbc_checkVcetneUzavZak);
		
		JLabel lblDatumOd = new JLabel("Datum od:");
		vypisy[0] = lblDatumOd;
		lblDatumOd.setForeground(barvy[11]);
		lblDatumOd.setFont(fonty[4]);
		GridBagConstraints gbc_lblDatumOd = new GridBagConstraints();
		gbc_lblDatumOd.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatumOd.anchor = GridBagConstraints.WEST;
		gbc_lblDatumOd.gridx = 0;
		gbc_lblDatumOd.gridy = 3;
		panel.add(lblDatumOd, gbc_lblDatumOd);
		
		JDateChooser dateChooser_1 = new MyJDateChooser();
		vypisy[1] = dateChooser_1;
		GridBagConstraints gbc_dateChooser_1 = new GridBagConstraints();
		gbc_dateChooser_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_dateChooser_1.gridwidth = 2;
		gbc_dateChooser_1.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooser_1.gridx = 1;
		gbc_dateChooser_1.gridy = 3;
		panel.add(dateChooser_1, gbc_dateChooser_1);
		
		JLabel lblDatumDo = new JLabel("Datum do:");
		vypisy[2] = lblDatumDo;
		lblDatumDo.setForeground(barvy[11]);
		lblDatumDo.setFont(fonty[4]);
		GridBagConstraints gbc_lblDatumDo = new GridBagConstraints();
		gbc_lblDatumDo.anchor = GridBagConstraints.WEST;
		gbc_lblDatumDo.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatumDo.gridx = 5;
		gbc_lblDatumDo.gridy = 3;
		panel.add(lblDatumDo, gbc_lblDatumDo);
		
		JDateChooser dateChooser_2 = new MyJDateChooser();
		vypisy[3] = dateChooser_2;
		GridBagConstraints gbc_dateChooser_2 = new GridBagConstraints();
		gbc_dateChooser_2.gridwidth = 2;
		gbc_dateChooser_2.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooser_2.fill = GridBagConstraints.BOTH;
		gbc_dateChooser_2.gridx = 6;
		gbc_dateChooser_2.gridy = 3;
		panel.add(dateChooser_2, gbc_dateChooser_2);
		
		napovedaDate = new JLabel("N\u00E1pov\u011Bda");
		vypisy[7] = napovedaDate;
		napovedaDate.setForeground(barvy[11]);
		napovedaDate.setFont(fonty[4]);
		GridBagConstraints gbc_napovedaDate = new GridBagConstraints();
		gbc_napovedaDate.gridwidth = 5;
		gbc_napovedaDate.anchor = GridBagConstraints.WEST;
		gbc_napovedaDate.insets = new Insets(0, 0, 5, 0);
		gbc_napovedaDate.gridx = 9;
		gbc_napovedaDate.gridy = 3;
		panel.add(napovedaDate, gbc_napovedaDate);
		
		JLabel lblsloTdne = new JLabel("\u010C\u00EDslo t\u00FDdne");
		vypisy[4] = lblsloTdne;
		lblsloTdne.setFont(fonty[4]);
		lblsloTdne.setForeground(barvy[11]);
		GridBagConstraints gbc_lblsloTdne = new GridBagConstraints();
		gbc_lblsloTdne.anchor = GridBagConstraints.WEST;
		gbc_lblsloTdne.fill = GridBagConstraints.VERTICAL;
		gbc_lblsloTdne.insets = new Insets(0, 0, 5, 5);
		gbc_lblsloTdne.gridx = 0;
		gbc_lblsloTdne.gridy = 4;
		panel.add(lblsloTdne, gbc_lblsloTdne);
		
		textField_2 = new JTextField();
		vypisy[5] = textField_2;
		textField_2.setForeground(Color.WHITE);
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_2.setColumns(10);
		textField_2.setBorder(new EmptyBorder(2, 0, 2, 0));
		textField_2.setBackground(new Color(98, 98, 98));
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 4;
		panel.add(textField_2, gbc_textField_2);
		
		yearChooser = new JYearChooser();
		vypisy[6] = yearChooser;
		GridBagConstraints gbc_yearChooser = new GridBagConstraints();
		gbc_yearChooser.insets = new Insets(0, 0, 5, 5);
		gbc_yearChooser.fill = GridBagConstraints.BOTH;
		gbc_yearChooser.gridx = 2;
		gbc_yearChooser.gridy = 4;
		panel.add(yearChooser, gbc_yearChooser);
		
		lblFormovna_1 = new JLabel("Formovna:");
		vypisy[10] = lblFormovna_1;
		lblFormovna_1.setForeground(new Color(246, 246, 246));
		lblFormovna_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_lblFormovna_1 = new GridBagConstraints();
		gbc_lblFormovna_1.anchor = GridBagConstraints.WEST;
		gbc_lblFormovna_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblFormovna_1.gridx = 5;
		gbc_lblFormovna_1.gridy = 4;
		panel.add(lblFormovna_1, gbc_lblFormovna_1);
		
		comboBoxFormovna = new JComboBox();
		vypisy[11] = comboBoxFormovna;
		comboBoxFormovna.setModel(new DefaultComboBoxModel(new String[] {"T", "S", "M"}));
		GridBagConstraints gbc_comboBoxFormovna = new GridBagConstraints();
		gbc_comboBoxFormovna.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxFormovna.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFormovna.gridx = 6;
		gbc_comboBoxFormovna.gridy = 4;
		panel.add(comboBoxFormovna, gbc_comboBoxFormovna);
		
		napovedaCisloT = new JLabel("N\u00E1pov\u011Bda");
		vypisy[8] = napovedaCisloT;
		napovedaCisloT.setForeground(new Color(246, 246, 246));
		napovedaCisloT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_napovedaCisloT = new GridBagConstraints();
		gbc_napovedaCisloT.anchor = GridBagConstraints.WEST;
		gbc_napovedaCisloT.gridwidth = 6;
		gbc_napovedaCisloT.insets = new Insets(0, 0, 5, 0);
		gbc_napovedaCisloT.gridx = 8;
		gbc_napovedaCisloT.gridy = 4;
		panel.add(napovedaCisloT, gbc_napovedaCisloT);
		
		vyhledej = new MyJButton("Vyhledat", 13,4, sklad);
		vyhledej.removeMouseListener(sklad.getMyJButonnListener());
		vyhledej.setPreferredSize(new Dimension(100, 22));
		vyhledej.setBorder(new LineBorder(barvy[15]));
		GridBagConstraints gbc_vyhledej = new GridBagConstraints();
		gbc_vyhledej.insets = new Insets(0, 0, 5, 5);
		gbc_vyhledej.fill = GridBagConstraints.BOTH;
		gbc_vyhledej.gridx = 0;
		gbc_vyhledej.gridy = 5;
		panel.add(vyhledej, gbc_vyhledej);
		
		//prevodDoPdf = new JButton("Ulo\u017Eit jako PDF");
		prevodDoPdf = new MyJButton("P\u0159evod do Excelu", 13,4, sklad);
		prevodDoPdf.setActionCommand("PDFuloz");
		prevodDoPdf.removeMouseListener(sklad.getMyJButonnListener());
		prevodDoPdf.setPreferredSize(new Dimension(150, 22));
		prevodDoPdf.setBorder(new LineBorder(barvy[15]));
		vypisy[9] = prevodDoPdf;
		GridBagConstraints gbc_prevodDoPdf = new GridBagConstraints();
		gbc_prevodDoPdf.anchor = GridBagConstraints.WEST;
		gbc_prevodDoPdf.gridwidth = 3;
		gbc_prevodDoPdf.fill = GridBagConstraints.VERTICAL;
		gbc_prevodDoPdf.insets = new Insets(0, 0, 5, 5);
		gbc_prevodDoPdf.gridx = 5;
		gbc_prevodDoPdf.gridy = 5;
		panel.add(prevodDoPdf, gbc_prevodDoPdf);
		
		
		addListeners();
		setZakaznik();
	}	
}
