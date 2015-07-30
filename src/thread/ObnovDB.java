package thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import storage.SkladOdkazu;
import app.ProgresBarFrame;
import sablony.errorwin.ExceptionWin;

public class ObnovDB extends SwingWorker<Boolean, Void>{
	public static final int ZADNYRESULTSET = -1;
	public static final int SPATNYACTIONTYPE = -2;
	public static final int SLOZNANEVYTVORENA = -3;
	public static final int ERROR = -4;
	public static final int ZALOHAOK = 0;
	
	private SkladOdkazu sklad;
	private JFrame hlavniOkno;
	private JFrame bar;
	private Connection conn;
	private File obnovDBSqlFile;
	
	public ObnovDB(JFrame okno, JFrame bar, Connection conn, File obnovDBSqlFile){
		hlavniOkno = okno;
		this.bar  = bar;
		this.conn = conn;
		this.obnovDBSqlFile = obnovDBSqlFile;
	}
	
	
	@Override
	protected Boolean doInBackground() throws Exception {
		try {
			ScriptRunner sr = new ScriptRunner(conn, false, true);
			sr.runScript(new InputStreamReader(new FileInputStream(obnovDBSqlFile), "UTF-8"));
		} catch (Exception e) {
			ExceptionWin.showExceptionMessage(e);
			return false;
		}
		return true;
	}
	
	 @Override
     public void done() {
		 boolean i;
		try {
			i = this.get();
			bar.setVisible(false);
			
			if(i){
				JOptionPane.showMessageDialog(hlavniOkno, "Obnova se podaøila. Vypnìtì prosím aplikaci a pøihlašte se.");
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Obnova se nepodaøila. Asi nemáte pravomoce nebo špatny soubor.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ExceptionWin.showExceptionMessage(e);
		} 
     }
	

}