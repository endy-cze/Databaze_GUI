package tiskExcel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

import com.opencsv.CSVWriter;

import app.MainFrame;
import sqlstorage.SQLStor;

public class VypisStavZakazekToCSV {
	
	public static boolean vypisToCSV(MainFrame hlavniOkno, String jmenoZakaznika, String cisloModelu, String nazevModelu, int idModelu, Date datumZakazky) throws SQLException, IOException{
		SQLStor sql = hlavniOkno.getSklad().getSql();
		//SimpleDateFormat sdf = hlavniOkno.getSklad().getSdf2();
		
		ResultSet rs = sql.vypisStavuZakazek(jmenoZakaznika, cisloModelu, nazevModelu, idModelu, datumZakazky);
		CSVWriter writer = null;
		
		
		// create folder
		String folder = "./vypisy";
		File f = new File(folder);
		if (f.mkdir()) {
			// System.out.println("Directory Created");
		} else {
			// System.out.println("Directory is not created");
		}
		ResultSetMetaData meta = rs.getMetaData();
		
		try{
		writer = new CSVWriter(
				new FileWriter(folder + "/15. VypisStavuZakazek.csv"), ',',
				CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
		} catch(FileNotFoundException e){
			if(e.getMessage().contains("Proces nemá pøístup k souboru")){
				JOptionPane.showMessageDialog(hlavniOkno, "Zavøete prosím soubor 15. VypisStavuZakazek.csv");
				return false;
			} else {
				throw e;
			}
			
		}
		// write data
		//writer.writeAll(rs, true);
		
		// feed in your array (or convert your data to an array)
		
		String[] data;
		int colCount = meta.getColumnCount();
		// nazev sloupcu
		data = new String[colCount];
		for(int i = 0; i < data.length; i++){
			data[i] = meta.getColumnLabel(i+1);
		}
		writer.writeNext(data, true);
		writer.flush();
		// data
		while (rs.next()) {
			data = new String[colCount];
			for (int i = 0; i < colCount; i++) {
				data[i] = rs.getString(i + 1);
				if(data[i] == null)data[i] = "\\N";
				data[i] = data[i].replaceAll("[\\n\\r]+", "\\\\n");	// escapovani novych radku a tabulatoru
				data[i] = data[i].replaceAll("[\\t]+", "\\\\t");
			}
			writer.writeNext(data, true);
			writer.flush();
		}
		writer.close();
		JOptionPane.showMessageDialog(hlavniOkno, "Soubor s výpisem stavu zakázek byl vytvoøen");
		return true;
	}

}
