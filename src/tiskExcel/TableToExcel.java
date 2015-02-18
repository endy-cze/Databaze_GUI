package tiskExcel;

import java.io.File;
import java.io.FileOutputStream;

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
	
	/**
	 * Vytvoøí tøídu, která vytvoøí .xls soubor do dané složky. Table model a èíslo výpisu spolu souvisí, jelikož
	 * podle èísla výpisu se vybere daná šablona, do které se budou data vkládat.
	 * @param model ze kterého èerpáme data
	 * @param name jméno souboru bez koncovky
	 * @param cisloVypisu èíslo výpisu (èíslo šablony, kterou použijeme pro soubor xls)
	 * @throws Exception vyhodí chybu, pokud nìco nesouhlasí
	 */
	public TableToExcel(TableModel model, String name, int cisloVypisu) throws Exception{
		this.export(model, name, cisloVypisu);
	}
	
	/**
	 * Metoda, kde dìje hlavní algoritmus. Zde se vše pøevádí
	 * @param model model, ze kretého èerpáme data
	 * @param name nazev souboru bez koncovky
	 * @param cisloVypis druh vypisu, podle kterého pøizpùsobíme .xls soubor
	 * @throws Exception 
	 */
	private void export(TableModel model, String name, int cisloVypis) throws Exception{
		HSSFWorkbook wb = new HSSFWorkbook();
		String [] atr = this.getAtributes(cisloVypis);
		HSSFSheet sheet = wb.createSheet(atr[0]);
		
		
		Footer footer = sheet.getFooter();
		footer.setRight("Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages());
		
		sheet.setPrintGridlines(true);
	    
		sheet.setMargin(Sheet.BottomMargin, 0.5);
		sheet.setMargin(Sheet.TopMargin, 0.5);
		sheet.setMargin(Sheet.LeftMargin, 0.5);
		sheet.setMargin(Sheet.RightMargin, 0.5);
		
		sheet.setMargin(Sheet.HeaderMargin, 0.5);
		sheet.setMargin(Sheet.FooterMargin, 0.5);
		
		sheet.setRepeatingRows(CellRangeAddress.valueOf("1:1"));
		
		
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
		
		
		this.insertData(atr[1], model, sheet, cisloVypis);
		
	

		
		// Write it into the output to a file
		
		File f = new File("./vypisy");
		try {
			if (f.mkdir()) {
				//System.out.println("Directory Created");
			} else {
				//System.out.println("Directory is not created");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		FileOutputStream fileOut = new FileOutputStream("./vypisy/"+name+".xls");
		wb.write(fileOut);
		wb.close();
		fileOut.close();
	}
	
	/**
	 * Metoda pro vložení dat do tabulky, ještì bych mìl zvážit že pøidam parametr pro mergovaní bunìk.
	 * Ještì musím vymyslet jak. Nìjaky jednoduhcy øešení. :)
	 * @param nadpis napis v tisku
	 * @param model ze kterého budeme èíst data
	 * @param sheet do kterého budeme data vkládat
	 * @param cisloVypis èíslo vypisu
	 * @throws Exception
	 */
	private void insertData(String nadpis, TableModel model, HSSFSheet sheet, int cisloVypis) throws Exception{
		HSSFRow fRow = (HSSFRow) sheet.getRow(sheet.getFirstRowNum());
		int colCount = 0;
		for(Cell cell : fRow){
			colCount++;
		}
		System.out.println("celkem bunek: "+colCount);
		colCount = sheet.getRow(0).getPhysicalNumberOfCells();
		System.out.println("celkem bunek: "+colCount);
		colCount = sheet.getRow(0).getLastCellNum();
		System.out.println("celkem bunek: "+colCount);
		if(model.getColumnCount() != colCount){
			throw new Exception("Spatny pocet sloupcu v modelu nebo šablonì");
		}
	}
	
	/**
	 * Metoda pro zjištìní atributù výpisu. Navrátí String pole o 3 prvcích v poøadí:
	 * <ol>
	 *  <li>Jméno sheetu</li>
	 *  <li>Nadpis v tisku papíru</li>
	 *  <li>Cestu kam uložit (relativní)</li>
	 * </ol>
	 * @param cisloVypisu Èíslo výpisu, o kterém chceme znát atributy
	 * @return String [] o 3 prvcích nebo null pokud neexistuje
	 */
	private String [] getAtributes(int cisloVypisu){
		String [] atr = null;
		switch(cisloVypisu){
		case 1:
			String [] atr1 = {"Stav neuzavøených zakázek", "Stav neuzavøených zakázek", ".//"};
			atr = atr1;
			break;
		case 2:
			break;
		}
		return atr;		
	}
}
