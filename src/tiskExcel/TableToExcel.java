package tiskExcel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import sablony.tabulka.QueryTableModel;

/**
 * Tøída pro export Table model do .xls souboru (Excel soubor). Návrh øešení problému:
 * 1. vytvoøit metodu se switchem, která podle èísla vypisu navratí název sheetu, nadpis, cestu kde uložit vèetnì jména
 * 2. vytvoøit metodu která nám vrátí kolik která bunka má šíøku a kolik øádek jsou nadpisy. 
 * 	 možná jen boolean jestli je to nutné, protože jediny kdo potøebuje víc øádkù
 *   je Licí plán - planovací - nezapomen doplnit poznamku do sloupce
 * 3. vytvoøit metodu která vloží data do xls souboru  (insertData(.))
 * Rozložení stránek (okraje stranky) budou pro všechny výpisy stejné
 * @author Havlicek
 *
 */
public class TableToExcel {
	public final static int liciPlanZakl = 21;
	public final static int liciPlanPlanovaci = 22;
	private JFrame hlavniOkno;
	private String [] columnNamesNotInt = {"Èíslo modelu", "Cislo_modelu","Jméno zákazníka","Èíslo objednávky",
			"Jméno modelu", "Materiál", "Vlastní materiál"};
	
	/**
	 * Vytvoøí .xls soubor do pøedem dané složky s jednoøádkovou hlavièkou.
	 * (Doporuèení: údaje by se mìli vejít na A4 na výšku)
	 * @param hlavniOkno pouzití na zobrazení chyby (metoda export())
	 * @param model ze kterého èerpáme data. Musí být <code>QueryTableModel</code>
	 * @param nadpisExt rozšíøení nadpisu v tisk (obvykle datum)
	 * @param name jméno souboru bez koncovky
	 * @param cisloExportu èíslo exportu (èíslo šablony, kterou použijeme pro soubor xls)
	 * @throws Exception vyhodí chybu, pokud nìco nesouhlasí
	 */
	public static void exportToExcelNaVysku(JFrame hlavniOkno, TableModel model, String nadpisExt, String name, int cisloExportu) throws Exception{
		new TableToExcel(hlavniOkno, model, nadpisExt, name, cisloExportu, true);
	}
	
	/**
	 * Vytvoøí .xls soubor do pøedem dané složky specialnì pro lici plan- Bude vice radku
	 * (Doporuèení: údaje by se mìli vejít na A4 na šíøku)
	 * @param hlavniOkno pouzití na zobrazení chyby (metoda export())
	 * @param model ze kterého èerpáme data. Musí být <code>QueryTableModel</code>
	 * @param nadpisExt rozšíøení nadpisu v tisk (obvykle datum)
	 * @param name jméno souboru bez koncovky
	 * @param cisloExportu èíslo exportu (èíslo šablony, kterou použijeme pro soubor xls)
	 * @throws Exception vyhodí chybu, pokud nìco nesouhlasí
	 */
	public static void exportToExcelNaSirku(JFrame hlavniOkno, TableModel model, String nadpisExt, String name, int cisloExportu) throws Exception{
		new TableToExcel(hlavniOkno, model, nadpisExt, name, cisloExportu, false);
	}
	
	/**
	 * Vytvoøí tøídu, která vytvoøí .xls soubor do pøedem dané složky s jednoøádkovou hlavièkou.
	 * (Doporuèení: údaje by se mìli vejít na A4 na výšku)
	 * @param hlavniOkno pouzití na zobrazení chyby (metoda export())
	 * @param model ze kterého èerpáme data. Musí být <code>QueryTableModel</code>
	 * @param nadpisExt rozšíøení nadpisu v tisk (obvykle datum)
	 * @param name jméno souboru bez koncovky
	 * @param cisloExportu èíslo exportu (èíslo šablony, kterou použijeme pro soubor xls)
	 * @param isNaVysku zda je tisk na výšku nebo šíøku. Výšku = <code>true</code>
	 * @throws Exception vyhodí chybu, pokud nìco nesouhlasí
	 */
	public TableToExcel(JFrame hlavniOkno, TableModel model,String nadpisExt, String name, int cisloExportu, boolean isNaVysku) throws Exception{
		this.hlavniOkno = hlavniOkno;
		this.export((QueryTableModel) model, nadpisExt, name, cisloExportu, isNaVysku);
	}
	
	/**
	 * Metoda, kde dìje hlavní algoritmus. Zde se vše pøevádí
	 * @param model model, ze kretého èerpáme data
	 * @param name nazev souboru bez koncovky
	 * @param nadpisExt rozšíøení nadpisu v tisk (obvykle datum)
	 * @param cisloExportu druh vypisu, podle kterého pøizpùsobíme .xls soubor
	 * @throws Exception 
	 */
	private void export(QueryTableModel model, String nadpisExt, String name, int cisloExportu, boolean isNaVysku) throws Exception{
		HSSFWorkbook wb = new HSSFWorkbook();
		String [] atr = this.getAtributes(cisloExportu);
		HSSFSheet sheet = wb.createSheet(atr[0]);
		
		//set header (nadpis v tisku)
		Header header = sheet.getHeader();
		header.setCenter(HSSFHeader.font("Stencil-Normal", "bold")+ HSSFHeader.fontSize((short) 14)+ atr[1] + nadpisExt);// + nadpisExt);
		
		sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);

