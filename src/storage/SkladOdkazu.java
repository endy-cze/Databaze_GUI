package storage;

import iListeners.MainActionMenuItemListener;
import iListeners.MainSideJButtonListener;
import iListeners.MyJButonnListener;
import iListeners.PridejZakaznikaList;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Connection;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import sablony.MyJButton;
import sablony.MyPopUp;
import sablony.tabulka.ColorCellTable;
import sablony.tabulka.TableColumnAdjuster;
import sqlstorage.SQLStor;
import app.ExpediceZmetek;
import app.MainFrame;
import app.Navigator;
import app.Planovani;
import app.ProgresBarFrame;

/**
 * V teto tride se pouze ukladaji odkazy, jedine co se tu vytvari jsou texty ActionCommand pro ItemMenu (v postranim menu)
 * @author Ondøej Havlíèek
 *
 */
public class SkladOdkazu {
	
	private SQLStor sql;
	
	/**
	 * <p>Techto actionComandu by melo být stejne jako JPopupMenuItems.</p>
	 * <p>Jsou to actionComandy jednotlivych oken (každy MenuItem v PopUpMenu ma vlastni). Tyto comandy jsou použity i jako actionComandy pro tisk, 
	 * jenom se pøed nì pøidá ještì øetìzec "PDF". Comandy se nastavujjí v tøíde <code>ParametryFiltr</code> a srovnavaji se v tøíde <code>HledejListener</code> (ActionListener).
	 *  A nebo ve tøíde <code>PromOknoNovyZakaznikAndSearch</code> v metode nastavOkno a srovnavaji v listeneru <code>VyberOrUpravListener</code></p>
	 * <p>Zatim co koukam, tak se tyto comandy pouzivaji pro tlacítko vyberUprav v <code>PromOknoNovyZakaznikAndSearch</code> a pro JButtony vyhledej (Hledej) a prevodDoPDF v <code>ParametryFiltr</code>.</p>
	 * <p>Zkontrolovat Listenery... zkotrolovano button v <code>PromOknoNovyZakaznikAndSearch</code> ma <code>VyberOrUpravListener</code> 
	 * a buttony v <code>ParametryFiltr</code> maji <code>HledejListener</code>. Kolize nehrozi.</p>
	 */
	private final String [][] commands = {
			{"PridejZakaznika", "PridejModel", "PridejZakazku"},
			{"Planovani", "VyhledejZakaznika", "VyhledejModel", "VyhledejZakazku", "VyhledejZakazku", "VyhledejZmetek", "VyhledejViniky", "VyhledejVady", "VyhledejKapPropocet"},
			{"NovyOdlitekZmetek", "UpravaCislaTavby"},
			{"Expedice", "LiciPlanZakladni", "LiciPlanPlanovaci", "PlanExpedice"},
			{"VypisStavNeuzavrenychZakazek","DenniVypisOdlitychKusu", "VypisVycistenychKusuZaObdobi", "MzdySlevacu", "VypisOdlitkuVKgKc",
				"VypisOdlitychKusuOdDo", "VypisPolozekSOdhadHmot", "VypisDleTerminuExpedice", "VypisExpedice od-do",
				"VypisZpozdeneVyroby", "InventuraRozpracVyroby", "VypisSkladuKeDnesnimuDni", "VypisZmetkuZaObdobi", "VypisVinikuVKgKc"},
			{"UpravaZakaznika", "UpravaModelu", "UpravaZakazky"},
			{"Pøidat viníky", "Pøidat druhy vad"},
			{"SmazatZakaznika", "SmazatModel", "SmazatZakazku"},
			{"ZalohaDB"}
	};

	// hlavni okno
	private MainFrame hlavniOkno;
	private JScrollPane scrollPane;
	private Navigator navigace;
	
	private JButton odhlasit; 
	
	private JPanel [] promOkna = null;
	private Planovani planovani;
	private ExpediceZmetek expedicezmetek;
	
	private MyJButton [] sideListButton;
	private MyPopUp [] sidePopupMenulist;
	private JMenuItem [][] sidePopupMenuItems;
	
	private MainSideJButtonListener sideButtonListener;
	private MainActionMenuItemListener itemList;
	
