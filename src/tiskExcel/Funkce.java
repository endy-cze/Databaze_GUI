package tiskExcel;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class Funkce {

	public static void main(String[] args) {
		try {
			funkce();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void funkce() throws IOException{
		Workbook wb = new HSSFWorkbook();
		// Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("new sheet");

		
		Footer footer = sheet.getFooter();

		footer.setRight("Page " + HeaderFooter.page() + " of "
				+ HeaderFooter.numPages());

		// Set the rows to repeat from row 4 to 5 on the first sheet.
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
		
	    sheet.setPrintGridlines(true);
	    
		sheet.setMargin(Sheet.BottomMargin, 0.5);
		sheet.setMargin(Sheet.TopMargin, 0.5);
		sheet.setMargin(Sheet.LeftMargin, 0.5);
		sheet.setMargin(Sheet.RightMargin, 0.5);
		
		sheet.setMargin(Sheet.HeaderMargin, 0.5);
		sheet.setMargin(Sheet.FooterMargin, 0.5);
		
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("workbook.xls");
		wb.write(fileOut);
		fileOut.close();
	}

}
