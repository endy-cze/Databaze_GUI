package iListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.ExpediceZmetek;
import app.MainFrame;
import app.Planovani;
import app.ProgresBarFrame;
import app.PromOknoNovaZakazka;
import app.PromOknoNovyModel;
import app.PromOknoNovyZakaznikAndSearch;
import sablony.errorwin.ExceptionWin;
import sablony.tabulka.ColorCellTable;
import sqlstorage.SQLStor;
import storage.SkladOdkazu;
import thread.PridelitPrivilegia;
import thread.ZalohujDB;

/**
 * Listener pro Vyber nebo uprav v PromOknoNovyZakaznikAndSearch (ne ten button ve Filtru ale ten druhy)
 * @author Havlicek
 *
 */
public class VyberOrUpravListener implements ActionListener {
	private SkladOdkazu sklad;
	private MainFrame hlavniOkno;
	private SQLStor sql;
	private JPanel [] promOkna = null;
	
	private String [][] comands;
	private ColorCellTable table;
	
	private String pomString;
	private PromOknoNovyZakaznikAndSearch tmp;
	private PromOknoNovyModel tmp3;
	private PromOknoNovaZakazka tmp4;
	private Planovani planovani;
	
	private static final String acesDenied = "execute command denied to user";
	
	private int idVybranehoObjektu;
	