	//vedlejsi Okno
	private ColorCellTable promOknoNovyZakaznikSearchColorTable;
	private ColorCellTable tableNovyModel;
	private ColorCellTable tableNovaZakazka;
	
	private TableColumnAdjuster PromOknoNovyZakaznikAndSearchcolumAdjuster;
	
	/**
	 * TextFiled ktery pøeda nove jméno nového zákazníka
	 */
	private JTextField noveJmenoZakaznikaTextField;
	/**
	 * JLabel ktery pøeda id upravovaneho zákazníka
	 */
	private JLabel idZakaznikaText;
	
	/**
	 * Seznam textFiledu ktery obsahuji parametry noveho modelu, popøípadì modelu ktery upravujeme
	 */
	private Component [] novyModelListTextComponent;

	/**
	 * listener pro pridani noveho modelu, zakaznika a zakazky
	 */
	private PridejZakaznikaList novyZakModelZakazka;

	// SQL
	private Connection conn;
	/**
	 * Celkem budou 5 druhy tabulek,
	 * 0. Seznam Zakazniku
	 * 1. Seznam Modelu
	 * 2. Seznam Zakazek
	 * 3. Seznam Fyzických kusù
	 * 4. Plánování
	 * A nazvy jejich sloupcu budou vypadat nasledovne
	 * pokud neco pozmenim ve 3 seznamu musím to pozmìnit i v tøíde PromOknoNovaZakazka v metodì initListComponents()
	 */
	private final String [][] nazvySloupcuTabulek = {
			{"ID zákazníka", "Jméno zákazníka",""},
			{"ID modelu", "Jméno modelu", "Èíslo modelu", "Materiál", "Materiál vlastní", "Hmotnost", "Odhadovaná hmotnost", "Formovna", "Norma slevaèe",""},
			{"ID zakázky", "Jméno zákazníka", "Èíslo objednávky","Èíslo modelu", "Datum pøijetí zakázky", "Poèet kusù", "Paganýrka", "Cena", "CZK/EUR","Cena je za kus","Kurz EU na CZK", "Id modelu","Jméno modelu",  
				"Materiál", "Materiál vlastní", "Formovna", "Hmotnost", "Odhadovaná hmotnost", "Norma slévaèe", "Id zákazníka", "Poznámka" ,""},
			{"ID kusu","Datum lití" ,"Èíslo tavby", "Odlito", "Vyèištìno", "Zmetek", "Dílèí termín", "Expedováno", "Èíslo faktury", "Datum vyèištìní", "Id viníka","Viník","Id vady","Vada",""},
			{"M\u011Bs\u00EDc", "\u010C\u00EDslo t\u00FDdne", "Po", "poèet", "\u00DAt", "poèet", "St", "poèet", "\u010Ct", "poèet", "P\u00E1", "poèet", "Rok plánování", ""}
	};
	
	/**
	 * Seznam vad a viniku
	 * 1 pozice je vady
	 * 2 potom vinico
	 */
	private String [][] vadyVinici = new String [2][];
	
	/**
	 * Prazdne tabulka<br>
	 * nazvySloupcuTabulek[0] = Zakazníci<br>
	 * nazvySloupcuTabulek[1] = Modely<br>
	 * nazvySloupcuTabulek[2] = Zakazky<br>
	 * nazvySloupcuTabulek[3] = Fyzické kusy<br>
	 * nazvySloupcuTabulek[4] = Rozvrh<br>
	 * prazdna tabulka [5] 
	 */
	private TableModel [] prazdneTabulky = {
			new DefaultTableModel(nazvySloupcuTabulek[0], 10),
			new DefaultTableModel(nazvySloupcuTabulek[1], 10),
			new DefaultTableModel(nazvySloupcuTabulek[2], 10),
			new DefaultTableModel(nazvySloupcuTabulek[3], 5),
			new DefaultTableModel(nazvySloupcuTabulek[4], 5),
			new DefaultTableModel(new String []{"Prázdná tabulka", "Prázdná tabulka", ""}, 5)
	};
	/**
	 * posledniPouzite[0] = Zakazníci<br>
	 * posledniPouzite[1] = Modely<br>
	 * posledniPouzite[2] = Zakazky<br>
	 * posledniPouzite[3] = Fyzické kusy<br>
	 */
	private TableModel [] posledniPouzite = {null, null, null, null};
	
