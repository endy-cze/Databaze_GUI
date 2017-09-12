package tiskExcel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.DateFormatConverter;

import app.MainFrame;
import sablony.ParametryFiltr;
import sablony.tabulka.QueryTableModel;

/**
 * Tøída pro export Table model do .xls souboru (Excel soubor).
 * Rozložení stránek (okraje stranky) budou pro všechny výpisy stejné, kromì vypisZmetku. Ten ma specialni sablonu.
 * Tøída se snaží detekovat jaky data type obsahuji sloupce a podle toho je pa ukladada.
 * @author Havlicek
 *
 */
public class TableToExcel {
	public static final int NUMERICDATA = 89;
	public static final int STRINGDATA = 90;
	public static final int DATEDATA = 91;
	
	private static final int KUM_ROZDIL_COLUMN = 4;
	private static final int OBSAZENOST_PROCENTA = 5;
	
	private JFrame hlavniOkno;
	//private static String [] columnNamesIsString = {"Èíslo modelu", "Cislo_modelu","Jméno zákazníka","Èíslo objednávky",
	//		"Jméno modelu", "Materiál", "Vlastní materiál", "Cislo_tavby", "Èíslo tavby"};
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
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
		String [] atr = getAtributes(cisloExportu);
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
		
		// fonty a formaty pro bunky		
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		HSSFCellStyle dateStyle = wb.createCellStyle();
		dateStyle.setFont(font);
		DataFormat poiDateFormat = wb.createDataFormat();
	    String excelFormatPattern = DateFormatConverter.convert(new Locale("Cz", "cs"), "d.M.yyyy");
		short dateFormat = poiDateFormat.getFormat(excelFormatPattern);
		dateStyle.setDataFormat(dateFormat);
		
		HSSFCellStyle doubleStyle = wb.createCellStyle();
		doubleStyle.setDataFormat(wb.createDataFormat().getFormat("0.00 %"));
		doubleStyle.setFont(font);
		
