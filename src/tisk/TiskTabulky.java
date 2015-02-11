package tisk;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Meta;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class TiskTabulky {
	private ResultSet rs;
	private Document document;
	private static String outputFilePath = "E:\\databaze-vyborny material\\Pdfzkousky\\Moje2Pdf.pdf";
	
	public static void main(String [] args){
		TiskTabulky tisk = new TiskTabulky(null);
	}
	
	public TiskTabulky(ResultSet rs){
		this.rs = rs;
		try {
			createDocument();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createDocument() throws FileNotFoundException, DocumentException{
		document = new Document(PageSize.A4, 50, 50, 50, 50);
//		ResultSetMetaData meta;
//		int colCount;
//		String [] headers;
//		try {
//			meta = rs.getMetaData();
//			colCount = meta.getColumnCount();
//			headers = new String[colCount];
//		      for (int h = 1; h < colCount; h++) {
//		    	  headers[h] = meta.getColumnName(h);
//		    	  System.out.println(headers[h]);
//		      }
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		
		
		
		
		PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(outputFilePath));
		document.open();
		// Listing 4. Creation of chapter object
		Paragraph title1 = new Paragraph("Chapter 1", FontFactory.getFont(
				FontFactory.HELVETICA, 18, Font.BOLDITALIC, new CMYKColor(0,
						255, 255, 17)));
		Chapter chapter1 = new Chapter(1);
		chapter1.setNumberDepth(0);

		// Listing 5. Creation of section object
		Paragraph title11 = new Paragraph("vypis odlitku",
				FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,
						new CMYKColor(0, 255, 255, 17)));
		Section section1 = chapter1.addSection(title11);
		section1.setNumberDepth(0);
		Paragraph someSectionText = new Paragraph(
				"This text comes as part of section 1 of chapter 1.");
		section1.add(someSectionText);
		someSectionText = new Paragraph("Following is a 3 X 2 table.");
		section1.add(someSectionText);
		
		
		// Listing 6. Creation of table object
		PdfPTable t = new PdfPTable(3);

		t.setSpacingBefore(25);
		t.setSpacingAfter(25);
		PdfPCell c1 = new PdfPCell(new Phrase("Header1"));
		t.addCell(c1);
		PdfPCell c2 = new PdfPCell(new Phrase("Header2"));
		t.addCell(c2);
		PdfPCell c3 = new PdfPCell(new Phrase("Header3"));
		t.addCell(c3);
		PdfPCell c4 = new PdfPCell(new Phrase("Header3"));
		t.addCell(c4);
		t.addCell("1.1");
		t.addCell("1.2");
		t.addCell("1.3");
		t.addCell("1.4");
		section1.add(t);
		
		document.add(chapter1);
		document.close();
		
		System.out.println("konec");
				
	}
}
