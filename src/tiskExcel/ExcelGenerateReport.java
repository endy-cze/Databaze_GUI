package tiskExcel;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
 
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
public class ExcelGenerateReport {
 
 private CellStyle cs = null;
 private CellStyle csBold = null;
 private CellStyle csTop = null;
 private CellStyle csRight = null;
 private CellStyle csBottom = null;
 private CellStyle csLeft = null;
 private CellStyle csTopLeft = null;
 private CellStyle csTopRight = null;
 private CellStyle csBottomLeft = null;
 private CellStyle csBottomRight = null;
 
 public static void main(String[] args) {
 
  ExcelGenerateReport myReport = new ExcelGenerateReport();
  myReport.createExcel();
 
 }
 
 public void createExcel() {
 
  try{
 
   Workbook wb = new XSSFWorkbook();
   Sheet sheet = wb.createSheet("My Excel Report");
 
   //Setup some styles that we need for the Cells
   setCellStyles(wb);
 
   //Get current Date and Time
   Date date = new Date(System.currentTimeMillis());
   DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
 
   //Set Column Widths
   sheet.setColumnWidth(0, 2500);
   sheet.setColumnWidth(1, 2500);
   sheet.setColumnWidth(2, 6000);
   sheet.setColumnWidth(3, 10000);
   sheet.setColumnWidth(4, 3000);
 
   //Setup the Page margins - Left, Right, Top and Bottom
   sheet.setMargin(Sheet.LeftMargin, 0.25);
   sheet.setMargin(Sheet.RightMargin, 0.25);
   sheet.setMargin(Sheet.TopMargin, 0.75);
   sheet.setMargin(Sheet.BottomMargin, 0.75);
 
   //Setup the Header and Footer Margins
   sheet.setMargin(Sheet.HeaderMargin, 0.25);
   sheet.setMargin(Sheet.FooterMargin, 0.25);
    
   //If you are using HSSFWorkbook() instead of XSSFWorkbook()
   //HSSFPrintSetup ps = (HSSFPrintSetup) sheet.getPrintSetup();
   //ps.setHeaderMargin((double) .25);
   //ps.setFooterMargin((double) .25);
 
   //Set Header Information
   Header header = sheet.getHeader();
   header.setLeft("*** ORIGINAL COPY ***");
   header.setCenter(HSSFHeader.font("Arial", "Bold") +
     HSSFHeader.fontSize((short) 14) + "SAMPLE ORDER");
   header.setRight(df.format(date));
 
   //Set Footer Information with Page Numbers
   Footer footer = sheet.getFooter();
   footer.setRight( "Page " + HeaderFooter.page() + " of " +
     HeaderFooter.numPages() );
 
 
   int rowIndex = 0;
   rowIndex = insertHeaderInfo(sheet, rowIndex);
   rowIndex = insertDetailInfo(sheet, rowIndex);
 
   rowIndex = 47 * 1;
   rowIndex = insertHeaderInfo(sheet, rowIndex);
   rowIndex = insertDetailInfo(sheet, rowIndex);
 
   rowIndex = 47 * 2;
   rowIndex = insertHeaderInfo(sheet, rowIndex);
   rowIndex = insertDetailInfo(sheet, rowIndex);
 
   //Write the Excel file
   FileOutputStream fileOut = null;
   fileOut = new FileOutputStream("data/myReport.xlsx");
   wb.write(fileOut);
   fileOut.close();
 
  }
  catch (Exception e) {
   System.out.println(e);
  }
 
 }
 
 private void setCellStyles(Workbook wb) {
 
  //font size 10
  Font f = wb.createFont();
  f.setFontHeightInPoints((short) 10);
 
  //Simple style
  cs = wb.createCellStyle();
  cs.setFont(f);
 
  //Bold Fond
  Font bold = wb.createFont();
  bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
  bold.setFontHeightInPoints((short) 10);
 
  //Bold style
  csBold = wb.createCellStyle();
  csBold.setBorderBottom(CellStyle.BORDER_THIN);
  csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
  csBold.setFont(bold);
 
  //Setup style for Top Border Line
  csTop = wb.createCellStyle();
  csTop.setBorderTop(CellStyle.BORDER_THIN);
  csTop.setTopBorderColor(IndexedColors.BLACK.getIndex());
  csTop.setFont(f);
 
  //Setup style for Right Border Line
  csRight = wb.createCellStyle();
  csRight.setBorderRight(CellStyle.BORDER_THIN);
  csRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
  csRight.setFont(f);
 
  //Setup style for Bottom Border Line
  csBottom = wb.createCellStyle();
  csBottom.setBorderBottom(CellStyle.BORDER_THIN);
  csBottom.setBottomBorderColor(IndexedColors.BLACK.getIndex());
  csBottom.setFont(f);
 
  //Setup style for Left Border Line
  csLeft = wb.createCellStyle();
  csLeft.setBorderLeft(CellStyle.BORDER_THIN);
  csLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex());
  csLeft.setFont(f);
 
