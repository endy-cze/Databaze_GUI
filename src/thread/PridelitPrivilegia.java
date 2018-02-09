package thread;

import java.io.File;
import java.io.FileInputStream;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import storage.SkladOdkazu;
import app.ProgresBarFrame;
import sablony.errorwin.ExceptionWin;
import sqlstorage.SQLStor;

public class PridelitPrivilegia extends SwingWorker<Integer, Void>{
	public static final int NENIOPRAVNENI = -1;
	public static final int SPATNECISLO = -2;
	public static final int SPATNYFORMAT = -3;
	public static final int ERROR = -4;
	public static final int VPORADKU = 0;
	
	private static final String acesDenied = "Access denied";

	
	private SkladOdkazu sklad;
	private JFrame hlavniOkno;
	private ProgresBarFrame bar;
	private Connection conn;
	private File heslaXMLFile;
	
	public PridelitPrivilegia(JFrame okno, ProgresBarFrame bar, File heslaXMLFile, SkladOdkazu sklad){
		hlavniOkno = okno;
		this.bar  = bar;
		bar.setThreadToCancel(null);
		this.heslaXMLFile = heslaXMLFile;
		this.sklad = sklad;
	}
	
	
	@Override
	protected Integer doInBackground() throws Exception {
		try {
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = null;
	        try {
	        	doc = dBuilder.parse(heslaXMLFile);
	        } catch(SAXException e){
	        	return SPATNYFORMAT;
	        }
	        doc.getDocumentElement().normalize();
	        NodeList nList = doc.getElementsByTagName("uzivatel");
			
	        String jmeno = null, host = null, heslo = null, povolani = null, cisloPovolani = null;
			int cPovolani = -1;
	        SQLStor sqlStor = sklad.getSql();
	        for (int i = 0; i < nList.getLength(); i++) {
	        	Node uzivatel = nList.item(i);
	        	Node tmp = uzivatel.getFirstChild(); // neni ten text ktery chci
	        	tmp = tmp.getNextSibling();
	        	jmeno = tmp.getTextContent();
	        	
	        	tmp = tmp.getNextSibling().getNextSibling();
	        	host = tmp.getTextContent();
	        	
	        	tmp = tmp.getNextSibling().getNextSibling();
	        	heslo = tmp.getTextContent();
	        	
	        	tmp = tmp.getNextSibling().getNextSibling();
	        	povolani = tmp.getTextContent();
	        	
	        	tmp = tmp.getNextSibling().getNextSibling();
	        	cisloPovolani = tmp.getTextContent();
	        	
	        	try {
	        		cPovolani = Integer.parseInt(cisloPovolani);
	        	} catch (NumberFormatException e) {
	    			ExceptionWin.showExceptionMessage(e);
	    			return SPATNECISLO;
	    		}
	        	
	        	sqlStor.pridejUzivateleSPrivilegii(jmeno, host, heslo, cPovolani);
	        	
	        }
			
		} catch (Exception e) {
			if(e.getLocalizedMessage().startsWith(acesDenied)){ // nemame opravneni
				return NENIOPRAVNENI;
			}
			ExceptionWin.showExceptionMessage(e);
			return ERROR;
		}
		return VPORADKU;
	}
	
	 @Override
     public void done() {
		 int i;
		try {
			i = this.get();
			bar.setVisible(false);
			switch(i){
			case VPORADKU:
				JOptionPane.showMessageDialog(hlavniOkno, "Privilegia pøidìlena");
				break;
			case NENIOPRAVNENI:
				JOptionPane.showMessageDialog(hlavniOkno, "Na tuto operaci nemáte oprávnìní");
				break;
			case SPATNYFORMAT:
				JOptionPane.showMessageDialog(hlavniOkno, "Soubor s hesly je špatnì naformátován");
				break;
			case SPATNECISLO:
				JOptionPane.showMessageDialog(hlavniOkno, "Špatnì zapsané èíslo povolání v souboru u uživatele");
				break;
			case ERROR:
				// zobrazi se exception window
				break;
			}
		} catch (Exception e) {
			ExceptionWin.showExceptionMessage(e);
		} 
     }
	

}