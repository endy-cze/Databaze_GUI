package tiskExcel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

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
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import sablony.tabulka.QueryTableModel;
import app.MainFrame;

public class PlanovaniLitiToExcelTisk {
	
	private MainFrame hlavniOkno;
	private SimpleDateFormat sdf;
	private static String [] columnNamesNotInt = {"Èíslo modelu", "Cislo_modelu","Jméno zákazníka","Èíslo objednávky",
		"Jméno modelu", "Materiál", "Vlastní materiál"};
	private final String [][] columnNamesPlanLiti = {
			{"Zákazník", "Jméno modelu", "Èíslo modelu", "Po", "Út", "St", "Èt", "Pá", "Celk."},
			{"Materiál", "Mater. 2", "Hmotnost", "Termín", "Objed.", "Odl – zm.", "Norma", "Norma celk."}
			};
	private final int [][] sirkyBunek = {
			{2,2,2,1,1,  1,1,1,1},
			{1,1,1,1,1,  1,3,3}
	};
	
	private final static int maxWidthMaterial = 256 * 9; // 9 je pocet písmen
	private final static int maxWidtColumnIndex= 1;
	private final static int paperSizeInChars= 11 * 9 - 2; // delka stranky A4 v poctu pismenech pismena velikosti 12
	private final static int widthTerminLiti = 256 * 12;
	private final static int terminLitiColumnIndex = 3;
	private final static int widthObjednano = 256 * 1	;
	private final static int objednanoColumnIndex = 256 * 7;
	
	private final static int TEXT = 1;
	private final static int CISLO = 2;
	private final int [][] dataFormat = {
			{TEXT,TEXT,TEXT,CISLO,CISLO,   CISLO,CISLO,CISLO,CISLO},
			{TEXT,TEXT,CISLO,TEXT,CISLO,   CISLO, CISLO, CISLO}
	};
	
	public static void exportPlanovaniLitiToExcel(MainFrame hlavniOkno, TableModel model,
			String nadpisExt, String name, SimpleDateFormat sdf) throws Exception{
		PlanovaniLitiToExcelTisk proces = new PlanovaniLitiToExcelTisk(hlavniOkno, (QueryTableModel) model, nadpisExt, name, sdf);
	}
	
	private PlanovaniLitiToExcelTisk(MainFrame hlavniOkno, QueryTableModel model,
			String nadpisExt, String name, SimpleDateFormat sdf) throws Exception{
		this.hlavniOkno = hlavniOkno;
		this.sdf = sdf;
		this.export(model, nadpisExt, name);;
		
	}
	
	private void export(QueryTableModel model, String nadpisExt, String name) throws Exception{
		HSSFWorkbook wb = new HSSFWorkbook();
		String [] atr = getAtributes();
		HSSFSheet sheet = wb.createSheet(atr[0]);
		
		// check konstanty
		//checkKonstants(sirkyBunek, columnNamesPlanLiti);	
		
		//set header (nadpis v tisku)
		Header header = sheet.getHeader();
		header.setCenter(HSSFHeader.font("Stencil-Normal", "bold")+ HSSFHeader.fontSize((short) 14)+ atr[1] + nadpisExt);// + nadpisExt);
		
		sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);

		sheet.getPrintSetup().setLandscape(true); // na sirku
		
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
		
		// font pro bunky
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		
		//vyska bunek
		sheet.setDefaultRowHeightInPoints((short) 15);
		
		HSSFCellStyle obycBorder = wb.createCellStyle();
		obycBorder.setBorderBottom(CellStyle.BORDER_THIN);
		obycBorder.setBorderLeft(CellStyle.BORDER_THIN);
		obycBorder.setBorderRight(CellStyle.BORDER_THIN);
		obycBorder.setBorderTop(CellStyle.BORDER_THIN);
		obycBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // zarovnat na vysku na stred
		obycBorder.setFont(font);
		
		// styl bunky s dvojitou hranici dole a 12 pismem
		HSSFCellStyle style = wb.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THICK);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // zarovnat na vysku na stred
		style.setFont(font);
		