  //Setup style for Top/Left corner cell Border Lines
  csTopLeft = wb.createCellStyle();
  csTopLeft.setBorderTop(CellStyle.BORDER_THIN);
  csTopLeft.setTopBorderColor(IndexedColors.BLACK.getIndex());
  csTopLeft.setBorderLeft(CellStyle.BORDER_THIN);
  csTopLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex());
  csTopLeft.setFont(f);
 
  //Setup style for Top/Right corner cell Border Lines
  csTopRight = wb.createCellStyle();
  csTopRight.setBorderTop(CellStyle.BORDER_THIN);
  csTopRight.setTopBorderColor(IndexedColors.BLACK.getIndex());
  csTopRight.setBorderRight(CellStyle.BORDER_THIN);
  csTopRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
  csTopRight.setFont(f);
 
  //Setup style for Bottom/Left corner cell Border Lines
  csBottomLeft = wb.createCellStyle();
  csBottomLeft.setBorderBottom(CellStyle.BORDER_THIN);
  csBottomLeft.setBottomBorderColor(IndexedColors.BLACK.getIndex());
  csBottomLeft.setBorderLeft(CellStyle.BORDER_THIN);
  csBottomLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex());
  csBottomLeft.setFont(f);
 
  //Setup style for Bottom/Right corner cell Border Lines
  csBottomRight = wb.createCellStyle();
  csBottomRight.setBorderBottom(CellStyle.BORDER_THIN);
  csBottomRight.setBottomBorderColor(IndexedColors.BLACK.getIndex());
  csBottomRight.setBorderRight(CellStyle.BORDER_THIN);
  csBottomRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
  csBottomRight.setFont(f);
 
 }
 
 private int insertHeaderInfo(Sheet sheet, int index){
 
  int rowIndex = index;
  Row row = null;
  Cell c = null;
 
  rowIndex++;
  row = sheet.createRow(rowIndex);
  c = row.createCell(0);
  c.setCellValue("Customer No:");
  c.setCellStyle(csTopLeft);
  c = row.createCell(1);
  c.setCellStyle(csTop);
  c = row.createCell(2);
  c.setCellValue("ABC");
  c.setCellStyle(csTopRight);
 
  rowIndex++;
  row = sheet.createRow(rowIndex);
  c = row.createCell(0);
  c.setCellValue("Order No:");
  c.setCellStyle(csLeft);
  c = row.createCell(2);
  c.setCellValue("123456");
  c.setCellStyle(csRight);
 
  rowIndex++;
  row = sheet.createRow(rowIndex);
  c = row.createCell(0);
  c.setCellStyle(csLeft);
  c = row.createCell(2);
  c.setCellStyle(csRight);
 
  rowIndex++;
  row = sheet.createRow(rowIndex);
  c = row.createCell(0);
  c.setCellValue("Name:");
  c.setCellStyle(csLeft);
  c = row.createCell(2);
  c.setCellValue("ABC Customer");
  c.setCellStyle(csRight);
 
  rowIndex++;
  row = sheet.createRow(rowIndex);
  c = row.createCell(0);
  c.setCellValue("Address:");
  c.setCellStyle(csLeft);
  c = row.createCell(2);
  c.setCellValue("123 Street No.");
  c.setCellStyle(csRight);
 
  rowIndex++;
  row = sheet.createRow(rowIndex);
  c = row.createCell(0);
  c.setCellStyle(csLeft);
  c = row.createCell(2);
  c.setCellValue("Unknown City, State ZIPCODE");
  c.setCellStyle(csRight);
 
  rowIndex++;
  row = sheet.createRow(rowIndex);
  c = row.createCell(0);
  c.setCellStyle(csBottomLeft);
  c = row.createCell(1);
  c.setCellStyle(csBottom);
  c = row.createCell(2);
  c.setCellValue("U.S.A.");
  c.setCellStyle(csBottomRight);
 
  rowIndex = rowIndex + 3;
  row = sheet.createRow(rowIndex);
  c = row.createCell(0);
  c.setCellValue("Line No");
  c.setCellStyle(csBold);
  c = row.createCell(1);
  c.setCellValue("Quantity");
  c.setCellStyle(csBold);
  c = row.createCell(2);
  c.setCellValue("Item No");
  c.setCellStyle(csBold);
  c = row.createCell(3);
  c.setCellValue("Description");
  c.setCellStyle(csBold);
  c = row.createCell(4);
  c.setCellValue("Price");
  c.setCellStyle(csBold);
 
  return rowIndex;
 
 }
 
 private int insertDetailInfo(Sheet sheet, int index){
 
  int rowIndex = 0;
  Row row = null;
  Cell c = null;
 
  for(int i = 1; i < 35; i++){
    
   rowIndex = index + i;
   row = sheet.createRow(rowIndex);
   c = row.createCell(0);
   c.setCellValue(i);
   c.setCellStyle(cs);
   c = row.createCell(1);
   c.setCellValue(10 + i);
   c.setCellStyle(cs);
   c = row.createCell(2);
   c.setCellValue("ITEM" + i);
   c.setCellStyle(cs);
   c = row.createCell(3);
   c.setCellValue("My ITEM" + i + " Decscription");
   c.setCellStyle(cs);
   c = row.createCell(4);
   c.setCellValue(1.11 * i);
   c.setCellStyle(cs);
 
  }
 
  return rowIndex;
 
 }
}