		sheet.getPrintSetup().setLandscape(!isNaVysku);
		
		
		//number of pages
		Footer footer = sheet.getFooter();
		footer.setRight("Strana " + HeaderFooter.page() + " z " + HeaderFooter.numPages());
		//set visibile grid lines on printed pages
		sheet.setPrintGridlines(true);
	    
		sheet.setMargin(Sheet.BottomMargin, 0.5);
		sheet.setMargin(Sheet.TopMargin, 0.6);
		sheet.setMargin(Sheet.LeftMargin, 0.3);
		sheet.setMargin(Sheet.RightMargin, 0.3);
		
		sheet.setMargin(Sheet.HeaderMargin, 0.1);
		sheet.setMargin(Sheet.FooterMargin, 0.3);
		//insert data
		this.insertData(model, sheet, cisloExportu);
		//set First row as header at all printed pages
		sheet.setRepeatingRows(CellRangeAddress.valueOf("1:1"));	
		
		// Write it into the output to a file
		
		//create folder
		File f = new File(atr[2]);
		try {
			if (f.mkdir()) {
				//System.out.println("Directory Created");
			} else {
				//System.out.println("Directory is not created");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(hlavniOkno, "Složka nebyla vytvoøena");
		}
		
		try {
			FileOutputStream fileOut = new FileOutputStream(atr[2]+"/"+name+".xls");
			wb.write(fileOut);
			wb.close();
			fileOut.close();
			JOptionPane.showMessageDialog(hlavniOkno, "Excel soubor "+atr[2].substring(2, atr[2].length())+"/"+name+".xls vytvoøen.");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(hlavniOkno, "Máte otevøený soubor, do kterého se zapisuje. Zavøete jej prosím");
			wb.close();
		}
		
		/*FileOutputStream fileOut = new FileOutputStream("./vypisy/"+name+".xls");
		wb.write(fileOut);
		wb.close();
		fileOut.close();*/
	}
	
	/**
	 * Metoda pro vložení dat do tabulky, ještì bych mìl zvážit že pøidam parametr pro mergovaní bunìk.
	 * Ještì musím vymyslet jak. Nìjaky jednoduhcy øešení. :)
	 * @param model ze kterého budeme èíst data
	 * @param sheet prazdny sheet do kterého budeme data vkládat
	 * @param cisloExportu èíslo exportu
	 * @throws Exception
	 */
	private void insertData(QueryTableModel model, HSSFSheet sheet, int cisloExportu) throws Exception{
		Row row = sheet.createRow(0);
		Cell cell = null;
		//insert Column name
		for(int i = 0; i < model.getColumnCount() -1 ; i++){//mam totiž jeden sloupec navic aby se mi srovnali tabulky viz QuerytableModel
			cell = row.createCell(i);
			cell.setCellValue(model.getColumnName(i));
		}
		//detect data format
		boolean [] isNumber = detectDataFormat(model); 
		//insert data 
		for(int i = 1; i <= model.getRowCount(); i++){
			row = sheet.createRow(i);
			for(int j = 0; j < model.getColumnCount() -1 ; j++){ //mam totiž jeden sloupec navic aby se mi srovnali tabulky viz QuerytableModel
				cell = row.createCell(j);
				//cell.getCellStyle().setWrapText(true);
				try {
					if (isNumber[j]) {
						if (model.getValueAt(i - 1, j).length() > 0) {
							cell.setCellValue(Double.parseDouble((model.getValueAt(i - 1, j))));
						}
					} else {
						cell.setCellValue(model.getValueAt(i - 1, j));
					}
				} catch (NumberFormatException | NullPointerException ex ) {
					isNumber[j] = false;
					cell.setCellValue(model.getValueAt(i - 1, j));
				}
			}
		}
		//Format data
		for(int i = 0; i < model.getColumnCount() -1 ; i++){//mam totiž jeden sloupec navic aby se mi srovnali tabulky viz QuerytableModel
			sheet.autoSizeColumn(i);
		}
	}
	