/*		
		HSSFCellStyle obycBorderGeneralAlign = wb.createCellStyle();
		obycBorderGeneralAlign.setBorderBottom(CellStyle.BORDER_THIN);
		obycBorderGeneralAlign.setBorderLeft(CellStyle.BORDER_THIN);
		obycBorderGeneralAlign.setBorderRight(CellStyle.BORDER_THIN);
		obycBorderGeneralAlign.setBorderTop(CellStyle.BORDER_THIN);
		obycBorderGeneralAlign.setFont(font);
		obycBorderGeneralAlign.setAlignment(CellStyle.ALIGN_GENERAL);
		
		// styl bunky s dvojitou hranici dole a 12 pismem
		HSSFCellStyle styleGeneralAlign = wb.createCellStyle();
		styleGeneralAlign.setBorderBottom(CellStyle.BORDER_THIN);
		styleGeneralAlign.setBorderLeft(CellStyle.BORDER_THIN);
		styleGeneralAlign.setBorderRight(CellStyle.BORDER_THIN);
		styleGeneralAlign.setBorderTop(CellStyle.BORDER_DOUBLE);
		styleGeneralAlign.setFont(font);
		styleGeneralAlign.setAlignment(CellStyle.ALIGN_GENERAL);
*/
		
		//insert data
		this.insertDataPlanovani(model, sheet, obycBorder, style);
		//this.insertDataPlanovani(model, sheet, obycBorder, style, obycBorderGeneralAlign, styleGeneralAlign);
		
		//set First row as header at all printed pages
		sheet.setRepeatingRows(CellRangeAddress.valueOf("1:2"));	
		
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
			JOptionPane.showMessageDialog(hlavniOkno, "Složka se nepodaøilo vytvoøit");
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
	 * Metoda pro zjištìní atributù výpisu. Navrátí String pole o 3 prvcích v poøadí:
	 * <ol>
	 *  <li>Jméno sheetu</li>
	 *  <li>Nadpis v tisku papíru</li>
	 *  <li>Cestu kam uložit (relativní)</li>
	 * </ol>
	 * @param cisloExportu Èíslo exportu, o kterém chceme znát atributy
	 * @return String [] o 3 prvcích nikdy <code>null</code>
	 */
	private String [] getAtributes(){
		String[] atr = { "prazdnyatr1", "prazdnyatr2", "prazdnyatr3" };
		atr[0] = "Licí plán";
		atr[1] = "Licí plán pro týden: ";
		atr[2] = "./lici_plany";
		return atr;
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
	private void insertDataPlanovani(QueryTableModel model, HSSFSheet sheet, HSSFCellStyle obycBorder, HSSFCellStyle stylBunkySDvojitouHranici) throws Exception{
			// ,HSSFCellStyle obycBorderGeneralAlign, HSSFCellStyle stylBunkySDvojitouHraniciGeneralAlign) throws Exception{
		Row row = null;
		Cell cell = null;
		// vyska radky
		int heightInPoints = 26;
		
		int colCount = 0;
		for(int i = 0; i < sirkyBunek[0].length; i++){
			colCount += sirkyBunek[0][i];
		}
		int cisBunky = 0;
		
		// jmena sloupcu už je jinak než normal
		
		for(int j = 0; j < sirkyBunek.length; j++){
			row = sheet.createRow(j);
			row.setHeightInPoints(heightInPoints);
			int sloupec = 0;
			for(int i = 0; i < sirkyBunek[j].length; i++){
				for (int m = 0; m < sirkyBunek[j][i]; m++){
					cell = row.createCell(sloupec);
					if(m == 0)cell.setCellValue(columnNamesPlanLiti[j][i]);
					sloupec++;
					cell.setCellStyle(obycBorder);
					if( j % 2 == 0){
						cell.setCellStyle(stylBunkySDvojitouHranici);
					}
					cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
				}
				sheet.addMergedRegion(new CellRangeAddress(j,j,cisBunky,cisBunky + sirkyBunek[j][i] - 1));
				cisBunky += sirkyBunek[j][i];
			}
			cisBunky = 0;
		}
		
		
		//detect data format
		 // je dan predem
		
		//insert data jinak než normal
		int rowIndex = 0;
		for(int r = 0; r < model.getRowCount(); r++){
			int sloupecModeluData = 0;
			for(int j = 0; j < sirkyBunek.length; j++){
				row = sheet.createRow(rowIndex + 2);
				row.setHeightInPoints(heightInPoints);
				int sloupec = 0;
				cisBunky = 0;
				for(int i = 0; i < sirkyBunek[j].length; i++){
					for(int m = 0; m < sirkyBunek[j][i]; m++){
						cell = row.createCell(sloupec);
						if(m == 0){
							if(dataFormat[j][i] == CISLO){
								if(!model.getValueAt(r, sloupecModeluData).isEmpty()){
									cell.setCellValue(Double.parseDouble(model.getValueAt(r, sloupecModeluData)));
								}
							} else {
								cell.setCellValue(model.getValueAt(r, sloupecModeluData));
							}
							sloupecModeluData++;
						}
						sloupec++;
						//cell.setCellStyle(obycBorderGeneralAlign);¨
						cell.setCellStyle(obycBorder);
						if( j % 2 == 0){
							cell.setCellStyle(stylBunkySDvojitouHranici);
							//cell.setCellStyle(stylBunkySDvojitouHraniciGeneralAlign);
						}
					}
					sheet.addMergedRegion(new CellRangeAddress(rowIndex +2,rowIndex +2,cisBunky,cisBunky + sirkyBunek[j][i] - 1));
					cisBunky += sirkyBunek[j][i];					
				}
				rowIndex++;
			}
		}
		
		//Format data
		
		for(int i = 0; i < colCount ; i++){//mam totiž jeden sloupec navic aby se mi srovnali tabulky viz QuerytableModel
			sheet.autoSizeColumn(i,true);
		}
		
		
		
		
		// overeni že se vše vejde
		// zmerim si delku  použite stranky
		/*
		int paperWidth = 0;
		for(int i = 0; i < colCount ; i++){
			paperWidth += sheet.getColumnWidth(i);
		}
		int widthInChars = paperWidth / 256;
		
		// nastaveni sirky datumu to je vždy stejny
		if (widthInChars > paperSizeInChars){
			sheet.setColumnWidth(terminLitiColumnIndex, widthTerminLiti);
		}
		// mozna nastavim i sirku odlito a odlito - zm, to je vetsinou jen jedno cislo
		
		//if (widthInChars > paperSizeInChars){
		//	sheet.setColumnWidth(objednanoColumnIndex, widthObjednano);
		//}
		
		// nastaveni sirky materialu
		if (widthInChars > paperSizeInChars){
			// v prvnim radku je prvni bunka sloucena do dvou
			// ve druhem radu je vl. material  a pak material
			int rozdilDoOptimalityInChars = widthInChars - paperSizeInChars;
			int width = Math.max(maxWidthMaterial, sheet.getColumnWidth(maxWidtColumnIndex) - rozdilDoOptimalityInChars * 256);
			sheet.setColumnWidth(maxWidtColumnIndex, width);	
		}
		*/
		
		// nastaveni sirky datumu to je vždy stejny
		
	}
}