	/**
	 * 
	 * Listener pro JButton VyberNeboUprav v PromOknoNovyZakaznikAndSearch (ne ten button ve Filtru ale ten druhy)
	 * @param sklad 
	 */
	public VyberOrUpravListener(SkladOdkazu sklad){
		this.sklad = sklad;
		comands = this.sklad.getCommands();
		table = sklad.getPromOknoNovyZakaznikSearchColorTable();
		hlavniOkno = sklad.getHlavniOkno();
		promOkna = sklad.getPromOkna();
		planovani = sklad.getPlanovani();
		sql = sklad.getSql();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		pomString = arg0.getActionCommand();
		System.out.println(pomString + " VyberorUprav");
		
		if(arg0.getActionCommand().equalsIgnoreCase("NovaZakazkaVyberZakaznika")){
			if(table.getSelectedRow() == -1){
				JOptionPane.showMessageDialog(hlavniOkno, "Neni vybraný žádný zákazník ");
				return;
			}
			tmp4 = (PromOknoNovaZakazka) promOkna[2];
			int selectedRow = table.getSelectedRow();
			String jmenoSloupce = table.getColumnName(0);
			if(jmenoSloupce.equalsIgnoreCase("Id_zakaznika") || jmenoSloupce.equalsIgnoreCase(sklad.getNazvySloupcuTabulek()[0][0])){
				tmp4.setComboBoxy((String)table.getValueAt(selectedRow, 0),(String) table.getValueAt(selectedRow, 1), null, null);
				hlavniOkno.setWindow(2);
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabulka "+jmenoSloupce);
			}
			return;
		}
		else if(arg0.getActionCommand().equalsIgnoreCase("NovaZakazkaVyberModel")){
			if(table.getSelectedRow() == -1){
				JOptionPane.showMessageDialog(hlavniOkno, "Neni vybraný žádný model ");
				return;
			}
			tmp4 = (PromOknoNovaZakazka) promOkna[2];
			int selectedRow = table.getSelectedRow();
			String jmenoSloupce = table.getColumnName(0);
			if(jmenoSloupce.equalsIgnoreCase("Id_modelu") || jmenoSloupce.equalsIgnoreCase(sklad.getNazvySloupcuTabulek()[1][0])){
				tmp4.setComboBoxy(null, null,(String)table.getValueAt(selectedRow, 0),(String) table.getValueAt(selectedRow, 2));
				hlavniOkno.setWindow(2);
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabulka "+jmenoSloupce);
			}
			return;
		}
		
		int i = 0, j = 0;
		boolean stop = false;
		for(i = 0; i < comands.length; i++){
			for( j = 0; j < comands[i].length; j++){
				if(comands[i][j] == null){
					break;
				}
				else if(comands[i][j].equalsIgnoreCase(pomString)) {
					stop = true;
					break;
				}
			}
			if(stop)break;
		}
		if(i != 8){ //pro zaloha DB nemužu z tabulky nic vybrat a ani nepotøebuju ID objektu
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(hlavniOkno, "Vyberte nìjakého zákazníka z tabulky");
				return;
			}

			// pøedpoklad že ID je vždy na 1 místì
			String pom = (String) this.table.getValueAt(selectedRow, 0);
			try {
				idVybranehoObjektu = Integer.parseInt(pom);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabulka");
				return;
			}
		}
		
		
		try {
			switch (i) {
			case 0:
				// Novy zakazka model ci zakaznik
				index0(j);
				break;
			case 1:
				index1(j);
				break;
			case 2:
				index2(j);
				break;
			case 3:
				index3(j);
				break;
			case 5:
				index5(j);
				break;
			case 6: // uprava viniku a vad
				index6(j);
				break;
			case 7:
				index7(j);
				break;
			case 8: //zaloha DB
				index8(j);
				break;
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
	 * Slouží pro novy model dle jineho
	 * @param j
	 */
	private void index0(int j){
		int selectedRow = table.getSelectedRow();
		String columnName = table.getColumnName(0);
		String [] parametry;
		if(columnName.equalsIgnoreCase("ID modelu") || columnName.equalsIgnoreCase("Id modelu")){
			hlavniOkno.setWindow(1);
			tmp3 = (PromOknoNovyModel) promOkna[1];
			selectedRow = table.getSelectedRow();
			parametry = new String [table.getColumnCount()-1];
			for(int m = 0; m < parametry.length; m++){
				parametry[m] = (String) this.table.getValueAt(selectedRow, m);
			}
			tmp3.setNovyModelDleStareho(parametry);
		} else {
			JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabulka");
			return;
		}
	}
		
	/**
	 * Spusti se jen a pouze tehdy když planujeme
	 * @param i
	 * @param j
	 * @throws Exception
	 */
	private void index1(int j) throws Exception{
		switch(j){
		case 0:
			String [] parametryZakazky = null;
			int selectedRow = table.getSelectedRow();
			parametryZakazky = new String [table.getColumnCount()-1]; // mam tam nastaveny jeden sloupec navic (nastaveny v Querry table modelu, kvuli CollumAdjusteru)
			for(int m = 0; m < parametryZakazky.length; m++){
				parametryZakazky[m] = (String) this.table.getValueAt(selectedRow, m);
			}
			String columnName = table.getColumnName(0);
			if(columnName.equalsIgnoreCase("ID zakázky") || columnName.equalsIgnoreCase("ID_zakazky")){
				ResultSet fyzKusy = sql.vyberFyzKusy(idVybranehoObjektu).rs;
				planovani.setPlanovani(parametryZakazky, fyzKusy, idVybranehoObjektu);
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabulka");
				return;
			}
			hlavniOkno.setWindow(3);
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		default: System.out.println("neco je sptne VyberorUpravListener");	
			break;
		}
		
		
	}
	
	
	/**
	 * Susti se když v mate okna zadat novy odlitek nebo zadat vyèištìny kus a kliknete na na tlaèítko "Vyber zakázku" (Jbutonn vyberoruprav)
	 * @param j
	 * @throws Exception 
	 */
	private void index2(int j) throws Exception{
		String [] parametryZakazky = null;
		int selectedRow = table.getSelectedRow();
		String columnName = table.getColumnName(0);
		switch(j){
		case 0:
			parametryZakazky = new String [table.getColumnCount()-1]; // mam tam nastaveny jeden sloupec navic (nastaveny v Querry table modelu, kvuli CollumAdjusteru)
			for(int m = 0; m < parametryZakazky.length; m++){
				parametryZakazky[m] = (String) this.table.getValueAt(selectedRow, m);
			}
			
			if(columnName.equalsIgnoreCase("ID zakázky") || columnName.equalsIgnoreCase("ID_zakazky")){
				((ExpediceZmetek) promOkna[4]).setZadejOdlitekZmetek(parametryZakazky, idVybranehoObjektu);
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabulka");
				return;
			}
			hlavniOkno.setWindow(4);
			break;
		case 1: // odhadova hmotnost, sem bych se neml dostat
			/**
			 * sem se teoreticky nikdy nedostanu protože dìlam pøíkaz uprav model(action comand == upravmodel)
			 *  (ve skuteènosti dìlam zadej odhadovou hmotnost) zachyti se to døív.
			 */
			JOptionPane.showMessageDialog(hlavniOkno, "Odhadova hmotnost, sem bych se nemel dostat, VyberOrUpravListener");
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "neco je sptne VyberorUpravListener");	
			break;
		}
		
	}
	/**
	 * Expedice a licí plány
	 * @param j
	 * @throws Exception 
	 */
	private void index3(int j) throws Exception{
		switch(j){
		case 0:
			/**
			 * Je to to samí
			 */
			index2(0);
			break;
		case 1:
			hlavniOkno.setWindow(4);
			break;
		case 2:
			hlavniOkno.setWindow(4);
			break;
		default: System.out.println("neco je sptne VyberorUpravListener");	
			break;
		}
		
	}
	
	/**
	 * Spusti se pri upravach
	 * @param i
	 * @param j
	 * @throws SQLException 
	 */
	private void index5(int j) throws SQLException{
		int selectedRow = table.getSelectedRow();
		String columnName = table.getColumnName(0);
		String [] parametry;
		switch(j){
		case 0:
			if(columnName.equalsIgnoreCase("ID zákazníka") || columnName.equalsIgnoreCase("Id_zakaznika")){
				hlavniOkno.setWindow(0);
				tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
				String jmenoZakaznika = (String) this.table.getValueAt(table.getSelectedRow(), 1);
				String idzakaznika = (String) this.table.getValueAt(table.getSelectedRow(), 0);
				tmp.setUpravZakaznika(idzakaznika, jmenoZakaznika);
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabulka");
				return;
			}
			break;
		case 1:
			if(columnName.equalsIgnoreCase("ID modelu") || columnName.equalsIgnoreCase("Id modelu")){
				hlavniOkno.setWindow(1);
				tmp3 = (PromOknoNovyModel) promOkna[1];
				selectedRow = table.getSelectedRow();
				parametry = new String [table.getColumnCount()-1];
				for(int m = 0; m < parametry.length; m++){
					parametry[m] = (String) this.table.getValueAt(selectedRow, m);
				}
				tmp3.setUpravModel(parametry);
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabulka");
				return;
			}
			break;
		case 2:
			hlavniOkno.setWindow(2);
			tmp4 = (PromOknoNovaZakazka) promOkna[2];
			selectedRow = table.getSelectedRow();
			parametry = new String [table.getColumnCount()-1]; // mam tam nastaveny jeden sloupec navic (nastaveny v Querry table modelu, kvuli CollumAdjusteru)
			for(int m = 0; m < parametry.length; m++){
				parametry[m] = (String) this.table.getValueAt(selectedRow, m);
			}
			tmp4.setUpravZakazku(parametry);
			break;
		default: System.out.println("neco je sptne VyberorUpravListener");	
			break;
		}
	}
	
	/**
	 * Spustí se pøi upravach viniku a vad
	 * @param j
	 * @throws Exception
	 */
	private void index6(int j) throws Exception {
		String columnName = table.getColumnName(0);
		switch(j){
		case 2:
			if(columnName.equalsIgnoreCase("ID viníka") || columnName.equalsIgnoreCase("Id_vinika")){
				hlavniOkno.setWindow(0);
				tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
				String jmenoVinika = (String) this.table.getValueAt(table.getSelectedRow(), 1);
				String idVinika = (String) this.table.getValueAt(table.getSelectedRow(), 0);
				tmp.setUpravVinika(idVinika, jmenoVinika);
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabulka");
				return;
			}
			break;
		case 3:
			if(columnName.equalsIgnoreCase("ID vady") || columnName.equalsIgnoreCase("idvady")){
				hlavniOkno.setWindow(0);
				tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
				String jmenoVady = (String) this.table.getValueAt(table.getSelectedRow(), 1);
				String idVady = (String) this.table.getValueAt(table.getSelectedRow(), 0);
				tmp.setUpravVadu(idVady, jmenoVady);
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabulka");
				return;
			}
			break;
		case 1:
			break;
		case 0:
			break;
		default: System.out.println("neco je sptne VyberorUpravListener index6()");	
			break;
		}
	}
	
	/** 
	 * Obsluha smazani
	 * @param j 
	 * @throws Exception 
	 */
	private void index7(int j) throws Exception {
		switch(j){
		case 0:
			// stejny jako index1(0);
			String [] parametryZakazky = null;
			int selectedRow = table.getSelectedRow();
			parametryZakazky = new String [table.getColumnCount()-1]; // mam tam nastaveny jeden sloupec navic (nastaveny v Querry table modelu, kvuli CollumAdjusteru)
			for(int m = 0; m < parametryZakazky.length; m++){
				parametryZakazky[m] = (String) this.table.getValueAt(selectedRow, m);
			}
			String columnName = table.getColumnName(0);
			if(columnName.equalsIgnoreCase("ID zakázky") || columnName.equalsIgnoreCase("ID_zakazky")){
				ResultSet fyzKusy = sql.vyberFyzKusy(idVybranehoObjektu).rs;
				planovani.setPlanovaniAndSmaz(parametryZakazky, fyzKusy, idVybranehoObjektu);
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabulka");
				return;
			}
			hlavniOkno.setWindow(3);
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "neco je spatne VyberorUpravListener index7()");	
			break;
		}
		
	}
	
