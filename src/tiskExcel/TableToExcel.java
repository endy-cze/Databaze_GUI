package tiskExcel;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Footer;
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
	
	private JFrame hlavniOkno;
	
	/**
	 * Vytvoøí tøídu, která vytvoøí .xls soubor do dané složky. Table model a èíslo výpisu spolu souvisí, jelikož
	 * podle èísla výpisu se vybere daná šablona, do které se budou data vkládat.
	 * @param hlavniOkno pouzití na zobrazení chyby (metoda export())
	 * @param model ze kterého èerpáme data
	 * @param name jméno souboru bez koncovky
	 * @param cisloExportu èíslo exportu (èíslo šablony, kterou použijeme pro soubor xls)
	 * @throws Exception vyhodí chybu, pokud nìco nesouhlasí
	 */
	public TableToExcel(JFrame hlavniOkno, QueryTableModel model, String name, int cisloExportu) throws Exception{
		this.hlavniOkno = hlavniOkno;
		this.export(model, name, cisloExportu);
	}
	
	/**
	 * Metoda, kde dìje hlavní algoritmus. Zde se vše pøevádí
	 * @param model model, ze kretého èerpáme data
	 * @param name nazev souboru bez koncovky
	 * @param cisloExportu druh vypisu, podle kterého pøizpùsobíme .xls soubor
	 * @throws Exception 
	 */
	private void export(QueryTableModel model, String name, int cisloExportu) throws Exception{
		HSSFWorkbook wb = new HSSFWorkbook();
		String [] atr = this.getAtributes(cisloExportu);
		HSSFSheet sheet = wb.createSheet(atr[0]);
		
		//number of pages
		Footer footer = sheet.getFooter();
		footer.setRight("Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages());
		//set visibile grid lines on printed pages
		sheet.setPrintGridlines(true);
	    
		sheet.setMargin(Sheet.BottomMargin, 0.5);
		sheet.setMargin(Sheet.TopMargin, 0.5);
		sheet.setMargin(Sheet.LeftMargin, 0.5);
		sheet.setMargin(Sheet.RightMargin, 0.5);
		
		sheet.setMargin(Sheet.HeaderMargin, 0.5);
		sheet.setMargin(Sheet.FooterMargin, 0.5);
		//insert data
		this.insertData(atr[1], model, sheet, cisloExportu);
		//set First row as header at all printed pages
		sheet.setRepeatingRows(CellRangeAddress.valueOf("1:1"));	

	

		
		// Write it into the output to a file
		
		File f = new File("./vypisy");
		try {
			if (f.mkdir()) {
				//System.out.println("Directory Created");
			} else {
				//System.out.println("Directory is not created");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je prázdné");
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
	 * @param sheet prazdny sheet do kterého budeme data vkládat
	 * @param cisloExportu èíslo exportu
	 * @throws Exception
	 */
	private void insertData(String nadpis, QueryTableModel model, HSSFSheet sheet, int cisloExportu) throws Exception{
		Row row = sheet.createRow(0);
		Cell cell = null;
		//insert Column name
		for(int i = 0; i < model.getColumnCount(); i++){
			cell = row.createCell(i);
			cell.setCellValue(model.getColumnName(i));
		}
		//insert data
		for(int i = 1; i < model.getRowCount() + 1; i++){
			row = sheet.createRow(i);
			for(int j = 0; j < model.getColumnCount(); j++){
				cell = row.createCell(j);
				cell.setCellValue(model.getValueAt(i, j));
			}
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
	 * @return String [] o 3 prvcích nebo null pokud neexistuje
	 */
	private String [] getAtributes(int cisloExportu){
		String [] atr = null;
		switch(cisloExportu){
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
