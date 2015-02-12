package tiskExcel;

import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class TableToExcel {
	
	public TableToExcel(TableModel model, String path, int cisloVypisu){
		this.export(model, path, cisloVypisu);
	}
	
	/**
	 * Metoda, kde dìje hlavní algorytmus. Zde se vše pøevádí
	 * @param model model, ze kretého èerpáme data
	 * @param path kam chceme uložit .xls soubor (pokud možno relativní)
	 * @param cisloVypis druh vypisu, podle kterého pøizpùsobíme .xls soubor
	 */
	private void export(TableModel model, String path, int cisloVypis){
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = this.createSheet(wb, cisloVypis);
		this.insertData(model, sheet);

	}
	
	/**
	 * Metoda pro vložení dat do tabulky, ještì bych mìl zvážit že pøidam parametr pro mergovaní bunìk.
	 * Ještì musím vymyslet jak. Nìjaky jednoduhcy øešení. :)
	 * @param model ze kterého budeme èíst data
	 * @param sheet do kterého budeme data vkládat
	 */
	private void insertData(TableModel model, Sheet sheet){
		
	}
	
	/**
	 * Metoda pro vytvoøení tøídy Sheet. Udìlí mu jméno podle èísla výpisu.
	 * @param wb do kterého vytvoøí Sheet
	 * @param cisloVypisu èíslo výpisu
	 * @return Sheet s pøidìleným jménem
	 */
	private Sheet createSheet(Workbook wb, int cisloVypisu){
		Sheet sheet = wb.createSheet("new sheet");
		return sheet;
	}

}
