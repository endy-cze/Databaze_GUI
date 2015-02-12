package tiskExcel;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class Funkce {

	/**
	 * na stranku se vejde 8 bunìk o šíøce 2500 pøí základním veliksti písma
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//funkce();
			merge();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
     * Font metric for the "0" zero character is 556/1000 x 10 POINTS = 5.56
POINTS per CHARACTER. 
     * 5.56 POINTS 		= 	1 CHARACTER.
     * 256 WIDTH UNITS 	= 	1 CHARACTER
     * 256 WIDTH UNITS 	= 	5.56 points
     * 1 WIDTH UNITS 	= 	1 / 256 CHARACTER
     *               	= 	5.56 / 256 POINTS
     *               	= 	0.0217 POINTS
     *               	= 	0.0217 / 72 INCHES
     *               	= 	0.0003 INCHES
     *               
     */
	public static void merge() throws IOException{
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("new sheet");
		sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
		
		sheet.setDisplayGridlines(true);
		sheet.setPrintGridlines(true);

		Row row = sheet.createRow((short) 1);
		Cell cell = row.createCell((short) 1);
		cell.setCellValue("This is a test of merging");
		
		// je to vicemene (x,y) odkud kam (x,y)
		CellRangeAddress sloucit = new CellRangeAddress(1,1,1,2);
		
		sheet.addMergedRegion(new CellRangeAddress(1, // first row (0-based)
				1, // last row (0-based)
				1, // first column (0-based)
				2 // last column (0-based)
		));
		
		row = sheet.createRow(0);
		for(int i = 0; i < 10; i++){
			cell = row.createCell(i);
			cell.setCellValue("Ahooj");
			sheet.setColumnWidth(i, 2500);
		}
		
		System.out.println(sheet.getColumnWidth(0));
		System.out.println(sheet.getColumnWidth(1));
		
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("workbook.xls");
		wb.write(fileOut);
		fileOut.close();
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
		
		// merging
		row = sheet.createRow((short) 1);
		cell = row.createCell((short) 1);
		cell.setCellValue("This is a test of merging");

		sheet.addMergedRegion(new CellRangeAddress(1, // first row (0-based)
				1, // last row (0-based)
				1, // first column (0-based)
				2 // last column (0-based)
		));
		
		
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("workbook.xls");
		wb.write(fileOut);
		fileOut.close();
	}

}