		//insert data
		this.insertData(model, sheet, cisloExportu, font, dateStyle, doubleStyle);
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
	}
	
	/**
	 * Metoda pro vložení dat do tabulky, ještì bych mìl zvážit že pøidam parametr pro mergovaní bunìk.
	 * Ještì musím vymyslet jak. Nìjaky jednoduhcy øešení. :)
	 * @param model ze kterého budeme èíst data
	 * @param sheet prazdny sheet do kterého budeme data vkládat
	 * @param cisloExportu èíslo exportu
	 * @param font font ktery použijeme v bunkach
	 * @throws Exception
	 */
	private void insertData(QueryTableModel model, HSSFSheet sheet, int cisloExportu, HSSFFont font, HSSFCellStyle dateStyle, HSSFCellStyle procentaStyle) throws Exception{
		Row row = sheet.createRow(0);
		Cell cell = null;
		
		//insert Column name
			
		cell = row.createCell(0);
		cell.setCellValue(model.getColumnName(0));
		// naveni fontu
		cell.getCellStyle().setFont(font);
		
		for(int i = 1; i < model.getColumnCount() -1 ; i++){ //mam totiž jeden sloupec navic aby se mi srovnali tabulky viz QuerytableModel
			cell = row.createCell(i);
			cell.setCellValue(model.getColumnName(i));
		}
		//detect data format
		int [] isNumber = detectDataFormat(model, sdf); 
		//insert data 
		for(int i = 1; i <= model.getRowCount(); i++){
			row = sheet.createRow(i);
			for(int j = 0; j < model.getColumnCount() -1 ; j++){ //mam totiž jeden sloupec navic aby se mi srovnali tabulky viz QuerytableModel
				cell = row.createCell(j);
				try {
					if (isNumber[j] == NUMERICDATA) {
						if (model.getValueAt(i - 1, j).length() > 0) {
							cell.setCellValue(Double.parseDouble((model.getValueAt(i - 1, j))));
						}
					} else if (isNumber[j] == DATEDATA) {
						cell.setCellValue(sdf.parse(model.getValueAt(i - 1, j)));
						cell.setCellStyle(dateStyle);
					} else {
						cell.setCellValue(model.getValueAt(i - 1, j));
					}
				} catch (NumberFormatException | NullPointerException | ParseException ex ) {
					//isNumber[j] = STRINGDATA;
					cell.setCellValue(model.getValueAt(i - 1, j));
				}
			}
		}
		//Format data
		for(int i = 0; i < model.getColumnCount() -1 ; i++){//mam totiž jeden sloupec navic aby se mi srovnali tabulky viz QuerytableModel
			sheet.autoSizeColumn(i);
		}
		
		// Create export-specified formulas
		createFormulas(cisloExportu, sheet, procentaStyle);
	}
	
	private void createFormulas(int cisloExportu, HSSFSheet sheet, HSSFCellStyle procentaStyle){
		// https://poi.apache.org/spreadsheet/formula.html
		int rowsCount = sheet.getPhysicalNumberOfRows();
		Row r;
		Cell c;
		switch(cisloExportu){
		case ParametryFiltr.VypisVytizeniKapacit:
			/* Vytvorit vzorce ktere si preji */
			// Kum. rozdil
			for(int i = 1; i < rowsCount; i++){
				r = sheet.getRow(i);
				c = r.getCell(KUM_ROZDIL_COLUMN);
				c.setCellFormula("D"+(i+1)+"-C"+(i+1));
				c = r.getCell(OBSAZENOST_PROCENTA);
				c.setCellFormula("D"+(i+1)+"/C"+(i+1));
				c.setCellStyle(procentaStyle);
			}
			break;
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
	private static String [] getAtributes(int cisloExportu){
		String [] atr = {"prazdnyatr1","prazdnyatr2","prazdnyatr3"};
		/**
		 * Kvuli tomu aby jmena souboru mìli èisla od 1 .. n a ne od 0 tak zvìtším i o 1
		 * viz metoda vypisyAndTisk(.,.) v HledejListener.java
		 */
		switch(cisloExportu){
		case ParametryFiltr.VypisStavNeuzavrenychZakazek:
			atr[0] = "Stav neuzavøených zakázek";atr[1] = "Stav neuzavøených zakázek ke dni ";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.DenniVypisOdlitychKusu:
			atr[0] = "Výpis odlitých kusù";atr[1] = "Výpis odlitých kusù ke dni ";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.VypisVycistenychKusuZaObdobi:
			atr[0] = "Výpis vyèištìných kusù za období";atr[1] = "Vyèištìné kusy od ";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.MzdySlevacu:
			atr[0] = "Mzdy slévaèù";atr[1] = "Mzdy slévaèù od ";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.VypisOdlitkuVKgKc:
			atr[0] = "Výpis odlitkù v kg-Kè za období";atr[1] = "Výpis odlitkù v kg-Kè od " ;atr[2] = "./vypisy";
			break;
		case ParametryFiltr.VypisOdlitychKusuOdDo:
			atr[0] = "Výpis odlitých kusù za období";atr[1] = "Výpis odlitých kusù od ";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.VypisPolozekSOdhadHmot:
			atr[0] = "Výpis položek s odhadovanou hmotností";atr[1] = "Položky s odhadovou hmotností ke dni ";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.VypisDleTerminuExpedice:
			atr[0] = "Výpis zakázek s termínem expedice v daném týdnu";atr[1] = atr[0];atr[2] = "./vypisy";
			break;
		case ParametryFiltr.VypisExpedice_od_do:
			atr[0] = "Výpis expedice zboží za období";atr[1] = "Expedice zboží od ";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.VypisZpozdeneVyroby:
			atr[0] = "Výpis zpoždìné výroby ke dni";atr[1] = atr[0];atr[2] = "./vypisy";
			break;
		case ParametryFiltr.InventuraRozpracVyroby:
			atr[0] = "Inventura rozpracované výroby";atr[1] = "Rozpracovaná výroba ke dni ";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.VypisSkladuKeDnesnimuDni:
			atr[0] = "Výpis skladu ke dnešnímu dni";atr[1] = "Seznam kusù na skladì ke dni ";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.VypisZmetkuZaObdobi:
			atr[0] = "Výpis zmetkù za období";atr[1] = "Výpis zmetkù od ";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.VypisVinikuVKgKcMzdy:
			atr[0] = "Výpis viníkù v kg-Kè období";atr[1] = "Výpis viníkù v kg/Kè od ";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.VypisVytizeniKapacit:
			atr[0] = "Výpis vytížení kapacit";atr[1] = "Výpis vytížení kapacit na 10 týdnù";atr[2] = "./vypisy";
			break;
		case ParametryFiltr.ZaklPlanLiti:
			atr[0] = "Základní licí plán";atr[1] = "Základní licí plán pro týden: ";atr[2] = "./lici_plany";
			break;
		case ParametryFiltr.PlanovaniLiti:
			atr[0] = "Licí plán";atr[1] = "Licí plán pro týden: ";atr[2] = "./lici_plany";
			break;
		case ParametryFiltr.PlanExpedice:
			atr[0] = "Plán expedice";atr[1] = "Plán expedice ke dni: ";atr[2] = "./lici_plany";
			break;
			}
		return atr;		
	}
	
	/**
	 * Detects whether column is a number or not based on column name or column value.
	 * @param model Table with values a column names
	 * @return int field with n-1 columns of the table with values of data type
	 */
	private static int [] detectDataFormat(QueryTableModel model, SimpleDateFormat sdf){
		int [] isNumber = new int [model.getColumnCount() - 1];
		for(int i = 0; i < model.getColumnCount() - 1; i++){
			Class<?> pom = model.getColumnClass(i);
			if(pom.getName().equals(String.class.getName())){
				isNumber[i] = STRINGDATA;
			} else if(pom.getName().equals(Integer.class.getName())){
				isNumber[i] = NUMERICDATA;
			} else if(pom.getName().equals(Double.class.getName())){
				isNumber[i] = NUMERICDATA;
			} else if(pom.getName().equals(java.util.Date.class.getName())){
				isNumber[i] = DATEDATA;
			} else if(pom.getName().equals(Boolean.class.getName())){
				isNumber[i] = STRINGDATA;
			} else {
				isNumber[i] = STRINGDATA;
			}
		}
		
		/*
		if(model.getRowCount() >= 2){
			isNumber = new int [model.getColumnCount()-1];
			// pokud ma model vice nez dve radky
			boolean exit = false;
			for (int m = 0; m < 2; m++) { // kontroluju prvni dve radky
				for (int j = 0; j < model.getColumnCount() - 1; j++) { // mam totiž jeden sloupec navic aby se mi srovnali tabulky viz QuerytableModel
					String tmp = model.getValueAt(m, j);
					if (tmp != null) {
						exit = false;
						String colName = model.getColumnName(j);
						for(int i = 0; i < columnNamesIsString.length;i++){ // zda sloupec neni nahodou povinny String a ne cislo
							if(columnNamesIsString[i].equalsIgnoreCase(model.getColumnName(j))){
								isNumber[j] = STRINGDATA;
								exit = true;
								break;
							}
						}
						if(exit){continue;}
						// test zda to je cislo
						try {
							Double.parseDouble((tmp));
							if(m == 0){
								isNumber[j] = NUMERICDATA; // ten and je jen pro nazornost // druhy radek tabulky
							}else {
								isNumber[j] = isNumber[j]; // prvni radek
							}
							continue;						
						} catch (NumberFormatException nfe) {
							// nothin
						}
						// test zda to je datum
						try {
							sdf.parse(tmp);
							isNumber[j] = DATEDATA; // staci pouze aby jeden z techto dvou radku byl datum
						} catch(ParseException  e){
							if(m == 0)isNumber[j] = STRINGDATA;
						}
					} else {
						if(isNumber[j] != DATEDATA)
							isNumber[j] = STRINGDATA;
					}
				}
			}
		} else {
			isNumber = new int [model.getColumnCount()-1];
			for(int i = 0; i < model.getColumnCount() - 1; i++){
				isNumber[i] = STRINGDATA;
			}
		}
		*/
		return isNumber;
	}
	
	/**
	 * Metoda pro vytvoreni specialniho vypisu zmetku pro sefa.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void vypisZmetkuZmdyToExcel(QueryTableModel tm, MainFrame hlavniOkno, String nadpisExt, String name) throws IOException, ParseException{
		HSSFWorkbook wb = new HSSFWorkbook();
		String [] atr = getAtributes(ParametryFiltr.VypisZmetkuZaObdobi);
		HSSFSheet sheet = wb.createSheet(atr[0]);
		
		//set header (nadpis v tisku)
		Header header = sheet.getHeader();
		header.setCenter(HSSFHeader.font("Stencil-Normal", "bold")+ HSSFHeader.fontSize((short) 16)+ atr[1] + nadpisExt);// + nadpisExt);
		
		sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
		// nasvateni na vysku
		sheet.getPrintSetup().setLandscape(false);
		
		
		//number of pages
		Footer footer = sheet.getFooter();
		footer.setRight("Strana " + HeaderFooter.page() + " z " + HeaderFooter.numPages());
		//set visibile grid lines on printed pages
		//sheet.setPrintGridlines(true);
	    
		sheet.setMargin(Sheet.BottomMargin, 0.5);
		sheet.setMargin(Sheet.TopMargin, 0.6);
		sheet.setMargin(Sheet.LeftMargin, 0.3);
		sheet.setMargin(Sheet.RightMargin, 0.3);
		
		sheet.setMargin(Sheet.HeaderMargin, 0.1);
		sheet.setMargin(Sheet.FooterMargin, 0.3);
		
		/**
		 * Pokud zmením velikost pisma (14 nebo 13?) tak musím
		 * zmìnit i èíslo ZMETKUNASTRANKU a POCETRADEKNASTRANKU podle toho.
		 */
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 13);
		HSSFCellStyle style = wb.createCellStyle();
		style.setBorderTop(CellStyle.BORDER_DOUBLE);
		style.setFont(font);
		HSSFCellStyle dateStyle = wb.createCellStyle();
		dateStyle.setFont(font);
		DataFormat poiDateFormat = wb.createDataFormat();
	    String excelFormatPattern = DateFormatConverter.convert(new Locale("Cz", "cs"), "d.M.yyyy");
		short dateFormat = poiDateFormat.getFormat(excelFormatPattern);
		dateStyle.setDataFormat(dateFormat);
		
		//insert data
		insertDataToVypisZmetkyMzdy(tm, sheet, font, style, dateStyle);
		
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
		
	}
	
	private static void insertDataToVypisZmetkyMzdy(QueryTableModel model, HSSFSheet sheet, HSSFFont font, HSSFCellStyle style, HSSFCellStyle dateStyle) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		int indexOfBeginRow = 0;
		for(int i = 0; i < model.getRowCount(); i++){
			if(i % ZMETKUNASTRANKU == 0 && i != 0){
				int s = indexOfBeginRow % POCETRADEKNASTRANKU;
				indexOfBeginRow += POCETRADEKNASTRANKU - s;
			}
			indexOfBeginRow = insertZmetekDoExcelu(model.getRow(i), sheet, indexOfBeginRow, sdf, font, style, dateStyle);
		}
	}
	
	/**
	 * Indexy sloupcu v tabulce
	 */
	public static final int JMENOZAKAZNIKA = 0;
	public static final int JMENOMODELU = 1;
	public static final int CISLOMODELU = 2;
	public static final int DATUMZMETKU = 3;
	public static final int JMENOVINIKA = 4;
	public static final int VADA = 5;
	public static final int KS = 6;
	public static final int NORMA = 7;
	public static final int NORMACELKEM = 8;
	public static final int HMOTNOSTNAKUS = 9;
	public static final int HMOTNOSTCELKEM = 10;
	public static final int VLASTNIMATERIAL = 11;
	public static final int CENAZAKUS = 12;
	public static final int CENACELKEM = 13;
	
	
	public static final int ZMETKUNASTRANKU = 4;
	public static final int POCETRADEKNASTRANKU = 47;
	
	/**
	 * Vloží do Excelu podle pøedem dané šablony jednu "bunku" ktera, reprezentuje Pro danou zakazku a daný datum poèet zmetku, Jmeno zakaznika ...
	 * @throws ParseException 
	 */
	public static int insertZmetekDoExcelu(String [] data, HSSFSheet sheet, int indexOfBeginRow, SimpleDateFormat sdf,
			HSSFFont f, HSSFCellStyle style, HSSFCellStyle dateStyle) throws ParseException{
		// nastaveni tloustky bunky
		//cell.getCellStyle().setBorderBottom(border);
		
		Cell cell = null;
		Row row = null;
		row = sheet.createRow(indexOfBeginRow);		
		addRowValuesVypisZmetekMzdy(row, "Zákazník", data[JMENOZAKAZNIKA], STRINGDATA, null, null, STRINGDATA, sdf, f, dateStyle);
		indexOfBeginRow++;
		row = sheet.createRow(indexOfBeginRow);
		addRowValuesVypisZmetekMzdy(row, "Název", data[JMENOMODELU], STRINGDATA, "È. modelu", data[CISLOMODELU], STRINGDATA, sdf, f, dateStyle);
		indexOfBeginRow++;
		row = sheet.createRow(indexOfBeginRow);
		addRowValuesVypisZmetekMzdy(row, "Viník", data[JMENOVINIKA], STRINGDATA, "Druh vady", data[VADA], STRINGDATA, sdf, f, dateStyle);
		indexOfBeginRow++;
		row = sheet.createRow(indexOfBeginRow);
		addRowValuesVypisZmetekMzdy(row, "Datum", data[DATUMZMETKU], DATEDATA, "Kusù", data[KS], NUMERICDATA, sdf, f, dateStyle);
		indexOfBeginRow++;
		row = sheet.createRow(indexOfBeginRow);
		addRowValuesVypisZmetekMzdy(row, "Mzda za kus", data[NORMA], NUMERICDATA, "Mzda celkem", data[NORMACELKEM], NUMERICDATA, sdf, f, dateStyle);
		indexOfBeginRow++;
		row = sheet.createRow(indexOfBeginRow);
		addRowValuesVypisZmetekMzdy(row, "Hmotnost ks", data[HMOTNOSTNAKUS], NUMERICDATA, "Hmotnost celkem", data[HMOTNOSTCELKEM], NUMERICDATA, sdf, f, dateStyle);
		indexOfBeginRow++;
		row = sheet.createRow(indexOfBeginRow);
		addRowValuesVypisZmetekMzdy(row, "P. cena", data[CENAZAKUS], NUMERICDATA, "Cena celkem", data[CENACELKEM], NUMERICDATA, sdf, f, dateStyle);
		indexOfBeginRow++;
		row = sheet.createRow(indexOfBeginRow);
		addRowValuesVypisZmetekMzdy(row, "Materiál", data[VLASTNIMATERIAL], STRINGDATA, null, null, STRINGDATA, sdf, f, dateStyle);
		indexOfBeginRow++;
		row = sheet.createRow(indexOfBeginRow);
		
		cell = row.createCell(0, Cell.CELL_TYPE_STRING);
		cell.setCellValue("Platit mzdy");
		cell.setCellStyle(style);
		cell = row.createCell(1, Cell.CELL_TYPE_STRING);
		cell.setCellValue("ANO - NE");
		cell.setCellStyle(style);
		cell = row.createCell(2, Cell.CELL_TYPE_BLANK);
		cell.setCellStyle(style);
		cell = row.createCell(3, Cell.CELL_TYPE_BLANK);
		cell.setCellStyle(style);
		cell = row.createCell(4, Cell.CELL_TYPE_BLANK);
		cell.setCellStyle(style);
		indexOfBeginRow++;
		
		//volna radka
		indexOfBeginRow++;
		
		//Format data
		for(int i = 0; i < 5 ; i++){ // mam 4 sloupce
			sheet.autoSizeColumn(i);
		}
		// vytvoøit mezeru
		sheet.setColumnWidth(2, 2000);
		
		return indexOfBeginRow;
	}
	
	private static void addRowValuesVypisZmetekMzdy(Row row,
			String nazevHodnoty1, String hodnota1, int isNumber1,
			String nazevHodnoty2, String hodnota2, int isNumber2,
			SimpleDateFormat sdf, HSSFFont f, HSSFCellStyle dateStyle) throws ParseException {
		
		Cell cell;
		cell = row.createCell(0, Cell.CELL_TYPE_STRING);
		cell.setCellValue(nazevHodnoty1);
		cell.getCellStyle().setFont(f);
		if (isNumber1 == NUMERICDATA){
			cell = row.createCell(1, Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.parseDouble(hodnota1));
		} else  if (isNumber1 == DATEDATA){ // datum
			cell = row.createCell(1);
			cell.setCellValue(sdf.parse(hodnota1));
			cell.setCellStyle(dateStyle);
		} else {
			cell = row.createCell(1, Cell.CELL_TYPE_STRING);
			cell.setCellValue(hodnota1);
		}
		cell.getCellStyle().setFont(f);
		// mezera
		cell = row.createCell(2, Cell.CELL_TYPE_BLANK);
		cell.getCellStyle().setFont(f);
		
		cell = row.createCell(3, Cell.CELL_TYPE_STRING);
		cell.setCellValue(nazevHodnoty2);
		cell.getCellStyle().setFont(f);
		if (isNumber2  == NUMERICDATA){
			cell = row.createCell(4, Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Double.parseDouble(hodnota2));
		} else  if (isNumber2 == DATEDATA){ // datum
			cell = row.createCell(4);
			cell.setCellValue(sdf.parse(hodnota2));
			cell.setCellStyle(dateStyle);
		} else {
			cell = row.createCell(4, Cell.CELL_TYPE_STRING);
			cell.setCellValue(hodnota2);
		}
		cell.getCellStyle().setFont(f);
	}
}
