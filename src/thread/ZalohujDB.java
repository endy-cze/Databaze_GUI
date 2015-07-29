package thread;

import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import sqlstorage.SQLStor;
import storage.SkladOdkazu;
import app.MainFrame;
import app.ProgresBarFrame;
import sablony.errorwin.ExceptionWin;

import com.opencsv.CSVWriter;

public class ZalohujDB extends SwingWorker<Integer, Void>{
	
	public static final int ZALOHUJ = 1;
	public static final int OBNOV = 2;
	
	public static final int ZADNYRESULTSET = -1;
	public static final int SPATNYACTIONTYPE = -2;
	public static final int SLOZNANEVYTVORENA = -3;
	public static final int ERROR = -4;
	public static final int ZALOHAOK = 0;
	
	private final int actionType;
	private SkladOdkazu sklad;
	private MainFrame hlavniOkno;
	private ProgresBarFrame bar;
	
	public ZalohujDB(SkladOdkazu sklad,int actionType){
		this.actionType = actionType;
		this.sklad = sklad;
		hlavniOkno = sklad.getHlavniOkno();
		bar = sklad.getBar();
		bar.setThreadToCancel(this);
	}
	
	
	@Override
	protected Integer doInBackground() throws Exception {
		Statement stm = null;
		SQLStor sqlStor = sklad.getSql();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		Date dnes = new Date();
		CSVWriter writer = null;
		try {
			if (actionType == ZALOHUJ) {
				stm = sqlStor.zalohaDB();
			}

			if (stm == null) {
				return SPATNYACTIONTYPE;
			}

			do {
				ResultSet rs = stm.getResultSet();
				if (rs == null) {
					return ZADNYRESULTSET;
				}
				// create folder
				String folder = "./zaloha_databaze";
				File f = new File(folder);
				try {
					if (f.mkdir()) {
						// System.out.println("Directory Created");
					} else {
						// System.out.println("Directory is not created");
					}
				} catch (Exception e) {
					return SLOZNANEVYTVORENA;
				}
				ResultSetMetaData meta = rs.getMetaData();

				writer = new CSVWriter(
						new FileWriter(folder + "/" + sdf.format(dnes) + "_" + meta.getTableName(1) + ".csv"), ',',
						CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

				// write data
				// feed in your array (or convert your data to an array)
				String[] data;
				int colCount = meta.getColumnCount();
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
			} while (stm.getMoreResults());
		} catch (Exception e) {
			ExceptionWin.showExceptionMessage(e);
			if(writer != null){
				writer.close();
			}
			return ERROR;
		}
		return ZALOHAOK;
	}
	
	 @Override
     public void done() {
		 int i;
		try {
			i = this.get();
			System.out.println(i);
			bar.setVisible(false);
			
			if(i == ZalohujDB.ZALOHAOK){
				JOptionPane.showMessageDialog(hlavniOkno, "Záloha vytvoøena. Nezapomeòte obèas složku 'zaloha_databaze' promazat!!! (myslím starší zálohy)");
			} else if(i == ZalohujDB.SPATNYACTIONTYPE){
				JOptionPane.showMessageDialog(hlavniOkno, "Špatny action type");
			} else if(i == ZalohujDB.ZADNYRESULTSET){
				JOptionPane.showMessageDialog(hlavniOkno, "Prazdny resultSet");
			} else if(i == ZalohujDB.SLOZNANEVYTVORENA){
				JOptionPane.showMessageDialog(hlavniOkno, "Složka nevytvoøena");
			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ExceptionWin.showExceptionMessage(e);
		} 
     }
	

}
