package tisk;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.TableModel;

import app.LoginWindow;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

public class JTableToPdf {
	
	public final String result;
	public final String nazevTisku;
	public static final String[][] MOVIES = {
        {
            "Cp1252",
            "A Very long Engagement (France)",
            "directed by Jean-Pierre Jeunet",
            "Un long dimanche de fian\u00e7ailles"
        },
        {
            "Cp1250",
            "No Man's Land (Bosnia-Herzegovina)",
            "Directed by Danis Tanovic",
            "Nikogar\u0161nja zemlja"
        },
        {
            "Cp1251",
            "You I Love (Russia)",
            "directed by Olga Stolpovskaja and Dmitry Troitsky",
            "\u042f \u043b\u044e\u0431\u043b\u044e \u0442\u0435\u0431\u044f"
        },
        {
            "Cp1253",
            "Brides (Greece)",
            "directed by Pantelis Voulgaris",
            "\u039d\u03cd\u03c6\u03b5\u03c2"
        }
    };
	
	private TableModel tableModel;
	/**
	 * 1. genericka
	 * 2. vypis liti
	 */
	private short druhStranky;
	private Document doc;
	
	private String [][] nazvySloupcuTisk = {
		{}
	};
	
	
	public JTableToPdf(TableModel tableModel, short druhStranky){
		nazevTisku = "DruhyStranky"; // na zaklade druhu stranky
		Date dnes = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.format(dnes);
		JTableToPdf.class.getResource("/tisk/"+nazevTisku+sdf.format(dnes));
		result = "E:\\databaze-vyborny material\\Pdfzkousky\\MyFirstTable.pdf";
		this.tableModel = tableModel;
		this.druhStranky = druhStranky;
		
		
	}


	/**
	 * 
	 * @param tableModel
	 * @param adresar
	 * @throws DocumentException
	 */
	public void createPdf(TableModel tableModel, String adresar) throws DocumentException{
		/**
		 * kodovani
		 */
		com.itextpdf.text.Font f = null;
		try {
			BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,
					BaseFont.CP1250, BaseFont.EMBEDDED);
			f = new com.itextpdf.text.Font(bf);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		 // step 1
        Document document = new Document();
        // step 2
        try {
			PdfWriter.getInstance(document, new FileOutputStream(adresar));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World!"));
        // step 5
        document.close();
		
		// the cell object
		PdfPCell cell;
		
	}
	
	private void addMetaData(Document document){
		document.addTitle("Hello World example");
		document.addAuthor("Bruno Lowagie");
		document.addSubject("This example shows how to add metadata");
		document.addKeywords("Metadata, iText, PDF");
		document.addCreator("My program using iText");
	}
	
	
}