	private MyJButonnListener myJButonnListener ;
	
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
	private Color [] barvy;
	
	/**
	 * Fonty
	 * new Font("Tahoma", Font.PLAIN, 12),
	 * new Font("Tahoma", Font.PLAIN, 20),
	 * new Font("Tahoma", Font.BOLD, 14),
	 * new Font("Tahoma", Font.PLAIN, 13)
	 */
	private Font [] fonty;
	
	/**
	 * SimpleDateFormat ve formatu dd.MM.yyyy
	 */
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	/**
	 * SimpleDateFormat ve formatu dd_MM_yyyy
	 */
	private final SimpleDateFormat sdf2 = new SimpleDateFormat("dd_MM_yyyy");
	
	private ProgresBarFrame bar = new ProgresBarFrame();

	public ProgresBarFrame getBar() {
		return bar;
	}

	/**
	 * V teto tride se pouze ukladaji odkazy, jedine co se tu vytvari jsou texty ActionCommand pro ItemMenu (v postranim menu)
	 */
	public SkladOdkazu(Connection conn, Color [] barvy, Font [] fonty){
		this.conn = conn;
		this.barvy = barvy;
		this.fonty = fonty;
		this.sql = new SQLStor(this);
		this.novyZakModelZakazka = new PridejZakaznikaList(this);
		this.myJButonnListener = new MyJButonnListener(this);
	}
	
	/**
	 * posledniPouzite[0] = Zakazníci<br>
	 * posledniPouzite[1] = Modely<br>
	 * posledniPouzite[2] = Zakazky<br>
	 * posledniPouzite[3] = Fyzické kusy<br>
	 * posledniPouzite[4] = Rozvrh<br>
	 * @param i index
	 * @return Table model posledni použite tabulkys
	 */
	public TableModel getPoslednipouziteTab(int i){
		return posledniPouzite[i];
	}
	
	/**
	 * /**
	 * posledniPouzite[0] = Zakazníci<br>
	 * posledniPouzite[1] = Modely<br>
	 * posledniPouzite[2] = Zakazky<br>
	 * posledniPouzite[3] = Fyzické kusy<br>
	 * posledniPouzite[4] = Rozvrh<br>
	 * @param i index
	 * @param tm Posledni pouzity model pro danou tabulku
	 */
	public void setPosledniPouzitaTab(int i, TableModel tm){
		posledniPouzite[i] = tm;
	}	
	
	/**
	 * Navrátí již vytvoøenou instanci {@code SimpleDateFormat}. Tato instance se vytváøí pøi vytvoøení tøídy {@code SkladOdkazu}
	 * @return SimpleDateFormat ve formatu dd.MM.yyyy
	 */
	public SimpleDateFormat getSdf() {
		return sdf;
	}


	public PridejZakaznikaList getNovyZakModelZakazka() {
		return novyZakModelZakazka;
	}

	public Component[] getNovyModelListTextComponent() {
		return novyModelListTextComponent;
	}

	public void setNovyModelListTextComponent(Component[] novyModelListTextComponent) {
		this.novyModelListTextComponent = novyModelListTextComponent;
	}

	public SQLStor getSql() {
		return sql;
	}

	public JTextField getNoveJmenoZakaznikaTextField() {
		return noveJmenoZakaznikaTextField;
	}

	public void setNoveJmenoZakaznikaTextField(
			JTextField noveJmenoZakaznikaTextField) {
		this.noveJmenoZakaznikaTextField = noveJmenoZakaznikaTextField;
	}

	public String[][] getNazvySloupcuTabulek() {
		return nazvySloupcuTabulek;
	}
	
	/**
	 * Prázdné tabulky<br>
	 * @return
	 * TableModel[0] = Zakazníci<br>
	 * TableModel[1] = Modely<br>
	 * TableModel[2] = Zakazky<br>
	 * TableModel[3] = Fyzické kusy<br>
	 * TableModel[4] = Rozvrh<br>
	 */
	public TableModel[] getPrazdneTabulky() {
		return prazdneTabulky;
	}

	public ExpediceZmetek getExpedicezmetek() {
		return expedicezmetek;
	}

	public void setExpedicezmetek(ExpediceZmetek expedicezmetek) {
		this.expedicezmetek = expedicezmetek;
	}

