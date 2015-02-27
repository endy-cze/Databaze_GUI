package thread;

import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import sablony.errorwin.ExceptionWin;
import sqlstorage.SQLStor;
import storage.SkladOdkazu;
import app.MainFrame;
import app.ProgresBarFrame;

import com.opencsv.CSVWriter;

public class ZalohujObnovDB extends SwingWorker<Integer, Void>{
	
	public static final int ZALOHUJ = 1;
	public static final int OBNOV = 2;
	
	public static final int ZADNYRESULTSET = -1;
	public static final int SPATNYACTIONTYPE = -2;
	public static final int SLOZNANEVYTVORENA = -3;
	public static final int ZALOHAOK = 0;
	
	private final int actionType;
	private SkladOdkazu sklad;
	private MainFrame hlavniOkno;
	private ProgresBarFrame bar;
	
	public ZalohujObnovDB(SkladOdkazu sklad,int actionType){
		this.actionType = actionType;
		this.sklad = sklad;
		hlavniOkno = sklad.getHlavniOkno();
		bar = sklad.getBar();
	}
	
	
	@Override
	protected Integer doInBackground() throws Exception {
		Statement stm = null;
		SQLStor sqlStor = sklad.getSql();
		SimpleDateFormat sdf = sklad.getSdf2();
		Date dnes = new Date();
		if(actionType == ZALOHUJ){
			stm = sqlStor.zalohaDB();
		}
		
		if(stm == null){
			return SPATNYACTIONTYPE;
		}
		
		do {
			ResultSet rs = stm.getResultSet();
			if (rs == null) {
				return ZADNYRESULTSET;
			}
			//create folder
			String folder = "./zaloha_databaze";
			File f = new File(folder);
			try {
				if (f.mkdir()) {
					//System.out.println("Directory Created");
				} else {
					//System.out.println("Directory is not created");
				}
			} catch (Exception e) {
				return SLOZNANEVYTVORENA;
			}
			ResultSetMetaData meta = rs.getMetaData();
			
			CSVWriter writer = new CSVWriter(new FileWriter(folder+"/"+sdf.format(dnes)+"_"+
			meta.getTableName(1)+".csv"),',',CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			// feed in your array (or convert your data to an array)
			writer.writeAll(rs, true);
			writer.close();
		} while (stm.getMoreResults());
		return ZALOHAOK;
	}
	
	 @Override
     public void done() {
		 int i;
		try {
			i = this.get();
			System.out.println(i);
			bar.setVisible(false);
			
			if(i == ZalohujObnovDB.ZALOHAOK){
				JOptionPane.showMessageDialog(hlavniOkno, "Zaloha vytvoøena");
			} else if(i == ZalohujObnovDB.SPATNYACTIONTYPE){
				JOptionPane.showMessageDialog(hlavniOkno, "Špatny action type");
			} else if(i == ZalohujObnovDB.ZADNYRESULTSET){
				JOptionPane.showMessageDialog(hlavniOkno, "Prazdny resultSet");
			} else if(i == ZalohujObnovDB.SLOZNANEVYTVORENA){
				JOptionPane.showMessageDialog(hlavniOkno, "Složka nevytvoøena");
			}		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
	

}