	/**
	 * Záloha, obnova databáze, prideleni privilegia
	 * @param j index, ktery popup menu se dìla (obnova nebo zaloha, vìtšinou zaloha, privilegia)
	 * @throws Exception 
	 */
	private void index8(int j) throws Exception {
		switch(j){
		case 0:
			ProgresBarFrame bar = sklad.getBar();
			bar.setZalohaDB();
			bar.setVisible(true);
			ZalohujDB zaloha = new ZalohujDB(this.sklad, ZalohujDB.ZALOHUJ);
			zaloha.execute();
			break;
		case 1:
			JOptionPane.showMessageDialog(hlavniOkno, "Vyberte soubor zalohy se jmenem \"<datum>_seznam_zakazek\", zbytek souborù musí být ve stejné složce");
			File curDirectory = new File("./");
			File obnovDBSqlFile = null, seznamZakazekCSVFile = null;
			JFileChooser chooser = new JFileChooser(curDirectory);
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("Soubor CSV", "csv");
		    chooser.setFileFilter(filter);
		    chooser.setName("Kokos");
		    chooser.setDialogTitle("Vyberte soubor seznam_zakazek.csv");
		    
		    int returnVal = chooser.showOpenDialog(this.hlavniOkno);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       seznamZakazekCSVFile = chooser.getSelectedFile();
		        // vybrat sql file pro strukturu databaze
		       JOptionPane.showMessageDialog(hlavniOkno, "Vyberte soubor strukturaDB.sql");
			
		       filter = new FileNameExtensionFilter("Soubor SQL", "sql");
		       chooser.setFileFilter(filter);
		       chooser.setCurrentDirectory(curDirectory);
		       chooser.setDialogTitle("Vyberte soubor strukturaDB.sql");
		       returnVal = chooser.showOpenDialog(this.hlavniOkno);
		       if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	   obnovDBSqlFile = chooser.getSelectedFile();
		    	   
		    	   System.out.println("parent "+ obnovDBSqlFile.getParent());
		    	   
		    	   // obnova DB
		    	   hlavniOkno.setEnabled(false);
		    	   int pom = this.sql.obnovaDB(obnovDBSqlFile, seznamZakazekCSVFile);
		    	   hlavniOkno.setEnabled(true);
		    	   if(pom == SQLStor.obnovaUspech){
		    		   JOptionPane.showMessageDialog(hlavniOkno, "Obnova byla provedena");
		    	   } 
		       } else {
		    	   JOptionPane.showMessageDialog(hlavniOkno, "Obnova nebyla provedena");
		       }
		    } else {
		    	   JOptionPane.showMessageDialog(hlavniOkno, "Obnova nebyla provedena");
		    }
		    
		   
			break;
		case 2: // prideleni privilegia
			JOptionPane.showMessageDialog(hlavniOkno, "Vyberte soubor hesla.xml");
			File heslaXMLFile = null;
			JFileChooser myChooser = new JFileChooser(new File("./"));
			FileNameExtensionFilter myFfilter = new FileNameExtensionFilter("Soubor XML", "xml");
			myChooser.setFileFilter(myFfilter);
			myChooser.setName("Kokos");
			myChooser.setDialogTitle("Vyberte soubor seznam_zakazek.csv");

			int returnValue = myChooser.showOpenDialog(this.hlavniOkno);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				heslaXMLFile = myChooser.getSelectedFile();
				ProgresBarFrame f = sklad.getBar();
				PridelitPrivilegia p = new PridelitPrivilegia(hlavniOkno, f, heslaXMLFile, sklad);
				f.setVisible(true);
				p.execute();
			}
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "neco je spatne VyberorUpravListener index8()");	
			break;
		}
	}
	
}