	public Planovani getPlanovani() {
		return planovani;
	}

	public void setPlanovani(Planovani planovani) {
		this.planovani = planovani;
	}

	public TableColumnAdjuster getPromOknoNovyZakaznikAndSearchColumAdjuster() {
		return PromOknoNovyZakaznikAndSearchcolumAdjuster;
	}

	public void setPromOknoNovyZakaznikAndSearchColumAdjuster(TableColumnAdjuster columAdjuster) {
		this.PromOknoNovyZakaznikAndSearchcolumAdjuster = columAdjuster;
	}
	
	public String[][] getCommands() {
		return commands;
	}

	public MainSideJButtonListener getSideButtonListener() {
		return sideButtonListener;
	}


	public void setSideButtonListener(MainSideJButtonListener sideButtonListener) {
		this.sideButtonListener = sideButtonListener;
	}


	public Font[] getFonty() {
		return fonty;
	}

	public JButton getOdhlasit() {
		return odhlasit;
	}


	public void setOdhlasit(JButton odhlasit) {
		this.odhlasit = odhlasit;
	}
	
	public MainFrame getHlavniOkno() {
		return hlavniOkno;
	}


	public void setHlavniOkno(MainFrame hlavniOkno) {
		this.hlavniOkno = hlavniOkno;
	}

	public Navigator getNavigator(){
		return this.navigace;
	}
	
	public void setNavigator(Navigator nav){
		this.navigace = nav;
	}

	public JPanel[] getPromOkna() {
		return promOkna;
	}


	public void setPromOkna(JPanel[] promOkna) {
		this.promOkna = promOkna;
	}

	public MyJButton[] getSideListButton() {
		return sideListButton;
	}


	public void setSideListButton(MyJButton[] sideListButton) {
		this.sideListButton = sideListButton;
	}


	public MyPopUp[] getSidePopupMenulist() {
		return sidePopupMenulist;
	}


	public void setSidePopupMenulist(MyPopUp[] sidePopupMenulist) {
		this.sidePopupMenulist = sidePopupMenulist;
	}


	public JMenuItem[][] getSidePopupMenuItems() {
		return sidePopupMenuItems;
	}


	public void setSidePopupMenuItems(JMenuItem[][] sidePopupMenuItems) {
		this.sidePopupMenuItems = sidePopupMenuItems;
	}


	public MainSideJButtonListener getListener() {
		return sideButtonListener;
	}


	public void setListener(MainSideJButtonListener listener) {
		this.sideButtonListener = listener;
	}


	public MainActionMenuItemListener getItemList() {
		return itemList;
	}


	public void setItemList(MainActionMenuItemListener itemList) {
		this.itemList = itemList;
	}


	public ColorCellTable getPromOknoNovyZakaznikSearchColorTable() {
		return promOknoNovyZakaznikSearchColorTable;
	}


	public void setPromOknoNovyZakaznikSearchColorTable(ColorCellTable tableNovyZakaznik) {
		this.promOknoNovyZakaznikSearchColorTable = tableNovyZakaznik;
	}


	public ColorCellTable getTableNovyModel() {
		return tableNovyModel;
	}


	public void setTableNovyModel(ColorCellTable table) {
		this.tableNovyModel = table;
	}


	public ColorCellTable getTableNovaZakazka() {
		return tableNovaZakazka;
	}



	public Connection getConn() {
		return conn;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

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
	 * @return
	 */
	public Color[] getBarvy() {
		return barvy;
	}

	/**
	 * Pole o 2 radach <br>
	 *  na [0] pozici jsou vady<br>
	 *  na [1] pozici jsou vinici
	 * @return
	 */
	public String[][] getVadyVinici() {
		return vadyVinici;
	}

	public void setVadyVinici(String[][] vadyVinici) {
		this.vadyVinici = vadyVinici;
	}

	public MyJButonnListener getMyJButonnListener() {
		return myJButonnListener;
	}

	public JLabel getIdZakaznikaText() {
		return idZakaznikaText;
	}

	public void setIdZakaznikaText(JLabel idZakaznikaText) {
		this.idZakaznikaText = idZakaznikaText;
	} 
	
	public SimpleDateFormat getSdf2() {
		return sdf2;
	}	
}
