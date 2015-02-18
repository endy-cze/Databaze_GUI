package tiskExcel;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class TableToExcel {
	
	/**
	 * Vytvoøí tøídu, která vytvoøí .xls soubor do dané složky. Table model a èíslo výpisu spolu souvisí, jelikož
	 * podle èísla výpisu se vybere daná šablona, do které se budou data vkládat.
	 * @param model ze kterého èerpáme data
	 * @param path kam chceme uložit soubor
	 * @param cisloVypisu èíslo výpisu (èíslo šablony, kterou použijeme pro soubor xls)
	 * @throws Exception vyhodí chybu, pokud nìco nesouhlasí
	 */
	public TableToExcel(TableModel model, String path, int cisloVypisu) throws Exception{
		this.export(model, path, cisloVypisu);
	}
	
	/**
	 * Metoda, kde dìje hlavní algorytmus. Zde se vše pøevádí
	 * @param model model, ze kretého èerpáme data
	 * @param path kam chceme uložit .xls soubor (pokud možno relativní)
	 * @param cisloVypis druh vypisu, podle kterého pøizpùsobíme .xls soubor
	 * @throws Exception 
	 */
	private void export(TableModel model, String path, int cisloVypis) throws Exception{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = this.createSheet(wb, cisloVypis);
		
		Footer footer = sheet.getFooter();
		footer.setRight("Page " + HeaderFooter.page() + " of "
				+ HeaderFooter.numPages());
		
		sheet.setPrintGridlines(true);
	    
		sheet.setMargin(Sheet.BottomMargin, 0.5);
		sheet.setMargin(Sheet.TopMargin, 0.5);
		sheet.setMargin(Sheet.LeftMargin, 0.5);
		sheet.setMargin(Sheet.RightMargin, 0.5);
		
		sheet.setMargin(Sheet.HeaderMargin, 0.5);
		sheet.setMargin(Sheet.FooterMargin, 0.5);
		
		sheet.setRepeatingRows(CellRangeAddress.valueOf("1:1"));
		
		
		this.insertData(model, sheet, cisloVypis);
		
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("Nadpis1");
		cell = row.createCell(1);
		cell.setCellValue("Nadpis2");
		for(int i = 1; i < 100; i++){
			row = sheet.createRow(i);
			cell = row.createCell(0);
			cell.setCellValue("Skoda smrdí "+i);
		}
		

		
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(path);
		wb.write(fileOut);
		fileOut.close();
	}
	
	/**
	 * Metoda pro vložení dat do tabulky, ještì bych mìl zvážit že pøidam parametr pro mergovaní bunìk.
	 * Ještì musím vymyslet jak. Nìjaky jednoduhcy øešení. :)
	 * @param model ze kterého budeme èíst data
	 * @param sheet do kterého budeme data vkládat
	 * @param cisloVypis èíslo vypisu
	 * @throws Exception 
	 */
	private void insertData(TableModel model, HSSFSheet sheet, int cisloVypis) throws Exception{
		HSSFRow fRow = (HSSFRow) sheet.getRow(sheet.getFirstRowNum());
		int colCount = 0;
		for(Cell cell : fRow){
			colCount++;
		}
		System.out.println("celkem bunek: "+colCount);
		if(model.getColumnCount() != colCount){
			throw new Exception("Spatny pocet sloupcu v modelu nebo šablonì");
		}
	}
	
	/**
	 * Metoda pro vytvoøení tøídy Sheet. Udìlí mu jméno podle èísla výpisu.
	 * @param wb do kterého vytvoøí Sheet
	 * @param cisloVypisu èíslo výpisu
	 * @return Sheet s pøidìleným jménem
	 */
	private HSSFSheet createSheet(HSSFWorkbook wb, int cisloVypisu){
		HSSFSheet sheet = wb.createSheet("new sheet");
		return sheet;
	}

}
