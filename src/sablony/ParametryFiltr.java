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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JCheckBox;

import com.toedter.calendar.JYearChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Tøída, které rozšiøuje JPanel a reprezentuje oddíl v aplikaci, ve které vyplnujeme parametry pro vyhledávání.
 * Nastavují se zde actionComandy pro tisk a také pro vyhledávání zákazníku, modelu, zakázek, atd.
 * Každý component v tomto JPanelu musí být umísten v <code>componentyFiltru</code>.
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
	
	private DefaultComboBoxModel<String> vlastniMaterialySeznamModel;
	private DefaultComboBoxModel<String> seznamFormoven;
	private DefaultComboBoxModel<String> seznamFormovenAPrazdny;
	
	private MyPopUp seznamVlMater;
	
	private ColorCellTable table;
	private TableColumnAdjuster columAdjuster;
	
	private JTextField jmenoZakaznikaText;
	private JTextField idModeluText;
	private JTextField idZakazky;
	private JTextField cisloModeluText;
	private JTextField cisloObjednavkyText;
	private JTextField nazevModeluText;
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
	 *  pole[16] = checkVcetneUzavZak <br>
	 *  pole[17] = vlMaterialLabel
	 */
	private Component [] pole= new Component[18];
	
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
	 *  ... <br>
	 *  vypisy[12] = JComboBox Vlastni material
	 *  
	 */
	private Component [] vypisy = new Component[15];
	private JPanel panel;
	private JLabel idZakazkyLabel;
	private JTextField cisloTydneText;
	private JCheckBox checkVcetneUzavZak;
	private JYearChooser yearChooser;
	private JLabel napovedaDate;
	private JLabel napovedaCisloT;
	private JButton prevodDoPdf;
	private JLabel formovnaLabel2;
	private JLabel vlMaterialLabel;
	private JButton vlMaterialJbutton;
	private JDateChooser datumZakazkyDateChooser;
	/**
	 * Slouží pro hledaní pøi výpisech
	 */
	private JComboBox<String> comboBoxFormovna2;
	/**
	 * Slouží pøi hledání pøi vyhledávání princip stejny pro hledani modelu
	 */
	private JComboBox<String> formovnaComboBox1;
	private JDateChooser odDatum;
	private JDateChooser doDatum;
	
	
	public final String [] actionComands = {"HledejZakazniky", "HledejModely",
			"HledejZakazky", "HledejFyzKusy", "HledejZmetky", "HledejViniky",
			"HledejVady", "ZaklPlanLiti", "PlanovaniLiti", "HledejKapacitniProcet",
			"PlanExpedice",
			"VypisStavNeuzavrenychZakazek","DenniVypisOdlitychKusu",
			"VypisVycistenychKusuZaObdobi", "MzdySlevacu", "VypisOdlitkuVKgKc",
			"VypisOdlitychKusuOdDo", "VypisPolozekSOdhadHmot", "VypisDleTerminuExpedice",
			"VypisExpedice od-do", "VypisZpozdeneVyroby", "InventuraRozpracVyroby",
			"VypisSkladuKeDnesnimuDni", "VypisZmetkuZaObdobi", "VypisVinikuVKgKcMzdy",
			"VypisStavuZakazek", "VypisVytizeniKapacit"};
	
	private final int [] cisloAkceToStav ={FILTR_HLEDEJ_ZAKAZNIKY, FILTR_HLEDEJ_MODELY,
			FILTR_HLEDEJ_ZAKAZKY, FILTR_HLEDEJ_FYZ_KUSY, FILTR_HLEDEJ_ZMETKY, FILTR_HLEDEJ_VINIKY,
			FILTR_HLEDEJ_VADY, FILTR_CISLO_TYDNE_CISLO_ROKU_FORMOVNA_PDF, FILTR_CISLO_TYDNE_CISLO_ROKU_FORMOVNA_PDF,
			FILTR_CISLO_TYDNE_CISLO_ROKU_FORMOVNA, FILTR_PRAZDNY_PDF,
			// vypisy
			FILTR_HLEDEJZAKAZKY_BEZ_UZAVRENO_PDF, FILTR_DATUM_OD_PDF, FILTR_DATUM_OD_DO_PDF, FILTR_DATUM_OD_DO_PDF,
			FILTR_DATUM_OD_DO_PDF, FILTR_VYPIS_ODLITYCH_VYROBENYCH_KUSU_PDF, FILTR_PRAZDNY_PDF,
			FILTR_POUZE_CISLO_TYDNE_CISLO_ROKU, FILTR_DATUM_OD_DO_PDF, FILTR_DATUM_OD,
			FILTR_PRAZDNY_PDF, FILTR_PRAZDNY_PDF, FILTR_DATUM_OD_DO_PDF, FILTR_DATUM_OD_DO_PDF,
			FILTR_HLEDEJ_STAV_ZAKAZEK, FILTR_VYTIZENI_KAPACIT_PDF};
	
	/**
	 * Seznam všech výpisù a hledání, pomocí kterých získám jednoznaèný stav v ParametryFiltr ktery chci a taky
	 * získám ActionCommand podle tohoto Seznamu. Action comandy jsou seøazeny podle techto indexu tak je moc nemenit!
	 */
	public static final int HledejZakazniky = 0;
	public static final int HledejModely = 1;
	public static final int HledejZakazky =2;
	public static final int HledejFyzKusy = 3;
	public static final int HledejZmetky = 4;
	public static final int HledejViniky = 5;
	public static final int HledejVady = 6;
	public static final int ZaklPlanLiti = 7;
	public static final int PlanovaniLiti = 8;
	public static final int HledejKapacitniProcet = 9;
	public static final int PlanExpedice = 10;
	public static final int VypisStavNeuzavrenychZakazek = 11;
	public static final int DenniVypisOdlitychKusu = 12;
	public static final int VypisVycistenychKusuZaObdobi = 13;
	public static final int MzdySlevacu = 14;
	public static final int VypisOdlitkuVKgKc = 15;
	public static final int VypisOdlitychKusuOdDo = 16;
	public static final int VypisPolozekSOdhadHmot = 17;
	public static final int VypisDleTerminuExpedice = 18;
	public static final int VypisExpedice_od_do = 19;
	public static final int VypisZpozdeneVyroby = 20;
	public static final int InventuraRozpracVyroby = 21;
	public static final int VypisSkladuKeDnesnimuDni = 22;
	public static final int VypisZmetkuZaObdobi = 23;
	public static final int VypisVinikuVKgKcMzdy = 24;
	public static final int VypisStavuZakazek = 25;
	public static final int VypisVytizeniKapacit = 26;

	/**
	 * Stavy ParametryFiltr. Slouží pro jednoznaèné urèení rozložení komponentù v ParametryFiltr.
	 */
	private static final int FILTR_HLEDEJ_ZAKAZNIKY = 1;
	private static final int FILTR_HLEDEJ_MODELY = 2;
	private static final int FILTR_HLEDEJ_ZAKAZKY = 3;
	private static final int FILTR_HLEDEJ_FYZ_KUSY = 4;
	private static final int FILTR_HLEDEJ_ZMETKY = 5;
	private static final int FILTR_HLEDEJ_VINIKY = 6;
	private static final int FILTR_HLEDEJ_VADY = 7;
	private static final int FILTR_HLEDEJZAKAZKY_BEZ_UZAVRENO_PDF = 8;
	private static final int FILTR_POUZE_CISLO_TYDNE_CISLO_ROKU_PDF = 9;
	private static final int FILTR_POUZE_CISLO_TYDNE_CISLO_ROKU = 10;
	private static final int FILTR_DATUM_OD = 11;
	private static final int FILTR_DATUM_OD_PDF = 12;
	private static final int FILTR_DATUM_OD_DO_PDF = 13;
	private static final int FILTR_PRAZDNY_PDF = 14;
	private static final int FILTR_VYPIS_ODLITYCH_VYROBENYCH_KUSU_PDF = 15;
	private static final int FILTR_CISLO_TYDNE_CISLO_ROKU_FORMOVNA = 16;
	private static final int FILTR_CISLO_TYDNE_CISLO_ROKU_FORMOVNA_PDF = 17;
	private static final int FILTR_HLEDEJ_STAV_ZAKAZEK = 18;
	private static final int FILTR_VYTIZENI_KAPACIT_PDF = 19;

	
	private int stavAktualni = -1;
	
	private JLabel jmenoZakaznikaLabelOrElse;
	private Component [] showZakazkyComponents = new Component [15];
	private JLabel formovnaLabel1;
	private JLabel cisloObjednavkyLabel;
	private JLabel cisloTydneLabel;
	private JLabel dateOdLabel;
	private JLabel dateDoLabel;
	private JLabel kapacityLabel;
	private JTextField kapacityTextField;
	
	/**
	 * Výpisy se mužou dìlat tady, ale vìtšina se dìlá v setVypisy()
	 * @param cisloAkce
	 */
	public void setParametryFiltr(int cisloAkce){
		String comand = actionComands[cisloAkce];
		int stav =  cisloAkceToStav[cisloAkce];
		this.setParametryFiltrState(stav, comand, cisloAkce);
	}
	
	public void setVypisy(int j){ // abych to nemusel psat cely rucne v PromOknoNovyZakaznikAndSearch
		this.setParametryFiltr(VypisStavNeuzavrenychZakazek + j);
	}
	
	private void setParametryFiltrState(int stav, String actionCommand, int cisloAkce){
		// vsechny komponenty nejdøív dam neviditelne a pak podle stav je zobrazuju a pøehazuju
		Component [] componentyVeFiltru = panel.getComponents();
		for(int i = 0; i < componentyVeFiltru.length; i++){
			componentyVeFiltru[i].setVisible(false);
		}
		// nastavim action comandy pro "vyhledej" a "prevodDoPdf" JButtony
		this.setActionComands(actionCommand, vyhledej, prevodDoPdf);
		// poskládám a upravím componenty podle stavu
		poskladejAUpravComponenty(stav);
		// uprav texty na komponentech
		upravTexty(cisloAkce);
		// zobrazím všechny komponenty podle stavu
		zobrazComponenty(stav);
	}
	
	private void setActionComands(String actionCommand, JButton vyhledej, JButton prevodDoPdf){
		vyhledej.setActionCommand(actionCommand);
		prevodDoPdf.setActionCommand("PDF" + actionCommand);
	}
	
	/**
	 * V podstatì tahle metoda jen pøesouvá Jbutton vyhledej.
	 * @param stav
	 */
	private void poskladejAUpravComponenty(int stav){
		GridBagLayout layout = null;
		GridBagConstraints gbc = null;
		// defaultnì je comboBoxFormovna2 pouze {T,M,S}
		comboBoxFormovna2.setModel(seznamFormoven);
		switch(stav){
		case FILTR_HLEDEJ_ZAKAZNIKY:
			pomMetoda2();
			break;
		case FILTR_HLEDEJ_MODELY:
			pomMetoda();
			break;
		case FILTR_HLEDEJ_ZAKAZKY:
			pomMetoda();
			break;
		case FILTR_HLEDEJ_FYZ_KUSY:
			layout = (GridBagLayout) panel.getLayout();
			gbc = layout.getConstraints(vyhledej);				
			layout.removeLayoutComponent(vyhledej);
			gbc.gridx = 4;
			gbc.gridy = 2;
			panel.add(vyhledej, gbc);
			break;
		case FILTR_HLEDEJ_ZMETKY: // stejny jako hledej zakazky
			pomMetoda();
			break;
		case FILTR_HLEDEJ_VINIKY:
			pomMetoda2();
			break;
		case FILTR_HLEDEJ_VADY:
			pomMetoda2();
			break;
		case FILTR_HLEDEJZAKAZKY_BEZ_UZAVRENO_PDF:
			pomMetoda();
			break;
		case FILTR_POUZE_CISLO_TYDNE_CISLO_ROKU_PDF:
			pomMetoda();
			break;
		case FILTR_POUZE_CISLO_TYDNE_CISLO_ROKU:
			pomMetoda();
			break;
		case FILTR_DATUM_OD:
			pomMetoda();
			break;
		case FILTR_DATUM_OD_PDF:
			pomMetoda();
			break;
		case FILTR_DATUM_OD_DO_PDF:
			pomMetoda();
			break;
		case FILTR_PRAZDNY_PDF:
			pomMetoda();
			break;
		case FILTR_VYPIS_ODLITYCH_VYROBENYCH_KUSU_PDF: // budou sice dva komponenty v jedne bunce ale nikdy nebudou visible oba dva najednou
			comboBoxFormovna2.setModel(seznamFormovenAPrazdny); // jediny vypis zatim, ktery muže vybírat i prázdné pole fomovny
			layout = (GridBagLayout) panel.getLayout();
			gbc = null;
			
			gbc = layout.getConstraints(vlMaterialLabel);				
			layout.removeLayoutComponent(vlMaterialLabel);
			gbc.gridx = 0;
			gbc.gridy = 4;
			panel.add(vlMaterialLabel, gbc);
			
			gbc = layout.getConstraints(vlMaterialJbutton);				
			layout.removeLayoutComponent(vlMaterialJbutton);
			gbc.gridx = 1;
			gbc.gridy = 4;
			panel.add(vlMaterialJbutton, gbc);
			break;
		case FILTR_CISLO_TYDNE_CISLO_ROKU_FORMOVNA:
			pomMetoda();
			break;
		case FILTR_CISLO_TYDNE_CISLO_ROKU_FORMOVNA_PDF:
			pomMetoda();
			break;
		case FILTR_HLEDEJ_STAV_ZAKAZEK:
			pomMetoda();
			break;
		case FILTR_VYTIZENI_KAPACIT_PDF:
			pomMetoda();
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Spatny vypis, ParametryFiltr.java -> poskladejComponenty() "+stav);
			break;
		}
	}
	
	private void pomMetoda(){
		GridBagLayout layout = null;
		GridBagConstraints gbc = null;
		layout = (GridBagLayout) panel.getLayout();
		gbc = layout.getConstraints(vyhledej);
		layout.removeLayoutComponent(vyhledej);
		gbc.gridx = 0;
		gbc.gridy = 5;
		panel.add(vyhledej, gbc);
	}
	
	private void pomMetoda2(){
		GridBagLayout layout = (GridBagLayout) panel.getLayout();
		GridBagConstraints gbc = layout.getConstraints(vyhledej);				
		layout.removeLayoutComponent(vyhledej);
		gbc.gridx = 4;
		gbc.gridy = 0;
		panel.add(vyhledej, gbc);
	}
	
	private void zobrazComponenty(int stav){
		vyhledej.setVisible(true); // ten je visible dycky
		switch(stav){
		case FILTR_HLEDEJ_ZAKAZNIKY:
			jmenoZakaznikaLabelOrElse.setVisible(true);
			jmenoZakaznikaText.setVisible(true);			
			break;
		case FILTR_HLEDEJ_MODELY:
			for(int i = 0; i < showZakazkyComponents.length; i++){
				showZakazkyComponents[i].setVisible(true);
			}
			this.formovnaLabel1.setVisible(true);
			this.formovnaComboBox1.setVisible(true);
			this.cisloObjednavkyLabel.setVisible(false);
			this.cisloObjednavkyText.setVisible(false);
			break;
		case FILTR_HLEDEJ_ZAKAZKY:
			for(int i = 0; i < showZakazkyComponents.length; i++){
				showZakazkyComponents[i].setVisible(true);
			}
			break;
		case FILTR_HLEDEJ_FYZ_KUSY:
			this.idZakazky.setVisible(true);
			this.idZakazkyLabel.setVisible(true);
			break;
		case FILTR_HLEDEJ_ZMETKY:
			for(int i = 0; i < showZakazkyComponents.length; i++){
				showZakazkyComponents[i].setVisible(true);
			}
			break;
		case FILTR_HLEDEJ_VINIKY:
			jmenoZakaznikaLabelOrElse.setVisible(true);
			jmenoZakaznikaText.setVisible(true);	
			break;
		case FILTR_HLEDEJ_VADY:
			jmenoZakaznikaLabelOrElse.setVisible(true);
			jmenoZakaznikaText.setVisible(true);	
			break;
		case FILTR_HLEDEJZAKAZKY_BEZ_UZAVRENO_PDF:
			for(int i = 0; i < showZakazkyComponents.length; i++){
				showZakazkyComponents[i].setVisible(true);
			}
			checkVcetneUzavZak.setVisible(false);
			prevodDoPdf.setVisible(true);
			break;
		case FILTR_POUZE_CISLO_TYDNE_CISLO_ROKU_PDF:
			cisloTydneLabel.setVisible(true);
			cisloTydneText.setVisible(true);
			yearChooser.setVisible(true);
			prevodDoPdf.setVisible(true);
			break;
		case FILTR_POUZE_CISLO_TYDNE_CISLO_ROKU:
			cisloTydneLabel.setVisible(true);
			cisloTydneText.setVisible(true);
			yearChooser.setVisible(true);
			break;
		case FILTR_DATUM_OD:
			dateOdLabel.setVisible(true);
			odDatum.setVisible(true);
			break;
		case FILTR_DATUM_OD_PDF:
			dateOdLabel.setVisible(true);
			odDatum.setVisible(true);
			prevodDoPdf.setVisible(true);
			break;
		case FILTR_DATUM_OD_DO_PDF:
			dateOdLabel.setVisible(true);
			odDatum.setVisible(true);
			dateDoLabel.setVisible(true);
			doDatum.setVisible(true);
			prevodDoPdf.setVisible(true);
			break;
		case FILTR_PRAZDNY_PDF:
			prevodDoPdf.setVisible(true);
			break;
		case FILTR_VYPIS_ODLITYCH_VYROBENYCH_KUSU_PDF:
			dateOdLabel.setVisible(true);
			odDatum.setVisible(true);
			dateDoLabel.setVisible(true);
			doDatum.setVisible(true);
			prevodDoPdf.setVisible(true);
	
			vlMaterialJbutton.setVisible(true);
			vlMaterialLabel.setVisible(true);
			comboBoxFormovna2.setVisible(true);
			formovnaLabel2.setVisible(true);
			break;
		case FILTR_CISLO_TYDNE_CISLO_ROKU_FORMOVNA:
			cisloTydneLabel.setVisible(true);
			cisloTydneText.setVisible(true);
			yearChooser.setVisible(true);
			
			comboBoxFormovna2.setVisible(true);
			formovnaLabel2.setVisible(true);
			break;
		case FILTR_CISLO_TYDNE_CISLO_ROKU_FORMOVNA_PDF:
			cisloTydneLabel.setVisible(true);
			cisloTydneText.setVisible(true);
			yearChooser.setVisible(true);
			
			comboBoxFormovna2.setVisible(true);
			formovnaLabel2.setVisible(true);
			prevodDoPdf.setVisible(true);
			break;
		case FILTR_HLEDEJ_STAV_ZAKAZEK:
			for(int i = 0; i < showZakazkyComponents.length; i++){
				showZakazkyComponents[i].setVisible(true);
			}
			checkVcetneUzavZak.setVisible(false);
			this.cisloObjednavkyLabel.setVisible(false);
			this.cisloObjednavkyText.setVisible(false);
			this.idZakazky.setVisible(false);
			this.idZakazkyLabel.setVisible(false);
			break;
		case FILTR_VYTIZENI_KAPACIT_PDF:
			cisloTydneLabel.setVisible(true);
			cisloTydneText.setVisible(true);
			yearChooser.setVisible(true);
			
			prevodDoPdf.setVisible(true);
			// kapacity textbox
			kapacityTextField.setVisible(true);
			kapacityLabel.setVisible(true);
			napovedaCisloT.setVisible(true);
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Spatny vypis, ParametryFiltr.java -> poskladejComponenty() "+stav);
			break;
		}
	}
	
	private void upravTexty(int cisloAkce){
		switch(cisloAkce){
		case HledejZakazniky:
			jmenoZakaznikaLabelOrElse.setText("Jm\u00E9no z\u00E1kazn\u00EDka:");
			break;
		case HledejModely:
			jmenoZakaznikaLabelOrElse.setText("Jm\u00E9no z\u00E1kazn\u00EDka:");
			break;
		case HledejZakazky:
			jmenoZakaznikaLabelOrElse.setText("Jm\u00E9no z\u00E1kazn\u00EDka:");
			break;
		case HledejFyzKusy:
			break;
		case HledejZmetky:
			jmenoZakaznikaLabelOrElse.setText("Jm\u00E9no z\u00E1kazn\u00EDka:");
			break;
		case HledejViniky:
			jmenoZakaznikaLabelOrElse.setText("Jm\u00E9no viníka:");
			break;
		case HledejVady:
			jmenoZakaznikaLabelOrElse.setText("Jm\u00E9no vady:");
			break;
		case ZaklPlanLiti:
			break;
		case PlanovaniLiti:
			break;
		case HledejKapacitniProcet:
			break;
		case PlanExpedice:
			break;
		case VypisStavNeuzavrenychZakazek:
			napovedaDate.setText("Vypíše stav všech neuzavøených zakázek, (maximálnì ale 100 øádkù)");
			break;
		case DenniVypisOdlitychKusu:
			break;
		case VypisVycistenychKusuZaObdobi:
			break;
		case MzdySlevacu:
			break;
		case VypisOdlitkuVKgKc:
			break;
		case VypisOdlitychKusuOdDo:
			break;
		case VypisPolozekSOdhadHmot:
			break;
		case VypisDleTerminuExpedice:
			break;
		case VypisExpedice_od_do:
			break;
		case VypisZpozdeneVyroby:
			break;
		case InventuraRozpracVyroby:
			break;
		case VypisSkladuKeDnesnimuDni:
			break;
		case VypisZmetkuZaObdobi:
			break;
		case VypisVinikuVKgKcMzdy:
			break;
		case VypisStavuZakazek:
			break;
		case VypisVytizeniKapacit:
			napovedaCisloT.setText("Vypíše se maximálnì prvních 10 týdnù pro každou formovnu");
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Spatny vypis, ParametryFiltr.java -> upravTexty() " + cisloAkce);
			break;
		}
	}
	
	
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
		
		HledejListener lt = new HledejListener(this, table, hlavniOkno, columAdjuster, actionComands);
		vyhledej.addActionListener(lt);
		vyhledej.addMouseListener(lt);
		prevodDoPdf.addActionListener(lt);
		prevodDoPdf.addMouseListener(lt);
		vlMaterialJbutton.addMouseListener(lt);
	}
	
	/**
	 * Create the panel.
	 * @throws SQLException 
	 */
	public ParametryFiltr(SkladOdkazu sklad, ColorCellTable table) throws SQLException {
		this.hlavniOkno = sklad.getHlavniOkno();
		this.sklad = sklad;
		this.table = table;
		this.barvy = sklad.getBarvy();
		this.fonty = sklad.getFonty();
		this.columAdjuster = sklad.getPromOknoNovyZakaznikAndSearchColumAdjuster();
		
		/**
		 * Duležité možná èasem umístím do threadu!!!! ale asi ne probíha to pøi vytvaøení GUI takže to je v pozadí
		 */
		ResultSet rs = sklad.getSql().vyberVlastniMaterialy();
		vlastniMaterialySeznamModel = this.createComboBoxListModelFromResultSet(rs);
		
		seznamVlMater = MyPopUp.createPopupResultSetVlastniMaterialy(rs);
		
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(barvy[0]);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		panel = new JPanel();
		add(panel);
		panel.setBackground(barvy[0]);
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[]{98, 58, 60, 40, 45, 0, 54, 49, 39, 0, 49, 42, 36, 0, 0};
		layout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		layout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(layout);
		
		jmenoZakaznikaLabelOrElse = new JLabel("Jm\u00E9no z\u00E1kazn\u00EDka:");
		pole[0] = jmenoZakaznikaLabelOrElse;
		showZakazkyComponents[0] = jmenoZakaznikaLabelOrElse;
		jmenoZakaznikaLabelOrElse.setFont(fonty[4]);
		jmenoZakaznikaLabelOrElse.setForeground(barvy[11]);
		GridBagConstraints gbc_jmenoZakaznikaLabelOrElse = new GridBagConstraints();
		gbc_jmenoZakaznikaLabelOrElse.anchor = GridBagConstraints.WEST;
		gbc_jmenoZakaznikaLabelOrElse.insets = new Insets(0, 0, 5, 5);
		gbc_jmenoZakaznikaLabelOrElse.gridx = 0;
		gbc_jmenoZakaznikaLabelOrElse.gridy = 0;
		panel.add(jmenoZakaznikaLabelOrElse, gbc_jmenoZakaznikaLabelOrElse);
		
		jmenoZakaznikaText = new JTextField();
		pole[1] = jmenoZakaznikaText;
		showZakazkyComponents[1] = jmenoZakaznikaText;
		jmenoZakaznikaText.setForeground(Color.WHITE);
		jmenoZakaznikaText.setFont(fonty[4]);
		jmenoZakaznikaText.setBorder(new EmptyBorder(2, 0, 2, 0));
		jmenoZakaznikaText.setBackground(barvy[20]);
		GridBagConstraints gbc_jmenoZakaznikaText = new GridBagConstraints();
		gbc_jmenoZakaznikaText.gridwidth = 3;
		gbc_jmenoZakaznikaText.insets = new Insets(0, 0, 5, 5);
		gbc_jmenoZakaznikaText.fill = GridBagConstraints.BOTH;
		gbc_jmenoZakaznikaText.gridx = 1;
		gbc_jmenoZakaznikaText.gridy = 0;
		panel.add(jmenoZakaznikaText, gbc_jmenoZakaznikaText);
		jmenoZakaznikaText.setColumns(10);
		
		JLabel cisloModeluLabel = new JLabel("\u010C\u00EDslo modelu");
		pole[2] = cisloModeluLabel;
		showZakazkyComponents[2] = cisloModeluLabel;
		cisloModeluLabel.setForeground(barvy[11]);
		cisloModeluLabel.setFont(fonty[4]);
		GridBagConstraints gbc_cisloModeluLabel = new GridBagConstraints();
		gbc_cisloModeluLabel.anchor = GridBagConstraints.WEST;
		gbc_cisloModeluLabel.insets = new Insets(0, 0, 5, 5);
		gbc_cisloModeluLabel.gridx = 5;
		gbc_cisloModeluLabel.gridy = 0;
		panel.add(cisloModeluLabel, gbc_cisloModeluLabel);
		
		cisloModeluText = new JTextField();
		pole[3] = cisloModeluText;
		showZakazkyComponents[3] = cisloModeluText;
		cisloModeluText.setForeground(Color.WHITE);
		cisloModeluText.setFont(fonty[4]);
		cisloModeluText.setColumns(10);
		cisloModeluText.setBorder(null);
		cisloModeluText.setBackground(barvy[20]);
		GridBagConstraints gbc_cisloModeluText = new GridBagConstraints();
		gbc_cisloModeluText.gridwidth = 2;
		gbc_cisloModeluText.insets = new Insets(0, 0, 5, 5);
		gbc_cisloModeluText.fill = GridBagConstraints.BOTH;
		gbc_cisloModeluText.gridx = 6;
		gbc_cisloModeluText.gridy = 0;
		panel.add(cisloModeluText, gbc_cisloModeluText);
		
		JLabel nazevModeluLabel = new JLabel("N\u00E1zev modelu");
		pole[4] = nazevModeluLabel;
		showZakazkyComponents[4] = nazevModeluLabel;
		nazevModeluLabel.setForeground(barvy[11]);
		nazevModeluLabel.setFont(fonty[4]);
		GridBagConstraints gbc_nazevModeluLabel = new GridBagConstraints();
		gbc_nazevModeluLabel.insets = new Insets(0, 0, 5, 5);
		gbc_nazevModeluLabel.anchor = GridBagConstraints.WEST;
		gbc_nazevModeluLabel.gridx = 9;
		gbc_nazevModeluLabel.gridy = 0;
		panel.add(nazevModeluLabel, gbc_nazevModeluLabel);
		
		nazevModeluText = new JTextField();
		pole[5] = nazevModeluText;
		showZakazkyComponents[5] = nazevModeluText;
		nazevModeluText.setForeground(Color.WHITE);
		nazevModeluText.setFont(fonty[4]);
		nazevModeluText.setColumns(10);
		nazevModeluText.setBorder(null);
		nazevModeluText.setBackground(barvy[20]);
		GridBagConstraints gbc_nazevModeluText = new GridBagConstraints();
		gbc_nazevModeluText.gridwidth = 3;
		gbc_nazevModeluText.insets = new Insets(0, 0, 5, 5);
		gbc_nazevModeluText.fill = GridBagConstraints.BOTH;
		gbc_nazevModeluText.gridx = 10;
		gbc_nazevModeluText.gridy = 0;
		panel.add(nazevModeluText, gbc_nazevModeluText);
		
		JLabel idModeluLabel = new JLabel("Id modelu:");
		pole[6] = idModeluLabel;
		showZakazkyComponents[6] = idModeluLabel;
		idModeluLabel.setForeground(barvy[11]);
		idModeluLabel.setFont(fonty[4]);
		GridBagConstraints gbc_idModeluLabel = new GridBagConstraints();
		gbc_idModeluLabel.anchor = GridBagConstraints.WEST;
		gbc_idModeluLabel.insets = new Insets(0, 0, 5, 5);
		gbc_idModeluLabel.gridx = 0;
		gbc_idModeluLabel.gridy = 1;
		panel.add(idModeluLabel, gbc_idModeluLabel);
		
		idModeluText = new JTextField();
		pole[7] = idModeluText;
		showZakazkyComponents[7] = idModeluText;
		idModeluText.setForeground(Color.WHITE);
		idModeluText.setFont(fonty[4]);
		idModeluText.setColumns(10);
		idModeluText.setBorder(null);
		idModeluText.setBackground(barvy[20]);
		GridBagConstraints gbc_idModeluText = new GridBagConstraints();
		gbc_idModeluText.gridwidth = 2;
		gbc_idModeluText.insets = new Insets(0, 0, 5, 5);
		gbc_idModeluText.fill = GridBagConstraints.BOTH;
		gbc_idModeluText.gridx = 1;
		gbc_idModeluText.gridy = 1;
		panel.add(idModeluText, gbc_idModeluText);
		
		JLabel lblDatumPijetZakzky = new JLabel("Zak\u00E1zka p\u0159ijata po");
		pole[8] = lblDatumPijetZakzky;
		showZakazkyComponents[8] = lblDatumPijetZakzky;
		lblDatumPijetZakzky.setForeground(barvy[11]);
		lblDatumPijetZakzky.setFont(fonty[4]);
		GridBagConstraints gbc_lblDatumPijetZakzky = new GridBagConstraints();
		gbc_lblDatumPijetZakzky.anchor = GridBagConstraints.WEST;
		gbc_lblDatumPijetZakzky.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatumPijetZakzky.gridx = 5;
		gbc_lblDatumPijetZakzky.gridy = 1;
		panel.add(lblDatumPijetZakzky, gbc_lblDatumPijetZakzky);
		
		datumZakazkyDateChooser = new JDateChooser();
		pole[9] = datumZakazkyDateChooser;
		showZakazkyComponents[9] = datumZakazkyDateChooser;
		GridBagConstraints gbc_datumZakazkyDateChooser = new GridBagConstraints();
		gbc_datumZakazkyDateChooser.gridwidth = 2;
		gbc_datumZakazkyDateChooser.insets = new Insets(0, 0, 5, 5);
		gbc_datumZakazkyDateChooser.fill = GridBagConstraints.BOTH;
		gbc_datumZakazkyDateChooser.gridx = 6;
		gbc_datumZakazkyDateChooser.gridy = 1;
		panel.add(datumZakazkyDateChooser, gbc_datumZakazkyDateChooser);
		
		formovnaLabel1 = new JLabel("Formovna");
		pole[10] = formovnaLabel1;
		formovnaLabel1.setForeground(barvy[11]);
		formovnaLabel1.setFont(fonty[4]);
		GridBagConstraints gbc_formovnaLabel1 = new GridBagConstraints();
		gbc_formovnaLabel1.anchor = GridBagConstraints.WEST;
		gbc_formovnaLabel1.insets = new Insets(0, 0, 5, 5);
		gbc_formovnaLabel1.gridx = 9;
		gbc_formovnaLabel1.gridy = 1;
		panel.add(formovnaLabel1, gbc_formovnaLabel1);
		
		formovnaComboBox1 = new JComboBox<String>();
		pole[11] = formovnaComboBox1;
		seznamFormovenAPrazdny = new DefaultComboBoxModel<String>(new String[] {"", "T", "S", "M"});
		formovnaComboBox1.setModel(seznamFormovenAPrazdny);
		GridBagConstraints gbc_formovnaComboBox1 = new GridBagConstraints();
		gbc_formovnaComboBox1.insets = new Insets(0, 0, 5, 5);
		gbc_formovnaComboBox1.fill = GridBagConstraints.HORIZONTAL;
		gbc_formovnaComboBox1.gridx = 10;
		gbc_formovnaComboBox1.gridy = 1;
		panel.add(formovnaComboBox1, gbc_formovnaComboBox1);
		
		idZakazkyLabel = new JLabel("Id zak\u00E1zky:");
		pole[12] = idZakazkyLabel;
		showZakazkyComponents[10] = idZakazkyLabel;
		idZakazkyLabel.setForeground(barvy[11]);
		idZakazkyLabel.setFont(fonty[4]);
		GridBagConstraints gbc_lblIdZakzky = new GridBagConstraints();
		gbc_lblIdZakzky.anchor = GridBagConstraints.WEST;
		gbc_lblIdZakzky.insets = new Insets(0, 0, 5, 5);
		gbc_lblIdZakzky.gridx = 0;
		gbc_lblIdZakzky.gridy = 2;
		panel.add(idZakazkyLabel, gbc_lblIdZakzky);
		
		idZakazky = new JTextField();
		pole[13] = idZakazky;
		showZakazkyComponents[11] = idZakazky;
		idZakazky.setBorder(new EmptyBorder(2, 0, 2, 0));
		idZakazky.setForeground(Color.WHITE);
		idZakazky.setFont(fonty[4]);
		idZakazky.setColumns(10);
		idZakazky.setBackground(barvy[20]);
		GridBagConstraints gbc_idZakazky = new GridBagConstraints();
		gbc_idZakazky.gridwidth = 2;
		gbc_idZakazky.insets = new Insets(0, 0, 5, 5);
		gbc_idZakazky.fill = GridBagConstraints.BOTH;
		gbc_idZakazky.gridx = 1;
		gbc_idZakazky.gridy = 2;
		panel.add(idZakazky, gbc_idZakazky);
		
		cisloObjednavkyLabel = new JLabel("\u010C\u00EDslo objedn\u00E1vky");
		pole[14] = cisloObjednavkyLabel;
		showZakazkyComponents[12] = cisloObjednavkyLabel;
		cisloObjednavkyLabel.setForeground(barvy[11]);
		cisloObjednavkyLabel.setFont(fonty[4]);
		GridBagConstraints gbc_cisloObjednavkyLabel = new GridBagConstraints();
		gbc_cisloObjednavkyLabel.anchor = GridBagConstraints.WEST;
		gbc_cisloObjednavkyLabel.insets = new Insets(0, 0, 5, 5);
		gbc_cisloObjednavkyLabel.gridx = 5;
		gbc_cisloObjednavkyLabel.gridy = 2;
		panel.add(cisloObjednavkyLabel, gbc_cisloObjednavkyLabel);
		
		cisloObjednavkyText = new JTextField();
		pole[15] = cisloObjednavkyText;
		showZakazkyComponents[13] = cisloObjednavkyText;
		cisloObjednavkyText.setForeground(Color.WHITE);
		cisloObjednavkyText.setFont(fonty[4]);
		cisloObjednavkyText.setColumns(10);
		cisloObjednavkyText.setBorder(null);
		cisloObjednavkyText.setBackground(barvy[20]);
		GridBagConstraints gbc_cisloObjednavkyText = new GridBagConstraints();
		gbc_cisloObjednavkyText.gridwidth = 2;
		gbc_cisloObjednavkyText.insets = new Insets(0, 0, 5, 5);
		gbc_cisloObjednavkyText.fill = GridBagConstraints.BOTH;
		gbc_cisloObjednavkyText.gridx = 6;
		gbc_cisloObjednavkyText.gridy = 2;
		panel.add(cisloObjednavkyText, gbc_cisloObjednavkyText);
		
		checkVcetneUzavZak = new JCheckBox("Pouze uzav\u0159en\u00E9 zak\u00E1zky");
		pole[16] = checkVcetneUzavZak;
		showZakazkyComponents[14] = checkVcetneUzavZak;
		checkVcetneUzavZak.setBorder(null);
		checkVcetneUzavZak.setFont(fonty[4]);
		checkVcetneUzavZak.setForeground(barvy[11]);
		checkVcetneUzavZak.setBackground(barvy[0]);
		GridBagConstraints gbc_checkVcetneUzavZak = new GridBagConstraints();
		gbc_checkVcetneUzavZak.anchor = GridBagConstraints.WEST;
		gbc_checkVcetneUzavZak.gridwidth = 4;
		gbc_checkVcetneUzavZak.insets = new Insets(0, 0, 5, 5);
		gbc_checkVcetneUzavZak.gridx = 9;
		gbc_checkVcetneUzavZak.gridy = 2;
		panel.add(checkVcetneUzavZak, gbc_checkVcetneUzavZak);
		
		dateOdLabel = new JLabel("Datum od:");
		vypisy[0] = dateOdLabel;
		dateOdLabel.setForeground(barvy[11]);
		dateOdLabel.setFont(fonty[4]);
		GridBagConstraints gbc_dateOdLabel = new GridBagConstraints();
		gbc_dateOdLabel.insets = new Insets(0, 0, 5, 5);
		gbc_dateOdLabel.anchor = GridBagConstraints.WEST;
		gbc_dateOdLabel.gridx = 0;
		gbc_dateOdLabel.gridy = 3;
		panel.add(dateOdLabel, gbc_dateOdLabel);
		
		odDatum = new MyJDateChooser();
		vypisy[1] = odDatum;
		GridBagConstraints gbc_odDatum = new GridBagConstraints();
		gbc_odDatum.fill = GridBagConstraints.HORIZONTAL;
		gbc_odDatum.gridwidth = 2;
		gbc_odDatum.insets = new Insets(0, 0, 5, 5);
		gbc_odDatum.gridx = 1;
		gbc_odDatum.gridy = 3;
		panel.add(odDatum, gbc_odDatum);
		
		dateDoLabel = new JLabel("Datum do:");
		vypisy[2] = dateDoLabel;
		dateDoLabel.setForeground(barvy[11]);
		dateDoLabel.setFont(fonty[4]);
		GridBagConstraints gbc_dateDoLabel = new GridBagConstraints();
		gbc_dateDoLabel.anchor = GridBagConstraints.WEST;
		gbc_dateDoLabel.insets = new Insets(0, 0, 5, 5);
		gbc_dateDoLabel.gridx = 5;
		gbc_dateDoLabel.gridy = 3;
		panel.add(dateDoLabel, gbc_dateDoLabel);
		
		doDatum = new MyJDateChooser();
		vypisy[3] = doDatum;
		GridBagConstraints gbc_doDatum = new GridBagConstraints();
		gbc_doDatum.gridwidth = 2;
		gbc_doDatum.insets = new Insets(0, 0, 5, 5);
		gbc_doDatum.fill = GridBagConstraints.BOTH;
		gbc_doDatum.gridx = 6;
		gbc_doDatum.gridy = 3;
		panel.add(doDatum, gbc_doDatum);
		
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
		
		cisloTydneLabel = new JLabel("\u010C\u00EDslo t\u00FDdne");
		vypisy[4] = cisloTydneLabel;
		cisloTydneLabel.setFont(fonty[4]);
		cisloTydneLabel.setForeground(barvy[11]);
		GridBagConstraints gbc_cisloTydneLabel = new GridBagConstraints();
		gbc_cisloTydneLabel.anchor = GridBagConstraints.WEST;
		gbc_cisloTydneLabel.fill = GridBagConstraints.VERTICAL;
		gbc_cisloTydneLabel.insets = new Insets(0, 0, 5, 5);
		gbc_cisloTydneLabel.gridx = 0;
		gbc_cisloTydneLabel.gridy = 4;
		panel.add(cisloTydneLabel, gbc_cisloTydneLabel);
		
		cisloTydneText = new JTextField();
		vypisy[5] = cisloTydneText;
		cisloTydneText.setForeground(Color.WHITE);
		cisloTydneText.setFont(fonty[4]);
		cisloTydneText.setColumns(10);
		cisloTydneText.setBorder(null);
		cisloTydneText.setBackground(barvy[20]);
		GridBagConstraints gbc_cisloTydneText = new GridBagConstraints();
		gbc_cisloTydneText.insets = new Insets(0, 0, 5, 5);
		gbc_cisloTydneText.fill = GridBagConstraints.HORIZONTAL;
		gbc_cisloTydneText.gridx = 1;
		gbc_cisloTydneText.gridy = 4;
		panel.add(cisloTydneText, gbc_cisloTydneText);
		
		yearChooser = new JYearChooser();
		vypisy[6] = yearChooser;
		GridBagConstraints gbc_yearChooser = new GridBagConstraints();
		gbc_yearChooser.insets = new Insets(0, 0, 5, 5);
		gbc_yearChooser.fill = GridBagConstraints.BOTH;
		gbc_yearChooser.gridx = 2;
		gbc_yearChooser.gridy = 4;
		panel.add(yearChooser, gbc_yearChooser);
		
		formovnaLabel2 = new JLabel("Formovna:");
		vypisy[10] = formovnaLabel2;
		formovnaLabel2.setForeground(new Color(246, 246, 246));
		formovnaLabel2.setFont(fonty[4]);
		GridBagConstraints gbc_formovnaLabel2 = new GridBagConstraints();
		gbc_formovnaLabel2.anchor = GridBagConstraints.WEST;
		gbc_formovnaLabel2.insets = new Insets(0, 0, 5, 5);
		gbc_formovnaLabel2.gridx = 5;
		gbc_formovnaLabel2.gridy = 4;
		panel.add(formovnaLabel2, gbc_formovnaLabel2);
		
		comboBoxFormovna2 = new JComboBox<String>();
		vypisy[11] = comboBoxFormovna2;
		seznamFormoven = new DefaultComboBoxModel<String>(new String[] {"T", "S", "M"});
		comboBoxFormovna2.setModel(seznamFormoven);
		GridBagConstraints gbc_comboBoxFormovna2 = new GridBagConstraints();
		gbc_comboBoxFormovna2.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxFormovna2.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxFormovna2.gridx = 6;
		gbc_comboBoxFormovna2.gridy = 4;
		panel.add(comboBoxFormovna2, gbc_comboBoxFormovna2);
		
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
		
		vlMaterialLabel = new JLabel("Vl. materi\u00E1l");
		pole[17] = vlMaterialLabel;
		vlMaterialLabel.setFont(fonty[4]);
		vlMaterialLabel.setForeground(barvy[11]);
		GridBagConstraints gbc_vlMaterialLabel = new GridBagConstraints();
		gbc_vlMaterialLabel.anchor = GridBagConstraints.WEST;
		gbc_vlMaterialLabel.insets = new Insets(0, 0, 5, 5);
		gbc_vlMaterialLabel.gridx = 9;
		gbc_vlMaterialLabel.gridy = 5;
		panel.add(vlMaterialLabel, gbc_vlMaterialLabel);
		
		//vlMaterialJbutton = new JComboBox<String>();
		vlMaterialJbutton = new MyJButton("Vyber materiály", 13, 4, this.sklad);
		vlMaterialJbutton.removeMouseListener(sklad.getMyJButonnListener());
		vlMaterialJbutton.setPreferredSize(new Dimension(130, 22));
		vlMaterialJbutton.setBorder(new LineBorder(barvy[15]));
		vlMaterialJbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seznamVlMater.show(vlMaterialJbutton, vlMaterialJbutton.getWidth(), 0);
			}
		});
		//this.vlMaterialJbutton.setModel(vlastniMaterialySeznamModel);
		vypisy[12] = vlMaterialJbutton;
		GridBagConstraints gbc_vlMaterialJbutton = new GridBagConstraints();
		gbc_vlMaterialJbutton.gridwidth = 2;
		gbc_vlMaterialJbutton.insets = new Insets(0, 0, 5, 5);
		gbc_vlMaterialJbutton.fill = GridBagConstraints.HORIZONTAL;
		gbc_vlMaterialJbutton.gridx = 10;
		gbc_vlMaterialJbutton.gridy = 5;
		panel.add(vlMaterialJbutton, gbc_vlMaterialJbutton);
		
		kapacityLabel = new JLabel("Kapacity [T;S;M]:");
		vypisy[13] = kapacityLabel;
		kapacityLabel.setFont(fonty[4]);
		kapacityLabel.setForeground(barvy[11]);
		GridBagConstraints gbc_kapacityLabel = new GridBagConstraints();
		gbc_kapacityLabel.anchor = GridBagConstraints.WEST;
		gbc_kapacityLabel.insets = new Insets(0, 0, 5, 5);
		gbc_kapacityLabel.gridx = 0;
		gbc_kapacityLabel.gridy = 6;
		panel.add(kapacityLabel, gbc_kapacityLabel);
		
		kapacityTextField = new JTextField();
		vypisy[14] = kapacityTextField;
		kapacityTextField.setText("70 000;21 000;10 000");
		kapacityTextField.setForeground(Color.WHITE);
		kapacityTextField.setFont(fonty[4]);
		kapacityTextField.setColumns(10);
		kapacityTextField.setBorder(null);
		kapacityTextField.setBackground(barvy[2]);
		GridBagConstraints gbc_kapacityTextField = new GridBagConstraints();
		gbc_kapacityTextField.gridwidth = 2;
		gbc_kapacityTextField.insets = new Insets(0, 0, 5, 5);
		gbc_kapacityTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_kapacityTextField.gridx = 1;
		gbc_kapacityTextField.gridy = 6;
		panel.add(kapacityTextField, gbc_kapacityTextField);
		kapacityTextField.setColumns(10);
		
		
		addListeners();
		//setHledejZakazniky();
		setParametryFiltr(ParametryFiltr.HledejZakazniky);
	}
	
	private DefaultComboBoxModel<String> createComboBoxListModelFromResultSet(ResultSet rs) throws SQLException{
		int size = 0;
		try {
		    rs.last();
		    size = rs.getRow();
		    rs.beforeFirst();
		}
		catch(Exception ex) {
			size = 0;
		}
		String [] vlMaterialy = new String [size];
		int i = 0;
		while(rs.next()){
			vlMaterialy[i] = rs.getString(1); // ma jen jeden sloupec
			i++;
		}
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(vlMaterialy);
		return model;
	}
	
	
	public String getJmenoZakaznikaOrVadyOrVinika(){
		return jmenoZakaznikaText.getText();
	}
	public String getCisloModelu(){
		return this.cisloModeluText.getText();
	}
	public String getNazevModelu(){
		return this.nazevModeluText.getText();
	}
	public String getIDZakazky() throws NumberFormatException{
		return  this.idZakazky.getText();
	}
	public String getIDModelu() throws NumberFormatException{
		return this.idModeluText.getText();
	}
	public Date getDatumZakazky(){
		return datumZakazkyDateChooser.getDate();
	}
	public String getFormovna1Pole(){
		return (String) formovnaComboBox1.getSelectedItem();
	}
	/*public String getSelectedVlastniMaterial(){
		return (String) this.vlMaterialJbutton.getSelectedItem();
	}*/
	public String [] getSelectedVlMaterials(){
		JCheckBox [] checkBoxItems = this.seznamVlMater.getCheckBoxItems();
		if(checkBoxItems[0].isSelected()){ // je vybrano vše
			return new String[] {checkBoxItems[0].getText()};
		}
		int size = 0;
		for(int i = 1; i < checkBoxItems.length; i++){ // prvni item je Vše
			if(checkBoxItems[i].isSelected()){
				size++;
			}
		}
		String [] selectedMaterials = new String [size];
		int pom = 0;
		for(int i = 1; i < checkBoxItems.length; i++){ // prvni item je Vše
			if(checkBoxItems[i].isSelected()){
				selectedMaterials[pom] = checkBoxItems[i].getText();
				pom++;
			}
		}
		return selectedMaterials;
	}
	public boolean isSelectedVcetneUzavreneZakazky(){
		return checkVcetneUzavZak.isSelected();
	}
	public String getCisloObjednavky(){
		return cisloObjednavkyText.getText();
	}
	public Date getOdDate(){
		return odDatum.getDate();
	}
	public Date getDoDate(){
		return doDatum.getDate();
	}
	public int getCisloTydne()  throws NumberFormatException{
		String s = this.cisloTydneText.getText();
		int id = 0;
		id = Integer.parseInt(s);
		return id;
	}
	public int getRok(){
		return this.yearChooser.getYear();
	}
	public String getFormovna2Vypisy(){
		return  (String) comboBoxFormovna2.getSelectedItem();
	}
	public String getKapacity(){
		return this.kapacityTextField.getText();
	}
}