	/**
	 * Metoda pro zjištìní atributù výpisu. Navrátí String pole o 3 prvcích v poøadí:
	 * <ol>
	 *  <li>Jméno sheetu</li>
	 *  <li>Nadpis v tisku papíru</li>
	 *  <li>Cestu kam uložit (relativní)</li>
	 * </ol>
	 * @param cisloExportu Èíslo exportu, o kterém chceme znát atributy
	 * @return String [] o 3 prvcích nikdy <code>null</code>
	 */
	private String [] getAtributes(int cisloExportu){
		String [] atr = {"prazdnyatr1","prazdnyatr2","prazdnyatr3"};
		/**
		 * Kvuli tomu aby jmena souboru mìli èisla od 1 .. n a ne od 0 tak zvìtším i o 1
		 * viz metoda vypisyAndTisk(.,.) v HledejListener.java
		 */
		switch(cisloExportu){
		case 0:
			atr[0] = "Stav neuzavøených zakázek";atr[1] = "Stav neuzavøených zakázek ke dni ";atr[2] = "./vypisy";
			break;
		case 1:
			atr[0] = "Výpis odlitých kusù";atr[1] = "Výpis odlitých kusù ke dni ";atr[2] = "./vypisy";
			break;
		case 2:
			atr[0] = "Výpis vyèištìných kusù za období";atr[1] = "Vyèištìné kusy od ";atr[2] = "./vypisy";
			break;
		case 3:
			atr[0] = "Mzdy slévaèù";atr[1] = "Mzdy slévaèù ke dni ";atr[2] = "./vypisy";
			break;
		case 4:
			atr[0] = "Výpis odlitkù v kg-kè za období";atr[1] = "Výpis odlitkù v kg-kè od " ;atr[2] = "./vypisy";
			break;
		case 5:
			atr[0] = "Výpis vyrobených kusù za období";atr[1] = atr[0];atr[2] = "./vypisy";
			break;
		case 6:
			atr[0] = "Výpis položek s odhadovanou hmotností";atr[1] = "Položky s odhadovou hmotností ke dni ";atr[2] = "./vypisy";
			break;
		case 7:
			atr[0] = "Výpis zakázek s termínem expedice v daném týdnu";atr[1] = atr[0];atr[2] = "./vypisy";
			break;
		case 8:
			atr[0] = "Výpis expedice zboží za období";atr[1] = "Expedice zboží od ";atr[2] = "./vypisy";
			break;
		case 9:
			atr[0] = "Výpis zpoždìné výroby ke dni";atr[1] = atr[0];atr[2] = "./vypisy";
			break;
		case 10:
			atr[0] = "Inventura rozpracované výroby";atr[1] = "Rozpracovaná výroba ke dni ";atr[2] = "./vypisy";
			break;
		case 11:
			atr[0] = "Výpis skladu ke dnešnímu dni";atr[1] = "Seznam kusù na skladì ke dni ";atr[2] = "./vypisy";
			break;
		case 12:
			atr[0] = "Výpis zmetkù za období";atr[1] = "Výpis zmetkù od ";atr[2] = "./vypisy";
			break;
		case 13:
			atr[0] = "Výpis viníkù za období";atr[1] = "Výpis viníkù od ";atr[2] = "./vypisy";
			break;
		case TableToExcel.liciPlanZakl:
			atr[0] = "Základni licí plán";atr[1] = "Základni licí plán pro týden: ";atr[2] = "./lici_plany";
			break;
		case TableToExcel.liciPlanPlanovaci:
			atr[0] = "Výpis skladu ke dnešnímu dni";atr[1] = "Licí plán pro týden: ";atr[2] = "./lici_plany";
			break;
		}
		return atr;		
	}
	
	/**
	 * Detects whether column is a number or not based on column name or column value.
	 * @param model Table with values a column names
	 * @return boolean field with n-1 columns of the table with true or false values
	 */
	private boolean [] detectDataFormat(QueryTableModel model){
		boolean [] isNumber = null;
		if(model.getRowCount() > 0){
			isNumber = new boolean [model.getColumnCount()-1];
			/**
			 * Moc se tady nechapu ale doposud to funguje tak to nebudu menit
			 */
			boolean exit = false;
			for (int m = 0; m < 2; m++) { // kontroluju prvni dve radky
				for (int j = 0; j < model.getColumnCount() - 1; j++) { // mam totiž jeden sloupec navic aby se mi srovnali tabulky viz QuerytableModel
					String tmp = model.getValueAt(m, j);
					try {
						if (tmp != null) {
							exit = false;
							for(int i = 0; i < columnNamesNotInt.length;i++){ // zda sloupec neni nahodou povinny String a ne cislo
								if(columnNamesNotInt[i].equalsIgnoreCase(model.getColumnName(j))){
									isNumber[j] = false;
									exit = true;
									break;
								}
							}
							if(exit){continue;}
							
							Double.parseDouble((tmp));
							if(m > 0){
								isNumber[j] = isNumber[j] && true; // ten and je jen pro nazornost // druhy radek tabulky
							}else {
								isNumber[j] = true; // prvni radek
							}
						} else {
							isNumber[j] = false;
						}
					} catch (NumberFormatException nfe) {
						isNumber[j] = false;
					}
				}
			}
		}
		return isNumber;
	}
}
