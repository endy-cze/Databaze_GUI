package tiskExcel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
	private final String [][] columnNamesPlanLiti = {
			{"Z�kazn�k", "Jm�no modelu", "��slo modelu", "Po", "�t", "St", "�t", "P�", "Celk.", "Pozn. zak�zka"},
			{"Materi�l", "Mater. 2", "Hmotnost", "Term�n", "Objed.", "Odl � zm.", "Norma", "Norma celk.", "Pozn. model"}
			};
	private final int [][] sirkyBunek = {
			{2,2,2,1,1,  1,1,1,1,1},
			{1,1,1,1,1,  1,3,3,1}
	};
	
	private final static int TEXT = 1;
	private final static int CISLO = 2;
	private final int [][] dataFormat = {
			{TEXT,TEXT,TEXT,CISLO,CISLO,   CISLO,CISLO,CISLO,CISLO, TEXT},
			{TEXT,TEXT,CISLO,TEXT,CISLO,   CISLO, CISLO, CISLO, TEXT}
	};
	
	public static void exportPlanovaniLitiToExcel(MainFrame hlavniOkno, TableModel model,
			String nadpisExt, String name) throws Exception{
		new PlanovaniLitiToExcelTisk(hlavniOkno, (QueryTableModel) model, nadpisExt, name);
	}
	
	private PlanovaniLitiToExcelTisk(MainFrame hlavniOkno, QueryTableModel model,
			String nadpisExt, String name) throws Exception{
		this.hlavniOkno = hlavniOkno;
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
		
		// obycejny styl
		HSSFCellStyle obycBorder = wb.createCellStyle();
		obycBorder.setBorderBottom(CellStyle.BORDER_THIN);
		obycBorder.setBorderLeft(CellStyle.BORDER_THIN);
		obycBorder.setBorderRight(CellStyle.BORDER_THIN);
		obycBorder.setBorderTop(CellStyle.BORDER_THIN);
		obycBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // zarovnat na vysku na stred
		obycBorder.setAlignment(CellStyle.ALIGN_LEFT); // zarovnani doleva (default)
		obycBorder.setFont(font);
		
		// styl bunky s dvojitou hranici dole a 12 pismem
		HSSFCellStyle styleWithThinBorder = wb.createCellStyle();
		styleWithThinBorder.setBorderBottom(CellStyle.BORDER_THIN);
		styleWithThinBorder.setBorderLeft(CellStyle.BORDER_THIN);
		styleWithThinBorder.setBorderRight(CellStyle.BORDER_THIN);
		styleWithThinBorder.setBorderTop(CellStyle.BORDER_THICK);
		styleWithThinBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // zarovnat na vysku na stred
		styleWithThinBorder.setAlignment(CellStyle.ALIGN_LEFT); // zarovnani doleva (default)
		styleWithThinBorder.setFont(font);
		
		// obycejny styl se zarovnanim na stred
		HSSFCellStyle obycBorderCentered = wb.createCellStyle();
		obycBorderCentered.setBorderBottom(CellStyle.BORDER_THIN);
		obycBorderCentered.setBorderLeft(CellStyle.BORDER_THIN);
		obycBorderCentered.setBorderRight(CellStyle.BORDER_THIN);
		obycBorderCentered.setBorderTop(CellStyle.BORDER_THIN);
		obycBorderCentered.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // zarovnat na vysku na stred
		obycBorderCentered.setAlignment(CellStyle.ALIGN_CENTER); // zarovnani doprostred)
		obycBorderCentered.setFont(font);
		
		// styl bunky s dvojitou hranici dole a 12 pismem a se zarovnanim na stred
		HSSFCellStyle styleWithThinBorderCentered = wb.createCellStyle();
		styleWithThinBorderCentered.setBorderBottom(CellStyle.BORDER_THIN);
		styleWithThinBorderCentered.setBorderLeft(CellStyle.BORDER_THIN);
		styleWithThinBorderCentered.setBorderRight(CellStyle.BORDER_THIN);
		styleWithThinBorderCentered.setBorderTop(CellStyle.BORDER_THICK);
		styleWithThinBorderCentered.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // zarovnat na vysku na stred
		styleWithThinBorderCentered.setAlignment(CellStyle.ALIGN_CENTER); // zarovnani doprostred
		styleWithThinBorderCentered.setFont(font);
		
		
		
		//insert data
		this.insertDataPlanovani(model, sheet, obycBorder, styleWithThinBorder, obycBorderCentered, styleWithThinBorderCentered);
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
			JOptionPane.showMessageDialog(hlavniOkno, "Slo�ka se nepoda�ilo vytvo�it");
		}
		
		try {
			FileOutputStream fileOut = new FileOutputStream(atr[2]+"/"+name+".xls");
			wb.write(fileOut);
			wb.close();
			fileOut.close();
			JOptionPane.showMessageDialog(hlavniOkno, "Excel soubor "+atr[2].substring(2, atr[2].length())+"/"+name+".xls vytvo�en.");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(hlavniOkno, "M�te otev�en� soubor, do kter�ho se zapisuje. Zav�ete jej pros�m");
			wb.close();
		}
	}
	
	/**
	 * Metoda pro zji�t�n� atribut� v�pisu. Navr�t� String pole o 3 prvc�ch v po�ad�:
	 * <ol>
	 *  <li>Jm�no sheetu</li>
	 *  <li>Nadpis v tisku pap�ru</li>
	 *  <li>Cestu kam ulo�it (relativn�)</li>
	 * </ol>
	 * @param cisloExportu ��slo exportu, o kter�m chceme zn�t atributy
	 * @return String [] o 3 prvc�ch nikdy <code>null</code>
	 */
	private String [] getAtributes(){
		String[] atr = { "prazdnyatr1", "prazdnyatr2", "prazdnyatr3" };
		atr[0] = "Lic� pl�n";
		atr[1] = "Lic� pl�n pro t�den: ";
		atr[2] = "./lici_plany";
		return atr;
	}
	
	/**
	 * Metoda pro vlo�en� dat do tabulky, je�t� bych m�l zv�it �e p�idam parametr pro mergovan� bun�k.
	 * Je�t� mus�m vymyslet jak. N�jaky jednoduhcy �e�en�. :)
	 * @param model ze kter�ho budeme ��st data
	 * @param sheet prazdny sheet do kter�ho budeme data vkl�dat
	 * @param cisloExportu ��slo exportu
	 * @param font font ktery pou�ijeme v bunkach
	 * @throws Exception
	 */
	private void insertDataPlanovani(QueryTableModel model, HSSFSheet sheet, HSSFCellStyle obycBorder,
			HSSFCellStyle stylBunkySDvojitouHranici, HSSFCellStyle obycBorderCentered,
			HSSFCellStyle stylBunkySDvojitouHraniciCentered) throws Exception{
		Row row = null;
		Cell cell = null;
		// vyska radky
		int heightInPoints = 26;
		
		int colCount = 0;
		for(int i = 0; i < sirkyBunek[0].length; i++){
			colCount += sirkyBunek[0][i];
		}
		int cisBunky = 0;
		
		// jmena sloupcu u� je jinak ne� normal
		
		for(int j = 0; j < sirkyBunek.length; j++){
			row = sheet.createRow(j);
			row.setHeightInPoints(heightInPoints);
			int sloupec = 0;
			for(int i = 0; i < sirkyBunek[j].length; i++){
				for (int m = 0; m < sirkyBunek[j][i]; m++){
					cell = row.createCell(sloupec);
					if(m == 0)cell.setCellValue(columnNamesPlanLiti[j][i]);
					sloupec++;
					
					// nastaveni stylu pro bunku (zatim mam celkem 4)
					if( j % 2 == 0){
						if(isCenteredLeft(columnNamesPlanLiti[j][i])){
							cell.setCellStyle(stylBunkySDvojitouHraniciCentered);
						} else {
							cell.setCellStyle(stylBunkySDvojitouHraniciCentered);
						}
					} else {
						if(isCenteredLeft(columnNamesPlanLiti[j][i])){
							cell.setCellStyle(obycBorderCentered);
						} else {
							cell.setCellStyle(obycBorderCentered);
						}
					}
				}
				sheet.addMergedRegion(new CellRangeAddress(j,j,cisBunky,cisBunky + sirkyBunek[j][i] - 1));
				cisBunky += sirkyBunek[j][i];
			}
			cisBunky = 0;
		}
		
		
		//detect data format
		 // je dan predem
		
		//insert data jinak ne� normal
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
						
						// nastaveni stylu pro bunku (zatim mam celkem 4)
						if( j % 2 == 0){
							if(isCenteredLeft(columnNamesPlanLiti[j][i])){
								cell.setCellStyle(stylBunkySDvojitouHranici);
							} else {
								cell.setCellStyle(stylBunkySDvojitouHraniciCentered);
							}
						} else {
							if(isCenteredLeft(columnNamesPlanLiti[j][i])){
								cell.setCellStyle(obycBorder);
							} else {
								cell.setCellStyle(obycBorderCentered);
							}
						}
					}
					sheet.addMergedRegion(new CellRangeAddress(rowIndex +2,rowIndex +2,cisBunky,cisBunky + sirkyBunek[j][i] - 1));
					cisBunky += sirkyBunek[j][i];					
				}
				rowIndex++;
			}
		}
		
		// zarovnat sloupce podle dat
		
		for(int i = 0; i < colCount ; i++){//mam toti� jeden sloupec navic aby se mi srovnali tabulky viz QuerytableModel
			sheet.autoSizeColumn(i,true);
		}
		
	}
	
	private boolean isCenteredLeft(String columnName){
		for(int i = 0; i < 3; i++){ // prvni tri to jsou
			if(this.columnNamesPlanLiti[0][i].equals(columnName)){
				return true;
			}
		}
		for(int i = 0; i < 2; i++){ // prvni dva to jsou
			if(this.columnNamesPlanLiti[1][i].equals(columnName)){
				return true;
			}
		}
		// poznamka model a poznamka zakazka to jsou taky
		if(columnNamesPlanLiti[0][9].equals(columnName) || columnNamesPlanLiti[1][8].equals(columnName)){
			return true;
		}
		return false;
	}
}
